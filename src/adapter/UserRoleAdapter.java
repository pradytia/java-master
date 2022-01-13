package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.DataTable.mdlDataTableParam;
import model.Menu.mdlAccessRole;
import model.Menu.mdlMenu;
import model.Query.mdlQueryExecute;
import model.Role.mdlRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class UserRoleAdapter {
    final static Logger logger = LogManager.getLogger(UserRoleAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlRole> GetRole(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = param.module_id;
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();

        List<mdlRole> mdlRoleList = new ArrayList<mdlRole>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;

        try {
            sql = "{call sp_role_get_v2 (?,?,?,?,?)}";
            listParam.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            listParam.add(QueryAdapter.QueryParam("int", param.page_size));
            listParam.add(QueryAdapter.QueryParam("string", param.search));
            listParam.add(QueryAdapter.QueryParam("string", param.order.name));
            listParam.add(QueryAdapter.QueryParam("string", param.order.dir));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "role", db_name, port);

            while (rowset.next()) {
                mdlRole mdlRole = new mdlRole();
                mdlRole.role_id = rowset.getString("role_id");
                mdlRole.role_name = rowset.getString("role_name");
                mdlRoleList.add(mdlRole);
            }
        } catch (Exception ex) {
            mdlRoleList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), user, db_name), ex);
        }
        return mdlRoleList;
    }

    public static boolean UploadRole(mdlRole param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = param.role_name;
        String sql = "";
        boolean resultExec = false;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<model.Query.mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();

        try {
            sql = "{call sp_role_upload(?,?)}";
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.role_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.role_name));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, "user", db_name, port);

            if (resultExec) {

            } else {

            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listMdlQueryExecute), "", ex.toString(), user, db_name), ex);
            return false;
        }
        return true;
    }

    public static boolean DeleteRole(mdlRole param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = param.role_id;
        String sql = "";
        String sqlCommand = "";
        boolean resultExec = false;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<model.Query.mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();

        try {

            //delete from user role detail ==> mst access role
            listMdlQueryExecute = new ArrayList<>();
            sql = "{call sp_role_detail_delete (?)}";
            sqlCommand += sql;

            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.role_id));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, "user", db_name, port);

            try {
                if (resultExec) {
                    listMdlQueryExecute = new ArrayList<>();
                    sql = "{call sp_role_delete (?)}";
                    sqlCommand += sql;

                    listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.role_id));
                    resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, "user", db_name, port);
                } else {
                    return resultExec;
                }
            } catch (Exception ex) {
                logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listMdlQueryExecute), "", ex.toString(), user, db_name), ex);
            }

        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listMdlQueryExecute), "", ex.toString(), user, db_name), ex);
        }

        return resultExec;

    }

    public static int GetTotalRole(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = param.module_id;
        int return_value = 0;
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();

        List<mdlRole> mdlRoleList = new ArrayList<mdlRole>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_role_total_get (?)}";
            listParam.add(QueryAdapter.QueryParam("string", param.search));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "role", db_name, port);

            while (rowset.next()) {
                return_value = rowset.getInt("total");
            }
        } catch (Exception ex) {
            return_value = 0;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), user, db_name), ex);
        }
        return return_value;
    }

    public static List<mdlRole> GetDropDownRole(String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = "GetDropDownRole";
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();

        List<mdlRole> mdlRoleList = new ArrayList<mdlRole>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;

        try {
            sql = "{call sp_dropdown_role_get}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "role", db_name, port);

            while (rowset.next()) {
                mdlRole mdlRole = new mdlRole();
                mdlRole.role_id = rowset.getString("role_id");
                mdlRole.role_name = rowset.getString("role_name");
                mdlRoleList.add(mdlRole);
            }
        } catch (Exception ex) {
            mdlRoleList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), user, db_name), ex);
        }
        return mdlRoleList;
    }
}
