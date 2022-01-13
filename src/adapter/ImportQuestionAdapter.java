package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import helper.DateHelper;
import model.Answer.mdlAnswer;
import model.Answer.mdlAnswerType;
import model.Query.mdlQueryExecute;
import model.Query.mdlResponseQuery;
import model.Question.mdlQuestion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ImportQuestionAdapter {
    final static Logger logger = LogManager.getLogger(ImportQuestionAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static mdlResponseQuery ImportExcelQuestion(Workbook workbook, mdlQuestion param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = param.created_by;
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        List<mdlQuestion> _mdlQuestionList = new ArrayList<mdlQuestion>();
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
                mdlQuestion _mdlQuestion = new mdlQuestion();

                //looping ke samping antar column
                while(_cellIterator.hasNext()) {
                    Cell _nextCell = _cellIterator.next();
                    int _columnIndex = _nextCell.getColumnIndex();

                    //proses get value from excel and set into model
                    if(_columnIndex == 0){

                        if(_nextCell.getCellType() == CellType.NUMERIC){
                            int valueSequence = (int) _nextCell.getNumericCellValue();
                            _mdlQuestion.setSequence(valueSequence);
                        }else if(_nextCell.getCellType() == CellType.STRING){
                            String sequenceString = _nextCell.getStringCellValue();
                            int valueSequence = Integer.parseInt(sequenceString);
                            _mdlQuestion.setSequence(valueSequence);
                        }

                    }else if(_columnIndex == 1){

                        if(_nextCell.getCellType() == CellType.STRING){
                            String valueNo = _nextCell.getStringCellValue();
                            _mdlQuestion.setNo(valueNo);
                        }else if(_nextCell.getCellType() == CellType.NUMERIC){
                            String valueNo = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlQuestion.setNo(valueNo);
                        }

                    }else if(_columnIndex == 2){

                        String valueQuestionText = _nextCell.getStringCellValue();
                        _mdlQuestion.setQuestion_text(valueQuestionText);

                    }else if(_columnIndex == 3){

                        if(_nextCell.getCellType() == CellType.STRING){
                            String valueAnswerTypeID = _nextCell.getStringCellValue();
                            _mdlQuestion.setAnswer_type_id(valueAnswerTypeID);
                        }else if(_nextCell.getCellType() == CellType.NUMERIC){
                            String valueAnswerTypeID = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlQuestion.setAnswer_type_id(valueAnswerTypeID);
                        }
                    }else if(_columnIndex == 4){

                        if(_nextCell.getCellType() == CellType.STRING){
                            String valueQuestionSetID = _nextCell.getStringCellValue();
                            _mdlQuestion.setQuestion_set_id(valueQuestionSetID);
                        }else if(_nextCell.getCellType() == CellType.NUMERIC){
                            String valueQuestionSetID = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlQuestion.setQuestion_set_id(valueQuestionSetID);
                        }
                    }else if(_columnIndex == 5){

                        if(_nextCell.getCellType() == CellType.STRING){
                            String valueQuestionCategoryID = _nextCell.getStringCellValue();
                            _mdlQuestion.setQuestion_category_id(valueQuestionCategoryID);
                        }else if(_nextCell.getCellType() == CellType.NUMERIC){
                            String valueQuestionCategoryID = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlQuestion.setQuestion_category_id(valueQuestionCategoryID);
                        }
                    }
                }
                _mdlQuestionList.add(_mdlQuestion);
            }
            workbook.close();

            //INSERT INTO DATABASE

            for (mdlQuestion _mdlQuestion : _mdlQuestionList){

                String dateNow = DateHelper.GetDateTimeNowCustomFormat("yyyyMMdd-HHmmss-SSS");
                StringBuilder sb = new StringBuilder();
                sb.append("QN-").append(dateNow);
                _mdlQuestion.question_id = sb.toString();

                String dateNow1 = DateHelper.GetDateTimeNowCustomFormat("yyyyMMdd-HHmmss-SSS");
                StringBuilder sb1 = new StringBuilder();
                sb1.append("AN-").append(dateNow1);
                _mdlQuestion.answer_id = sb1.toString();

                List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

                sql = "{call sp_question_import(?,?,?,?,?,?,?,?,?)}";

                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", _mdlQuestion.sequence));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlQuestion.no));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlQuestion.question_id));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlQuestion.question_text));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlQuestion.answer_type_id));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlQuestion.question_set_id));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlQuestion.question_category_id));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlQuestion.answer_id));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));

                _mdlResponseQuery = QueryAdapter.QueryManipulateWithDB2(sql, _mdlQueryExecuteList, functionName, user, db_name);

                if(_mdlResponseQuery.Status == true){

                    mdlAnswer _mdlAnswer = new mdlAnswer();
                    String dateNow2 = DateHelper.GetDateTimeNowCustomFormat("yyyyMMdd-HHmmss-SSS");
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("AN-").append(dateNow2);
                    _mdlAnswer.answer_id = sb2.toString();

                    List<mdlAnswerType> _mdlAnswerTypeList = new ArrayList<mdlAnswerType>();

                    _mdlAnswerTypeList = AnswerTypeAdapter.GetAnswerTypeFromQuestion(_mdlQuestion.answer_type_id, db_name, port);

                    for (mdlAnswerType _mdlAnswerType :  _mdlAnswerTypeList){
                        List<mdlQueryExecute> _mdlQueryExecuteList1 = new ArrayList<mdlQueryExecute>();

                        sql = "{call sp_answer_import(?,?,?,?,?,?)}";

                        _mdlQueryExecuteList1.add(QueryAdapter.QueryParam("int", _mdlQuestion.sequence));
                        _mdlQueryExecuteList1.add(QueryAdapter.QueryParam("string", _mdlQuestion.no));
                        _mdlQueryExecuteList1.add(QueryAdapter.QueryParam("string", _mdlQuestion.answer_id));
                        _mdlQueryExecuteList1.add(QueryAdapter.QueryParam("string", _mdlAnswerType.answer_type_text));
                        _mdlQueryExecuteList1.add(QueryAdapter.QueryParam("string", _mdlQuestion.question_id));
                        _mdlQueryExecuteList1.add(QueryAdapter.QueryParam("string", param.created_by));

                        _mdlResponseQuery = QueryAdapter.QueryManipulateWithDB2(sql, _mdlQueryExecuteList1, functionName, user, db_name);
                    }
                }

            }

        }catch (Exception e){
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", e.toString(), user, db_name), e);
        }

        return _mdlResponseQuery;
    }
}
