package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.DataTable.mdlDataTableParam;
import model.Download.mdlDownloadParam;
import model.Product.mdlProductUom;
import model.Query.mdlQueryExecute;
import model.Uom.mdlUom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class ProductUomAdapter {
    final static Logger logger = LogManager.getLogger(ProductUomAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlProductUom> GetProductUomCheck(mdlProductUom param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param.product_id;

        List<mdlProductUom> _mdlProductUomList = new ArrayList<mdlProductUom>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        boolean resultExec = false;

        try {
            sql = "{call sp_product_uom_get(?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.product_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.uom));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

            while (rowset.next()) {
                model.Product.mdlProductUom _mdlProductUom = new model.Product.mdlProductUom();
                _mdlProductUom.setProduct_id(rowset.getString("product_id"));
                _mdlProductUom.setUom(rowset.getString("uom"));
                _mdlProductUom.setBase_uom(rowset.getString("base_uom"));
                _mdlProductUom.setQuantity(rowset.getString("quantity"));
                _mdlProductUom.setCreated_by(rowset.getString("created_by"));
                _mdlProductUom.setCreated_date(rowset.getString("created_date"));
                _mdlProductUom.setUpdated_by(rowset.getString("updated_by"));
                _mdlProductUom.setUpdated_date(rowset.getString("updated_date"));
                _mdlProductUom.setIs_active(rowset.getString("is_active"));
                _mdlProductUomList.add(_mdlProductUom);
            }
        } catch (Exception ex) {
            _mdlProductUomList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlProductUomList;
    }


    public static List<mdlUom> GetDropdownProdUombyProd(mdlProductUom param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param.created_by;

        List<mdlUom> _mdlUomList = new ArrayList<mdlUom>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;

        try {
            sql = "{call sp_product_uom_dropdown_by_prodid_get(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.product_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "uom", db_name, port);

            while (rowset.next()) {
                model.Uom.mdlUom _mdlUom = new model.Uom.mdlUom();
                _mdlUom.setUom(rowset.getString("uom"));
                _mdlUom.setDescription(rowset.getString("description"));
                _mdlUomList.add(_mdlUom);
            }
        } catch (Exception ex) {
            _mdlUomList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }

        return _mdlUomList;
    }

    public static List<mdlProductUom> GetProductUomWeb(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<model.Product.mdlProductUom> _mdlProductUomList = new ArrayList<>();
        CachedRowSet jrs = null;
        String sql = "";
        String search_part = "";
        String user = param.module_id;

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<>();
        try {
            sql = "{call sp_product_uom_get_with_paging_v2 (?,?,?,?,?)}";
            String search_string = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", search_string));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.name));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.dir));
            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Product Uom List", db_name, port);
            while (jrs.next()) {
                model.Product.mdlProductUom _mdlProductUom = new model.Product.mdlProductUom();
                _mdlProductUom.product_id = jrs.getString("product_id");
                _mdlProductUom.uom = jrs.getString("uom");
                _mdlProductUom.base_uom = jrs.getString("base_uom");
                _mdlProductUom.quantity = jrs.getString("quantity");
                _mdlProductUom.created_by = jrs.getString("created_by");
                _mdlProductUom.created_date = jrs.getString("created_date");
                _mdlProductUom.updated_by = jrs.getString("updated_by");
                _mdlProductUom.updated_date = jrs.getString("updated_date");
                _mdlProductUom.is_active = jrs.getString("is_active");
                _mdlProductUom.product_name = jrs.getString("product_name");
                _mdlProductUomList.add(_mdlProductUom);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlProductUomList;
    }

    public static int GetProductUomTotalList(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;
        String sql = "";
        int returnValue = 0;
        String search_part = "";
        String user = param.module_id;

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        try {
            sql = "{call sp_product_uom_get_total_list(?)}";
            String search_string = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", search_string));
            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Product UOM Total", db_name, port);
            while (crs.next()) {
                returnValue = crs.getInt("total");
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return returnValue;
    }

    // Upload Product UOM
    public static boolean UploadProductUom(mdlProductUom param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";
        List<model.Query.mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_product_uom_upload(?,?,?,?,?,?)}";
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.product_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.uom));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.base_uom));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.quantity));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.created_by));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.updated_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listMdlQueryExecute), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean DeleteProductUom(mdlProductUom param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = "product_uom";
        String sql = "";

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_product_uom_delete(?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.product_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.uom));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }


}
