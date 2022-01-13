package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.DataTable.mdlDataTableParam;
import model.Download.mdlDownloadParam;
import model.Product.mdlProductGroup;
import model.Product.mdlProductGroup;
import model.Query.mdlQueryExecute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class ProductGroupAdapter {

    final static Logger logger = LogManager.getLogger(ProductGroupAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlProductGroup> GetProductGroupCheck(mdlProductGroup param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param.product_group_id;

        List<mdlProductGroup> _mdlProductGroupList = new ArrayList<mdlProductGroup>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        boolean resultExec = false;

        try {
            sql = "{call sp_product_group_get(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.product_group_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            while (rowset.next()) {
                model.Product.mdlProductGroup _mdlProductGroup = new model.Product.mdlProductGroup();
                _mdlProductGroup.product_group_id = rowset.getString("product_group_id");
                _mdlProductGroup.product_group_name = rowset.getString("product_group_name");
                _mdlProductGroup.created_by = rowset.getString("created_by");
                _mdlProductGroup.created_date = rowset.getString("created_date");
                _mdlProductGroup.updated_by = rowset.getString("updated_by");
                _mdlProductGroup.updated_date = rowset.getString("updated_date");
                _mdlProductGroup.is_active = rowset.getString("is_active");
                _mdlProductGroupList.add(_mdlProductGroup);
            }
        } catch (Exception ex) {
            _mdlProductGroupList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlProductGroupList;
    }

    public static List<mdlProductGroup> GetProductGroupWeb(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet jrs = null;
        String sql = "";
        String search_part = "";
        String user = param.module_id;

        List<model.Product.mdlProductGroup> _mdlProductGroupList = new ArrayList<>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();

        try {

            sql = "{call sp_product_group_get_with_paging_v2(?,?,?,?,?)}";

            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            listParam.add(QueryAdapter.QueryParam("string", searchString));
            listParam.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            listParam.add(QueryAdapter.QueryParam("int", param.page_size));
            listParam.add(QueryAdapter.QueryParam("string", param.order.name));
            listParam.add(QueryAdapter.QueryParam("string", param.order.dir));

            jrs = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "Product Group List", db_name, port);
            while (jrs.next()) {
                model.Product.mdlProductGroup _mdlProductGroup = new model.Product.mdlProductGroup();
                _mdlProductGroup.product_group_id = jrs.getString("product_group_id");
                _mdlProductGroup.product_group_name = jrs.getString("product_group_name");
                _mdlProductGroup.created_by = jrs.getString("created_by");
                _mdlProductGroup.created_date = jrs.getString("created_date");
                _mdlProductGroup.updated_by = jrs.getString("updated_by");
                _mdlProductGroup.updated_date = jrs.getString("updated_date");
                _mdlProductGroup.is_active = jrs.getString("is_active");
                _mdlProductGroupList.add(_mdlProductGroup);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), user, db_name), ex);
        }

        return _mdlProductGroupList;
    }

    public static int GetProductGroupTotalList(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;
        String sql = "";
        int returnValue = 0;
        String search_part = "";
        String user = param.module_id;

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        try {
            sql = "{call sp_product_group_get_total_list(?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Product Group Total", db_name, port);
            while (crs.next()) {
                returnValue = crs.getInt("total");
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return returnValue;
    }

    // Upload Product UOM
    public static boolean UploadProductGroup(mdlProductGroup param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";
        List<model.Query.mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_product_group_upload(?,?,?,?)}";
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.product_group_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.product_group_name));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.created_by));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.updated_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listMdlQueryExecute), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean DeleteProductGroup(mdlProductGroup param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_product_group_delete(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.product_group_id));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static List<mdlProductGroup> GetDropDownProductGroup(String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = "Product Group";

        List<mdlProductGroup> mdlProductGroupList = new ArrayList<mdlProductGroup>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_product_group_dropdown_get}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "product_group", db_name, port);
            while (rowset.next()) {
                mdlProductGroup _mdlProductGroup = new mdlProductGroup();
                _mdlProductGroup.product_group_id = rowset.getString("product_group_id");
                _mdlProductGroup.product_group_name = rowset.getString("product_group_name");
                mdlProductGroupList.add(_mdlProductGroup);
            }
        } catch (Exception ex) {
            mdlProductGroupList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, "", "", ex.toString(), user, db_name), ex);
        }
        return mdlProductGroupList;
    }
}
