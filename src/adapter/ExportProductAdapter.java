package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import helper.DateHelper;
import model.Export.mdlExportParam;
import model.Product.mdlProduct;
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

public class ExportProductAdapter {
    final static Logger logger = LogManager.getLogger(ExportProductAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static String ExportProduct(mdlProduct param1, mdlExportParam param2, int port, String db_name){

        String sql = "";
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String urlLink = "";
        String user = param1.created_by;
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        CachedRowSet jrs = null;
        List<mdlProduct> mdlProductList = new ArrayList<mdlProduct>();
        FileOutputStream outPutStream = null;

        try {

            sql = "{call sp_product_for_export()}";
            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

            while (jrs.next()) {
                mdlProduct _mdlProduct = new mdlProduct();
                _mdlProduct.product_id = jrs.getString("product_id");
                _mdlProduct.product_name = jrs.getString("product_name");
                _mdlProduct.product_type = jrs.getString("product_type");
                _mdlProduct.product_group = jrs.getString("product_group");
                _mdlProduct.product_weight = jrs.getString("product_weight");
                _mdlProduct.uom = jrs.getString("uom");
                _mdlProduct.dnr_code = jrs.getString("dnr_code");
                _mdlProduct.sap_code = jrs.getString("sap_code");
                _mdlProduct.price = jrs.getString("price");
                _mdlProduct.module_id = jrs.getString("module_id");

                mdlProductList.add(_mdlProduct);
            }

            //== Create Workbook==\\

            String[] columns = {"ID Produk", "Nama Produk", "Tipe Produk", "Grup Produk", "Berat Produk", "UOM", "Kode DNR", "Kode SAP", "Harga", "Termasuk PPN", "ID Modul"};

            Sheet sheet = workbook.createSheet("Produk");
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
            for(mdlProduct _mdlProduct : mdlProductList){
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(_mdlProduct.product_id);
                row.createCell(1).setCellValue(_mdlProduct.product_name);
                row.createCell(2).setCellValue(_mdlProduct.product_type);
                row.createCell(3).setCellValue(_mdlProduct.product_group);
                row.createCell(4).setCellValue(_mdlProduct.product_weight);
                row.createCell(5).setCellValue(_mdlProduct.uom);
                row.createCell(6).setCellValue(_mdlProduct.dnr_code);
                row.createCell(7).setCellValue(_mdlProduct.sap_code);
                row.createCell(8).setCellValue(_mdlProduct.price);
                row.createCell(9).setCellValue(_mdlProduct.is_ppn_include);
                row.createCell(10).setCellValue(_mdlProduct.module_id);
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

                String fileName = "Produk_" + DateHelper.GetDateTimeNowCustomFormat("yyyyMMddHHmmss");
                String url = new String(paramFileUrl+ param2.app_name + "/Product/" + fileName + ".xlsx");
                File createFile = new File(paramFilePath+ param2.app_name + "/Product");
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

    public static String ExportProductTemplate(mdlProduct param1, mdlExportParam param2, int port, String db_name) {
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
            String[] columns = {"ID Produk", "Nama Produk", "Tipe Produk", "Grup Produk", "Berat Produk", "UOM", "Kode DNR", "Kode SAP", "Harga"};

            Sheet sheet = workbook.createSheet("Produk");
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
                row.createCell(2).setCellValue("Contoh (0001)");
                row.createCell(3).setCellValue("Contoh (0001)");
                row.createCell(4).setCellValue("Contoh (50)");
                row.createCell(5).setCellValue("Contoh (PCS)");
                row.createCell(6).setCellValue("Contoh (001)");
                row.createCell(7).setCellValue("Contoh (001)");
                row.createCell(8).setCellValue("Contoh (10000)");
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
                String fileName = "Produk" + DateHelper.GetDateTimeNowCustomFormat("yyyyMMddHHmmss");
                String url = paramFileUrl + param2.app_name + "/Product/" + fileName + ".xlsx";
                File createFile = new File(paramFilePath + param2.app_name + "/Product");
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
