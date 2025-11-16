package Model.DAO;

import Database.DatabaseConnection;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class CustomerReportDAO {

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
}
