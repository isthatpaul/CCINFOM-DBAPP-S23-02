package Model;

import Database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OverdueNoticeCRUD
{
    // CREATE
    public boolean addRecord(OverdueNotice notice) {
        String sql = "INSERT INTO OverdueNotice (BillID, OverdueDate, PenaltyAmount, NoticeDate, EscalationStatus, SentByStaffID) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, notice.billID());
            ps.setDate(2, notice.overdueDate());
            ps.setDouble(3, notice.penaltyAmount());
            ps.setDate(4, notice.noticeDate());
            ps.setString(5, notice.escalationStatus());
            ps.setInt(6, notice.sentByStaffID());
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
        String sql = "SELECT * FROM OverdueNotice";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                OverdueNotice on = new OverdueNotice(
                        rs.getInt("NoticeID"),
                        rs.getInt("BillID"),
                        rs.getDate("OverdueDate"),
                        rs.getDouble("PenaltyAmount"),
                        rs.getDate("NoticeDate"),
                        rs.getString("EscalationStatus"),
                        rs.getInt("SentByStaffID")
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
        String sql = "SELECT * FROM OverdueNotice WHERE NoticeID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, noticeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new OverdueNotice(
                            rs.getInt("NoticeID"),
                            rs.getInt("BillID"),
                            rs.getDate("OverdueDate"),
                            rs.getDouble("PenaltyAmount"),
                            rs.getDate("NoticeDate"),
                            rs.getString("EscalationStatus"),
                            rs.getInt("SentByStaffID")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("OverdueNotice getRecordById error: " + e.getMessage());
        }
        return null;
    }

    // UPDATE
    public boolean updateRecord(OverdueNotice notice) {
        String sql = "UPDATE OverdueNotice SET BillID = ?, OverdueDate = ?, PenaltyAmount = ?, NoticeDate = ?, EscalationStatus = ?, SentByStaffID = ? WHERE NoticeID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, notice.billID());
            ps.setDate(2, notice.overdueDate());
            ps.setDouble(3, notice.penaltyAmount());
            ps.setDate(4, notice.noticeDate());
            ps.setString(5, notice.escalationStatus());
            ps.setInt(6, notice.sentByStaffID());
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
        String sql = "DELETE FROM OverdueNotice WHERE NoticeID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, noticeId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("OverdueNotice deleteRecord error: " + e.getMessage());
            return false;
        }
    }
}