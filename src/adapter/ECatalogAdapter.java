package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.DataTable.mdlDataTableParam;
import model.ECatalog.mdlECatalog;
import model.Query.mdlQueryExecute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class ECatalogAdapter {
    final static Logger logger = LogManager.getLogger(ECatalogAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlECatalog> GetECatalogheck(mdlECatalog param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String user = param.e_catalog_id;
        List<mdlECatalog> _mdlEcatalogList = new ArrayList<mdlECatalog>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        boolean resultExec = false;
        try {
            sql = "{call sp_e_catalog_get_id(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.e_catalog_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            while (rowset.next()) {
                mdlECatalog _mdlEcatalog = new mdlECatalog();
                _mdlEcatalog.setE_catalog_id(rowset.getString("e_catalog_id"));
                _mdlEcatalog.setFile_name(rowset.getString("file_name"));
                _mdlEcatalog.setPath(rowset.getString("path"));
                _mdlEcatalog.setType_of_file(rowset.getString("type_of_file"));
                _mdlEcatalog.setTitle(rowset.getString("title"));
                _mdlEcatalog.setCreated_by(rowset.getString("created_by"));
                _mdlEcatalog.setCreated_date(rowset.getString("created_date"));
                _mdlEcatalog.setUpdated_by(rowset.getString("updated_by"));
                _mdlEcatalog.setUpdated_date(rowset.getString("updated_date"));
                _mdlEcatalog.setIs_active(rowset.getString("is_active"));
                _mdlEcatalogList.add(_mdlEcatalog);
                resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name);
            }
        } catch (Exception ex) {
            _mdlEcatalogList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlEcatalogList;
    }

    public static List<mdlECatalog> GetECatalog(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<mdlQueryExecute>();
        List<mdlECatalog> _mdlECatalogList = new ArrayList<mdlECatalog>();
        String Query = "sp_e_catalog_get_v2 ?,?,?,?,? ";
        CachedRowSet crs = null;
        try {
            listParam.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            listParam.add(QueryAdapter.QueryParam("int", param.page_size));
            listParam.add(QueryAdapter.QueryParam("string", param.search));
            listParam.add(QueryAdapter.QueryParam("string", param.order.name));
            listParam.add(QueryAdapter.QueryParam("string", param.order.dir));
            crs = QueryAdapter.QueryExecuteWithDB(Query, listParam, "GetECatalog", "", db_name, port);
            if (crs.next() == false) {
                _mdlECatalogList = null;
            } else {
                do {
                    mdlECatalog _mdlECatalog = new mdlECatalog();

                    _mdlECatalog.e_catalog_id = crs.getString("e_catalog_id");
                    _mdlECatalog.file_name = crs.getString("file_name");
                    _mdlECatalog.title = crs.getString("title");
                    _mdlECatalog.is_active = crs.getString("is_active");

                    _mdlECatalogList.add(_mdlECatalog);
                } while (crs.next());
            }
        } catch (Exception ex) {
            _mdlECatalogList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, Query, gson.toJson(listParam), "", ex.toString(), "", db_name), ex);
        }
        return _mdlECatalogList;
    }

    public static boolean InsertECatalog(mdlECatalog param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        boolean resultExec = false;
        List<mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_e_catalog_upload(?,?,?,?,?,?,?)}";
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.e_catalog_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.file_name));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.path));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.type_of_file));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.title));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.created_by));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.updated_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, "user", db_name, port);

            if (resultExec) {

            } else {

            }

        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listMdlQueryExecute), "", ex.toString(), "user", db_name), ex);
            return false;
        }
        return true;
    }

    public static boolean DeleteECatalog(mdlECatalog param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String sqlDetail = "";
        String sqlImage = "";
        boolean resultExec = false;
        boolean resultExecDetail = false;
        boolean resultExecImage = false;
        List<mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();
        try {
            sqlImage = "{call sp_e_catalog_delete (?)}";
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.e_catalog_id));
            resultExecImage = QueryAdapter.QueryManipulateWithDB(sqlImage, listMdlQueryExecute, functionName, "user", db_name, port);

        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sqlImage, gson.toJson(listMdlQueryExecute), "", ex.toString(), "", db_name), ex);
            return false;
        }
        return true;
    }

    public static int GetTotalECatalog(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        int return_value = 0;
        String sql = "";
        List<mdlECatalog> mdlECatalogList = new ArrayList<mdlECatalog>();
        List<mdlQueryExecute> listParam = new ArrayList<mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_e_catalog_total_get (?)}";
            listParam.add(QueryAdapter.QueryParam("string", param.search));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "User", db_name, port);
            while (rowset.next()) {
                return_value = rowset.getInt("total");
            }
        } catch (Exception ex) {
            return_value = 0;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), "", db_name), ex);
        }
        return return_value;
    }
}
