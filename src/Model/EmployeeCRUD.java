package Model;

import Database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeCRUD {

    // CREATE
    public boolean addRecord(Employee employee) {
        String sql = "INSERT INTO Employee (DepartmentID, FirstName, LastName, Street, City, Province, ZipCode, ContactNumber, Position, HireDate, LastLoginDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, employee.departmentID());
            ps.setString(2, employee.firstName());
            ps.setString(3, employee.lastName());
            ps.setString(4, employee.street());
            ps.setString(5, employee.city());
            ps.setString(6, employee.province());
            ps.setString(7, employee.zipCode());
            ps.setString(8, employee.contactNumber());
            ps.setString(9, employee.position());
            ps.setDate(10, employee.hireDate());
            ps.setDate(11, employee.lastLoginDate());

            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Employee addRecord error: " + e.getMessage());
            return false;
        }
    }

    // READ ALL
    public List<Employee> getAllRecords() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM Employee";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Employee e = new Employee(
                        rs.getInt("EmployeeID"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Street"),
                        rs.getString("City"),
                        rs.getString("Province"),
                        rs.getString("ZipCode"),
                        rs.getString("ContactNumber"),
                        rs.getInt("DepartmentID"),
                        rs.getString("Position"),
                        rs.getDate("HireDate"),
                        rs.getDate("LastLoginDate")
                );
                list.add(e);
            }

        } catch (SQLException e) {
            System.err.println("Employee getAllRecords error: " + e.getMessage());
        }
        return list;
    }

    // READ ONE
    public Employee getRecordById(int employeeID) {
        String sql = "SELECT * FROM Employee WHERE EmployeeID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, employeeID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Employee(
                            rs.getInt("EmployeeID"),
                            rs.getString("FirstName"),
                            rs.getString("LastName"),
                            rs.getString("Street"),
                            rs.getString("City"),
                            rs.getString("Province"),
                            rs.getString("ZipCode"),
                            rs.getString("ContactNumber"),
                            rs.getInt("DepartmentID"),
                            rs.getString("Position"),
                            rs.getDate("HireDate"),
                            rs.getDate("LastLoginDate")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Employee getRecordById error: " + e.getMessage());
        }

        return null;
    }

    // UPDATE
    public boolean updateRecord(Employee employee) {
        String sql = "UPDATE Employee SET DepartmentID = ?, FirstName = ?, LastName = ?, Street = ?, City = ?, Province = ?, ZipCode = ?, ContactNumber = ?, Position = ?, HireDate = ?, LastLoginDate = ? WHERE EmployeeID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, employee.departmentID());
            ps.setString(2, employee.firstName());
            ps.setString(3, employee.lastName());
            ps.setString(4, employee.street());
            ps.setString(5, employee.city());
            ps.setString(6, employee.province());
            ps.setString(7, employee.zipCode());
            ps.setString(8, employee.contactNumber());
            ps.setString(9, employee.position());
            ps.setDate(10, employee.hireDate());
            ps.setDate(11, employee.lastLoginDate());
            ps.setInt(12, employee.employeeID());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Employee updateRecord error: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean deleteRecord(int employeeID) {
        String sql = "DELETE FROM Employee WHERE EmployeeID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, employeeID);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Employee deleteRecord error: " + e.getMessage());
            return false;
        }
    }
}