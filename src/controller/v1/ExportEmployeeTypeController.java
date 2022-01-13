package controller.v1;

import adapter.ExportEmployeeTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.AuthorizationAdapter;
import core.ErrorSchemaAdapter;
import core.LogAdapter;
import model.Client.mdlDB;
import model.Employee.mdlEmployeeType;
import model.ErrorSchema.mdlErrorSchema;
import model.Export.mdlExportParam;
import model.Result.mdlResultFinal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1")
public class ExportEmployeeTypeController {
    final static Logger logger = LogManager.getLogger(ExportEmployeeTypeController.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();
    String db_name = "";
    int port = 0;

    @RequestMapping(value = "/export-employee-type", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal ExportExcelEmployeeType(
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

        String apiMethod = request.getMethod();
        String url = "/export-employee-type";
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        String urlLink = "";
        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();

        try{
            port =  request.getLocalPort();
            mdlEmployeeType paramResult1 = gson.fromJson(param, mdlEmployeeType.class);
            mdlExportParam paramResult2 = gson.fromJson(param, mdlExportParam.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port);
//         _mdlResultFinal.status_code = "ERR-00-000"; //only-testing

            if(_mdlResultFinal.status_code.equals("ERR-00-000")) {
//            db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) _mdlResultFinal.value).db_id;

                urlLink = ExportEmployeeTypeAdapter.ExportEmployeeType(paramResult1, paramResult2, port, db_name);

                if(urlLink != "" || urlLink != null){
                    _mdlErrorSchema.error_code = "ERR-00-000";
                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                    _mdlResultFinal.value = urlLink;
                }else {
                    _mdlErrorSchema.error_code = "ERR-99-008";
                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "", gson.toJson(param), gson.toJson(_mdlResultFinal)));
                }

            }else{
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

    @RequestMapping(value = "/export-employee-type-template", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal ExportExcelEmplTypTemplate(
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

        String urlLink = "";
        String url = "/export-employee-type-template";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
        try {
            mdlEmployeeType paramResult1 = gson.fromJson(param, mdlEmployeeType.class);

            mdlExportParam paramResult2 = gson.fromJson(param, mdlExportParam.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port);

            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
                db_name = ((mdlDB) _mdlResultFinal.value).db_id;

                urlLink = ExportEmployeeTypeAdapter.ExportEmployeeTypeTemplate(paramResult1, paramResult2, port,  db_name);
                if (!urlLink.equals("")) {
                    _mdlErrorSchema.error_code = "ERR-00-000";
                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                    _mdlResultFinal.value = urlLink;
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
}
