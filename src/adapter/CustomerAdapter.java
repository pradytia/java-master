package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.Customer.*;
import model.DataTable.mdlDataTableParam;
import model.Download.mdlDownloadParam;
import model.Query.mdlQueryExecute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter {
    final static Logger logger = LogManager.getLogger(CustomerAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlCustomer> GetCustomerCheck(mdlCustomer param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param.customer_id;

        List<mdlCustomer> _mdlCustomerList = new ArrayList<mdlCustomer>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        boolean resultExec = false;

        try {
            sql = "{call sp_customer_get(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.customer_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

            while (rowset.next()) {
                model.Customer.mdlCustomer _mdlCustomer = new model.Customer.mdlCustomer();
                _mdlCustomer.setCustomer_id(rowset.getString("customer_id"));
                _mdlCustomer.setBranch_id(rowset.getString("branch_id"));
                _mdlCustomer.setModule_id(rowset.getString("module_id"));
                _mdlCustomer.setCustomer_type_id(rowset.getString("customer_type_id"));
                _mdlCustomer.setCustomer_name(rowset.getString("customer_name"));
                _mdlCustomer.setCustomer_address(rowset.getString("customer_address"));
                _mdlCustomer.setPhone(rowset.getString("phone"));
                _mdlCustomer.setEmail(rowset.getString("email"));
                _mdlCustomer.setPic(rowset.getString("pic"));
                _mdlCustomer.setRadius(rowset.getString("radius"));
                _mdlCustomer.setCity(rowset.getString("city"));
                _mdlCustomer.setCountry_region_code(rowset.getString("country_region_code"));
                _mdlCustomer.setBlocked(rowset.getString("blocked"));
                _mdlCustomer.setAccount(rowset.getString("account"));
                _mdlCustomer.setChannel(rowset.getString("channel"));
                _mdlCustomer.setDistributor(rowset.getString("distributor"));
                _mdlCustomer.setGender(rowset.getString("gender"));
                _mdlCustomer.setCreated_by(rowset.getString("created_by"));
                _mdlCustomer.setCreated_date(rowset.getString("created_date"));
                _mdlCustomer.setUpdated_by(rowset.getString("updated_by"));
                _mdlCustomer.setUpdated_date(rowset.getString("updated_date"));
                _mdlCustomer.setIs_active(rowset.getString("is_active"));
                _mdlCustomerList.add(_mdlCustomer);
                resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name);
            }
        } catch (Exception ex) {
            _mdlCustomerList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlCustomerList;
    }

    public static List<model.Customer.mdlCustomer> GetCustomer(mdlDownloadParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param.employee_id;
        List<model.Customer.mdlCustomer> _mdlCustomerList = new ArrayList<model.Customer.mdlCustomer>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;

        try {
            sql = "{call sp_customer_list(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.module_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            while (rowset.next()) {
                model.Customer.mdlCustomer _mdlCustomer = new model.Customer.mdlCustomer();
                _mdlCustomer.setCustomer_id(rowset.getString("customer_id"));
                _mdlCustomer.setBranch_id(rowset.getString("branch_id"));
                _mdlCustomer.setModule_id(rowset.getString("module_id"));
                _mdlCustomer.setCustomer_type_id(rowset.getString("customer_type_id"));
                _mdlCustomer.setCustomer_name(rowset.getString("customer_name"));
                _mdlCustomer.setCustomer_address(rowset.getString("customer_address"));
                _mdlCustomer.setPhone(rowset.getString("phone"));
                _mdlCustomer.setEmail(rowset.getString("email"));
                _mdlCustomer.setPic(rowset.getString("pic"));
                _mdlCustomer.setLatitude(rowset.getString("latitude"));
                _mdlCustomer.setLongitude(rowset.getString("longitude"));
                _mdlCustomer.setRadius(rowset.getString("radius"));
                _mdlCustomer.setCity(rowset.getString("city"));
                _mdlCustomer.setCountry_region_code(rowset.getString("country_region_code"));
                _mdlCustomer.setBlocked(rowset.getString("blocked"));
                _mdlCustomer.setAccount(rowset.getString("account"));
                _mdlCustomer.setChannel(rowset.getString("channel"));
                _mdlCustomer.setDistributor(rowset.getString("distributor"));
                _mdlCustomer.setGender(rowset.getString("gender"));
                _mdlCustomer.setCreated_by(rowset.getString("created_by"));
                _mdlCustomer.setCreated_date(rowset.getString("created_date"));
                _mdlCustomer.setUpdated_by(rowset.getString("updated_by"));
                _mdlCustomer.setUpdated_date(rowset.getString("updated_date"));
                _mdlCustomerList.add(_mdlCustomer);
            }
        } catch (Exception ex) {
            _mdlCustomerList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "", db_name), ex);
        }
        return _mdlCustomerList;
    }

    public static List<model.Customer.mdlCustomerType> GetCustomerType(mdlDownloadParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param.employee_id;
        List<model.Customer.mdlCustomerType> _mdlCustomerTypeList = new ArrayList<mdlCustomerType>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_customer_type_list()}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

            while (rowset.next()) {
                model.Customer.mdlCustomerType _mdlCustomerType = new model.Customer.mdlCustomerType();
                _mdlCustomerType.setCustomer_type_id(rowset.getString("customer_type_id"));
                _mdlCustomerType.setCustomer_type_name(rowset.getString("customer_type_name"));
                _mdlCustomerType.setDescription(rowset.getString("description"));
                _mdlCustomerType.setCreated_by(rowset.getString("created_by"));
                _mdlCustomerType.setCreated_date(rowset.getString("created_date"));
                _mdlCustomerType.setUpdated_by(rowset.getString("updated_by"));
                _mdlCustomerType.setUpdated_date(rowset.getString("updated_date"));
                _mdlCustomerTypeList.add(_mdlCustomerType);
            }

        } catch (Exception ex) {
            _mdlCustomerTypeList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", ex.toString(), user, db_name), ex);
        }
        return _mdlCustomerTypeList;
    }

    public static List<model.Customer.mdlCustomer> GetCustomerWeb(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<model.Customer.mdlCustomer> _mdlCustomerList = new ArrayList<mdlCustomer>();
        CachedRowSet jrs = null;
        String sql = "";
        String search_part = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<>();
        try {
            sql = "{call sp_customer_get_with_paging_v2(?,?,?,?,?)}";

            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.name));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.dir));
            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "customer list", db_name, port);

            while (jrs.next()) {
                model.Customer.mdlCustomer _mdlCustomer = new model.Customer.mdlCustomer();
                _mdlCustomer.customer_id = jrs.getString("customer_id");
                _mdlCustomer.branch_id = jrs.getString("branch_id");
                _mdlCustomer.customer_name = jrs.getString("customer_name");
                _mdlCustomer.customer_type_id = jrs.getString("customer_type_id");
                _mdlCustomer.customer_address = jrs.getString("customer_address");
                _mdlCustomer.phone = jrs.getString("phone");
                _mdlCustomer.email = jrs.getString("email");
                _mdlCustomer.pic = jrs.getString("pic");
                _mdlCustomer.distributor = jrs.getString("distributor");
                _mdlCustomer.gender = jrs.getString("gender");
                _mdlCustomer.created_by = jrs.getString("created_by");
                _mdlCustomer.created_date = jrs.getString("created_date");
                _mdlCustomer.updated_by = jrs.getString("updated_by");
                _mdlCustomer.updated_date = jrs.getString("updated_date");
                _mdlCustomer.is_active = jrs.getString("is_active");
                _mdlCustomerList.add(_mdlCustomer);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "customer list", db_name), ex);
        }
        return _mdlCustomerList;
    }

    public static int GetCustomerTotalList(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;
        String sql = "";
        int returnValue = 0;
        String search_part = "";
        try {

            List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();

            sql = "{call sp_customer_get_total_list(?)}";

            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));

            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "customer total", db_name, port);

            while (crs.next()) {
                returnValue = crs.getInt("total");
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", ex.toString(), "customer total", db_name), ex);
        }
        return returnValue;
    }

    // Upload Customer
    public static boolean UploadCustomer(mdlCustomer param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_customer_upload(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.customer_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.branch_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.module_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.customer_type_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.customer_name));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.customer_address));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.phone));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.email));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.pic));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.radius));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.city));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.country_region_code));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.blocked));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.account));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.channel));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.distributor));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.gender));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.updated_by));

            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean UpdateStatusCustomer(mdlCustomer param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_customer_update_status(?,?,?)}";

            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.customer_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.is_active));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));

            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean DeleteCustomer(mdlCustomer param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {

            sql = "{call sp_customer_delete_v1(?)}";

            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.customer_id));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);


        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }


    public static boolean CheckVisitByCstID(mdlCustomer param, String db_name, int port) {

        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";
        Integer total = 0;
        CachedRowSet rowset = null;

        try {
            List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
            sql = "{call sp_check_trx_visit_by_cst_id(?)}";

            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.customer_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

            while (rowset.next()) {
                total = rowset.getInt("total");
            }

            if (total <= 0) {
                resultExec = true;
            } else {
                resultExec = false;
            }

        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(param), "", ex.toString(), user, db_name), ex);
        }


        return resultExec;
    }

    public static boolean CheckCallPlanByCstID(mdlCustomer param, String db_name, int port) {

        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";
        Integer total = 0;
        CachedRowSet rowset = null;
        try {

            List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

            sql = "{call sp_check_call_plan_by_cst_id(?)}";

            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.customer_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

            while (rowset.next()) {
                total = rowset.getInt("total");
            }

            if (total <= 0) {
                resultExec = true;
            } else {
                resultExec = false;
            }

        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(param), "", ex.toString(), user, db_name), ex);
        }

        return resultExec;
    }

    public static boolean CheckCustomerNpwpByCstID(mdlCustomer param, String db_name, int port) {

        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";
        Integer total = 0;
        CachedRowSet rowset = null;
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {


            sql = "{call sp_check_customer_npwp_by_cst_id(?)}";

            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.customer_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

            while (rowset.next()) {
                total = rowset.getInt("total");
            }

            if (total <= 0) {
                resultExec = true;
            } else {
                resultExec = false;
            }

        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }

        return resultExec;
    }

    public static boolean CheckCustomerCoordinateByCstID(mdlCustomer param, String db_name, int port) {

        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";
        Integer total = 0;
        CachedRowSet rowset = null;
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {

            sql = "{call sp_check_customer_coordinate_by_cst_id(?)}";

            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.customer_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

            while (rowset.next()) {
                total = rowset.getInt("total");
            }

            if (total <= 0) {
                resultExec = true;
            } else {
                resultExec = false;
            }

        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }

        return resultExec;
    }

    public static boolean CheckCustomerInfoByCstID(mdlCustomer param, String db_name, int port) {

        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";
        Integer total = 0;
        CachedRowSet rowset = null;
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {


            sql = "{call sp_check_customer_info_by_cst_id(?)}";

            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.customer_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

            while (rowset.next()) {
                total = rowset.getInt("total");
            }

            if (total <= 0) {
                resultExec = true;
            } else {
                resultExec = false;
            }

        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }

        return resultExec;
    }

    public static List<mdlCustomer> GetDropDownCustomerByBranch(mdlCustomerParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();

        List<mdlCustomer> mdlCustomerList = new ArrayList<mdlCustomer>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;

        try {
            sql = "{call sp_customer_dropdown_by_branch_id(?)}";
            listParam.add(QueryAdapter.QueryParam("string", param.branch_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "customer", db_name, port);

            while (rowset.next()) {
                mdlCustomer _mdlCustomer = new mdlCustomer();
                _mdlCustomer.customer_id = rowset.getString("customer_id");
                _mdlCustomer.customer_name = rowset.getString("customer_name");
                _mdlCustomer.total_address = rowset.getString("total_address");
                mdlCustomerList.add(_mdlCustomer);
            }
        } catch (Exception ex) {
            mdlCustomerList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), "customer", db_name), ex);
        }
        return mdlCustomerList;
    }

    public static List<mdlCustomer> GetDropDownCustomerByBranchExcCPDet(mdlCustomerParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();

        List<mdlCustomer> mdlCustomerList = new ArrayList<mdlCustomer>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;

        try {
            sql = "{call sp_customer_dropdown_by_branch_id_exc_cp_detail(?,?)}";
            listParam.add(QueryAdapter.QueryParam("string", param.branch_id));
            listParam.add(QueryAdapter.QueryParam("string", param.call_plan_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "customer", db_name, port);

            while (rowset.next()) {
                mdlCustomer _mdlCustomer = new mdlCustomer();
                _mdlCustomer.customer_id = rowset.getString("customer_id");
                _mdlCustomer.customer_name = rowset.getString("customer_name");
                mdlCustomerList.add(_mdlCustomer);
            }
        } catch (Exception ex) {
            mdlCustomerList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), "customer", db_name), ex);
        }
        return mdlCustomerList;
    }

    public static List<mdlCustomerVisitTracking> GetCustomerCoordinate(mdlCustomerParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String user = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<mdlCustomerVisitTracking> _mdlCustomerVisitTrackingList = new ArrayList<mdlCustomerVisitTracking>();
        CachedRowSet rowset = null;

        try {
            for (int i = 0; i < param.customer_id.size(); i++) {
                List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
                try {
                    sql = "{call sp_customer_coordinate_by_visit_tracking_get(?)}";
                    listParam.add(QueryAdapter.QueryParam("string", param.customer_id.get(i)));
                    rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, user, db_name, port);
                    while (rowset.next()) {
                        mdlCustomerVisitTracking _mdlCustomerVisitTracking = new mdlCustomerVisitTracking();
                        _mdlCustomerVisitTracking.customer_id = rowset.getString("customer_id");
                        _mdlCustomerVisitTracking.customer_name = rowset.getString("customer_name");
                        _mdlCustomerVisitTracking.customer_address = rowset.getString("customer_address");
                        _mdlCustomerVisitTracking.latitude = rowset.getString("latitude");
                        _mdlCustomerVisitTracking.longitude = rowset.getString("longitude");
                        _mdlCustomerVisitTracking.radius = rowset.getString("radius");
                        _mdlCustomerVisitTracking.city = rowset.getString("city");
                        _mdlCustomerVisitTracking.country_region_code = rowset.getString("country_region_code");
                        _mdlCustomerVisitTrackingList.add(_mdlCustomerVisitTracking);
                    }
                } catch (Exception ex) {
                    logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), user, db_name), ex);
                }
            }
        } catch (Exception ex) {
            _mdlCustomerVisitTrackingList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", ex.toString(), user, db_name), ex);
        }

        return _mdlCustomerVisitTrackingList;
    }

    public static List<model.Customer.mdlCustomer> GetCustomerDropdownforCallplan(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<model.Customer.mdlCustomer> _mdlCustomerList = new ArrayList<mdlCustomer>();
        CachedRowSet jrs = null;
        String sql = "";
        String search_part = "";

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<>();

        try {
            sql = "{call sp_customer_get_with_paging_for_call_plan(?,?,?,?)}";

            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.branch_id));

            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "customer list", db_name, port);

            while (jrs.next()) {

                model.Customer.mdlCustomer _mdlCustomer = new model.Customer.mdlCustomer();

                _mdlCustomer.customer_id = jrs.getString("customer_id");
                _mdlCustomer.branch_id = jrs.getString("branch_id");
                _mdlCustomer.customer_name = jrs.getString("customer_name");
                _mdlCustomer.customer_address = jrs.getString("customer_address");
                _mdlCustomerList.add(_mdlCustomer);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "customer list", db_name), ex);
        }
        return _mdlCustomerList;
    }

    public static int GetCustomerTotalListCallplan(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;
        String sql = "";
        int returnValue = 0;
        String search_part = "";
        try {

            List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();

            sql = "{call sp_customer_get_total_list_for_call_plan(?,?)}";

            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.branch_id));

            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "customer total", db_name, port);

            while (crs.next()) {
                returnValue = crs.getInt("total");
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", ex.toString(), "customer total", db_name), ex);
        }
        return returnValue;
    }
}
