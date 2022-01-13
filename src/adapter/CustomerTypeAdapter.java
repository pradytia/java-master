package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.Customer.mdlCustomerType;
import model.Download.mdlDownloadParam;
import model.Globals;
import model.Query.mdlQueryExecute;
import model.DataTable.mdlDataTableParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class CustomerTypeAdapter {
    final static Logger logger = LogManager.getLogger(CustomerTypeAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlCustomerType> GetCustomerTypeCheck(mdlCustomerType param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String user = param.customer_type_id;
        List<mdlCustomerType> _mdlCustomerTypeList = new ArrayList<mdlCustomerType>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        boolean resultExec = false;
        try {
            sql = "{call sp_customer_type_get(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.customer_type_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            while (rowset.next()) {
                model.Customer.mdlCustomerType _mdlCustomerType = new model.Customer.mdlCustomerType();
                _mdlCustomerType.customer_type_id = rowset.getString("customer_type_id");
                _mdlCustomerType.customer_type_name = rowset.getString("customer_type_name");
                _mdlCustomerType.description = rowset.getString("description");
                _mdlCustomerType.is_active = rowset.getString("is_active");
                _mdlCustomerType.created_by = rowset.getString("created_by");
                _mdlCustomerType.created_date = rowset.getString("created_date");
                _mdlCustomerType.updated_by = rowset.getString("updated_by");
                _mdlCustomerType.updated_date = rowset.getString("updated_date");
                _mdlCustomerTypeList.add(_mdlCustomerType);
            }
        } catch (Exception ex) {
            _mdlCustomerTypeList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlCustomerTypeList;
    }

    public static List<mdlCustomerType> GetCustomerType(mdlDownloadParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String user = param.employee_id;
        List<model.Customer.mdlCustomerType> _mdlCustomerTypeList = new ArrayList<mdlCustomerType>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp__mdlCustomerTypeList()}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            while (rowset.next()) {
                model.Customer.mdlCustomerType _mdlCustomerType = new model.Customer.mdlCustomerType();
                _mdlCustomerType.setCustomer_type_id(rowset.getString("customerTypeID("));
                _mdlCustomerType.setCustomer_type_name(rowset.getString("customerTypeName"));
                _mdlCustomerType.setDescription(rowset.getString("description"));
                _mdlCustomerTypeList.add(_mdlCustomerType);
            }
        } catch (Exception ex) {
            _mdlCustomerTypeList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlCustomerTypeList;
    }

    public static List<mdlCustomerType> GetCustomerTypeWeb(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<model.Customer.mdlCustomerType> _mdlCustomerTypeList = new ArrayList<>();
        CachedRowSet jrs = null;
        String sql = "";
        String search_part = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<>();
        try {
            sql = "{call sp_customer_type_get_with_paging_v2(?,?,?,?,?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.name));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.dir));
            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Customer Type List", db_name, port,Globals.ip, Globals.login,Globals.pass);
            while (jrs.next()) {
                model.Customer.mdlCustomerType _mdlCustomerType = new model.Customer.mdlCustomerType();
                _mdlCustomerType.customer_type_id = jrs.getString("customer_type_id");
                _mdlCustomerType.customer_type_name = jrs.getString("customer_type_name");
                _mdlCustomerType.description = jrs.getString("description");
                _mdlCustomerType.created_by = jrs.getString("created_by");
                _mdlCustomerType.created_date = jrs.getString("created_date");
                _mdlCustomerType.updated_by = jrs.getString("updated_by");
                _mdlCustomerType.updated_date = jrs.getString("updated_date");
                _mdlCustomerType.is_active = jrs.getString("is_active");
                _mdlCustomerTypeList.add(_mdlCustomerType);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "", db_name), ex);
        }
        return _mdlCustomerTypeList;
    }

    public static int GetCustomerTypeTotalList(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;
        String sql = "";
        int returnValue = 0;
        String search_part = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        try {
            sql = "{call sp_customer_type_get_total_list(?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Customer Type Total", db_name, port,Globals.ip, Globals.login,Globals.pass);

            while (crs.next()) {
                returnValue = crs.getInt("total");
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "", db_name), ex);
        }
        return returnValue;
    }

    // Upload Customer Type
    public static boolean UploadCustomerType(mdlCustomerType param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_customer_type_upload(?,?,?,?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.customer_type_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.customer_type_name));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.description));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.updated_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean DeleteCustomerType(mdlCustomerType param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = "Customer Type";
        String sql = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_customer_type_delete(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.customer_type_id));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static List<mdlCustomerType> GetDropDownCustomerType(String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlCustomerType> mdlCustomerTypeList = new ArrayList<mdlCustomerType>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_customer_type_dropdown_get}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "customer_type", db_name, port, Globals.ip,Globals.login,Globals.pass);
            while (rowset.next()) {
                mdlCustomerType _mdlCustomerType = new mdlCustomerType();
                _mdlCustomerType.customer_type_id = rowset.getString("customer_type_id");
                _mdlCustomerType.customer_type_name = rowset.getString("customer_type_name");
                mdlCustomerTypeList.add(_mdlCustomerType);
            }
        } catch (Exception ex) {
            mdlCustomerTypeList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", "", "", ex.toString(), "customer_type", db_name), ex);
        }
        return mdlCustomerTypeList;
    }
}
