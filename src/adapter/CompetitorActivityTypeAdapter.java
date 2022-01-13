package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.Competitor.mdlCompetitorActivityType;
import model.Query.mdlQueryExecute;
import model.Result.mdlResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class CompetitorActivityTypeAdapter {
    final static Logger logger = LogManager.getLogger(CompetitorActivityTypeAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static boolean UploadActivityWeb(List<mdlCompetitorActivityType> param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String user = param.get(0).created_by;
        boolean resultExec = false;
        try {
            for (mdlCompetitorActivityType _mdlCompetitorActivityType : param) {
                List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
                try {
                    sql = "{call sp_competitor_activity_type_upload(?,?,?,?,?,?)}";
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCompetitorActivityType.competitor_activity_type_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCompetitorActivityType.description));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCompetitorActivityType.category));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCompetitorActivityType.seq));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCompetitorActivityType.created_by));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCompetitorActivityType.updated_by));
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
