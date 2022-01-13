package adapter;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.Product.mdlProductType;
import model.Query.mdlQueryExecute;
import model.Query.mdlResponseQuery;
import model.Result.mdlResultFinal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;


public class ImportProductTypeAdapter {
    final static Logger logger = LogManager.getLogger(ImportProductTypeAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static mdlResponseQuery ImportExcelProductType(Workbook workbook, mdlProductType param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = param.created_by;
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        List<mdlProductType> _mdlProductTypeList = new ArrayList<mdlProductType>();
        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlResponseQuery _mdlResponseQuery = new mdlResponseQuery();
        ArrayList<String> productTypeID = new ArrayList<>();

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
                mdlProductType _mdlProductType = new mdlProductType();


                //looping ke samping antar column
                while (_cellIterator.hasNext()) {
                    Cell _nextCell = _cellIterator.next();
                    int _columnIndex = _nextCell.getColumnIndex();

                    //proses get value from excel and set into model
                    if (_columnIndex == 0) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valuePTID = _nextCell.getStringCellValue();
                            _mdlProductType.setProduct_type_id(valuePTID);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valuePTID = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlProductType.setProduct_type_id(valuePTID);
                        }

                    } else if (_columnIndex == 1) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valuePTName = _nextCell.getStringCellValue();
                            _mdlProductType.setProduct_type_name(valuePTName);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valuePTName = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlProductType.setProduct_type_name(valuePTName);
                        } else {
                            errorList.add(
                                    "Error in Row : " + currentRow.getRowNum() + "Column : " + columnIndicator + "Desc : ERROR INPUT TYPE"
                            );
                            errorIndicator++;
                        }


                    }
                }
                _mdlProductTypeList.add(_mdlProductType);
                productTypeID.add(_mdlProductType.product_type_id);
            }
            workbook.close();

            for(int i = 0; i < productTypeID.size(); i++) {

                if (productTypeID.get(i) == null || productTypeID.get(i).isEmpty()) {
                    errorRow.add(i + 1);
                    errorIndicator ++;
                }

            }

            if(errorIndicator > 0){

                _mdlResponseQuery.Response = errorRow;
                _mdlResponseQuery.Status = false;

            } else {
                //INSERT INTO DATABASE

                for (mdlProductType _mdlProductType : _mdlProductTypeList) {

                    List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

                    sql = "{call sp_product_type_import(?,?,?)}";

                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlProductType.product_type_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlProductType.product_type_name));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));

                    _mdlResponseQuery = QueryAdapter.QueryManipulateWithDB2_v3(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

                }
            }

        }catch(Exception e){
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", e.toString(), user, db_name), e);
        }

        return _mdlResponseQuery;
    }
}
