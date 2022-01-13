package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.Query.mdlQueryExecute;
import model.Query.mdlResponseQuery;
import model.Region.mdlRegion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ImportRegionAdapter {
    final static Logger logger = LogManager.getLogger(ImportRegionAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static mdlResponseQuery ImportExcelRegion(Workbook workbook, mdlRegion param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = param.created_by;
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        List<mdlRegion> _mdlRegionList = new ArrayList<mdlRegion>();
        mdlResponseQuery _mdlResponseQuery = new mdlResponseQuery();
        ArrayList<String> regionID = new ArrayList<>();

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
                mdlRegion _mdlRegion = new mdlRegion();

                //looping ke samping antar column
                while(_cellIterator.hasNext()) {
                    Cell _nextCell = _cellIterator.next();
                    int _columnIndex = _nextCell.getColumnIndex();

                    //proses get value from excel and set into model
                    if(_columnIndex == 0){

                        if(_nextCell.getCellType() == CellType.STRING){
                            String valueRegionID = _nextCell.getStringCellValue();
                            _mdlRegion.setRegion_id(valueRegionID);
                        }else if(_nextCell.getCellType() == CellType.NUMERIC){
                            String valueRegionID = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlRegion.setRegion_id(valueRegionID);
                        }

                    }else if(_columnIndex == 1){
                        String valueRegionName = _nextCell.getStringCellValue();
                        _mdlRegion.setRegion_name(valueRegionName);
                    }
                }
                _mdlRegionList.add(_mdlRegion);
                regionID.add(_mdlRegion.region_id);
            }
            workbook.close();

            for(int i = 0; i < regionID.size(); i++) {

                if (regionID.get(i) == null || regionID.get(i).isEmpty()) {
                    errorRow.add(i + 1);
                    errorIndicator ++;
                }

            }

            if(errorIndicator > 0){

                _mdlResponseQuery.Response = errorRow;
                _mdlResponseQuery.Status = false;

            } else {

                //INSERT INTO DATABASE

                for (mdlRegion _mdlRegion : _mdlRegionList) {

                    List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

                    sql = "{call sp_region_import(?,?,?)}";

                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlRegion.region_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlRegion.region_name));
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
