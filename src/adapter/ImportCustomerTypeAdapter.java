package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.Customer.mdlCustomerType;
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

public class ImportCustomerTypeAdapter {
    final static Logger logger = LogManager.getLogger(ImportCustomerTypeAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static mdlResponseQuery ImportExcelCustomerType(Workbook workbook, mdlCustomerType param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = param.created_by;
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        List<mdlCustomerType> _mdlCustomerTypeList = new ArrayList<mdlCustomerType>();
        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlResponseQuery _mdlResponseQuery = new mdlResponseQuery();
        ArrayList<String> customerTypeID = new ArrayList<>();

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
                mdlCustomerType _mdlCustomerType = new mdlCustomerType();


                //looping ke samping antar column
                while (_cellIterator.hasNext()) {
                    Cell _nextCell = _cellIterator.next();
                    int _columnIndex = _nextCell.getColumnIndex();

                    //proses get value from excel and set into model
                    if (_columnIndex == 0) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueCustID = _nextCell.getStringCellValue();
                            _mdlCustomerType.setCustomer_type_id(valueCustID);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueCustID = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlCustomerType.setCustomer_type_id(valueCustID);
                        }

                    } else if (_columnIndex == 1) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueCustName = _nextCell.getStringCellValue();
                            _mdlCustomerType.setCustomer_type_name(valueCustName);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueCustName = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlCustomerType.setCustomer_type_name(valueCustName);
                        } else {
                            errorList.add(
                                    "Error in Row : " + currentRow.getRowNum() + "Column : " + columnIndicator + "Desc : ERROR INPUT TYPE"
                            );
                            errorIndicator++;
                        }


                    } else if (_columnIndex == 2) {
                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueDesc = _nextCell.getStringCellValue();
                            _mdlCustomerType.setDescription(valueDesc);
                        } else {
                            errorList.add(
                                    "Error in Row : " + currentRow.getRowNum() + "Column : " + columnIndicator + "Desc : ERROR INPUT TYPE"
                            );
                            errorIndicator++;
                        }


                    }
                }
                _mdlCustomerTypeList.add(_mdlCustomerType);
                customerTypeID.add(_mdlCustomerType.customer_type_id);
            }
            workbook.close();

            for (int i = 0; i < customerTypeID.size(); i++) {

                if (customerTypeID.get(i) == null || customerTypeID.get(i).isEmpty()) {
                    errorRow.add(i + 1);
                    errorIndicator++;
                }

            }

            if (errorIndicator > 0) {

                _mdlResponseQuery.Response = errorRow;
                _mdlResponseQuery.Status = false;

            } else {

                //INSERT INTO DATABASE

                for (mdlCustomerType _mdlCustomerType : _mdlCustomerTypeList) {

                    List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

                    sql = "{call sp_customer_type_import(?,?,?,?)}";

                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCustomerType.customer_type_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCustomerType.customer_type_name));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCustomerType.description));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));

                    _mdlResponseQuery = QueryAdapter.QueryManipulateWithDB2_v3(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

                }
            }

        } catch (Exception e) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", e.toString(), user, db_name), e);
        }
        return _mdlResponseQuery;
    }
}
