package controller.v1;

import adapter.EmployeeImageAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.AuthorizationAdapter;
import core.ErrorSchemaAdapter;
import core.LogAdapter;
import helper.DateHelper;
import helper.FileHelper;
import model.Client.mdlDB;
import model.DataTable.mdlDataTableParam;
import model.DataTable.mdlDataTableResult;
import model.Employee.mdlEmployeeImage;
import model.ErrorSchema.mdlErrorSchema;
import model.Globals;
import model.Result.mdlResultFinal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1")
public class EmployeeImageController {
    final static Logger logger = LogManager.getLogger(EmployeeImageController.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();
    String db_name = "";
    int port = 0;

    @RequestMapping(value = "/get-employee-image-list", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal GetEmployeeImage(
            @RequestHeader String authorization,
            @RequestHeader String key,
            @RequestHeader String timestamp,
            @RequestHeader String signature,
            @RequestBody String param,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        port = request.getLocalPort();
        if(port==80) port = 443;

        long startTime = System.currentTimeMillis();
        String systemFunction = Thread.currentThread().getStackTrace()[1].getMethodName();

        String url = "/get-employee-image-list";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
        try {
            mdlDataTableParam paramResult = gson.fromJson(param, mdlDataTableParam.class);

            mdlEmployeeImage paramResult2 = gson.fromJson(param, mdlEmployeeImage.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port); //only-live
//        _mdlResultFinal.status_code = "ERR-00-000"; //only-testing

            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//            db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live

                List<mdlEmployeeImage> _mdlEmployeeImageList = EmployeeImageAdapter.GetEmployeeImageWithPaging(paramResult, paramResult2, db_name, port);
                int _mdlEmployeeImageListTotal = EmployeeImageAdapter.GetEmployeeImageTotalList(paramResult, paramResult2, db_name, port);

                mdlDataTableResult _mdlDataTableResult = new mdlDataTableResult();

                _mdlDataTableResult.records_total = _mdlEmployeeImageListTotal;
                _mdlDataTableResult.records_filtered = _mdlEmployeeImageListTotal;
                _mdlDataTableResult.draw = paramResult.draw + 1;
                _mdlDataTableResult.data = _mdlEmployeeImageList;

                _mdlErrorSchema.error_code = "ERR-00-000";
                _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                _mdlResultFinal.value = _mdlDataTableResult;

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

    @RequestMapping(value = "/upload-employee-image", method = RequestMethod.POST, consumes = "multipart/form-data")
    public @ResponseBody
    mdlResultFinal UploadEmployeeImages(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestHeader String authorization,
            @RequestHeader String key,
            @RequestHeader String timestamp,
            @RequestHeader String signature,
            @RequestParam("file") final MultipartFile image,
            @RequestParam("json") final String param) {
        port = request.getLocalPort();
        if(port==80) port = 443;

        long startTime = System.currentTimeMillis();
        String systemFunction = Thread.currentThread().getStackTrace()[1].getMethodName();

        String url = "/upload-employee-image";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema mdlErrorSchema = new mdlErrorSchema();
        try {
            port =  request.getLocalPort() ;

            boolean isValid = true;
            String StorageDir = "";
            String finalimageName = "";

            mdlEmployeeImage paramResult = gson.fromJson(param, mdlEmployeeImage.class);

            mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port);

            if (mdlResultFinal.status_code.equals("ERR-00-000")) {
                db_name = ((mdlDB) mdlResultFinal.value).db_id; //only-live
                if (image != null) {
                    try {
                        Context context = (Context) new InitialContext().lookup("java:comp/env");
                        String _app_name = ((mdlDB) mdlResultFinal.value).app_name;
                        if (port==443){
                            StorageDir = (String) context.lookup("url_web_server_for_upload_employee_image") + _app_name + "/";
                        }else {
                            StorageDir = (String) context.lookup("url_web_server_for_upload_employee_image_dev") + _app_name + "/";
                        }

                        String fileName = image.getOriginalFilename();
                        int IndexOf = fileName.indexOf(".");
                        String domainName = fileName.substring(IndexOf);
                        finalimageName = paramResult.employee_id + "_" + DateHelper.GetDateTimeNowCustomFormat("yyyyMMddHHmmss") + domainName;
                        String uploadedFileLocation = StorageDir + finalimageName;

                        FileHelper.writeToFile(image.getInputStream(), StorageDir, uploadedFileLocation);
                        isValid = EmployeeImageAdapter.UploadEmployeeImage(paramResult, StorageDir, finalimageName, db_name, port);
                    } catch (Exception e) {
                        isValid = false;
                        logger.error(LogAdapter.logToLog4jException(startTime, url, apiMethod, systemFunction, jsonHeader, gson.toJson(param), gson.toJson(mdlResultFinal), e.toString()));
                    }
                }

                if (!isValid) {
                    mdlErrorSchema.error_code = "ERR-99-008";
                    mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(mdlErrorSchema, port);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "", gson.toJson(param), gson.toJson(mdlResultFinal)));
                } else {
                    mdlErrorSchema.error_code = "ERR-00-000";
                    mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(mdlErrorSchema, port);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.error(LogAdapter.logToLog4j(false, startTime, 401, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(mdlResultFinal)));
            }
        } catch (Exception ex) {
            mdlErrorSchema.error_code = "ERR-99-999";
            mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(mdlErrorSchema, port);
            mdlResultFinal.value = ex.toString();
            logger.error(LogAdapter.logToLog4jException(startTime, url, apiMethod, systemFunction, jsonHeader, gson.toJson(param), gson.toJson(mdlResultFinal), ex.toString()), ex);
        }
        return mdlResultFinal;
    }

    @RequestMapping(value = "/delete-employee-image", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal DeleteEmployeeImage(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestHeader String authorization,
            @RequestHeader String key,
            @RequestHeader String timestamp,
            @RequestHeader String signature,
            @RequestBody String param
    ) {
        port = request.getLocalPort();
        if(port==80) port = 443;

        long startTime = System.currentTimeMillis();
        String systemFunction = Thread.currentThread().getStackTrace()[1].getMethodName();

        String url = "/delete-employee-image";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
        try {
            mdlEmployeeImage paramResult = gson.fromJson(param, mdlEmployeeImage.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port);
            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {

                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live

                boolean result = EmployeeImageAdapter.DeleteEmployeeImage(paramResult, db_name, port);
                if (result) {
                    _mdlErrorSchema.error_code = "ERR-00-000";
                } else {
                    _mdlErrorSchema.error_code = "ERR-99-008";
                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "", gson.toJson(param), gson.toJson(_mdlResultFinal)));
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

    @RequestMapping(value = "/upload-employee-image-noimg", method = RequestMethod.POST, consumes = "multipart/form-data")
    public @ResponseBody
    mdlResultFinal UploadEmployeeImagesNoImg(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestHeader String authorization,
            @RequestHeader String key,
            @RequestHeader String timestamp,
            @RequestHeader String signature,
            //@RequestParam("file") final MultipartFile image,
            @RequestParam("json") final String param) {
        port = request.getLocalPort();
        if(port==80) port = 443;

        long startTime = System.currentTimeMillis();
        String systemFunction = Thread.currentThread().getStackTrace()[1].getMethodName();

        String url = "/upload-employee-image-noimg";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema mdlErrorSchema = new mdlErrorSchema();
        try {
            boolean result = false;
            mdlEmployeeImage paramResult = gson.fromJson(param, mdlEmployeeImage.class);

            mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port);
//            mdlResultFinal.status_code = "ERR-00-000";
            if (mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffs";
                db_name = ((mdlDB) mdlResultFinal.value).db_id; //only-live
                result = EmployeeImageAdapter.UploadEmployeeImageNoImg(paramResult, db_name, port);

                if (result == false) {
                    mdlErrorSchema.error_code = "ERR-99-008";
                    mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(mdlErrorSchema, port, Globals.ip, Globals.login,Globals.pass);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "", gson.toJson(param), gson.toJson(mdlResultFinal)));
                } else {
                    mdlErrorSchema.error_code = "ERR-00-000";
                    mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(mdlErrorSchema, port, Globals.ip, Globals.login,Globals.pass);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.error(LogAdapter.logToLog4j(false, startTime, 401, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(mdlResultFinal)));
            }
        } catch (Exception ex) {
            mdlErrorSchema.error_code = "ERR-99-999";
            mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(mdlErrorSchema, port);
            mdlResultFinal.value = ex.toString();
            logger.error(LogAdapter.logToLog4jException(startTime, url, apiMethod, systemFunction, jsonHeader, gson.toJson(param), gson.toJson(mdlResultFinal), ex.toString()), ex);
        }
        return mdlResultFinal;
    }
}
