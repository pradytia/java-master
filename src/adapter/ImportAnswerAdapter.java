package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.Query.mdlQueryExecute;
import model.Answer.mdlAnswer;
import model.Query.mdlResponseQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ImportAnswerAdapter {
    final static Logger logger = LogManager.getLogger(ImportAnswerAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static mdlResponseQuery ImportExcelAnswer(Workbook workbook, mdlAnswer param, String db_name, int port) {
        String user = param.created_by;
        String sql = "";
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        List<mdlAnswer> _mdlAnswerList = new ArrayList<mdlAnswer>();
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
                mdlAnswer _mdlAnswer = new mdlAnswer();

                //looping ke samping antar column
                while (_cellIterator.hasNext()) {
                    Cell _nextCell = _cellIterator.next();
                    int _columnIndex = _nextCell.getColumnIndex();

                    //proses get value from excel and set into model
                    if (_columnIndex == 0) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueSequence = _nextCell.getStringCellValue();
                            _mdlAnswer.setSequence(valueSequence);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueSequence = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlAnswer.setSequence(valueSequence);
                        }

                    } else if (_columnIndex == 1) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueNo = _nextCell.getStringCellValue();
                            _mdlAnswer.setNo(valueNo);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueNo = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlAnswer.setNo(valueNo);
                        }

                    } else if (_columnIndex == 2) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueAnswerID = _nextCell.getStringCellValue();
                            _mdlAnswer.setAnswer_id(valueAnswerID);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueAnswerID = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlAnswer.setAnswer_id(valueAnswerID);
                        }

                    } else if (_columnIndex == 3) {

                        String valueAnswerText = _nextCell.getStringCellValue();
                        _mdlAnswer.setAnswer_text(valueAnswerText);

                    } else if (_columnIndex == 4) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueQuestionID = _nextCell.getStringCellValue();
                            _mdlAnswer.setQuestion_id(valueQuestionID);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueQuestionID = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlAnswer.setQuestion_id(valueQuestionID);
                        }
                    }
                }
                _mdlAnswerList.add(_mdlAnswer);
            }
            workbook.close();

            //INSERT INTO DATABASE
            for (mdlAnswer _mdlAnswer : _mdlAnswerList) {
                List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
                sql = "{call sp_answer_import(?,?,?,?,?,?)}";
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlAnswer.sequence));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlAnswer.no));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlAnswer.answer_id));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlAnswer.answer_text));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlAnswer.question_id));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));
                _mdlResponseQuery = QueryAdapter.QueryManipulateWithDB2_v3(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", ex.toString(), user, db_name), ex);
        }
        return _mdlResponseQuery;
    }
}
