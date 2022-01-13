package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.DataTable.mdlDataTableParam;
import model.Query.mdlQueryExecute;
import model.Reason.mdlReason;
import model.Download.mdlDownloadParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class ReasonAdapter {
    final static Logger logger = LogManager.getLogger(ReasonAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlReason> GetReasonCheck(mdlReason param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param.reason_id;

        List<mdlReason> _mdlReasonList = new ArrayList<mdlReason>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_reason_get(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.reason_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

            while (rowset.next()) {
                mdlReason _mdlReason = new mdlReason();
                _mdlReason.setReason_id(rowset.getString("reason_id"));
                _mdlReason.setReason_type(rowset.getString("reason_type"));
                _mdlReason.setValue(rowset.getString("value"));
                _mdlReason.setCreated_by(rowset.getString("created_by"));
                _mdlReason.setCreated_date(rowset.getString("created_date"));
                _mdlReason.setUpdated_by(rowset.getString("updated_by"));
                _mdlReason.setUpdated_date(rowset.getString("updated_date"));
                _mdlReasonList.add(_mdlReason);

            }
        } catch (Exception ex) {
            _mdlReasonList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlReasonList;
    }

    public static List<model.Reason.mdlReason> GetReason(mdlDownloadParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param.employee_id;

        List<model.Reason.mdlReason> _mdlReasonList = new ArrayList<mdlReason>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_reason_list()}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

            while (rowset.next()) {
                model.Reason.mdlReason _mdlReason = new model.Reason.mdlReason();
                _mdlReason.setReason_id(rowset.getString("reason_id"));
                _mdlReason.setReason_type(rowset.getString("reason_type"));
                _mdlReason.setValue(rowset.getString("value"));
                _mdlReason.setCreated_by(rowset.getString("created_by"));
                _mdlReason.setCreated_date(rowset.getString("created_date"));
                _mdlReason.setUpdated_by(rowset.getString("updated_by"));
                _mdlReason.setUpdated_date(rowset.getString("updated_date"));
                _mdlReasonList.add(_mdlReason);
            }
        } catch (Exception ex) {
            _mdlReasonList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlReasonList;
    }

    public static List<mdlReason> GetReasonWeb(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<model.Reason.mdlReason> _mdlReasonList = new ArrayList<>();
        CachedRowSet jrs = null;
        String sql = "";
        String user = param.module_id;
        String search_part = "";

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<>();
        try {
            sql = "{call sp_reason_get_with_paging_v2(?,?,?,?,?)}";
            String search_string = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", search_string));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.name));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.dir));

            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Reason", db_name, port);

            while (jrs.next()) {
                model.Reason.mdlReason _mdlReason = new model.Reason.mdlReason();
                _mdlReason.reason_id = jrs.getString("reason_id");
                _mdlReason.reason_type = jrs.getString("reason_type");
                _mdlReason.value = jrs.getString("value");
                _mdlReason.created_by = jrs.getString("created_by");
                _mdlReason.created_date = jrs.getString("created_date");
                _mdlReason.updated_by = jrs.getString("updated_by");
                _mdlReason.updated_date = jrs.getString("updated_date");
                _mdlReason.is_active = jrs.getString("is_active");
                _mdlReasonList.add(_mdlReason);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlReasonList;
    }

    public static int GetReasonTotal(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;
        String sql = "";
        String user = param.module_id;
        int return_value = 0;
        String search_part = "";

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        try {
            sql = "{call sp_reason_get_total_list(?)}";
            String search_string = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", search_string));

            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "reason total", db_name, port);
            while (crs.next()) {
                return_value = crs.getInt("total");
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return return_value;
    }

    // Upload Reason
    public static boolean UploadReason(mdlReason reason_param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = reason_param.created_by;
        String sql = "";

        List<model.Query.mdlQueryExecute> list_mdl_query_execute = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_reason_upload(?,?,?,?,?)}";
            list_mdl_query_execute.add(QueryAdapter.QueryParam("string", reason_param.reason_id));
            list_mdl_query_execute.add(QueryAdapter.QueryParam("string", reason_param.reason_type));
            list_mdl_query_execute.add(QueryAdapter.QueryParam("string", reason_param.value));
            list_mdl_query_execute.add(QueryAdapter.QueryParam("string", reason_param.created_by));
            list_mdl_query_execute.add(QueryAdapter.QueryParam("string", reason_param.updated_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, list_mdl_query_execute, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(list_mdl_query_execute), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean UpdateStatusReason(mdlReason reason_param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = reason_param.created_by;
        String sql = "";

        List<model.Query.mdlQueryExecute> list_mdl_query_execute = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_reason_update_status(?,?,?)}";
            list_mdl_query_execute.add(QueryAdapter.QueryParam("string", reason_param.reason_id));
            list_mdl_query_execute.add(QueryAdapter.QueryParam("string", reason_param.is_active));
            list_mdl_query_execute.add(QueryAdapter.QueryParam("string", reason_param.created_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, list_mdl_query_execute, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(list_mdl_query_execute), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean DeleteReason(mdlReason param, String db_name, int port) {

        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_reason_delete(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.reason_id));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }
}
