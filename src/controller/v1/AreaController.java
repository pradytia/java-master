package controller.v1;

import adapter.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.AuthorizationAdapter;
import core.ErrorSchemaAdapter;
import core.LogAdapter;
import model.Area.mdlAreaParam;
import model.Client.mdlDB;
import model.DataTable.mdlDataTableResult;
import model.DataTable.mdlDataTableParam;
import model.ErrorSchema.mdlErrorSchema;
import model.Area.mdlArea;
import model.Result.mdlResultFinal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static core.LogAdapter.GetIsActiveLogApiConfig;

@RestController
@RequestMapping("/v1")
public class AreaController {
    final static Logger logger = LogManager.getLogger(AreaController.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();
    String db_name = "";
    int port = 0;

    @RequestMapping(value = "/get-area-list", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal GetArea(
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

        String url = "/get-area-list";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
        try {
            mdlDataTableParam paramResult = gson.fromJson(param, mdlDataTableParam.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port); //only-live
//        _mdlResultFinal.status_code = "ERR-00-000"; //only-testing

            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//            db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live

                List<mdlArea> _mdlAreaList = AreaAdapter.GetAreaWeb(paramResult, db_name, port);
                int _mdlAreaListTotal = AreaAdapter.GetAreaList(paramResult, db_name, port);

                mdlDataTableResult _mdlDataTableResult = new mdlDataTableResult();
                _mdlDataTableResult.records_total = _mdlAreaListTotal;
                _mdlDataTableResult.records_filtered = _mdlAreaListTotal;
                _mdlDataTableResult.draw = paramResult.draw + 1;
                _mdlDataTableResult.data = _mdlAreaList;
                _mdlErrorSchema.error_code = "ERR-00-000";
                _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                _mdlResultFinal.value = _mdlDataTableResult;

            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.error(LogAdapter.logToLog4j(false, startTime, 401, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
            }
        } catch (Exception ex) {
            _mdlErrorSchema.error_code = "ERR-99-999";
            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
            _mdlResultFinal.value = ex.toString();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error(LogAdapter.logToLog4jException(startTime, url, apiMethod, systemFunction, jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal), ex.toString()), ex);
        }
        return _mdlResultFinal;
    }

    @RequestMapping(value = "/check-area", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal GetAreaCheck(
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

        String url = "/check-area";
        String api_method = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();

        try {
            mdlArea paramResult = gson.fromJson(param, mdlArea.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, api_method, String.valueOf(param), port);
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing

            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live

                List<mdlArea> _mdlAreaList = adapter.AreaAdapter.GetAreaCheck(paramResult, db_name, port);

                if (!paramResult.area_id.equals("")) {
                    if (_mdlAreaList == null || _mdlAreaList.size() == 0) {
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

    @RequestMapping(value = "/upload-area", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal UploadAreaWeb(
            HttpServletResponse response,
            HttpServletRequest request,
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

        String url = "/upload-area";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
        //TODO 1 (UploadAreaWeb) : Param untuk InsertLogApi
        String lException = "";
        String is_error = "";
        try {
            mdlArea paramResult = gson.fromJson(param, mdlArea.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, String.valueOf(param), port);
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing

            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//            db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live

                boolean result = adapter.AreaAdapter.UploadAreaWeb(paramResult, db_name, port);
                if (result) {
                    _mdlErrorSchema.error_code = "ERR-00-000";
                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                } else {
                    _mdlErrorSchema.error_code = "ERR-99-008";
                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
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
            // TODO 3 (UploadAreaWeb) : Set Data exception untuk InsertLogApi
            lException = "master_core - " + ex.toString();
            //
            is_error = "1";
//            return _mdlResultFinal;
        }

        String core_name = "master_core";
        model.Log.mdlLogApiConfig mdlLogApiConfig = new model.Log.mdlLogApiConfig();
        mdlLogApiConfig = GetIsActiveLogApiConfig(core_name, db_name);
        //is_active ambil dari main_core
        if (is_error.equals("1") || mdlLogApiConfig.is_active == true) {

            //TODO 4 (UploadAreaWeb) : Set Data jsonOut dan status_api untuk InsertLogApi
            String status_api = response.getStatus() + " ; " + response.getBufferSize() + " B";

            //TODO 5 (UploadAreaWeb) : Input data ke model.Log.mdlInsertLog modelInsertLogParam
            model.Log.mdlInsertLog modelInsertLogParam = new model.Log.mdlInsertLog();
            modelInsertLogParam.api_name = url;
            modelInsertLogParam.fuction_name = "UploadAreaWeb";
            modelInsertLogParam.api_header = jsonHeader;
            modelInsertLogParam.api_input = param;
            modelInsertLogParam.api_output = gson.toJson(_mdlResultFinal);
            modelInsertLogParam.api_status = status_api;
            modelInsertLogParam.exception = lException;
            modelInsertLogParam.created_by = "master_core";
            modelInsertLogParam.response_time = String.valueOf(startTime);
            modelInsertLogParam.status_code = _mdlResultFinal.status_code;
            modelInsertLogParam.db = db_name;

            //TODO 6 (UploadAreaWeb) : Input data yang telah diisi ke core.LogAdapter.InsertLog
            LogAdapter.InsertLogApi(modelInsertLogParam);

        }
        return _mdlResultFinal;
    }

    @RequestMapping(value = "/delete-area",
            method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal DeleteArea(
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

        String url = "/delete-area";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
        try {
            mdlAreaParam paramResult = gson.fromJson(param, mdlAreaParam.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, String.valueOf(param), port);
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing

            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live

                boolean result = adapter.AreaAdapter.DeleteArea(paramResult, db_name, port);

                if (result) {
                    _mdlErrorSchema.error_code = "ERR-00-000";
                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                } else {
                    _mdlErrorSchema.error_code = "ERR-99-008";
                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
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

    @RequestMapping(value = "/get-dropdown-area-by-district", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal GetDropDownAreaByDistrict(
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

        String url = "/get-dropdown-area-by-district";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
        try {
            mdlAreaParam paramResult = gson.fromJson(param, mdlAreaParam.class);

            mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port);
//        mdlResultFinal.status_code = "ERR-00-000"; // only-testing
            if (mdlResultFinal.status_code.equals("ERR-00-000")) {
//            db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) mdlResultFinal.value).db_id;
                List<mdlArea> customerList;
                customerList = AreaAdapter.GetDropDownAreaByDistrict(paramResult, db_name, port);

                _mdlErrorSchema.error_code = "ERR-00-000";
                mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                mdlResultFinal.value = customerList;

            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.error(LogAdapter.logToLog4j(false, startTime, 401, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(mdlResultFinal)));
            }
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            _mdlErrorSchema.error_code = "ERR-99-999";
            mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
            mdlResultFinal.value = ex.toString();
            logger.error(LogAdapter.logToLog4jException(startTime, url, apiMethod, systemFunction, jsonHeader, gson.toJson(param), gson.toJson(mdlResultFinal), ex.toString()), ex);
        }

        return mdlResultFinal;

    }

    @RequestMapping(value = "/get-dropdown-area-multiple-by-district", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal GetDropDownAreaMultipleByDistrict(
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

        String url = "/get-dropdown-area-multiple-by-district";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema mdlErrorSchema = new mdlErrorSchema();
        try {
            mdlAreaParam paramResult = gson.fromJson(param, mdlAreaParam.class);

            mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port);
//        mdlResultFinal.status_code = "ERR-00-000"; // only-testing
            if (mdlResultFinal.status_code.equals("ERR-00-000")) {
//            db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) mdlResultFinal.value).db_id;
                List<mdlArea> customerList;
                customerList = AreaAdapter.GetDropDownAreaMultipleByDistrict(paramResult, db_name, port);

                mdlErrorSchema.error_code = "ERR-00-000";
                mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(mdlErrorSchema, port);
                mdlResultFinal.value = customerList;

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

    @RequestMapping(value = "/get-dropdown-area", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal GetDropDownArea(
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

        String url = "/get-dropdown-area";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema mdlErrorSchema = new mdlErrorSchema();

        try {
            // mdlAreaParam paramResult = gson.fromJson(param, mdlAreaParam.class);

            mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port);
//        mdlResultFinal.status_code = "ERR-00-000"; // only-testing
            if (mdlResultFinal.status_code.equals("ERR-00-000")) {
//            db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) mdlResultFinal.value).db_id;
                List<mdlArea> customerList;
                customerList = AreaAdapter.GetDropDownArea(db_name, port);

                mdlErrorSchema.error_code = "ERR-00-000";
                mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(mdlErrorSchema, port);
                mdlResultFinal.value = customerList;

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