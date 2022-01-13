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
import model.Result.mdlResultFinal;
import model.Uom.mdlUom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;

public class ImportUomAdapter {
    final static Logger logger = LogManager.getLogger(ImportUomAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();


    public static mdlResponseQuery ImportExcelUom(Workbook workbook, mdlUom param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = param.created_by;
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        List<mdlUom> _mdlUomList = new ArrayList<mdlUom>();
        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlResponseQuery _mdlResponseQuery = new mdlResponseQuery();
        ArrayList<String> uomID = new ArrayList<>();

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
                mdlUom _mdlUom = new mdlUom();


                //looping ke samping antar column
                while (_cellIterator.hasNext()) {
                    Cell _nextCell = _cellIterator.next();
                    int _columnIndex = _nextCell.getColumnIndex();

                    //proses get value from excel and set into model
                    if (_columnIndex == 0) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueUom = _nextCell.getStringCellValue();
                            _mdlUom.setUom(valueUom);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueUom = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlUom.setUom(valueUom);
                        }

                    } else if (_columnIndex == 1) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueDescription = _nextCell.getStringCellValue();
                            _mdlUom.setDescription(valueDescription);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueDescription = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlUom.setDescription(valueDescription);
                        } else {
                            errorList.add(
                                    "Error in Row : " + currentRow.getRowNum() + "Column : " + columnIndicator + "Desc : ERROR INPUT TYPE"
                            );
                            errorIndicator++;
                        }


                    }
                }
                _mdlUomList.add(_mdlUom);
                uomID.add(_mdlUom.uom);
            }
            workbook.close();


            for(int i = 0; i < uomID.size(); i++) {

                if (uomID.get(i) == null || uomID.get(i).isEmpty()) {
                    errorRow.add(i + 1);
                    errorIndicator ++;
                }

            }

            if(errorIndicator > 0){

                _mdlResponseQuery.Response = errorRow;
                _mdlResponseQuery.Status = false;

            } else {
                //INSERT INTO DATABASE


                for (mdlUom _mdlUom : _mdlUomList) {

                    List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

                    sql = "{call sp_uom_import(?,?,?)}";

                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlUom.uom));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlUom.description));
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
