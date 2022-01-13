package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.DataTable.mdlDataTableParam;
import model.Product.mdlProductStatus;
import model.Query.mdlQueryExecute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class ProductStatusAdapter {
    final static Logger logger = LogManager.getLogger(ProductStatusAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlProductStatus> GetProductStatusCheck(mdlProductStatus param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param.product_id;

        List<mdlProductStatus> _mdlProductStatusList = new ArrayList<mdlProductStatus>();
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        CachedRowSet rowset = null;
        boolean resultExec = false;
        try {
            sql = "{call sp_product_status_get(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.product_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

            while (rowset.next()) {
                mdlProductStatus _mdlProductStatus = new mdlProductStatus();
                _mdlProductStatus.product_id = rowset.getString("product_id");
                _mdlProductStatus.status_id = rowset.getString("status_id");
                _mdlProductStatus.is_active = rowset.getString("is_active");
                _mdlProductStatus.created_by = rowset.getString("created_by");
                _mdlProductStatus.created_date = rowset.getString("created_date");
                _mdlProductStatus.updated_by = rowset.getString("updated_by");
                _mdlProductStatus.updated_date = rowset.getString("updated_date");
                _mdlProductStatusList.add(_mdlProductStatus);
            }
        } catch (Exception ex) {
            _mdlProductStatusList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlProductStatusList;
    }

    // Upload Product UOM
    public static boolean UploadProductStatus(mdlProductStatus param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";

        List<mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_product_status_upload(?,?,?)}";
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.product_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.status_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.created_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listMdlQueryExecute), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean DeleteProductStatus(mdlProductStatus param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";

        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_product_type_delete(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.product_id));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static List<mdlProductStatus> GetDropDownProductStatus(String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = "Product Type";
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();

        List<mdlProductStatus> mdlProductStatusList = new ArrayList<mdlProductStatus>();
        List<mdlQueryExecute> listParam = new ArrayList<mdlQueryExecute>();
        CachedRowSet rowset = null;

        try {
            sql = "{call sp_product_status_dropdown_get}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "product_type", db_name, port);

            while (rowset.next()) {
                mdlProductStatus _mdlProductStatus = new mdlProductStatus();
                _mdlProductStatus.status_id = rowset.getString("status_id");
                _mdlProductStatus.status_name = rowset.getString("status_name");
                mdlProductStatusList.add(_mdlProductStatus);
            }
        } catch (Exception ex) {
            mdlProductStatusList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), user, db_name), ex);
        }
        return mdlProductStatusList;
    }
}
