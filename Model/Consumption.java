package Model;

public class Consumption
{
    private int consumptionId;
    private int meterId;
    private Double consumptionValue;
    private String readingDate;
    private String billingPeriod;

    public Consumption(int consumptionId, int meterId, Double consumptionValue, String readingDate, String billingPeriod)
    {
        this.consumptionId = consumptionId;
        this.meterId = meterId;
        this.consumptionValue = consumptionValue;
        this.readingDate = readingDate;
        this.billingPeriod = billingPeriod;
    }

    // Getters and Setters
    public int getConsumptionId() { return consumptionId; }
    public void setConsumptionId(int consumptionId) { this.consumptionId = consumptionId; }
    public int getMeterId() { return meterId; }
    public void setMeterId(int meterId) { this.meterId = meterId; }
    public Double getConsumptionValue() { return consumptionValue; }
    public void setConsumptionValue(Double consumptionValue) { this.consumptionValue = consumptionValue; }
    public String getReadingDate() { return readingDate; }
    public void setReadingDate(String readingDate) { this.readingDate = readingDate; }
    public String getBillingPeriod() { return billingPeriod; }
    public void setBillingPeriod(String billingPeriod) { this.billingPeriod = billingPeriod; }
}
