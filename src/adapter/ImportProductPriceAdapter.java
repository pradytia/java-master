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
import model.Product.mdlProductPriceTyp;
import model.Query.mdlQueryExecute;
import model.Query.mdlResponseQuery;
import model.Result.mdlResultFinal;
import model.Product.mdlProductPrice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public class ImportProductPriceAdapter  {
    final static Logger logger = LogManager.getLogger(ImportProductPriceAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static mdlResponseQuery ImportExcelProductPrice(Workbook workbook, mdlProductPrice param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = param.created_by;
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        List<mdlProductPrice> _mdlProductPriceList = new ArrayList<mdlProductPrice>();
        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlResponseQuery _mdlResponseQuery = new mdlResponseQuery();
        ArrayList<String> productPriceID = new ArrayList<>();

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
                mdlProductPrice _mdlProductPrice = new mdlProductPrice();


                while (_cellIterator.hasNext()) {
                    Cell _nextCell = _cellIterator.next();
                    int _columnIndex = _nextCell.getColumnIndex();


                    if (_columnIndex == 0) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueProductID = _nextCell.getStringCellValue();
                            _mdlProductPrice.setProduct_id(valueProductID);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueProductID = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlProductPrice.setProduct_id(valueProductID);
                        }

                    } else if (_columnIndex == 1) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueProductName = _nextCell.getStringCellValue();
                            _mdlProductPrice.setProduct_name(valueProductName);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueProductName = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlProductPrice.setProduct_name(valueProductName);
                        }


                    }else if (_columnIndex == 2) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueBranchID = _nextCell.getStringCellValue();
                            _mdlProductPrice.setBranch_id(valueBranchID);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueBranchID = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlProductPrice.setBranch_id(valueBranchID);
                        }


                    }else if (_columnIndex == 3) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueBranchName = _nextCell.getStringCellValue();
                            _mdlProductPrice.setBranch_name(valueBranchName);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueBranchName = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlProductPrice.setBranch_name(valueBranchName);
                        }


                    } else if (_columnIndex == 4) {
                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valuePrice = _nextCell.getStringCellValue();
                            _mdlProductPrice.setPrice(valuePrice);
                        }else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valuePrice = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlProductPrice.setPrice(valuePrice);
                        } else {
                            errorList.add(
                                    "Error in Row : " + currentRow.getRowNum() + "Column : " + columnIndicator + "Desc : ERROR INPUT TYPE"
                            );
                            errorIndicator++;
                        }
                    }else if (_columnIndex == 5) {
                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueModule = _nextCell.getStringCellValue();
                            _mdlProductPrice.setModule_id(valueModule);
                        }else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueModule = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlProductPrice.setModule_id(valueModule);
                        }
                    }
                }
                _mdlProductPriceList.add(_mdlProductPrice);
                productPriceID.add(_mdlProductPrice.product_id);
            }
            workbook.close();


            for(int i = 0; i < productPriceID.size(); i++) {

                if (productPriceID.get(i) == null || productPriceID.get(i).isEmpty()) {
                    errorRow.add(i + 1);
                    errorIndicator ++;
                }

            }

            if(errorIndicator > 0){

                _mdlResponseQuery.Response = errorRow;
                _mdlResponseQuery.Status = false;

            } else {
                //INSERT INTO DATABASE

                for (mdlProductPrice _mdlProductPrice : _mdlProductPriceList) {

                    List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

                    sql = "{call sp_product_price_import(?,?,?,?,?)}";

                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlProductPrice.product_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlProductPrice.branch_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlProductPrice.price));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlProductPrice.module_id));

                    _mdlResponseQuery = QueryAdapter.QueryManipulateWithDB2_v3(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

                }
            }

        }catch(Exception e){
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", e.toString(), user, db_name), e);
        }

        return _mdlResponseQuery;
    }

    public static mdlResponseQuery ImportExcelProductPriceTyp(Workbook workbook, mdlProductPriceTyp param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = param.created_by;
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        List<mdlProductPriceTyp> _mdlProductPriceTypList = new ArrayList<mdlProductPriceTyp>();
        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlResponseQuery _mdlResponseQuery = new mdlResponseQuery();
        ArrayList<String> productPriceID = new ArrayList<>();

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
                mdlProductPriceTyp _mdlProductPriceTyp = new mdlProductPriceTyp();

                while (_cellIterator.hasNext()) {
                    Cell _nextCell = _cellIterator.next();
                    int _columnIndex = _nextCell.getColumnIndex();

                    if (_columnIndex == 0) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueProductID = _nextCell.getStringCellValue();
                            _mdlProductPriceTyp.setProduct_id(valueProductID);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueProductID = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlProductPriceTyp.setProduct_id(valueProductID);
                        }

                    } else if (_columnIndex == 1) {
                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueProductName = _nextCell.getStringCellValue();
                            _mdlProductPriceTyp.setProduct_name(valueProductName);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueProductName = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlProductPriceTyp.setProduct_name(valueProductName);
                        }

                    }else if (_columnIndex == 2) {
                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueCustomerID = _nextCell.getStringCellValue();
                            _mdlProductPriceTyp.setCustomer_type_id(valueCustomerID);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueCustomerID = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlProductPriceTyp.setCustomer_type_id(valueCustomerID);
                        }
                    }else if (_columnIndex == 3) {
                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueCustomerTypeName = _nextCell.getStringCellValue();
                            _mdlProductPriceTyp.setCustomer_type_name(valueCustomerTypeName);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueCustomerTypeName = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlProductPriceTyp.setCustomer_type_name(valueCustomerTypeName);
                        }
                    } else if (_columnIndex == 4) {
                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valuePrice = _nextCell.getStringCellValue();
                            _mdlProductPriceTyp.setPrice(valuePrice);
                        }else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valuePrice = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlProductPriceTyp.setPrice(valuePrice);
                        }
                    }
                }
                _mdlProductPriceTypList.add(_mdlProductPriceTyp);
                productPriceID.add(_mdlProductPriceTyp.product_id);
            }
            workbook.close();


            for(int i = 0; i < productPriceID.size(); i++) {

                if (productPriceID.get(i) == null || productPriceID.get(i).isEmpty()) {
                    errorRow.add(i + 1);
                    errorIndicator ++;
                }

            }

            if(errorIndicator > 0){

                _mdlResponseQuery.Response = errorRow;
                _mdlResponseQuery.Status = false;

            } else {
                //INSERT INTO DATABASE

                for (mdlProductPriceTyp _mdlProductPriceTyp : _mdlProductPriceTypList) {

                    List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
                    sql = "{call sp_product_price_import_typ(?,?,?,?,?)}";
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlProductPriceTyp.product_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlProductPriceTyp.customer_type_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlProductPriceTyp.price));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.module_id));

                    _mdlResponseQuery = QueryAdapter.QueryManipulateWithDB2_v3(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
                }
            }
        }catch(Exception e){
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", e.toString(), user, db_name), e);
        }

        return _mdlResponseQuery;
    }

}
