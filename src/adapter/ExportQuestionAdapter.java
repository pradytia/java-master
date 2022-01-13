package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import helper.DateHelper;
import model.Export.mdlExportParam;
import model.Query.mdlQueryExecute;
import model.Question.mdlQuestion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.rowset.CachedRowSet;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ExportQuestionAdapter {
    final static Logger logger = LogManager.getLogger(ExportQuestionAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static String ExportQuestion (mdlQuestion param1, mdlExportParam param2, int port, String db_name){
        String sql = "";
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String urlLink = "";
        String user = param1.created_by;
        Workbook workbook = new XSSFWorkbook();
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        CachedRowSet jrs = null;
        List<mdlQuestion> mdlQuestionList = new ArrayList<mdlQuestion>();
        FileOutputStream outPutStream = null;

        try {
            sql = "{call sp_question_for_export(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param1.question_set_id));
            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);

            while (jrs.next()) {
                mdlQuestion _mdlQuestion = new mdlQuestion();
                _mdlQuestion.question_id = jrs.getString("question_id");
                _mdlQuestion.question_text = jrs.getString("question_text");
                _mdlQuestion.answer_type_id = jrs.getString("answer_type_id");
//                _mdlQuestion.is_sub_question = jrs.getString("is_sub_question");
                _mdlQuestion.sequence = jrs.getInt("sequence");
                _mdlQuestion.question_set_id = jrs.getString("question_set_id");
                _mdlQuestion.question_category_id = jrs.getString("question_category_id");
                _mdlQuestion.answer_id = jrs.getString("answer_id");
                _mdlQuestion.no = jrs.getString("no");
//                _mdlQuestion.mandatory = jrs.getString("mandatory");
//                _mdlQuestion.is_efective_call = jrs.getString("is_efective_call");
                mdlQuestionList.add(_mdlQuestion);
            }

            //== Create Workbook==\\
            String[] columns = {"Urutan",  "No", "Isi Pertanyaan", "ID Tipe Jawaban",  "ID Set Pertanyaan", "ID Kategori Pertanyaan", "ID Jawaban", "ID Pertanyaan"};

            Sheet sheet = workbook.createSheet("Pertanyaan");
            Font headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 11);
            headerFont.setColor(IndexedColors.BLACK.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);

            for(int i = 0; i < columns.length; i++ ){
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }

            int rowNum = 1;
            for(mdlQuestion _mdlQuestion : mdlQuestionList){
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(_mdlQuestion.sequence);
                row.createCell(1).setCellValue(_mdlQuestion.no);
                row.createCell(2).setCellValue(_mdlQuestion.question_text);
                row.createCell(3).setCellValue(_mdlQuestion.answer_type_id);
                row.createCell(4).setCellValue(_mdlQuestion.question_set_id);
                row.createCell(5).setCellValue(_mdlQuestion.question_category_id);
                row.createCell(6).setCellValue(_mdlQuestion.answer_id);
                row.createCell(7).setCellValue(_mdlQuestion.question_id);
//                row.createCell(9).setCellValue(_mdlQuestion.mandatory);
//                row.createCell(10).setCellValue(_mdlQuestion.is_efective_call);
            }

            for (int i = 0; i < columns.length; i++){
                sheet.autoSizeColumn(i);
            }

            try {
                Context context = (Context) new InitialContext().lookup("java:comp/env");
                String paramFilePath = "";
                String paramFileUrl = "";
                if (port==443){
                    paramFilePath = (String) context.lookup("path_web_server_for_upload_report");
                    paramFileUrl = (String) context.lookup("url_web_server_for_upload_report");
                }else {
                    paramFilePath = (String) context.lookup("path_web_server_for_upload_report_dev");
                    paramFileUrl = (String) context.lookup("url_web_server_for_upload_report_dev");
                }

                String fileName = "Pertanyaan_" + DateHelper.GetDateTimeNowCustomFormat("yyyyMMddHHmmss");
                String url = new String(paramFileUrl+ param2.app_name  +"/Question/" + fileName + ".xlsx");
                File createFile = new File(paramFilePath+ param2.app_name + "/Question");
                createFile.mkdirs();

                File myFile = new File(createFile.getAbsolutePath() + "/" + fileName + ".xlsx");

                outPutStream = new FileOutputStream(myFile);
                urlLink = url;
                workbook.write(outPutStream);
                outPutStream.close();
                workbook.close();
            }catch (Exception ex){
                logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param2), "", ex.toString(), user, db_name), ex);
            }
        }catch (Exception ex){
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param1), "", ex.toString(), user, db_name), ex);
        }
        return urlLink;
    }

    public static String ExportQuestionTemplate(mdlQuestion param1, mdlExportParam param2, int port, String db_name) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String sql = "";
        String urlLink = "";
        String user = param1.created_by;
        Workbook workbook = new XSSFWorkbook();
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        CachedRowSet jrs = null;
        FileOutputStream outPutStream = null;
        try {
            //== Create Workbook==\\
            String[] columns = {"Urutan",  "No", "Isi Pertanyaan", "ID Tipe Jawaban",  "ID Set Pertanyaan", "ID Kategori Pertanyaan"};

            Sheet sheet = workbook.createSheet("Pertanyaan");
            Font headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 11);
            headerFont.setColor(IndexedColors.BLACK.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }

            int rowNum = 1;
            for (int i = 0; i < 1; i++) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue("Contoh (1)");
                row.createCell(1).setCellValue("Contoh (1)");
                row.createCell(2).setCellValue("Contoh (Pertanyaan A)");
                row.createCell(3).setCellValue("Contoh (0001)");
                row.createCell(4).setCellValue("Contoh (0001)");
                row.createCell(5).setCellValue("Contoh (0001)");
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try {
                Context context = (Context) new InitialContext().lookup("java:comp/env");
                String paramFilePath = "";
                String paramFileUrl = "";
                if (port==443){
                    paramFilePath = (String) context.lookup("path_web_server_for_upload_report");
                    paramFileUrl = (String) context.lookup("url_web_server_for_upload_report");
                }else {
                    paramFilePath = (String) context.lookup("path_web_server_for_upload_report_dev");
                    paramFileUrl = (String) context.lookup("url_web_server_for_upload_report_dev");
                }
                String fileName = "Pertanyaan_" + DateHelper.GetDateTimeNowCustomFormat("yyyyMMddHHmmss");
                String url = paramFileUrl + param2.app_name + "/Question/" + fileName + ".xlsx";
                File createFile = new File(paramFilePath + param2.app_name + "/Question");
                createFile.mkdirs();

                File myFile = new File(createFile.getAbsolutePath() + "/" + fileName + ".xlsx");

                outPutStream = new FileOutputStream(myFile);
                urlLink = url;
                workbook.write(outPutStream);
                outPutStream.close();
                workbook.close();
            } catch (Exception ex) {
                logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(param2), "", ex.toString(), user, db_name), ex);
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(param1), "", ex.toString(), user, db_name), ex);
        }
        return urlLink;
    }
}
