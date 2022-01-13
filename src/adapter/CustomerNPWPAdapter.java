package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import helper.DateHelper;
import model.CustomerNPWP.mdlCustomerNPWP;
import model.DataTable.mdlDataTableParam;
import model.Export.mdlExportParam;
import model.Query.mdlQueryExecute;
import model.Query.mdlResponseQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.rowset.CachedRowSet;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CustomerNPWPAdapter {
    final static Logger logger = LogManager.getLogger(CustomerNPWPAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlCustomerNPWP> GetCustomerNPWPByCustomerID(String customerID, String user, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlCustomerNPWP> listCustomerNPWP = new ArrayList<mdlCustomerNPWP>();
        List<mdlQueryExecute> listParam = new ArrayList<mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_customer_npwp_get (?)} ";
            listParam.add(QueryAdapter.QueryParam("string", customerID));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, user, db_name, port);
            while (rowset.next()) {
                mdlCustomerNPWP _customerNPWP = new mdlCustomerNPWP();
                _customerNPWP.customer_id = rowset.getString("customer_id");
                _customerNPWP.npwp_no = rowset.getString("npwp_no");
                _customerNPWP.npwp_name = rowset.getString("npwp_name");
                _customerNPWP.npwp_address = rowset.getString("npwp_address");
                _customerNPWP.npwp_city = rowset.getString("npwp_city");
                _customerNPWP.npwp_zip_code = rowset.getString("npwp_zip_code");
                _customerNPWP.ktp_no = rowset.getString("ktp_no");
                listCustomerNPWP.add(_customerNPWP);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), user, db_name), ex);
        }
        return listCustomerNPWP;
    }

    public static List<mdlCustomerNPWP> GetCustomerNPWPAll(String user, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlCustomerNPWP> listCustomerNPWP = new ArrayList<mdlCustomerNPWP>();
        List<mdlQueryExecute> listParam = new ArrayList<mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_customer_npwp_get_all ()} ";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, user, db_name);
            while (rowset.next()) {
                mdlCustomerNPWP _customerNPWP = new mdlCustomerNPWP();
                _customerNPWP.customer_id = rowset.getString("customer_id");
                _customerNPWP.npwp_no = rowset.getString("npwp_no");
                _customerNPWP.npwp_name = rowset.getString("npwp_name");
                _customerNPWP.npwp_address = rowset.getString("npwp_address");
                _customerNPWP.npwp_city = rowset.getString("npwp_city");
                _customerNPWP.npwp_zip_code = rowset.getString("npwp_zip_code");
                _customerNPWP.ktp_no = rowset.getString("ktp_no");
                listCustomerNPWP.add(_customerNPWP);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), user, db_name), ex);
        }
        return listCustomerNPWP;
    }

    public static List<mdlCustomerNPWP> GetCustomerNPWPWithPagingByCustomerID(mdlDataTableParam param, String customerID, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<mdlCustomerNPWP> _mdlCustomerNPWPList = new ArrayList<>();
        CachedRowSet rowset = null;
        String sql = "";
        String search_part = "";
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<>();
        try {
            sql = "{call sp_customer_npwp_get_with_paging(?,?,?,?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", customerID));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.page_size));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "customer list", db_name, port);
            while (rowset.next()) {
                mdlCustomerNPWP _customerNPWP = new mdlCustomerNPWP();
                _customerNPWP.customer_id = rowset.getString("customer_id");
                _customerNPWP.npwp_no = rowset.getString("npwp_no");
                _customerNPWP.npwp_name = rowset.getString("npwp_name");
                _customerNPWP.npwp_address = rowset.getString("npwp_address");
                _customerNPWP.npwp_city = rowset.getString("npwp_city");
                _customerNPWP.npwp_zip_code = rowset.getString("npwp_zip_code");
                _customerNPWP.ktp_no = rowset.getString("ktp_no");
                _customerNPWP.is_active = rowset.getString("is_active");
                _mdlCustomerNPWPList.add(_customerNPWP);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "customer list", db_name), ex);
        }
        return _mdlCustomerNPWPList;
    }

    public static int GetCustomerNPWPTotalListByCustomerID(mdlDataTableParam param, String customerID, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;
        String sql = "";
        int returnValue = 0;
        String search_part = "";
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_customer_npwp_get_total_list(?,?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", customerID));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "customer NPWP total", db_name, port);
            while (crs.next()) {
                returnValue = crs.getInt("total");
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "customer NPWP total", db_name), ex);
        }
        return returnValue;
    }

    public static boolean DeleteCustomerNPWPByCustomerIDAndNoNPWP(mdlCustomerNPWP param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = "";
        String sql = "";
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_customer_npwp_delete(?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.customer_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.npwp_no));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean InsertOrUpdateCustomerNPWP(mdlCustomerNPWP param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = "admin";
        String sql = "";
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_customer_npwp_upload(?,?,?,?,?,?,?,?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.customer_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.npwp_no));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.npwp_name));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.npwp_address));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.npwp_city));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.npwp_zip_code));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.ktp_no));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", user));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", user));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean UpdateStatusCustomerNPWP(mdlCustomerNPWP param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_customer_npwp_update_status(?,?,?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.customer_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.npwp_no));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.is_active));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static String ExportCustomerNPWP(mdlCustomerNPWP param1, mdlExportParam param2, int port, String db_name) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String urlLink = "";
        String user = param1.created_by;
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
        List<mdlCustomerNPWP> mdlCustomerNPWPList = new ArrayList<mdlCustomerNPWP>();
        FileOutputStream outPutStream = null;
        try {
            mdlCustomerNPWPList = GetCustomerNPWPByCustomerID(param1.customer_id, param1.created_by, db_name, port);

            //== Create Workbook==\\
            String[] columns = {"ID Pelanggan", "No NPWP", "Nama NPWP", "Alamat"};
            Sheet sheet = workbook.createSheet("Pelanggan NPWP");
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
            for (mdlCustomerNPWP _mdlCustomerNPWP : mdlCustomerNPWPList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(_mdlCustomerNPWP.customer_id);
                row.createCell(1).setCellValue(_mdlCustomerNPWP.npwp_no);
                row.createCell(2).setCellValue(_mdlCustomerNPWP.npwp_name);
                row.createCell(3).setCellValue(_mdlCustomerNPWP.npwp_address);
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

                String fileName = "NPWP_Pelanggan" + DateHelper.GetDateTimeNowCustomFormat("yyyyMMddHHmmss");
                String url = paramFileUrl + param2.app_name + "/CustomerNPWP/" + fileName + ".xlsx";
                File createFile = new File(paramFilePath + param2.app_name + "/CustomerNPWP");
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

    public static String ExportCustomerNPWPAll(mdlCustomerNPWP param1, mdlExportParam param2, int port, String db_name) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String urlLink = "";
        String user = param1.created_by;
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
        List<mdlCustomerNPWP> mdlCustomerNPWPList = new ArrayList<mdlCustomerNPWP>();
        FileOutputStream outPutStream = null;
        try {
            mdlCustomerNPWPList = GetCustomerNPWPAll(param1.created_by, db_name, port);

            //== Create Workbook==\\
            String[] columns = {"ID Pelanggan", "No NPWP", "Nama NPWP", "Alamat"};
            Sheet sheet = workbook.createSheet("Pelanggan NPWP");
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
            for (mdlCustomerNPWP _mdlCustomerNPWP : mdlCustomerNPWPList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(_mdlCustomerNPWP.customer_id);
                row.createCell(1).setCellValue(_mdlCustomerNPWP.npwp_no);
                row.createCell(2).setCellValue(_mdlCustomerNPWP.npwp_name);
                row.createCell(3).setCellValue(_mdlCustomerNPWP.npwp_address);
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

                String fileName = "NPWP_Pelanggan" + DateHelper.GetDateTimeNowCustomFormat("yyyyMMddHHmmss");
                String url = paramFileUrl + param2.app_name + "/CustomerNPWP/" + fileName + ".xlsx";
                File createFile = new File(paramFilePath + param2.app_name + "/CustomerNPWP");
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

    public static mdlResponseQuery ImportExcelCustomer(Workbook workbook, mdlCustomerNPWP param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String user = param.created_by;
        boolean resultExec = false;
        List<mdlCustomerNPWP> _mdlCustomerNPWPList = new ArrayList<mdlCustomerNPWP>();
        mdlResponseQuery _mdlResponseQuery = new mdlResponseQuery();
        ArrayList<String> customerID = new ArrayList<>();
        ArrayList<String> NPWPNo = new ArrayList<>();
        try {
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> _rowIterator = firstSheet.iterator();
            ArrayList<String> errorList = new ArrayList<String>();
            ArrayList<Integer> errorRow = new ArrayList<Integer>();
            int errorIndicator = 0;
            int columnIndicator = 1;

            //Skipping Excel header => looping ke bawah(row)
            while (_rowIterator.hasNext()) {
                Row currentRow = _rowIterator.next();
                int rowIndex = currentRow.getRowNum();

                if (rowIndex == 0) {
                    continue;
                }

                Iterator<Cell> _cellIterator = currentRow.cellIterator();
                mdlCustomerNPWP _mdlCustomerNPWP = new mdlCustomerNPWP();

                //looping ke samping column
                while (_cellIterator.hasNext()) {
                    Cell next_cell = _cellIterator.next();
                    int column_index = next_cell.getColumnIndex();
                    //proses get value from excel and set into model
                    if (column_index == 0) {
                        if (next_cell.getCellType() == CellType.STRING) {
                            String valueCustomerID = next_cell.getStringCellValue();
                            _mdlCustomerNPWP.customer_id = valueCustomerID;
                        } else if (next_cell.getCellType() == CellType.NUMERIC) {
                            String valueCustomerID = NumberToTextConverter.toText(next_cell.getNumericCellValue());
                            _mdlCustomerNPWP.customer_id = valueCustomerID;
                        }
                    } else if (column_index == 1) {
                        if (next_cell.getCellType() == CellType.STRING) {
                            String valueNPWPNo = next_cell.getStringCellValue();
                            _mdlCustomerNPWP.npwp_no = valueNPWPNo;
                        } else if (next_cell.getCellType() == CellType.NUMERIC) {
                            String valueNPWPNo = NumberToTextConverter.toText(next_cell.getNumericCellValue());
                            _mdlCustomerNPWP.npwp_no = valueNPWPNo;
                        }
                    } else if (column_index == 2) {
                        if (next_cell.getCellType() == CellType.STRING) {
                            String valueNPWPName = next_cell.getStringCellValue();
                            _mdlCustomerNPWP.npwp_name = valueNPWPName;
                        } else if (next_cell.getCellType() == CellType.NUMERIC) {
                            String valueNPWPName = NumberToTextConverter.toText(next_cell.getNumericCellValue());
                            _mdlCustomerNPWP.npwp_name = valueNPWPName;
                        }
                    } else if (column_index == 3) {
                        if (next_cell.getCellType() == CellType.STRING) {
                            String valueNPWPAddress = next_cell.getStringCellValue();
                            _mdlCustomerNPWP.npwp_address = valueNPWPAddress;
                        } else if (next_cell.getCellType() == CellType.NUMERIC) {
                            String valueNPWPAddress = NumberToTextConverter.toText(next_cell.getNumericCellValue());
                            _mdlCustomerNPWP.npwp_address = valueNPWPAddress;
                        }
                    } else if (column_index == 4) {
                        if (next_cell.getCellType() == CellType.STRING) {
                            String valueNPWPCity = next_cell.getStringCellValue();
                            _mdlCustomerNPWP.npwp_city = valueNPWPCity;
                        } else if (next_cell.getCellType() == CellType.NUMERIC) {
                            String valueNPWPCity = NumberToTextConverter.toText(next_cell.getNumericCellValue());
                            _mdlCustomerNPWP.npwp_city = valueNPWPCity;
                        }
                    }
                }
                _mdlCustomerNPWPList.add(_mdlCustomerNPWP);
                customerID.add(_mdlCustomerNPWP.customer_id);
                NPWPNo.add(_mdlCustomerNPWP.npwp_no);
            }
            workbook.close();

            for (int i = 0; i < customerID.size(); i++) {
                if (customerID.get(i) == null || customerID.get(i).isEmpty()) {
                    errorRow.add(i + 1);
                    errorIndicator++;
                }
                if (NPWPNo.get(i) == null || NPWPNo.get(i).isEmpty()) {
                    errorRow.add(i + 1);
                    errorIndicator++;
                }
            }

            if (errorIndicator > 0) {
                _mdlResponseQuery.Response = errorRow;
                _mdlResponseQuery.Status = false;

            } else {
                //INSERT INTO DATABASE
                for (mdlCustomerNPWP _mdlCustomer : _mdlCustomerNPWPList) {
                    _mdlResponseQuery = ImportCustomerNPWP(_mdlCustomer, user, db_name, port);
                }
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(param), "", ex.toString(), user, db_name), ex);
        }
        return _mdlResponseQuery;
    }

    public static mdlResponseQuery ImportCustomerNPWP(mdlCustomerNPWP param, String user, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        mdlResponseQuery _mdlResponseQuery = new mdlResponseQuery();
        String sql = "";
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_customer_npwp_import(?,?,?,?,?,?,?,?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.customer_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.npwp_no));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.npwp_name));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.npwp_address));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.npwp_city));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.npwp_zip_code));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.ktp_no));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", user));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", user));
            _mdlResponseQuery = QueryAdapter.QueryManipulateWithDB2_v3(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlResponseQuery;
    }
}
