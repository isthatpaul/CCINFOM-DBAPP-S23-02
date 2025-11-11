package Model;

public class UserAccount
{
    private Long userId;
    private Long employeeId; // Foreign Key (Unique)
    private String username;
    private String password; // Storing as hash, not plain password
    private String role;
    private String assignedBranch;

    public UserAccount(Long userId, Long employeeId, String username, String password, String role, String assignedBranch)
    {
        this.userId = userId;
        this.employeeId = employeeId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.assignedBranch = assignedBranch;
    }

    // Getters and Setters
    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    public Long getEmployeeId() { return employeeId; }

    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return password; }

    public void setPasswordHash(String passwordHash) { this.password = password; }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    public String getAssignedBranch() { return assignedBranch; }

    public void setAssignedBranch(String assignedBranch) { this.assignedBranch = assignedBranch; }
}