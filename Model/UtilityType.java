package Model;

import java.sql.Date;

public record UtilityType(
        int utilityTypeID, // PK
        String utilityTypeName,
        String description,
        String unitOfMeasure,
        Date createdDate,
        Date modifiedDate,
        boolean isActive
) {}
