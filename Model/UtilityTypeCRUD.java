package Model;

import Database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilityTypeCRUD
{
    // CREATE
    public boolean addRecord(UtilityType utilityType) {
        String sql = "INSERT INTO UtilityType (UtilityTypeName, Description, UnitOfMeasure, CreatedDate, ModifiedDate, IsActive) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt  = conn.prepareStatement(sql))
        {
            stmt.setString(1, utilityType.utilityTypeName());
            stmt.setString(2, utilityType.description());
            stmt.setString(3, utilityType.unitOfMeasure());
            stmt.setDate(4, utilityType.createdDate());
            stmt.setDate(5, utilityType.modifiedDate());
            stmt.setBoolean(6, utilityType.isActive());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("UtilityType addRecord error: " + e.getMessage());
            return false;
        }
    }

    // READ ALL
    public List<UtilityType> getAllRecords() {
        List<UtilityType> utilityTypeList = new ArrayList<>();
        String sql = "SELECT * FROM UtilityType";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                UtilityType ut = new UtilityType(
                        rs.getInt("UtilityTypeID"),
                        rs.getString("UtilityTypeName"),
                        rs.getString("Description"),
                        rs.getString("UnitOfMeasure"),
                        rs.getDate("CreatedDate"),
                        rs.getDate("ModifiedDate"),
                        rs.getBoolean("IsActive")
                );
                utilityTypeList.add(ut);
            }
        } catch (SQLException e) {
            System.err.println("UtilityType getAllRecords error: " + e.getMessage());
        }
        return utilityTypeList;
    }

    // READ ONE
    public UtilityType getRecordById(int id) {
        String sql = "SELECT * FROM UtilityType WHERE UtilityTypeID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new UtilityType(
                            rs.getInt("UtilityTypeID"),
                            rs.getString("UtilityTypeName"),
                            rs.getString("Description"),
                            rs.getString("UnitOfMeasure"),
                            rs.getDate("CreatedDate"),
                            rs.getDate("ModifiedDate"),
                            rs.getBoolean("IsActive")
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("UtilityType getRecordById error: " + e.getMessage());
        }
        return null;
    }

    // UPDATE
    public boolean updateRecord(UtilityType utilityType) {
        String sql = "UPDATE UtilityType SET UtilityTypeName = ?, Description = ?, UnitOfMeasure = ?, CreatedDate = ?, ModifiedDate = ?, IsActive = ? WHERE UtilityTypeID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, utilityType.utilityTypeName());
            stmt.setString(2, utilityType.description());
            stmt.setString(3, utilityType.unitOfMeasure());
            stmt.setDate(4, utilityType.createdDate());
            stmt.setDate(5, utilityType.modifiedDate());
            stmt.setBoolean(6, utilityType.isActive());
            stmt.setInt(7, utilityType.utilityTypeID());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("UtilityType updateRecord error: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean deleteRecord(int id) {
        String sql = "DELETE FROM UtilityType WHERE UtilityTypeID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("UtilityType deleteRecord error: " + e.getMessage());
            return false;
        }
    }
}