package Model;

import Database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OverdueNoticeCRUD
{
    // CREATE
    public boolean addRecord(OverdueNotice notice) {
        String sql = "INSERT INTO OVERDUE_NOTICE (BILL_ID, OVERDUE_DATE, PENALTY_AMOUNT, NOTICE_DATE, ESCALATION_STATUS, SENT_BY_USER_ID) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, notice.billID());
            ps.setDate(2, notice.overdueDate());
            ps.setDouble(3, notice.penaltyAmount());
            ps.setDate(4, notice.noticeDate());
            ps.setString(5, notice.escalationStatus());
            ps.setInt(6, notice.sentByUserID());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("OverdueNotice addRecord error: " + e.getMessage());
            return false;
        }
    }

    // READ ALL
    public List<OverdueNotice> getAllRecords() {
        List<OverdueNotice> list = new ArrayList<>();
        String sql = "SELECT * FROM OVERDUE_NOTICE";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                OverdueNotice on = new OverdueNotice(
                        rs.getInt("NOTICE_ID"),
                        rs.getInt("BILL_ID"),
                        rs.getDate("Overdue_date"),
                        rs.getDouble("PENALTY_AMOUNT"),
                        rs.getDate("notice_date"),
                        rs.getString("escalation_status"),
                        rs.getInt("sent_by_user_id")
                );
                list.add(on);
            }
        } catch (SQLException e) {
            System.err.println("OverdueNotice getAllRecords error: " + e.getMessage());
        }
        return list;
    }

    // READ ONE
    public OverdueNotice getRecordById(int noticeId) {
        String sql = "SELECT * FROM OVERDUE_NOTICE WHERE NOTICE_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, noticeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new OverdueNotice(
                            rs.getInt("NOTICE_ID"),
                            rs.getInt("BILL_ID"),
                            rs.getDate("Overdue_date"),
                            rs.getDouble("PENALTY_AMOUNT"),
                            rs.getDate("notice_date"),
                            rs.getString("escalation_status"),
                            rs.getInt("sent_by_user_id")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("OverdueNotice getRecordById error: " + e.getMessage());
        }
        return null;
    }

    // UPDATE is typically not done for notices, but included for completeness.
    public boolean updateRecord(OverdueNotice notice) {
        String sql = "UPDATE OVERDUE_NOTICE SET BILL_ID = ?, OVERDUE_DATE = ?, PENALTY_AMOUNT = ?, escalation_status = ?, sent_by_user_id = ? WHERE NOTICE_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, notice.billID());
            ps.setDate(2, notice.overdueDate());
            ps.setDouble(3, notice.penaltyAmount());
            ps.setDate(4, notice.noticeDate());
            ps.setString(5, notice.escalationStatus());
            ps.setInt(6, notice.sentByUserID());
            ps.setInt(7, notice.noticeID());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("OverdueNotice updateRecord error: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean deleteRecord(int noticeId) {
        String sql = "DELETE FROM OVERDUE_NOTICE WHERE NOTICE_ID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, noticeId);
            return true;
        } catch (SQLException e) {
            System.err.println("OverdueNotice deleteRecord error: " + e.getMessage());
            return false;
        }
    }
}