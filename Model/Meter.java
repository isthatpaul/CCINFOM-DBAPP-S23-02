package Model;

public class Meter
{
    private int meterId;
    private int utilityTypeId; // Foreign Key
    private String meterSerialNumber; // Unique Constraint
    private String meterStatus;


    public Meter(int meterId, int utilityTypeId, String meterSerialNumber, String meterStatus)
    {
        this.meterId = meterId;
        this.utilityTypeId = utilityTypeId;
        this.meterSerialNumber = meterSerialNumber;
        this.meterStatus = meterStatus;
    }

    // Getters and Setters
    public int getMeterId() { return meterId; }

    public void setMeterId(int meterId) { this.meterId = meterId; }

    public int getUtilityTypeId() { return utilityTypeId; }

    public void setUtilityTypeId(int utilityTypeId) { this.utilityTypeId = utilityTypeId; }

    public String getMeterSerialNumber() { return meterSerialNumber; }

    public void setMeterSerialNumber(String meterSerialNumber) { this.meterSerialNumber = meterSerialNumber; }

    public String getMeterStatus() { return meterStatus; }
    
    public void setMeterStatus(String meterStatus) { this.meterStatus = meterStatus; }
}