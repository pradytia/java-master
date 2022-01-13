package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.DataTable.mdlDataTableParam;
import model.Download.mdlDownloadParam;
import model.POSM.mdlPOSMVisit;
import model.Query.mdlQueryExecute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class POSMVisitAdapter {

    final static Logger logger = LogManager.getLogger(POSMVisitAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlPOSMVisit> GetPOSMVisit(mdlDownloadParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param.employee_id;

        List<model.POSM.mdlPOSMVisit> _mdlPOSMVisitList = new ArrayList<mdlPOSMVisit>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_posm_visit_list()}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            while (rowset.next()) {
                model.POSM.mdlPOSMVisit _mdlPOSMVisit = new model.POSM.mdlPOSMVisit();
                _mdlPOSMVisit.setVisit_id(rowset.getString("visit_id"));
                _mdlPOSMVisit.setCustomer_id(rowset.getString("customer_name"));
                _mdlPOSMVisit.setPosm_id(rowset.getString("posm_id"));
                _mdlPOSMVisit.setEmployee_id(rowset.getString("employee_name"));
                _mdlPOSMVisit.setBranch_id(rowset.getString("branch_id"));
                _mdlPOSMVisit.setQuantity(rowset.getString("quantity"));
                _mdlPOSMVisit.setCreated_date(rowset.getString("created_date"));
                _mdlPOSMVisit.setCreated_by(rowset.getString("created_by"));
                _mdlPOSMVisit.setUpdated_by(rowset.getString("updated_by"));
                _mdlPOSMVisit.setUpdated_date(rowset.getString("updated_date"));
                _mdlPOSMVisitList.add(_mdlPOSMVisit);
            }
        } catch (Exception ex) {
            _mdlPOSMVisitList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, "", "", ex.toString(), user, db_name), ex);
        }
        return _mdlPOSMVisitList;
    }

    public static List<mdlPOSMVisit> GetPOSMVisitWeb(mdlDataTableParam param, String db_name,int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<model.POSM.mdlPOSMVisit> posm_list = new ArrayList<>();
        CachedRowSet jrs = null;
        String sql = "";
        String search_part = "";
        String user = param.module_id;

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<>();

        try {
            sql = "{call sp_posm_visit_get_with_paging(?,?,?)}";
            String search_string = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", search_string));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.page_size));
            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "POSMVisitList", db_name, port);

            while (jrs.next()) {
                model.POSM.mdlPOSMVisit mdl_posm_visit = new model.POSM.mdlPOSMVisit();
                mdl_posm_visit.visit_id = jrs.getString("visit_id");
                mdl_posm_visit.customer_id = jrs.getString("customer_id");
                mdl_posm_visit.posm_id = jrs.getString("posm_id");
                mdl_posm_visit.employee_id = jrs.getString("employee_id");
                mdl_posm_visit.branch_id = jrs.getString("branch_id");
                mdl_posm_visit.quantity = jrs.getString("quantity");
                mdl_posm_visit.created_by = jrs.getString("created_by");
                mdl_posm_visit.created_date = jrs.getString("created_date");
                mdl_posm_visit.updated_by = jrs.getString("updated_by");
                mdl_posm_visit.updated_date = jrs.getString("updated_date");
                mdl_posm_visit.is_active = jrs.getString("is_active");
                posm_list.add(mdl_posm_visit);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }

        return posm_list;
    }

    public static int GetPOSMVisitTotalList(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;
        String sql = "";
        int return_value = 0;
        String search_part = "";
        String user = param.module_id;
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();

        try {
            sql = "{call sp_posm_visit_get_total_list(?)}";
            String search_string = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", search_string));
            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "POSM Visit Total", db_name, port);
            while (crs.next()) {
                return_value = crs.getInt("total");
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return return_value;
    }

    // Upload POSM Visit

    public static boolean UploadPOSMVisit(List<model.POSM.mdlPOSMVisit> posm_params, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean result_exec = false;
        String user = posm_params.get(0).created_by;
        String sql = "";

        List<model.Query.mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();
        try {
            for (model.POSM.mdlPOSMVisit posm_param : posm_params) {
                sql = "{call sp_posm_visit_upload(?,?,?,?,?,?,?,?)}";
                listMdlQueryExecute.add(QueryAdapter.QueryParam("string", posm_param.visit_id));
                listMdlQueryExecute.add(QueryAdapter.QueryParam("string", posm_param.customer_id));
                listMdlQueryExecute.add(QueryAdapter.QueryParam("string", posm_param.posm_id));
                listMdlQueryExecute.add(QueryAdapter.QueryParam("string", posm_param.employee_id));
                listMdlQueryExecute.add(QueryAdapter.QueryParam("string", posm_param.branch_id));
                listMdlQueryExecute.add(QueryAdapter.QueryParam("string", posm_param.quantity));
                listMdlQueryExecute.add(QueryAdapter.QueryParam("string", posm_param.created_by));
                listMdlQueryExecute.add(QueryAdapter.QueryParam("string", posm_param.updated_by));
                result_exec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, user, db_name, port);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listMdlQueryExecute), "", ex.toString(), user, db_name), ex);
        }
        return result_exec;
    }
}
