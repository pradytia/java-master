package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.Cost.mdlCost;
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

public class ImportCostAdapter {
    final static Logger logger = LogManager.getLogger(ImportCostAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static mdlResponseQuery ImportExcelCost(Workbook workbook, mdlCost param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = param.created_by;
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        List<mdlCost> _mdlCostList = new ArrayList<mdlCost>();
        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlResponseQuery _mdlResponseQuery = new mdlResponseQuery();
        ArrayList<String> costID = new ArrayList<>();

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
                mdlCost _mdlCost = new mdlCost();


                //looping ke samping antar column
                while (_cellIterator.hasNext()) {
                    Cell _nextCell = _cellIterator.next();

                    if (_nextCell.getCellType() == CellType.BLANK) {
                        continue;
                    }
                    int _columnIndex = _nextCell.getColumnIndex();

                    //proses get value from excel and set into model
                    if (_columnIndex == 0) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueCostID = _nextCell.getStringCellValue();
                            _mdlCost.setCost_id(valueCostID);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueCostID = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlCost.setCost_id(valueCostID);
                        }


                    } else if (_columnIndex == 1) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueCostName = _nextCell.getStringCellValue();
                            _mdlCost.setCost_name(valueCostName);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueCostName = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlCost.setCost_name(valueCostName);
                        }


                    }
                }
                _mdlCostList.add(_mdlCost);
                costID.add(_mdlCost.cost_id);
            }
            workbook.close();

            for (int i = 0; i < costID.size(); i++) {

                if (costID.get(i) == null || costID.get(i).isEmpty()) {
                    errorRow.add(i + 1);
                    errorIndicator++;
                }

            }

            if (errorIndicator > 0) {

                _mdlResponseQuery.Response = errorRow;
                _mdlResponseQuery.Status = false;

            } else {

                //INSERT INTO DATABASE
                for (mdlCost _mdlCost : _mdlCostList) {
                    List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
                    sql = "{call sp_cost_import(?,?,?,?)}";
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCost.cost_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCost.cost_name));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", ""));
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
