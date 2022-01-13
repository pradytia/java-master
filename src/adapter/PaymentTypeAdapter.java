package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.DataTable.mdlDataTableParam;
import model.Payment.mdlPaymentType;
import model.Query.mdlQueryExecute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class PaymentTypeAdapter {

    final static Logger logger = LogManager.getLogger(PaymentTypeAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlPaymentType> GetPaymentTypeWeb(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<mdlPaymentType> _mdlPaymentTypeList = new ArrayList<>();
        CachedRowSet jrs = null;
        String sql = "";
        String user = param.module_id;
        String search_part = "";

        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<>();

        try {

            sql = "{call sp_payment_type_get_with_paging_v2(?,?,?,?,?)}";

            String search_string = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", search_string));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.name));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.dir));

            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Reason", db_name, port);
            while (jrs.next()) {
                mdlPaymentType _mdlPaymentType = new mdlPaymentType();
                _mdlPaymentType.payment_type_id = jrs.getString("payment_type_id");
                _mdlPaymentType.payment_type_name = jrs.getString("payment_type_name");
                if (jrs.getString("is_credit").equals("true")) {
                    _mdlPaymentType.is_credit = "1";
                } else {
                    _mdlPaymentType.is_credit = "0";
                }
                _mdlPaymentType.created_by = jrs.getString("created_by");
                _mdlPaymentType.created_date = jrs.getString("created_date");
                _mdlPaymentType.updated_by = jrs.getString("updated_by");
                _mdlPaymentType.updated_date = jrs.getString("updated_date");
                _mdlPaymentType.is_active = jrs.getString("is_active");
                _mdlPaymentTypeList.add(_mdlPaymentType);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlPaymentTypeList;
    }

    public static int GetPaymentTypeTotal(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;
        String sql = "";
        String user = param.module_id;
        int return_value = 0;
        String search_part = "";

        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

        try {
            sql = "{call sp_payment_type_get_total_list(?)}";
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
    public static boolean UploadPaymentType(mdlPaymentType param, String db_name, int port) {

        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";

        List<mdlQueryExecute> list_mdl_query_execute = new ArrayList<mdlQueryExecute>();


        try {
            sql = "{call sp_payment_type_upload(?,?,?,?)}";

            list_mdl_query_execute.add(QueryAdapter.QueryParam("string", param.payment_type_id));
            list_mdl_query_execute.add(QueryAdapter.QueryParam("string", param.payment_type_name));
            list_mdl_query_execute.add(QueryAdapter.QueryParam("string", param.is_credit));
            list_mdl_query_execute.add(QueryAdapter.QueryParam("string", param.created_by));

            resultExec = QueryAdapter.QueryManipulateWithDB(sql, list_mdl_query_execute, functionName, user, db_name, port);

        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(list_mdl_query_execute), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean UpdateStatusPaymentType(mdlPaymentType param, String db_name, int port) {

        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";

        List<mdlQueryExecute> list_mdl_query_execute = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_payment_type_update_status(?,?,?)}";
            list_mdl_query_execute.add(QueryAdapter.QueryParam("string", param.payment_type_id));
            list_mdl_query_execute.add(QueryAdapter.QueryParam("string", param.is_active));
            list_mdl_query_execute.add(QueryAdapter.QueryParam("string", param.created_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, list_mdl_query_execute, functionName, user, db_name, port);

        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(list_mdl_query_execute), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean DeletePaymentType(mdlPaymentType param, String db_name, int port) {

        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";

        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_payment_type_delete(?)}";

            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.payment_type_id));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }
}
