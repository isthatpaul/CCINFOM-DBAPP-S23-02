package Model;

import Database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerCRUD
{
    // CREATE
    public boolean addRecord(Customer customer) {
        String sql = "INSERT INTO Customer (AccountNumber, FirstName, LastName, Street, City, Province, ZipCode, ContactNumber, CreatedDate, BillingStatus) VALUES (?, ?, ?, ?, ?, ?, ? , ?, ?, ?)";
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
            System.err.println("Customer addRecord error: " + e.getMessage());
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
                        rs.getInt("CustomerID"),
                        rs.getString("AccountNumber"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Street"),
                        rs.getString("City"),
                        rs.getString("Province"),
                        rs.getString("ZipCode"),
                        rs.getString("ContactNumber"),
                        rs.getDate("CreatedDate"),
                        rs.getString("BillingStatus")
                );
                CustomerList.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Customer getAllRecords error: " + e.getMessage());
        }
        return CustomerList;
    }

    // READ ONE
    public Customer getRecordById(int id) {
        String sql = "SELECT * FROM Customer WHERE CustomerID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                            rs.getInt("CustomerID"),
                            rs.getString("AccountNumber"),
                            rs.getString("FirstName"),
                            rs.getString("LastName"),
                            rs.getString("Street"),
                            rs.getString("City"),
                            rs.getString("Province"),
                            rs.getString("ZipCode"),
                            rs.getString("ContactNumber"),
                            rs.getDate("CreatedDate"),
                            rs.getString("BillingStatus")
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("Customer getRecordById error: " + e.getMessage());
        }
        return null;
    }

    public Customer getCustomerByMeterId(int meterId) {
        String sql = "SELECT c.* FROM Customer c JOIN MeterAssignment ma ON c.CustomerID = ma.CustomerID WHERE ma.MeterID = ? AND ma.Status = 'Active'";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, meterId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                            rs.getInt("CustomerID"),
                            rs.getString("AccountNumber"),
                            rs.getString("FirstName"),
                            rs.getString("LastName"),
                            rs.getString("Street"),
                            rs.getString("City"),
                            rs.getString("Province"),
                            rs.getString("ZipCode"),
                            rs.getString("ContactNumber"),
                            rs.getDate("CreatedDate"),
                            rs.getString("BillingStatus")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // UPDATE
    public boolean updateRecord(Customer customer) {
        String sql = "UPDATE Customer SET AccountNumber = ?, FirstName = ?, LastName = ?, Street = ?, City = ?, Province = ?, ZipCode = ?, ContactNumber = ?, CreatedDate = ?, BillingStatus = ? WHERE CustomerID = ?";
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
            System.err.println("Customer updateRecord error: " + e.getMessage());
            return false;
        }
    }


    // DELETE
    public boolean deleteRecord(int id) {
        String sql = "DELETE FROM Customer WHERE CustomerID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Customer deleteRecord error: " + e.getMessage());
            return false;
        }
    }
}