package controller.v1;

import adapter.JenisProgramAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.AuthorizationAdapter;
import core.ErrorSchemaAdapter;
import core.LogAdapter;
import model.Client.mdlDB;
import model.DataTable.mdlDataTableParam;
import model.DataTable.mdlDataTableResult;
import model.ErrorSchema.mdlErrorSchema;
import model.JenisProgram.mdlJenisprogram;
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
public class JenisProgramController {
    final static Logger logger = LogManager.getLogger(JenisProgramController.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();
    String db_name = "";
    int port = 0;

    //Upload Jenis Program
    @RequestMapping( value = "/upload-jenis-program", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal UploadJenisProgram (
            HttpServletResponse response,
            HttpServletRequest request,
            @RequestHeader String authorization,
            @RequestHeader String key,
            @RequestHeader String timestamp,
            @RequestHeader String signature,
            @RequestBody String param){
        port = request.getLocalPort();
        if(port==80) port = 443;

        long startTime = System.currentTimeMillis();
        String systemFunction = Thread.currentThread().getStackTrace()[1].getMethodName();

        String url = "/upload-jenis-program";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();

        //TODO 1 (UploadInfoProgramPenjualan) : Param untuk InsertLogApi
        String lException = "";
        String is_error = "";
        //

        try {

            mdlJenisprogram paramResult = gson.fromJson(param, mdlJenisprogram.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, String.valueOf(param), port);
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing

            if(_mdlResultFinal.status_code.equals("ERR-00-000")){
//                db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live

                boolean result = adapter.JenisProgramAdapter.UploadJenisProgram(paramResult, db_name, port);

                if(result == true){
                    _mdlErrorSchema.error_code = "ERR-00-000";
                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
                }else {
                    _mdlErrorSchema.error_code = "ERR-99-008";
                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
                    logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
                }

            }else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.error(LogAdapter.logToLog4j(false, startTime, 401, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
            }

        }catch (Exception ex){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            _mdlErrorSchema.error_code = "ERR-99-999";
            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
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
        if(is_error.equals("1")  || mdlLogApiConfig.is_active == true) {
            //TODO 4 (UploadJenisProgram) : Set Data jsonOut dan status_api untuk InsertLogApi
            String status_api = response.getStatus() + " ; " + response.getBufferSize() + " B";
            //

            //TODO 5 (UploadJenisProgram) : Input data ke model.Log.mdlInsertLog modelInsertLogParam
            model.Log.mdlInsertLog modelInsertLogParam = new model.Log.mdlInsertLog();
            modelInsertLogParam.api_name = url;
            modelInsertLogParam.fuction_name = "UploadJenisProgram";
            modelInsertLogParam.api_header = jsonHeader;
            modelInsertLogParam.api_input = param;
            modelInsertLogParam.api_output = gson.toJson(_mdlResultFinal);
            modelInsertLogParam.api_status = status_api;
            modelInsertLogParam.exception = lException;
            modelInsertLogParam.created_by = "master_core";
            modelInsertLogParam.response_time = String.valueOf(startTime);
            modelInsertLogParam.status_code = _mdlResultFinal.status_code;
            modelInsertLogParam.db = db_name;
            //

            //TODO 6 (UploadInfoProgramPenjualan) : Input data yang telah diisi ke core.LogAdapter.InsertLog
            LogAdapter.InsertLogApi(modelInsertLogParam);
        }

        return _mdlResultFinal;
    }


    //GetjenisprogramList
    @RequestMapping(value = "/get-jenis-program-list", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal GetJenisProgram(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestHeader String authorization,
            @RequestHeader String key,
            @RequestHeader String timestamp,
            @RequestHeader String signature,
            @RequestBody String param
    ){
        port = request.getLocalPort();
        if(port==80) port = 443;

        long startTime = System.currentTimeMillis();
        String systemFunction = Thread.currentThread().getStackTrace()[1].getMethodName();

        String url = "/get-jenis-program-list";
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

            if(_mdlResultFinal.status_code.equals("ERR-00-000")) {
//            db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live


                int _mdlJenisProgramTotalList = JenisProgramAdapter.GetJenisProgramList(paramResult, db_name, port);
                List<mdlJenisprogram> _mdlJenisProgramList =JenisProgramAdapter.GetJenisProgramWeb(paramResult, db_name, port);

                mdlDataTableResult _mdlDataTableResult = new mdlDataTableResult();
                _mdlDataTableResult.records_total = _mdlJenisProgramTotalList;
                _mdlDataTableResult.records_filtered = _mdlJenisProgramTotalList;
                _mdlDataTableResult.draw = paramResult.draw + 1;
                _mdlDataTableResult.data = _mdlJenisProgramList;

                _mdlErrorSchema.error_code = "ERR-00-000";
                _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                _mdlResultFinal.value = _mdlDataTableResult;



            }
            else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.error(LogAdapter.logToLog4j(false, startTime, 401, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
            }

        }catch (Exception ex) {
            _mdlErrorSchema.error_code = "ERR-99-999";
            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
            _mdlResultFinal.value = ex.toString();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error(LogAdapter.logToLog4jException(startTime, url, apiMethod, systemFunction, jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal), ex.toString()), ex);
        }
        return _mdlResultFinal;
    }

    //Delete Jenis Program
    @RequestMapping( value = "/delete-jenis-program", method = RequestMethod.POST)
    public @ResponseBody mdlResultFinal DeleteJenisProgram (
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestHeader String authorization,
            @RequestHeader String key,
            @RequestHeader String timestamp,
            @RequestHeader String signature,
            @RequestBody String param){
        port = request.getLocalPort();
        if(port==80) port = 443;

        long startTime = System.currentTimeMillis();
        String systemFunction = Thread.currentThread().getStackTrace()[1].getMethodName();

        String url = "/delete-jenis-program";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();

        try {

            mdlJenisprogram paramResult = gson.fromJson(param, mdlJenisprogram.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, String.valueOf(param), port);
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing

            if(_mdlResultFinal.status_code.equals("ERR-00-000")){
//                db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live

                boolean result = adapter.JenisProgramAdapter.DeleteJenisProgram(paramResult, db_name, port);

                if (result) {
                    _mdlErrorSchema.error_code = "ERR-00-000";
                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                } else {
                    _mdlErrorSchema.error_code = "ERR-99-008";
                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
                }

            }else {
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

    @RequestMapping(value = "/get-dropdown-jenis-program", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal GetDropDownJenisProgram(
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

        String url = "/get-dropdown-jenis-program";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema mdlErrorSchema = new mdlErrorSchema();

        try {
            mdlJenisprogram paramResult = gson.fromJson(param, mdlJenisprogram.class);

            mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port);
//        mdlResultFinal.status_code = "ERR-00-000"; // only-testing
            if (mdlResultFinal.status_code.equals("ERR-00-000")) {
//            db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) mdlResultFinal.value).db_id;
                List<mdlJenisprogram> jenisprogramList = JenisProgramAdapter.GetDropDownJenisProgram(db_name, port);

                mdlErrorSchema.error_code = "ERR-00-000";
                mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(mdlErrorSchema, port);
                mdlResultFinal.value = jenisprogramList;

            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.error(LogAdapter.logToLog4j(false, startTime, 401, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(mdlResultFinal)));
            }

        }catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mdlErrorSchema.error_code = "ERR-99-999";
            mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(mdlErrorSchema, port);
            mdlResultFinal.value = ex.toString();
            logger.error(LogAdapter.logToLog4jException(startTime, url, apiMethod, systemFunction, jsonHeader, gson.toJson(param), gson.toJson(mdlResultFinal), ex.toString()), ex);
        }
        return mdlResultFinal;

    }
}
