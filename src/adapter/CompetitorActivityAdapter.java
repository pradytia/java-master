package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.Competitor.mdlCompetitorActivity;
import model.Query.mdlQueryExecute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class CompetitorActivityAdapter {
    final static Logger logger = LogManager.getLogger(CompetitorActivityAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static boolean UploadCompetitorActivityTrxWeb(List<mdlCompetitorActivity> param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String user = param.get(0).created_by;
        boolean resultExec = false;
        try {
            for (mdlCompetitorActivity _mdlCompetitorActivity : param) {
                List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
                try {
                    sql = "{call sp_competitor_activity_upload(?,?,?,?,?,?,?,?,?,?,?)}";
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCompetitorActivity.competitor_activity_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCompetitorActivity.visit_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCompetitorActivity.competitor_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCompetitorActivity.competitor_activity_type_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCompetitorActivity.competitor_product_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCompetitorActivity.customer_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCompetitorActivity.employee_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCompetitorActivity.branch_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCompetitorActivity.note));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCompetitorActivity.created_by));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCompetitorActivity.updated_by));
                    resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
                } catch (Exception ex) {
                    logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
                }
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(param), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }
}
