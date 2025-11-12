package Model;

import java.sql.Date;

public class Employee
{
    private int employeeID;
    private String firstName;
    private String lastName;
    private String address;
    private String contactNumber;
    private int departmentID;
    private String position;
    private Date hireDate;
    private Date lastLoginDate;

    public Employee(int employeeID, String firstName, String lastName, String address, String contactNumber, int departmentID, String position, Date hireDate, Date lastLoginDate)
    {
        this.employeeID = employeeID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.contactNumber = contactNumber;
        this.departmentID = departmentID;
        this.position = position;
        this.hireDate = hireDate;
        this.lastLoginDate = lastLoginDate;
    }

    // getters and setters
    public int getEmployeeID() { return employeeID; }

    public void setEmployeeID(int employeeID) { this.employeeID = employeeID; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getContactNumber() { return contactNumber; }

    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public int getDepartmentID() { return departmentID; }

    public void setDepartmentID(int departmentID) { this.departmentID = departmentID; }

    public String getPosition() { return position; }

    public void setPosition(String position) { this.position = position; }

    public Date getHireDate() { return hireDate; }

    public void setHireDate(Date hireDate) { this.hireDate = hireDate; }

    public Date getLastLoginDate() { return lastLoginDate; }

    public void setLastLoginDate(Date lastLoginDate) { this.lastLoginDate = lastLoginDate; }
}
