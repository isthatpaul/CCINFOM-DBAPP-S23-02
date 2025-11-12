package Model;

import java.sql.Date;

public class OverdueNotice
{
    private int noticeID; // PK
    private int billID; // FK
    private String overdueDate;
    private Double penaltyAmount;
    private Date noticeDate;
    private String escalationStatus;
    private int sentByUserID; // FK

    public OverdueNotice(int noticeID, int billID, String overdueDate, Double penaltyAmount, Date noticeDate, String escalationStatus, int sentByUserID)
    {
        this.noticeID = noticeID;
        this.billID = billID;
        this.overdueDate = overdueDate;
        this.penaltyAmount = penaltyAmount;
        this.noticeDate = noticeDate;
        this.escalationStatus = escalationStatus;
        this.sentByUserID = sentByUserID;
    }

    // Getters and Setters
    public int getNoticeID() { return noticeID; }

    public void setNoticeID(int noticeID) { this.noticeID = noticeID; }

    public int getBillID() { return billID; }

    public void setBillID(int billID) { this.billID = billID; }

    public String getOverdueDate() { return overdueDate; }

    public void setOverdueDate(String overdueDate) { this.overdueDate = overdueDate; }

    public Double getPenaltyAmount() { return penaltyAmount; }

    public void setPenaltyAmount(Double penaltyAmount) { this.penaltyAmount = penaltyAmount; }

    public Date getNoticeDate() { return noticeDate; }

    public void setNoticeDate(Date noticeDate) { this.noticeDate = noticeDate; }

    public String getEscalationStatus() { return escalationStatus; }

    public void setEscalationStatus(String escalationStatus) { this.escalationStatus = escalationStatus; }

    public int getSentByUserID() { return sentByUserID; }

    public void setSentByUserID(int sentByUserID) { this.sentByUserID = sentByUserID; }
}