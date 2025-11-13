package Model;

import Database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserAccountCRUD {

    // CREATE
    public boolean addRecord(UserAccount user)
    {
        String sql = "INSERT INTO USER_ACCOUNT (EMPLOYEE_ID, USERNAME, PASSWORD_HASH, ROLE, ASSIGNED_BRANCH) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, user.employeeID());
            ps.setString(2, user.username());
            ps.setString(3, user.passwordHash());
            ps.setString(4, user.role());
            ps.setString(5, user.assignedBranch());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("UserAccount addRecord error: " + e.getMessage());
            return false;
        }
    }

    // READ ALL (Optional for Security, but included for completeness)
    public List<UserAccount> getAllRecords()
    {
        List<UserAccount> list = new ArrayList<>();
        String sql = "SELECT USER_ID, EMPLOYEE_ID, USERNAME, ROLE, ASSIGNED_BRANCH FROM USER_ACCOUNT"; // Exclude password_hash
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                // Note: Password hash is set to an empty string for security
                UserAccount ua = new UserAccount(
                        rs.getInt("USER_ID"),
                        rs.getInt("EMPLOYEE_ID"),
                        rs.getString("USERNAME"),
                        "",
                        rs.getString("ROLE"),
                        rs.getString("ASSIGNED_BRANCH")
                );
                list.add(ua);
            }
        } catch (SQLException e) {
            System.err.println("UserAccount getAllRecords error: " + e.getMessage());
        }
        return list;
    }

    // READ ONE
    public UserAccount getRecordById(int userId)
    {
        String sql = "SELECT * FROM USER_ACCOUNT WHERE USER_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new UserAccount(
                            rs.getInt("USER_ID"),
                            rs.getInt("EMPLOYEE_ID"),
                            rs.getString("USERNAME"),
                            rs.getString("PASSWORD_HASH"),
                            rs.getString("ROLE"),
                            rs.getString("ASSIGNED_BRANCH")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("UserAccount getRecordById error: " + e.getMessage());
        }
        return null;
    }

    // UPDATE
    public boolean updateRecord(UserAccount user) {
        // Does not update PASSWORD_HASH unless a separate function is called
        String sql = "UPDATE USER_ACCOUNT SET EMPLOYEE_ID = ?, USERNAME = ?, ROLE = ?, ASSIGNED_BRANCH = ? WHERE USER_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, user.employeeID());
            ps.setString(2, user.username());
            ps.setString(3, user.passwordHash());
            ps.setString(4, user.role());
            ps.setString(5, user.assignedBranch());
            ps.setInt(6, user.employeeID());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("UserAccount updateRecord error: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean deleteRecord(Long userId) {
        String sql = "DELETE FROM USER_ACCOUNT WHERE USER_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("UserAccount deleteRecord error: " + e.getMessage());
            return false;
        }
    }
}