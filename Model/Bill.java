package Model;

public class Bill
{
    private int billID; // PK
    private int customerID; // FK
    private int consumptionID; // FK, UNIQUE
    private int rateID; // FK
    private Double amountDue;
    private String dueDate;
    private String status;
    private int generatedByUserID; // FK
    private int technicianID; // FK

    public Bill(int billID, int customerID, int consumptionID, int rateID, Double amountDue, String dueDate, String status, int generatedByUserID, int technicianID)
    {
        this.billID = billID;
        this.customerID = customerID;
        this.consumptionID = consumptionID;
        this.rateID = rateID;
        this.amountDue = amountDue;
        this.dueDate = dueDate;
        this.status = status;
        this.generatedByUserID = generatedByUserID;
        this.technicianID = technicianID;
    }

    // Getters and Setters
    public int getBillID() { return billID; }

    public void setBillID(int billID) { this.billID = billID; }

    public int  getCustomerID() { return customerID; }

    public void setCustomerID(int customerID) { this.customerID = customerID; }

    public int getConsumptionID() { return consumptionID; }

    public void setConsumptionID(int consumptionID) { this.consumptionID = consumptionID; }

    public int getRateID() { return rateID; }

    public void setRateID(int rateID) { this.rateID = rateID; }

    public Double getAmountDue() { return amountDue; }

    public void setAmountDue(Double amountDue) { this.amountDue = amountDue; }

    public String getDueDate() { return dueDate; }

    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public int getGeneratedByUserID() { return generatedByUserID; }

    public void setGeneratedByUserID(int generatedByUserID) { this.generatedByUserID = generatedByUserID; }

    public int getTechnicianID() { return technicianID; }

    public void setTechnicianID(int technicianID) { this.technicianID = technicianID; }
}
