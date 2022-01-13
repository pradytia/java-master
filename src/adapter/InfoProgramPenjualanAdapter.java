package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.DataTable.mdlDataTableParam;
import model.InfoProgramPenjualan.mdlInfoProgramPenjualan;
import model.Query.mdlQueryExecute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class InfoProgramPenjualanAdapter {
    final static Logger logger = LogManager.getLogger(InfoProgramPenjualanAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    //Upload
    public static boolean UploadInfoProgramPenjualan(mdlInfoProgramPenjualan param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";
        List<mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();

        try {
            sql = "{call sp_info_program_penjualan_upload(?,?,?,?,?,?,?,?)}";

            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.no_ip));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.jenis_program_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.start_date));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.end_date));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.kondisi_program));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.keterangan));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.created_by));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.updated_by));

            resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, user, db_name, port);

        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(listMdlQueryExecute), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean DeleteInfoJenisProgramPenjualan(mdlInfoProgramPenjualan param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

        try {

            sql = "{call sp_info_program_penjualan_delete(?)}";

            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.no_ip));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);


        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static List<mdlInfoProgramPenjualan> GetInfoProgramPenjualanWeb(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();

        List<mdlInfoProgramPenjualan> _mdlInfoProgramPenjualanList = new ArrayList<mdlInfoProgramPenjualan>();
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        CachedRowSet jrs = null;

        try {

            sql = "{call sp_info_program_penjualan_get_with_paging_v2(?,?,?,?,?)}";

            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.name));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.order.dir));

            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "infoprogrampenjualan", db_name, port);

            while (jrs.next()) {

                mdlInfoProgramPenjualan _mdlInfoProgramPenjualan = new mdlInfoProgramPenjualan();

                _mdlInfoProgramPenjualan.no_ip = jrs.getString("no_ip");
                _mdlInfoProgramPenjualan.jenis_program_id = jrs.getString("jenis_program_id");
                _mdlInfoProgramPenjualan.start_date = jrs.getString("start_date");
                _mdlInfoProgramPenjualan.end_date = jrs.getString("end_date");
                _mdlInfoProgramPenjualan.kondisi_program = jrs.getString("kondisi_program");
                _mdlInfoProgramPenjualan.keterangan = jrs.getString("keterangan");
                _mdlInfoProgramPenjualan.is_active = jrs.getString("is_active");
                _mdlInfoProgramPenjualan.created_by = jrs.getString("created_by");
                _mdlInfoProgramPenjualan.created_date = jrs.getString("created_date");
                _mdlInfoProgramPenjualan.updated_by = jrs.getString("updated_by");
                _mdlInfoProgramPenjualan.updated_date = jrs.getString("updated_date");

                _mdlInfoProgramPenjualanList.add(_mdlInfoProgramPenjualan);

            }

        } catch (Exception ex) {
            _mdlInfoProgramPenjualanList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "", db_name), ex);
        }
        return _mdlInfoProgramPenjualanList;
    }


    public static int GetInfoProgramPenjualanList(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        int returnValue = 0;
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

        try {

            sql = "{call sp_info_program_penjualan_get_total_list(?)}";

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
}
