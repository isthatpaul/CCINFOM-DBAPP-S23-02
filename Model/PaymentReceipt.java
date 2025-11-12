package Model;

public class PaymentReceipt
{
    private int paymentID; // PK
    private int billID; // FK
    private String paymentDate;
    private Double amountPaid;
    private String paymentMethod;
    private String receiptNumber; // UNIQUE Constraint
    private int processedByUserID; // FK
    private int collectorID; // FK
    private String status;

    public PaymentReceipt(int paymentID, int billID, String paymentDate, Double amountPaid, String paymentMethod, String receiptNumber, int processedByUserID, int collectorID, String status)
    {
        this.paymentID = paymentID;
        this.billID = billID;
        this.paymentDate = paymentDate;
        this.amountPaid = amountPaid;
        this.paymentMethod = paymentMethod;
        this.receiptNumber = receiptNumber;
        this.processedByUserID = processedByUserID;
        this.collectorID = collectorID;
        this.status = status;
    }

    // Getters and Setters
    public int getPaymentID() { return paymentID; }

    public void setPaymentID(int paymentID) { this.paymentID = paymentID; }

    public int getBillID() { return billID; }

    public void setBillID(int billID) { this.billID = billID; }

    public String getPaymentDate() { return paymentDate;}

    public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }

    public Double getAmountPaid() { return amountPaid; }

    public void setAmountPaid(Double amountPaid) { this.amountPaid = amountPaid; }

    public String getPaymentMethod() { return paymentMethod; }

    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getReceiptNumber() { return receiptNumber; }

    public void setReceiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; }

    public int getProcessedByUserID() { return processedByUserID; }

    public void setProcessedByUserID(int processedByUserID) { this.processedByUserID = processedByUserID; }

    public int getCollectorID() { return collectorID; }

    public void setCollectorID(int collectorID) { this.collectorID = collectorID; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}