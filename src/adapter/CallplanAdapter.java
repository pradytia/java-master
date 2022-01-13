package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.Callplan.mdlCallplan;
import model.Download.mdlDownloadParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class CallplanAdapter {
    final static Logger logger = LogManager.getLogger(CallplanAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<model.Callplan.mdlCallplan> GetCallplan(mdlDownloadParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String user = param.employee_id;
        List<mdlCallplan> _mdlCallplanList = new ArrayList<mdlCallplan>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_call_plan_get(?,?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.employee_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.start_date));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.end_date));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            while (rowset.next()) {
                model.Callplan.mdlCallplan _mdlCallplan = new model.Callplan.mdlCallplan();
                _mdlCallplan.setCall_plan_id(rowset.getString("call_plan_id"));
                _mdlCallplan.setEmployee_id(rowset.getString("employee_id"));
                _mdlCallplan.setBranch_id(rowset.getString("branch_id"));
                _mdlCallplan.setVehicle_id(rowset.getString("vehicle_id"));
                _mdlCallplan.setDate(rowset.getString("date_"));
                _mdlCallplan.setIs_finish(rowset.getString("is_finish"));
                _mdlCallplan.setIs_download(rowset.getString("is_download"));
                _mdlCallplan.setCreated_by(rowset.getString("created_by"));
                _mdlCallplan.setCreated_date(rowset.getString("created_date"));
                _mdlCallplan.setLast_updated_by(rowset.getString("updated_by"));
                _mdlCallplan.setLast_date(rowset.getString("updated_date"));
                _mdlCallplanList.add(_mdlCallplan);
            }
        } catch (Exception ex) {
            _mdlCallplanList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlCallplanList;
    }
}
