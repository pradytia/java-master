package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.DataTable.mdlDataTableParam;
import model.Query.mdlQueryExecute;
import model.Answer.mdlAnswerType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class AnswerTypeAdapter {
    final static Logger logger = LogManager.getLogger(AnswerTypeAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlAnswerType> GetAnswerTypeWeb(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlAnswerType> _mdlAnswerTypeList = new ArrayList<mdlAnswerType>();
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        CachedRowSet jrs = null;
        try {
            sql = "{call sp_answer_type_get_with_paging(?,?,?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.page_size));

            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "AnswerTypeList", db_name, port);
            while (jrs.next()) {
                model.Answer.mdlAnswerType _mdlAnswerType = new model.Answer.mdlAnswerType();
                _mdlAnswerType.answer_type_id = jrs.getString("answer_type_id");
                _mdlAnswerType.answer_type_text = jrs.getString("answer_type_text");
                _mdlAnswerType.is_multiple = jrs.getString("is_multiple");
                _mdlAnswerType.created_by = jrs.getString("created_by");
                _mdlAnswerType.created_date = jrs.getString("created_date");
                _mdlAnswerType.updated_by = jrs.getString("updated_by");
                _mdlAnswerType.updated_date = jrs.getString("updated_date");
                _mdlAnswerType.is_active = jrs.getString("is_active");
                _mdlAnswerTypeList.add(_mdlAnswerType);
            }

        } catch (Exception ex) {
            _mdlAnswerTypeList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "", db_name), ex);
        }
        return _mdlAnswerTypeList;
    }

    public static int GetAnswerTypeList(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        int returnValue = 0;
        String sql = "";
        CachedRowSet crs = null;
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_answer_type_get_total_list(?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Answer Type Total", db_name, port);
            while (crs.next()) {
                returnValue = crs.getInt("total");
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "", db_name), ex);
        }
        return returnValue;
    }

    public static List<mdlAnswerType> GetAnswerTypeFromQuestion(String param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlAnswerType> _mdlAnswerTypeList = new ArrayList<mdlAnswerType>();
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        CachedRowSet jrs = null;
        try {
            sql = "{call sp_get_answer_type_by_id(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param));

            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "AnswerTypeList", db_name, port);
            while (jrs.next()) {
                model.Answer.mdlAnswerType _mdlAnswerType = new model.Answer.mdlAnswerType();
                _mdlAnswerType.answer_type_id = jrs.getString("answer_type_id");
                _mdlAnswerType.answer_type_text = jrs.getString("answer_type_text");
                _mdlAnswerType.is_multiple = jrs.getString("is_multiple");
                _mdlAnswerTypeList.add(_mdlAnswerType);
            }
        } catch (Exception ex) {
            _mdlAnswerTypeList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "", db_name), ex);
        }
        return _mdlAnswerTypeList;
    }

    public static List<mdlAnswerType> GetDropDownAnswerType(String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlAnswerType> mdlAnswerTypeList = new ArrayList<mdlAnswerType>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_answer_type_dropdown_get}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "answer", db_name, port);
            while (rowset.next()) {
                mdlAnswerType _mdlAnswerType = new mdlAnswerType();
                _mdlAnswerType.answer_type_id = rowset.getString("answer_type_id");
                _mdlAnswerType.answer_type_text = rowset.getString("answer_type_text");
                mdlAnswerTypeList.add(_mdlAnswerType);
            }
        } catch (Exception ex) {
            mdlAnswerTypeList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, "", "", ex.toString(), "", db_name), ex);
        }
        return mdlAnswerTypeList;
    }
}