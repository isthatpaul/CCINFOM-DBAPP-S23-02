package Model;

import Database.DatabaseConnection;
import Model.UtilityType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilityTypeCRUD
{
    // CREATE
    public boolean addRecord(UtilityType utilityType) {
        String sql = "INSERT INTO REF_UTILITY_TYPE (UTILITY_TYPE_NAME, DESCRIPTION, UNIT_OF_MEASURE, CREATED_DATE, MODIFIED_DATE, IS_ACTIVE) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt  = conn.prepareStatement(sql))
        {
            stmt.setString(1, utilityType.getUtilityTypeName());
            stmt.setString(2, utilityType.getDescription());
            stmt.setString(3, utilityType.getUnitOfMeasure());
            stmt.setDate(4, utilityType.getCreatedDate());
            stmt.setDate(5, utilityType.getModifiedDate());
            stmt.setBoolean(6, utilityType.isActive());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("addRecord error: " + e.getMessage());
            return false;
        }
    }

    // READ ALL
    public List<UtilityType> getAllRecords() {
        List<UtilityType> utilityTypeList = new ArrayList<>();
        String sql = "SELECT * FROM REF_UTILITY_TYPE";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                UtilityType ut = new UtilityType(
                        rs.getInt("Utility_Type_ID"),
                        rs.getString("Utility_Type_Name"),
                        rs.getString("Description"),
                        rs.getString("unit_of_measure"),
                        rs.getDate("created_date"),
                        rs.getDate("modified_date"),
                        rs.getBoolean("is_active")
                );
                utilityTypeList.add(ut);
            }
        } catch (SQLException e) {
            System.err.println("getAllRecords error: " + e.getMessage());
        }
        return utilityTypeList;
    }

    // READ ONE
    public UtilityType getRecordById(int id) {
        String sql = "SELECT * FROM REF_UTILITY_TYPE WHERE utility_type_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new UtilityType(
                        rs.getInt("utility_type_id"),
                        rs.getString("utility_type_name"),
                        rs.getString("Description"),
                        rs.getString("unit_of_measure"),
                        rs.getDate("created_date"),
                        rs.getDate("modified_date"),
                        rs.getBoolean("is_active")
                );
            }
        } catch (Exception e) {
            System.err.println("getRecordById error: " + e.getMessage());
        }
        return null;
    }

    // UPDATE
    public boolean updateRecord(UtilityType utilityType) {
        String sql = "UPDATE REF_UTILITY_TYPE SET utility_type_id=?, utility_type_name=?, description=?, unit_of_measure=?, created_date=?, modified_date=? WHERE utility_type_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, utilityType.getUtilityTypeName());
            stmt.setString(2, utilityType.getDescription());
            stmt.setString(3, utilityType.getUnitOfMeasure());
            stmt.setDate(4, utilityType.getCreatedDate());
            stmt.setDate(5, utilityType.getModifiedDate());
            stmt.setInt(6, utilityType.getUtilityTypeID());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("updateRecord error: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean deleteRecord(int id) {
        String sql = "DELETE FROM REF_UTILITY_TYPE WHERE utility_type_id=?";
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
