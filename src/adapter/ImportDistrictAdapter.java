package adapter;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import core.LogAdapter;
import database.QueryAdapter;
import model.ErrorExcel.mdlErrorExcel;
import model.Query.mdlQueryExecute;
import model.District.mdlDistrict;
import model.Query.mdlResponseQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public class ImportDistrictAdapter {
    final static Logger logger = LogManager.getLogger(ImportDistrictAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static mdlResponseQuery ImportExcelDistrict(Workbook workbook, mdlDistrict param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = param.created_by;
        String sql = "";
        String function_name = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        List<mdlDistrict> _mdlDistrictList = new ArrayList<mdlDistrict>();
        mdlResponseQuery _mdlResponseQuery = new mdlResponseQuery();
        ArrayList<String> districtID = new ArrayList<>();

        try {
//            int getSheet = workbook.getNumberOfSheets();
//            System.out.println(getSheet);
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> _rowIterator = firstSheet.iterator();
            ArrayList<String> errorList = new ArrayList<String>();
            ArrayList<Integer> errorRow = new ArrayList<Integer>();
            int errorIndicator = 0;
            int columnIndicator = 1;
            mdlErrorExcel _mdlErrorExcel = new mdlErrorExcel();


            //Skipping Excel header => looping ke bawah(row)
            while (_rowIterator.hasNext()) {
                Row currentRow = _rowIterator.next();
                int rowIndex = currentRow.getRowNum();

                if (rowIndex == 0) {
                    continue;
                }

                Iterator<Cell> _cellIterator = currentRow.cellIterator();
                mdlDistrict _mdlDistrict = new mdlDistrict();

                //looping ke samping(column)
                while (_cellIterator.hasNext()) {
                    Cell _nextCell = _cellIterator.next();
                    int columnIndex = _nextCell.getColumnIndex();

                    //proses get value from excel and set into model

                    if (columnIndex == 0) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueDistrictID = _nextCell.getStringCellValue();
                            _mdlDistrict.setDistrict_id(valueDistrictID);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueDistrictID = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlDistrict.setDistrict_id(valueDistrictID);
                        }

                    } else if (columnIndex == 1) {
                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueRegionID = _nextCell.getStringCellValue();
                            _mdlDistrict.setRegion_id(valueRegionID);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueRegionID = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlDistrict.setRegion_id(valueRegionID);
                        } else {

                            errorIndicator++;
                        }
                    } else if (columnIndex == 2) {
                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueDistrictName = _nextCell.getStringCellValue();
                            _mdlDistrict.setDistrict_name(valueDistrictName);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueDistrictName = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlDistrict.setDistrict_name(valueDistrictName);
                        }
                    }
                }
                _mdlDistrictList.add(_mdlDistrict);
                districtID.add(_mdlDistrict.district_id);
            }
            workbook.close();

            for (int i = 0; i < districtID.size(); i++) {

                if (districtID.get(i) == null) {
                    errorRow.add(i + 1);
                    errorIndicator++;
                }

            }

            if (errorIndicator > 0) {

                _mdlResponseQuery.Response = errorRow;
                _mdlResponseQuery.Status = false;

            } else {

                //INSERT INTO DATABASE

                for (mdlDistrict _mdlDistrict : _mdlDistrictList) {

                    List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

                    sql = "{call sp_district_import(?,?,?,?)}";

                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlDistrict.district_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlDistrict.region_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlDistrict.district_name));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));

                    _mdlResponseQuery = QueryAdapter.QueryManipulateWithDB2_v3(sql, _mdlQueryExecuteList, function_name, user, db_name, port);
                }
            }
        } catch (Exception e) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", function_name, "", gson.toJson(param), "", e.toString(), user, db_name), e);
        }
        return _mdlResponseQuery;
    }
}
