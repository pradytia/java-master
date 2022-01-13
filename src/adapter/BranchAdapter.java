package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.Branch.*;
import model.DataTable.mdlDataTableParam;
import model.Download.mdlDownloadParam;
import model.Query.mdlQueryExecute;
import model.Employee.mdlEmployee;
import model.User.mdlUserBranch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class BranchAdapter {
    final static Logger logger = LogManager.getLogger(BranchAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlBranch> GetBranchCheck(mdlBranch param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String user = param.branch_id;
        List<mdlBranch> _mdlBranchList = new ArrayList<mdlBranch>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        boolean resultExec = false;
        try {
            sql = "{call sp_branch_get(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.branch_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            while (rowset.next()) {
                mdlBranch _mdlBranch = new mdlBranch();
                _mdlBranch.setBranch_id(rowset.getString("branch_id"));
                _mdlBranch.setDistrict_id(rowset.getString("district_id"));
                _mdlBranch.setRegion_id(rowset.getString("region_id"));
                _mdlBranch.setArea_id(rowset.getString("area_id"));
                _mdlBranch.setBranch_name(rowset.getString("branch_name"));
                _mdlBranch.setBranch_description(rowset.getString("branch_description"));
                _mdlBranch.setLatitude(rowset.getString("latitude"));
                _mdlBranch.setLongitude(rowset.getString("longitude"));
                _mdlBranch.setCity(rowset.getString("city"));
                _mdlBranch.setPost_code(rowset.getString("post_code"));
                _mdlBranch.setCountry_region_code(rowset.getString("country_region_code"));
                _mdlBranch.setPhone(rowset.getString("phone"));
                _mdlBranch.setFax(rowset.getString("fax"));
                _mdlBranch.setCreated_by(rowset.getString("created_by"));
                _mdlBranch.setCreated_date(rowset.getString("created_date"));
                _mdlBranch.setUpdated_by(rowset.getString("updated_by"));
                _mdlBranch.setUpdated_date(rowset.getString("updated_date"));
                _mdlBranchList.add(_mdlBranch);
            }
        } catch (Exception ex) {
            _mdlBranchList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlBranchList;
    }

    public static List<model.Branch.mdlBranch> GetBranch(mdlDownloadParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String user = param.employee_id;
        List<model.Branch.mdlBranch> _mdlBranchList = new ArrayList<mdlBranch>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_branch_list()}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            while (rowset.next()) {
                model.Branch.mdlBranch _mdlBranch = new model.Branch.mdlBranch();
                _mdlBranch.setBranch_id(rowset.getString("branch_id"));
                _mdlBranch.setDistrict_id(rowset.getString("district_id"));
                _mdlBranch.setRegion_id(rowset.getString("region_id"));
                _mdlBranch.setArea_id(rowset.getString("area_id"));
                _mdlBranch.setBranch_name(rowset.getString("branch_name"));
                _mdlBranch.setBranch_description(rowset.getString("branch_description"));
                _mdlBranch.setLatitude(rowset.getString("latitude"));
                _mdlBranch.setLongitude(rowset.getString("longitude"));
                _mdlBranch.setCity(rowset.getString("city"));
                _mdlBranch.setPost_code(rowset.getString("post_code"));
                _mdlBranch.setCountry_region_code(rowset.getString("country_region_code"));
                _mdlBranch.setPhone(rowset.getString("phone"));
                _mdlBranch.setFax(rowset.getString("fax"));
                _mdlBranch.setCreated_by(rowset.getString("created_by"));
                _mdlBranch.setCreated_date(rowset.getString("created_date"));
                _mdlBranch.setUpdated_by(rowset.getString("updated_by"));
                _mdlBranch.setUpdated_date(rowset.getString("updated_date"));
                _mdlBranchList.add(_mdlBranch);
            }
        } catch (Exception ex) {
            _mdlBranchList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(param), "", ex.toString(), user, db_name), ex);
        }

        return _mdlBranchList;
    }

    // Get Branch List With Paging
    public static List<mdlBranch> GetBranchWeb(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<mdlBranch> _mdlBranchList = new ArrayList<mdlBranch>();
        CachedRowSet jrs = null;
        String sql = "";
        String search_part = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<>();
        try {
            sql = "{call sp_branch_get_with_paging_v2(?,?,?,?,?)}";
            String search_string = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", search_string));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.name));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.dir));

            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "branchList", db_name, port);
            while (jrs.next()) {
                model.Branch.mdlBranch _mdlBranch = new model.Branch.mdlBranch();
                _mdlBranch.branch_id = jrs.getString("branch_id");
                _mdlBranch.district_id = jrs.getString("district_id");
                _mdlBranch.region_id = jrs.getString("region_id");
                _mdlBranch.area_id = jrs.getString("area_id");
                _mdlBranch.branch_name = jrs.getString("branch_name");
                _mdlBranch.branch_description = jrs.getString("branch_description");
                _mdlBranch.latitude = jrs.getString("latitude");
                _mdlBranch.longitude = jrs.getString("longitude");
                _mdlBranch.city = jrs.getString("city");
                _mdlBranch.post_code = jrs.getString("post_code");
                _mdlBranch.country_region_code = jrs.getString("country_region_code");
                _mdlBranch.phone = jrs.getString("phone");
                _mdlBranch.fax = jrs.getString("fax");
                _mdlBranch.is_active = jrs.getString("is_active");
                _mdlBranch.created_by = jrs.getString("created_by");
                _mdlBranch.created_date = jrs.getString("created_date");
                _mdlBranch.updated_by = jrs.getString("updated_by");
                _mdlBranch.updated_date = jrs.getString("updated_date");
                _mdlBranchList.add(_mdlBranch);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "branchList", db_name), ex);
        }
        return _mdlBranchList;
    }

    public static int GetBranchTotalList(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;
        String sql = "";
        int returnValue = 0;
        String search_part = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        try {
            sql = "{call sp_branch_get_total_list(?)}";
            String search_string = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", search_string));

            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "branchTotal", db_name, port);
            while (crs.next()) {
                returnValue = crs.getInt("total");
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "branchTotal", db_name), ex);
        }
        return returnValue;
    }

    public static boolean UploadBranch(mdlBranch param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_branch_upload(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.branch_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.district_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.region_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.area_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.branch_name));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.branch_description));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.latitude));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.longitude));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.city));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.post_code));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.country_region_code));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.phone));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.fax));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.updated_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean UpdateStatusBranch(mdlBranch param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_branch_update_status(?,?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.branch_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.is_active));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean DeleteBranch(mdlBranch param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_branch_delete(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.branch_id));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static List<mdlBranch> GetDropDownBranch(String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlBranch> mdlBranchList = new ArrayList<mdlBranch>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_dropdown_branch_get}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "role", db_name, port);
            while (rowset.next()) {
                mdlBranch mdlBranch = new mdlBranch();
                mdlBranch.branch_id = rowset.getString("branch_id");
                mdlBranch.branch_name = rowset.getString("branch_name");
                mdlBranchList.add(mdlBranch);
            }
        } catch (Exception ex) {
            mdlBranchList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, "", "", ex.toString(), "role", db_name), ex);
        }
        return mdlBranchList;
    }

    public static List<mdlBranch> GetDropDownBranchMultipeByArea(mdlBranchParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlBranch> mdlBranchList = new ArrayList<mdlBranch>();
        CachedRowSet rowset = null;
        try {
            for (int i = 0; i < param.arr_area_id.size(); i++) {
                List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
                try {
                    sql = "{call sp_dropdown_branch_by_area_get(?)}";
                    listParam.add(QueryAdapter.QueryParam("string", param.arr_area_id.get(i)));
                    rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "role", db_name, port);
                    while (rowset.next()) {
                        mdlBranch mdlBranch = new mdlBranch();
                        mdlBranch.branch_id = rowset.getString("branch_id");
                        mdlBranch.branch_name = rowset.getString("branch_name");
                        mdlBranchList.add(mdlBranch);
                    }
                } catch (Exception ex) {
                    mdlBranchList = null;
                    logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), "role", db_name), ex);
                }
            }
        } catch (Exception ex) {
            mdlBranchList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", ex.toString(), "role", db_name), ex);
        }
        return mdlBranchList;
    }

    public static List<mdlUserBranch> UpdateDropDownRole(mdlUserBranch param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlUserBranch> mdlUserBranchList = new ArrayList<mdlUserBranch>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_dropdown_branch_update(?)}";
            listParam.add(QueryAdapter.QueryParam("string", param.user_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "branch", db_name, port);
            while (rowset.next()) {
                mdlUserBranch mdlUserBranch = new mdlUserBranch();
                mdlUserBranch.user_id = rowset.getString("user_id");
                mdlUserBranch.branch_id = rowset.getString("branch_id");
                mdlUserBranch.branch_name = rowset.getString("branch_name");
                mdlUserBranch.region_id = rowset.getString("region_id");
                mdlUserBranch.district_id = rowset.getString("district_id");
                mdlUserBranch.area_id = rowset.getString("area_id");
                mdlUserBranchList.add(mdlUserBranch);
            }
        } catch (Exception ex) {
            mdlUserBranchList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), "branch", db_name), ex);
        }
        return mdlUserBranchList;
    }

    public static List<mdlDailyMessageBranch> UpdateDropDownDailyMessage(mdlDailyMessageBranch param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlDailyMessageBranch> mdlDailyMessageBranchList = new ArrayList<mdlDailyMessageBranch>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_dropdown_branch_update_by_daily_message(?)}";
            listParam.add(QueryAdapter.QueryParam("string", param.message_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "branch", db_name, port);
            while (rowset.next()) {
                mdlDailyMessageBranch _mdlDailyMessageBranch = new mdlDailyMessageBranch();
                _mdlDailyMessageBranch.employee_id = rowset.getString("employee_id");
                _mdlDailyMessageBranch.branch_id = rowset.getString("branch_id");
                mdlDailyMessageBranchList.add(_mdlDailyMessageBranch);
            }
        } catch (Exception ex) {
            mdlDailyMessageBranchList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), "branch", db_name), ex);
        }
        return mdlDailyMessageBranchList;
    }

    public static List<mdlBranchEmployee> GetDropDownBranchEmployee(mdlEmployee param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlBranchEmployee> _mdlBranchEmployeeList = new ArrayList<mdlBranchEmployee>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_dropdown_branch_vs_employee(?)}";
            listParam.add(QueryAdapter.QueryParam("string", param.employee_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "branch", db_name, port);
            while (rowset.next()) {
                mdlBranchEmployee _mdlBranchEmployee = new mdlBranchEmployee();
                _mdlBranchEmployee.branch_id = rowset.getString("branch_id");
                _mdlBranchEmployee.branch_name = rowset.getString("branch_name");
                _mdlBranchEmployeeList.add(_mdlBranchEmployee);
            }
        } catch (Exception ex) {
            _mdlBranchEmployeeList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), "branch", db_name), ex);
        }
        return _mdlBranchEmployeeList;
    }


    public static List<mdlBranchEmployee> GetDropDownBranchByEmployee(mdlBranchParamEmployee param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlBranchEmployee> _mdlBranchEmployeeList = new ArrayList<mdlBranchEmployee>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        try {
            if (param.employee_id != null) {
                for (int i = 0; i < param.employee_id.size(); i++) {
                    CachedRowSet rowset = null;
                    listParam = new ArrayList<model.Query.mdlQueryExecute>();
                    try {
                        sql = "{call sp_dropdown_branch_vs_employee(?)}";
                        listParam.add(QueryAdapter.QueryParam("string", param.employee_id.size() != 0 ? param.employee_id.get(i) : ""));

                        rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "branch", db_name, port);
                        while (rowset.next()) {
                            mdlBranchEmployee _mdlBranchEmployee = new mdlBranchEmployee();
                            _mdlBranchEmployee.branch_id = rowset.getString("branch_id");
                            _mdlBranchEmployee.branch_name = rowset.getString("branch_name");
                            _mdlBranchEmployeeList.add(_mdlBranchEmployee);
                        }
                    } catch (Exception ex) {
                        _mdlBranchEmployeeList = null;
                        logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), "branch", db_name), ex);
                    }
                }
            } else {
                CachedRowSet rowset = null;
                listParam = new ArrayList<model.Query.mdlQueryExecute>();
                sql = "{call sp_dropdown_branch_vs_employee(?)}";
                listParam.add(QueryAdapter.QueryParam("string", ""));
                rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "branch", db_name, port);
                while (rowset.next()) {
                    mdlBranchEmployee _mdlBranchEmployee = new mdlBranchEmployee();
                    _mdlBranchEmployee.branch_id = rowset.getString("branch_id");
                    _mdlBranchEmployee.branch_name = rowset.getString("branch_name");
                    _mdlBranchEmployeeList.add(_mdlBranchEmployee);
                }
            }
        } catch (Exception ex) {
            _mdlBranchEmployeeList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), "branch", db_name), ex);
        }
        return _mdlBranchEmployeeList;
    }

    public static List<mdlBranch> GetDropDownBranchAllForCustomReport(String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlBranch> mdlBranchList = new ArrayList<mdlBranch>();
        CachedRowSet rowset = null;
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();

        try {
            sql = "{call sp_branch_dropdown_all}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "branch", db_name, port);
            while (rowset.next()) {
                mdlBranch _mdlBranch = new mdlBranch();
                _mdlBranch.branch_id = rowset.getString("branch_id");
                _mdlBranch.branch_name = rowset.getString("branch_name");
                mdlBranchList.add(_mdlBranch);
            }

        } catch (Exception ex) {
            mdlBranchList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, "", "", ex.toString(), "branch", db_name), ex);
        }
        return mdlBranchList;
    }
}
