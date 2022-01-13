package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import helper.DateHelper;
import model.Customer.mdlCustomerCoordinate;
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

public class ExportCustomerCoordinateAdapter {

    final static Logger logger = LogManager.getLogger(ExportCustomerCoordinateAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static String ExportCustomerCoordinate(mdlCustomerCoordinate param1, mdlExportParam param2, int port, String db_name){

        String sql = "";
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String urlLink = "";
        String user = param1.created_by;
        Workbook workbook = new XSSFWorkbook();
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        CachedRowSet jrs = null;
        List<mdlCustomerCoordinate> mdlCustomerCoordinateList = new ArrayList<mdlCustomerCoordinate>();
        FileOutputStream outPutStream = null;

        try {

            sql = "{call sp_customer_coordinate_for_export_v2(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param1.customer_id));
            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

            while (jrs.next()) {
                mdlCustomerCoordinate _mdlCustomerCoordinate = new mdlCustomerCoordinate();
                _mdlCustomerCoordinate.customer_id = jrs.getString("customer_id");
                _mdlCustomerCoordinate.seq = jrs.getString("seq");
                _mdlCustomerCoordinate.customer_address = jrs.getString("customer_address");
                _mdlCustomerCoordinate.radius = jrs.getString("radius");
                _mdlCustomerCoordinate.city = jrs.getString("city");
                _mdlCustomerCoordinate.country_region_code = jrs.getString("country_region_code");
                _mdlCustomerCoordinate.latitude = jrs.getString("latitude");
                _mdlCustomerCoordinate.longitude = jrs.getString("longitude");
                mdlCustomerCoordinateList.add(_mdlCustomerCoordinate);
            }

            //== Create Workbook==\\

            String[] columns = {"ID Pelanggan", "Urutan", "Alamat Pelanggan", "Radius", "Kota", "Nama Wilayah", "Latitude", "Longitude"};


            Sheet sheet = workbook.createSheet("Alamat Pelanggan");
            Font headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 12);
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
            for(mdlCustomerCoordinate _mdlCustomerCoordinate : mdlCustomerCoordinateList){
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(_mdlCustomerCoordinate.customer_id);
                row.createCell(1).setCellValue(_mdlCustomerCoordinate.seq);
                row.createCell(2).setCellValue(_mdlCustomerCoordinate.customer_address);
                row.createCell(3).setCellValue(_mdlCustomerCoordinate.radius);
                row.createCell(4).setCellValue(_mdlCustomerCoordinate.city);
                row.createCell(5).setCellValue(_mdlCustomerCoordinate.country_region_code);
                row.createCell(6).setCellValue(_mdlCustomerCoordinate.latitude);
                row.createCell(7).setCellValue(_mdlCustomerCoordinate.longitude);
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

                String fileName = "AlamatPelanggan_" + DateHelper.GetDateTimeNowCustomFormat("yyyyMMddHHmmss");
                String url = new String(paramFileUrl+ param2.app_name + "/CustomerAddress/" + fileName + ".xlsx");
                File createFile = new File(paramFilePath+ param2.app_name + "/CustomerAddress");
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

    public static String ExportCustomerCoordinateAll(mdlCustomerCoordinate param1, mdlExportParam param2, int port, String db_name){
        String sql = "";
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String urlLink = "";
        String user = param1.created_by;
        Workbook workbook = new XSSFWorkbook();
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        CachedRowSet jrs = null;
        List<mdlCustomerCoordinate> mdlCustomerCoordinateList = new ArrayList<mdlCustomerCoordinate>();
        FileOutputStream outPutStream = null;

        try {

            sql = "{call sp_customer_coordinate_for_export_all()}";
            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

            while (jrs.next()) {
                mdlCustomerCoordinate _mdlCustomerCoordinate = new mdlCustomerCoordinate();
                _mdlCustomerCoordinate.customer_id = jrs.getString("customer_id");
                _mdlCustomerCoordinate.seq = jrs.getString("seq");
                _mdlCustomerCoordinate.customer_address = jrs.getString("customer_address");
                _mdlCustomerCoordinate.radius = jrs.getString("radius");
                _mdlCustomerCoordinate.city = jrs.getString("city");
                _mdlCustomerCoordinate.country_region_code = jrs.getString("country_region_code");
                _mdlCustomerCoordinate.latitude = jrs.getString("latitude");
                _mdlCustomerCoordinate.longitude = jrs.getString("longitude");
                mdlCustomerCoordinateList.add(_mdlCustomerCoordinate);
            }

            //== Create Workbook==\\
            String[] columns = {"ID Pelanggan", "Urutan", "Alamat Pelanggan", "Radius", "Kota", "Nama Wilayah", "Latitude", "Longitude"};

            Sheet sheet = workbook.createSheet("Alamat Pelanggan");
            Font headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 12);
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
            for(mdlCustomerCoordinate _mdlCustomerCoordinate : mdlCustomerCoordinateList){
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(_mdlCustomerCoordinate.customer_id);
                row.createCell(1).setCellValue(_mdlCustomerCoordinate.seq);
                row.createCell(2).setCellValue(_mdlCustomerCoordinate.customer_address);
                row.createCell(3).setCellValue(_mdlCustomerCoordinate.radius);
                row.createCell(4).setCellValue(_mdlCustomerCoordinate.city);
                row.createCell(5).setCellValue(_mdlCustomerCoordinate.country_region_code);
                row.createCell(6).setCellValue(_mdlCustomerCoordinate.latitude);
                row.createCell(7).setCellValue(_mdlCustomerCoordinate.longitude);
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

                String fileName = "AlamatPelanggan_" + DateHelper.GetDateTimeNowCustomFormat("yyyyMMddHHmmss");
                String url = new String(paramFileUrl+ param2.app_name + "/CustomerAddress/" + fileName + ".xlsx");
                File createFile = new File(paramFilePath+ param2.app_name + "/CustomerAddress");
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
}
