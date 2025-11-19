package Services;

import Model.*;
import config.AppConfig; // Import the configuration file
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

public class BillService {

    private final BillCRUD billCRUD = new BillCRUD();
    private final MeterCRUD meterCRUD = new MeterCRUD();
    private final RateCRUD rateCRUD = new RateCRUD();
    private final CustomerCRUD customerCRUD = new CustomerCRUD();
    private final OverdueNoticeCRUD overdueNoticeCRUD = new OverdueNoticeCRUD();

    public Bill generateBillFromConsumption(Consumption consumption, int staffId) throws BillingException {
        Meter meter = meterCRUD.getRecordById(consumption.meterID());
        if (meter == null) {
            throw new BillingException("Meter with ID " + consumption.meterID() + " not found.");
        }

        Customer customer = customerCRUD.getCustomerByMeterId(consumption.meterID());
        if (customer == null) {
            throw new BillingException("No active customer found for Meter ID " + consumption.meterID() + ".");
        }

        Rate rate = rateCRUD.findActiveRateForUtility(meter.utilityTypeID(), consumption.readingDate());
        if (rate == null) {
            throw new BillingException("No active rate found for Utility Type ID " + meter.utilityTypeID() + " on date " + consumption.readingDate());
        }

        double amountDue = consumption.consumptionValue() * rate.ratePerUnit();

        Calendar cal = Calendar.getInstance();
        cal.setTime(consumption.readingDate());
        // FIX: Use the value from AppConfig instead of a hardcoded number.
        cal.add(Calendar.DAY_OF_MONTH, AppConfig.DEFAULT_BILL_DUE_DAYS);
        Date dueDate = new Date(cal.getTimeInMillis());

        int technicianID = staffId; 

        Bill newBill = new Bill(
                0,
                customer.customerID(),
                consumption.consumptionID(),
                rate.rateID(),
                amountDue,
                dueDate,
                "UNPAID",
                staffId,
                technicianID
        );

        if (!billCRUD.addRecord(newBill)) {
            throw new BillingException("Failed to save the new bill to the database.");
        }
        
        return billCRUD.getLatestBillForConsumption(consumption.consumptionID());
    }

    public boolean updateBillAfterPayment(int billID, double amountPaid) {
        Bill bill = billCRUD.getRecordById(billID);
        if (bill == null) {
            return false;
        }

        double newAmountDue = bill.amountDue() - amountPaid;
        String newStatus = newAmountDue <= 0 ? "PAID" : "PARTIALLY_PAID";

        Bill updatedBill = new Bill(
                bill.billID(),
                bill.customerID(),
                bill.consumptionID(),
                bill.rateID(),
                newAmountDue,
                bill.dueDate(),
                newStatus,
                bill.generatedByStaffID(),
                bill.technicianID()
        );

        return billCRUD.updateRecord(updatedBill);
    }

    public int applyPenaltiesToOverdueBills(int staffId) {
        int penaltiesApplied = 0;
        List<Bill> allBills = billCRUD.getAllRecords();
        java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());

        for (Bill bill : allBills) {
            if (("UNPAID".equals(bill.status()) || "PARTIALLY_PAID".equals(bill.status())) 
                && bill.dueDate().before(currentDate)) {
                
                double penaltyAmount = bill.amountDue() * AppConfig.OVERDUE_PENALTY_RATE;
                
                OverdueNotice notice = new OverdueNotice(
                        0,
                        bill.billID(),
                        currentDate,
                        penaltyAmount,
                        currentDate,
                        "PENDING",
                        staffId
                );
                
                boolean noticeCreated = overdueNoticeCRUD.addRecord(notice);
                
                if (noticeCreated) {
                    double newAmountDue = bill.amountDue() + penaltyAmount;
                    
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
                    
                    if (billCRUD.updateRecord(updatedBill)) {
                        penaltiesApplied++;
                    }
                }
            }
        }
        return penaltiesApplied;
    }

    public static class BillingException extends Exception {
        public BillingException(String message) {
            super(message);
        }
    }
}