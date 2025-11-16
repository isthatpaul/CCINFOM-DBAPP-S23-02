package Model;

import Database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentCRUD
{
    // CREATE
    public boolean addRecord(Department department) {
        String sql = "INSERT INTO Department (DepartmentName, Description) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setString(1, department.departmentName());
            ps.setString(2, department.description());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Department addRecord error: " + e.getMessage());
            return false;
        }
    }

    // READ ALL
    public List<Department> getAllRecords() {
        List<Department> list = new ArrayList<>();
        String sql = "SELECT * FROM Department";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql))
        {
            while (rs.next()) {
                Department d = new Department(
                        rs.getInt("DepartmentID"),
                        rs.getString("DepartmentName"),
                        rs.getString("Description")
                );
                list.add(d);
            }
        } catch (SQLException e) {
            System.err.println("Department getAllRecords error: " + e.getMessage());
        }
        return list;
    }

    // READ ONE
    public Department getRecordById(int departmentId) {
        String sql = "SELECT * FROM Department WHERE DepartmentID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, departmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Department(
                            rs.getInt("DepartmentID"),
                            rs.getString("DepartmentName"),
                            rs.getString("Description")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Department getRecordById error: " + e.getMessage());
        }
        return null;
    }

    // UPDATE
    public boolean updateRecord(Department department) {
        String sql = "UPDATE Department SET DepartmentName = ?, Description = ? WHERE DepartmentID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setString(1, department.departmentName());
            ps.setString(2, department.description());
            ps.setInt(3, department.departmentID());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Department updateRecord error: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean deleteRecord(int departmentId) {
        String sql = "DELETE FROM Department WHERE DepartmentID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, departmentId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Department deleteRecord error: " + e.getMessage());
            return false;
        }
    }
}