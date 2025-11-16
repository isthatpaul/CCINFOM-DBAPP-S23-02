package Model;

import Database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffCRUD {

    // CREATE
    public boolean addRecord(Staff staff) {
        String sql = "INSERT INTO Staff (EmployeeID, Username, Password, Role, AssignedBranch) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, staff.employeeID());
            ps.setString(2, staff.username());
            ps.setString(3, staff.passwordHash());
            ps.setString(4, staff.role());
            if (staff.assignedBranch() == null) {
                ps.setNull(5, Types.VARCHAR);
            } else {
                ps.setString(5, staff.assignedBranch());
            }

            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Staff addRecord error: " + e.getMessage());
            return false;
        }
    }

    // READ ALL
    public List<Staff> getAllRecords() {
        List<Staff> list = new ArrayList<>();
        String sql = "SELECT * FROM Staff";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Staff s = new Staff(
                        rs.getInt("StaffID"),
                        rs.getInt("EmployeeID"),
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("Role"),
                        rs.getString("AssignedBranch")
                );
                list.add(s);
            }
        } catch (SQLException e) {
            System.err.println("Staff getAllRecords error: " + e.getMessage());
        }
        return list;
    }

    // READ ONE by StaffID
    public Staff getRecordById(int staffId) {
        String sql = "SELECT * FROM Staff WHERE StaffID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, staffId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Staff(
                            rs.getInt("StaffID"),
                            rs.getInt("EmployeeID"),
                            rs.getString("Username"),
                            rs.getString("Password"),
                            rs.getString("Role"),
                            rs.getString("AssignedBranch")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Staff getRecordById error: " + e.getMessage());
        }
        return null;
    }

    // READ ONE by Username (used by authentication)
    public Staff getRecordByUsername(String username) {
        String sql = "SELECT * FROM Staff WHERE Username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Staff(
                            rs.getInt("StaffID"),
                            rs.getInt("EmployeeID"),
                            rs.getString("Username"),
                            rs.getString("Password"),
                            rs.getString("Role"),
                            rs.getString("AssignedBranch")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Staff getRecordByUsername error: " + e.getMessage());
        }
        return null;
    }

    // READ ONE by EmployeeID (optional helper)
    public Staff getRecordByEmployeeId(int employeeId) {
        String sql = "SELECT * FROM Staff WHERE EmployeeID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Staff(
                            rs.getInt("StaffID"),
                            rs.getInt("EmployeeID"),
                            rs.getString("Username"),
                            rs.getString("Password"),
                            rs.getString("Role"),
                            rs.getString("AssignedBranch")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Staff getRecordByEmployeeId error: " + e.getMessage());
        }
        return null;
    }

    // UPDATE full record (including password hash)
    public boolean updateRecord(Staff staff) {
        String sql = "UPDATE Staff SET EmployeeID = ?, Username = ?, Password = ?, Role = ?, AssignedBranch = ? WHERE StaffID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, staff.employeeID());
            ps.setString(2, staff.username());
            ps.setString(3, staff.passwordHash());
            ps.setString(4, staff.role());
            if (staff.assignedBranch() == null) {
                ps.setNull(5, Types.VARCHAR);
            } else {
                ps.setString(5, staff.assignedBranch());
            }
            ps.setInt(6, staff.staffID());

            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Staff updateRecord error: " + e.getMessage());
            return false;
        }
    }

    // Convenience: update only password hash (used during rehash on login)
    public boolean updatePasswordHash(int staffId, String newHash) {
        String sql = "UPDATE Staff SET Password = ? WHERE StaffID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newHash);
            ps.setInt(2, staffId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Staff updatePasswordHash error: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean deleteRecord(int staffId) {
        String sql = "DELETE FROM Staff WHERE StaffID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, staffId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Staff deleteRecord error: " + e.getMessage());
            return false;
        }
    }
}