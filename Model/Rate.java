package Model;

public class Rate
{
    private Long rateId;
    private Long utilityTypeId; // Foreign Key
    private Double ratePerUnit;
    private String effectiveDate;


    public Rate(Long rateId, Long utilityTypeId, Double ratePerUnit, String effectiveDate)
    {
        this.rateId = rateId;
        this.utilityTypeId = utilityTypeId;
        this.ratePerUnit = ratePerUnit;
        this.effectiveDate = effectiveDate;
    }

    // Getters and Setters
    public Long getRateId() { return rateId; }

    public void setRateId(Long rateId) { this.rateId = rateId; }

    public Long getUtilityTypeId() { return utilityTypeId; }

    public void setUtilityTypeId(Long utilityTypeId) { this.utilityTypeId = utilityTypeId; }

    public Double getRatePerUnit() { return ratePerUnit; }

    public void setRatePerUnit(Double ratePerUnit) { this.ratePerUnit = ratePerUnit; }

    public String getEffectiveDate() { return effectiveDate; }

    public void setEffectiveDate(String effectiveDate) { this.effectiveDate = effectiveDate; }
}
