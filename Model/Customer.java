package Model;

public class Customer
{
    private int customerID;
    private String accountNumber;
    private String firstName;
    private String lastName;
    private String street;
    private String city;
    private String province;
    private String zipCode;
    private String contactNumber;
    private String createdDate;
    private String billingStatus;

    public Customer(int customerID, String accountNumber, String firstName, String lastName, String street,String city, String province, String zipCode, String contactNumber, String createdDate, String billingStatus)
    {
        this.customerID = customerID;
        this.accountNumber = accountNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.province = province;
        this.contactNumber = contactNumber;
        this.createdDate = createdDate;
        this.billingStatus = billingStatus;
    }

    // getters and setters
    public int getCustomerID()
    {
        return customerID;
    }

    public void setCustomerID(int customerID)
    {
        this.customerID = customerID;
    }

    public String getAccountNumber()
    {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber)
    {
        this.accountNumber = accountNumber;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getStreet()
    {
        return street;
    }

    public void setStreet(String street)
    {
        this.street = street;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public String getZipCode()
    {
        return zipCode;
    }

    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }

    public String getContactNumber()
    {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber)
    {
        this.contactNumber = contactNumber;
    }

    public String getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(String createdDate)
    {
        this.createdDate = createdDate;
    }

    public String getBillingStatus() { return billingStatus;}

    public void setBillingStatus(String billingStatus) { this.billingStatus = billingStatus;}
}
