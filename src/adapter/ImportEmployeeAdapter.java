package adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.Employee.mdlEmployee;
import model.Query.mdlQueryExecute;
import model.Query.mdlResponseQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import utils.StringUtil;


public class ImportEmployeeAdapter {
    final static Logger logger = LogManager.getLogger(ImportEmployeeAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static mdlResponseQuery ImportExcelIntoDB (Workbook workbook, mdlEmployee param, String db_name, int port){
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String user = param.created_by;
        boolean resultExec = false;
        List<mdlEmployee> _mdlEmployeeList = new ArrayList<mdlEmployee>();
        mdlResponseQuery _mdlResponseQuery = new mdlResponseQuery();
        ArrayList<String> employeeID = new ArrayList<>();

        try{
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = firstSheet.iterator();
            ArrayList<String> errorList = new ArrayList<String>();
            ArrayList<Integer> errorRow = new ArrayList<Integer>();
            int errorIndicator = 0;
            int columnIndicator = 1;

            //Skipping Excel header => looping ke bawah(row)
            while (rowIterator.hasNext()){
                Row currentRow = rowIterator.next();
                int rowIndex = currentRow.getRowNum();

                if (rowIndex == 0) {
                    continue;
                }

                Iterator<Cell> _cellIterator = currentRow.cellIterator();
                mdlEmployee _mdlEmployee = new mdlEmployee();

                //looping ke samping(column)
                while(_cellIterator.hasNext()){
                    Cell _nextCell = _cellIterator.next();
                    int columnIndex = _nextCell.getColumnIndex();

                    //proses get value from excel and set into model

                    if (columnIndex == 0) {
                        //STATEMENT
                        if(_nextCell.getCellType() == CellType.STRING){
                            String valueEmployeeID = _nextCell.getStringCellValue();
                            _mdlEmployee.setEmployee_id(valueEmployeeID);
                        }else if(_nextCell.getCellType() == CellType.NUMERIC){
                            String valueEmployeeID = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlEmployee.setEmployee_id(valueEmployeeID);
                        }

                    }else if (columnIndex == 1) {
                        String valueEmployeeName = _nextCell.getStringCellValue();
                        _mdlEmployee.setEmployee_name(valueEmployeeName);

                    } else if (columnIndex == 2) {

                        if(_nextCell.getCellType() == CellType.STRING){
                            String valueEmployeeTypeID = _nextCell.getStringCellValue();
                            _mdlEmployee.setEmployee_type_id(valueEmployeeTypeID);
                        }else if(_nextCell.getCellType() == CellType.NUMERIC){
                            String valueEmployeeTypeID = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlEmployee.setEmployee_type_id(valueEmployeeTypeID);
                        }

                    } else if (columnIndex == 3) {

                        if(_nextCell.getCellType() == CellType.STRING){
                            String valueBranchID = _nextCell.getStringCellValue();
                            _mdlEmployee.setBranch_id(valueBranchID);
                        }else if (_nextCell.getCellType() == CellType.NUMERIC){
                            String valueBranchID = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlEmployee.setBranch_id(valueBranchID);
                        }

                    } else if (columnIndex == 4) {

                        if(_nextCell.getCellType() == CellType.STRING){

                            String entryDate = _nextCell.getStringCellValue();
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = (Date)formatter.parse(entryDate);
                            SimpleDateFormat _simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            String newValueEntryDate = _simpleDateFormat.format(date);
                            _mdlEmployee.setEntry_date(newValueEntryDate);

                        }else if (_nextCell.getCellType() == CellType.NUMERIC){
                            String entryDate = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            Date javaDate= DateUtil.getJavaDate(Double.parseDouble(entryDate));
                            SimpleDateFormat _simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            String newValueEntryDate = _simpleDateFormat.format(javaDate);
                            _mdlEmployee.setEntry_date(newValueEntryDate);
                        }

                    }else if (columnIndex == 5) {

                        if(_nextCell.getCellType() == CellType.STRING){
                            String valueGender = _nextCell.getStringCellValue();
                            _mdlEmployee.setGender(valueGender);
                        }else if (_nextCell.getCellType() == CellType.NUMERIC){
                            String valueGender = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlEmployee.setGender(valueGender);
                        }

                    }else if (columnIndex == 6) {

                        if(_nextCell.getCellType() == CellType.STRING){
                            String valueEmail = _nextCell.getStringCellValue();
                            _mdlEmployee.setEmail(valueEmail);
                        }else if (_nextCell.getCellType() == CellType.NUMERIC){
                            String valueEmail = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlEmployee.setEmail(valueEmail);
                        }

                    }else if (columnIndex == 7) {

                        if(_nextCell.getCellType() == CellType.STRING){
                            String valuePhone = _nextCell.getStringCellValue();
                            _mdlEmployee.setPhone(valuePhone);
                        }else if (_nextCell.getCellType() == CellType.NUMERIC){
                            String valuePhone = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlEmployee.setPhone(valuePhone);
                        }

                    } else if (columnIndex == 8) {

                        if(_nextCell.getCellType() == CellType.STRING){

                            String ValueOutDate = _nextCell.getStringCellValue();
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = (Date)formatter.parse(ValueOutDate);
                            SimpleDateFormat _simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            String newValueOutDate = _simpleDateFormat.format(date);
                            _mdlEmployee.setOut_date(newValueOutDate);

                        }else if (_nextCell.getCellType() == CellType.NUMERIC){
                            String ValueOutDate = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            Date javaDate= DateUtil.getJavaDate(Double.parseDouble(ValueOutDate));
                            SimpleDateFormat _simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            String newValueOutDate = _simpleDateFormat.format(javaDate);
                            _mdlEmployee.setOut_date(newValueOutDate);
                        }

                    }
                }
                _mdlEmployeeList.add(_mdlEmployee);
                employeeID.add(_mdlEmployee.employee_id);
            }
            workbook.close();

            for(int i = 0; i < employeeID.size(); i++) {

                if (employeeID.get(i) == null || employeeID.get(i).isEmpty()) {
                    errorRow.add(i + 1);
                    errorIndicator ++;
                }

            }

            if(errorIndicator > 0){

                _mdlResponseQuery.Response = errorRow;
                _mdlResponseQuery.Status = false;

            } else {
                //INSERT INTO DATABASE

                for (mdlEmployee _mdlEmployee : _mdlEmployeeList) {

                    List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

                    sql = "{call sp_employee_import(?,?,?,?,?,?,?,?,?,?)}";

                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", StringUtil.removeSpecialCharAndWhiteSpace(_mdlEmployee.employee_id)));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlEmployee.employee_name));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlEmployee.employee_type_id));
//                   _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlEmployee.branch_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", ""));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlEmployee.entry_date));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlEmployee.gender));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlEmployee.email));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlEmployee.phone));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlEmployee.out_date));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));

                    _mdlResponseQuery = QueryAdapter.QueryManipulateWithDB2_v3(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

                    if (_mdlResponseQuery.Status) {
                        List<String> listBranch = Stream.of(_mdlEmployee.branch_id.split("\\s*,\\s*", -1))
                                .collect(Collectors.toList());

                        for (String branchID: listBranch) {
                            _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

                            sql = "{call sp_employee_vs_branch_import(?,?,?)}";

                            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string",  StringUtil.removeSpecialCharAndWhiteSpace(_mdlEmployee.employee_id)));
                            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", branchID));
                            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));

                            _mdlResponseQuery = QueryAdapter.QueryManipulateWithDB2_v3(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
                        }
                    }

                }
            }

        }catch (Exception ex){
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(param), "", ex.toString(), user, db_name), ex);
        }
        return _mdlResponseQuery;
    }
}
