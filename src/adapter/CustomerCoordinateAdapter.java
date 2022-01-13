package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.Customer.mdlCustomerCoordinate;
import model.Query.mdlQueryExecute;
import model.DataTable.mdlDataTableParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class CustomerCoordinateAdapter {
    final static Logger logger = LogManager.getLogger(CustomerCoordinateAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlCustomerCoordinate> GetCustomerCoordinateCheck(mdlCustomerCoordinate param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String user = param.customer_id;
        List<mdlCustomerCoordinate> _mdlCustomerCoordinateList = new ArrayList<mdlCustomerCoordinate>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        boolean resultExec = false;
        try {
            sql = "{call sp_customer_coordinate_get(?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.customer_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.seq));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            while (rowset.next()) {
                model.Customer.mdlCustomerCoordinate _mdlCustomerCoordinate = new model.Customer.mdlCustomerCoordinate();
                _mdlCustomerCoordinate.customer_id = rowset.getString("customer_id");
                _mdlCustomerCoordinate.seq = rowset.getString("seq");
                _mdlCustomerCoordinate.customer_address = rowset.getString("customer_address");
                _mdlCustomerCoordinate.city = rowset.getString("city");
                _mdlCustomerCoordinate.radius = rowset.getString("radius");
                _mdlCustomerCoordinate.country_region_code = rowset.getString("country_region_code");
                _mdlCustomerCoordinate.longitude = rowset.getString("longitude");
                _mdlCustomerCoordinate.latitude = rowset.getString("latitude");
                _mdlCustomerCoordinate.is_active = rowset.getString("is_active");
                _mdlCustomerCoordinate.created_by = rowset.getString("created_by");
                _mdlCustomerCoordinate.created_date = rowset.getString("created_date");
                _mdlCustomerCoordinate.updated_by = rowset.getString("updated_by");
                _mdlCustomerCoordinate.updated_date = rowset.getString("updated_date");
                _mdlCustomerCoordinateList.add(_mdlCustomerCoordinate);
            }
        } catch (Exception ex) {
            _mdlCustomerCoordinateList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlCustomerCoordinateList;
    }

    public static List<mdlCustomerCoordinate> GetCustomerCoordinateWeb(mdlDataTableParam param, String customerID, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<model.Customer.mdlCustomerCoordinate> _mdlCustomerCoordinateList = new ArrayList<>();
        CachedRowSet jrs = null;
        String sql = "";
        String search_part = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<>();
        try {
            sql = "{call sp_customer_coordinate_get_with_paging_v4(?,?,?,?,?,?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", customerID));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.name));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.dir));

            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Customer Coordinate List", db_name, port);
            while (jrs.next()) {
                model.Customer.mdlCustomerCoordinate _mdlCustomerCoordinate = new model.Customer.mdlCustomerCoordinate();
                _mdlCustomerCoordinate.customer_id = jrs.getString("customer_id");
                _mdlCustomerCoordinate.seq = jrs.getString("seq");
                _mdlCustomerCoordinate.customer_address = jrs.getString("customer_address");
                _mdlCustomerCoordinate.longitude = jrs.getString("longitude");
                _mdlCustomerCoordinate.latitude = jrs.getString("latitude");
                _mdlCustomerCoordinate.created_by = jrs.getString("created_by");
                _mdlCustomerCoordinate.created_date = jrs.getString("created_date");
                _mdlCustomerCoordinate.updated_by = jrs.getString("updated_by");
                _mdlCustomerCoordinate.updated_date = jrs.getString("updated_date");
                _mdlCustomerCoordinate.is_active = jrs.getString("is_active");
                _mdlCustomerCoordinate.radius = jrs.getString("radius");
                _mdlCustomerCoordinate.city = jrs.getString("city");
                _mdlCustomerCoordinate.country_region_code = jrs.getString("country_region_code");
                _mdlCustomerCoordinateList.add(_mdlCustomerCoordinate);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "Customer Coordinate List", db_name), ex);
        }
        return _mdlCustomerCoordinateList;
    }

    public static int GetCustomerCoordinateTotalList(mdlDataTableParam param, String customerID, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;
        String sql = "";
        int returnValue = 0;
        String search_part = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        try {
            sql = "{call sp_customer_coordinate_get_total_list_v2(?,?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", customerID));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));

            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Customer Coordinate Total", db_name, port);
            while (crs.next()) {
                returnValue = crs.getInt("total");
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "Customer Coordinate Total", db_name), ex);
        }
        return returnValue;
    }

    // Upload Customer Type
    public static boolean UploadCustomerCoordinate(mdlCustomerCoordinate param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_customer_coordinate_upload(?,?,?,?,?,?,?,?,?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.customer_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.seq));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.customer_address));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", helper.NullHelper.NulltoZero(param.radius)));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", helper.NullHelper.NulltoStringEmpty(param.city)));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", helper.NullHelper.NulltoStringEmpty(param.country_region_code)));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.latitude));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.longitude));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.updated_by));

            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean DeleteCustomerCoordinate(mdlCustomerCoordinate param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = "Customer Coordinate";
        String sql = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_customer_coordinate_delete_v2(?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.customer_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.seq));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean UpdateStatusCustomerCoordinate(mdlCustomerCoordinate param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";
        List<model.Query.mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_customer_coodinate_update_status(?,?,?)}";
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.customer_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.is_active));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.created_by));

            resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listMdlQueryExecute), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static List<mdlCustomerCoordinate> GetDropDownCustomerCoordinateByCustomerID(mdlCustomerCoordinate param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlCustomerCoordinate> mdlCustomerCoordinateList = new ArrayList<mdlCustomerCoordinate>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_dropdown_customer_coordinate_by_customer_id_get(?)}";
            listParam.add(QueryAdapter.QueryParam("string", param.customer_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "role", db_name, port);
            while (rowset.next()) {
                mdlCustomerCoordinate _mdlCustomerCoordinate = new mdlCustomerCoordinate();
                _mdlCustomerCoordinate.customer_id = rowset.getString("customer_id");
                _mdlCustomerCoordinate.customer_address = rowset.getString("customer_address");
                mdlCustomerCoordinateList.add(_mdlCustomerCoordinate);
            }
        } catch (Exception ex) {
            mdlCustomerCoordinateList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), "role", db_name), ex);
        }
        return mdlCustomerCoordinateList;
    }

    public static List<mdlCustomerCoordinate> GetDropDownCustomerCoordinateByCustomerIDSeq(mdlCustomerCoordinate param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlCustomerCoordinate> mdlCustomerCoordinateList = new ArrayList<mdlCustomerCoordinate>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_dropdown_customer_coordinate_by_customer_id_get_seq(?)}";
            listParam.add(QueryAdapter.QueryParam("string", param.customer_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "role", db_name, port);
            while (rowset.next()) {
                mdlCustomerCoordinate _mdlCustomerCoordinate = new mdlCustomerCoordinate();
                _mdlCustomerCoordinate.seq = rowset.getString("seq");
                _mdlCustomerCoordinate.customer_address = rowset.getString("customer_address");
                mdlCustomerCoordinateList.add(_mdlCustomerCoordinate);
            }
        } catch (Exception ex) {
            mdlCustomerCoordinateList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), "role", db_name), ex);
        }
        return mdlCustomerCoordinateList;
    }
}
