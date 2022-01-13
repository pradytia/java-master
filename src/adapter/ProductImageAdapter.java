package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.DataTable.mdlDataTableParam;
import model.Product.mdlProductImage;
import model.Query.mdlQueryExecute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class ProductImageAdapter {

    final static Logger logger = LogManager.getLogger(ProductImageAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlProductImage> GetProductImageWithPaging(mdlDataTableParam param, mdlProductImage param2, String db_name, int port){
        long startTime = System.currentTimeMillis();
        String user = param.module_id;
        String sql = "";
        String functionName  = Thread.currentThread().getStackTrace()[1].getMethodName();

        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<>();
        List<mdlProductImage> _mdlProductImageList = new ArrayList<>();
        CachedRowSet rowSet = null;
        try {
            sql = "{call sp_product_image_get_with_paging(?,?,?,?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param2.product_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.page_size));

            rowSet = QueryAdapter.QueryExecuteWithDB(sql,_mdlQueryExecuteList,functionName,"productImage", db_name, port);
            while (rowSet.next()){
                mdlProductImage _mdlProductImage = new mdlProductImage();
                _mdlProductImage.product_id = rowSet.getString("product_id");
                _mdlProductImage.image_name = rowSet.getString("image_name");
                _mdlProductImage.image_path = rowSet.getString("image_path");
                _mdlProductImage.created_by = rowSet.getString("created_by");
                _mdlProductImage.created_date = rowSet.getString("created_date");
                _mdlProductImage.updated_by = rowSet.getString("updated_by");
                _mdlProductImage.updated_date = rowSet.getString("updated_date");
                _mdlProductImage.is_default = rowSet.getString("is_default");
                _mdlProductImageList.add(_mdlProductImage);
            }
        }catch (Exception ex){
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return  _mdlProductImageList;
    }

    public static int GetProductImageTotalList (mdlDataTableParam param, mdlProductImage param2, String db_name, int port){
        long startTime = System.currentTimeMillis();
        String user = param.module_id;
        int valueTotal = 0;
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet rowSet = null;
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<>();
        try {
            sql = "{call sp_product_image_get_total_list(?,?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param2.product_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            rowSet = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "productImage", db_name, port);
            while (rowSet.next()){
                valueTotal = rowSet.getInt("total");
            }
        }catch (Exception ex){
            valueTotal = 0;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return valueTotal;
    }

    public static boolean UploadProductImage(mdlProductImage param, String pathStorage, String finalImageName, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = param.created_by;
        String sql = "";
        boolean resultExec = false;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();

        List<model.Query.mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();
        try {
            Context context = (Context) new InitialContext().lookup("java:comp/env");
            String urlApacheImageLive = (String) context.lookup("url_server_live");
            String urlApacheImageLocal = (String) context.lookup("url_server_local");
            sql = "{call sp_product_images_upload(?,?,?,?)}";
            String prod_dir = pathStorage + finalImageName;
            String newUrlApache = prod_dir.replace(urlApacheImageLocal, urlApacheImageLive);
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.product_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", finalImageName));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", newUrlApache));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.created_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, "user", db_name, port);
        } catch (Exception ex) {
            resultExec = false;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listMdlQueryExecute), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean DeleteProductImage(mdlProductImage param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = param.product_id;
        String sql = "";
        boolean resultExec = false;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<model.Query.mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_product_image_delete(?)}";
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.image_name));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, "user", db_name, port);
        } catch (Exception ex) {
            resultExec = false;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listMdlQueryExecute), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean UpdateStatusProductImage(mdlProductImage param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean result_exec = false;
        String user = param.updated_by;
        String sql = "";
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_product_image_update_status(?,?,?)}";
            listParam.add(QueryAdapter.QueryParam("string", param.image_name));
            listParam.add(QueryAdapter.QueryParam("string", param.is_default));
            listParam.add(QueryAdapter.QueryParam("string", param.created_by));
            result_exec = QueryAdapter.QueryManipulateWithDB(sql, listParam, functionName, user, db_name, port);
        } catch (Exception ex) {
            ex.getMessage();
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), user, db_name), ex);
        }
        return result_exec;
    }

    public static boolean UploadProductNoImage(mdlProductImage param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = param.created_by;
        String sql = "";
        boolean resultExec = false;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();

        List<model.Query.mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();
        try {
            Context context = (Context) new InitialContext().lookup("java:comp/env");
            String paramFilePath = (String) context.lookup("url_ws");
            sql = "{call sp_product_images_upload(?,?,?,?)}";
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.product_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.image_name));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", paramFilePath+param.image_path));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.created_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, "user", db_name, port);
        } catch (Exception ex) {
            resultExec = false;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listMdlQueryExecute), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }
}
