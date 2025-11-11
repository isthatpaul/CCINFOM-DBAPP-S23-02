package Model;

public class Consumption
{
    private Long consumptionId;
    private Long meterId;
    private Double consumptionValue;
    private String readingDate;
    private String billingPeriod;

    public Consumption(Long consumptionId, Long meterId, Double consumptionValue, String readingDate, String billingPeriod)
    {
        this.consumptionId = consumptionId;
        this.meterId = meterId;
        this.consumptionValue = consumptionValue;
        this.readingDate = readingDate;
        this.billingPeriod = billingPeriod;
    }

    // Getters and Setters
    public Long getConsumptionId() { return consumptionId; }
    public void setConsumptionId(Long consumptionId) { this.consumptionId = consumptionId; }
    public Long getMeterId() { return meterId; }
    public void setMeterId(Long meterId) { this.meterId = meterId; }
    public Double getConsumptionValue() { return consumptionValue; }
    public void setConsumptionValue(Double consumptionValue) { this.consumptionValue = consumptionValue; }
    public String getReadingDate() { return readingDate; }
    public void setReadingDate(String readingDate) { this.readingDate = readingDate; }
    public String getBillingPeriod() { return billingPeriod; }
    public void setBillingPeriod(String billingPeriod) { this.billingPeriod = billingPeriod; }
}
