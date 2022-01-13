package adapter;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.Product.mdlProductGroup;
import model.Query.mdlQueryExecute;
import model.Query.mdlResponseQuery;
import model.Result.mdlResultFinal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;


public class ImportProductGroupAdapter {
    final static Logger logger = LogManager.getLogger(ImportProductGroupAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static mdlResponseQuery ImportExcelProductGroup(Workbook workbook, mdlProductGroup param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = param.created_by;
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        List<mdlProductGroup> _mdlProductGroupList = new ArrayList<mdlProductGroup>();
        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlResponseQuery _mdlResponseQuery = new mdlResponseQuery();
        ArrayList<String> productGroupID = new ArrayList<>();

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
                mdlProductGroup _mdlProductGroup = new mdlProductGroup();


                //looping ke samping antar column
                while (_cellIterator.hasNext()) {
                    Cell _nextCell = _cellIterator.next();
                    int _columnIndex = _nextCell.getColumnIndex();

                    //proses get value from excel and set into model
                    if (_columnIndex == 0) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valuePGID = _nextCell.getStringCellValue();
                            _mdlProductGroup.setProduct_group_id(valuePGID);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valuePGID = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlProductGroup.setProduct_group_id(valuePGID);
                        }

                    } else if (_columnIndex == 1) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valuePGName = _nextCell.getStringCellValue();
                            _mdlProductGroup.setProduct_group_name(valuePGName);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valuePGName = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlProductGroup.setProduct_group_name(valuePGName);
                        } else {
                            errorList.add(
                                    "Error in Row : " + currentRow.getRowNum() + "Column : " + columnIndicator + "Desc : ERROR INPUT TYPE"
                            );
                            errorIndicator++;
                        }


                    }
                }
                _mdlProductGroupList.add(_mdlProductGroup);
                productGroupID.add(_mdlProductGroup.product_group_id);
            }
            workbook.close();


            for(int i = 0; i < productGroupID.size(); i++) {

                if (productGroupID.get(i) == null || productGroupID.get(i).isEmpty()) {
                    errorRow.add(i + 1);
                    errorIndicator ++;
                }

            }

            if(errorIndicator > 0){

                _mdlResponseQuery.Response = errorRow;
                _mdlResponseQuery.Status = false;

            } else {
                //INSERT INTO DATABASE

                for (mdlProductGroup _mdlProductGroup : _mdlProductGroupList) {

                    List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

                    sql = "{call sp_product_group_import(?,?,?)}";

                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlProductGroup.product_group_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlProductGroup.product_group_name));
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
