package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.DataTable.mdlDataTableParam;
import model.Promo.mdlPromo;
import model.Download.mdlDownloadParam;
import model.Query.mdlQueryExecute;
import model.Result.mdlResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class PromoAdapter {

    final static Logger logger = LogManager.getLogger(PromoAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<model.Promo.mdlPromo> GetPromo(mdlDownloadParam param, String db_name, int port) {

        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param.employee_id;

        List<model.Promo.mdlPromo> _mdlPromoList = new ArrayList<mdlPromo>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;

        try {
            sql = "{call sp_promo_list()}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            while (rowset.next()) {
                model.Promo.mdlPromo _mdlPromo = new model.Promo.mdlPromo();
                _mdlPromo.setPromo_id(rowset.getString("promo_id"));
                _mdlPromo.setPromo_name(rowset.getString("promo_name"));
                _mdlPromo.setPromo_category(rowset.getString("promo_category"));
                _mdlPromo.setCreated_date(rowset.getString("created_date"));
                _mdlPromo.setCreated_by(rowset.getString("created_by"));
                _mdlPromo.setUpdated_date(rowset.getString("updated_date"));
                _mdlPromo.setUpdated_by(rowset.getString("updated_by"));
                _mdlPromoList.add(_mdlPromo);
            }
        } catch (Exception ex) {
            _mdlPromoList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlPromoList;
    }

    //=============================== WEB ==================================================== \\


    public static List<mdlPromo> GetPromoWeb(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param.module_id;

        List<mdlPromo> _mdlPromoList = new ArrayList<mdlPromo>();
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        CachedRowSet jrs = null;
        try {
            sql = "{call sp_promo_get_with_paging(?,?,?)}";
            String search_string = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", search_string));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.page_size));

            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Promo List", db_name, port);
            while (jrs.next()) {
                model.Promo.mdlPromo _mdlPromo = new model.Promo.mdlPromo();
                _mdlPromo.promo_id = jrs.getString("promo_id");
                _mdlPromo.promo_name = jrs.getString("promo_name");
                _mdlPromo.promo_category = jrs.getString("promo_category");
                _mdlPromo.created_date = jrs.getString("created_date");
                _mdlPromo.created_by = jrs.getString("created_by");
                _mdlPromo.updated_date = jrs.getString("updated_date");
                _mdlPromo.updated_by = jrs.getString("updated_by");
                _mdlPromo.is_active = jrs.getString("is_active");
                _mdlPromoList.add(_mdlPromo);
            }
        } catch (Exception ex) {
            _mdlPromoList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlPromoList;
    }

    public static int GetPromoList(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        int return_value = 0;
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;
        String user = param.module_id;

        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_promo_get_total_list(?)}";
            String search_string = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", search_string));
            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Promo Total", db_name, port);
            while (crs.next()) {
                return_value = crs.getInt("total");
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return return_value;
    }

    public static boolean UploadPromoWeb(List<mdlPromo> param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String user = param.get(0).created_by;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            for (mdlPromo params : param) {
                sql = "{call sp_promo_upload(?,?,?,?,?)}";
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", params.promo_id));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", params.promo_name));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", params.promo_category));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", params.created_by));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", params.updated_by));
                resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }
}
