package Model;

import java.sql.Date;

public record OverdueNotice(
        int noticeID, // PK
        int billID, // FK to Bill
        Date overdueDate,
        Double penaltyAmount,
        Date noticeDate,
        String escalationStatus,
        int sentByStaffID
) {}