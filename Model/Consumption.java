package Model;

import java.sql.Date;

public class Consumption
{
    private int consumptionId;
    private int meterId;
    private Double consumptionValue;
    private Date readingDate;
    private Date billingPeriod;

    public Consumption(int consumptionId, int meterId, Double consumptionValue, Date readingDate, Date billingPeriod)
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

    public Date getReadingDate() { return readingDate; }

    public void setReadingDate(Date readingDate) { this.readingDate = readingDate; }

    public Date getBillingPeriod() { return billingPeriod; }

    public void setBillingPeriod(Date billingPeriod) { this.billingPeriod = billingPeriod; }
}
