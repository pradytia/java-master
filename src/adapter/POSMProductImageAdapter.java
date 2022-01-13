package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.DataTable.mdlDataTableParam;
import model.Employee.mdlEmployeeImage;
import model.POSM.mdlPOSMProductImage;
import model.Query.mdlQueryExecute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class POSMProductImageAdapter {
    final static Logger logger = LogManager.getLogger(POSMProductImageAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlPOSMProductImage> GetProductImageWithPaging(mdlDataTableParam param, mdlPOSMProductImage param2, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<>();
        List<mdlPOSMProductImage> _mdlPOSMProductImageList = new ArrayList<>();
        CachedRowSet rowSet = null;
        try {
            sql = "{call sp_posm_product_image_get_with_paging(?,?,?,?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param2.posm_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.page_size));
            rowSet = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "employeeImage", db_name, port);
            while (rowSet.next()) {
                mdlPOSMProductImage _mdlPOSMProductImage = new mdlPOSMProductImage();
                _mdlPOSMProductImage.posm_id = rowSet.getString("posm_id");
                _mdlPOSMProductImage.image_name = rowSet.getString("image_name");
                _mdlPOSMProductImage.image_path = rowSet.getString("image_path");
                _mdlPOSMProductImage.created_by = rowSet.getString("created_by");
                _mdlPOSMProductImage.created_date = rowSet.getString("created_date");
                _mdlPOSMProductImageList.add(_mdlPOSMProductImage);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "employeeImage", db_name), ex);
        }
        return _mdlPOSMProductImageList;
    }

    public static int GetPOSMProductImageTotalList(mdlDataTableParam param, mdlPOSMProductImage param2, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        int valueTotal = 0;
        String sql = "";
        CachedRowSet rowSet = null;
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<>();
        try {
            sql = "{call sp_posm_product_image_get_total_list(?,?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param2.posm_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            rowSet = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "employeeImage", db_name, port);
            while (rowSet.next()) {
                valueTotal = rowSet.getInt("total");
            }
        } catch (Exception ex) {
            valueTotal = 0;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "productImage", db_name), ex);
        }
        return valueTotal;
    }

    public static boolean UploadPosmProductImage(mdlPOSMProductImage param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        boolean resultExec = false;
        List<mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_posm_product_images_upload(?,?,?,?)}";
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.posm_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.image_name));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.image_path));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.created_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, "user", db_name, port);
        } catch (Exception ex) {
            resultExec = false;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listMdlQueryExecute), "", ex.toString(), "", db_name), ex);
        }
        return resultExec;
    }

    public static boolean DeletePOSMProductImage(mdlPOSMProductImage param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        boolean resultExec = false;
        List<model.Query.mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_posm_product_image_delete(?)}";
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.image_name));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, "user", db_name, port);
        } catch (Exception ex) {
            resultExec = false;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listMdlQueryExecute), "", ex.toString(), "", db_name), ex);
        }
        return resultExec;
    }

    public static boolean UploadPosmProductImageNoImage(mdlPOSMProductImage param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        boolean resultExec = false;
        List<mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();
        try {
            Context context = (Context) new InitialContext().lookup("java:comp/env");
            String paramFilePath = (String) context.lookup("url_ws");
            sql = "{call sp_posm_product_images_upload(?,?,?,?)}";
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.posm_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.image_name));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", paramFilePath+param.image_path));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.created_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, "user", db_name, port);
        } catch (Exception ex) {
            resultExec = false;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listMdlQueryExecute), "", ex.toString(), "", db_name), ex);
        }
        return resultExec;
    }

}
