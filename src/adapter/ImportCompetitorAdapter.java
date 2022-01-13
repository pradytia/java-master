package adapter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.ErrorExcel.mdlErrorExcel;
import model.Query.mdlQueryExecute;
import model.Competitor.mdlCompetitor;
import model.Query.mdlResponseQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public class ImportCompetitorAdapter {
    final static Logger logger = LogManager.getLogger(ImportCompetitorAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static mdlResponseQuery ImportExcelCompetitor(Workbook workbook, mdlCompetitor param, String db_name, int port){
        long startTime = System.currentTimeMillis();
        String user = param.created_by;
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        List<mdlCompetitor> _mdlCompetitorList = new ArrayList<mdlCompetitor>();
        mdlResponseQuery _mdlResponseQuery = new mdlResponseQuery();
        ArrayList<String> competitorID = new ArrayList<>();

        try {

            Sheet _firstSheet = workbook.getSheetAt(0);
            Iterator<Row> _rowIterator = _firstSheet.iterator();
            ArrayList<String> errorList = new ArrayList<String>();
            ArrayList<Integer> errorRow = new ArrayList<Integer>();
            int errorIndicator = 0;
            int columnIndicator = 1;

            //Skipping Excel header ==> looping ke bawah(row)
            while (_rowIterator.hasNext()) {
                Row _currentRow = _rowIterator.next();
                int _rowIndex = _currentRow.getRowNum();

                if (_rowIndex == 0) {
                    continue;
                }

                Iterator<Cell> _cellIterator = _currentRow.cellIterator();
                mdlCompetitor _mdlCompetitor = new mdlCompetitor();

                //looping ke samping antar column
                while(_cellIterator.hasNext()) {
                    Cell _nextCell = _cellIterator.next();
                    int _columnIndex = _nextCell.getColumnIndex();

                    //proses get value from excel and set into model
                    if(_columnIndex == 0){

                        if(_nextCell.getCellType() == CellType.STRING){
                            String valueCompetitorID = _nextCell.getStringCellValue();
                            _mdlCompetitor.setCompetitor_id(valueCompetitorID);
                        }else if(_nextCell.getCellType() == CellType.NUMERIC){
                            String valueCompetitorID = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlCompetitor.setCompetitor_id(valueCompetitorID);
                        }

                    }else if(_columnIndex == 1){
                        String valueCompetitorName = _nextCell.getStringCellValue();
                        _mdlCompetitor.setCompetitor_name(valueCompetitorName);
                    }
                }
                _mdlCompetitorList.add(_mdlCompetitor);
                competitorID.add(_mdlCompetitor.competitor_id);
            }
            workbook.close();


            for(int i = 0; i < competitorID.size(); i++) {

                if (competitorID.get(i) == null || competitorID.get(i).isEmpty()) {
                    errorRow.add(i + 1);
                    errorIndicator ++;
                }

            }

            if(errorIndicator > 0){
                _mdlResponseQuery.Response = errorRow;
                _mdlResponseQuery.Status = false;
            } else {
                //INSERT INTO DATABASE
                for (mdlCompetitor _mdlCompetitor : _mdlCompetitorList) {
                    List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
                    sql = "{call sp_competitor_import(?,?,?)}";
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCompetitor.competitor_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCompetitor.competitor_name));
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
