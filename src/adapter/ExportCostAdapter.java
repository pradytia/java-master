package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import helper.DateHelper;
import model.Cost.mdlCost;
import model.Export.mdlExportParam;
import model.Query.mdlQueryExecute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.rowset.CachedRowSet;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ExportCostAdapter {

    final static Logger logger = LogManager.getLogger(ExportCostAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static String ExportCost(mdlCost param1, mdlExportParam param2, int port, String db_name) {

        String sql = "";
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String urlLink = "";
        String user = param1.created_by;
        Workbook workbook = new XSSFWorkbook();
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        CachedRowSet jrs = null;
        List<mdlCost> mdlCostList = new ArrayList<mdlCost>();
        FileOutputStream outPutStream = null;

        try {

            sql = "{call sp_cost_for_export()}";
            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

            while (jrs.next()) {
                mdlCost _mdlCost = new mdlCost();
                _mdlCost.cost_id = jrs.getString("cost_id");
                _mdlCost.cost_name = jrs.getString("cost_name");
                mdlCostList.add(_mdlCost);
            }

            //== Create Workbook==\\

            String[] columns = {"ID Biaya", "Nama Biaya"};


            Sheet sheet = workbook.createSheet("Biaya");
            Font headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 11);
            headerFont.setColor(IndexedColors.BLACK.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }


            int rowNum = 1;
            for (mdlCost _mdlCost : mdlCostList) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(_mdlCost.cost_id);
                row.createCell(1).setCellValue(_mdlCost.cost_name);
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }


            try {
                Context context = (Context) new InitialContext().lookup("java:comp/env");
                String paramFilePath = "";
                String paramFileUrl = "";
                if (port==443){
                    paramFilePath = (String) context.lookup("path_web_server_for_upload_report");
                    paramFileUrl = (String) context.lookup("url_web_server_for_upload_report");
                }else {
                    paramFilePath = (String) context.lookup("path_web_server_for_upload_report_dev");
                    paramFileUrl = (String) context.lookup("url_web_server_for_upload_report_dev");
                }

                String fileName = "Biaya_" + DateHelper.GetDateTimeNowCustomFormat("yyyyMMddHHmmss");
                String url = new String(paramFileUrl + param2.app_name + "/Cost/" + fileName + ".xlsx");
                File createFile = new File(paramFilePath + param2.app_name + "/Cost");
                createFile.mkdirs();

                File myFile = new File(createFile.getAbsolutePath() + "/" + fileName + ".xlsx");

                outPutStream = new FileOutputStream(myFile);
                urlLink = url;
                workbook.write(outPutStream);
                outPutStream.close();
                workbook.close();
            } catch (Exception ex) {
                logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param2), "", ex.toString(), user, db_name), ex);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param1), "", ex.toString(), user, db_name), ex);
        }
        return urlLink;
    }

    public static String ExportCostTemplate(mdlCost param1, mdlExportParam param2, int port, String db_name) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String urlLink = "";
        String user = param1.created_by;
        Workbook workbook = new XSSFWorkbook();
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        CachedRowSet jrs = null;
        FileOutputStream outPutStream = null;
        try {
            //== Create Workbook==\\
            String[] columns = {"ID Biaya", "Nama Biaya"};

            Sheet sheet = workbook.createSheet("Biaya");
            Font headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 11);
            headerFont.setColor(IndexedColors.BLACK.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }

            int rowNum = 1;
            for (int i = 0; i < 1; i++) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue("Contoh (0001)");
                row.createCell(1).setCellValue("Contoh (Biaya A)");
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try {
                Context context = (Context) new InitialContext().lookup("java:comp/env");
                String paramFilePath = "";
                String paramFileUrl = "";
                if (port==443){
                    paramFilePath = (String) context.lookup("path_web_server_for_upload_report");
                    paramFileUrl = (String) context.lookup("url_web_server_for_upload_report");
                }else {
                    paramFilePath = (String) context.lookup("path_web_server_for_upload_report_dev");
                    paramFileUrl = (String) context.lookup("url_web_server_for_upload_report_dev");
                }
                String fileName = "Biaya_" + DateHelper.GetDateTimeNowCustomFormat("yyyyMMddHHmmss");
                String url = paramFileUrl + param2.app_name + "/Cost/" + fileName + ".xlsx";
                File createFile = new File(paramFilePath + param2.app_name + "/Cost");
                createFile.mkdirs();

                File myFile = new File(createFile.getAbsolutePath() + "/" + fileName + ".xlsx");

                outPutStream = new FileOutputStream(myFile);
                urlLink = url;
                workbook.write(outPutStream);
                outPutStream.close();
                workbook.close();
            } catch (Exception ex) {
                logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(param2), "", ex.toString(), user, db_name), ex);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(param1), "", ex.toString(), user, db_name), ex);
        }
        return urlLink;
    }
}
