package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.DataTable.mdlDataTableParam;
import model.Download.mdlDownloadParam;
import model.Employee.mdlEmployeeImage;
import model.POSM.mdlPOSMProduct;
import model.POSM.mdlPOSMProductImage;
import model.Query.mdlQueryExecute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class POSMProductAdapter {
    final static Logger logger = LogManager.getLogger(POSMProductAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlPOSMProduct> GetPOSMProductCheck(mdlPOSMProduct param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param.posm_id;

        List<mdlPOSMProduct> _mdlPOSMProductList = new ArrayList<mdlPOSMProduct>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        boolean resultExec = false;

        try {
            sql = "{call sp_posm_product_get(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.posm_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

            while (rowset.next()) {
                model.POSM.mdlPOSMProduct _mdlPOSMProduct = new model.POSM.mdlPOSMProduct();
                _mdlPOSMProduct.posm_id = rowset.getString("posm_id");
                _mdlPOSMProduct.posm_name = rowset.getString("posm_name");
                _mdlPOSMProduct.is_active = rowset.getString("is_active");
                _mdlPOSMProduct.created_by = rowset.getString("created_by");
                _mdlPOSMProduct.created_date = rowset.getString("created_date");
                _mdlPOSMProduct.updated_by = rowset.getString("updated_by");
                _mdlPOSMProduct.updated_date = rowset.getString("updated_date");
                _mdlPOSMProductList.add(_mdlPOSMProduct);
            }
        } catch (Exception ex) {
            _mdlPOSMProductList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlPOSMProductList;
    }

    public static List<mdlPOSMProduct> GetPOSMProduct(mdlDownloadParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param.employee_id;

        List<model.POSM.mdlPOSMProduct> _mdlPOSMProductList = new ArrayList<mdlPOSMProduct>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;

        try {
            sql = "{call sp_posm_product_list()}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

            while (rowset.next()) {
                model.POSM.mdlPOSMProduct _mdlPOSMProduct = new model.POSM.mdlPOSMProduct();
                _mdlPOSMProduct.setPosm_id(rowset.getString("posm_id"));
                _mdlPOSMProduct.setPosm_name(rowset.getString("posm_name"));
                _mdlPOSMProduct.setCreated_date(rowset.getString("created_date"));
                _mdlPOSMProduct.setCreated_by(rowset.getString("created_by"));
                _mdlPOSMProduct.setUpdated_by(rowset.getString("updated_by"));
                _mdlPOSMProduct.setUpdated_date(rowset.getString("updated_date"));
                _mdlPOSMProductList.add(_mdlPOSMProduct);
            }
        } catch (Exception ex) {
            _mdlPOSMProductList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", ex.toString(), "", db_name), ex);
        }

        return _mdlPOSMProductList;
    }

    public static List<mdlPOSMProduct> GetPOSMProductWeb(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<model.POSM.mdlPOSMProduct> _mdlPOSMProductList = new ArrayList<>();
        CachedRowSet jrs = null;
        String sql = "";
        String search_part = "";
        String user = param.search;
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<>();

        try {

            sql = "{call sp_posm_product_get_with_paging_v2(?,?,?,?,?)}";

            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.name));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.dir));

            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Posm Product List", db_name, port);

            while (jrs.next()) {

                model.POSM.mdlPOSMProduct _mdlPOSMProduct = new model.POSM.mdlPOSMProduct();

                _mdlPOSMProduct.posm_id = jrs.getString("posm_id");
                _mdlPOSMProduct.posm_name = jrs.getString("posm_name");
                if(db_name.equals("db_ffs") || db_name.equals("db_bna0") || db_name.equals("db_bna1")){
                    _mdlPOSMProduct.total_stock = jrs.getString("total_stock");
                }
                _mdlPOSMProduct.created_by = jrs.getString("created_by");
                _mdlPOSMProduct.created_date = jrs.getString("created_date");
                _mdlPOSMProduct.updated_by = jrs.getString("updated_by");
                _mdlPOSMProduct.updated_date = jrs.getString("updated_date");
                _mdlPOSMProduct.is_active = jrs.getString("is_active");

                _mdlPOSMProductList.add(_mdlPOSMProduct);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }

        return _mdlPOSMProductList;
    }

    public static int GetPOSMProductTotalList(mdlDataTableParam param, String db_name, int port) {

        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;
        long startTime = System.currentTimeMillis();
        String sql = "";
        int returnValue = 0;
        String search_part = "";

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();

        try {
            sql = "{call sp_posm_product_get_total_list(?)}";

            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));

            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "POSM Product Total", db_name, port);

            while (crs.next()) {
                returnValue = crs.getInt("total");
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "", db_name), ex);
        }
        return returnValue;
    }

    // Upload POSM
    public static boolean UploadPOSMProduct(mdlPOSMProduct param, String db_name, int port) {

        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();


        try {
            sql = "{call sp_posm_product_upload(?,?,?,?,?)}";

            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.posm_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.posm_name));
            if(param.total_stock == null || param.total_stock.equals("") || param.total_stock.equals(("-"))){
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", "0"));
            }else{
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.total_stock));
            }
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.updated_by));

            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean UpdateStatusPOSMProduct(mdlPOSMProduct param, String db_name, int port) {

        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

        try {
            sql = "{call sp_posm_product_update_status(?,?,?)}";

            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.posm_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.is_active));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));

            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean DeletePOSMProduct(mdlPOSMProduct param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

        try {
            sql = "{call sp_posm_product_delete(?)}";

            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.posm_id));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }
}
