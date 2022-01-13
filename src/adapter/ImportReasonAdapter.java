package adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.ErrorExcel.mdlErrorExcel;
import model.Query.mdlQueryExecute;
import model.Query.mdlResponseQuery;
import model.Reason.mdlReason;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;


public class ImportReasonAdapter {
    final static Logger logger = LogManager.getLogger(ImportReasonAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static mdlResponseQuery ImportExcelReason(Workbook workbook, mdlReason param, String db_name, int port){
        long startTime = System.currentTimeMillis();
        String user = param.created_by;
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        List<mdlReason> _mdlReasonList = new ArrayList<mdlReason>();
        mdlResponseQuery _mdlResponseQuery = new mdlResponseQuery();
        ArrayList<String> reasonID = new ArrayList<>();

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
                mdlReason _mdlReason = new mdlReason();

                //looping ke samping(column)
                while(_cellIterator.hasNext()) {
                    Cell _nextCell = _cellIterator.next();
                    int columnIndex = _nextCell.getColumnIndex();

                    //proses get value from excel and set into model

                    if(columnIndex == 0){

                        if(_nextCell.getCellType() == CellType.STRING){
                            String valueReasonID = _nextCell.getStringCellValue();
                            _mdlReason.setReason_id(valueReasonID);
                        }else if(_nextCell.getCellType() == CellType.NUMERIC){
                            String valueReasonID = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlReason.setReason_id(valueReasonID);
                        }

                    }else if(columnIndex == 1){
                        if(_nextCell.getCellType() == CellType.STRING){
                            String valueReasonType = _nextCell.getStringCellValue().toLowerCase();

                            if (valueReasonType.equals("kunjungan")) {
//                                String capitalize = valueReasonType.substring(0,1).toUpperCase() + valueReasonType.substring(1);
                                String value = "visit";
                                _mdlReason.setReason_type(value);
                            } else if (valueReasonType.equals("kunjungan pelanggan")) {
//                                char[] chars = valueReasonType.toCharArray();
//                                chars[0] = Character.toUpperCase(chars[0]);
//                                chars[10] = Character.toUpperCase(chars[10]);
//                                valueReasonType = new String(chars);
                                String value = "customer";
                                _mdlReason.setReason_type(value);
                            }else {
                                _mdlReason.setReason_type(" ");
                            }

                        }else if(_nextCell.getCellType() == CellType.NUMERIC){
                            String valueReasonType = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            if (valueReasonType.equals("kunjungan")) {
//                                String capitalize = valueReasonType.substring(0,1).toUpperCase() + valueReasonType.substring(1);
                                String value = "visit";
                                _mdlReason.setReason_type(value);
                            } else if (valueReasonType.equals("kunjungan pelanggan")) {
//                                char[] chars = valueReasonType.toCharArray();
//                                chars[0] = Character.toUpperCase(chars[0]);
//                                chars[10] = Character.toUpperCase(chars[10]);
//                                valueReasonType = new String(chars);
                                String value = "customer";
                                _mdlReason.setReason_type(value);
                            }else {
                                _mdlReason.setReason_type(" ");
                            }
                        }else {

                            errorIndicator ++;
                        }
                    }else if(columnIndex == 2){
                        if(_nextCell.getCellType() == CellType.STRING){
                            String value = _nextCell.getStringCellValue();
                            _mdlReason.setValue(value);
                        }else if(_nextCell.getCellType() == CellType.NUMERIC){
                            String value = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlReason.setValue(value);
                        }

                    }
                }
                _mdlReasonList.add(_mdlReason);
                reasonID.add(_mdlReason.reason_id);
            }
            workbook.close();


            for(int i = 0; i < reasonID.size(); i++) {

                if (reasonID.get(i) == null || reasonID.get(i).isEmpty()) {
                    errorRow.add(i + 1);
                    errorIndicator ++;
                }

            }

            if(errorIndicator > 0){

                _mdlResponseQuery.Response = errorRow;
                _mdlResponseQuery.Status = false;

            } else {
                //INSERT INTO DATABASE

                for (mdlReason _mdlReason : _mdlReasonList) {

                    List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

                    sql = "{call sp_reason_import(?,?,?,?)}";

                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlReason.reason_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlReason.reason_type));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlReason.value));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));

                    _mdlResponseQuery = QueryAdapter.QueryManipulateWithDB2_v3(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

                }
            }

        }catch (Exception e){
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", e.toString(), user, db_name), e);
        }
        return _mdlResponseQuery;
    }
}
