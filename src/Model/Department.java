package Model;

public class Department
{
    private Long departmentId;
    private String departmentName;

    public Department(Long departmentId, String departmentName)
    {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }

    // Getters and Setters
    public Long getDepartmentId() { return departmentId; }

    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

    public String getDepartmentName() { return departmentName; }

    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
}