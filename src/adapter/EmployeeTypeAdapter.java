package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.DataTable.mdlDataTableParam;
import model.Employee.mdlEmployeeType;
import model.Employee.mdlEmployeeTypeParam;
import model.Query.mdlQueryExecute;
import model.User.mdlUserBranch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class EmployeeTypeAdapter {
    final static Logger logger = LogManager.getLogger(EmployeeTypeAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlEmployeeType> GetEmployeeTypeCheck(mdlEmployeeType param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String user = param.employee_type_id;

        List<mdlEmployeeType> _mdlEmployeeTypeList = new ArrayList<mdlEmployeeType>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        boolean resultExec = false;
        try {
            sql = "{call sp_employee_type_get(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.employee_type_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            while (rowset.next()) {
                mdlEmployeeType _mdlEmployeeType = new mdlEmployeeType();
                _mdlEmployeeType.employee_type_id = rowset.getString("employee_type_id");
                _mdlEmployeeType.employee_type_name = rowset.getString("employee_type_name");
                _mdlEmployeeType.description = rowset.getString("description");
                _mdlEmployeeType.is_active = rowset.getString("is_active");
                _mdlEmployeeType.created_by = rowset.getString("created_by");
                _mdlEmployeeType.created_date = rowset.getString("created_date");
                _mdlEmployeeType.updated_by = rowset.getString("updated_by");
                _mdlEmployeeType.updated_date = rowset.getString("updated_date");
                _mdlEmployeeTypeList.add(_mdlEmployeeType);
            }
        } catch (Exception ex) {
            _mdlEmployeeTypeList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlEmployeeTypeList;
    }

    public static List<mdlEmployeeType> GetEmployeeTypeWeb(mdlEmployeeType param1, mdlDataTableParam param2, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<model.Employee.mdlEmployeeType> _mdlEmployeeTypeList = new ArrayList<mdlEmployeeType>();
        CachedRowSet jrs = null;
        String sql = "";
        String search_part = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<>();
        try {
            sql = "{call sp_employee_type_get_with_paging_v2(?,?,?,?,?,?)}";
            String searchString = "%" + param2.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param1.module_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param2.page_number - 1) * param2.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param2.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param2.order.name));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param2.order.dir));
            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Employee Type List", db_name, port);
            while (jrs.next()) {
                mdlEmployeeType _mdlEmployeeType = new mdlEmployeeType();
                _mdlEmployeeType.employee_type_id = jrs.getString("employee_type_id");
                _mdlEmployeeType.employee_type_name = jrs.getString("employee_type_name");
                _mdlEmployeeType.description = jrs.getString("description");
                _mdlEmployeeType.created_by = jrs.getString("created_by");
                _mdlEmployeeType.created_date = jrs.getString("created_date");
                _mdlEmployeeType.updated_by = jrs.getString("updated_by");
                _mdlEmployeeType.updated_date = jrs.getString("updated_date");
                _mdlEmployeeType.is_active = jrs.getString("is_active");
                _mdlEmployeeTypeList.add(_mdlEmployeeType);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "", db_name), ex);
        }
        return _mdlEmployeeTypeList;
    }

    public static int GetEmployeeTypeTotalList(mdlEmployeeType param1, mdlDataTableParam param2, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;
        String sql = "";
        int returnValue = 0;
        String search_part = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        try {
            sql = "{call sp_employee_type_get_total_list(?,?)}";
            String searchString = "%" + param2.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param1.module_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Customer Type Total", db_name, port);
            while (crs.next()) {
                returnValue = crs.getInt("total");
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "", db_name), ex);
        }
        return returnValue;
    }

    public static boolean UploadEmployeeType(mdlEmployeeType param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String user = param.created_by;
        boolean resultExec = false;
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_employee_type_upload(?,?,?,?,?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.employee_type_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.employee_type_name));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.description));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.module_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.updated_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean DeleteEmployeeType(mdlEmployeeType param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = "area";
        String sql = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            //Check Mst Question
            int checkQuestion = CheckEmployeeTypeQuestion(param, db_name, port);

            if(checkQuestion > 0){
                resultExec = false;
            }else{
                sql = "{call sp_employee_type_delete(?)}";
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.employee_type_id));
                resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            }

        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static List<mdlEmployeeType> GetDropDownEmployeeType(String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlEmployeeType> mdlEmployeeTypeList = new ArrayList<mdlEmployeeType>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_employee_type_dropdown_get}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "employee_type", db_name, port);

            while (rowset.next()) {
                mdlEmployeeType _mdlEmployeeType = new mdlEmployeeType();
                _mdlEmployeeType.employee_type_id = rowset.getString("employee_type_id");
                _mdlEmployeeType.employee_type_name = rowset.getString("employee_type_name");
                mdlEmployeeTypeList.add(_mdlEmployeeType);
            }
        } catch (Exception ex) {
            mdlEmployeeTypeList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), "", db_name), ex);
        }
        return mdlEmployeeTypeList;
    }

    public static List<mdlUserBranch> UpdateDropDownEmployeeType(mdlEmployeeTypeParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlUserBranch> mdlUserBranchList = new ArrayList<mdlUserBranch>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_dropdown_branch_update(?)}";
            listParam.add(QueryAdapter.QueryParam("string", param.employee_type_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "branch", db_name, port);
            while (rowset.next()) {
                mdlUserBranch mdlUserBranch = new mdlUserBranch();
                mdlUserBranch.user_id = rowset.getString("user_id");
                mdlUserBranch.branch_id = rowset.getString("branch_id");
                mdlUserBranchList.add(mdlUserBranch);
            }
        } catch (Exception ex) {
            mdlUserBranchList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), "", db_name), ex);
        }
        return mdlUserBranchList;
    }

    public static int CheckEmployeeTypeQuestion(mdlEmployeeType param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;
        String sql = "";
        int returnValue = 0;
        String search_part = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        try {
            sql = "{call sp_employee_type_get_question_total(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.employee_type_id));
            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Customer Type Total", db_name, port);
            while (crs.next()) {
                returnValue = crs.getInt("total");
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "", db_name), ex);
        }
        return returnValue;
    }
}
