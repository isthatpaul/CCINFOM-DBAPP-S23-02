package Services;

import Database.DatabaseConnection;
import Model.Meter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MeterDAO
{
    public MeterDAO() {}

    public void addMeter(Meter meter) throws SQLException
    {
        String query = "INSERT INTO meter (UTILITY_TYPE_ID, METER_SERIAL_NUMBER, METER_STATUS)" +
                       "VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, meter.getUtilityTypeId());
            stmt.setString(2, meter.getMeterSerialNumber());
            stmt.setString(3, meter.getMeterStatus());

            stmt.executeUpdate();
        }
    }
}
