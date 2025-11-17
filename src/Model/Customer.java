package Model;

import java.sql.Date;

public record Customer(
        int customerID, // PK
        String accountNumber, // UNIQUE
        String firstName,
        String lastName,
        String street,
        String city,
        String province,
        String zipCode,
        String contactNumber,
        Date createdDate,
        String billingStatus
) {}
