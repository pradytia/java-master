package controller.v1;

import adapter.ImportProductPriceAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.AuthorizationAdapter;
import core.ErrorSchemaAdapter;
import core.LogAdapter;
import model.Client.mdlDB;
import model.ErrorSchema.mdlErrorSchema;
import model.Globals;
import model.Product.mdlProductPrice;
import model.Product.mdlProductPriceTyp;
import model.Query.mdlResponseQuery;
import model.Result.mdlResultFinal;
import model.Result.mdlResultSqlErrorRespon;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1")
public class ImportProductPriceController  {
    final static Logger logger = LogManager.getLogger(ImportProductPriceController.class);
    Gson gson = new GsonBuilder().serializeNulls().create();
    String db_name = "";
    int port = 0;

    @RequestMapping(value = "/import-product-price", method = RequestMethod.POST, consumes = "multipart/form-data")
    public @ResponseBody
    mdlResultFinal ImportExcelProductPrice(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestHeader String authorization,
            @RequestHeader String key,
            @RequestHeader String timestamp,
            @RequestHeader String signature,
            @RequestParam("file") final MultipartFile excelfile,
            @RequestParam("json") final String param
    ) {
        port = request.getLocalPort();
        if(port==80) port = 443;


        long startTime = System.currentTimeMillis();
        String systemFunction = Thread.currentThread().getStackTrace()[1].getMethodName();

        String apiMethod = request.getMethod();
        String url = "/import-product-price";
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();

        try {
            mdlProductPrice paramResult = gson.fromJson(param, mdlProductPrice.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, String.valueOf(param), port);
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing
            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live
//                db_name = "db_ffs"; //only-testing

                if(excelfile != null && !excelfile.isEmpty()){
                    String file_name = excelfile.getOriginalFilename();
                    String extension = FilenameUtils.getExtension(file_name);
                    Workbook workbook = null;
                    InputStream _inputStream = excelfile.getInputStream();

                    if(extension.equals("xlsx")){
                        workbook = new XSSFWorkbook(_inputStream);
                        mdlResponseQuery result =  ImportProductPriceAdapter.ImportExcelProductPrice(workbook, paramResult, db_name, port);

                        if(result.Status == true){
                            _mdlErrorSchema.error_code = "ERR-00-000";
                            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                        }else {
                            if(result.Response != null) {
                                _mdlErrorSchema.error_code = "ERR-05-001";
                            }else {
                                _mdlErrorSchema.error_code = "ERR-05-002";
                            }
                            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                            _mdlResultFinal.value = result.Response;
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
                        }

                    }else if(extension.equals("xls")){
                        workbook = new HSSFWorkbook(_inputStream);
                        mdlResponseQuery result =  ImportProductPriceAdapter.ImportExcelProductPrice(workbook, paramResult, db_name, port);

                        if(result.Status == true){
                            _mdlErrorSchema.error_code = "ERR-00-000";
                            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                        }else {
                            if(result.Response != null) {
                                _mdlErrorSchema.error_code = "ERR-05-001";
                            }else {
                                _mdlErrorSchema.error_code = "ERR-05-002";
                            }
                            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                            _mdlResultFinal.value = result.Response;
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
                        }
                    }else {
                        _mdlErrorSchema.error_code = "ERR-05-003";
                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
                    }
                } else {
                    _mdlErrorSchema.error_code = "ERR-05-002";
                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.error(LogAdapter.logToLog4j(false, startTime, 401, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
            }

        }catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            _mdlErrorSchema.error_code = "ERR-99-999";
            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
            _mdlResultFinal.value = ex.toString();
            logger.error(LogAdapter.logToLog4jException(startTime, url, apiMethod, systemFunction, jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal), ex.toString()), ex);
        }
        return _mdlResultFinal;
    }

    @RequestMapping(value = "/import-product-price-typ", method = RequestMethod.POST, consumes = "multipart/form-data")
    public @ResponseBody
    mdlResultFinal ImportExcelProductPriceTyp(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestHeader String authorization,
            @RequestHeader String key,
            @RequestHeader String timestamp,
            @RequestHeader String signature,
            @RequestParam("file") final MultipartFile excelfile,
            @RequestParam("json") final String param
    ) {
        port = request.getLocalPort();
        if(port==80) port = 443;

        long startTime = System.currentTimeMillis();
        String systemFunction = Thread.currentThread().getStackTrace()[1].getMethodName();

        String apiMethod = request.getMethod();
        String url = "/import-product-price-typ";
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();

        try {
            mdlProductPriceTyp paramResult = gson.fromJson(param, mdlProductPriceTyp.class);
            //_mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v2(authorization, key, url, timestamp, signature, apiMethod, String.valueOf(param));
            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port, Globals.ip,Globals.login,Globals.pass);

            //            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing
            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live
//                db_name = "db_ffs"; //only-testing

                if(excelfile != null && !excelfile.isEmpty()){
                    String file_name = excelfile.getOriginalFilename();
                    String extension = FilenameUtils.getExtension(file_name);
                    Workbook workbook = null;
                    InputStream _inputStream = excelfile.getInputStream();

                    if(extension.equals("xlsx")){
                        workbook = new XSSFWorkbook(_inputStream);
                        mdlResponseQuery result =  ImportProductPriceAdapter.ImportExcelProductPriceTyp(workbook, paramResult, db_name, port);

                        if(result.Status == true){
                            _mdlErrorSchema.error_code = "ERR-00-000";
                            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                        }else {
                            if(result.Response != null) {
                                _mdlErrorSchema.error_code = "ERR-05-001";
                            }else {
                                _mdlErrorSchema.error_code = "ERR-05-002";
                            }
                            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                            _mdlResultFinal.value = result.Response;
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
                        }

                    }else if(extension.equals("xls")){
                        workbook = new HSSFWorkbook(_inputStream);
                        mdlResponseQuery result =  ImportProductPriceAdapter.ImportExcelProductPriceTyp(workbook, paramResult, db_name, port);

                        if(result.Status == true){
                            _mdlErrorSchema.error_code = "ERR-00-000";
                            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                        }else {
                            if(result.Response != null) {
                                _mdlErrorSchema.error_code = "ERR-05-001";
                            }else {
                                _mdlErrorSchema.error_code = "ERR-05-002";
                            }
                            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                            _mdlResultFinal.value = result.Response;
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
                        }
                    }else {
                        _mdlErrorSchema.error_code = "ERR-05-003";
                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
                    }
                } else {
                    _mdlErrorSchema.error_code = "ERR-05-002";
                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.error(LogAdapter.logToLog4j(false, startTime, 401, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
            }

        }catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            _mdlErrorSchema.error_code = "ERR-99-999";
            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
            _mdlResultFinal.value = ex.toString();
            logger.error(LogAdapter.logToLog4jException(startTime, url, apiMethod, systemFunction, jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal), ex.toString()), ex);
        }
        return _mdlResultFinal;
    }
}
