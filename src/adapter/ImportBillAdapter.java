package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.Bill.mdlBill;
import model.Query.mdlQueryExecute;
import model.Query.mdlResponseQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ImportBillAdapter {
    final static Logger logger = LogManager.getLogger(ImportBillAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static mdlResponseQuery ImportExcelBill(MultipartFile file_excel, String extension, mdlBill param, String db_name, int port){
        long startTime = System.currentTimeMillis();
        String user = param.created_by;
        String sql  = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        List<mdlBill> _mdlBillList = new ArrayList<mdlBill>();
        mdlResponseQuery _mdlResponseQuery = new mdlResponseQuery();

        try {
            //Proses reading file excel
            InputStream input_stream = file_excel.getInputStream();
            Workbook workbook = null;

            //statement for extension Excel File
            if(extension.equals("xlsx")){
                workbook = new XSSFWorkbook(input_stream);
            }else if(extension.equals("xls")){
                workbook = new HSSFWorkbook(input_stream);
            }else {
                workbook = null;
            }

            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = firstSheet.iterator();

            //Skipping Excel header => looping ke bawah(row)
            while (rowIterator.hasNext()) {
                Row currentRow = rowIterator.next();
                int rowIndex = currentRow.getRowNum();

                if (rowIndex == 0) {
                    continue;
                }

                Iterator<Cell> cellIterator = currentRow.cellIterator();
                mdlBill _mdlBill = new mdlBill();

                //looping ke samping antar column
                while(cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();
                    int columnIndex = nextCell.getColumnIndex();

                    //proses get value from excel and set into model
                    if(columnIndex == 0){

                        if(nextCell.getCellType() == CellType.STRING){
                            String valueBillID = nextCell.getStringCellValue();
                            _mdlBill.setBill_id(valueBillID);
                        }else if(nextCell.getCellType() == CellType.NUMERIC){
                            String valueBillID = NumberToTextConverter.toText(nextCell.getNumericCellValue());
                            _mdlBill.setBill_id(valueBillID);
                        }

                    }else if(columnIndex == 1){

                        if(nextCell.getCellType() == CellType.STRING){
                            String valueInvoiceID = nextCell.getStringCellValue();
                            _mdlBill.setInvoice_id(valueInvoiceID);
                        }else if(nextCell.getCellType() == CellType.NUMERIC){
                            String valueInvoiceID = NumberToTextConverter.toText(nextCell.getNumericCellValue());
                            _mdlBill.setInvoice_id(valueInvoiceID);
                        }

                    }else if(columnIndex == 2){

                        if(nextCell.getCellType() == CellType.STRING){
                            String valueBranchID = nextCell.getStringCellValue();
                            _mdlBill.setBranch_id(valueBranchID);
                        }else if(nextCell.getCellType() == CellType.NUMERIC){
                            String valueBranchID = NumberToTextConverter.toText(nextCell.getNumericCellValue());
                            _mdlBill.setBranch_id(valueBranchID);
                        }

                    }else if (columnIndex == 3){

                        if(nextCell.getCellType() == CellType.STRING){
                            String valueCustomerID = nextCell.getStringCellValue();
                            _mdlBill.setCustomer_id(valueCustomerID);
                        }else if(nextCell.getCellType() == CellType.NUMERIC){
                            String valueCustomerID = NumberToTextConverter.toText(nextCell.getNumericCellValue());
                            _mdlBill.setCustomer_id(valueCustomerID);
                        }

                    }else if(columnIndex == 4){
                        Date valueEntryDate = nextCell.getDateCellValue();
                        //convert Date to String from excel
                        SimpleDateFormat simple_date_format = new SimpleDateFormat("yyyy-MM-dd");
                        String new_valueEntryDate = simple_date_format.format(valueEntryDate);
                        _mdlBill.setInvoice_date(new_valueEntryDate);

                    }else if (columnIndex == 5){

                        if(nextCell.getCellType() == CellType.STRING){
                            String valueInvoice = nextCell.getStringCellValue();
                            _mdlBill.setInvoice_value(valueInvoice);
                        }else if(nextCell.getCellType() == CellType.NUMERIC){
                            String valueInvoice = NumberToTextConverter.toText(nextCell.getNumericCellValue());
                            _mdlBill.setInvoice_value(valueInvoice);
                        }

                    }else if (columnIndex == 6){

                        if(nextCell.getCellType() == CellType.STRING){
                            String valueRemainInvoice = nextCell.getStringCellValue();
                            _mdlBill.setRemain_invoice_value(valueRemainInvoice);
                        }else if(nextCell.getCellType() == CellType.NUMERIC){
                            String valueRemainInvoice = NumberToTextConverter.toText(nextCell.getNumericCellValue());
                            _mdlBill.setRemain_invoice_value(valueRemainInvoice);
                        }

                    }else if (columnIndex == 7){

                        if(nextCell.getCellType() == CellType.STRING){
                            String valueSalesOrderID = nextCell.getStringCellValue();
                            _mdlBill.setSales_order_id(valueSalesOrderID);
                        }else if(nextCell.getCellType() == CellType.NUMERIC){
                            String valueSalesOrderID = NumberToTextConverter.toText(nextCell.getNumericCellValue());
                            _mdlBill.setSales_order_id(valueSalesOrderID);
                        }

                    }else if(columnIndex == 8){
                        Date valueEntryDate = nextCell.getDateCellValue();
                        //convert Date to String from excel
                        SimpleDateFormat simple_date_format = new SimpleDateFormat("yyyy-MM-dd");
                        String new_valueEntryDate = simple_date_format.format(valueEntryDate);
                        _mdlBill.setSales_order_date(new_valueEntryDate);

                    }else if(columnIndex == 9){
                        Date valueEntryDate = nextCell.getDateCellValue();
                        //convert Date to String from excel
                        SimpleDateFormat simple_date_format = new SimpleDateFormat("yyyy-MM-dd");
                        String new_valueEntryDate = simple_date_format.format(valueEntryDate);
                        _mdlBill.setDue_date(new_valueEntryDate);

                    }
                }
                _mdlBillList.add(_mdlBill);
            }
            workbook.close();

            //INSERT INTO DATABASE
            for(mdlBill _mdlBill : _mdlBillList){
                List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
                sql = "{call sp_bill_import(?,?,?,?,?,?,?,?,?,?,?)}";
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlBill.bill_id));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlBill.invoice_id));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlBill.branch_id));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlBill.customer_id));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlBill.invoice_date));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlBill.invoice_value));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlBill.remain_invoice_value));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlBill.sales_order_id));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlBill.sales_order_date));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", _mdlBill.due_date));
                _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));
                _mdlResponseQuery = QueryAdapter.QueryManipulateWithDB2_v3(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
            }
        }catch (Exception e){
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", e.toString(), user, db_name), e);
        }
        return _mdlResponseQuery;
    }
}
