package Model;

import java.sql.Date;

public record Bill(
        int billID, // PK
        int customerID, // FK to Customer
        int consumptionID, // FK to Consumption (and UNIQUE)
        int rateID, // FK to Rate
        Double amountDue,
        Date dueDate,
        String status, // e.g., 'UNPAID', 'PARTIALLY_PAID', 'OVERDUE', 'PAID'
        int generatedByStaffID, // FK to UserAccount
        Integer technicianID // FK to Employee
) {}