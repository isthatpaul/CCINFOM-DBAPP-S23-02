package Services;

import Model.*;

/**
 * Payment Service for business logic
 * Assigned to: SAMONTE, Joshua Carlos B.
 */
public class PaymentService {
    
    private PaymentCRUD paymentCRUD;
    private BillCRUD billCRUD;
    
    public PaymentService() {
        this.paymentCRUD = new PaymentCRUD();
        this.billCRUD = new BillCRUD();
    }
    
    /**
     * Process payment and update bill status
     * Transaction 3: Payment Processing
     */
    public boolean processPayment(Payment payment) {
        // Validate unique receipt number
        if (!isReceiptNumberUnique(payment.receiptNumber())) {
            System.err.println("Receipt number already exists!");
            return false;
        }
        
        // Validate amount
        if (payment.amountPaid() <= 0) {
            System.err.println("Amount must be greater than zero!");
            return false;
        }
        
        // Create payment record
        boolean paymentCreated = paymentCRUD.addRecord(payment);
        if (!paymentCreated) {
            return false;
        }
        
        // Update bill status
        Bill bill = billCRUD.getRecordById(payment.billID());
        if (bill != null) {
            updateBillAfterPayment(bill, payment.amountPaid());
        }
        
        return true;
    }
    
    private boolean isReceiptNumberUnique(String receiptNumber) {
        return paymentCRUD.getAllRecords().stream()
                .noneMatch(p -> p.receiptNumber().equals(receiptNumber));
    }
    
    private void updateBillAfterPayment(Bill bill, double amountPaid) {
        double newAmountDue = bill.amountDue() - amountPaid;
        String newStatus;
        
        if (newAmountDue <= 0) {
            newStatus = "PAID";
            newAmountDue = 0;
        } else if (newAmountDue < bill.amountDue()) {
            newStatus = "PARTIALLY_PAID";
        } else {
            newStatus = bill.status();
        }
        
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
        
        billCRUD.updateRecord(updatedBill);
    }
}