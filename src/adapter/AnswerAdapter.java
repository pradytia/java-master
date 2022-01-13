package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.Answer.mdlAnswer;
import model.Answer.mdlAnswerType;
import model.DataTable.mdlDataTableParam;
import model.Download.mdlDownloadParam;
import model.Query.mdlQueryExecute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class AnswerAdapter {
    final static Logger logger = LogManager.getLogger(AnswerAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<model.Answer.mdlAnswer> GetAnswer(mdlDownloadParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String user = param.employee_id;
        List<model.Answer.mdlAnswer> _mdlAnswerList = new ArrayList<mdlAnswer>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_answer_list()}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

            while (rowset.next()) {
                model.Answer.mdlAnswer _mdlGetAnswer = new model.Answer.mdlAnswer();
                _mdlGetAnswer.setAnswer_id(rowset.getString("answer_id"));
                _mdlGetAnswer.setAnswer_text(rowset.getString("answer_text"));
                _mdlGetAnswer.setQuestion_id(rowset.getString("question_id"));
                _mdlGetAnswer.setSub_question(rowset.getString("sub_question"));
                _mdlGetAnswer.setIs_sub_question(rowset.getString("is_sub_question"));
                _mdlGetAnswer.setSequence(rowset.getString("sequence"));
                _mdlGetAnswer.setNo(rowset.getString("no"));
                _mdlGetAnswer.setCreated_by(rowset.getString("created_by"));
                _mdlGetAnswer.setUpdated_by(rowset.getString("updated_by"));
                _mdlGetAnswer.setCreated_date(rowset.getString("created_date"));
                _mdlGetAnswer.setUpdated_date(rowset.getString("updated_date"));
                _mdlAnswerList.add(_mdlGetAnswer);
            }
        } catch (Exception ex) {
            _mdlAnswerList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(param), "", ex.toString(), user, db_name), ex);
        }
        return _mdlAnswerList;
    }

    public static List<model.Answer.mdlAnswerType> GetAnswerType(mdlDownloadParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String user = param.employee_id;
        List<model.Answer.mdlAnswerType> _mdlAnswerTypeList = new ArrayList<mdlAnswerType>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_answer_type_list()}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

            while (rowset.next()) {
                model.Answer.mdlAnswerType _mdlGetAnswerType = new model.Answer.mdlAnswerType();
                _mdlGetAnswerType.setAnswer_type_id(rowset.getString("answer_type_id"));
                _mdlGetAnswerType.setAnswer_type_text(rowset.getString("answer_type_text"));
                _mdlGetAnswerType.setIs_multiple(rowset.getString("is_multiple"));
                _mdlGetAnswerType.setCreated_by(rowset.getString("created_by"));
                _mdlGetAnswerType.setCreated_date(rowset.getString("created_date"));
                _mdlGetAnswerType.setUpdated_by(rowset.getString("updated_by"));
                _mdlGetAnswerType.setUpdated_date(rowset.getString("updated_date"));
                _mdlAnswerTypeList.add(_mdlGetAnswerType);
            }
        } catch (Exception ex) {
            _mdlAnswerTypeList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(param), "", ex.toString(), user, db_name), ex);
        }
        return _mdlAnswerTypeList;
    }

    //====================================WEB========================================================\\

    public static List<mdlAnswer> GetAnswerWeb(mdlAnswer param1, mdlDataTableParam param2, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlAnswer> _mdlAnswerList = new ArrayList<mdlAnswer>();
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        CachedRowSet jrs = null;
        try {
            sql = "{call sp_answer_get_with_paging(?,?,?,?)}";
            String searchString = "%" + param2.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param1.question_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param2.page_number - 1) * param2.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param2.page_size));

            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Answer", db_name, port);
            while (jrs.next()) {
                model.Answer.mdlAnswer _mdlAnswer = new model.Answer.mdlAnswer();
                _mdlAnswer.setAnswer_id(jrs.getString("answer_id"));
                _mdlAnswer.setAnswer_text(jrs.getString("answer_text"));
                _mdlAnswer.setQuestion_id(jrs.getString("question_id"));
                _mdlAnswer.setSub_question(jrs.getString("sub_question"));
                _mdlAnswer.setIs_sub_question(jrs.getString("is_sub_question"));
                _mdlAnswer.setSequence(jrs.getString("sequence"));
                _mdlAnswer.setNo(jrs.getString("no"));
                _mdlAnswer.setIs_active(jrs.getString("is_active"));
                _mdlAnswer.setCreated_by(jrs.getString("created_by"));
                _mdlAnswer.setUpdated_by(jrs.getString("updated_by"));
                _mdlAnswer.setCreated_date(jrs.getString("created_date"));
                _mdlAnswer.setUpdated_date(jrs.getString("updated_date"));
                _mdlAnswerList.add(_mdlAnswer);
            }
        } catch (Exception ex) {
            _mdlAnswerList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "Answer Paging", db_name), ex);
        }
        return _mdlAnswerList;
    }

    public static int GetAnswerList(mdlAnswer param1, mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        int returnValue = 0;
        String sql = "";
        CachedRowSet crs = null;
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_answer_get_total_list(?,?)}";

            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param1.question_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));

            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Answer List", db_name, port);
            while (crs.next()) {
                returnValue = crs.getInt("total");
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "AnswerList", db_name), ex);
        }
        return returnValue;
    }

    public static boolean updateStatusAnswerWeb(mdlAnswer param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String user = param.created_by;
        boolean resultExec = false;
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_answer_update_status(?,?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.answer_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.is_active));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));

            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }

        return resultExec;
    }

    public static boolean UploadAnswerWeb(mdlAnswer param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String user = param.created_by;
        boolean resultExec = false;
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_answer_upload(?,?,?,?,?,?,?,?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.answer_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.answer_text));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.question_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.sub_question));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.is_sub_question));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.sequence));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.no));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.updated_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean DeleteAnswer(mdlAnswer param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = "answer";
        String sql = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_answer_delete(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.answer_id));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static List<mdlAnswer> GetDropDownAnswer(String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlAnswer> mdlAnswerList = new ArrayList<mdlAnswer>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_answer_dropdown_get}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "answer", db_name, port);

            while (rowset.next()) {
                mdlAnswer _mdlAnswer = new mdlAnswer();
                _mdlAnswer.answer_id = rowset.getString("answer_id");
                _mdlAnswer.answer_text = rowset.getString("answer_text");
                mdlAnswerList.add(_mdlAnswer);
            }
        } catch (Exception ex) {
            mdlAnswerList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), "answer", db_name), ex);
        }
        return mdlAnswerList;
    }
}
