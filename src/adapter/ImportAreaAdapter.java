package adapter;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.sqlserver.jdbc.StringUtils;
import core.LogAdapter;
import database.QueryAdapter;
import model.Query.mdlQueryExecute;
import model.Query.mdlResponseQuery;
import model.Result.mdlResultFinal;
import model.Area.mdlArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public class ImportAreaAdapter {
    final static Logger logger = LogManager.getLogger(ImportAreaAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static mdlResponseQuery ImportExcelArea(Workbook workbook, mdlArea param, String db_name, int port) {

        String user = param.created_by;
        String sql = "";
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        List<mdlArea> _mdlAreaList = new ArrayList<mdlArea>();
        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlResponseQuery _mdlResponseQuery = new mdlResponseQuery();
        ArrayList<String> areaID = new ArrayList<>();

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
                mdlArea _mdlArea = new mdlArea();


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
                            String valueAreaID = _nextCell.getStringCellValue();
                            _mdlArea.setArea_id(valueAreaID);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueAreaID = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlArea.setArea_id(valueAreaID);
                        }


                    } else if (_columnIndex == 1) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueDistrictID = _nextCell.getStringCellValue();
                            _mdlArea.setdistrict_id(valueDistrictID);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueDistrictID = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlArea.setdistrict_id(valueDistrictID);
                        }


                    } else if (_columnIndex == 2) {
                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueAreaName = _nextCell.getStringCellValue();
                            _mdlArea.setArea_name(valueAreaName);
                        }
                    }
                }
                _mdlAreaList.add(_mdlArea);
                areaID.add(_mdlArea.area_id);
            }
            workbook.close();

            for (int i = 0; i < areaID.size(); i++) {

                if (areaID.get(i) == null || areaID.get(i).isEmpty()) {
                    errorRow.add(i + 1);
                    errorIndicator++;
                }

            }

            if (errorIndicator > 0) {
                _mdlResponseQuery.Response = errorRow;
                _mdlResponseQuery.Status = false;

            } else {
                //INSERT INTO DATABASE
                for (mdlArea _mdlArea : _mdlAreaList) {
                    List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
                    sql = "{call sp_area_import(?,?,?,?)}";
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlArea.area_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlArea.district_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlArea.area_name));
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
