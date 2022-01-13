package adapter;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.DataTable.mdlDataTableParam;
import model.Globals;
import model.Product.mdlProduct;
import model.Product.mdlProductUom;
import model.Query.mdlQueryExecute;
import model.Download.mdlDownloadParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter {

    final static Logger logger = LogManager.getLogger(ProductAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlProduct> GetProductCheck(mdlProduct param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param.product_id;

        List<mdlProduct> _mdlProductList = new ArrayList<mdlProduct>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        boolean resultExec = false;
        try {

            sql = "{call sp_product_get(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.product_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            while (rowset.next()) {
                mdlProduct _mdlProduct = new model.Product.mdlProduct();
                _mdlProduct.setProduct_id(rowset.getString("product_id"));
                _mdlProduct.setProduct_name(rowset.getString("product_name"));
                _mdlProduct.setProduct_type(rowset.getString("product_type"));
                _mdlProduct.setProduct_group(rowset.getString("product_group"));
                _mdlProduct.setProduct_weight(rowset.getString("product_weight"));
                _mdlProduct.setUom(rowset.getString("uom"));
                _mdlProduct.setDnr_code(rowset.getString("dnr_code"));
                _mdlProduct.setSap_code(rowset.getString("sap_code"));
                _mdlProduct.setPrice(rowset.getString("price"));
                _mdlProduct.setCreated_by(rowset.getString("created_by"));
                _mdlProduct.setCreated_date(rowset.getString("created_date"));
                _mdlProduct.setUpdated_by(rowset.getString("updated_by"));
                _mdlProduct.setModule_id(rowset.getString("module_id"));
                _mdlProduct.setUpdated_date(rowset.getString("updated_date"));
                _mdlProductList.add(_mdlProduct);
            }
        } catch (Exception ex) {
            _mdlProductList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlProductList;
    }

    public static List<model.Product.mdlProduct> GetProduct(mdlDownloadParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param.employee_id;

        List<model.Product.mdlProduct> _mdlProductList = new ArrayList<mdlProduct>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_product_list(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.module_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            while (rowset.next()) {
                mdlProduct _mdlProduct = new model.Product.mdlProduct();
                _mdlProduct.setProduct_id(rowset.getString("product_id"));
                _mdlProduct.setProduct_name(rowset.getString("product_name"));
                _mdlProduct.setProduct_type(rowset.getString("product_type"));
                _mdlProduct.setProduct_group(rowset.getString("product_group"));
                _mdlProduct.setProduct_weight(rowset.getString("product_weight"));
                _mdlProduct.setUom(rowset.getString("uom"));
                _mdlProduct.setDnr_code(rowset.getString("dnr_code"));
                _mdlProduct.setSap_code(rowset.getString("sap_code"));
                _mdlProduct.setPrice(rowset.getString("price"));
                _mdlProduct.setCreated_by(rowset.getString("created_by"));
                _mdlProduct.setCreated_date(rowset.getString("created_date"));
                _mdlProduct.setUpdated_by(rowset.getString("updated_by"));
                _mdlProduct.setModule_id(rowset.getString("module_id"));
                _mdlProduct.setUpdated_date(rowset.getString("updated_date"));
                _mdlProductList.add(_mdlProduct);
            }
        } catch (Exception ex) {
            _mdlProductList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlProductList;
    }

    public static List<model.Product.mdlProductUom> GetProductUom(mdlDownloadParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param.employee_id;

        List<model.Product.mdlProductUom> _mdlProductUomList = new ArrayList<mdlProductUom>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_product_uom_list()}";
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
                _mdlProductUomList.add(_mdlProductUom);
            }
        } catch (Exception ex) {
            _mdlProductUomList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlProductUomList;
    }

    //================================== WEB =============================================== \\

    public static List<mdlProduct> GetProductWeb(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<model.Product.mdlProduct> product_list = new ArrayList<>();
        CachedRowSet jrs = null;
        String sql = "";
        String search_part = "";
        String user = param.module_id;

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<>();
        try {
            sql = "{call sp_product_get_with_paging_v2(?,?,?,?,?)}";
            String search_string = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", search_string));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.name));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.dir));
            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Product List", db_name, port);

            while (jrs.next()) {
                model.Product.mdlProduct mdl_product = new model.Product.mdlProduct();
                mdl_product.product_id = jrs.getString("product_id");
                mdl_product.product_name = jrs.getString("product_name");
                mdl_product.product_type = jrs.getString("product_type");
                mdl_product.product_group = jrs.getString("product_group");
                mdl_product.product_weight = jrs.getString("product_weight");
                mdl_product.uom = jrs.getString("uom");
                mdl_product.dnr_code = jrs.getString("dnr_code");
                mdl_product.sap_code = jrs.getString("sap_code");
                mdl_product.price = jrs.getString("price");
                mdl_product.created_by = jrs.getString("created_by");
                mdl_product.created_date = jrs.getString("created_date");
                mdl_product.updated_by = jrs.getString("updated_by");
                mdl_product.updated_by = jrs.getString("module_id");
                mdl_product.updated_date = jrs.getString("updated_date");
                mdl_product.is_active = jrs.getString("is_active");
                mdl_product.is_ppn_include = jrs.getString("is_ppn_include");
                mdl_product.product_weight_uom = jrs.getString("product_weight_uom");
                mdl_product.status = jrs.getString("status");
                product_list.add(mdl_product);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return product_list;
    }

    public static int GetProductTotalList(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;
        String sql = "";
        int return_value = 0;
        String search_part = "";
        String user = param.module_id;
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        try {
            sql = "{call sp_product_get_total_list(?)}";
            String search_string = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", search_string));
            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, functionName, db_name, port);
            while (crs.next()) {
                return_value = crs.getInt("total");
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return return_value;
    }

    public static List<mdlProduct> GetProductWebOnlyActive(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<model.Product.mdlProduct> product_list = new ArrayList<>();
        CachedRowSet jrs = null;
        String sql = "";
        String search_part = "";
        String user = param.module_id;

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<>();
        try {
            sql = "{call sp_product_get_with_paging_onlyacv(?,?,?,?)}";
            String search_string = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.branch_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", search_string));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.page_size));
            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Product List", db_name, port,Globals.ip, Globals.login,Globals.pass);
            while (jrs.next()) {
                model.Product.mdlProduct mdl_product = new model.Product.mdlProduct();
                mdl_product.product_id = jrs.getString("product_id");
                mdl_product.product_name = jrs.getString("product_name");
                mdl_product.product_type = jrs.getString("product_type");
                mdl_product.product_group = jrs.getString("product_group");
                mdl_product.product_weight = jrs.getString("product_weight");
                mdl_product.uom = jrs.getString("uom");
                mdl_product.dnr_code = jrs.getString("dnr_code");
                mdl_product.sap_code = jrs.getString("sap_code");
                mdl_product.price = jrs.getString("price");
                mdl_product.created_by = jrs.getString("created_by");
                mdl_product.created_date = jrs.getString("created_date");
                mdl_product.updated_by = jrs.getString("updated_by");
                mdl_product.updated_by = jrs.getString("module_id");
                mdl_product.updated_date = jrs.getString("updated_date");
                mdl_product.is_active = jrs.getString("is_active");
                mdl_product.stock = jrs.getString("stock");
                product_list.add(mdl_product);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return product_list;
    }

    public static int GetProductTotalListOnlyActive(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;
        String sql = "";
        int return_value = 0;
        String search_part = "";
        String user = param.module_id;

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        try {
            sql = "{call sp_product_get_total_list_onlyacv(?)}";
            String search_string = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", search_string));
            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, functionName, db_name, port,Globals.ip, Globals.login,Globals.pass);
            while (crs.next()) {
                return_value = crs.getInt("total");
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return return_value;
    }

    // Upload Product
    public static boolean UploadProduct(mdlProduct product_param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean result_exec = false;
        String user = product_param.created_by;
        String sql = "";

        List<model.Query.mdlQueryExecute> list_mdl_query_execute = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_product_upload(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            list_mdl_query_execute.add(QueryAdapter.QueryParam("string", product_param.product_id));
            list_mdl_query_execute.add(QueryAdapter.QueryParam("string", product_param.product_name));
            list_mdl_query_execute.add(QueryAdapter.QueryParam("string", product_param.product_type));
            list_mdl_query_execute.add(QueryAdapter.QueryParam("string", product_param.product_group));
            list_mdl_query_execute.add(QueryAdapter.QueryParam("string", product_param.product_weight));
            list_mdl_query_execute.add(QueryAdapter.QueryParam("string", product_param.uom));
            list_mdl_query_execute.add(QueryAdapter.QueryParam("string", product_param.dnr_code));
            list_mdl_query_execute.add(QueryAdapter.QueryParam("string", product_param.sap_code));
            list_mdl_query_execute.add(QueryAdapter.QueryParam("string", product_param.price));
            list_mdl_query_execute.add(QueryAdapter.QueryParam("string", product_param.created_by));
            list_mdl_query_execute.add(QueryAdapter.QueryParam("string", product_param.updated_by));
            list_mdl_query_execute.add(QueryAdapter.QueryParam("string", product_param.module_id));
            list_mdl_query_execute.add(QueryAdapter.QueryParam("string", product_param.is_ppn_include));
            list_mdl_query_execute.add(QueryAdapter.QueryParam("string", product_param.product_weight_uom));
            result_exec = QueryAdapter.QueryManipulateWithDB(sql, list_mdl_query_execute, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(list_mdl_query_execute), "", ex.toString(), user, db_name), ex);
        }
        return result_exec;
    }

    public static boolean UpdateStatusProduct(mdlProduct product_param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean result_exec = false;
        String user = product_param.created_by;
        String sql = "";

        List<model.Query.mdlQueryExecute> list_mdl_query_execute = new ArrayList<mdlQueryExecute>();

        try {
            sql = "{call sp_product_update_status(?,?,?)}";
            list_mdl_query_execute.add(QueryAdapter.QueryParam("string", product_param.product_id));
            list_mdl_query_execute.add(QueryAdapter.QueryParam("string", product_param.is_active));
            list_mdl_query_execute.add(QueryAdapter.QueryParam("string", product_param.created_by));
            result_exec = QueryAdapter.QueryManipulateWithDB(sql, list_mdl_query_execute, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(list_mdl_query_execute), "", ex.toString(), user, db_name), ex);
        }
        return result_exec;
    }

    public static boolean DeleteProduct(mdlProduct param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

        try {
            sql = "{call sp_product_delete(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.product_id));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static List<mdlProduct> GetDropDownProduct(String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = "product";

        List<mdlProduct> mdlProductList = new ArrayList<mdlProduct>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;

        try {
            sql = "{call sp_product_dropdown_get()}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "product", db_name, port);
            while (rowset.next()) {
                mdlProduct _mdlProduct = new mdlProduct();
                _mdlProduct.product_id = rowset.getString("product_id");
                _mdlProduct.product_name = rowset.getString("product_name");
                mdlProductList.add(_mdlProduct);
            }
        } catch (Exception ex) {
            mdlProductList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, "", "", ex.toString(), user, db_name), ex);
        }
        return mdlProductList;
    }
}
