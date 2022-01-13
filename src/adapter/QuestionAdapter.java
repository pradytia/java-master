package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.DataTable.mdlDataTableParam;
import model.Question.*;
import model.Query.mdlQueryExecute;
import model.Download.mdlDownloadParam;
import model.Result.mdlResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class QuestionAdapter {
    final static Logger logger = LogManager.getLogger(QuestionAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<model.Question.mdlQuestion> GetQuestion(mdlDownloadParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param.employee_id;

        List<model.Question.mdlQuestion> _mdlQuestionList = new ArrayList<mdlQuestion>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;

        try {
            sql = "{call sp_question_list()}";
//            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.employee_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

            while (rowset.next()) {
                model.Question.mdlQuestion _mdlQuestion = new model.Question.mdlQuestion();
                _mdlQuestion.setQuestion_id(rowset.getString("question_id"));
                _mdlQuestion.setQuestion_text(rowset.getString("question_text"));
                _mdlQuestion.setAnswer_type_id(rowset.getString("answer_type_id"));
                _mdlQuestion.setIs_sub_question(rowset.getString("is_sub_question"));
                _mdlQuestion.setSequence(rowset.getInt("sequence"));
                _mdlQuestion.setQuestion_set_id(rowset.getString("question_set_id"));
                _mdlQuestion.setQuestion_category_id(rowset.getString("question_category_id"));
                _mdlQuestion.setAnswer_id(rowset.getString("answer_id"));
                _mdlQuestion.setNo(rowset.getString("no"));
                _mdlQuestion.setMandatory(rowset.getString("mandatory"));
                _mdlQuestion.setIs_active(rowset.getString("is_active"));
                _mdlQuestion.setCreated_by(rowset.getString("created_by"));
                _mdlQuestion.setUpdated_by(rowset.getString("updated_by"));
                _mdlQuestion.setCreated_date(rowset.getString("created_date"));
                _mdlQuestion.setUpdated_date(rowset.getString("updated_date"));
                _mdlQuestion.setIs_efective_call(rowset.getString("is_efective_call"));
                _mdlQuestionList.add(_mdlQuestion);
            }
        } catch (Exception ex) {
            _mdlQuestionList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlQuestionList;
    }

    public static List<model.Question.mdlQuestionCategory> GetQuestionCategory(mdlDownloadParam param, String db_name, int port) {

        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param.employee_id;

        List<model.Question.mdlQuestionCategory> _mdlQuestionCategoryList = new ArrayList<mdlQuestionCategory>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;

        try {
            sql = "{call sp_question_category_list()}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            while (rowset.next()) {
                mdlQuestionCategory _mdlQuestionCategory = new model.Question.mdlQuestionCategory();
                _mdlQuestionCategory.setQuestion_category_id(rowset.getString("question_category_id"));
                _mdlQuestionCategory.setQuestion_category_text(rowset.getString("question_category_text"));
                _mdlQuestionCategory.setIs_active(rowset.getString("is_active"));
                _mdlQuestionCategory.setCreated_by(rowset.getString("created_by"));
                _mdlQuestionCategory.setLast_update_by(rowset.getString("updated_by"));
                _mdlQuestionCategory.setCreated_date(rowset.getString("created_date"));
                _mdlQuestionCategory.setLast_date(rowset.getString("updated_date"));
                _mdlQuestionCategoryList.add(_mdlQuestionCategory);
            }
        } catch (Exception ex) {
            _mdlQuestionCategoryList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }

        return _mdlQuestionCategoryList;
    }

    public static List<model.Question.mdlQuestionSet> GetQuestionSet(mdlDownloadParam param, String db_name, int port) {

        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param.employee_id;

        List<model.Question.mdlQuestionSet> mdlQuestionSetList = new ArrayList<mdlQuestionSet>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;

        try {
            sql = "{call sp_question_set_list}";

            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

            while (rowset.next()) {
                model.Question.mdlQuestionSet _mdlQuestionSet = new model.Question.mdlQuestionSet();
                _mdlQuestionSet.setQuestion_set_id(rowset.getString("question_set_id"));
                _mdlQuestionSet.setQuestion_set_text(rowset.getString("question_set_text"));
                _mdlQuestionSet.setCreated_by(rowset.getString("created_by"));
                _mdlQuestionSet.setLast_updated_by(rowset.getString("updated_by"));
                _mdlQuestionSet.setCreated_date(rowset.getString("created_date"));
                _mdlQuestionSet.setLast_date(rowset.getString("updated_date"));
                mdlQuestionSetList.add(_mdlQuestionSet);
            }
        } catch (Exception ex) {
            mdlQuestionSetList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return mdlQuestionSetList;
    }


    //============================================= WEB ========================================================\\


    public static List<mdlQuestionJoin> GetQuestionWeb(mdlQuestion param1, mdlDataTableParam param2, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String user = param2.module_id;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<mdlQuestionJoin> _mdlQuestionList = new ArrayList<mdlQuestionJoin>();
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        CachedRowSet jrs = null;

        try {
            sql = "{call sp_question_get_join(?,?,?,?)}";

            String search_string = "%" + param2.search.trim().replace(" ", "%") + "%";

            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param1.question_set_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", search_string));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param2.page_number - 1) * param2.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param2.page_size));

            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Question", db_name, port);

            while (jrs.next()) {
                mdlQuestionJoin _mdlQuestionJoin = new model.Question.mdlQuestionJoin();
                _mdlQuestionJoin.sequence = jrs.getString("sequence");
                _mdlQuestionJoin.no = jrs.getString("no");
                _mdlQuestionJoin.question_id = jrs.getString("question_id");
                _mdlQuestionJoin.question_text = jrs.getString("question_text");
                _mdlQuestionJoin.question_category_id = jrs.getString("question_category_id");
                _mdlQuestionJoin.question_category_text = jrs.getString("question_category_text");
                _mdlQuestionJoin.answer_type_id = jrs.getString("answer_type_id");
                _mdlQuestionJoin.answer_type_text = jrs.getString("answer_type_text");
                _mdlQuestionJoin.mandatory = jrs.getString("mandatory");
                _mdlQuestionJoin.is_sub_question = jrs.getString("is_sub_question");
                _mdlQuestionJoin.question_set_id = jrs.getString("question_set_id");
                _mdlQuestionJoin.is_efective_call = jrs.getString("is_efective_call");
                _mdlQuestionJoin.is_active = jrs.getString("is_active");
                _mdlQuestionList.add(_mdlQuestionJoin);
            }

        } catch (Exception ex) {
            _mdlQuestionList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlQuestionList;
    }

    public static int GetQuestionTotal(mdlQuestion param1, mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        int return_value = 0;
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;
        String user = param.module_id;

        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {

            sql = "{call sp_question_get_total_list(?,?)}";

            String search_string = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param1.question_set_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", search_string));

            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Question Total", db_name, port);

            while (crs.next()) {
                return_value = crs.getInt("total");
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return return_value;
    }

    public static boolean updateStatusQuestionWeb(mdlQuestion param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String user = param.created_by;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean result_exec = false;

        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_question_update_status(?,?,?)}";

            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.question_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.is_active));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));

            result_exec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return result_exec;
    }

    public static boolean UploadQuestionWeb(mdlQuestion param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String subSql = "";
        String user = param.created_by;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean result_exec = false;

        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_question_upload(?,?,?,?,?,?,?,?,?,?,?,?)}";

            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.question_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.question_text));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.answer_type_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.is_sub_question));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.sequence));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.question_set_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.question_category_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.no));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.mandatory));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.updated_by));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.is_efective_call));

            result_exec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

            if (result_exec) {

                try {
                    // Delete question employee type
                    List<mdlQueryExecute> _mdlQueryExecuteDeleteList = new ArrayList<mdlQueryExecute>();
                    subSql = "{call sp_question_employee_type_delete(?)}";

                    _mdlQueryExecuteDeleteList.add(QueryAdapter.QueryParam("string", param.question_id));

                    result_exec = QueryAdapter.QueryManipulateWithDB(subSql, _mdlQueryExecuteDeleteList, functionName, user, db_name, port);

                    // Upload question
                    // employee type
                    try {

                        for (int i = 0; i < param.employee_type_id.size(); i++) {

                            List<mdlQueryExecute> _mdlQueryExecuteList1 = new ArrayList<mdlQueryExecute>();
                            subSql = "{call sp_question_employee_type_upload(?,?,?,?)}";

                            _mdlQueryExecuteList1.add(QueryAdapter.QueryParam("string", param.question_id));
                            _mdlQueryExecuteList1.add(QueryAdapter.QueryParam("string", param.employee_type_id.get(i)));
                            _mdlQueryExecuteList1.add(QueryAdapter.QueryParam("string", param.created_by));
                            _mdlQueryExecuteList1.add(QueryAdapter.QueryParam("string", param.updated_by));

                            result_exec = QueryAdapter.QueryManipulateWithDB(subSql, _mdlQueryExecuteList1, functionName, user, db_name, port);
                        }

                    } catch (Exception ex) {
                        logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
                    }
                } catch (Exception ex) {
                    logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
                }
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return result_exec;
    }

    public static boolean DeleteQuestion(mdlQuestion param, String db_name, int port) {

        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = "question";
        String sql = "";

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_question_delete(?)}";

            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.question_id));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }

        return resultExec;
    }

    public static boolean checkAnswerByQuestion(mdlQuestion param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = "question";
        String sql = "";
        Integer total = 0;
        CachedRowSet rowset = null;
        try {

            List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

            sql = "{call sp_check_validation_answer_by_qtn_id(?)}";

            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.question_id));
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
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, "", "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static List<mdlQuestion> GetDropDownQuestion(String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = "Question";
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();

        List<mdlQuestion> mdlQuestionList = new ArrayList<mdlQuestion>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;

        try {
            sql = "{call sp_question_dropdown_get}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "area", db_name, port);

            while (rowset.next()) {
                mdlQuestion _mdlQuestion = new mdlQuestion();
                _mdlQuestion.question_id = rowset.getString("question_id");
                _mdlQuestion.question_text = rowset.getString("question_text");
                mdlQuestionList.add(_mdlQuestion);
            }
        } catch (Exception ex) {
            mdlQuestionList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, "", "", ex.toString(), user, db_name), ex);
        }
        return mdlQuestionList;
    }
}
