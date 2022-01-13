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
import model.Product.mdlProduct;
import model.Query.mdlResponseQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public class ImportProductAdapter {
    final static Logger logger = LogManager.getLogger(ImportProductAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static mdlResponseQuery ImportExcelProduct(Workbook workbook, mdlProduct param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = param.created_by;
        String sql = "";
        String function_name = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        List<mdlProduct> _mdlProductList = new ArrayList<mdlProduct>();
        mdlResponseQuery _mdlResponseQuery = new mdlResponseQuery();
        ArrayList<String> productID = new ArrayList<>();

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
                mdlProduct _mdlProduct = new mdlProduct();

                //looping ke samping(column)
                while (_cellIterator.hasNext()) {
                    Cell _nextCell = _cellIterator.next();
                    int columnIndex = _nextCell.getColumnIndex();

                    //proses get value from excel and set into model

                    if (columnIndex == 0) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueProductID = _nextCell.getStringCellValue();
                            _mdlProduct.setProduct_id(valueProductID);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueProductID = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlProduct.setProduct_id(valueProductID);
                        }

                    } else if (columnIndex == 1) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueProductName = _nextCell.getStringCellValue();
                            _mdlProduct.setProduct_name(valueProductName);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueProductName = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlProduct.setProduct_name(valueProductName);
                        }

                    } else if (columnIndex == 2) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueProductType = _nextCell.getStringCellValue();
                            _mdlProduct.setProduct_type(valueProductType);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueProductType = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlProduct.setProduct_type(valueProductType);
                        }

                    } else if (columnIndex == 3) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueProductGroup = _nextCell.getStringCellValue();
                            _mdlProduct.setProduct_group(valueProductGroup);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueProductGroup = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlProduct.setProduct_group(valueProductGroup);
                        }

                    } else if (columnIndex == 4) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueProductWeight = _nextCell.getStringCellValue();
                            _mdlProduct.setProduct_weight(valueProductWeight);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueProductWeight = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlProduct.setProduct_weight(valueProductWeight);
                        }

                    } else if (columnIndex == 5) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueUom = _nextCell.getStringCellValue();
                            _mdlProduct.setUom(valueUom);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueUom = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlProduct.setUom(valueUom);
                        }

                    } else if (columnIndex == 6) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueDnrCode = _nextCell.getStringCellValue();
                            _mdlProduct.setDnr_code(valueDnrCode);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueDnrCode = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlProduct.setDnr_code(valueDnrCode);
                        }

                    } else if (columnIndex == 7) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueSapCode = _nextCell.getStringCellValue();
                            _mdlProduct.setSap_code(valueSapCode);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueSapCode = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlProduct.setSap_code(valueSapCode);
                        }

                    } else if (columnIndex == 8) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valuePrice = _nextCell.getStringCellValue();
                            _mdlProduct.setPrice(valuePrice);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valuePrice = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlProduct.setPrice(valuePrice);
                        }

                    }
                }
                _mdlProductList.add(_mdlProduct);
                productID.add(_mdlProduct.product_id);
            }
            workbook.close();


            for (int i = 0; i < productID.size(); i++) {

                if (productID.get(i) == null || productID.get(i).isEmpty()) {
                    errorRow.add(i + 1);
                    errorIndicator++;
                }

            }

            if (errorIndicator > 0) {

                _mdlResponseQuery.Response = errorRow;
                _mdlResponseQuery.Status = false;

            } else {

                //INSERT INTO DATABASE

                for (mdlProduct _mdlProduct : _mdlProductList) {

                    List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

                    sql = "{call sp_product_import(?,?,?,?,?,?,?,?,?,?)}";

                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlProduct.product_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlProduct.product_name));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlProduct.product_type));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlProduct.product_group));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlProduct.product_weight));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlProduct.uom));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlProduct.dnr_code));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlProduct.sap_code));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlProduct.price));
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
