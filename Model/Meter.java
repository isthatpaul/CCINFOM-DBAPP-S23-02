package Model;

public class Meter
{
    private Long meterId;
    private Long utilityTypeId; // Foreign Key
    private String meterSerialNumber; // Unique Constraint
    private String meterStatus;


    public Meter(Long meterId, Long utilityTypeId, String meterSerialNumber, String meterStatus)
    {
        this.meterId = meterId;
        this.utilityTypeId = utilityTypeId;
        this.meterSerialNumber = meterSerialNumber;
        this.meterStatus = meterStatus;
    }

    // Getters and Setters
    public Long getMeterId() { return meterId; }

    public void setMeterId(Long meterId) { this.meterId = meterId; }

    public Long getUtilityTypeId() { return utilityTypeId; }

    public void setUtilityTypeId(Long utilityTypeId) { this.utilityTypeId = utilityTypeId; }

    public String getMeterSerialNumber() { return meterSerialNumber; }

    public void setMeterSerialNumber(String meterSerialNumber) { this.meterSerialNumber = meterSerialNumber; }

    public String getMeterStatus() { return meterStatus; }
    
    public void setMeterStatus(String meterStatus) { this.meterStatus = meterStatus; }
}