package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.Competitor.mdlCompetitor;
import model.DataTable.mdlDataTableParam;
import model.Download.mdlDownloadParam;
import model.Query.mdlQueryExecute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;


public class CompetitorAdapter {
    final static Logger logger = LogManager.getLogger(CompetitorAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlCompetitor> GetCompetitorCheck(mdlCompetitor param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String user = param.competitor_id;
        List<mdlCompetitor> _mdlCompetitorList = new ArrayList<mdlCompetitor>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        boolean resultExec = false;
        try {
            sql = "{call sp_competitor_get(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.competitor_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            while (rowset.next()) {
                mdlCompetitor _mdlCompetitor = new mdlCompetitor();
                _mdlCompetitor.setCompetitor_id(rowset.getString("competitor_id"));
                _mdlCompetitor.setCompetitor_name(rowset.getString("competitor_name"));
                _mdlCompetitor.setCreated_date(rowset.getString("created_date"));
                _mdlCompetitor.setCreated_by(rowset.getString("created_by"));
                _mdlCompetitor.setUpdated_date(rowset.getString("updated_date"));
                _mdlCompetitor.setUpdated_by(rowset.getString("updated_by"));
                _mdlCompetitorList.add(_mdlCompetitor);
            }
        } catch (Exception ex) {
            _mdlCompetitorList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlCompetitorList;
    }

    public static List<model.Competitor.mdlCompetitor> GetCompetitor(mdlDownloadParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String user = param.employee_id;
        List<model.Competitor.mdlCompetitor> _mdlCompetitorList = new ArrayList<mdlCompetitor>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_competitor_list()}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            while (rowset.next()) {
                model.Competitor.mdlCompetitor _mdlCompetitor = new model.Competitor.mdlCompetitor();
                _mdlCompetitor.setCompetitor_id(rowset.getString("competitor_id"));
                _mdlCompetitor.setCompetitor_name(rowset.getString("competitor_name"));
                _mdlCompetitor.setCreated_date(rowset.getString("created_date"));
                _mdlCompetitor.setCreated_by(rowset.getString("created_by"));
                _mdlCompetitor.setUpdated_date(rowset.getString("updated_date"));
                _mdlCompetitor.setUpdated_by(rowset.getString("updated_by"));
                _mdlCompetitorList.add(_mdlCompetitor);
            }
        } catch (Exception ex) {
            _mdlCompetitorList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlCompetitorList;
    }

    //====================================WEB========================================================\\

    public static List<mdlCompetitor> GetCompetitorWeb(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlCompetitor> _mdlCompetitorList = new ArrayList<mdlCompetitor>();
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        CachedRowSet jrs = null;
        try {
            sql = "{call sp_competitor_get_with_paging_v2(?,?,?,?,?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.name));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.dir));

            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "competitor list", db_name, port);
            while (jrs.next()) {
                model.Competitor.mdlCompetitor _mdlCompetitor = new model.Competitor.mdlCompetitor();
                _mdlCompetitor.competitor_id = jrs.getString("competitor_id");
                _mdlCompetitor.competitor_name = jrs.getString("competitor_name");
                _mdlCompetitor.created_date = jrs.getString("created_date");
                _mdlCompetitor.created_by = jrs.getString("created_by");
                _mdlCompetitor.updated_date = jrs.getString("updated_date");
                _mdlCompetitor.updated_by = jrs.getString("updated_by");
                _mdlCompetitor.is_active = jrs.getString("is_active");
                _mdlCompetitorList.add(_mdlCompetitor);
            }
        } catch (Exception ex) {
            _mdlCompetitorList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "competitor list", db_name), ex);
        }
        return _mdlCompetitorList;
    }

    public static int GetCompetitorList(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        int returnValue = 0;
        String sql = "";
        CachedRowSet crs = null;
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_competitor_get_total_list(?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "competitor total", db_name, port);
            while (crs.next()) {
                returnValue = crs.getInt("total");
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "competitor total", db_name), ex);
        }
        return returnValue;
    }

    public static boolean UploadCompetitorWeb(mdlCompetitor param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        try {
            sql = "{call sp_competitor_upload(?,?,?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.competitor_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.competitor_name));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.updated_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean DeleteCompetitor(mdlCompetitor param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_competitor_delete(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.competitor_id));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }
}
