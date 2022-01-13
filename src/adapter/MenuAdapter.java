package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.DataTable.mdlDataTableParam;
import model.Menu.mdlAccessRole;
import model.Menu.mdlMenu;
import model.Query.mdlQueryExecute;
import model.Role.mdlRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class MenuAdapter {
    final static Logger logger = LogManager.getLogger(MenuAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlMenu> GetMenu(mdlRole param1, mdlDataTableParam param2, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param1.role_id;


        List<model.Menu.mdlMenu> mdlMenuList = new ArrayList<mdlMenu>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;

        try {
            sql = "{call sp_menu_get(?,?,?,?)}";
            listParam.add(QueryAdapter.QueryParam("string", param1.role_id));
            listParam.add(QueryAdapter.QueryParam("int", (param2.page_number - 1) * param2.page_size));
            listParam.add(QueryAdapter.QueryParam("int", param2.page_size));
            listParam.add(QueryAdapter.QueryParam("string", param2.search));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, user, db_name, port);

            while (rowset.next()) {
                model.Menu.mdlMenu mdlMenu = new model.Menu.mdlMenu();

                String type = rowset.getString("type");
                switch (type.toUpperCase()) {
                    case "DASHBOARD":
                        mdlMenu.type = "Dashboard";
                        break;
                    case "CALLPLAN":
                        mdlMenu.type = "Rencana Kunjungan";
                        break;
                    case "REPORT":
                        mdlMenu.type = "Laporan";
                        break;
                    case "TRACKING":
                        mdlMenu.type = "Pelacakan";
                        break;
                    case "DATA":
                        mdlMenu.type = "Data";
                        break;
                    case "SETTING":
                        mdlMenu.type = "Pengaturan";
                        break;
                    case "MASTER":
                        mdlMenu.type = "Master";
                        break;
                    case "CUSTOM-MENU":
                        mdlMenu.type = "Menu Kustom";
                        break;
                    default:
                }
                mdlMenu.menu_id = rowset.getString("menu_id");
                mdlMenu.menu_name = rowset.getString("menu_name");
                mdlMenu.is_view = rowset.getBoolean("is_view");
                mdlMenu.is_insert = rowset.getBoolean("is_insert");
                mdlMenu.is_update = rowset.getBoolean("is_update");
                mdlMenu.is_delete = rowset.getBoolean("is_delete");
                mdlMenu.is_print = rowset.getBoolean("is_print");

                mdlMenuList.add(mdlMenu);

            }

        } catch (Exception ex) {
            mdlMenuList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), user, db_name), ex);
        }
        return mdlMenuList;
    }

    public static int GetTotalMenu(mdlRole param1, mdlDataTableParam param2, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        int return_value = 0;
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param1.role_id;

        List<mdlRole> mdlRoleList = new ArrayList<mdlRole>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;

        try {
            sql = "{call sp_menu_total_get (?,?)}";
            listParam.add(QueryAdapter.QueryParam("string", param1.role_id));
            listParam.add(QueryAdapter.QueryParam("string", param2.search));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "role", db_name, port);

            while (rowset.next()) {
                return_value = rowset.getInt("total");
            }
        } catch (Exception ex) {
            return_value = 0;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), user, db_name), ex);
        }
        return return_value;
    }


    public static List<mdlMenu> updateAccessRole(mdlAccessRole mdlAccessRole, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        boolean resultExec = false;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = mdlAccessRole.role_id;

        List<mdlMenu> mdlMenuList = new ArrayList<mdlMenu>();
        List<model.Query.mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();

        try {
            sql = "{call sp_role_access_update (?,?,?,?,?,?,?,?)}";
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", mdlAccessRole.role_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", mdlAccessRole.menu_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("boolean", mdlAccessRole.is_view));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("boolean", mdlAccessRole.is_insert));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("boolean", mdlAccessRole.is_update));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("boolean", mdlAccessRole.is_delete));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("boolean", mdlAccessRole.is_print));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", mdlAccessRole.created_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, "user", db_name, port);

            if (resultExec) {
//                result.statusCode = "00";
//                result.statusMessage = "Upload Result Survey Success";
//                mdlRole role = new mdlRole();
//                role.setRoleID(mdlAccessRole.roleID);
//                mdlMenuList = getMenu(role);
            } else {
//                result.statusCode = "01";
//                result.statusMessage = "Upload Failed";
            }

        } catch (Exception ex) {
            mdlMenuList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listMdlQueryExecute), "", ex.toString(), user, db_name), ex);
        }
        return mdlMenuList;
    }
}
