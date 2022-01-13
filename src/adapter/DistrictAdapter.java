package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.District.mdlDistrict;
import model.District.mdlDistrictParam;
import model.Query.mdlQueryExecute;
import model.DataTable.mdlDataTableParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class DistrictAdapter {
    final static Logger logger = LogManager.getLogger(DistrictAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlDistrict> GetDistrictCheck(mdlDistrict param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String user = param.district_id;
        List<mdlDistrict> _mdlDistrictList = new ArrayList<mdlDistrict>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        boolean resultExec = false;
        try {
            sql = "{call sp_district_get(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.district_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            while (rowset.next()) {
                mdlDistrict _mdlDistrict = new mdlDistrict();
                _mdlDistrict.district_id = rowset.getString("district_id");
                _mdlDistrict.region_id = rowset.getString("region_id");
                _mdlDistrict.district_name = rowset.getString("district_name");
                _mdlDistrict.is_active = rowset.getString("is_active");
                _mdlDistrict.created_by = rowset.getString("created_by");
                _mdlDistrict.created_date = rowset.getString("created_date");
                _mdlDistrict.updated_by = rowset.getString("updated_by");
                _mdlDistrict.updated_date = rowset.getString("updated_date");
                _mdlDistrictList.add(_mdlDistrict);
            }
        } catch (Exception ex) {
            _mdlDistrictList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlDistrictList;
    }

    public static List<mdlDistrict> GetDistrictWeb(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlDistrict> _mdlDistrictList = new ArrayList<mdlDistrict>();
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        CachedRowSet jrs = null;
        try {
            sql = "{call sp_district_get_paging_v2(?,?,?,?,?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.name));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.dir));

            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "District", db_name, port);
            while (jrs.next()) {
                mdlDistrict _mdlDistrict = new mdlDistrict();
                _mdlDistrict.district_id = jrs.getString("district_id");
                _mdlDistrict.region_id = jrs.getString("region_id");
                _mdlDistrict.district_name = jrs.getString("district_name");
                _mdlDistrict.created_by = jrs.getString("created_by");
                _mdlDistrict.created_date = jrs.getString("created_date");
                _mdlDistrict.updated_by = jrs.getString("updated_by");
                _mdlDistrict.updated_date = jrs.getString("updated_date");
                _mdlDistrict.is_active = jrs.getString("is_active");
                _mdlDistrictList.add(_mdlDistrict);
            }
        } catch (Exception ex) {
            _mdlDistrictList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "District", db_name), ex);
        }
        return _mdlDistrictList;
    }

    public static int GetDistrictList(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        int returnValue = 0;
        CachedRowSet crs = null;
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_district_get_total_list(?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "District", db_name, port);
            while (crs.next()) {
                returnValue = crs.getInt("total");
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "", db_name), ex);
        }
        return returnValue;
    }

    public static boolean UploadDistrictWeb(mdlDistrict param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String user = param.created_by;
        boolean resultExec = false;
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_district_upload(?,?,?,?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.district_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.region_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.district_name));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.updated_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "", db_name), ex);
        }
        return resultExec;
    }

    public static boolean DeleteDistrict(mdlDistrictParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = "district";
        String sql = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_district_delete(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.district_id));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "", db_name), ex);
        }
        return resultExec;
    }

    public static List<mdlDistrict> GetDropDownDistrictByRegion(mdlDistrictParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlDistrict> mdlDistrictList = new ArrayList<mdlDistrict>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;

        try {
            sql = "{call sp_district_dropdown_by_region_id(?)}";
            listParam.add(QueryAdapter.QueryParam("string", param.region_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "district", db_name, port);
            while (rowset.next()) {
                mdlDistrict _mdlDistrict = new mdlDistrict();
                _mdlDistrict.district_id = rowset.getString("district_id");
                _mdlDistrict.district_name = rowset.getString("district_name");
                mdlDistrictList.add(_mdlDistrict);
            }
        } catch (Exception ex) {
            mdlDistrictList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(listParam), "", ex.toString(), "district", db_name), ex);
        }
        return mdlDistrictList;
    }

    public static List<mdlDistrict> GetDropDownMultipleDistrictByRegion(mdlDistrictParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlDistrict> mdlDistrictList = new ArrayList<mdlDistrict>();
        CachedRowSet rowset = null;
        try {
            for (int i = 0; i < param.arr_region_id.size(); i++) {
                List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
                try {
                    sql = "{call sp_district_dropdown_by_region_id(?)}";
                    listParam.add(QueryAdapter.QueryParam("string", param.arr_region_id.get(i)));
                    rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "district", db_name, port);
                    while (rowset.next()) {
                        mdlDistrict _mdlDistrict = new mdlDistrict();
                        _mdlDistrict.district_id = rowset.getString("district_id");
                        _mdlDistrict.district_name = rowset.getString("district_name");
                        mdlDistrictList.add(_mdlDistrict);
                    }
                } catch (Exception ex) {
                    logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), "district", db_name), ex);
                }
            }

        } catch (Exception ex) {
            mdlDistrictList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", ex.toString(), "district", db_name), ex);
        }
        return mdlDistrictList;
    }

    public static List<mdlDistrict> GetDropDownDistrict(String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlDistrict> mdlDistrictList = new ArrayList<mdlDistrict>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_district_dropdown}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "district", db_name, port);
            while (rowset.next()) {
                mdlDistrict _mdlDistrict = new mdlDistrict();
                _mdlDistrict.district_id = rowset.getString("district_id");
                _mdlDistrict.district_name = rowset.getString("district_name");
                mdlDistrictList.add(_mdlDistrict);
            }
        } catch (Exception ex) {
            mdlDistrictList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), "district", db_name), ex);
        }
        return mdlDistrictList;
    }
}
