package adapter;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.Answer.mdlAnswer;
import model.Callplan.mdlImportCallPlan;
import model.Customer.mdlCustomer;
import model.DataTable.mdlDataTableParam;
import model.Employee.mdlEmployee;
import model.Query.mdlQueryExecute;
import model.Query.mdlResponseQuery;
import model.Result.mdlResult;
import model.Callplan.mdlCallplan;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.CachedRowSet;

public class ImportCallPlanAdapter {
    final static Logger logger = LogManager.getLogger(ImportCallPlanAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static mdlResponseQuery ImportExcelCallPlan(MultipartFile file_excel, String extension, mdlCallplan param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = param.created_by;
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        List<mdlCallplan> _mdlCallplanList = new ArrayList<mdlCallplan>();
        mdlResponseQuery _mdlResponseQuery = new mdlResponseQuery();

        try {
            //Proses reading file excel
            InputStream input_stream = file_excel.getInputStream();
            Workbook workbook = null;

            //statement for extension Excel File
            if (extension.equals("xlsx")) {
                workbook = new XSSFWorkbook(input_stream);
            } else if (extension.equals("xls")) {
                workbook = new HSSFWorkbook(input_stream);
            } else {
                workbook = null;
            }

            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = firstSheet.iterator();

            //Skipping Excel header => looping ke bawah(row)
            while (rowIterator.hasNext()) {
                Row currentRow = rowIterator.next();
                int rowIndex = currentRow.getRowNum();

                if (rowIndex == 0) {
                    continue;
                }

                Iterator<Cell> cellIterator = currentRow.cellIterator();
                mdlCallplan _mdlCallplan = new mdlCallplan();

                //looping ke samping antar column
                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();
                    int columnIndex = nextCell.getColumnIndex();

                    //proses get value from excel and set into model
                    if (columnIndex == 0) {

                        if (nextCell.getCellType() == CellType.STRING) {
                            String valueCallPlanID = nextCell.getStringCellValue();
                            _mdlCallplan.setCall_plan_id(valueCallPlanID);
                        } else if (nextCell.getCellType() == CellType.NUMERIC) {
                            String valueCallPlanID = NumberToTextConverter.toText(nextCell.getNumericCellValue());
                            _mdlCallplan.setCall_plan_id(valueCallPlanID);
                        }

                    } else if (columnIndex == 1) {

                        if (nextCell.getCellType() == CellType.STRING) {
                            String valueEmployeeID = nextCell.getStringCellValue();
                            _mdlCallplan.setEmployee_id(valueEmployeeID);
                        } else if (nextCell.getCellType() == CellType.NUMERIC) {
                            String valueEmployeeID = NumberToTextConverter.toText(nextCell.getNumericCellValue());
                            _mdlCallplan.setEmployee_id(valueEmployeeID);
                        }

                    } else if (columnIndex == 2) {

                        if (nextCell.getCellType() == CellType.STRING) {
                            String valueBranchID = nextCell.getStringCellValue();
                            _mdlCallplan.setBranch_id(valueBranchID);
                        } else if (nextCell.getCellType() == CellType.NUMERIC) {
                            String valueBranchID = NumberToTextConverter.toText(nextCell.getNumericCellValue());
                            _mdlCallplan.setBranch_id(valueBranchID);
                        }

                    } else if (columnIndex == 3) {

                        if (nextCell.getCellType() == CellType.STRING) {
                            String valueVehicleID = nextCell.getStringCellValue();
                            _mdlCallplan.setVehicle_id(valueVehicleID);
                        } else if (nextCell.getCellType() == CellType.NUMERIC) {
                            String valueVehicleID = NumberToTextConverter.toText(nextCell.getNumericCellValue());
                            _mdlCallplan.setVehicle_id(valueVehicleID);
                        }

                    } else if (columnIndex == 4) {
                        Date valueEntryDate = nextCell.getDateCellValue();
                        //convert Date to String from excel
                        SimpleDateFormat simple_date_format = new SimpleDateFormat("yyyy-MM-dd");
                        String new_valueEntryDate = simple_date_format.format(valueEntryDate);
                        _mdlCallplan.setDate(new_valueEntryDate);
                    }
                }
                _mdlCallplanList.add(_mdlCallplan);
            }
            workbook.close();

            //INSERT INTO DATABASE
            for (mdlCallplan _mdlCallplan : _mdlCallplanList) {
                List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
                sql = "{call sp_call_plan_import(?,?,?,?,?,?)}";
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCallplan.call_plan_id));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCallplan.employee_id));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCallplan.branch_id));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCallplan.vehicle_id));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCallplan.date));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));
                _mdlResponseQuery = QueryAdapter.QueryManipulateWithDB2_v3(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            }
        } catch (Exception e) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", e.toString(), user, db_name), e);
        }

        return _mdlResponseQuery;
    }

    public static mdlResponseQuery ImportExcelCallPlanV2(MultipartFile file_excel, String extension, mdlCallplan param, String db_name) {
        long startTime = System.currentTimeMillis();
        String user = param.created_by;
        String sql = "";
        String sqlDetail = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        List<mdlImportCallPlan> _mdlCallplanList = new ArrayList<mdlImportCallPlan>();
        mdlResponseQuery _mdlResponseQuery = new mdlResponseQuery();
        mdlResponseQuery _mdlResponseQueryDetail = new mdlResponseQuery();

        try {
            //Proses reading file excel
            InputStream input_stream = file_excel.getInputStream();
            Workbook workbook = null;

            //statement for extension Excel File
            if (extension.equals("xlsx")) {
                workbook = new XSSFWorkbook(input_stream);
            } else if (extension.equals("xls")) {
                workbook = new HSSFWorkbook(input_stream);
            } else {
                workbook = null;
            }

            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = firstSheet.iterator();

            //Skipping Excel header => looping ke bawah(row)
            while (rowIterator.hasNext()) {
                Row currentRow = rowIterator.next();
                int rowIndex = currentRow.getRowNum();

                if (rowIndex == 0) {
                    continue;
                }

                Iterator<Cell> cellIterator = currentRow.cellIterator();
                mdlImportCallPlan _mdlCallplan = new mdlImportCallPlan();

                //looping ke samping antar column
                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();
                    int columnIndex = nextCell.getColumnIndex();

                    //proses get value from excel and set into model
                    if (columnIndex == 0) {

                        if (nextCell.getCellType() == CellType.STRING) {
                            String valueBranchID = nextCell.getStringCellValue();
                            _mdlCallplan.setBranch_id(valueBranchID);
                        } else if (nextCell.getCellType() == CellType.NUMERIC) {
                            String valueBranchID = NumberToTextConverter.toText(nextCell.getNumericCellValue());
                            _mdlCallplan.setBranch_id(valueBranchID);
                        }

                    } else if (columnIndex == 1) {

                        if (nextCell.getCellType() == CellType.STRING) {
                            String valueEmployeeID = nextCell.getStringCellValue();
                            _mdlCallplan.setEmployee_id(valueEmployeeID);
                        } else if (nextCell.getCellType() == CellType.NUMERIC) {
                            String valueEmployeeID = NumberToTextConverter.toText(nextCell.getNumericCellValue());
                            _mdlCallplan.setEmployee_id(valueEmployeeID);
                        }

                    } else if (columnIndex == 2) {

                        Date valueEntryDate = nextCell.getDateCellValue();
                        //convert Date to String from excel
                        SimpleDateFormat simple_date_format = new SimpleDateFormat("yyyy-MM-dd");
                        String new_valueEntryDate = simple_date_format.format(valueEntryDate);
                        _mdlCallplan.setDate(new_valueEntryDate);

                    } else if (columnIndex == 3) {

                        if (nextCell.getCellType() == CellType.STRING) {
                            String valueCustomerID = nextCell.getStringCellValue();
                            _mdlCallplan.setCustomer_id(valueCustomerID);
                        } else if (nextCell.getCellType() == CellType.NUMERIC) {
                            String valueCustomerID = NumberToTextConverter.toText(nextCell.getNumericCellValue());
                            _mdlCallplan.setCustomer_id(valueCustomerID);
                        }

                    } else if (columnIndex == 4) {
                        Date valueEntryDate = nextCell.getDateCellValue();
                        //convert Date to String from excel
                        SimpleDateFormat simple_date_format = new SimpleDateFormat("hh:mm:ss");
                        String new_valueEntryDate = simple_date_format.format(valueEntryDate);
                        _mdlCallplan.setTime(new_valueEntryDate);

                    }

                }
                _mdlCallplanList.add(_mdlCallplan);
            }
            workbook.close();

            //INSERT INTO DATABASE

            for (mdlImportCallPlan _mdlCallplan : _mdlCallplanList) {

                String call_plan_id = "";
                String prev_call_plan_id = "";
                String dateID = _mdlCallplan.date.replace("-","");
                int sequence = 0;

                call_plan_id = "C-" + _mdlCallplan.employee_id + "-" + dateID;

                if(!prev_call_plan_id.equals(call_plan_id)){

                    List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

                    sql = "{call sp_call_plan_import_v2(?,?,?,?,?)}";

                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", call_plan_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCallplan.branch_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCallplan.employee_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCallplan.date));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));

                    _mdlResponseQuery = QueryAdapter.QueryManipulateWithDB2(sql, _mdlQueryExecuteList, functionName, user, db_name);

                    prev_call_plan_id = call_plan_id;
                }

                List<mdlQueryExecute> _mdlQueryExecuteDetailList = new ArrayList<mdlQueryExecute>();
                sqlDetail = "{call sp_call_plan_detail_import_v2(?,?,?,?)}";
                _mdlQueryExecuteDetailList.add(QueryAdapter.QueryParam("string", call_plan_id));
                _mdlQueryExecuteDetailList.add(QueryAdapter.QueryParam("string", _mdlCallplan.customer_id));
                _mdlQueryExecuteDetailList.add(QueryAdapter.QueryParam("string", _mdlCallplan.time));
                _mdlQueryExecuteDetailList.add(QueryAdapter.QueryParam("int", sequence));
                _mdlResponseQueryDetail = QueryAdapter.QueryManipulateWithDB2(sqlDetail, _mdlQueryExecuteDetailList, functionName, user, db_name);
            }
        } catch (Exception e) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", e.toString(), user, db_name), e);
        }
        return _mdlResponseQuery;
    }

    public static mdlResponseQuery ImportExcelCallPlanV3(MultipartFile file_excel, String extension, mdlCallplan param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = param.created_by;
        String sql = "";
        String sqlDetail = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        List<mdlImportCallPlan> _mdlCallplanList = new ArrayList<mdlImportCallPlan>();
        mdlResponseQuery _mdlResponseQuery = new mdlResponseQuery();
        mdlResponseQuery _mdlResponseQueryDetail = new mdlResponseQuery();

        try {
            //Proses reading file excel
            InputStream input_stream = file_excel.getInputStream();
            Workbook workbook = null;

            //statement for extension Excel File
            if (extension.equals("xlsx")) {
                workbook = new XSSFWorkbook(input_stream);
            } else if (extension.equals("xls")) {
                workbook = new HSSFWorkbook(input_stream);
            } else {
                workbook = null;
            }

            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = firstSheet.iterator();

            //Skipping Excel header => looping ke bawah(row)
            while (rowIterator.hasNext()) {
                Row currentRow = rowIterator.next();
                int rowIndex = currentRow.getRowNum();

                if (rowIndex == 0) {
                    continue;
                }

                Iterator<Cell> cellIterator = currentRow.cellIterator();
                mdlImportCallPlan _mdlCallplan = new mdlImportCallPlan();

                //looping ke samping antar column
                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();
                    int columnIndex = nextCell.getColumnIndex();

                    //proses get value from excel and set into model
                    if (columnIndex == 0) {

                        if (nextCell.getCellType() == CellType.STRING) {
                            String valueBranchID = nextCell.getStringCellValue();
                            _mdlCallplan.setBranch_id(valueBranchID);
                        } else if (nextCell.getCellType() == CellType.NUMERIC) {
                            String valueBranchID = NumberToTextConverter.toText(nextCell.getNumericCellValue());
                            _mdlCallplan.setBranch_id(valueBranchID);
                        }

                    } else if (columnIndex == 1) {

                        if (nextCell.getCellType() == CellType.STRING) {
                            String valueEmployeeID = nextCell.getStringCellValue();
                            _mdlCallplan.setEmployee_id(valueEmployeeID);
                        } else if (nextCell.getCellType() == CellType.NUMERIC) {
                            String valueEmployeeID = NumberToTextConverter.toText(nextCell.getNumericCellValue());
                            _mdlCallplan.setEmployee_id(valueEmployeeID);
                        }

                    } else if (columnIndex == 2) {

                        Date valueEntryDate = nextCell.getDateCellValue();
                        //convert Date to String from excel
                        SimpleDateFormat simple_date_format = new SimpleDateFormat("yyyy-MM-dd");
                        String new_valueEntryDate = simple_date_format.format(valueEntryDate);
                        _mdlCallplan.setDate(new_valueEntryDate);

                    } else if (columnIndex == 3) {

                        if (nextCell.getCellType() == CellType.STRING) {
                            String valueCustomerID = nextCell.getStringCellValue();
                            _mdlCallplan.setCustomer_id(valueCustomerID);
                        } else if (nextCell.getCellType() == CellType.NUMERIC) {
                            String valueCustomerID = NumberToTextConverter.toText(nextCell.getNumericCellValue());
                            _mdlCallplan.setCustomer_id(valueCustomerID);
                        }

                    } else if (columnIndex == 4) {
                        Date valueEntryDate = nextCell.getDateCellValue();
                        //convert Date to String from excel
                        SimpleDateFormat simple_date_format = new SimpleDateFormat("hh:mm:ss");
                        String new_valueEntryDate = simple_date_format.format(valueEntryDate);
                        _mdlCallplan.setTime(new_valueEntryDate);

                    }

                }
                _mdlCallplanList.add(_mdlCallplan);
            }
            workbook.close();

            //INSERT INTO DATABASE

            for (mdlImportCallPlan _mdlCallplan : _mdlCallplanList) {

                String call_plan_id = "";
                String prev_call_plan_id = "";
                String dateID = _mdlCallplan.date.replace("-","");
                int sequence = 0;

                call_plan_id = "C-" + _mdlCallplan.employee_id + "-" + dateID;

                mdlCustomer _mdlCustomer = new mdlCustomer();
                _mdlCustomer.customer_id = _mdlCallplan.customer_id;
                List<mdlCustomer> _mdlCustomerList = GetCustomerCheckIsActiveImport(_mdlCustomer, db_name);

                mdlEmployee _mdlEmployee = new mdlEmployee();
                _mdlEmployee.employee_id = _mdlCallplan.employee_id;
                List<mdlEmployee> _mdlEmployeeList = GetEmployeeCheckIsActiveImport(_mdlEmployee, db_name);

                String employee_is_active = _mdlEmployeeList.get(0).is_active;
                String customer_is_active = _mdlCustomerList.get(0).is_active;

                //dari kondisi is_active false
                if(employee_is_active.equals("true")){
                    if (customer_is_active.equals("true")){
                        if(!prev_call_plan_id.equals(call_plan_id)){

                            List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

                            sql = "{call sp_call_plan_import_v2(?,?,?,?,?)}";

                            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", call_plan_id));
                            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCallplan.branch_id));
                            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCallplan.employee_id));
                            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCallplan.date));
                            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));

                            _mdlResponseQuery = QueryAdapter.QueryManipulateWithDB2_v3(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

                            prev_call_plan_id = call_plan_id;
                        }
                        List<mdlQueryExecute> _mdlQueryExecuteDetailList = new ArrayList<mdlQueryExecute>();
                        sqlDetail = "{call sp_call_plan_detail_import_v2(?,?,?,?)}";
                        _mdlQueryExecuteDetailList.add(QueryAdapter.QueryParam("string", call_plan_id));
                        _mdlQueryExecuteDetailList.add(QueryAdapter.QueryParam("string", _mdlCallplan.customer_id));
                        _mdlQueryExecuteDetailList.add(QueryAdapter.QueryParam("string", _mdlCallplan.time));
                        _mdlQueryExecuteDetailList.add(QueryAdapter.QueryParam("int", sequence));
                        _mdlResponseQueryDetail = QueryAdapter.QueryManipulateWithDB2_v3(sqlDetail, _mdlQueryExecuteDetailList, functionName, user, db_name, port);
                    }else{
                        _mdlResponseQuery.Status = false;
                    }
                }else{
                    _mdlResponseQuery.Status = false;
                }
            }
        } catch (Exception e) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", e.toString(), user, db_name), e);
        }
        return _mdlResponseQuery;
    }

    public static List<mdlCustomer> GetCustomerCheckIsActiveImport(mdlCustomer param, String db_name) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param.customer_id;

        List<mdlCustomer> _mdlCustomerList = new ArrayList<mdlCustomer>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;

        try {
            sql = "{call sp_customer_get(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.customer_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name);

            while (rowset.next()) {
                model.Customer.mdlCustomer _mdlCustomer = new model.Customer.mdlCustomer();
                _mdlCustomer.setCustomer_id(rowset.getString("customer_id"));
                _mdlCustomer.setCustomer_name(rowset.getString("customer_name"));
                _mdlCustomer.setIs_active(rowset.getString("is_active"));
                _mdlCustomerList.add(_mdlCustomer);
            }
        } catch (Exception ex) {
            _mdlCustomerList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlCustomerList;
    }

    public static List<mdlEmployee> GetEmployeeCheckIsActiveImport(mdlEmployee param, String db_name) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param.employee_id;

        List<mdlEmployee> _mdlEmployeeList = new ArrayList<mdlEmployee>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;
        boolean resultExec = false;

        try {

            sql = "{call sp_employee_get_id(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.employee_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name);
            while (rowset.next()) {
                model.Employee.mdlEmployee _mdlEmployee = new model.Employee.mdlEmployee();
                _mdlEmployee.employee_id = rowset.getString("employee_id");
                _mdlEmployee.employee_name = rowset.getString("employee_name");
                _mdlEmployee.is_active = rowset.getString("is_active");
                _mdlEmployeeList.add(_mdlEmployee);
            }
        } catch (Exception ex) {
            _mdlEmployeeList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", ex.toString(), user, db_name), ex);
        }
        return _mdlEmployeeList;
    }

}
