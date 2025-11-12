package Model;

public class MeterAssignment
{
    private int assignmentID;
    private int customerID;
    private int meterID;
    private String assignmentDate;
    private String installationDate;
    private int assignedByUserID;
    private String status;
    private String lastUpdated;

    public MeterAssignment(int assignmentID, int customerID, int meterID, String assignmentDate, String installationDate, int assignedByUserID, String status, String lastUpdated)
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

    public String getAssignmentDate() { return assignmentDate; }

    public void setAssignmentDate(String assignmentDate) { this.assignmentDate = assignmentDate; }

    public String getInstallationDate() { return installationDate; }

    public void setInstallationDate(String installationDate) { this.installationDate = installationDate; }

    public int getAssignedByUserID() { return assignedByUserID; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getLastUpdated() { return lastUpdated; }

    public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }
}
