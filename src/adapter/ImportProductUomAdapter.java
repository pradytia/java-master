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
import model.Product.mdlProductUom;
import model.Query.mdlResponseQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public class ImportProductUomAdapter {
    final static Logger logger = LogManager.getLogger(ImportProductUomAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static mdlResponseQuery ImportExcelProductUom(Workbook workbook, mdlProductUom param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = param.created_by;
        String sql = "";
        String function_name = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        List<mdlProductUom> _mdlProductUomList = new ArrayList<mdlProductUom>();
        mdlResponseQuery _mdlResponseQuery = new mdlResponseQuery();
        ArrayList<String> productUomID = new ArrayList<>();

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
                mdlProductUom _mdlProductUom = new mdlProductUom();

                //looping ke samping(column)
                while (_cellIterator.hasNext()) {
                    Cell _nextCell = _cellIterator.next();
                    int columnIndex = _nextCell.getColumnIndex();

                    //proses get value from excel and set into model

                    if (columnIndex == 0) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueProductID = _nextCell.getStringCellValue();
                            _mdlProductUom.setProduct_id(valueProductID);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueProductID = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlProductUom.setProduct_id(valueProductID);
                        }

                    } else if (columnIndex == 1) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueUom = _nextCell.getStringCellValue();
                            _mdlProductUom.setUom(valueUom);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueUom = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlProductUom.setUom(valueUom);
                        }

                    } else if (columnIndex == 2) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueBaseUom = _nextCell.getStringCellValue();
                            _mdlProductUom.setBase_uom(valueBaseUom);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueBaseUom = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlProductUom.setBase_uom(valueBaseUom);
                        }

                    } else if (columnIndex == 3) {

                        if (_nextCell.getCellType() == CellType.STRING) {
                            String valueQuantity = _nextCell.getStringCellValue();
                            _mdlProductUom.setQuantity(valueQuantity);
                        } else if (_nextCell.getCellType() == CellType.NUMERIC) {
                            String valueQuantity = NumberToTextConverter.toText(_nextCell.getNumericCellValue());
                            _mdlProductUom.setQuantity(valueQuantity);
                        }

                    }
                }
                _mdlProductUomList.add(_mdlProductUom);
                productUomID.add(_mdlProductUom.product_id);
            }
            workbook.close();


            for (int i = 0; i < productUomID.size(); i++) {

                if (productUomID.get(i) == null || productUomID.get(i).isEmpty()) {
                    errorRow.add(i + 1);
                    errorIndicator++;
                }

            }

            if (errorIndicator > 0) {

                _mdlResponseQuery.Response = errorRow;
                _mdlResponseQuery.Status = false;

            } else {

                //INSERT INTO DATABASE

                for (mdlProductUom _mdlProductUom : _mdlProductUomList) {

                    List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

                    sql = "{call sp_product_uom_import(?,?,?,?,?)}";

                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlProductUom.product_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlProductUom.uom));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlProductUom.base_uom));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlProductUom.quantity));
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
