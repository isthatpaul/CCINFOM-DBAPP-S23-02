package Model;

import java.sql.Date;

public record Payment(
        int paymentID, // PK
        int billID, // FK to Bill
        Date paymentDate,
        Double amountPaid,
        String paymentMethod,// e.g., 'CASH', 'ONLINE', 'E-WALLET'
        String receiptNumber,
        int processedByStaffID,
        int collectorID,
        String status
) {}