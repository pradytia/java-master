package controller.v1;

import adapter.ECatalogAdapter;
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
import model.District.mdlDistrict;
import model.ECatalog.mdlECatalog;
import model.ErrorSchema.mdlErrorSchema;
import model.Result.mdlResultFinal;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1")
public class ECatalogController {
    final static Logger logger = LogManager.getLogger(ECatalogController.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();
    String db_name = "";
    int port = 0;

    @RequestMapping(value = "/check-e-catalog", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal GetECatalogCheck(
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

        String url = "/check-e-catalog";
        String api_method = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
        try {
            mdlECatalog paramResult = gson.fromJson(param, mdlECatalog.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, api_method, String.valueOf(param), port);
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing

            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live

                List<mdlECatalog> _mdlECatalogList = adapter.ECatalogAdapter.GetECatalogheck(paramResult, db_name, port);
                if (!paramResult.e_catalog_id.equals("")) {
                    if (_mdlECatalogList.size() == 0) {
                        _mdlErrorSchema.error_code = "ERR-00-000";
                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                        _mdlResultFinal.value = false;
                    } else {
                        _mdlErrorSchema.error_code = "ERR-00-000";
                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                        _mdlResultFinal.value = true;
                    }
                } else {
                    _mdlErrorSchema.error_code = "ERR-99-008";
                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, api_method, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.error(LogAdapter.logToLog4j(false, startTime, 401, url, api_method, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
            }
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            _mdlErrorSchema.error_code = "ERR-99-999";
            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
            _mdlResultFinal.value = ex.toString();
            logger.error(LogAdapter.logToLog4jException(startTime, url, api_method, systemFunction, jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal), ex.toString()), ex);
        }
        return _mdlResultFinal;
    }

    @RequestMapping(value = "/get-e-catalog", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal GetECatalog(@RequestHeader String authorization,
                               @RequestHeader String key,
                               @RequestHeader String timestamp,
                               @RequestHeader String signature,
                               HttpServletRequest request,
                               HttpServletResponse response,
                               @RequestBody String param) {
        port = request.getLocalPort();
        if(port==80) port = 443;

        long startTime = System.currentTimeMillis();
        String systemFunction = Thread.currentThread().getStackTrace()[1].getMethodName();

        String url = "/get-e-catalog";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
        try {
            mdlDataTableParam paramResult = gson.fromJson(param, mdlDataTableParam.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port);
            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live
                List<mdlECatalog> _ECatalogList = adapter.ECatalogAdapter.GetECatalog(paramResult, db_name, port);
                int dataListTotal = ECatalogAdapter.GetTotalECatalog(paramResult, db_name, port);


                mdlDataTableResult _mdlDataTableResult = new mdlDataTableResult();

                _mdlDataTableResult.records_total = dataListTotal;
                _mdlDataTableResult.records_filtered = dataListTotal;
                _mdlDataTableResult.draw = paramResult.draw + 1;
                _mdlDataTableResult.data = _ECatalogList;

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

    @RequestMapping(value = "/upload-e-catalog", method = RequestMethod.POST, consumes = "multipart/form-data")
    public @ResponseBody
    mdlResultFinal InsertECatalog(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestHeader String authorization,
            @RequestHeader String key,
            @RequestHeader String timestamp,
            @RequestHeader String signature,
            @RequestParam("file") final MultipartFile file,
            @RequestParam("json") final String param) {
        port = request.getLocalPort();
        if(port==80) port = 443;

        long startTime = System.currentTimeMillis();
        String systemFunction = Thread.currentThread().getStackTrace()[1].getMethodName();

        String url = "/upload-e-catalog";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema mdlErrorSchema = new mdlErrorSchema();
        try {
            boolean isValid = true;

            mdlECatalog paramResult = gson.fromJson(param, mdlECatalog.class);

            mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port);

            if (mdlResultFinal.status_code.equals("ERR-00-000")) {

                db_name = ((mdlDB) mdlResultFinal.value).db_id;

                String app_name = ((mdlDB) mdlResultFinal.value).app_name;

                Date currentDate = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(currentDate);  //use java.util.Date object as arguement
                // get the value of all the calendar date fields.

                String imageYear = String.valueOf(cal.get(Calendar.YEAR));
                String imageMonth = String.valueOf(cal.get(Calendar.MONTH) + 1);
                String imageDay = String.valueOf(cal.get(Calendar.DATE));

                String folderDir = app_name + "/" + imageYear + "/" + imageMonth + "/" + imageDay + "/";
                if (file != null) {
                    String fileName = file.getOriginalFilename();
                    String ext = FilenameUtils.getExtension(fileName);
                    if (!ext.isEmpty() && (ext.equals("pdf"))) {
                        try {
                            mdlECatalog _mdlMdlECatalogImage = new mdlECatalog();

                            Context context = (Context) new InitialContext().lookup("java:comp/env");
                            StringBuilder sb = new StringBuilder();

                            String paramFilePath = (String) context.lookup("path_web_server_for_upload");
                            String directory = sb.append(paramFilePath).append(folderDir).toString();
                            String image_time_string = "ECT" + DateHelper.GetDateTimeNowCustomFormat("yyyyMMddHHmmssSSS");
                            String uploadedFileLocation = sb.append(image_time_string).append(".").append(ext).toString();

                            FileHelper.writeToFile(file.getInputStream(), directory, uploadedFileLocation);

                            String urlWebServer = (String) context.lookup("url_web_server_for_upload");
                            String _file_name = image_time_string + "." + "pdf";
                            _mdlMdlECatalogImage.e_catalog_id = paramResult.e_catalog_id;
                            _mdlMdlECatalogImage.title = paramResult.title;
                            _mdlMdlECatalogImage.file_name = _file_name;
                            _mdlMdlECatalogImage.path = urlWebServer + folderDir;
                            _mdlMdlECatalogImage.type_of_file = ext;
                            _mdlMdlECatalogImage.created_by = paramResult.created_by;

                            //Insert Data File E-Catalog
                            boolean status = ECatalogAdapter.InsertECatalog(_mdlMdlECatalogImage, db_name, port);
                            if (!status) {
                                isValid = false;
                            }
                        } catch (Exception e) {
                            isValid = false;
                            logger.error(LogAdapter.logToLog4jException(startTime, url, apiMethod, systemFunction, jsonHeader, gson.toJson(param), gson.toJson(mdlResultFinal), e.toString()));
                        }
                    } else {
                        isValid = false;
                    }
                }
                if (!isValid) {
                    mdlErrorSchema.error_code = "ERR-99-008";
                    mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(mdlErrorSchema, port);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(mdlResultFinal)));
                } else {
                    mdlErrorSchema.error_code = "ERR-00-000";
                    mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(mdlErrorSchema, port);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.error(LogAdapter.logToLog4j(false, startTime, 401, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(mdlResultFinal)));
            }
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mdlErrorSchema.error_code = "ERR-99-999";
            mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(mdlErrorSchema, port);
            mdlResultFinal.value = ex.toString();
            logger.error(LogAdapter.logToLog4jException(startTime, url, apiMethod, systemFunction, jsonHeader, gson.toJson(param), gson.toJson(mdlResultFinal), ex.toString()), ex);
        }
        return mdlResultFinal;
    }

    @RequestMapping(value = "/upload-e-catalog-no-file", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal InsertECatalogNoFile(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestHeader String authorization,
            @RequestHeader String key,
            @RequestHeader String timestamp,
            @RequestHeader String signature,
            @RequestBody String param) {
        port = request.getLocalPort();
        if(port==80) port = 443;

        long startTime = System.currentTimeMillis();
        String systemFunction = Thread.currentThread().getStackTrace()[1].getMethodName();

        String url = "/upload-e-catalog-no-file";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema mdlErrorSchema = new mdlErrorSchema();
        try {
            mdlECatalog paramResult = gson.fromJson(param, mdlECatalog.class);

            mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port);
            if (mdlResultFinal.status_code.equals("ERR-00-000")) {

                db_name = ((mdlDB) mdlResultFinal.value).db_id;
                boolean status = ECatalogAdapter.InsertECatalog(paramResult, db_name, port);
                if (status) {
                    mdlErrorSchema.error_code = "ERR-00-000";
                    mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(mdlErrorSchema, port);
                } else {
                    mdlErrorSchema.error_code = "ERR-99-008";
                    mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(mdlErrorSchema, port);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(mdlResultFinal)));
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.error(LogAdapter.logToLog4j(false, startTime, 401, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(mdlResultFinal)));
            }
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mdlErrorSchema.error_code = "ERR-99-999";
            mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(mdlErrorSchema, port);
            mdlResultFinal.value = ex.toString();
            logger.error(LogAdapter.logToLog4jException(startTime, url, apiMethod, systemFunction, jsonHeader, gson.toJson(param), gson.toJson(mdlResultFinal), ex.toString()), ex);
        }
        return mdlResultFinal;
    }

    @RequestMapping(value = "/delete-e-catalog", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal DeleteECatalog(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestHeader String authorization,
            @RequestHeader String key,
            @RequestHeader String timestamp,
            @RequestHeader String signature,
            @RequestBody String param) {
        port = request.getLocalPort();
        if(port==80) port = 443;

        long startTime = System.currentTimeMillis();
        String systemFunction = Thread.currentThread().getStackTrace()[1].getMethodName();

        String url = "/delete-e-catalog";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema mdlErrorSchema = new mdlErrorSchema();
        try {
            mdlECatalog paramResult = gson.fromJson(param, mdlECatalog.class);

            mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port);

            if (mdlResultFinal.status_code.equals("ERR-00-000")) {
//            db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) mdlResultFinal.value).db_id;
                boolean status = ECatalogAdapter.DeleteECatalog(paramResult, db_name, port);
                if (status) {
                    mdlErrorSchema.error_code = "ERR-00-000";
                    mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(mdlErrorSchema, port);
                } else {
                    mdlErrorSchema.error_code = "ERR-99-008";
                    mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(mdlErrorSchema, port);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(mdlResultFinal)));
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.error(LogAdapter.logToLog4j(false, startTime, 401, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(mdlResultFinal)));
            }
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mdlErrorSchema.error_code = "ERR-99-999";
            mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(mdlErrorSchema, port);
            mdlResultFinal.value = ex.toString();
            logger.error(LogAdapter.logToLog4jException(startTime, url, apiMethod, systemFunction, jsonHeader, gson.toJson(param), gson.toJson(mdlResultFinal), ex.toString()), ex);
        }
        return mdlResultFinal;
    }
}
