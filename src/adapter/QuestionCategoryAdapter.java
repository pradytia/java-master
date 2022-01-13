package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.DataTable.mdlDataTableParam;
import model.Query.mdlQueryExecute;
import model.Question.mdlQuestionCategory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class QuestionCategoryAdapter {
    final static Logger logger = LogManager.getLogger(QuestionCategoryAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlQuestionCategory> GetQuestionCategoryWeb(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param.module_id;

        List<mdlQuestionCategory> _mdlQuestionCategoryList = new ArrayList<mdlQuestionCategory>();
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        CachedRowSet jrs = null;

        try {
            sql = "{call sp_question_category_get_pagging(?,?,?)}";

            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";

            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.page_size));
            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Question Category", db_name, port);

            while (jrs.next()) {
                model.Question.mdlQuestionCategory _mdlQuestionCategory = new model.Question.mdlQuestionCategory();
                _mdlQuestionCategory.question_category_id = jrs.getString("question_category_id");
                _mdlQuestionCategory.question_category_text = jrs.getString("question_category_text");
                _mdlQuestionCategory.created_by = jrs.getString("created_by");
                _mdlQuestionCategory.updated_by = jrs.getString("updated_by");
                _mdlQuestionCategory.created_date = jrs.getString("created_date");
                _mdlQuestionCategory.updated_date = jrs.getString("updated_date");
                _mdlQuestionCategory.is_active = jrs.getString("is_active");
                _mdlQuestionCategoryList.add(_mdlQuestionCategory);
            }

        } catch (Exception ex) {
            _mdlQuestionCategoryList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlQuestionCategoryList;
    }

    public static int GetQuestionCategoryTotal(mdlDataTableParam param, String db_name, int port) {

        long startTime = System.currentTimeMillis();
        int returnValue = 0;
        String sql = "";
        String user = param.module_id;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;

        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

        try {
            sql = "{call sp_question_category_get_total_list(?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Question Total", db_name, port);
            while (crs.next()) {
                returnValue = crs.getInt("total");
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return returnValue;
    }

    public static boolean UploadQuestionCategoryWeb(List<mdlQuestionCategory> param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String user = param.get(0).created_by;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

        try {
            for (mdlQuestionCategory mdlQuestionCategory : param) {
                sql = "{call sp_question_category_upload(?,?,?,?)}";

                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", mdlQuestionCategory.question_category_id));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", mdlQuestionCategory.question_category_text));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", mdlQuestionCategory.created_by));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", mdlQuestionCategory.updated_by));

                resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }

        return resultExec;
    }

    public static List<mdlQuestionCategory> GetDropDownQuestionCategory(String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = "Question Category";

        List<mdlQuestionCategory> mdlQuestionCategoryList = new ArrayList<mdlQuestionCategory>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;

        try {
            sql = "{call sp_dropdown_question_category_get}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "question_category", db_name, port);

            while (rowset.next()) {
                mdlQuestionCategory _mdlQuestionCategory = new mdlQuestionCategory();
                _mdlQuestionCategory.question_category_id = rowset.getString("question_category_id");
                _mdlQuestionCategory.question_category_text = rowset.getString("question_category_text");
                mdlQuestionCategoryList.add(_mdlQuestionCategory);
            }
        } catch (Exception ex) {
            mdlQuestionCategoryList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), user, db_name), ex);
        }
        return mdlQuestionCategoryList;
    }

}
