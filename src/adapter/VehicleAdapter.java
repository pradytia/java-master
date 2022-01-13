package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.Vehicle.mdlVehicle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class VehicleAdapter {
    final static Logger logger = LogManager.getLogger(VehicleAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlVehicle> GetDropDownVehicle(String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlVehicle> mdlVehicleList = new ArrayList<mdlVehicle>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_dropdown_vehicle_get}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "role", db_name, port);
            while (rowset.next()) {
                mdlVehicle mdlvehicle = new mdlVehicle();
                mdlvehicle.vehicle_id = rowset.getString("vehicle_id");
                mdlvehicle.vehicle_name = rowset.getString("vehicle_name");
                mdlVehicleList.add(mdlvehicle);
            }
        } catch (Exception ex) {
            mdlVehicleList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, "", "", ex.toString(), "", db_name), ex);
        }
        return mdlVehicleList;
    }
}
