package Model;

import java.sql.Date;

public record Consumption(
        int consumptionID, // PK
        int meterID, // FK to Meter
        Double consumptionValue,
        Date readingDate,
        Date billingPeriod
) {}