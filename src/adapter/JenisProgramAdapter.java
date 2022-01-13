package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.DataTable.mdlDataTableParam;
import model.JenisProgram.mdlJenisprogram;
import model.Query.mdlQueryExecute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class JenisProgramAdapter {
    final static Logger logger = LogManager.getLogger(JenisProgramAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    //Upload
    public static boolean UploadJenisProgram(mdlJenisprogram param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";
        List<mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();

        try {

            sql = "{call sp_jenis_program_upload(?,?,?)}";

            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.jenis_program_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.jenis_program_name));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.created_by));

            resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(listMdlQueryExecute), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    //Delete
    public static boolean DeleteJenisProgram(mdlJenisprogram param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

        try {

            sql = "{call sp_jenis_program_delete(?)}";

            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.jenis_program_id));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "", db_name), ex);
        }
        return resultExec;
    }

    public static List<mdlJenisprogram> GetJenisProgramWeb(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();

        List<mdlJenisprogram> _mdlJenisProgramList = new ArrayList<mdlJenisprogram>();
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        CachedRowSet jrs = null;

        try {
            sql = "{call sp_jenis_program_get_with_paging_v2(?,?,?,?,?)}";

            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.name));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.dir));

            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Sbb", db_name, port);
            while (jrs.next()) {
                mdlJenisprogram _mdljenisprogram = new mdlJenisprogram();

                _mdljenisprogram.jenis_program_id = jrs.getString("jenis_program_id");
                _mdljenisprogram.jenis_program_name = jrs.getString("jenis_program_name");
                _mdljenisprogram.is_active = jrs.getString("is_active");
                _mdljenisprogram.created_by = jrs.getString("created_by");
                _mdljenisprogram.created_date = jrs.getString("created_date");
                _mdljenisprogram.updated_by = jrs.getString("updated_by");
                _mdljenisprogram.updated_date = jrs.getString("updated_date");

                _mdlJenisProgramList.add(_mdljenisprogram);
            }

        } catch (Exception ex) {
            _mdlJenisProgramList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "", db_name), ex);
        }
        return _mdlJenisProgramList;
    }


    public static int GetJenisProgramList(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        int returnValue = 0;
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

        try {
            sql = "{call sp_jenis_program_get_total_list(?)}";

            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));

            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "sbb", db_name, port);

            while (crs.next()) {
                returnValue = crs.getInt("total");
            }

        } catch (Exception ex) {
            returnValue = 0;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "", db_name), ex);
        }

        return returnValue;
    }

    public static List<mdlJenisprogram> GetDropDownJenisProgram(String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();

        List<mdlJenisprogram> mdlJenisprogramList = new ArrayList<mdlJenisprogram>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;

        try {
            sql = "{call sp_jenis_program_dropdown}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "JenisProgram", db_name, port);

            while (rowset.next()) {
                mdlJenisprogram _mdlJenisprogram = new mdlJenisprogram();
                _mdlJenisprogram.jenis_program_id = rowset.getString("jenis_program_id");
                _mdlJenisprogram.jenis_program_name = rowset.getString("jenis_program_name");
                mdlJenisprogramList.add(_mdlJenisprogram);
            }
        } catch (Exception ex) {
            mdlJenisprogramList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(listParam), "", ex.toString(), "", db_name), ex);
        }
        return mdlJenisprogramList;
    }

}
