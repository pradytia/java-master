package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controller.v1.RegionController;
import core.LogAdapter;
import database.QueryAdapter;
import model.DataTable.mdlDataTableParam;
import model.Query.mdlQueryExecute;
import model.Region.mdlRegion;
import model.Region.mdlRegionParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class RegionAdapter {
    final static Logger logger = LogManager.getLogger(RegionAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlRegion> GetRegionCheck(mdlRegion param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param.region_id;

        List<mdlRegion> _mdlRegionList = new ArrayList<mdlRegion>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        boolean resultExec = false;

        try {
            sql = "{call sp_region_get(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.region_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            while (rowset.next()) {
                mdlRegion _mdlRegion = new mdlRegion();
                _mdlRegion.region_id = rowset.getString("region_id");
                _mdlRegion.region_name = rowset.getString("region_name");
                _mdlRegion.is_active = rowset.getString("is_active");
                _mdlRegion.created_by = rowset.getString("created_by");
                _mdlRegion.created_date = rowset.getString("created_date");
                _mdlRegion.updated_by = rowset.getString("updated_by");
                _mdlRegion.updated_date = rowset.getString("updated_date");
                _mdlRegionList.add(_mdlRegion);
            }
        } catch (Exception ex) {
            _mdlRegionList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlRegionList;
    }

    public static List<mdlRegion> GetRegionWeb(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String user = param.module_id;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<mdlRegion> _mdlRegionList = new ArrayList<mdlRegion>();
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        CachedRowSet jrs = null;

        try {
            sql = "{call sp_region_get_with_paging_v2(?,?,?,?,?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.name));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.dir));
            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Region", db_name, port);

            while (jrs.next()) {
                mdlRegion _mdlRegion = new mdlRegion();
                _mdlRegion.region_id = jrs.getString("region_id");
                _mdlRegion.region_name = jrs.getString("region_name");
                _mdlRegion.created_by = jrs.getString("created_by");
                _mdlRegion.created_date = jrs.getString("created_date");
                _mdlRegion.updated_by = jrs.getString("updated_by");
                _mdlRegion.updated_date = jrs.getString("updated_date");
                _mdlRegion.is_active = jrs.getString("is_active");
                _mdlRegionList.add(_mdlRegion);
            }

        } catch (Exception ex) {
            _mdlRegionList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlRegionList;
    }

    public static int GetRegionList(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        int returnValue = 0;
        String sql = "";
        String user = param.module_id;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;

        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_region_total_list(?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));

            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Region", db_name, port);
            while (crs.next()) {
                returnValue = crs.getInt("total");
            }

        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return returnValue;
    }

    public static boolean UploadRegionWeb(mdlRegion param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String user = param.created_by;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;

        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_region_upload(?,?,?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.region_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.region_name));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.updated_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean DeleteRegion(mdlRegionParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = "region";
        String sql = "";

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_region_delete(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.region_id));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static List<mdlRegion> GetDropDownRegion(String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = "GetDropDownRegion";

        List<mdlRegion> mdlRegionList = new ArrayList<mdlRegion>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_region_dropdown_get}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "region", db_name, port);

            while (rowset.next()) {
                mdlRegion _mdlRegion = new mdlRegion();
                _mdlRegion.region_id = rowset.getString("region_id");
                _mdlRegion.region_name = rowset.getString("region_name");
                mdlRegionList.add(_mdlRegion);
            }
        } catch (Exception ex) {
            mdlRegionList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), user, db_name), ex);
        }
        return mdlRegionList;
    }
}
