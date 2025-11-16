package Model;

import Database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeCRUD {

    // CREATE
    public boolean addRecord(Employee employee) {
        String sql = "INSERT INTO EMPLOYEE " +
                "(FIRSTNAME, LASTNAME, STREET, CITY, PROVINCE, ZIPCODE, CONTACTNUMBER, " +
                "DEPARTMENTID, POSITION, HIREDATE, LASTLOGINDATE) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, employee.firstName());
            ps.setString(2, employee.lastName());
            ps.setString(3, employee.street());
            ps.setString(4, employee.city());
            ps.setString(5, employee.province());
            ps.setString(6, employee.zipCode());
            ps.setString(7, employee.contactNumber());
            ps.setInt(8, employee.departmentID());
            ps.setString(9, employee.position());

            // Correct order
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
        String sql = "SELECT * FROM EMPLOYEE";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Employee e = new Employee(
                        rs.getInt("EMPLOYEE_ID"),
                        rs.getString("FIRSTNAME"),
                        rs.getString("LASTNAME"),
                        rs.getString("STREET"),
                        rs.getString("CITY"),
                        rs.getString("PROVINCE"),
                        rs.getString("ZIPCODE"),
                        rs.getString("CONTACTNUMBER"),
                        rs.getInt("DEPARTMENTID"),
                        rs.getString("POSITION"),
                        rs.getDate("HIREDATE"),
                        rs.getDate("LASTLOGINDATE")
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
        String sql = "SELECT * FROM EMPLOYEE WHERE EMPLOYEEID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, employeeID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Employee(
                            rs.getInt("EMPLOYEE_ID"),
                            rs.getString("FIRSTNAME"),
                            rs.getString("LASTNAME"),
                            rs.getString("STREET"),
                            rs.getString("CITY"),
                            rs.getString("PROVINCE"),
                            rs.getString("ZIPCODE"),
                            rs.getString("CONTACTNUMBER"),
                            rs.getInt("DEPARTMENTID"),
                            rs.getString("POSITION"),
                            rs.getDate("HIREDATE"),
                            rs.getDate("LASTLOGINDATE")
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
        String sql = "UPDATE EMPLOYEE SET " +
                "FIRSTNAME=?, LASTNAME=?, STREET=?, CITY=?, PROVINCE=?, ZIPCODE=?, " +
                "CONTACTNUMBER=?, DEPARTMENTID=?, POSITION=?, HIREDATE=?, LASTLOGINDATE=? " +
                "WHERE EMPLOYEEID=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, employee.firstName());
            ps.setString(2, employee.lastName());
            ps.setString(3, employee.street());
            ps.setString(4, employee.city());
            ps.setString(5, employee.province());
            ps.setString(6, employee.zipCode());
            ps.setString(7, employee.contactNumber());
            ps.setInt(8, employee.departmentID());
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
        String sql = "DELETE FROM EMPLOYEE WHERE EMPLOYEEID = ?";

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
