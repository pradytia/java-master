package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import helper.DateHelper;
import model.Customer.mdlCustomer;
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

public class ExportCustomerAdapter {
    final static Logger logger = LogManager.getLogger(ExportCustomerAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static String ExportCustomer(mdlCustomer param1, mdlExportParam param2, int port, String db_name) {
        String sql = "";
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String urlLink = "";
        String user = param1.created_by;
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        CachedRowSet jrs = null;
        List<mdlCustomer> mdlCustomerList = new ArrayList<mdlCustomer>();
        FileOutputStream outPutStream = null;

        try {
            sql = "{call sp_customer_for_export_v1()}";
            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            while (jrs.next()) {
                mdlCustomer _mdlCustomer = new mdlCustomer();
                _mdlCustomer.customer_id = jrs.getString("customer_id");
                _mdlCustomer.branch_id = jrs.getString("branch_id");
                _mdlCustomer.customer_type_id = jrs.getString("customer_type_id");
                _mdlCustomer.customer_name = jrs.getString("customer_name");
                _mdlCustomer.phone = jrs.getString("phone");
                _mdlCustomer.email = jrs.getString("email");
                _mdlCustomer.pic = jrs.getString("pic");
                _mdlCustomer.account = jrs.getString("account");
                _mdlCustomer.gender = jrs.getString("gender");
                _mdlCustomer.customer_info_id = jrs.getString("customer_info_id");
                _mdlCustomer.desc_ = jrs.getString("desc_");
                _mdlCustomer.value_ = jrs.getString("value_");
                _mdlCustomer.type_value = jrs.getString("type_value");
                _mdlCustomer.customer_address = jrs.getString("customer_address");
                _mdlCustomer.seq = jrs.getString("seq");
                _mdlCustomer.longitude = jrs.getString("longitude");
                _mdlCustomer.latitude = jrs.getString("latitude");
                _mdlCustomer.radius = jrs.getString("radius");
                _mdlCustomer.city = jrs.getString("city");
                _mdlCustomer.country_region_code = jrs.getString("country_region_code");
                _mdlCustomer.npwp_no = jrs.getString("npwp_no");
                _mdlCustomer.npwp_name = jrs.getString("npwp_name");
                _mdlCustomer.npwp_address = jrs.getString("npwp_address");
                _mdlCustomer.npwp_city = jrs.getString("npwp_city");
                _mdlCustomer.npwp_zip_code = jrs.getString("npwp_zip_code");
                _mdlCustomer.ktp_no = jrs.getString("ktp_no");
                mdlCustomerList.add(_mdlCustomer);
            }

            //== Create Workbook==\\

            String[] columns = {"ID Pelanggan", "ID Cabang", "ID Tipe Pelanggan", "Nama Pelanggan", "No Telp", "Email",
                    "Gambar", "Akun", "Jenis Kelamin", "ID Pelanggan Info", "Description", "Value",
                    "Type Value", "Alamat Pelanggan", "Urutan", "Longitude", "Latitude", "Radius",
                    "Kota", "Nama Wilayah", "No NPWP", "Nama NPWP", "Alamat NPWP", "Kota NPWP", "Kode Pos NPWP", "No KTP"};


            Sheet sheet = workbook.createSheet("Pelanggan");
            Font headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setColor(IndexedColors.BLACK.getIndex());
            headerFont.setBold(true);

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }

            int rowNum = 1;
            for (mdlCustomer _mdlCustomer : mdlCustomerList) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(_mdlCustomer.customer_id);
                row.createCell(1).setCellValue(_mdlCustomer.branch_id);
                row.createCell(2).setCellValue(_mdlCustomer.customer_type_id);
                row.createCell(3).setCellValue(_mdlCustomer.customer_name);
                row.createCell(4).setCellValue(_mdlCustomer.phone);
                row.createCell(5).setCellValue(_mdlCustomer.email);
                row.createCell(6).setCellValue(_mdlCustomer.pic);
                row.createCell(7).setCellValue(_mdlCustomer.account);
                row.createCell(8).setCellValue(_mdlCustomer.gender);
                row.createCell(9).setCellValue(_mdlCustomer.customer_info_id);
                row.createCell(10).setCellValue(_mdlCustomer.desc_);
                row.createCell(11).setCellValue(_mdlCustomer.value_);
                row.createCell(12).setCellValue(_mdlCustomer.type_value);
                row.createCell(13).setCellValue(_mdlCustomer.customer_address);
                row.createCell(14).setCellValue(_mdlCustomer.seq);
                row.createCell(15).setCellValue(_mdlCustomer.longitude);
                row.createCell(16).setCellValue(_mdlCustomer.latitude);
                row.createCell(17).setCellValue(_mdlCustomer.radius);
                row.createCell(18).setCellValue(_mdlCustomer.city);
                row.createCell(19).setCellValue(_mdlCustomer.country_region_code);
                row.createCell(20).setCellValue(_mdlCustomer.npwp_no);
                row.createCell(21).setCellValue(_mdlCustomer.npwp_name);
                row.createCell(22).setCellValue(_mdlCustomer.npwp_address);
                row.createCell(23).setCellValue(_mdlCustomer.npwp_city);
                row.createCell(24).setCellValue(_mdlCustomer.npwp_zip_code);
                row.createCell(25).setCellValue(_mdlCustomer.ktp_no);

            }

            for (int i = 0; i < columns.length; i++) {
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

                String fileName = "Pelanggan_" + DateHelper.GetDateTimeNowCustomFormat("yyyyMMddHHmmss");
                String url = new String(paramFileUrl + param2.app_name + "/Customer/" + fileName + ".xlsx");
                File createFile = new File(paramFilePath + param2.app_name + "/Customer");
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

    public static String ExportCustomerTemplate(mdlCustomer param1, mdlExportParam param2, int port, String db_name) {
        String sql = "";
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String urlLink = "";
        String user = param1.created_by;
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        CachedRowSet jrs = null;
        List<mdlCustomer> mdlCustomerList = new ArrayList<mdlCustomer>();
        FileOutputStream outPutStream = null;

        try {

            //== Create Workbook==\\
            String[] columns = {"ID Pelanggan", "ID Cabang", "ID Tipe Pelanggan", "Nama Pelanggan", "Alamat Pelanggan", "No Telp", "Email",
                    "PIC", "Akun", "Jenis Kelamin"};

            Sheet sheet = workbook.createSheet("Pelanggan");
            Font headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setColor(IndexedColors.BLACK.getIndex());
            headerFont.setBold(true);

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

                row.createCell(0).setCellValue("Contoh (000001)");
                row.createCell(1).setCellValue("Contoh (000001)");
                row.createCell(2).setCellValue("Contoh (EPT0001)");
                row.createCell(3).setCellValue("Contoh (Pelanggan A)");
                row.createCell(4).setCellValue("Contoh (jl.S.Parman)");
                row.createCell(5).setCellValue("Contoh (0811111)");
                row.createCell(6).setCellValue("Contoh (test@gmail.com)");
                row.createCell(7).setCellValue("Contoh (PIC A)");
                row.createCell(8).setCellValue("Contoh (Test)");
                row.createCell(9).setCellValue("Contoh (Perempuan)");

            }

            for (int i = 0; i < columns.length; i++) {
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

                String fileName = "Pelanggan_" + DateHelper.GetDateTimeNowCustomFormat("yyyyMMddHHmmss");
                String url = new String(paramFileUrl + param2.app_name + "/Customer/" + fileName + ".xlsx");
                File createFile = new File(paramFilePath + param2.app_name + "/Customer");
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
}
