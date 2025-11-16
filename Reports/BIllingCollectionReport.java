package Reports;

import java.sql.Date;

public record BIllingCollectionReport(
        int billID,
        String customerName,
        double consumptionValue,
        double amountDue,
        Double amountPaid,
        Date billingDate,
        Date paymentDate,
        String staffName
) {}
