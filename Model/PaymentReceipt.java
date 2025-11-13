package Model;

import java.sql.Date;

public record PaymentReceipt(
        int paymentID, // PK
        int billID, // FK to Bill
        Date paymentDate,
        Double amountPaid,
        String paymentMethod,// e.g., 'CASH', 'ONLINE', 'E-WALLET'
        String receiptNumber,
        int processedByUserID,
        int collectorID,
        String status
) {}