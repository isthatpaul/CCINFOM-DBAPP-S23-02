package Model;

import Database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentCRUD
{
    // CREATE
    public boolean addRecord(Department department) {
        String sql = "INSERT INTO DEPARTMENT (DEPARTMENTNAME, Description) VALUES (?, ?)";
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
        String sql = "SELECT * FROM DEPARTMENT";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql))
        {
            while (rs.next()) {
                Department d = new Department(
                        rs.getInt("DEPARTMENT_ID"),
                        rs.getString("DEPARTMENT_NAME"),
                        rs.getString("DESCRIPTION")
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
        String sql = "SELECT * FROM DEPARTMENT WHERE DEPARTMENTID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, departmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Department(
                            rs.getInt("DEPARTMENT_ID"),
                            rs.getString("DEPARTMENT_NAME"),
                            rs.getString("DESCRIPTION")
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
        String sql = "UPDATE DEPARTMENT SET DEPARTMENTNAME=?, Description=? WHERE DEPARTMENTID=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setString(1, department.departmentName());
            ps.setLong(2, department.departmentID());
            ps.setString(2, department.description());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Department updateRecord error: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean deleteRecord(int departmentId) {
        String sql = "DELETE FROM DEPARTMENT WHERE DEPARTMENTID=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, departmentId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Department deleteRecord error: " + e.getMessage());
            return false;
        }
    }
}
