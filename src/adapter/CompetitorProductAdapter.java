package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.Competitor.mdlCompetitorProduct;
import model.Query.mdlQueryExecute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class CompetitorProductAdapter {
    final static Logger logger = LogManager.getLogger(CompetitorActivityTypeAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static boolean UploadCompetitorProductWeb(List<mdlCompetitorProduct> param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String user = param.get(0).created_by;
        boolean resultExec = false;
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            for (mdlCompetitorProduct _mdlCompetitorProduct : param) {
                sql = "{call sp_competitor_product_upload(?,?,?,?,?)}";
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCompetitorProduct.competitor_product_id));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCompetitorProduct.competitor_id));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCompetitorProduct.competitor_product_name));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCompetitorProduct.created_by));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCompetitorProduct.updated_by));
                resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }
}
