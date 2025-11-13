package Model;

import java.sql.Date;

public record Employee(
        int employeeID, // PK
        String firstName,
        String lastName,
        String address,
        String contactNumber,
        Integer departmentID, // FK to Department
        String position,
        Date hireDate,
        Date lastLoginDate
) {}
