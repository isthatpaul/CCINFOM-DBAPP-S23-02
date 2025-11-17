package Model;

public record Meter(
        int meterID, // PK
        int utilityTypeID, // FK to UtilityType
        String meterSerialNumber, // UNIQUE
        String meterStatus // e.g., 'ASSIGNED', 'AVAILABLE', 'DECOMMISSIONED'
) {}