package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.Warehouse.mdlWarehouse;
import model.DataTable.mdlDataTableParam;
import model.Query.mdlQueryExecute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class WarehouseAdapter {
    final static Logger logger = LogManager.getLogger(WarehouseAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlWarehouse> GetWarehouseWeb(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();

        String sql = "";
        List<mdlWarehouse> _mdlWarehouseList = new ArrayList<mdlWarehouse>();
        List<mdlQueryExecute> _mdlQueryExecute = new ArrayList<mdlQueryExecute>();
        CachedRowSet jrs = null;
        try {
            sql = "{call sp_warehouse_get_with_paging(?,?,?)}";

            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecute.add(QueryAdapter.QueryParam("string", searchString));
            _mdlQueryExecute.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));

            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecute, functionName, "warehouse", db_name, port);
            while (jrs.next()) {
                mdlWarehouse _mdlWarehouse = new mdlWarehouse();
                _mdlWarehouse.warehouse_id = jrs.getString("warehouse_id");
                _mdlWarehouse.branch_id = jrs.getString("branch_id");
                _mdlWarehouse.warehouse_name = jrs.getString("warehouse_name");
                _mdlWarehouse.warehouse_address = jrs.getString("warehouse_address");
                _mdlWarehouse.post_code = jrs.getString("post_code");
                _mdlWarehouse.city = jrs.getString("city");
                _mdlWarehouse.country = jrs.getString("country");
                _mdlWarehouse.phone = jrs.getString("phone");
                _mdlWarehouse.contact = jrs.getString("contact");
                _mdlWarehouse.latitude = jrs.getString("latitude");
                _mdlWarehouse.longitude = jrs.getString("longitude");
                _mdlWarehouse.customer_id = jrs.getString("customer_id");
                _mdlWarehouse.radius = jrs.getString("radius");
                _mdlWarehouseList.add(_mdlWarehouse);
            }
        } catch (Exception ex) {
            _mdlWarehouseList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(param), "", ex.toString(), "warehouse", db_name), ex);
        }

        return _mdlWarehouseList;
    }

    public static int GetWarehouseList(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();

        int returnValue = 0;
        String sql = "";
        CachedRowSet crs = null;
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_warehouse_get_total_list(?)}";

            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));

            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "warehouse", db_name, port);
            while (crs.next()) {
                returnValue = crs.getInt("total");
            }

        } catch (Exception ex) {
            returnValue = 0;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(param), "", ex.toString(), "warehouse", db_name), ex);
        }
        return returnValue;
    }
}
