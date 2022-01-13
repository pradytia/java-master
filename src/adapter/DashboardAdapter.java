package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.Dashboard.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class DashboardAdapter {
    final static Logger logger = LogManager.getLogger(DashboardAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static mdlDashboardGlobal GetDashboard(mdlDashboardParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        CachedRowSet rowset = null;
        mdlDashboardGlobal _mdlDashboardGlobal = new mdlDashboardGlobal();
        List<mdlDashboardCard> _mdlDashboardCardList = new ArrayList<>();
        List<mdlDashboardChart> _mdlDashboardChartList = new ArrayList<>();
        List<mdlDashboardCardMonthly> _mdlDashboardCardMonthlyList = new ArrayList<>();
        List<mdlDashboardTable> _mdlDashboardTableList = new ArrayList<>();

        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        try {
            sql = "{call sp_dashboard_card_get(?)}";
            listParam.add(QueryAdapter.QueryParam("string", param.user_id));
//            listParam.add(QueryAdapter.QueryParam("string", param.search));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "", db_name, port);
            while (rowset.next()) {
                mdlDashboardCard _mdlDashboardCard = new mdlDashboardCard();
                _mdlDashboardCard.visit_total = rowset.getString("visit_total");
                _mdlDashboardCard.survey_total = rowset.getString("survey_total");
                _mdlDashboardCard.sales_total = rowset.getString("sales_total");
                _mdlDashboardCard.delivery_total = rowset.getString("delivery_total");
                _mdlDashboardCardList.add(_mdlDashboardCard);
            }
            _mdlDashboardGlobal.dashboardCardList = _mdlDashboardCardList;
        } catch (Exception ex) {
            _mdlDashboardCardList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), "", db_name), ex);
        }

        try {
            listParam = new ArrayList<model.Query.mdlQueryExecute>();
            sql = "{call sp_dashboard_chart_get(?)}";
            listParam.add(QueryAdapter.QueryParam("string", param.user_id));
//            listParam.add(QueryAdapter.QueryParam("string", param.search));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "", db_name, port);
            while (rowset.next()) {
                mdlDashboardChart _mdlDashboardChart = new mdlDashboardChart();
                _mdlDashboardChart.chart_type = rowset.getString("chart_type");
                _mdlDashboardChart.chart_date = rowset.getString("chart_date");
                _mdlDashboardChart.chart_total = rowset.getString("chart_total");
                _mdlDashboardChartList.add(_mdlDashboardChart);
            }
            _mdlDashboardGlobal.dashboardChartList = _mdlDashboardChartList;
        } catch (Exception ex) {
            _mdlDashboardChartList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), "", db_name), ex);
        }

        try {
            listParam = new ArrayList<model.Query.mdlQueryExecute>();
            sql = "{call sp_dashboard_chart_monthly_total_get(?)}";
            listParam.add(QueryAdapter.QueryParam("string", param.user_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "", db_name, port);
            while (rowset.next()) {
                mdlDashboardCardMonthly _mdlDashboardCardMonthly = new mdlDashboardCardMonthly();
                _mdlDashboardCardMonthly.visit_total = rowset.getString("visit_total");
                _mdlDashboardCardMonthly.survey_total = rowset.getString("survey_total");
                _mdlDashboardCardMonthly.sales_total = rowset.getString("sales_total");
                _mdlDashboardCardMonthly.delivery_total = rowset.getString("delivery_total");
                _mdlDashboardCardMonthlyList.add(_mdlDashboardCardMonthly);
            }
            _mdlDashboardGlobal.dashboardChartMonthlyTotalList = _mdlDashboardCardMonthlyList;
        } catch (Exception ex) {
            _mdlDashboardCardMonthlyList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), "", db_name), ex);
        }

        try {
            listParam = new ArrayList<model.Query.mdlQueryExecute>();
            sql = "{call sp_dashboard_table_get(?)}";
            listParam.add(QueryAdapter.QueryParam("string", param.user_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "", db_name, port);
            while (rowset.next()) {
                mdlDashboardTable _mdlDashboardTable = new mdlDashboardTable();
                _mdlDashboardTable.employee_id = rowset.getString("employee_id");
                _mdlDashboardTable.employee_name = rowset.getString("employee_name");
                _mdlDashboardTable.branch_id = rowset.getString("branch_id");
                _mdlDashboardTable.branch_name = rowset.getString("branch_name");
                _mdlDashboardTable.visit_success = rowset.getString("visit_success");
                _mdlDashboardTable.total_progress = rowset.getString("total_progress");
                _mdlDashboardTable.total_visit = rowset.getString("total_visit");
                _mdlDashboardTable.last_activity = rowset.getString("last_activity");
                _mdlDashboardTableList.add(_mdlDashboardTable);
            }
            _mdlDashboardGlobal.dashboardTableList = _mdlDashboardTableList;
        } catch (Exception ex) {
            _mdlDashboardTableList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), "", db_name), ex);
        }
        return _mdlDashboardGlobal;
    }
}
