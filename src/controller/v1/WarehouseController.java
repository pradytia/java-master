package controller.v1;

import adapter.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.AuthorizationAdapter;
import core.ErrorSchemaAdapter;
import core.LogAdapter;
import model.Client.mdlDB;
import model.DataTable.mdlDataTableResult;
import model.DataTable.mdlDataTableParam;
import model.ErrorSchema.mdlErrorSchema;
import model.Result.mdlResultFinal;
import model.Warehouse.mdlWarehouse;
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

@RestController
@RequestMapping("/v1")
public class WarehouseController {

    final static Logger logger = LogManager.getLogger(WarehouseController.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();
    String db_name = "";
    int port = 0;

    @RequestMapping(value = "/get-warehouse-list", method = RequestMethod.POST)
    public @ResponseBody mdlResultFinal GetWarehouse(
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

        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
        String url = "/get-warehouse-list";

        try {
            mdlDataTableParam paramResult;
            paramResult = gson.fromJson(param, mdlDataTableParam.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port);

            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
                db_name = ((mdlDB) _mdlResultFinal.value).db_id;

                List<mdlWarehouse> _mdlWarehouseList = WarehouseAdapter.GetWarehouseWeb(paramResult, db_name, port);
                int _mdlWarehouseTotal = WarehouseAdapter.GetWarehouseList(paramResult, db_name, port);

                mdlDataTableResult _mdlDataTableResult = new mdlDataTableResult();

                    _mdlDataTableResult.records_total = _mdlWarehouseTotal;
                    _mdlDataTableResult.records_filtered = _mdlWarehouseTotal;
                    _mdlDataTableResult.draw = paramResult.draw + 1;
                    _mdlDataTableResult.data = _mdlWarehouseList;

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
}