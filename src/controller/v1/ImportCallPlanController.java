package controller.v1;

import adapter.ImportCallPlanAdapter;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import core.AuthorizationAdapter;
import core.ErrorSchemaAdapter;
import core.LogAdapter;
import model.Callplan.mdlCallplan;
import com.google.gson.Gson;
import model.Client.mdlDB;
import model.ErrorSchema.mdlErrorSchema;
import model.Query.mdlResponseQuery;
import model.Result.mdlResult;
import model.Result.mdlResultFinal;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1")
public class ImportCallPlanController {
    final static Logger logger = LogManager.getLogger(ImportCallPlanController.class);
    Gson gson = new GsonBuilder().serializeNulls().create();
    String db_name = "";
    int port = 0;

    @RequestMapping(value = "/import-call-plan", method = RequestMethod.POST, consumes = "multipart/form-data")
    public @ResponseBody
    mdlResultFinal ImportExcelCallPlan(
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

        String url = "/import-call-plan";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
        try {
            mdlCallplan paramResult = gson.fromJson(param, mdlCallplan.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, String.valueOf(param), port);
//            _mdlResultFinal.statusCode = "ERR-00-000"; //only-testing

            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//            db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live

                if (excelfile != null && !excelfile.isEmpty()) {
                    String fileName = excelfile.getOriginalFilename();
                    String ext = FilenameUtils.getExtension(fileName);

                    mdlResponseQuery result = ImportCallPlanAdapter.ImportExcelCallPlan(excelfile, ext, paramResult, db_name, port);
                    if (result.Status) {
                        _mdlErrorSchema.error_code = "ERR-00-000";
                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                    } else {
                        _mdlErrorSchema.error_code = "ERR-99-008";
                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
                        _mdlResultFinal.value = ((SQLException) result.Response).getMessage();
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
                    }
                } else {
                    _mdlErrorSchema.error_code = "ERR-99-999";
                    _mdlResultFinal.status_message_eng = "Excel File Empty";
                    _mdlResultFinal.status_message_ind = "File Excel Kosong";
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.error(LogAdapter.logToLog4j(false, startTime, 401, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
            }
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            _mdlErrorSchema.error_code = "ERR-99-999";
            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
            _mdlResultFinal.value = ex.toString();
            logger.error(LogAdapter.logToLog4jException(startTime, url, apiMethod, systemFunction, jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal), ex.toString()), ex);
        }
        return _mdlResultFinal;
    }

    @RequestMapping(value = "/import-call-plan-v2", method = RequestMethod.POST, consumes = "multipart/form-data")
    public @ResponseBody
    mdlResultFinal ImportExcelCallPlanV2(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestHeader String authorization,
            @RequestHeader String key,
            @RequestHeader String timestamp,
            @RequestHeader String signature,
            @RequestParam("file") final MultipartFile excelfile,
            @RequestParam("json") final String param
    ) {
        long startTime = System.currentTimeMillis();
        String systemFunction = Thread.currentThread().getStackTrace()[1].getMethodName();

        String url = "/import-call-plan-v2";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
        try {
            mdlCallplan paramResult = gson.fromJson(param, mdlCallplan.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, String.valueOf(param), port);
            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
             db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live
                if (excelfile != null && !excelfile.isEmpty()) {
                    String fileName = excelfile.getOriginalFilename();
                    String ext = FilenameUtils.getExtension(fileName);

                    mdlResponseQuery result = ImportCallPlanAdapter.ImportExcelCallPlanV3(excelfile, ext, paramResult, db_name, port);
                    if (result.Status) {
                        _mdlErrorSchema.error_code = "ERR-00-000";
                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                    } else if (result.Status == false) {
                        _mdlErrorSchema.error_code = "ERR-99-999";
                        _mdlResultFinal.status_message_eng = "Status customer is not active";
                        _mdlResultFinal.status_message_ind = "Status pelanggan tidak aktif";
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    }else{
                        _mdlErrorSchema.error_code = "ERR-99-008";
                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                        _mdlResultFinal.value = ((SQLException) result.Response).getMessage();
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
                    }
                } else {
                    _mdlErrorSchema.error_code = "ERR-99-999";
                    _mdlResultFinal.status_message_eng = "Excel File Empty";
                    _mdlResultFinal.status_message_ind = "File Excel Kosong";
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.error(LogAdapter.logToLog4j(false, startTime, 401, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
            }

        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            _mdlErrorSchema.error_code = "ERR-99-999";
            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
            _mdlResultFinal.value = ex.toString();
            logger.error(LogAdapter.logToLog4jException(startTime, url, apiMethod, systemFunction, jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal), ex.toString()), ex);
        }
        return _mdlResultFinal;
    }

}
