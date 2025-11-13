package Model;

import java.sql.Date;

public record Rate(
        int rateID, // PK
        Integer utilityTypeID, // FK to UtilityType
        Double ratePerUnit,
        Date effectiveDate
) {}