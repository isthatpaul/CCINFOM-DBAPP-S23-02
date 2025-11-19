package Services;

import Model.*;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

public class BillService {

    private final BillCRUD billCRUD = new BillCRUD();
    private final MeterCRUD meterCRUD = new MeterCRUD();
    private final RateCRUD rateCRUD = new RateCRUD();
    private final CustomerCRUD customerCRUD = new CustomerCRUD();
    private final OverdueNoticeCRUD overdueNoticeCRUD = new OverdueNoticeCRUD();
    
    private static final double OVERDUE_PENALTY_RATE = 0.05; // 5%

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
        cal.add(Calendar.DAY_OF_MONTH, 20); // Example: 20 days to pay
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
        int penaltiesAppliedCount = 0;
        List<Bill> overdueBills = billCRUD.getBillsEligibleForPenalty(new Date(System.currentTimeMillis()));
        
        for (Bill bill : overdueBills) {
            try {
                if (applyPenaltyToBill(bill.billID(), staffId)) {
                    penaltiesAppliedCount++;
                }
            } catch (BillingException e) {
                System.err.println("Could not apply penalty to bill ID " + bill.billID() + ": " + e.getMessage());
            }
        }
        return penaltiesAppliedCount;
    }

    public boolean applyPenaltyToBill(int billId, int staffId) throws BillingException {
        Bill bill = billCRUD.getRecordById(billId);
        if (bill == null) {
            throw new BillingException("Bill with ID " + billId + " not found.");
        }

        if (!isBillEligibleForPenalty(bill)) {
             return false;
        }

        if (overdueNoticeCRUD.hasOverdueNotice(bill.billID())) {
            return false;
        }

        double penaltyAmount = bill.amountDue() * OVERDUE_PENALTY_RATE;
        Date currentDate = new Date(System.currentTimeMillis());

        // The fix is here: get the next available NoticeID before creating the object.
        int nextNoticeID = overdueNoticeCRUD.getNextNoticeID();

        OverdueNotice notice = new OverdueNotice(
                nextNoticeID, // Use the generated ID
                bill.billID(),
                currentDate,
                penaltyAmount,
                currentDate,
                "First Notice",
                staffId
        );

        if (!overdueNoticeCRUD.addRecord(notice)) {
            throw new BillingException("Failed to create an overdue notice for Bill ID: " + bill.billID());
        }

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

        if (!billCRUD.updateRecord(updatedBill)) {
            overdueNoticeCRUD.deleteRecord(notice.noticeID());
            throw new BillingException("Failed to update bill with penalty. Overdue notice creation was rolled back.");
        }

        return true;
    }

    private boolean isBillEligibleForPenalty(Bill bill) {
        Date currentDate = new Date(System.currentTimeMillis());
        String status = bill.status();
        
        boolean isStatusEligible = "UNPAID".equalsIgnoreCase(status) || "PARTIALLY_PAID".equalsIgnoreCase(status);
        boolean isPastDueDate = bill.dueDate().before(currentDate);
        
        return isStatusEligible && isPastDueDate;
    }

    public static class BillingException extends Exception {
        public BillingException(String message) {
            super(message);
        }
    }
}