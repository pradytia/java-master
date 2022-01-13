package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.DataTable.mdlDataTableParam;
import model.Message.mdlDailyMessage;
import model.Download.mdlDownloadParam;
import model.Message.mdlDailyMessageDetail;
import model.Query.mdlQueryExecute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class DailyMessageAdapter {
    final static Logger logger = LogManager.getLogger(DashboardAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<model.Message.mdlDailyMessage> GetDailyMessage(mdlDownloadParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String user = param.employee_id;
        List<model.Message.mdlDailyMessage> _mdlDailyMessageList = new ArrayList<mdlDailyMessage>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_daily_message_list()}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            while (rowset.next()) {
                model.Message.mdlDailyMessage _mdlDailyMessage = new model.Message.mdlDailyMessage();
                _mdlDailyMessage.setMessage_id(rowset.getString("message_id"));
                _mdlDailyMessage.setBranch_id(rowset.getString("branch_id"));
                _mdlDailyMessage.setMessage_name(rowset.getString("message_name"));
                _mdlDailyMessage.setMessage_desc(rowset.getString("message_desc"));
                _mdlDailyMessage.setMessage_img(rowset.getString("message_img"));
                _mdlDailyMessage.setImg_path(rowset.getString("img_path"));
                _mdlDailyMessage.setStart_date(rowset.getString("start_date"));
                _mdlDailyMessage.setEnd_date(rowset.getString("end_date"));
                _mdlDailyMessage.setMessage_type(rowset.getString("message_type"));
                _mdlDailyMessage.setCreated_by(rowset.getString("created_by"));
                _mdlDailyMessage.setCreated_date(rowset.getString("created_date"));
                _mdlDailyMessage.setUpdated_by(rowset.getString("updated_by"));
                _mdlDailyMessage.setUpdated_date(rowset.getString("updated_date"));
                _mdlDailyMessageList.add(_mdlDailyMessage);
            }
        } catch (Exception ex) {
            _mdlDailyMessageList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(param), "", ex.toString(), user, db_name), ex);
        }
        return _mdlDailyMessageList;
    }

    public static List<mdlDailyMessage> GetDailyMessageWeb(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<model.Message.mdlDailyMessage> _mdlDailyMessageList = new ArrayList<>();
        CachedRowSet jrs = null;
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<>();
        try {
            sql = "{call sp_daily_message_get_with_paging(?,?,?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.page_size));
            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Daily Message List", db_name, port);
            while (jrs.next()) {
                model.Message.mdlDailyMessage _mdlDailyMessage = new model.Message.mdlDailyMessage();
                _mdlDailyMessage.message_id = jrs.getString("message_id");
                _mdlDailyMessage.branch_id = jrs.getString("branch_id");
                _mdlDailyMessage.message_name = jrs.getString("message_name");
                _mdlDailyMessage.message_desc = jrs.getString("message_desc");
                _mdlDailyMessage.message_img = jrs.getString("message_img");
                _mdlDailyMessage.img_path = jrs.getString("img_path");
                _mdlDailyMessage.start_date = jrs.getString("start_date");
                _mdlDailyMessage.end_date = jrs.getString("end_date");
                _mdlDailyMessage.message_type = jrs.getString("message_type");
                _mdlDailyMessage.created_by = jrs.getString("created_by");
                _mdlDailyMessage.created_date = jrs.getString("created_date");
                _mdlDailyMessage.updated_by = jrs.getString("updated_by");
                _mdlDailyMessage.updated_date = jrs.getString("updated_date");
                _mdlDailyMessage.is_active = jrs.getString("is_active");
                _mdlDailyMessageList.add(_mdlDailyMessage);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "Daily Message List", db_name), ex);
        }
        return _mdlDailyMessageList;
    }

    public static int GetDailyMessageTotalList(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        CachedRowSet crs = null;
        int returnValue = 0;
        String search_part = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        try {
            sql = "{call sp_daily_message_get_total_list(?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Daily Message Total", db_name, port);
            while (crs.next()) {
                returnValue = crs.getInt("total");
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(param), "", ex.toString(), "Daily Message Total", db_name), ex);
        }
        return returnValue;
    }

    // Upload Daily Message
    public static boolean UploadDailyMessage(List<model.Message.mdlDailyMessage> param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.get(0).created_by;
        String sql = "";
        try {
            for (model.Message.mdlDailyMessage _mdlDailyMessage : param) {
                List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
                try {
                    sql = "{call sp_daily_message_upload(?,?,?,?,?,?,?,?,?,?,?)}";
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlDailyMessage.message_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlDailyMessage.branch_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlDailyMessage.message_name));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlDailyMessage.message_desc));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlDailyMessage.message_img));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlDailyMessage.img_path));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlDailyMessage.start_date));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlDailyMessage.end_date));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlDailyMessage.message_type));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlDailyMessage.created_by));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlDailyMessage.updated_by));
                    resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
                } catch (Exception ex) {
                    logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
                }

                //setelah upload header success ==> upload Daily Message detail
                for (mdlDailyMessageDetail _mdlDailyMessageDetail : _mdlDailyMessage.mdlDailyMessageDetailsParam) {
                    String employee_list = "";
                    for (String _employee : _mdlDailyMessageDetail.employee_id) {
                        if (employee_list.equals(""))
                            employee_list = _employee;
                        else
                            employee_list += _employee;
                    }

                    String branch_list = "";
                    for (String _branch : _mdlDailyMessageDetail.branch_id) {
                        if (branch_list.equals(""))
                            branch_list = _branch;
                        else
                            branch_list += _branch;
                    }

                    List<mdlQueryExecute> _mdlQueryExecute = new ArrayList<mdlQueryExecute>();
                    try {
                        sql = "{call sp_daily_message_detail_upload(?,?,?)}";
                        _mdlQueryExecute.add(QueryAdapter.QueryParam("string", _mdlDailyMessageDetail.message_id));
                        _mdlQueryExecute.add(QueryAdapter.QueryParam("string", _mdlDailyMessageDetail.employee_id));
                        _mdlQueryExecute.add(QueryAdapter.QueryParam("string", _mdlDailyMessageDetail.branch_id));
                        resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecute, functionName, user, db_name, port);
                    } catch (Exception ex) {
                        logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecute), "", ex.toString(), user, db_name), ex);
                    }
                }
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }
}
