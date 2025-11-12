package Model;

public class Rate
{
    private int rateId;
    private int utilityTypeId; // Foreign Key
    private Double ratePerUnit;
    private String effectiveDate;


    public Rate(int rateId, int utilityTypeId, Double ratePerUnit, String effectiveDate)
    {
        this.rateId = rateId;
        this.utilityTypeId = utilityTypeId;
        this.ratePerUnit = ratePerUnit;
        this.effectiveDate = effectiveDate;
    }

    // Getters and Setters
    public int getRateId() { return rateId; }

    public void setRateId(int rateId) { this.rateId = rateId; }

    public int getUtilityTypeId() { return utilityTypeId; }

    public void setUtilityTypeId(int utilityTypeId) { this.utilityTypeId = utilityTypeId; }

    public Double getRatePerUnit() { return ratePerUnit; }

    public void setRatePerUnit(Double ratePerUnit) { this.ratePerUnit = ratePerUnit; }

    public String getEffectiveDate() { return effectiveDate; }

    public void setEffectiveDate(String effectiveDate) { this.effectiveDate = effectiveDate; }
}
