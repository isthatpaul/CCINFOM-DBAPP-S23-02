package Services;

import Model.Bill;
import Model.BillCRUD;
import Model.Consumption;
import Model.Rate;
import Model.OverdueNotice;
import Model.OverdueNoticeCRUD;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

public class BillService {

    private final BillCRUD billCRUD = new BillCRUD();
    private final OverdueNoticeCRUD overdueNoticeCRUD = new OverdueNoticeCRUD();

    // Generate a new bill with business rule checks
    public boolean generateBill(Consumption consumption, Rate rate, int customerID, int staffID, Integer technicianID, int dueDays) {
        // Prevent duplicate bill for same customer, utility type, and billing period
        List<Bill> bills = billCRUD.getAllRecords();
        for (Bill b : bills) {
            if (b.customerID() == customerID && b.rateID() == rate.rateID()) {
                Date billDate = b.dueDate();
                Date consumptionDate = consumption.readingDate();

                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(billDate);
                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(consumptionDate);

                if (cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
                        && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) {
                    System.err.println("Bill already exists for this customer, utility, and billing period.");
                    return false;
                }
            }
        }

        // Compute amount due
        double amountDue = consumption.consumptionValue() * rate.ratePerUnit();

        // Calculate due date
        Calendar cal = Calendar.getInstance();
        cal.setTime(consumption.readingDate());
        cal.add(Calendar.DAY_OF_MONTH, dueDays);
        Date dueDate = new Date(cal.getTimeInMillis());

        // Create Bill object
        Bill bill = new Bill(
                0,
                customerID,
                consumption.consumptionID(),
                rate.rateID(),
                amountDue,
                dueDate,
                "UNPAID",
                staffID,
                technicianID
        );

        return billCRUD.addRecord(bill);
    }

    // Convenience method with default 15-day due period and no technician
    public boolean generateBill(Consumption consumption, Rate rate, int customerID, int staffID) {
        return generateBill(consumption, rate, customerID, staffID, null, 15);
    }

    // Apply penalties to overdue bills
    public void applyPenalty(double penaltyRate) {
        List<Bill> bills = billCRUD.getAllRecords();
        Date today = new Date(System.currentTimeMillis());

        for (Bill bill : bills) {
            if (bill.status().equalsIgnoreCase("UNPAID") || bill.status().equalsIgnoreCase("PARTIALLY_PAID")) {
                if (today.after(bill.dueDate())) {
                    double penalty = bill.amountDue() * penaltyRate;
                    double newAmountDue = bill.amountDue() + penalty;

                    // Update bill
                    Bill updatedBill = new Bill(
                            bill.billID(),
                            bill.customerID(),
                            bill.consumptionID(),
                            bill.rateID(),
                            newAmountDue,
                            bill.dueDate(),
                            "OVERDUE",
                            bill.generatedByStaffID(),
                            bill.technicianID()
                    );
                    billCRUD.updateRecord(updatedBill);

                    // Create overdue notice
                    OverdueNotice notice = new OverdueNotice(
                            0,
                            bill.billID(),
                            today,
                            penalty,
                            today,
                            "SENT",
                            bill.generatedByStaffID()
                    );
                    overdueNoticeCRUD.addRecord(notice);
                }
            }
        }
    }

    // Update bill status after payment
    public boolean updateBillAfterPayment(int billID, double amountPaid) {
        Bill bill = billCRUD.getRecordById(billID);
        if (bill == null) return false;

        double remainingAmount = bill.amountDue() - amountPaid;
        String newStatus = remainingAmount <= 0 ? "PAID" : "PARTIALLY_PAID";

        Bill updatedBill = new Bill(
                bill.billID(),
                bill.customerID(),
                bill.consumptionID(),
                bill.rateID(),
                remainingAmount,
                bill.dueDate(),
                newStatus,
                bill.generatedByStaffID(),
                bill.technicianID()
        );

        return billCRUD.updateRecord(updatedBill);
    }
}
