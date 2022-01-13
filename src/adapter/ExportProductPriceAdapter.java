package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import helper.DateHelper;
import model.Export.mdlExportParam;
import model.Product.mdlProductPrice;
import model.Product.mdlProductPriceTyp;
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

public class ExportProductPriceAdapter {
    final static Logger logger = LogManager.getLogger(ExportProductPriceAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static String ExportProductPrice(mdlProductPrice param1, mdlExportParam param2, int port, String db_name){
        String sql = "";
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String urlLink = "";
        String user = param1.created_by;
        Workbook workbook = new XSSFWorkbook();
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        CachedRowSet jrs = null;
        List<mdlProductPrice> mdlProductPriceList = new ArrayList<mdlProductPrice>();
        FileOutputStream outPutStream = null;
        try {
            sql = "{call sp_product_price_for_export()}";
            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

            while (jrs.next()) {
                mdlProductPrice _mdlProductPrice = new mdlProductPrice();
                _mdlProductPrice.product_id = jrs.getString("product_id");
                _mdlProductPrice.product_name = jrs.getString("product_name");
                _mdlProductPrice.branch_id = jrs.getString("branch_id");
                _mdlProductPrice.branch_name = jrs.getString("branch_name");
                _mdlProductPrice.price = jrs.getString("price");
                _mdlProductPrice.module_id = jrs.getString("module_id");
                mdlProductPriceList.add(_mdlProductPrice);
            }

            //== Create Workbook==\\
            String[] columns = {"ID Produk", "Nama Produk", "ID Cabang", "Nama Cabang", "Harga", "ID Modul"};

            Sheet sheet = workbook.createSheet("Harga Produk");
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
            for(mdlProductPrice _mdlProductPrice : mdlProductPriceList){
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(_mdlProductPrice.product_id);
                row.createCell(1).setCellValue(_mdlProductPrice.product_name);
                row.createCell(2).setCellValue(_mdlProductPrice.branch_id);
                row.createCell(3).setCellValue(_mdlProductPrice.branch_name);
                row.createCell(4).setCellValue(_mdlProductPrice.price);
                row.createCell(5).setCellValue(_mdlProductPrice.module_id);
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

                String fileName = "Harga Produk_" + DateHelper.GetDateTimeNowCustomFormat("yyyyMMddHHmmss");
                String url = new String(paramFileUrl+ param2.app_name + "/ProductPrice/" + fileName + ".xlsx");
                File createFile = new File(paramFilePath+ param2.app_name + "/ProductPrice");
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

    public static String ExportProductPriceTemplate(mdlProductPrice param1, mdlExportParam param2, int port, String db_name) {
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
            String[] columns = {"ID Produk", "Nama Produk", "ID Cabang", "Nama Cabang", "Harga"};

            Sheet sheet = workbook.createSheet("Harga Produk");
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
                row.createCell(1).setCellValue("Contoh (Produk A)");
                row.createCell(2).setCellValue("Contoh (001)");
                row.createCell(3).setCellValue("Contoh (Lembang)");
                row.createCell(4).setCellValue("Contoh (10000)");
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
                String fileName = "Harga Produk_" + DateHelper.GetDateTimeNowCustomFormat("yyyyMMddHHmmss");
                String url = paramFileUrl + param2.app_name + "/ProductPrice/" + fileName + ".xlsx";
                File createFile = new File(paramFilePath + param2.app_name + "/ProductPrice");
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

    public static String ExportProductPriceTyp(mdlProductPriceTyp param1, mdlExportParam param2, int port, String db_name){
        String sql = "";
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String urlLink = "";
        String user = param1.created_by;
        Workbook workbook = new XSSFWorkbook();
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        CachedRowSet jrs = null;
        List<mdlProductPriceTyp> mdlProductPriceTypList = new ArrayList<mdlProductPriceTyp>();
        FileOutputStream outPutStream = null;
        try {
            sql = "{call sp_product_price_for_export_typ()}";
            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

            while (jrs.next()) {
                mdlProductPriceTyp _mdlProductPriceTyp = new mdlProductPriceTyp();
                _mdlProductPriceTyp.product_id = jrs.getString("product_id");
                _mdlProductPriceTyp.product_name = jrs.getString("product_name");
                _mdlProductPriceTyp.customer_type_id = jrs.getString("customer_type_id");
                _mdlProductPriceTyp.customer_type_name = jrs.getString("customer_type_name");
                _mdlProductPriceTyp.price = jrs.getString("price");
                _mdlProductPriceTyp.module_id = jrs.getString("module_id");
                mdlProductPriceTypList.add(_mdlProductPriceTyp);
            }

            //== Create Workbook==\\
            String[] columns = {"ID Produk", "Nama Produk", "ID Tipe Pelanggan", "Nama Tipe Pelanggan", "Harga", "ID Modul"};

            Sheet sheet = workbook.createSheet("Harga Produk dr Type Pelanggan");
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
            for(mdlProductPriceTyp _mdlProductPriceTyp : mdlProductPriceTypList){
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(_mdlProductPriceTyp.product_id);
                row.createCell(1).setCellValue(_mdlProductPriceTyp.product_name);
                row.createCell(2).setCellValue(_mdlProductPriceTyp.customer_type_id);
                row.createCell(3).setCellValue(_mdlProductPriceTyp.customer_type_name);
                row.createCell(4).setCellValue(_mdlProductPriceTyp.price);
                row.createCell(5).setCellValue(_mdlProductPriceTyp.module_id);
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

                String fileName = "Harga Produk_" + DateHelper.GetDateTimeNowCustomFormat("yyyyMMddHHmmss");
                String url = new String(paramFileUrl+ param2.app_name + "/ProductPrice/" + fileName + ".xlsx");
                File createFile = new File(paramFilePath+ param2.app_name + "/ProductPrice");
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

    public static String ExportProductPriceTemplateTyp(mdlProductPriceTyp param1, mdlExportParam param2, int port, String db_name) {
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
            String[] columns = {"ID Produk", "Nama Produk", "ID Tipe Pelanggan", "Nama Tipe Pelanggan", "Harga"};

            Sheet sheet = workbook.createSheet("Harga Produk");
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
                row.createCell(1).setCellValue("Contoh (Produk A)");
                row.createCell(2).setCellValue("Contoh (001)");
                row.createCell(3).setCellValue("Contoh (Outlet)");
                row.createCell(4).setCellValue("Contoh (10000)");
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
                String fileName = "Harga Produk_" + DateHelper.GetDateTimeNowCustomFormat("yyyyMMddHHmmss");
                String url = paramFileUrl + param2.app_name + "/ProductPrice/" + fileName + ".xlsx";
                File createFile = new File(paramFilePath + param2.app_name + "/ProductPrice");
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
