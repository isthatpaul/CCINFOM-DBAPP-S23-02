package Services;

import Database.DatabaseConnection;
import Model.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO
{

    public CustomerDAO() {}

    public void addCustomer(Customer customer) throws SQLException
    {
        // Note: Assumes customer.getCreatedDate() returns a String in 'yyyy-MM-dd' format
        String query = "INSERT INTO CUSTOMER (ACCOUNT_NUMBER, FIRST_NAME, LAST_NAME, STREET, CITY, PROVINCE, ZIP_CODE, CONTACT_NUMBER, CREATED_DATE, BILLING_STATUS)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Connection is obtained and closed automatically by try-with-resources
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, customer.getAccountNumber());
            stmt.setString(2, customer.getFirstName());
            stmt.setString(3, customer.getLastName());
            stmt.setString(4, customer.getStreet());
            stmt.setString(5, customer.getCity());
            stmt.setString(6, customer.getProvince());
            stmt.setString(7, customer.getZipCode());
            stmt.setString(8, customer.getContactNumber());
            stmt.setDate(9, customer.getCreatedDate());
            stmt.setString(10, customer.getBillingStatus());

            stmt.executeUpdate();
        }
    }

    public Customer getCustomer(int customerID) throws SQLException
    {
        String sql = "SELECT * FROM customer WHERE CUSTOMER_ID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                            rs.getInt("CUSTOMER_ID"),
                            rs.getString("ACCOUNT_NUMBER"),
                            rs.getString("FIRST_NAME"),
                            rs.getString("LAST_NAME"),
                            rs.getString("STREET"),
                            rs.getString("CITY"),
                            rs.getString("PROVINCE"),
                            rs.getString("ZIP_CODE"),
                            rs.getString("CONTACT_NUMBER"),
                            rs.getDate("CREATED_DATE"),
                            rs.getString("BILLING_STATUS")
                    );
                }
            }
        }
        return null;
    }

    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM customer";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                customers.add(new Customer(
                        rs.getInt("CUSTOMER_ID"),
                        rs.getString("ACCOUNT_NUMBER"),
                        rs.getString("FIRST_NAME"),
                        rs.getString("LAST_NAME"),
                        rs.getString("STREET"),
                        rs.getString("CITY"),
                        rs.getString("PROVINCE"),
                        rs.getString("ZIP_CODE"),
                        rs.getString("CONTACT_NUMBER"),
                        rs.getDate("CREATED_DATE"), // Retrieving as String to match model
                        rs.getString("BILLING_STATUS")
                ));
            }
        }
        return customers;
    }

    public void updateCustomer(Customer customer) throws SQLException
    {
        String query = "UPDATE CUSTOMER SET ACCOUNT_NUMBER = ?, FIRST_NAME = ?, LAST_NAME = ?, STREET = ?, CITY = ?, PROVINCE = ?, ZIP_CODE = ?, CONTACT_NUMBER = ?, CREATED_DATE = ?, BILLING_STATUS = ? WHERE CUSTOMER_ID = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query))
        {
             stmt.setString(1, customer.getAccountNumber());
             stmt.setString(2, customer.getFirstName());
             stmt.setString(3, customer.getLastName());
             stmt.setString(4, customer.getStreet());
             stmt.setString(5, customer.getCity());
             stmt.setString(6, customer.getProvince());
             stmt.setString(7, customer.getZipCode());
             stmt.setString(8, customer.getContactNumber());
             stmt.setDate(9, customer.getCreatedDate());
             stmt.setString(10, customer.getBillingStatus());
             stmt.setInt(11, customer.getCustomerID());
             stmt.executeUpdate();
        }
    }

    /**
     * Deletes a customer in a transaction, enforcing Customer Rule 3 (no deletion with active/unpaid bills).
     */
    public void deleteCustomerTransaction(int customerID) throws SQLException
    {
        // Checks for bills with STATUS IN ('UNPAID', 'PARTIALLY_PAID', 'OVERDUE')
        String checkBillsQuery = "SELECT COUNT(*) FROM BILL WHERE CUSTOMER_ID = ? AND STATUS IN ('UNPAID', 'PARTIALLY_PAID', 'OVERDUE')";
        String deleteCustomerQuery = "DELETE FROM CUSTOMER WHERE CUSTOMER_ID = ?";

        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Check for active/unpaid bills
            try (PreparedStatement checkStmt = conn.prepareStatement(checkBillsQuery))
            {
                checkStmt.setInt(1, customerID);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new SQLException("Customer has active or unpaid bills and cannot be deleted.");
                    }
                }
            }

            // 2. Delete the customer
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteCustomerQuery))
            {
                deleteStmt.setInt(1, customerID);
                int rowsAffected = deleteStmt.executeUpdate();
                if (rowsAffected == 0)
                {
                    throw new SQLException("Customer not found or no rows deleted.");
                }
            }
            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // Rollback on error
            }
            throw e;
        } finally {
            if (conn != null) {
                // Must ensure connection is returned to the pool (closed)
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
}