package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.Area.mdlArea;
import model.Area.mdlAreaParam;
import model.DataTable.mdlDataTableParam;
import model.Query.mdlQueryExecute;
import model.Result.mdlResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class AreaAdapter {
    final static Logger logger = LogManager.getLogger(AreaAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlArea> GetAreaCheck(mdlArea param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String user = param.area_id;
        List<mdlArea> _mdlAreaList = new ArrayList<mdlArea>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        boolean resultExec = false;
        try {
            sql = "{call sp_area_get(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.area_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            while (rowset.next()) {
                mdlArea _mdlArea = new mdlArea();
                _mdlArea.setArea_id(rowset.getString("area_id"));
                _mdlArea.setdistrict_id(rowset.getString("district_id"));
                _mdlArea.setArea_name(rowset.getString("area_name"));
                _mdlArea.setIs_active(rowset.getString("is_active"));
                _mdlArea.setCreated_by(rowset.getString("created_by"));
                _mdlArea.setCreated_date(rowset.getString("created_date"));
                _mdlArea.setUpdated_by(rowset.getString("updated_by"));
                _mdlArea.setUpdated_date(rowset.getString("updated_date"));
                _mdlAreaList.add(_mdlArea);
            }
        } catch (Exception ex) {
            _mdlAreaList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlAreaList;
    }

    public static List<mdlArea> GetAreaWeb(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlArea> _mdlAreaList = new ArrayList<mdlArea>();
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        CachedRowSet jrs = null;
        try {
            sql = "{call sp_area_get_with_paging_v2(?,?,?,?,?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.name));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.dir));

            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "area", db_name, port);
            while (jrs.next()) {
                mdlArea _mdlArea = new mdlArea();
                _mdlArea.area_id = jrs.getString("area_id");
                _mdlArea.district_id = jrs.getString("district_id");
                _mdlArea.area_name = jrs.getString("area_name");
                _mdlArea.created_by = jrs.getString("created_by");
                _mdlArea.created_date = jrs.getString("created_date");
                _mdlArea.updated_by = jrs.getString("updated_by");
                _mdlArea.updated_date = jrs.getString("updated_date");
                _mdlArea.is_active = jrs.getString("is_active");
                _mdlAreaList.add(_mdlArea);
            }
        } catch (Exception ex) {
            _mdlAreaList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "area", db_name), ex);
        }
        return _mdlAreaList;
    }

    public static int GetAreaList(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        int returnValue = 0;
        String sql = "";
        CachedRowSet crs = null;
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_area_get_total_list(?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));

            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "area", db_name, port);
            while (crs.next()) {
                returnValue = crs.getInt("total");
            }
        } catch (Exception ex) {
            returnValue = 0;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "area", db_name), ex);
        }
        return returnValue;
    }

    public static boolean UploadAreaWeb(mdlArea param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String user = param.created_by;
        boolean resultExec = false;
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_area_upload(?,?,?,?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.area_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.area_name));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.district_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.updated_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean DeleteArea(mdlAreaParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = "area";
        String sql = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_area_delete(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.area_id));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static List<mdlArea> GetDropDownAreaByDistrict(mdlAreaParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlArea> mdlAreaList = new ArrayList<mdlArea>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_area_dropdown_by_district_id(?)}";
            listParam.add(QueryAdapter.QueryParam("string", param.district_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "area", db_name, port);
            while (rowset.next()) {
                mdlArea _mdlArea = new mdlArea();
                _mdlArea.area_id = rowset.getString("area_id");
                _mdlArea.area_name = rowset.getString("area_name");
                mdlAreaList.add(_mdlArea);
            }
        } catch (Exception ex) {
            mdlAreaList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), "area", db_name), ex);
        }
        return mdlAreaList;
    }

    public static List<mdlArea> GetDropDownAreaMultipleByDistrict(mdlAreaParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlArea> mdlAreaList = new ArrayList<mdlArea>();
        CachedRowSet rowset = null;
        try {
            for (int i = 0; i < param.arr_district_id.size(); i++) {
                List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
                try {
                    sql = "{call sp_area_dropdown_by_district_id(?)}";
                    listParam.add(QueryAdapter.QueryParam("string", param.arr_district_id.get(i)));
                    rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "area", db_name, port);

                    while (rowset.next()) {
                        mdlArea _mdlArea = new mdlArea();
                        _mdlArea.area_id = rowset.getString("area_id");
                        _mdlArea.area_name = rowset.getString("area_name");
                        mdlAreaList.add(_mdlArea);
                    }
                } catch (Exception ex) {
                    logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), "area", db_name), ex);
                }
            }
        } catch (Exception ex) {
            mdlAreaList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(param), "", ex.toString(), "area", db_name), ex);
        }
        return mdlAreaList;
    }

    public static List<mdlArea> GetDropDownArea(String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlArea> mdlAreaList = new ArrayList<mdlArea>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_area_dropdown}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "area", db_name, port);

            while (rowset.next()) {
                mdlArea _mdlArea = new mdlArea();
                _mdlArea.area_id = rowset.getString("area_id");
                _mdlArea.area_name = rowset.getString("area_name");
                mdlAreaList.add(_mdlArea);
            }
        } catch (Exception ex) {
            mdlAreaList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, "", "", ex.toString(), "area", db_name), ex);
        }
        return mdlAreaList;
    }
}
