package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.Query.mdlQueryExecute;
import model.Query.mdlResponseQuery;
import model.Result.mdlResultFinal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import model.POSM.mdlPOSMProduct;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ImportPosmProductAdapter {
    final static Logger logger = LogManager.getLogger(ImportPosmProductAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static mdlResponseQuery ImportExcelPosmProduct(Workbook workbook, mdlPOSMProduct param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = param.created_by;
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        List<mdlPOSMProduct> _mdlPOSMProductList = new ArrayList<mdlPOSMProduct>();
        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlResponseQuery _mdlResponseQuery = new mdlResponseQuery();
        ArrayList<String> POSMProductID = new ArrayList<>();

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
                mdlPOSMProduct _mdlPOSMProduct = new mdlPOSMProduct();


                //looping ke samping antar column
                while (_cellIterator.hasNext()) {
                    Cell _nextCell = _cellIterator.next();
                    int _columnIndex = _nextCell.getColumnIndex();

                    //proses get value from excel and set into model
                    if (_columnIndex == 0) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valuePosmID = _nextCell.getStringCellValue();
                            _mdlPOSMProduct.setPosm_id(valuePosmID);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valuePosmID = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlPOSMProduct.setPosm_id(valuePosmID);
                        }

                    } else if (_columnIndex == 1) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valuePosmName = _nextCell.getStringCellValue();
                            _mdlPOSMProduct.setPosm_name(valuePosmName);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valuePosmName = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlPOSMProduct.setPosm_name(valuePosmName);
                        } else {
                            errorList.add(
                                    "Error in Row : " + currentRow.getRowNum() + "Column : " + columnIndicator + "Desc : ERROR INPUT TYPE"
                            );
                            errorIndicator++;
                        }

                    } else if (_columnIndex == 2) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueTotalStock = _nextCell.getStringCellValue();
                            _mdlPOSMProduct.setTotal_stock(valueTotalStock);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueTotalStock = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlPOSMProduct.setTotal_stock(valueTotalStock);
                        } else if (_nextCell.getCellType() == null || _nextCell.getCellType() == CellType.BLANK) {
                            String valueTotalStock = "0";
                            _mdlPOSMProduct.setTotal_stock(valueTotalStock);
                        }

                    }
                }
                _mdlPOSMProductList.add(_mdlPOSMProduct);
                POSMProductID.add(_mdlPOSMProduct.posm_id);
            }
            workbook.close();

            for (int i = 0; i < POSMProductID.size(); i++) {

                if (POSMProductID.get(i) == null || POSMProductID.get(i).isEmpty()) {
                    errorRow.add(i + 1);
                    errorIndicator++;
                }

            }

            if (errorIndicator > 0) {

                _mdlResponseQuery.Response = errorRow;
                _mdlResponseQuery.Status = false;

            } else {

                //INSERT INTO DATABASE

                for (mdlPOSMProduct _mdlPOSMProduct : _mdlPOSMProductList) {

                    List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

                    sql = "{call sp_posm_product_import(?,?,?,?)}";

                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlPOSMProduct.posm_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlPOSMProduct.posm_name));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlPOSMProduct.total_stock));
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
