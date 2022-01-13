package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import helper.DateHelper;
import model.Branch.mdlBranch;
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

public class ExportBranchAdapter {
    final static Logger logger = LogManager.getLogger(ExportBranchAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static String ExportBranch(mdlBranch param1, mdlExportParam param2, int port, String db_name) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String urlLink = "";
        String user = param1.created_by;
        Workbook workbook = new XSSFWorkbook();
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        CachedRowSet jrs = null;
        List<mdlBranch> mdlBranchList = new ArrayList<mdlBranch>();
        FileOutputStream outPutStream = null;
        try {
            sql = "{call sp_branch_for_export()}";
            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            while (jrs.next()) {
                mdlBranch _mdlBranch = new mdlBranch();
                _mdlBranch.branch_id = jrs.getString("branch_id");
                _mdlBranch.district_id = jrs.getString("district_id");
                _mdlBranch.region_id = jrs.getString("region_id");
                _mdlBranch.area_id = jrs.getString("area_id");
                _mdlBranch.branch_name = jrs.getString("branch_name");
                _mdlBranch.branch_description = jrs.getString("branch_description");
                _mdlBranch.latitude = jrs.getString("latitude");
                _mdlBranch.longitude = jrs.getString("longitude");
                _mdlBranch.city = jrs.getString("city");
                _mdlBranch.post_code = jrs.getString("post_code");
                _mdlBranch.country_region_code = jrs.getString("country_region_code");
                _mdlBranch.phone = jrs.getString("phone");
                _mdlBranch.fax = jrs.getString("fax");
                mdlBranchList.add(_mdlBranch);
            }
            //== Create Workbook==\\
            String[] columns = {"ID Cabang", "ID Distrik", "ID Wilayah", "ID Daerah", "Nama Cabang", "Deskripsi Cabang", "Latitude", "Longitude", "Kota", "Kode Pos", "Nama Wilayah", "No Telp", "Fax"};
            Sheet sheet = workbook.createSheet("Cabang");
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
            for (mdlBranch _mdlBranch : mdlBranchList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(_mdlBranch.branch_id);
                row.createCell(1).setCellValue(_mdlBranch.district_id);
                row.createCell(2).setCellValue(_mdlBranch.region_id);
                row.createCell(3).setCellValue(_mdlBranch.area_id);
                row.createCell(4).setCellValue(_mdlBranch.branch_name);
                row.createCell(5).setCellValue(_mdlBranch.branch_description);
                row.createCell(6).setCellValue(_mdlBranch.latitude);
                row.createCell(7).setCellValue(_mdlBranch.longitude);
                row.createCell(8).setCellValue(_mdlBranch.city);
                row.createCell(9).setCellValue(_mdlBranch.post_code);
                row.createCell(10).setCellValue(_mdlBranch.country_region_code);
                row.createCell(11).setCellValue(_mdlBranch.phone);
                row.createCell(12).setCellValue(_mdlBranch.fax);
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

                String fileName = "Cabang_" + DateHelper.GetDateTimeNowCustomFormat("yyyyMMddHHmmss");
                String url = paramFileUrl + param2.app_name + "/Branch/" + fileName + ".xlsx";
                File createFile = new File(paramFilePath + param2.app_name + "/Branch");
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

    public static String ExportBranchTemplate(mdlBranch param1, mdlExportParam param2, int port, String db_name) {
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
            String[] columns = {"ID Cabang", "ID Distrik", "ID Wilayah", "ID Daerah", "Nama Cabang", "Deskripsi Cabang", "Latitude", "Longitude", "Kota", "Kode Pos", "Nama Wilayah", "No Telp", "Fax"};

            Sheet sheet = workbook.createSheet("Cabang");
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
                row.createCell(1).setCellValue("Contoh (0001)");
                row.createCell(2).setCellValue("Contoh (0001)");
                row.createCell(3).setCellValue("Contoh (0001)");
                row.createCell(4).setCellValue("Contoh (lembang)");
                row.createCell(5).setCellValue("Contoh (wilayah jabar)");
                row.createCell(6).setCellValue("Contoh (0)");
                row.createCell(7).setCellValue("Contoh (0)");
                row.createCell(8).setCellValue("Contoh (Bandung)");
                row.createCell(9).setCellValue("Contoh (151111)");
                row.createCell(10).setCellValue("Contoh (Bandung)");
                row.createCell(11).setCellValue("Contoh (08111)");
                row.createCell(12).setCellValue("Contoh (123)");
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
                String fileName = "Cabang_" + DateHelper.GetDateTimeNowCustomFormat("yyyyMMddHHmmss");
                String url = paramFileUrl + param2.app_name + "/Branch/" + fileName + ".xlsx";
                File createFile = new File(paramFilePath + param2.app_name + "/Branch");
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
