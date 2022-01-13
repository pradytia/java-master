package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.DataTable.mdlDataTableParam;
import model.Question.mdlQuestionEmployee;
import model.Query.mdlQueryExecute;
import model.Question.mdlQuestionEmployeeParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class QuestionEmployeeAdapter {

    final static Logger logger = LogManager.getLogger(QuestionEmployeeAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlQuestionEmployee> GetQuestionEmployeeWeb(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param.module_id;
        List<mdlQuestionEmployee> _mdlQuestionEmployeeList = new ArrayList<mdlQuestionEmployee>();
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        CachedRowSet jrs = null;

        try {
            sql = "{call sp_question_employee_type_get_with_paging(?,?,?)}";

            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.page_size));

            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Question", db_name, port);

            while (jrs.next()) {
                mdlQuestionEmployee _mdlQuestionEmployee = new mdlQuestionEmployee();
                _mdlQuestionEmployee.question_id = jrs.getString("question_id");
                _mdlQuestionEmployee.employee_type_id = jrs.getString("employee_type_id");
                _mdlQuestionEmployee.created_date = jrs.getString("created_date");
                _mdlQuestionEmployee.created_by = jrs.getString("created_by");
                _mdlQuestionEmployee.updated_date = jrs.getString("updated_date");
                _mdlQuestionEmployee.updated_by = jrs.getString("updated_by");
                _mdlQuestionEmployeeList.add(_mdlQuestionEmployee);
            }
        } catch (Exception ex) {
            _mdlQuestionEmployeeList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlQuestionEmployeeList;
    }

    public static int GetQuestionEmployeeTotal(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        int return_value = 0;
        String sql = "";
        String user = param.module_id;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;

        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_question_employee_get_total_list(?)}";

            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));

            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Question Total", db_name, port);
            while (crs.next()) {
                return_value = crs.getInt("total");
            }

        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return return_value;
    }

    public static boolean UploadQuestionEmployeeWeb(List<mdlQuestionEmployee> question_param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String user = question_param.get(0).created_by;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;

        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

        try {
            for (mdlQuestionEmployee _mdlQuestionEmployee : question_param) {
                    sql = "{call sp_question_employee_type_upload(?,?,?,?)}";
                    _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlQuestionEmployee.question_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlQuestionEmployee.employee_type_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlQuestionEmployee.created_by));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlQuestionEmployee.updated_by));
                    resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
                }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }

        return resultExec;
    }

    public static List<mdlQuestionEmployee> GetQuestionEmployeeByQuestionID(mdlQuestionEmployeeParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = param.question_id;
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<mdlQuestionEmployee> _mdlQuestionEmployeeList = new ArrayList<mdlQuestionEmployee>();
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        CachedRowSet jrs = null;

        try {
            sql = "{call sp_question_employee_type_by_qtn_id_get(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.question_id));
            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Question", db_name, port);
            while (jrs.next()) {
                mdlQuestionEmployee _mdlQuestionEmployee = new mdlQuestionEmployee();
                _mdlQuestionEmployee.employee_type_id = jrs.getString("employee_type_id");
                _mdlQuestionEmployeeList.add(_mdlQuestionEmployee);
            }
        } catch (Exception ex) {
            _mdlQuestionEmployeeList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlQuestionEmployeeList;
    }
}
