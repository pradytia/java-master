package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import helper.DateHelper;
import model.CustomerInfo.mdlCustomerInfo;
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

public class CustomerInfoAdapter {
    final static Logger logger = LogManager.getLogger(CustomerInfoAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlCustomerInfo> GetCustomerInfoByCustomerID(String customerID, String user, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlCustomerInfo> listCustomerInfo = new ArrayList<mdlCustomerInfo>();
        List<mdlQueryExecute> listParam = new ArrayList<mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_customer_info_get (?)} ";
            listParam.add(database.QueryAdapter.QueryParam("string", customerID));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, user, db_name, port);
            while (rowset.next()) {
                mdlCustomerInfo _customerInfo = new mdlCustomerInfo();
                _customerInfo.customer_id = rowset.getString("customer_id");
                _customerInfo.id = rowset.getString("id");
                _customerInfo.desc = rowset.getString("desc_");
                _customerInfo.value = rowset.getString("value_");
                _customerInfo.type_value = rowset.getString("type_value");
                listCustomerInfo.add(_customerInfo);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), user, db_name), ex);
        }
        return listCustomerInfo;
    }

    public static List<mdlCustomerInfo> GetCustomerInfoAll(String user, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        List<mdlCustomerInfo> listCustomerInfo = new ArrayList<mdlCustomerInfo>();
        List<mdlQueryExecute> listParam = new ArrayList<mdlQueryExecute>();
        CachedRowSet rowset = null;
        try {
            sql = "{call sp_customer_info_get_all ()} ";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, user, db_name, port);
            while (rowset.next()) {
                mdlCustomerInfo _customerInfo = new mdlCustomerInfo();
                _customerInfo.customer_id = rowset.getString("customer_id");
                _customerInfo.id = rowset.getString("id");
                _customerInfo.desc = rowset.getString("desc_");
                _customerInfo.value = rowset.getString("value_");
                _customerInfo.type_value = rowset.getString("type_value");
                listCustomerInfo.add(_customerInfo);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), user, db_name), ex);
        }
        return listCustomerInfo;
    }

    public static List<mdlCustomerInfo> GetCustomerInfoWithPagingByCustomerID(mdlDataTableParam param, String customerID, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<mdlCustomerInfo> _mdlCustomerInfoList = new ArrayList<>();
        CachedRowSet rowset = null;
        String sql = "";
        String search_part = "";
        System.out.println((param.page_number - 1) * param.page_size);
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<>();
        try {
            sql = "{call sp_customer_info_get_with_paging(?,?,?,?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", customerID));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param.page_size));

            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "customer list", db_name, port);
            while (rowset.next()) {
                mdlCustomerInfo _customerInfo = new mdlCustomerInfo();
                _customerInfo.customer_id = rowset.getString("customer_id");
                _customerInfo.id = rowset.getString("id");
                _customerInfo.desc = rowset.getString("desc_");
                _customerInfo.value = rowset.getString("value_");
                _customerInfo.type_value = rowset.getString("type_value");
                _customerInfo.is_active = rowset.getString("is_active");
                _mdlCustomerInfoList.add(_customerInfo);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "customer list", db_name), ex);
        }
        return _mdlCustomerInfoList;
    }

    public static int GetCustomerInfoTotalListByCustomerID(mdlDataTableParam param, String customerID, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;
        String sql = "";
        int returnValue = 0;
        String search_part = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        try {
            sql = "{call sp_customer_info_get_total_list(?,?)}";
            String searchString = "%" + param.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", customerID));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));

            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "customer info total", db_name, port);
            while (crs.next()) {
                returnValue = crs.getInt("total");
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "customer info total", db_name), ex);
        }
        return returnValue;
    }

    public static boolean DeleteCustomerInfoByCustomerIDAndID(mdlCustomerInfo param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = "";
        String sql = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_customer_info_delete(?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.customer_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.id));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean InsertOrUpdateCustomerInfo(mdlCustomerInfo param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = "admin";
        String sql = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_customer_info_upload(?,?,?,?,?,?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.customer_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.desc));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.value));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.type_value));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", user));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", user));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean UpdateStatusCustomerInfo(mdlCustomerInfo param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_customer_info_update_status(?,?,?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.customer_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.is_active));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static String ExportCustomerInfo(mdlCustomerInfo param1, mdlExportParam param2, int port, String db_name) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String urlLink = "";
        String user = param1.created_by;
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
        List<mdlCustomerInfo> mdlCustomerInfoList = new ArrayList<mdlCustomerInfo>();
        FileOutputStream outPutStream = null;

        try {
            mdlCustomerInfoList = GetCustomerInfoByCustomerID(param1.customer_id, param1.created_by, db_name, port);
            //== Create Workbook==\\
            String[] columns = {"ID Pelanggan", "ID", "Description", "Value", "Type Value"};
            Sheet sheet = workbook.createSheet("Pelanggan Info");
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
            for (mdlCustomerInfo _mdlCustomerInfo : mdlCustomerInfoList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(_mdlCustomerInfo.customer_id);
                row.createCell(1).setCellValue(_mdlCustomerInfo.id);
                row.createCell(2).setCellValue(_mdlCustomerInfo.desc);
                row.createCell(3).setCellValue(_mdlCustomerInfo.value);
                row.createCell(4).setCellValue(_mdlCustomerInfo.type_value);
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

                String fileName = "Info_Pelanggan" + DateHelper.GetDateTimeNowCustomFormat("yyyyMMddHHmmss");
                String url = paramFileUrl + param2.app_name + "/CustomerInfo/" + fileName + ".xlsx";
                File createFile = new File(paramFilePath + param2.app_name + "/CustomerInfo");
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

    public static String ExportCustomerInfoAll(mdlCustomerInfo param1, mdlExportParam param2, int port, String db_name) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String urlLink = "";
        String user = param1.created_by;
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
        List<mdlCustomerInfo> mdlCustomerInfoList = new ArrayList<mdlCustomerInfo>();
        FileOutputStream outPutStream = null;

        try {
            mdlCustomerInfoList = GetCustomerInfoAll(param1.created_by, db_name, port);
            //== Create Workbook==\\
            String[] columns = {"ID Pelanggan", "ID", "Description", "Value", "Type Value"};
            Sheet sheet = workbook.createSheet("Pelanggan Info");
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
            for (mdlCustomerInfo _mdlCustomerInfo : mdlCustomerInfoList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(_mdlCustomerInfo.customer_id);
                row.createCell(1).setCellValue(_mdlCustomerInfo.id);
                row.createCell(2).setCellValue(_mdlCustomerInfo.desc);
                row.createCell(3).setCellValue(_mdlCustomerInfo.value);
                row.createCell(4).setCellValue(_mdlCustomerInfo.type_value);
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

                String fileName = "Info_Pelanggan" + DateHelper.GetDateTimeNowCustomFormat("yyyyMMddHHmmss");
                String url = paramFileUrl + param2.app_name + "/CustomerInfo/" + fileName + ".xlsx";
                File createFile = new File(paramFilePath + param2.app_name + "/CustomerInfo");
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

    public static mdlResponseQuery ImportExcelCustomer(Workbook workbook, mdlCustomerInfo param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String user = param.created_by;
        boolean resultExec = false;
        List<mdlCustomerInfo> _mdlCustomerInfoList = new ArrayList<mdlCustomerInfo>();
        mdlResponseQuery _mdlResponseQuery = new mdlResponseQuery();
        ArrayList<String> customerID = new ArrayList<>();
        ArrayList<String> id = new ArrayList<>();
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
                mdlCustomerInfo _mdlCustomerInfo = new mdlCustomerInfo();

                //looping ke samping column
                while (_cellIterator.hasNext()) {
                    Cell next_cell = _cellIterator.next();
                    int column_index = next_cell.getColumnIndex();

                    //proses get value from excel and set into model
                    if (column_index == 0) {
                        if (next_cell.getCellType() == CellType.STRING) {
                            _mdlCustomerInfo.customer_id = next_cell.getStringCellValue();
                        } else if (next_cell.getCellType() == CellType.NUMERIC) {
                            _mdlCustomerInfo.customer_id = NumberToTextConverter.toText(next_cell.getNumericCellValue());
                        }
                    } else if (column_index == 1) {
                        if (next_cell.getCellType() == CellType.STRING) {
                            _mdlCustomerInfo.id = next_cell.getStringCellValue();
                        } else if (next_cell.getCellType() == CellType.NUMERIC) {
                            _mdlCustomerInfo.id = NumberToTextConverter.toText(next_cell.getNumericCellValue());
                        }
                    } else if (column_index == 2) {
                        if (next_cell.getCellType() == CellType.STRING) {
                            _mdlCustomerInfo.desc = next_cell.getStringCellValue();
                        } else if (next_cell.getCellType() == CellType.NUMERIC) {
                            _mdlCustomerInfo.desc = NumberToTextConverter.toText(next_cell.getNumericCellValue());
                        }
                    } else if (column_index == 3) {
                        if (next_cell.getCellType() == CellType.STRING) {
                            _mdlCustomerInfo.value = next_cell.getStringCellValue();
                        } else if (next_cell.getCellType() == CellType.NUMERIC) {
                            _mdlCustomerInfo.value = NumberToTextConverter.toText(next_cell.getNumericCellValue());
                        }
                    } else if (column_index == 4) {
                        if (next_cell.getCellType() == CellType.STRING) {
                            _mdlCustomerInfo.type_value = next_cell.getStringCellValue();
                        } else if (next_cell.getCellType() == CellType.NUMERIC) {
                            _mdlCustomerInfo.type_value = NumberToTextConverter.toText(next_cell.getNumericCellValue());
                        }
                    }
                }
                _mdlCustomerInfoList.add(_mdlCustomerInfo);
                customerID.add(_mdlCustomerInfo.customer_id);
                id.add(_mdlCustomerInfo.id);
            }
            workbook.close();

            for (int i = 0; i < customerID.size(); i++) {
                if (customerID.get(i) == null || customerID.get(i).isEmpty()) {
                    errorRow.add(i + 1);
                    errorIndicator++;
                }
                if (id.get(i) == null || id.get(i).isEmpty()) {
                    errorRow.add(i + 1);
                    errorIndicator++;
                }
            }

            if (errorIndicator > 0) {
                _mdlResponseQuery.Response = errorRow;
                _mdlResponseQuery.Status = false;
            } else {
                //INSERT INTO DATABASE
                for (mdlCustomerInfo _mdlCustomer : _mdlCustomerInfoList) {
                    _mdlResponseQuery = ImportCustomerInfo(_mdlCustomer, user, db_name);
                }
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(param), "", ex.toString(), user, db_name), ex);
        }
        return _mdlResponseQuery;
    }


    public static mdlResponseQuery ImportCustomerInfo(mdlCustomerInfo param, String user, String db_name) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        mdlResponseQuery _mdlResponseQuery = new mdlResponseQuery();
        String sql = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_customer_info_import(?,?,?,?,?,?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.customer_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.desc));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.value));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.type_value));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", user));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", user));
            _mdlResponseQuery = QueryAdapter.QueryManipulateWithDB2(sql, _mdlQueryExecuteList, functionName, user, db_name);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlResponseQuery;
    }

}
