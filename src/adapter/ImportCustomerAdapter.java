package adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.Query.mdlQueryExecute;
import model.Query.mdlResponseQuery;
import model.Customer.mdlCustomer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import utils.StringUtil;

public class ImportCustomerAdapter {
    final static Logger logger = LogManager.getLogger(ImportCustomerAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static mdlResponseQuery ImportExcelCustomer(Workbook workbook, mdlCustomer param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String user = param.created_by;
        boolean resultExec = false;
        List<mdlCustomer> _mdlCustomerList = new ArrayList<mdlCustomer>();
        mdlResponseQuery _mdlResponseQuery = new mdlResponseQuery();
        ArrayList<String> customerID = new ArrayList<>();

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
                mdlCustomer _mdlCustomer = new mdlCustomer();

                //looping ke samping column
                while (_cellIterator.hasNext()) {
                    Cell next_cell = _cellIterator.next();
                    int column_index = next_cell.getColumnIndex();

                    //proses get value from excel and set into model

                    if (column_index == 0) {

                        if (next_cell.getCellType() == CellType.STRING) {
                            String valueCustomerID = next_cell.getStringCellValue();
                            _mdlCustomer.setCustomer_id(valueCustomerID);
                        } else if (next_cell.getCellType() == CellType.NUMERIC) {
                            String valueCustomerID = NumberToTextConverter.toText(next_cell.getNumericCellValue());
                            _mdlCustomer.setCustomer_id(valueCustomerID);
                        }

                    } else if (column_index == 1) {

                        if (next_cell.getCellType() == CellType.STRING) {
                            String valueBranchID = next_cell.getStringCellValue();
                            _mdlCustomer.setBranch_id(valueBranchID);
                        } else if (next_cell.getCellType() == CellType.NUMERIC) {
                            String valueBranchID = NumberToTextConverter.toText(next_cell.getNumericCellValue());
                            _mdlCustomer.setBranch_id(valueBranchID);
                        }

                    } else if (column_index == 2) {

                        if (next_cell.getCellType() == CellType.STRING) {
                            String valueCustTypeID = next_cell.getStringCellValue();
                            _mdlCustomer.setCustomer_type_id(valueCustTypeID);
                        } else if (next_cell.getCellType() == CellType.NUMERIC) {
                            String valueCustTypeID = NumberToTextConverter.toText(next_cell.getNumericCellValue());
                            _mdlCustomer.setCustomer_type_id(valueCustTypeID);
                        }

                    } else if (column_index == 3) {
                        String valueCustomerName = next_cell.getStringCellValue();
                        _mdlCustomer.setCustomer_name(valueCustomerName);
                    } else if (column_index == 4) {
                        String valueCustomerAddress = next_cell.getStringCellValue();
                        _mdlCustomer.setCustomer_address(valueCustomerAddress);
                    } else if (column_index == 5) {

                        if (next_cell.getCellType() == CellType.STRING) {
                            String valuePhone = next_cell.getStringCellValue();
                            _mdlCustomer.setPhone(valuePhone);
                        } else if (next_cell.getCellType() == CellType.NUMERIC) {
                            String valuePhone = NumberToTextConverter.toText(next_cell.getNumericCellValue());
                            _mdlCustomer.setPhone(valuePhone);
                        }

                    } else if (column_index == 6) {
                        String valueEmail = next_cell.getStringCellValue();
                        _mdlCustomer.setEmail(valueEmail);
                    } else if (column_index == 7) {

                        if (next_cell.getCellType() == CellType.STRING) {
                            String valuePIC = next_cell.getStringCellValue();
                            _mdlCustomer.setPic(valuePIC);
                        } else if (next_cell.getCellType() == CellType.NUMERIC) {
                            String valuePIC = NumberToTextConverter.toText(next_cell.getNumericCellValue());
                            _mdlCustomer.setPic(valuePIC);
                        }


                    } else if (column_index == 8) {

                        if (next_cell.getCellType() == CellType.STRING) {
                            String valueAccount = next_cell.getStringCellValue();
                            _mdlCustomer.setAccount(valueAccount);
                        } else if (next_cell.getCellType() == CellType.NUMERIC) {
                            String valueAccount = NumberToTextConverter.toText(next_cell.getNumericCellValue());
                            _mdlCustomer.setAccount(valueAccount);
                        }

                    } else if (column_index == 9) {
                        String valueGender = next_cell.getStringCellValue();
                        _mdlCustomer.setGender(valueGender);
                    }
                }
                if(_mdlCustomer.customer_id != null){
                    _mdlCustomerList.add(_mdlCustomer);
                    customerID.add(_mdlCustomer.customer_id);
                }
            }

            workbook.close();

            for (int i = 0; i < customerID.size(); i++) {

                if (customerID.get(i) == null || customerID.get(i).isEmpty()) {
                    errorRow.add(i + 1);
                    errorIndicator++;
                }

            }

            if (errorIndicator > 0) {

                _mdlResponseQuery.Response = errorRow;
                _mdlResponseQuery.Status = false;

            } else {
                //INSERT INTO DATABASE

                for (mdlCustomer _mdlCustomer : _mdlCustomerList) {

                    List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

                    sql = "{call sp_customer_import(?,?,?,?,?,?,?,?,?,?,?,?)}";

                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", StringUtil.removeSpecialCharAndWhiteSpace(_mdlCustomer.customer_id)));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCustomer.branch_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.module_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCustomer.customer_type_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCustomer.customer_name));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCustomer.customer_address));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCustomer.phone));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCustomer.email));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCustomer.pic));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCustomer.account));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCustomer.gender));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));

                    _mdlResponseQuery = QueryAdapter.QueryManipulateWithDB2_v3(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

                }
            }

        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", ex.toString(), user, db_name), ex);
        }
        return _mdlResponseQuery;
    }
}