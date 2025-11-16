package Model.DAO;

import Database.DatabaseConnection;
import Reports.CustomerAccountReport;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerReportDAO {

    // Existing simple count-by-branch method (kept, unchanged signature)
    public Map<String, Integer> getCustomerCountPerBranch(int month, int year) {
        Map<String, Integer> branchCustomerCount = new HashMap<>();
        String sql = """
            SELECT s.AssignedBranch, COUNT(DISTINCT m.CustomerID) AS totalCustomers
            FROM MeterAssignment m
            JOIN Staff s ON m.AssignedByStaffID = s.StaffID
            WHERE MONTH(m.AssignmentDate) = ? AND YEAR(m.AssignmentDate) = ?
            GROUP BY s.AssignedBranch
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, month);
            ps.setInt(2, year);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    branchCustomerCount.put(rs.getString("AssignedBranch"), rs.getInt("totalCustomers"));
                }
            }

        } catch (SQLException e) {
            System.err.println("CustomerReportDAO error: " + e.getMessage());
        }

        return branchCustomerCount;
    }

    public List<CustomerAccountReport> getCustomerAccountReport(Date startInclusive, Date endInclusive) {
        List<CustomerAccountReport> results = new ArrayList<>();
        String sql = """
            SELECT c.CustomerID,
                   CONCAT(c.FirstName, ' ', c.LastName) AS customerName,
                   s.AssignedBranch,
                   m.MeterSerialNumber,
                   m.MeterStatus
            FROM MeterAssignment ma
            JOIN Customer c ON ma.CustomerID = c.CustomerID
            JOIN Staff s ON ma.AssignedByStaffID = s.StaffID
            LEFT JOIN Meter m ON ma.MeterID = m.MeterID
            WHERE ma.AssignmentDate BETWEEN ? AND ?
              AND c.BillingStatus = 'ACTIVE'
            ORDER BY s.AssignedBranch, c.CustomerID
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, startInclusive);
            ps.setDate(2, endInclusive);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CustomerAccountReport row = new CustomerAccountReport(
                            rs.getInt("CustomerID"),
                            rs.getString("customerName"),
                            rs.getString("AssignedBranch"),
                            rs.getString("MeterSerialNumber"),
                            rs.getString("MeterStatus")
                    );
                    results.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("CustomerReportDAO getCustomerAccountReport error: " + e.getMessage());
        }

        return results;
    }
}