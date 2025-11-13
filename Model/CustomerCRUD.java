package Model;

import Database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerCRUD
{
    // CREATE
    public boolean addRecord(Customer customer) {
        String sql = "INSERT INTO Customer (account_number, first_name, last_name, street, city, province, zip_code, contact_number, created_date, billing_status) VALUES (?, ?, ?, ?, ?, ?, ? , ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt  = conn.prepareStatement(sql))
        {
            stmt.setString(1, customer.accountNumber());
            stmt.setString(2, customer.firstName());
            stmt.setString(3, customer.lastName());
            stmt.setString(4, customer.street());
            stmt.setString(5, customer.city());
            stmt.setString(6, customer.province());
            stmt.setString(7, customer.zipCode());
            stmt.setString(8, customer.contactNumber());
            stmt.setDate(9, customer.createdDate());
            stmt.setString(10, customer.billingStatus());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("addRecord error: " + e.getMessage());
            return false;
        }
    }

    // READ ALL
    public List<Customer> getAllRecords() {
        List<Customer> CustomerList = new ArrayList<>();
        String sql = "SELECT * FROM Customer";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Customer c = new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("account_number"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("street"),
                        rs.getString("city"),
                        rs.getString("province"),
                        rs.getString("zip_code"),
                        rs.getString("contact_number"),
                        rs.getDate("created_date"),
                        rs.getString("billing_status")
                );
                CustomerList.add(c);
            }
        } catch (SQLException e) {
            System.err.println("getAllRecords error: " + e.getMessage());
        }
        return CustomerList;
    }

    // READ ONE
    public Customer getRecordById(int id) {
        String sql = "SELECT * FROM Customer WHERE customer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("account_number"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("street"),
                        rs.getString("city"),
                        rs.getString("province"),
                        rs.getString("zip_code"),
                        rs.getString("contact_number"),
                        rs.getDate("created_date"),
                        rs.getString("billing_status")
                );
            }
        } catch (Exception e) {
            System.err.println("getRecordById error: " + e.getMessage());
        }
        return null;
    }

    // UPDATE
    public boolean updateRecord(Customer customer) {
        String sql = "UPDATE Customer SET account_number=?, first_name=?, last_name=?, street=?, city=?, province=?, zip_code=?, contact_number=?, created_date=?, billing_status=? WHERE customer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.accountNumber());
            stmt.setString(2, customer.firstName());
            stmt.setString(3, customer.lastName());
            stmt.setString(4, customer.street());
            stmt.setString(5, customer.city());
            stmt.setString(6, customer.province());
            stmt.setString(7, customer.zipCode());
            stmt.setString(8, customer.contactNumber());
            stmt.setDate(9, customer.createdDate());
            stmt.setString(10, customer.billingStatus());
            stmt.setInt(11, customer.customerID());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("updateRecord error: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean deleteRecord(int id) {
        String sql = "DELETE FROM Customer WHERE customer_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("deleteRecord error: " + e.getMessage());
            return false;
        }
    }
}
