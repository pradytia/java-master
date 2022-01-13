package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.DataTable.mdlDataTableParam;
import model.Product.mdlProductType;
import model.Product.mdlProductType;
import model.Query.mdlQueryExecute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class ProductTypeAdapter {

    final static Logger logger = LogManager.getLogger(ProductTypeAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlProductType> GetProductTypeCheck(mdlProductType param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param.product_type_id;

        List<mdlProductType> _mdlProductTypeList = new ArrayList<mdlProductType>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        boolean resultExec = false;
        try {
            sql = "{call sp_product_type_get(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.product_type_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            while (rowset.next()) {
                model.Product.mdlProductType _mdlProductType = new model.Product.mdlProductType();
                _mdlProductType.product_type_id = rowset.getString("product_type_id");
                _mdlProductType.product_type_name = rowset.getString("product_type_name");
                _mdlProductType.is_active = rowset.getString("is_active");
                _mdlProductType.created_by = rowset.getString("created_by");
                _mdlProductType.created_date = rowset.getString("created_date");
                _mdlProductType.updated_by = rowset.getString("updated_by");
                _mdlProductType.updated_date = rowset.getString("updated_date");
                _mdlProductTypeList.add(_mdlProductType);
            }
        } catch (Exception ex) {
            _mdlProductTypeList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlProductTypeList;
    }

    public static List<mdlProductType> GetProductTypeWeb(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet jrs = null;
        String sql = "";
        String search_part = "";
        String user = param.module_id;

        List<model.Product.mdlProductType> _mdlProductTypeList = new ArrayList<>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        try {
            sql = "{call sp_product_type_get_with_paging_v2(?,?,?,?,?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            listParam.add(QueryAdapter.QueryParam("string", searchString));
            listParam.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            listParam.add(QueryAdapter.QueryParam("int", param.page_size));
            listParam.add(QueryAdapter.QueryParam("string", param.order.name));
            listParam.add(QueryAdapter.QueryParam("string", param.order.dir));

            jrs = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "Product Type List", db_name, port);

            while (jrs.next()) {
                model.Product.mdlProductType _mdlProductType = new model.Product.mdlProductType();
                _mdlProductType.product_type_id = jrs.getString("product_type_id");
                _mdlProductType.product_type_name = jrs.getString("product_type_name");
                _mdlProductType.created_by = jrs.getString("created_by");
                _mdlProductType.created_date = jrs.getString("created_date");
                _mdlProductType.updated_by = jrs.getString("updated_by");
                _mdlProductType.updated_date = jrs.getString("updated_date");
                _mdlProductType.is_active = jrs.getString("is_active");
                _mdlProductTypeList.add(_mdlProductType);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), user, db_name), ex);
        }
        return _mdlProductTypeList;
    }

    public static int GetProductTypeTotalList(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;
        String sql = "";
        int returnValue = 0;
        String search_part = "";
        String user = param.module_id;

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        try {
            sql = "{call sp_product_type_get_total_list(?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Product Type Total", db_name, port);
            while (crs.next()) {
                returnValue = crs.getInt("total");
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return returnValue;
    }

    public static boolean UploadProductType(mdlProductType param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";

        List<model.Query.mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_product_type_upload(?,?,?,?)}";
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.product_type_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.product_type_name));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.created_by));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.updated_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listMdlQueryExecute), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean DeleteProductType(mdlProductType param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_product_type_delete(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.product_type_id));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static List<mdlProductType> GetDropDownProductType(String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = "Product Type";
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();

        List<mdlProductType> mdlProductTypeList = new ArrayList<mdlProductType>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_product_type_dropdown_get}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "product_type", db_name, port);
            while (rowset.next()) {
                mdlProductType _mdlProductType = new mdlProductType();
                _mdlProductType.product_type_id = rowset.getString("product_type_id");
                _mdlProductType.product_type_name = rowset.getString("product_type_name");
                mdlProductTypeList.add(_mdlProductType);
            }
        } catch (Exception ex) {
            mdlProductTypeList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), user, db_name), ex);
        }
        return mdlProductTypeList;
    }
}
