package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.Employee.mdlEmployeeType;
import model.Query.mdlQueryExecute;
import model.Query.mdlResponseQuery;
import model.Result.mdlResultFinal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ImportEmployeeTypeAdapter {
    final static Logger logger = LogManager.getLogger(ImportEmployeeTypeAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static mdlResponseQuery ImportExcelEmployeeType(Workbook workbook, mdlEmployeeType param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = param.created_by;
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        List<mdlEmployeeType> _mdlEmployeeTypeList = new ArrayList<mdlEmployeeType>();
        mdlResponseQuery _mdlResponseQuery = new mdlResponseQuery();
        ArrayList<String> employeeTypeID = new ArrayList<>();

        try {

            Sheet _firstSheet = workbook.getSheetAt(0);
            Iterator<Row> _rowIterator = _firstSheet.iterator();
            ArrayList<String> errorList = new ArrayList<String>();
            ArrayList<Integer> errorRow = new ArrayList<Integer>();
            int errorIndicator = 0;
            int columnIndicator = 1;

            //Skipping Excel header ==> looping ke bawah(row)
            while (_rowIterator.hasNext()) {
                Row currentRow = _rowIterator.next();
                int _rowIndex = currentRow.getRowNum();

                if (_rowIndex == 0) {
                    continue;
                }

                Iterator<Cell> _cellIterator = currentRow.cellIterator();
                mdlEmployeeType _mdlEmployeeType = new mdlEmployeeType();


                //looping ke samping antar column
                while (_cellIterator.hasNext()) {
                    Cell _nextCell = _cellIterator.next();
                    int _columnIndex = _nextCell.getColumnIndex();

                    //proses get value from excel and set into model
                    if (_columnIndex == 0) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueEmployeeID = _nextCell.getStringCellValue();
                            _mdlEmployeeType.setEmployee_type_id(valueEmployeeID);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueEmployeeID = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlEmployeeType.setEmployee_type_id(valueEmployeeID);
                        }

                    } else if (_columnIndex == 1) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueEmployeeName = _nextCell.getStringCellValue();
                            _mdlEmployeeType.setEmployee_type_name(valueEmployeeName);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueEmployeeName = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlEmployeeType.setEmployee_type_name(valueEmployeeName);
                        } else {
                            errorList.add(
                                    "Error in Row : " + currentRow.getRowNum() + "Column : " + columnIndicator + "Desc : ERROR INPUT TYPE"
                            );
                            errorIndicator++;
                        }


                    } else if (_columnIndex == 2) {
                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueDesc = _nextCell.getStringCellValue();
                            _mdlEmployeeType.setDescription(valueDesc);
                        } else {
                            errorList.add(
                                    "Error in Row : " + currentRow.getRowNum() + "Column : " + columnIndicator + "Desc : ERROR INPUT TYPE"
                            );
                            errorIndicator++;
                        }


                    }
                }
                _mdlEmployeeTypeList.add(_mdlEmployeeType);
                employeeTypeID.add(_mdlEmployeeType.employee_type_id);
            }
            workbook.close();

            for(int i = 0; i < employeeTypeID.size(); i++) {

                if (employeeTypeID.get(i) == null || employeeTypeID.get(i).isEmpty()) {
                    errorRow.add(i + 1);
                    errorIndicator ++;
                }

            }

            if(errorIndicator > 0){

                _mdlResponseQuery.Response = errorRow;
                _mdlResponseQuery.Status = false;

            } else {
                //INSERT INTO DATABASE

                for (mdlEmployeeType _mdlEmployeeType : _mdlEmployeeTypeList) {

                    List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

                    sql = "{call sp_employee_type_import(?,?,?,?,?)}";

                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlEmployeeType.employee_type_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlEmployeeType.employee_type_name));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlEmployeeType.description));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.module_id));

                    _mdlResponseQuery = QueryAdapter.QueryManipulateWithDB2_v3(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

                }
            }

        }catch(Exception e){
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", e.toString(), user, db_name), e);
        }

        return _mdlResponseQuery;
    }
}
