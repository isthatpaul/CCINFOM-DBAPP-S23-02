package Model;

public class Employee
{
    private int employeeID;
    private String fullName;
    private String address;
    private String contactNumber;
    private int departmentID;
    private String position;
    private String hireDate;
    private String lastLoginDate;

    public Employee()
    {

    }

    // getters and setters
    public int getEmployeeID() { return employeeID; }

    public void setEmployeeID(int employeeID) { this.employeeID = employeeID; }

    public String getFullName() { return fullName; }

    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getContactNumber() { return contactNumber; }

    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public int getDepartmentID() { return departmentID; }

    public void setDepartmentID(int departmentID) { this.departmentID = departmentID; }

    public String getPosition() { return position; }

    public void setPosition(String position) { this.position = position; }

    public String getHireDate() { return hireDate; }

    public void setHireDate(String hireDate) { this.hireDate = hireDate; }

    public String getLastLoginDate() { return lastLoginDate; }

    public void setLastLoginDate(String lastLoginDate) { this.lastLoginDate = lastLoginDate; }
}
