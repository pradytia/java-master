package controller.v1;

import adapter.ExportQuestionAdapter;
import adapter.ExportQuestionSetAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.AuthorizationAdapter;
import core.ErrorSchemaAdapter;
import core.LogAdapter;
import model.Client.mdlDB;
import model.ErrorSchema.mdlErrorSchema;
import model.Export.mdlExportParam;
import model.Question.mdlQuestionSet;
import model.Result.mdlResultFinal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1")
public class ExportQuestionSetController {
    final static Logger logger = LogManager.getLogger(ExportQuestionSetController.class);
    Gson gson = new GsonBuilder().serializeNulls().create();
    String db_name = "";
    int port = 0;

    @RequestMapping(value = "/export-question-set", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal ExportExcelQuestionSet(
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
        String url = "/export-question-set";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();

        try{
            mdlQuestionSet paramResult1;
            paramResult1 = gson.fromJson(param, mdlQuestionSet.class);

            mdlExportParam paramResult2;
            paramResult2 = gson.fromJson(param, mdlExportParam.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port);
//         _mdlResultFinal.status_code = "ERR-00-000"; //only-testing

            port =  request.getLocalPort();
            if(_mdlResultFinal.status_code.equals("ERR-00-000")) {
//            db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) _mdlResultFinal.value).db_id;

                urlLink = ExportQuestionSetAdapter.ExportQuestionSet(paramResult1, paramResult2, port, db_name);

                if(urlLink != "" || urlLink != null){
                    _mdlErrorSchema.error_code = "ERR-00-000";
                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                    _mdlResultFinal.value = urlLink;
                }else {
                    _mdlErrorSchema.error_code = "ERR-99-008";
                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                    _mdlResultFinal.value =  null;
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

    @RequestMapping(value = "/export-question-set-template", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal ExportExcelCostTemplate(
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
        String url = "/export-question-set-template";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
        try {
            port = request.getLocalPort();
            mdlQuestionSet paramResult1 = gson.fromJson(param, mdlQuestionSet.class);

            mdlExportParam paramResult2 = gson.fromJson(param, mdlExportParam.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port);

            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
                db_name = ((mdlDB) _mdlResultFinal.value).db_id;

                urlLink = ExportQuestionSetAdapter.ExportQuestionSetTemplate(paramResult1, paramResult2, port,  db_name);
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
