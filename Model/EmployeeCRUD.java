package Model;

import Database.DatabaseConnection;
import Model.Employee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeCRUD
{
    // CREATE
    public boolean addRecord(Employee employee) {
        String sql = "INSERT INTO EMPLOYEE (FIRST_NAME, LAST_NAME, ADDRESS, CONTACT_NUMBER, DEPARTMENT_ID, POSITION, HIRE_DATE, LAST_LOGIN_DATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, employee.getFirstName());
            ps.setString(2, employee.getLastName());
            ps.setString(3, employee.getAddress());
            ps.setString(4, employee.getContactNumber());
            ps.setInt(5, employee.getDepartmentID());
            ps.setString(6, employee.getPosition());
            ps.setDate(7, employee.getHireDate());
            ps.setDate(8, employee.getLastLoginDate());
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
        String sql = "SELECT * FROM EMPLOYEE";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Employee e = new Employee(
                        rs.getInt("EMPLOYEE_ID"),
                        rs.getString("FIRST_NAME"),
                        rs.getString("LAST_NAME"),
                        rs.getString("ADDRESS"),
                        rs.getString("CONTACT_NUMBER"),
                        rs.getInt("DEPARTMENT_ID"),
                        rs.getString("POSITION"),
                        rs.getDate("HIRE_DATE"),
                        rs.getDate("LAST_LOGIN_DATE")
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
        String sql = "SELECT * FROM EMPLOYEE WHERE EMPLOYEE_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, employeeID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Employee(
                            rs.getInt("EMPLOYEE_ID"),
                            rs.getString("FIRST_NAME"),
                            rs.getString("LAST_NAME"),
                            rs.getString("ADDRESS"),
                            rs.getString("CONTACT_NUMBER"),
                            rs.getInt("DEPARTMENT_ID"),
                            rs.getString("POSITION"),
                            rs.getDate("HIRE_DATE"),
                            rs.getDate("LAST_LOGIN_DATE")
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
        String sql = "UPDATE EMPLOYEE SET FIRST_NAME = ?, LAST_NAME = ?, ADDRESS = ?, CONTACT_NUMBER = ?, DEPARTMENT_ID = ?, POSITION = ?, HIRE_DATE = ?, LAST_LOGIN_DATE = ? WHERE EMPLOYEE_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, employee.getFirstName());
            ps.setString(2, employee.getLastName());
            ps.setString(3, employee.getAddress());
            ps.setString(4, employee.getContactNumber());
            ps.setInt(5, employee.getDepartmentID());
            ps.setString(6, employee.getPosition());
            ps.setDate(7, employee.getHireDate());
            ps.setDate(8, employee.getLastLoginDate());
            ps.setInt(9, employee.getEmployeeID());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Employee updateRecord error: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean deleteRecord(int employeeID) {
        String sql = "DELETE FROM EMPLOYEE WHERE EMPLOYEE_ID = ?";
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
