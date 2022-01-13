package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.DataTable.mdlDataTableParam;
import model.Employee.mdlEmployeeImage;
import model.Globals;
import model.Query.mdlQueryExecute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class EmployeeImageAdapter {
    final static Logger logger = LogManager.getLogger(EmployeeImageAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlEmployeeImage> GetEmployeeImageWithPaging(mdlDataTableParam param, mdlEmployeeImage param2, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<>();
        List<mdlEmployeeImage> _mdlEmployeeImageList = new ArrayList<>();
        CachedRowSet rowSet = null;
        try {
            sql = "{call sp_employee_image_get_with_paging(?,?,?,?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param2.employee_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.page_size));
            rowSet = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "employeeImage", db_name, port);
            while (rowSet.next()) {
                mdlEmployeeImage _mdlEmployeeImage = new mdlEmployeeImage();
                _mdlEmployeeImage.employee_id = rowSet.getString("employee_id");
                _mdlEmployeeImage.image_name = rowSet.getString("image_name");
                _mdlEmployeeImage.image_path = rowSet.getString("image_path");
                _mdlEmployeeImage.created_by = rowSet.getString("created_by");
                _mdlEmployeeImage.created_date = rowSet.getString("created_date");
                _mdlEmployeeImage.updated_by = rowSet.getString("updated_by");
                _mdlEmployeeImage.updated_date = rowSet.getString("updated_date");
                _mdlEmployeeImageList.add(_mdlEmployeeImage);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "employeeImage", db_name), ex);
        }
        return _mdlEmployeeImageList;
    }

    public static int GetEmployeeImageTotalList(mdlDataTableParam param, mdlEmployeeImage param2, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        int valueTotal = 0;
        String sql = "";
        CachedRowSet rowSet = null;
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<>();
        try {
            sql = "{call sp_employee_image_get_total_list(?,?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param2.employee_id));
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

    public static boolean UploadEmployeeImage(mdlEmployeeImage param, String pathStorage, String finalImageName, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        boolean resultExec = false;
        List<model.Query.mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();
        try {
            Context context = (Context) new InitialContext().lookup("java:comp/env");
            String urlApacheImageLive = (String) context.lookup("url_server_live");
            String urlApacheImageLocal = (String) context.lookup("url_server_local");

            sql = "{call sp_employee_images_upload(?,?,?,?)}";
            String prod_dir = pathStorage + finalImageName;
            String newUrlApache = prod_dir.replace(urlApacheImageLocal, urlApacheImageLive);
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.employee_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", finalImageName));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", newUrlApache));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.created_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, "user", db_name, port);
        } catch (Exception ex) {
            resultExec = false;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listMdlQueryExecute), "", ex.toString(), "", db_name), ex);
        }
        return resultExec;
    }

    public static boolean DeleteEmployeeImage(mdlEmployeeImage param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        boolean resultExec = false;
        List<model.Query.mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_employee_image_delete(?)}";
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.image_name));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, "user", db_name, port);
        } catch (Exception ex) {
            resultExec = false;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listMdlQueryExecute), "", ex.toString(), "", db_name), ex);
        }
        return resultExec;
    }
    public static boolean UploadEmployeeImageNoImg(mdlEmployeeImage param,  String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        boolean resultExec = false;
        List<model.Query.mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();
        try {
            Context context = (Context) new InitialContext().lookup("java:comp/env");
            String paramFilePath = (String) context.lookup("url_ws");
            sql = "{call sp_employee_images_upload(?,?,?,?)}";;
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.employee_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.image_name));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", paramFilePath+param.image_path));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.created_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, "user", db_name, port, Globals.ip, Globals.login,Globals.pass);
        } catch (Exception ex) {
            resultExec = false;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listMdlQueryExecute), "", ex.toString(), "", db_name), ex);
        }
        return resultExec;
    }
}
