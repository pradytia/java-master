package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import helper.DateHelper;
import model.Employee.mdlEmployee;
import model.Export.mdlExportParam;
import model.Query.mdlQueryExecute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.rowset.CachedRowSet;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

public class ExportEmployeeAdapter {
    final static Logger logger = LogManager.getLogger(ExportEmployeeAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static String ExportEmployee(mdlEmployee param1, mdlExportParam param2, int port, String db_name){
        String sql = "";
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String urlLink = "";
        String user = param1.created_by;
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        CachedRowSet jrs = null;
        List<mdlEmployee> mdlEmployeeList = new ArrayList<mdlEmployee>();
        FileOutputStream outPutStream = null;

        try {
            sql = "{call sp_employee_for_export()}";
            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

            while (jrs.next()) {
                mdlEmployee _mdlEmployee = new mdlEmployee();
                _mdlEmployee.employee_id = jrs.getString("employee_id");
                _mdlEmployee.employee_name = jrs.getString("employee_name");
                _mdlEmployee.employee_type_id = jrs.getString("employee_type_id");
                _mdlEmployee.branch_id = jrs.getString("branch_id");
                _mdlEmployee.entry_date = jrs.getString("entry_date");
                _mdlEmployee.gender = jrs.getString("gender");
                _mdlEmployee.email = jrs.getString("email");
                _mdlEmployee.phone = jrs.getString("phone");
                _mdlEmployee.out_date = jrs.getString("out_date");

                List<String> branchIDs = EmployeeAdapter.GetBranchIDsByEmployeeID(_mdlEmployee.employee_id, db_name, port);
                _mdlEmployee.branch_id_list = branchIDs;
                mdlEmployeeList.add(_mdlEmployee);
            }

            //== Create Workbook==\\

            String[] columns = {"ID Pegawai", "Nama Pegawai", "ID Tipe Pegawai", "ID Cabang", "Tanggal Masuk","Jenis Kelamin", "Email", "Phone", "Tanggal Keluar"};

            Sheet sheet = workbook.createSheet("Pegawai");
            Font headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setColor(IndexedColors.BLACK.getIndex());
            headerFont.setBold(true);

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);

            for(int i = 0; i < columns.length; i++ ){
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }

            int rowNum = 1;
            for(mdlEmployee _mdlEmployee : mdlEmployeeList){
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(_mdlEmployee.employee_id);
                row.createCell(1).setCellValue(_mdlEmployee.employee_name);
                row.createCell(2).setCellValue(_mdlEmployee.employee_type_id);
                String branchID = _mdlEmployee.branch_id_list.stream().collect(joining(", "));
                row.createCell(3).setCellValue(branchID);
                row.createCell(4).setCellValue(_mdlEmployee.entry_date);
                row.createCell(5).setCellValue(_mdlEmployee.gender);
                row.createCell(6).setCellValue(_mdlEmployee.email);
                row.createCell(7).setCellValue(_mdlEmployee.phone);
                row.createCell(8).setCellValue(_mdlEmployee.out_date);
            }

            for (int i = 0; i < columns.length; i++){
                sheet.setColumnWidth(i, 25 * 256);
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

                String fileName = "Pegawai_" + DateHelper.GetDateTimeNowCustomFormat("yyyyMMddHHmmss");
                String url = new String(paramFileUrl+ param2.app_name +"/Employee/" + fileName + ".xlsx");
                File createFile = new File(paramFilePath+ param2.app_name + "/Employee");
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

    public static String ExportEmployeeTemplate(mdlEmployee param1, mdlExportParam param2, int port, String db_name) {
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
            String[] columns = {"ID Pegawai", "Nama Pegawai", "ID Tipe Pegawai", "ID Cabang", "Tanggal Masuk","Jenis Kelamin", "Email", "Phone", "Tanggal Keluar"};

            Sheet sheet = workbook.createSheet("Pegawai");
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
                row.createCell(2).setCellValue("Contoh (001)");
                row.createCell(3).setCellValue("Contoh (0001)");
                row.createCell(4).setCellValue("Contoh (2020-01-01)");
                row.createCell(5).setCellValue("Contoh (Perempuan)");
                row.createCell(6).setCellValue("Contoh (test@gmail.com)");
                row.createCell(7).setCellValue("Contoh (08129231231)");
                row.createCell(8).setCellValue("Contoh (2029-01-01)");
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
                String fileName = "Pegawai_" + DateHelper.GetDateTimeNowCustomFormat("yyyyMMddHHmmss");
                String url = paramFileUrl + param2.app_name + "/Employee/" + fileName + ".xlsx";
                File createFile = new File(paramFilePath + param2.app_name + "/Employee");
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
