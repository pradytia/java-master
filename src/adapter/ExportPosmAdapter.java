package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import helper.DateHelper;
import model.Export.mdlExportParam;
import model.POSM.mdlPOSMProduct;
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

public class ExportPosmAdapter {

    final static Logger logger = LogManager.getLogger(ExportPosmAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static String ExportPosm(mdlPOSMProduct param1, mdlExportParam param2, int port, String db_name){
        String sql = "";
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String urlLink = "";
        String user = param1.created_by;
        Workbook workbook = new XSSFWorkbook();
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        CachedRowSet jrs = null;
        List<mdlPOSMProduct> mdlPOSMProductList = new ArrayList<mdlPOSMProduct>();
        FileOutputStream outPutStream = null;

        try {
            sql = "{call sp_posm_for_export()}";
            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

            while (jrs.next()) {
                mdlPOSMProduct _mdlPOSMProduct = new mdlPOSMProduct();
                _mdlPOSMProduct.posm_id = jrs.getString("posm_id");
                _mdlPOSMProduct.posm_name = jrs.getString("posm_name");
                _mdlPOSMProduct.posm_name = jrs.getString("total_stock");

                mdlPOSMProductList.add(_mdlPOSMProduct);
            }

            //== Create Workbook==\\

            String[] columns = {"ID POSM", "Nama POSM", "Total Stock"};

            Sheet sheet = workbook.createSheet("POSM-Produk");
            Font headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 11);
            headerFont.setColor(IndexedColors.BLACK.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);

            for(int i = 0; i < columns.length; i++ ){
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }

            int rowNum = 1;
            for(mdlPOSMProduct _mdlPOSMProduct : mdlPOSMProductList){
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(_mdlPOSMProduct.posm_id);
                row.createCell(1).setCellValue(_mdlPOSMProduct.posm_name);
                row.createCell(2).setCellValue(_mdlPOSMProduct.total_stock);
            }

            for (int i = 0; i < columns.length; i++){
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

                String fileName = "POSM_" + DateHelper.GetDateTimeNowCustomFormat("yyyyMMddHHmmss");
                String url = new String(paramFileUrl+ param2.app_name +"/Posm/" + fileName + ".xlsx");
                File createFile = new File(paramFilePath+ param2.app_name + "/Posm");
                createFile.mkdirs();

                File myFile = new File(createFile.getAbsolutePath() + "/" + fileName + ".xlsx");

                outPutStream = new FileOutputStream(myFile);
                urlLink = url;
                workbook.write(outPutStream);
                outPutStream.close();
                workbook.close();
            }catch (Exception ex){
                logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param2), "", ex.toString(), user, db_name), ex);
            }
        }catch (Exception ex){
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param1), "", ex.toString(), user, db_name), ex);
        }
        return urlLink;
    }

    public static String ExportPOSMTemplate(mdlPOSMProduct param1, mdlExportParam param2, int port, String db_name) {
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
            String[] columns = {"ID POSM", "Nama POSM", "Total Stock"};

            Sheet sheet = workbook.createSheet("POSM-Produk");
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
                row.createCell(1).setCellValue("Contoh (TEST)");
                row.createCell(2).setCellValue("Contoh (20.25)");
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
                String fileName = "POSM_" + DateHelper.GetDateTimeNowCustomFormat("yyyyMMddHHmmss");
                String url = paramFileUrl + param2.app_name + "/Posm/" + fileName + ".xlsx";
                File createFile = new File(paramFilePath + param2.app_name + "/Posm");
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
