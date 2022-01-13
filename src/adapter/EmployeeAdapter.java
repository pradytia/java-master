package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.DataTable.mdlDataTableParam;
import model.Employee.mdlEmployee;
import model.Employee.mdlEmployeeParam;
import model.Query.mdlQueryExecute;
import model.Select2.mdlSelect2Param;
import model.User.mdlUserBranch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.StringUtil;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class EmployeeAdapter {
    final static Logger logger = LogManager.getLogger(EmployeeAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlEmployee> GetEmployeeCheck(mdlEmployee param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param.employee_id;

        List<mdlEmployee> _mdlEmployeeList = new ArrayList<mdlEmployee>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        boolean resultExec = false;

        try {

            sql = "{call sp_employee_get_id(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.employee_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            while (rowset.next()) {
                model.Employee.mdlEmployee _mdlEmployee = new model.Employee.mdlEmployee();
                _mdlEmployee.employee_id = rowset.getString("employee_id");
                _mdlEmployee.employee_name = rowset.getString("employee_name");
                _mdlEmployee.employee_type_id = rowset.getString("employee_type_id");
                _mdlEmployee.branch_id = rowset.getString("branch_id");
                _mdlEmployee.entry_date = rowset.getString("entry_date");
                _mdlEmployee.gender = rowset.getString("gender");
                _mdlEmployee.email = rowset.getString("email");
                _mdlEmployee.phone = rowset.getString("phone");
                _mdlEmployee.out_date = rowset.getString("out_date");
                _mdlEmployee.created_by = rowset.getString("created_by");
                _mdlEmployee.created_date = rowset.getString("created_date");
                _mdlEmployee.updated_by = rowset.getString("updated_by");
                _mdlEmployee.updated_date = rowset.getString("updated_date");
                _mdlEmployee.is_active = rowset.getString("is_active");
                _mdlEmployeeList.add(_mdlEmployee);
            }
        } catch (Exception ex) {
            _mdlEmployeeList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", ex.toString(), user, db_name), ex);
        }
        return _mdlEmployeeList;
    }

    public static List<mdlEmployee> GetEmployeeLicense(mdlSelect2Param param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<model.Employee.mdlEmployee> _mdlEmployeeList = new ArrayList<>();
        CachedRowSet jrs = null;
        String sql = "";
        String search_part = "";

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<>();

        try {
            sql = "{call sp_employee_license_get(?, ?)}";
            if (param.param_type.equals("update")) {
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.id));
            } else {
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("null", java.sql.Types.NULL));
            }
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.module_id));
            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Employee License List", db_name, port);

            while (jrs.next()) {
                model.Employee.mdlEmployee _mdlEmployee = new model.Employee.mdlEmployee();
                _mdlEmployee.employee_id = jrs.getString("employee_id");
                _mdlEmployee.employee_name = jrs.getString("employee_name");
                _mdlEmployee.employee_type_id = jrs.getString("employee_type_id");
                _mdlEmployee.branch_id = jrs.getString("branch_id");
                _mdlEmployee.entry_date = jrs.getString("entry_date");
                _mdlEmployeeList.add(_mdlEmployee);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", ex.toString(), "", db_name), ex);
        }

        return _mdlEmployeeList;
    }

    public static List<mdlEmployee> GetEmployeeWeb(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<model.Employee.mdlEmployee> _mdlEmployeeList = new ArrayList<>();
        CachedRowSet jrs = null;
        String sql = "";
        String search_part = "";

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<>();

        try {

            sql = "{call sp_employee_get_with_paging_v2(?,?,?,?,?)}";

            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
//            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.module_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.name));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.dir));

            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Employee List", db_name, port);

            while (jrs.next()) {

                model.Employee.mdlEmployee _mdlEmployee = new model.Employee.mdlEmployee();

                _mdlEmployee.employee_id = jrs.getString("employee_id");
                _mdlEmployee.employee_name = jrs.getString("employee_name");
                _mdlEmployee.employee_type_id = jrs.getString("employee_type_id");
                _mdlEmployee.employee_type_name = jrs.getString("employee_type_name");
                _mdlEmployee.branch_id = jrs.getString("branch_id");
                _mdlEmployee.entry_date = jrs.getString("entry_date");
                _mdlEmployee.out_date = jrs.getString("out_date");
                _mdlEmployee.gender = jrs.getString("gender");
                _mdlEmployee.phone = jrs.getString("phone");
                _mdlEmployee.email = jrs.getString("email");
                _mdlEmployee.created_by = jrs.getString("created_by");
                _mdlEmployee.created_date = jrs.getString("created_date");
                _mdlEmployee.updated_by = jrs.getString("updated_by");
                _mdlEmployee.updated_date = jrs.getString("updated_date");
                _mdlEmployee.is_active = jrs.getString("is_active");
                _mdlEmployee.is_register = db_name.contains("db_csp") ? jrs.getString("is_register") : "";

                _mdlEmployeeList.add(_mdlEmployee);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", ex.toString(), "", db_name), ex);
        }

        return _mdlEmployeeList;
    }

    public static List<mdlEmployee> GetEmployeeIsUnregisterWeb(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<model.Employee.mdlEmployee> _mdlEmployeeList = new ArrayList<mdlEmployee>();
        CachedRowSet jrs = null;
        String sql = "";
        String search_part = "";

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<>();

        try {

            sql = "{call sp_employee_is_unregister_get_with_paging(?, ?, ?)}";

            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
//            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.module_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.page_size));

            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Employee List", db_name, port);

            while (jrs.next()) {

                model.Employee.mdlEmployee _mdlEmployee = new model.Employee.mdlEmployee();

                _mdlEmployee.employee_id = jrs.getString("employee_id");
                _mdlEmployee.employee_name = jrs.getString("employee_name");
                _mdlEmployee.employee_type_id = jrs.getString("employee_type_id");
                _mdlEmployee.employee_type_name = jrs.getString("employee_type_name");
                _mdlEmployee.branch_id = jrs.getString("branch_id");
                _mdlEmployee.entry_date = jrs.getString("entry_date");
                _mdlEmployee.out_date = jrs.getString("out_date");
                _mdlEmployee.gender = jrs.getString("gender");
                _mdlEmployee.phone = jrs.getString("phone");
                _mdlEmployee.email = jrs.getString("email");
                _mdlEmployee.created_by = jrs.getString("created_by");
                _mdlEmployee.created_date = jrs.getString("created_date");
                _mdlEmployee.updated_by = jrs.getString("updated_by");
                _mdlEmployee.updated_date = jrs.getString("updated_date");
                _mdlEmployee.is_active = jrs.getString("is_active");
                _mdlEmployee.is_register = db_name.contains("db_csp") ? jrs.getString("is_register") : "";

                _mdlEmployeeList.add(_mdlEmployee);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", ex.toString(), "", db_name), ex);
        }

        return _mdlEmployeeList;
    }

    public static int GetEmployeeTotalList(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;
        String sql = "";
        int returnValue = 0;
        String search_part = "";

        try {

            List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();

            sql = "{call sp_employee_get_total_list(?)}";

            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
//            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.module_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));

            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Employee List", db_name, port);

            while (crs.next()) {
                returnValue = crs.getInt("total");
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", ex.toString(), "", db_name), ex);
        }
        return returnValue;
    }

    public static int GetEmployeeIsUnregisterTotalList(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;
        String sql = "";
        int returnValue = 0;
        String search_part = "";

        try {

            List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();

            sql = "{call sp_employee_is_unregister_get_total_list(?)}";

            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
//            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.module_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));

            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Employee List", db_name, port);

            while (crs.next()) {
                returnValue = crs.getInt("total");
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", ex.toString(), "", db_name), ex);
        }
        return returnValue;
    }

    // Upload Employee
    public static boolean UploadEmployee(mdlEmployee param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        boolean resultExec_del = false;

        String user = param.created_by;
        String sql = "";

        try {
            List<model.Query.mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();

            sql = "{call sp_employee_upload(?,?,?,?,?,?,?,?,?,?,?)}";

            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", StringUtil.removeSpecialCharAndWhiteSpace(param.employee_id)));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.employee_name));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.employee_type_id));
//            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.branch_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", ""));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.phone));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.email));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.gender));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.entry_date));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.out_date));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.created_by));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.updated_by));

            resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, user, db_name, port);

            listMdlQueryExecute = new ArrayList<mdlQueryExecute>();
            String sqlDelete = "{call sp_employee_vs_branch_delete(?)}";
            List<model.Query.mdlQueryExecute> listMdlQueryExecuteDelete = new ArrayList<mdlQueryExecute>();
            listMdlQueryExecuteDelete.add(QueryAdapter.QueryParam("string", param.employee_id));
            resultExec_del = QueryAdapter.QueryManipulateWithDB(sqlDelete, listMdlQueryExecuteDelete, functionName, user, db_name, port);
            if (resultExec_del) {
                for (String lbranch_id : param.branch_id_list) {
                    sql = "{call sp_employee_vs_branch_upload(?,?,?)}";
                    List<model.Query.mdlQueryExecute> listMdlQueryExecute2 = new ArrayList<mdlQueryExecute>();
                    listMdlQueryExecute2.add(QueryAdapter.QueryParam("string", StringUtil.removeSpecialCharAndWhiteSpace(param.employee_id)));
                    listMdlQueryExecute2.add(QueryAdapter.QueryParam("string", lbranch_id));
                    listMdlQueryExecute2.add(QueryAdapter.QueryParam("string", param.created_by));
                    resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute2, functionName, user, db_name, port);
                }
            }

        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean UpdateStatusEmployee(mdlEmployee param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";

        try {
            List<model.Query.mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();
            sql = "{call sp_employee_update_status(?,?,?)}";
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.employee_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.is_active));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.created_by));

            resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean UpdateStatusIsRegisterEmployee(mdlEmployee param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";

        try {
            List<model.Query.mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();
            sql = "{call sp_employee_update_status_is_register(?,?,?)}";
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.employee_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.is_register));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.created_by));

            resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean DeleteEmployee(mdlEmployee param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";

        try {
            List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
            sql = "{call sp_employee_delete(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.employee_id));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static List<mdlEmployee> GetDropDownEmployee(mdlEmployeeParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();

        List<mdlEmployee> mdlEmployeeList = new ArrayList<mdlEmployee>();

        try {
            for (int i = 0; i < param.branch_id.size(); i++) {
                CachedRowSet rowset = null;
                List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
                String branch_id = "";
                branch_id = param.branch_id.get(i);
                sql = "sp_employee_dropdown_by_branch_id ?";
                listParam.add(QueryAdapter.QueryParam("string", branch_id));
                rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "employee", db_name, port);
                while (rowset.next()) {
                    mdlEmployee _mdlEmployee = new mdlEmployee();
                    _mdlEmployee.employee_id = rowset.getString("employee_id");
                    _mdlEmployee.employee_name = rowset.getString("employee_name");
                    mdlEmployeeList.add(_mdlEmployee);
                }
            }
        } catch (Exception ex) {
            mdlEmployeeList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", ex.toString(), "", db_name), ex);
        }
        return mdlEmployeeList;
    }

    public static List<mdlEmployee> GetDropDownEmployeeByBranch(mdlEmployee param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();

        List<mdlEmployee> mdlEmployeeList = new ArrayList<mdlEmployee>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_employee_dropdown_by_branch_id(?)}";
            listParam.add(QueryAdapter.QueryParam("string", param.branch_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "employee", db_name, port);

            while (rowset.next()) {
                mdlEmployee _mdlEmployee = new mdlEmployee();
                _mdlEmployee.employee_id = rowset.getString("employee_id");
                _mdlEmployee.employee_name = rowset.getString("employee_name");
                mdlEmployeeList.add(_mdlEmployee);
            }
        } catch (Exception ex) {
            mdlEmployeeList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", ex.toString(), "", db_name), ex);
        }
        return mdlEmployeeList;
    }

    public static List<mdlEmployee> GetDropDownEmployeeAll(String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();

        List<mdlEmployee> mdlEmployeeList = new ArrayList<mdlEmployee>();

        try {
            CachedRowSet rowset = null;
            List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();

            sql = "{call sp_employee_dropdown_all}";

            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "employee", db_name, port);

            while (rowset.next()) {
                mdlEmployee _mdlEmployee = new mdlEmployee();
                _mdlEmployee.employee_id = rowset.getString("employee_id");
                _mdlEmployee.employee_name = rowset.getString("employee_name");
                mdlEmployeeList.add(_mdlEmployee);
            }

        } catch (Exception ex) {
            mdlEmployeeList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", "", "", ex.toString(), "", db_name), ex);
        }
        return mdlEmployeeList;
    }

    public static List<mdlUserBranch> UpdateDropDownEmployee(mdlUserBranch param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();

        List<mdlUserBranch> mdlUserEmployeeList = new ArrayList<mdlUserBranch>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;

        try {
            sql = "{call sp_dropdown_employee_update(?)}";
            listParam.add(QueryAdapter.QueryParam("string", param.user_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "employee", db_name, port);
            while (rowset.next()) {
                mdlUserBranch mdlUserBranch = new mdlUserBranch();
                mdlUserBranch.user_id = rowset.getString("user_id");
                mdlUserBranch.employee_id = rowset.getString("employee_id");
                mdlUserBranch.employee_name = rowset.getString("employee_name");
                mdlUserEmployeeList.add(mdlUserBranch);
            }
        } catch (Exception ex) {
            mdlUserEmployeeList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", ex.toString(), "", db_name), ex);
        }
        return mdlUserEmployeeList;
    }

    //digunakan untuk menu kustom
    public static List<mdlEmployee> GetDropDownEmployeeLaporan(mdlEmployeeParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();

        List<mdlEmployee> mdlEmployeeList = new ArrayList<mdlEmployee>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;

        try {
            sql = "{call sp_dropdown_employee_by_user_get(?)}";
            listParam.add(QueryAdapter.QueryParam("string", param.user_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "employee", db_name, port);

            while (rowset.next()) {
                mdlEmployee mdlEmployee = new mdlEmployee();
                mdlEmployee.employee_id = rowset.getString("employee_id");
                mdlEmployee.employee_name = rowset.getString("employee_name");
                mdlEmployeeList.add(mdlEmployee);
            }
        } catch (Exception ex) {
            mdlEmployeeList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", "", "", ex.toString(), "", db_name), ex);
        }
        return mdlEmployeeList;
    }


    public static List<String> GetBranchIDsByEmployeeID(String employeeID, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<String> branchIDs = new ArrayList<>();
        CachedRowSet jrs = null;
        String sql = "";
        String search_part = "";

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<>();
        try {
            sql = "{call sp_employee_vs_branch_get_by_employee(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", employeeID));
            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "system", db_name, port);
            while (jrs.next()) {
                String branchID = jrs.getString("branch_id");
                branchIDs.add(branchID);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", employeeID, "", ex.toString(), "", db_name), ex);
        }

        return branchIDs;
    }

    public static List<mdlEmployee> GetDropDownEmployeeForCallPlan(mdlEmployeeParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();

        List<mdlEmployee> mdlEmployeeList = new ArrayList<mdlEmployee>();

        try {

            for (int i = 0; i < param.branch_id.size(); i++) {
                CachedRowSet rowset = null;
                List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();

                String branch_id = "";
                branch_id = param.branch_id.get(i);

                sql = "sp_employee_dropdown_by_branch_id_for_call_plan ?";
                listParam.add(QueryAdapter.QueryParam("string", branch_id));

                rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "employee", db_name, port);

                while (rowset.next()) {
                    mdlEmployee _mdlEmployee = new mdlEmployee();
                    _mdlEmployee.employee_id = rowset.getString("employee_id");
                    _mdlEmployee.employee_name = rowset.getString("employee_name");
                    mdlEmployeeList.add(_mdlEmployee);
                }
            }
        } catch (Exception ex) {
            mdlEmployeeList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", ex.toString(), "", db_name), ex);
        }
        return mdlEmployeeList;
    }

}
