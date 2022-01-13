package adapter;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.Query.mdlQueryExecute;
import model.Query.mdlResponseQuery;
import model.Result.mdlResult;
import model.Branch.mdlBranch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public class ImportBranchAdapter {
    final static Logger logger = LogManager.getLogger(ImportBranchAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static mdlResponseQuery ImportExcelBranch (Workbook workbook, mdlBranch param, String db_name, int port){
        long startTime = System.currentTimeMillis();
        String user = param.created_by;
        String sql  = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        List<mdlBranch> _mdlBranchList = new ArrayList<mdlBranch>();
        mdlResponseQuery _mdlResponseQuery = new mdlResponseQuery();
        ArrayList<String> branchID = new ArrayList<>();

        try {

            Sheet _firstSheet = workbook.getSheetAt(0);
            Iterator<Row> _rowIterator = _firstSheet.iterator();
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
                mdlBranch _mdlBranch = new mdlBranch();

                //looping ke samping(antar column)
                while(_cellIterator.hasNext()) {
                    Cell _nextCell = _cellIterator.next();
                    int _columnIndex = _nextCell.getColumnIndex();

                    //proses get value from excel and set into model

                    if (_columnIndex == 0) {

                        if(_nextCell.getCellType() == CellType.STRING){
                            String value_branch_id = _nextCell.getStringCellValue();
                            _mdlBranch.setBranch_id(value_branch_id);
                        }else if(_nextCell.getCellType() == CellType.NUMERIC){
                            String value_branch_id = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlBranch.setBranch_id(value_branch_id);
                        }

                    }else if(_columnIndex == 1){

                        if(_nextCell.getCellType() == CellType.STRING){
                            String valueDistrictID = _nextCell.getStringCellValue();
                            _mdlBranch.setDistrict_id(valueDistrictID);
                        }else if(_nextCell.getCellType() == CellType.NUMERIC){
                            String valueDistrictID = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlBranch.setDistrict_id(valueDistrictID);
                        }

                    } else if(_columnIndex == 2){

                        if(_nextCell.getCellType() == CellType.STRING){
                            String valueRegionID = _nextCell.getStringCellValue();
                            _mdlBranch.setRegion_id(valueRegionID);
                        }else if(_nextCell.getCellType() == CellType.NUMERIC){
                            String valueRegionID = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlBranch.setRegion_id(valueRegionID);
                        }

                    }else if(_columnIndex == 3){

                        if(_nextCell.getCellType() == CellType.STRING){
                            String valueAreaID = _nextCell.getStringCellValue();
                            _mdlBranch.setArea_id(valueAreaID);
                        }else if(_nextCell.getCellType() == CellType.NUMERIC){
                            String valueAreaID = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlBranch.setArea_id(valueAreaID);
                        }

                    }else if(_columnIndex == 4){
                        String value_branch_name = _nextCell.getStringCellValue();
                        _mdlBranch.setBranch_name(value_branch_name);

                    }else if(_columnIndex == 5){
                        String value_branch_desc = _nextCell.getStringCellValue();
                        _mdlBranch.setBranch_description(value_branch_desc);

                    }else if(_columnIndex == 6){

                        if(_nextCell.getCellType() == CellType.STRING){
                            String value_latitude = _nextCell.getStringCellValue();
                            _mdlBranch.setLatitude(value_latitude);
                        }else if(_nextCell.getCellType() == CellType.NUMERIC){
                            String value_latitude = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlBranch.setLatitude(value_latitude);
                        }

                    }else if(_columnIndex == 7){

                        if(_nextCell.getCellType() == CellType.STRING){
                            String value_longitude = _nextCell.getStringCellValue();
                            _mdlBranch.setLongitude(value_longitude);
                        }else if(_nextCell.getCellType() == CellType.NUMERIC){
                            String value_longitude = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlBranch.setLongitude(value_longitude);
                        }

                    }else if(_columnIndex == 8){
                        String value_city = _nextCell.getStringCellValue();
                        _mdlBranch.setCity(value_city);
                    }else if(_columnIndex == 9){

                        if(_nextCell.getCellType() == CellType.STRING){
                            String value_post_code = _nextCell.getStringCellValue();
                            _mdlBranch.setPost_code(value_post_code);
                        }else if(_nextCell.getCellType() == CellType.NUMERIC){
                            String value_post_code = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlBranch.setPost_code(value_post_code);
                        }

                    }else if(_columnIndex == 10){

                        if(_nextCell.getCellType() == CellType.STRING){
                            String value_country_code = _nextCell.getStringCellValue();
                            _mdlBranch.setCountry_region_code(value_country_code);
                        }else if(_nextCell.getCellType() == CellType.NUMERIC){
                            String value_country_code = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlBranch.setCountry_region_code(value_country_code);
                        }
                    }else if (_columnIndex == 11){

                        if(_nextCell.getCellType() == CellType.STRING){
                            String value_phone = _nextCell.getStringCellValue();
                            _mdlBranch.setPhone(value_phone);
                        }else if(_nextCell.getCellType() == CellType.NUMERIC){
                            String value_phone = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlBranch.setPhone(value_phone);
                        }
                    }else if (_columnIndex == 12){
                        if(_nextCell.getCellType() == CellType.STRING){
                            String value_fax = _nextCell.getStringCellValue();
                            _mdlBranch.setFax(value_fax);
                        }else if(_nextCell.getCellType() == CellType.NUMERIC){
                            String value_fax = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlBranch.setFax(value_fax);
                        }
                    }

                }
                _mdlBranchList.add(_mdlBranch);
                branchID.add(_mdlBranch.branch_id);
            }
            workbook.close();


            for(int i = 0; i < branchID.size(); i++) {

                if (branchID.get(i) == null || branchID.get(i).isEmpty()) {
                    errorRow.add(i + 1);
                    errorIndicator ++;
                }

            }

            if(errorIndicator > 0){

                _mdlResponseQuery.Response = errorRow;
                _mdlResponseQuery.Status = false;

            } else {
                //INSERT INTO DATABASE
                for (mdlBranch _mdlBranch_excel : _mdlBranchList) {
                    List<mdlQueryExecute> mdl_query_execute_list = new ArrayList<mdlQueryExecute>();
                    sql = "{call sp_branch_import(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
                    mdl_query_execute_list.add(QueryAdapter.QueryParam("string", _mdlBranch_excel.branch_id));
                    mdl_query_execute_list.add(QueryAdapter.QueryParam("string", _mdlBranch_excel.district_id));
                    mdl_query_execute_list.add(QueryAdapter.QueryParam("string", _mdlBranch_excel.region_id));
                    mdl_query_execute_list.add(QueryAdapter.QueryParam("string", _mdlBranch_excel.area_id));
                    mdl_query_execute_list.add(QueryAdapter.QueryParam("string", _mdlBranch_excel.branch_name));
                    mdl_query_execute_list.add(QueryAdapter.QueryParam("string", _mdlBranch_excel.branch_description));
                    mdl_query_execute_list.add(QueryAdapter.QueryParam("string", _mdlBranch_excel.latitude));
                    mdl_query_execute_list.add(QueryAdapter.QueryParam("string", _mdlBranch_excel.longitude));
                    mdl_query_execute_list.add(QueryAdapter.QueryParam("string", _mdlBranch_excel.city));
                    mdl_query_execute_list.add(QueryAdapter.QueryParam("string", _mdlBranch_excel.post_code));
                    mdl_query_execute_list.add(QueryAdapter.QueryParam("string", _mdlBranch_excel.country_region_code));
                    mdl_query_execute_list.add(QueryAdapter.QueryParam("string", _mdlBranch_excel.phone));
                    mdl_query_execute_list.add(QueryAdapter.QueryParam("string", _mdlBranch_excel.fax));
                    mdl_query_execute_list.add(QueryAdapter.QueryParam("string", param.created_by));
                    _mdlResponseQuery = QueryAdapter.QueryManipulateWithDB2_v3(sql, mdl_query_execute_list, functionName, user, db_name, port);
                }
            }
        }catch (Exception e){
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", e.toString(), user, db_name), e);
        }
        return _mdlResponseQuery;
    }
}
