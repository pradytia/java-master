package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.Answer.mdlAnswer;
import model.Cost.mdlCost;
import model.DataTable.mdlDataTableParam;
import model.Download.mdlDownloadParam;
import model.Query.mdlQueryExecute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class CostAdapter {
    final static Logger logger = LogManager.getLogger(CostAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<model.Cost.mdlCost> GetCost(mdlDownloadParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String user = param.employee_id;
        List<model.Cost.mdlCost> _mdlCostList = new ArrayList<mdlCost>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_cost_list()}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            while (rowset.next()) {
                model.Cost.mdlCost _mdlCost = new model.Cost.mdlCost();
                _mdlCost.setCost_id(rowset.getString("cost_id"));
                _mdlCost.setCost_name(rowset.getString("cost_name"));
                _mdlCost.setType(rowset.getString("type"));
                _mdlCost.setCreated_by(rowset.getString("created_by"));
                _mdlCost.setCreated_date(rowset.getString("created_date"));
                _mdlCost.setUpdated_by(rowset.getString("updated_by"));
                _mdlCost.setUpdated_date(rowset.getString("updated_date"));
                _mdlCostList.add(_mdlCost);
            }
        } catch (Exception ex) {
            _mdlCostList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlCostList;
    }

    public static List<mdlCost> GetCostWeb(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<model.Cost.mdlCost> _mdlCostList = new ArrayList<>();
        CachedRowSet jrs = null;
        String sql = "";
        String search_part = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<>();
        try {
            sql = "{call sp_cost_get_with_paging_v2(?,?,?,?,?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.name));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.dir));
            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Cost List", db_name, port);
            while (jrs.next()) {
                model.Cost.mdlCost _mdlCost = new model.Cost.mdlCost();
                _mdlCost.cost_id = jrs.getString("cost_id");
                _mdlCost.cost_name = jrs.getString("cost_name");
                _mdlCost.type = jrs.getString("type");
                _mdlCost.created_by = jrs.getString("created_by");
                _mdlCost.created_date = jrs.getString("created_date");
                _mdlCost.updated_by = jrs.getString("updated_by");
                _mdlCost.updated_date = jrs.getString("updated_date");
                _mdlCost.is_active = jrs.getString("is_active");
                _mdlCostList.add(_mdlCost);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "Cost List", db_name), ex);
        }
        return _mdlCostList;
    }

    public static int GetCostTotalList(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;
        String sql = "";
        int returnValue = 0;
        String search_part = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        try {
            sql = "{call sp_cost_get_total_list(?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Cost Total", db_name, port);
            while (crs.next()) {
                returnValue = crs.getInt("total");
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "Cost Total", db_name), ex);
        }
        return returnValue;
    }

    // Upload Cost
    public static boolean UploadCost(mdlCost param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_cost_upload(?,?,?,?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.cost_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.cost_name));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.type));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.updated_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean DeleteCost(mdlCost param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = "answer";
        String sql = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_cost_delete(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.cost_id));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static List<mdlCost> GetCostDropdown(String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlCost> mdlCostList = new ArrayList<mdlCost>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_cost_dropdown}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "cost", db_name, port);
            while (rowset.next()) {
                mdlCost _mdlCost = new mdlCost();
                _mdlCost.cost_id = rowset.getString("cost_id");
                _mdlCost.type = rowset.getString("type");
                mdlCostList.add(_mdlCost);
            }
        } catch (Exception ex) {
            mdlCostList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", "", "", ex.toString(), "cost", db_name), ex);
        }
        return mdlCostList;
    }
}