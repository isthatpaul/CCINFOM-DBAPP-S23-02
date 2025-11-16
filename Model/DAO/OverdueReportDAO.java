package Model.DAO;

import Database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OverdueReportDAO {

    // Get all overdue bills for a specific month and year
    public List<Integer> getOverdueBills(int month, int year) {
        List<Integer> overdueBillIDs = new ArrayList<>();
        String sql = """
            SELECT b.BillID
            FROM Bill b
            LEFT JOIN Payment p ON b.BillID = p.BillID
            WHERE b.Status != 'PAID'
            AND MONTH(b.DueDate) = ?
            AND YEAR(b.DueDate) = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, month);
            ps.setInt(2, year);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    overdueBillIDs.add(rs.getInt("BillID"));
                }
            }

        } catch (SQLException e) {
            System.err.println("OverdueReportDAO error: " + e.getMessage());
        }

        return overdueBillIDs;
    }
}
