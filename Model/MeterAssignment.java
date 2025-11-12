package Model;

import java.sql.Date;

public class MeterAssignment
{
    private int assignmentID;
    private int customerID;
    private int meterID;
    private Date assignmentDate;
    private Date installationDate;
    private int assignedByUserID;
    private String status;
    private Date lastUpdated;

    public MeterAssignment(int assignmentID, int customerID, int meterID, Date assignmentDate, Date installationDate, int assignedByUserID, String status, Date lastUpdated)
    {
        this.assignmentID = assignmentID;
        this.customerID = customerID;
        this.meterID = meterID;
        this.assignmentDate = assignmentDate;
        this.installationDate = installationDate;
        this.assignedByUserID = assignedByUserID;
        this.status = status;
        this.lastUpdated = lastUpdated;
    }

    public int getAssignmentID() { return assignmentID; }

    public void setAssignmentID(int assignmentID) { this.assignmentID = assignmentID; }

    public int getCustomerID() { return customerID; }

    public void setCustomerID(int customerID) { this.customerID = customerID; }

    public int getMeterID() { return meterID; }

    public void setMeterID(int meterID) { this.meterID = meterID; }

    public Date getAssignmentDate() { return assignmentDate; }

    public void setAssignmentDate(Date assignmentDate) { this.assignmentDate = assignmentDate; }

    public Date getInstallationDate() { return installationDate; }

    public void setInstallationDate(Date installationDate) { this.installationDate = installationDate; }

    public int getAssignedByUserID() { return assignedByUserID; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public Date getLastUpdated() { return lastUpdated; }

    public void setLastUpdated(Date lastUpdated) { this.lastUpdated = lastUpdated; }
}
