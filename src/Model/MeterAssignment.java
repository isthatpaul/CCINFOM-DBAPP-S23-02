package Model;

import java.sql.Date;

public record MeterAssignment(
        int assignmentID, // PK
        int customerID, // FK to Customer
        int meterID, // FK to Meter
        Date assignmentDate,
        Date installationDate,
        int  assignedByStaffID,
        String status, // e.g., 'ACTIVE', 'INACTIVE'
        Date lastUpdated
) {}