package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.DataTable.mdlDataTableParam;
import model.Query.mdlQueryExecute;
import model.Question.mdlQuestion;
import model.Question.mdlQuestionSet;
import model.Result.mdlResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class QuestionSetAdapter {
    final static Logger logger = LogManager.getLogger(QuestionSetAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlQuestionSet> GetQuestionSetWeb(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param.module_id;

        List<mdlQuestionSet> _mdlQuestionSetList = new ArrayList<mdlQuestionSet>();
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        CachedRowSet jrs = null;

        try {
            sql = "{call sp_question_set_get_with_paging_v2(?,?,?,?,?)}";

            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.name));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.dir));
            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Question Set", db_name, port);

            while (jrs.next()) {
                model.Question.mdlQuestionSet _mdlQuestionSet = new model.Question.mdlQuestionSet();
                _mdlQuestionSet.question_set_id = jrs.getString("question_set_id");
                _mdlQuestionSet.question_set_text = jrs.getString("question_set_text");
                _mdlQuestionSet.created_by = jrs.getString("created_by");
                _mdlQuestionSet.updated_by = jrs.getString("updated_by");
                _mdlQuestionSet.created_date = jrs.getString("created_date");
                _mdlQuestionSet.updated_date = jrs.getString("updated_date");
                _mdlQuestionSet.is_active = jrs.getString("is_active");
                _mdlQuestionSetList.add(_mdlQuestionSet);
            }
        } catch (Exception ex) {
            _mdlQuestionSetList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlQuestionSetList;
    }

    public static int GetQuestionSetTotal(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        int returnValue = 0;
        String sql = "";
        String user = param.module_id;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

        try {
            sql = "{call sp_question_set_get_total_list(?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Question total", db_name, port);
            while (crs.next()) {
                returnValue = crs.getInt("total");
            }

        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return returnValue;
    }

    public static boolean UploadQuestionSet(mdlQuestionSet param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String user = param.created_by;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;

        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

        try {
            sql = "{call sp_question_set_upload(?,?,?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.question_set_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.question_set_text));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.updated_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }

        return resultExec;
    }

    public static boolean updateStatusQuestionSet(mdlQuestionSet param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String user = param.created_by;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

        try {
            sql = "{call sp_question_set_update_status(?,?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.question_set_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.is_active));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }

        return resultExec;
    }

    public static boolean DeleteQuestionSet(mdlQuestionSet param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = "delete question set";
        String sql = "";

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_question_set_delete(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.question_set_id));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean checkQuestionByQuestionSet(mdlQuestionSet param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = "question set";
        String sql = "";
        Integer total = 0;
        CachedRowSet rowset = null;
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

        try {
            sql = "{call sp_check_validation_question_by_qtn_set(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.question_set_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "area", db_name, port);
            while (rowset.next()) {
                total = rowset.getInt("total");
            }

            if (total <= 0) {
                resultExec = true;
            } else {
                resultExec = false;
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static List<mdlQuestionSet> GetDropDownQuestionSet(String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = "Question Set";
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();

        List<mdlQuestionSet> mdlQuestionSetList = new ArrayList<mdlQuestionSet>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_question_set_dropdown_get}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "area", db_name, port);

            while (rowset.next()) {
                mdlQuestionSet _mdlQuestionSet = new mdlQuestionSet();
                _mdlQuestionSet.question_set_id = rowset.getString("question_set_id");
                _mdlQuestionSet.question_set_text = rowset.getString("question_set_text");
                mdlQuestionSetList.add(_mdlQuestionSet);
            }
        } catch (Exception ex) {
            mdlQuestionSetList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, "", "", ex.toString(), user, db_name), ex);
        }
        return mdlQuestionSetList;
    }
}
