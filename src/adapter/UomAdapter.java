package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.DataTable.mdlDataTableParam;
import model.Query.mdlQueryExecute;
import model.Uom.mdlUom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class UomAdapter {
    final static Logger logger = LogManager.getLogger(UomAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlUom> GetUomCheck(mdlUom param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param.uom;

        List<mdlUom> _mdlUomList = new ArrayList<mdlUom>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        boolean resultExec = false;

        try {
            sql = "{call sp_uom_get(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.uom));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            while (rowset.next()) {
                model.Uom.mdlUom _mdlUom = new model.Uom.mdlUom();
                _mdlUom.uom = rowset.getString("uom");
                _mdlUom.description = rowset.getString("description");
                _mdlUom.created_by = rowset.getString("created_by");
                _mdlUom.created_date = rowset.getString("created_date");
                _mdlUom.updated_by = rowset.getString("updated_by");
                _mdlUom.updated_date = rowset.getString("updated_date");
                _mdlUom.is_active = rowset.getString("is_active");
                _mdlUomList.add(_mdlUom);
            }
        } catch (Exception ex) {
            _mdlUomList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlUomList;
    }

    public static List<mdlUom> GetUomWeb(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet jrs = null;
        String sql = "";
        String search_part = "";
        String user = param.module_id;

        List<model.Uom.mdlUom> _mdlUomList = new ArrayList<>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        try {
            sql = "{call sp_uom_get_with_paging_v2(?,?,?,?,?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            listParam.add(QueryAdapter.QueryParam("string", searchString));
            listParam.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            listParam.add(QueryAdapter.QueryParam("int", param.page_size));
            listParam.add(QueryAdapter.QueryParam("string", param.order.name));
            listParam.add(QueryAdapter.QueryParam("string", param.order.dir));
            jrs = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "Product Type List", db_name, port);

            while (jrs.next()) {
                model.Uom.mdlUom _mdlUom = new model.Uom.mdlUom();
                _mdlUom.uom = jrs.getString("uom");
                _mdlUom.description = jrs.getString("description");
                _mdlUom.created_by = jrs.getString("created_by");
                _mdlUom.created_date = jrs.getString("created_date");
                _mdlUom.updated_by = jrs.getString("updated_by");
                _mdlUom.updated_date = jrs.getString("updated_date");
                _mdlUom.is_active = jrs.getString("is_active");
                _mdlUomList.add(_mdlUom);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), user, db_name), ex);
        }
        return _mdlUomList;
    }

    public static int GetUomTotalList(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;
        String sql = "";
        int returnValue = 0;
        String search_part = "";
        String user = param.module_id;

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        try {
            sql = "{call sp_uom_get_total_list(?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Uom Total", db_name, port);
            while (crs.next()) {
                returnValue = crs.getInt("total");
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return returnValue;
    }

    public static boolean UploadUom(mdlUom param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";
        List<model.Query.mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();

        try {
            sql = "{call sp_uom_upload(?,?,?,?)}";
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.uom));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.description));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.created_by));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.updated_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listMdlQueryExecute), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean DeleteUom(mdlUom param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_uom_delete(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.uom));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static List<mdlUom> GetDropDownUom(String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = "GetDropDownUom";
        List<mdlUom> mdlUomList = new ArrayList<mdlUom>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;

        try {
            sql = "{call sp_uom_dropdown_get}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "uom", db_name, port);
            while (rowset.next()) {
                mdlUom _mdlUom = new mdlUom();
                _mdlUom.uom = rowset.getString("uom");
                _mdlUom.description = rowset.getString("description");
                mdlUomList.add(_mdlUom);
            }
        } catch (Exception ex) {
            mdlUomList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), user, db_name), ex);
        }
        return mdlUomList;
    }
}
