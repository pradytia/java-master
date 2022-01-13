package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.Customer.mdlCustomerCoordinate;
import model.Query.mdlQueryExecute;
import model.Query.mdlResponseQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ImportCustomerCoordinateAdapter {
    final static Logger logger = LogManager.getLogger(ImportCustomerCoordinateAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static mdlResponseQuery ImportExcelIntoDB(Workbook workbook, mdlCustomerCoordinate param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String user = param.created_by;
        boolean resultExec = false;
        List<mdlCustomerCoordinate> _mdlCustomerCoordinateList = new ArrayList<mdlCustomerCoordinate>();
        mdlResponseQuery _mdlResponseQuery = new mdlResponseQuery();
        ArrayList<String> customerCoordinateID = new ArrayList<>();

        try {

            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = firstSheet.iterator();
            Iterator<Row> _rowIterator = firstSheet.iterator();
            ArrayList<String> errorList = new ArrayList<String>();
            ArrayList<Integer> errorRow = new ArrayList<Integer>();
            int errorIndicator = 0;

            //Skipping Excel header => looping ke bawah(row)
            while (rowIterator.hasNext()) {
                Row currentRow = rowIterator.next();
                int rowIndex = currentRow.getRowNum();

                if (rowIndex == 0) {
                    continue;
                }

                Iterator<Cell> _cellIterator = currentRow.cellIterator();
                mdlCustomerCoordinate _mdlCustomerCoordinate = new mdlCustomerCoordinate();

                //looping ke samping(column)
                while (_cellIterator.hasNext()) {
                    Cell _nextCell = _cellIterator.next();
                    int columnIndex = _nextCell.getColumnIndex();

                    //proses get value from excel and set into model

                    if (columnIndex == 0) {
                        //STATEMENT
                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueCustomerID = _nextCell.getStringCellValue();
                            _mdlCustomerCoordinate.setCustomer_id(valueCustomerID);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueCustomerID = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlCustomerCoordinate.setCustomer_id(valueCustomerID);
                        }

                    } else if (columnIndex == 1) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueSeq = _nextCell.getStringCellValue();
                            _mdlCustomerCoordinate.setSeq(valueSeq);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueSeq = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlCustomerCoordinate.setSeq(valueSeq);
                        }

                    } else if (columnIndex == 2) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueCustomerAddress = _nextCell.getStringCellValue();
                            _mdlCustomerCoordinate.setCustomer_address(valueCustomerAddress);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueCustomerAddress = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlCustomerCoordinate.setCustomer_address(valueCustomerAddress);
                        }

                    } else if (columnIndex == 3) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueRadius = _nextCell.getStringCellValue();
                            _mdlCustomerCoordinate.setRadius(valueRadius);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueRadius = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlCustomerCoordinate.setRadius(valueRadius);
                        }

                    } else if (columnIndex == 4) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueCity = _nextCell.getStringCellValue();
                            _mdlCustomerCoordinate.setCity(valueCity);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueCity = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlCustomerCoordinate.setCity(valueCity);
                        }

                    } else if (columnIndex == 5) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueCountry = _nextCell.getStringCellValue();
                            _mdlCustomerCoordinate.setCountry_region_code(valueCountry);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueCountry = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlCustomerCoordinate.setCountry_region_code(valueCountry);
                        }

                    } else if (columnIndex == 6) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueLatitude = _nextCell.getStringCellValue();
                            _mdlCustomerCoordinate.setLatitude(valueLatitude);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueLatitude = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlCustomerCoordinate.setLatitude(valueLatitude);
                        }

                    } else if (columnIndex == 7) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueLongitude = _nextCell.getStringCellValue();
                            _mdlCustomerCoordinate.setLongitude(valueLongitude);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueLongitude = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlCustomerCoordinate.setLongitude(valueLongitude);
                        }

                    }
                }
                _mdlCustomerCoordinateList.add(_mdlCustomerCoordinate);
                customerCoordinateID.add(_mdlCustomerCoordinate.customer_id);
            }
            workbook.close();

            if (errorIndicator > 0) {

                _mdlResponseQuery.Response = errorRow;
                _mdlResponseQuery.Status = false;

            } else {

                //INSERT INTO DATABASE

                for (mdlCustomerCoordinate _mdlCustomerCoordinate : _mdlCustomerCoordinateList) {

                    List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

                    sql = "{call sp_customer_coordinate_import_v2(?,?,?,?,?,?,?,?,?)}";
                    if (_mdlCustomerCoordinate.longitude == null || _mdlCustomerCoordinate.latitude == null) {
                        _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCustomerCoordinate.customer_id));
                        _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCustomerCoordinate.seq));
                        _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCustomerCoordinate.customer_address));
                        _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));
                        _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCustomerCoordinate.radius));
                        _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCustomerCoordinate.city));
                        _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCustomerCoordinate.country_region_code));
                        _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", "0"));
                        _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", "0"));
                    } else {
                        _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCustomerCoordinate.customer_id));
                        _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCustomerCoordinate.seq));
                        _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCustomerCoordinate.customer_address));
                        _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));
                        _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCustomerCoordinate.radius));
                        _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCustomerCoordinate.city));
                        _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCustomerCoordinate.country_region_code));
                        _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCustomerCoordinate.latitude));
                        _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlCustomerCoordinate.longitude));
                    }
                    _mdlResponseQuery = QueryAdapter.QueryManipulateWithDB2_v3(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
                }
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", ex.toString(), user, db_name), ex);
        }
        return _mdlResponseQuery;
    }
}
