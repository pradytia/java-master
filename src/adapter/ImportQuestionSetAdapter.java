package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.Query.mdlQueryExecute;
import model.Query.mdlResponseQuery;
import model.Question.mdlQuestionSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ImportQuestionSetAdapter {
    final static Logger logger = LogManager.getLogger(ImportQuestionSetAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static mdlResponseQuery ImportExcelQuestionSet(Workbook workbook, mdlQuestionSet param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = param.created_by;
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        List<mdlQuestionSet> _mdlQuestionSetList = new ArrayList<mdlQuestionSet>();
        mdlResponseQuery _mdlResponseQuery = new mdlResponseQuery();

        try {

            Sheet _firstSheet = workbook.getSheetAt(0);
            Iterator<Row> _rowIterator = _firstSheet.iterator();

            //Skipping Excel header ==> looping ke bawah(row)
            while (_rowIterator.hasNext()) {
                Row _currentRow = _rowIterator.next();
                int _rowIndex = _currentRow.getRowNum();

                if (_rowIndex == 0) {
                    continue;
                }

                Iterator<Cell> _cellIterator = _currentRow.cellIterator();
                mdlQuestionSet _mdlQuestionSet = new mdlQuestionSet();

                //looping ke samping antar column
                while(_cellIterator.hasNext()) {
                    Cell _nextCell = _cellIterator.next();
                    int _columnIndex = _nextCell.getColumnIndex();

                    //proses get value from excel and set into model
                    if(_columnIndex == 0){

                        if(_nextCell.getCellType() == CellType.STRING){
                            String valueQuestionSetID = _nextCell.getStringCellValue();
                            _mdlQuestionSet.setQuestion_set_id(valueQuestionSetID);
                        }else if(_nextCell.getCellType() == CellType.NUMERIC){
                            String valueQuestionSetID = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlQuestionSet.setQuestion_set_id(valueQuestionSetID);
                        }

                    }else if(_columnIndex == 1){
                        String valueQuestionSetText = _nextCell.getStringCellValue();
                        _mdlQuestionSet.setQuestion_set_text(valueQuestionSetText);
                    }
                }
                _mdlQuestionSetList.add(_mdlQuestionSet);
            }
            workbook.close();

            //INSERT INTO DATABASE

            for (mdlQuestionSet _mdlQuestionSet : _mdlQuestionSetList){

                List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

                sql = "{call sp_question_set_import(?,?,?)}";

                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlQuestionSet.question_set_id));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlQuestionSet.question_set_text));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));

                _mdlResponseQuery = QueryAdapter.QueryManipulateWithDB2_v3(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

            }

        }catch (Exception e){
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", e.toString(), user, db_name), e);
        }

        return _mdlResponseQuery;
    }
}
