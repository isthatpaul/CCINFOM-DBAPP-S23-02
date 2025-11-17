package Model;

import java.sql.Date;

public record Employee(
        int employeeID, // PK
        String firstName,
        String lastName,
        String street,
        String city,
        String province,
        String zipCode,
        String contactNumber,
        int departmentID, // FK to Department
        String position,
        Date hireDate,
        Date lastLoginDate
) {}
