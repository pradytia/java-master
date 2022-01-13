package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controller.v1.ProductPriceController;
import core.LogAdapter;
import database.QueryAdapter;
import model.DataTable.mdlDataTableParam;
import model.Globals;
import model.Product.mdlProductPrice;
import model.Product.mdlProductPriceTyp;
import model.Product.mdlProductPriceUpload;
import model.Product.mdlProductPriceUploadTyp;
import model.Query.mdlQueryExecute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProductPriceAdapter {

    final static Logger logger = LogManager.getLogger(ProductPriceAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlProductPrice> GetProductPriceCheck(mdlProductPrice param, String db_name, int port){
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param.product_id;

        List<mdlProductPrice> _mdlProductPriceList = new ArrayList<mdlProductPrice>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        boolean resultExec = false;

        try{
            sql = "{call sp_product_price_get(?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.product_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.branch_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            while(rowset.next()){
                model.Product.mdlProductPrice _mdlProductPrice = new model.Product.mdlProductPrice();
                _mdlProductPrice.product_id = rowset.getString("product_id");
                _mdlProductPrice.branch_id = rowset.getString("branch_id");
                _mdlProductPrice.price = rowset.getString("price");
                _mdlProductPrice.created_by = rowset.getString("created_by");
                _mdlProductPrice.created_date = rowset.getString("created_date");
                _mdlProductPrice.updated_by = rowset.getString("updated_by");
                _mdlProductPrice.updated_date = rowset.getString("updated_date");
                _mdlProductPrice.module_id = rowset.getString("module_id");
                _mdlProductPrice.is_active = rowset.getString("is_active");
                _mdlProductPriceList.add(_mdlProductPrice);;
            }
        }catch (Exception ex){
            _mdlProductPriceList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlProductPriceList;
    }

    public static List<mdlProductPrice> GetProductPriceWeb(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet jrs = null;
        String sql = "";
        String search_part = "";
        String user = param.module_id;

        List<model.Product.mdlProductPrice> _mdlProductPriceList = new ArrayList<>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();

        try {
            sql = "{call sp_product_price_get_with_paging_v2(?,?,?,?,?)}";
            listParam.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            listParam.add(QueryAdapter.QueryParam("int", param.page_size));
            listParam.add(QueryAdapter.QueryParam("string", param.search));
            listParam.add(QueryAdapter.QueryParam("string", param.order.name));
            listParam.add(QueryAdapter.QueryParam("string", param.order.dir));
            jrs = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "Product Price List", db_name, port,Globals.ip, Globals.login,Globals.pass);

            while (jrs.next()) {
                model.Product.mdlProductPrice _mdlProductPrice = new model.Product.mdlProductPrice();
                _mdlProductPrice.product_id = jrs.getString("product_id");
                _mdlProductPrice.branch_id = jrs.getString("branch_id");
                _mdlProductPrice.price = jrs.getString("price");
                _mdlProductPrice.module_id = jrs.getString("module_id");
                _mdlProductPrice.created_by = jrs.getString("created_by");
                _mdlProductPrice.created_date = jrs.getString("created_date");
                _mdlProductPrice.updated_by = jrs.getString("updated_by");
                _mdlProductPrice.updated_date = jrs.getString("updated_date");
                _mdlProductPrice.is_active = jrs.getString("is_active");
                _mdlProductPrice.product_name = jrs.getString("product_name");
                _mdlProductPrice.branch_name = jrs.getString("branch_name");
                _mdlProductPriceList.add(_mdlProductPrice);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), user, db_name), ex);
        }
        return _mdlProductPriceList;
    }

    public static int GetProductPriceTotalList(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;
        String sql = "";
        int returnValue = 0;
        String search_part = "";
        String user = param.module_id;

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        try {
            sql = "{call sp_product_price_get_total_list(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.search));
            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Product Price Total", db_name, port,Globals.ip ,Globals.login,Globals.pass);

            while (crs.next()) {
                returnValue = crs.getInt("total");
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return returnValue;
    }

    public static List<mdlProductPriceTyp> GetProductPriceWebTyp(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet jrs = null;
        String sql = "";
        String search_part = "";
        String user = param.module_id;

        List<model.Product.mdlProductPriceTyp> _mdlProductPriceTypList = new ArrayList<>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();

        try {
            sql = "{call sp_product_price_typ_get_with_paging_v2(?,?,?,?,?)}";
            listParam.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            listParam.add(QueryAdapter.QueryParam("int", param.page_size));
            listParam.add(QueryAdapter.QueryParam("string", param.search));
            listParam.add(QueryAdapter.QueryParam("string", param.order.name));
            listParam.add(QueryAdapter.QueryParam("string", param.order.dir));
            jrs = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "Product Price List", db_name, port, Globals.ip, Globals.login, Globals.pass);

            while (jrs.next()) {
                model.Product.mdlProductPriceTyp _mdlProductPriceTyp = new model.Product.mdlProductPriceTyp();
                _mdlProductPriceTyp.product_id = jrs.getString("product_id");
                _mdlProductPriceTyp.price = jrs.getString("price");
                _mdlProductPriceTyp.module_id = jrs.getString("module_id");
                _mdlProductPriceTyp.created_by = jrs.getString("created_by");
                _mdlProductPriceTyp.created_date = jrs.getString("created_date");
                _mdlProductPriceTyp.updated_by = jrs.getString("updated_by");
                _mdlProductPriceTyp.updated_date = jrs.getString("updated_date");
                _mdlProductPriceTyp.is_active = jrs.getString("is_active");
                _mdlProductPriceTyp.product_name = jrs.getString("product_name");
                _mdlProductPriceTyp.customer_type_id = jrs.getString("customer_type_id");
                _mdlProductPriceTyp.customer_type_name = jrs.getString("customer_type_name");
                _mdlProductPriceTypList.add(_mdlProductPriceTyp);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), user, db_name), ex);
        }
        return _mdlProductPriceTypList;
    }

    public static int GetProductPriceTotalListTyp(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;
        String sql = "";
        int returnValue = 0;
        String search_part = "";
        String user = param.module_id;

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        try {
            sql = "{call sp_product_price_typ_get_total_list(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.search));
            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Product Price Total", db_name, port, Globals.ip, Globals.login, Globals.pass);

            while (crs.next()) {
                returnValue = crs.getInt("total");
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return returnValue;
    }


    // Upload Product UOM
    public static boolean UploadProductPrice (mdlProductPriceUpload param, String db_name, int port){
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";
        List<model.Query.mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();

        try{
            for (int i = 0; i < param.branch_id.size(); i++) {
                sql = "{call sp_product_price_upload(?,?,?,?,?,?)}";
                listMdlQueryExecute = new ArrayList<mdlQueryExecute>();
                listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.product_id));
                listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.branch_id.get(i)));
                listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.price));
                listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.module_id));
                listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.created_by));
                listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.updated_by));
                resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, user, db_name, port,Globals.ip,Globals.login,Globals.pass);
            }
        } catch (Exception ex){
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listMdlQueryExecute), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean UploadProductPriceTyp (mdlProductPriceUploadTyp param, String db_name, int port){
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";
        List<model.Query.mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();
        try{
            for (int i = 0; i < param.customer_type_id.size(); i++) {
                sql = "{call sp_product_price_upload_typ(?,?,?,?,?,?)}";
                listMdlQueryExecute = new ArrayList<mdlQueryExecute>();
                listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.product_id));
                listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.customer_type_id.get(i)));
                listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.price));
                listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.module_id));
                listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.created_by));
                listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.updated_by));
                resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, user, db_name, port,Globals.ip,Globals.login,Globals.pass);
            }
        } catch (Exception ex){
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listMdlQueryExecute), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }


    public static boolean DeleteProductPrice (mdlProductPrice param, String db_name, int port){

        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = "product_Price";
        String sql = "";

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try{
            sql = "{call sp_product_price_delete(?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.product_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.branch_id));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex){
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean DeleteProductPriceTyp (mdlProductPriceTyp param, String db_name, int port){

        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = "product_Price";
        String sql = "";

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try{
            sql = "{call sp_product_price_delete_typ(?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.product_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.customer_type_id));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port,Globals.ip,Globals.login,Globals.pass);
        } catch (Exception ex){
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }
}
