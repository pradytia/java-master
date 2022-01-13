package controller.v1;

import adapter.ImportDistrictAdapter;
import com.google.gson.GsonBuilder;
import core.AuthorizationAdapter;
import core.ErrorSchemaAdapter;
import core.LogAdapter;
import model.Client.mdlDB;
import model.ErrorSchema.mdlErrorSchema;
import model.District.mdlDistrict;
import com.google.gson.Gson;
import model.Query.mdlResponseQuery;
import model.Result.mdlResultFinal;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1")
public class ImportDistrictController {
    final static Logger logger = LogManager.getLogger(ImportDistrictController.class);
    Gson gson = new GsonBuilder().serializeNulls().create();
    String db_name = "";
    int port = 0;

    @RequestMapping(value = "/import-district", method = RequestMethod.POST, consumes = "multipart/form-data")
    public @ResponseBody
    mdlResultFinal ImportExcelRayon(
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

        String url = "/import-district";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
        try {
            mdlDistrict paramResult = gson.fromJson(param, mdlDistrict.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, String.valueOf(param), port);
            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live

                if (excelfile != null && !excelfile.isEmpty()) {
                    String file_name = excelfile.getOriginalFilename();
                    String extension = FilenameUtils.getExtension(file_name);
                    InputStream _inputStream = excelfile.getInputStream();
                    Workbook workbook = null;

                    if (extension.equalsIgnoreCase("xlsx") || extension.equalsIgnoreCase("xls")) {
                        workbook = new XSSFWorkbook(_inputStream);
                        mdlResponseQuery result = ImportDistrictAdapter.ImportExcelDistrict(workbook, paramResult, db_name, port);

                        if (result.Status) {
                            _mdlErrorSchema.error_code = "ERR-00-000";
                            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                        } else {
                            if (result.Response != null) {
                                _mdlErrorSchema.error_code = "ERR-05-001";
                            } else {
                                _mdlErrorSchema.error_code = "ERR-05-002";
                            }
                            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                            _mdlResultFinal.value = result.Response;
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
                        }
                    } else {
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
