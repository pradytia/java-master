package controller.v1;

import adapter.SbbAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.AuthorizationAdapter;
import core.ErrorSchemaAdapter;
import core.LogAdapter;
import model.Client.mdlDB;
import model.DataTable.mdlDataTableParam;
import model.DataTable.mdlDataTableResult;
import model.ErrorSchema.mdlErrorSchema;
import model.Result.mdlResultFinal;
import model.Sbb.mdlSbb;
import model.Sbb.mdlSbbUomQty;

import model.Stock.mdlStockResult;
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
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_OK;

@RestController
@RequestMapping("/v1")
public class SbbController {

    final static Logger logger = LogManager.getLogger(SbbController.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();
    String db_name = "";
    int port = 0;

    //Upload SBB
    @RequestMapping(value = "/upload-sbb", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal UploadSbb(
            HttpServletResponse response,
            HttpServletRequest request,
            @RequestHeader String authorization,
            @RequestHeader String key,
            @RequestHeader String timestamp,
            @RequestHeader String signature,
            @RequestBody String param) {
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

        String url =  "/upload-sbb";

        //TODO 1 (UploadAreaWeb) : Param untuk InsertLogApi
        String lException = "";
        String is_error = "";
        try {
            mdlSbb paramResult;
            paramResult = gson.fromJson(param, mdlSbb.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, String.valueOf(param), port);
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing

            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live

                boolean result = adapter.SbbAdapter.UploadSbb(paramResult, db_name, port);

                if(result == true){
                    _mdlErrorSchema.error_code = "ERR-00-000";
                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                    _mdlResultFinal.value =  null;
                    response.setStatus(SC_OK);
                      }else {
                    _mdlErrorSchema.error_code = "ERR-99-008";
                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                    _mdlResultFinal.value =  null;
                    response.setStatus(SC_BAD_REQUEST);
                    logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
                }
            }else{
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.error(LogAdapter.logToLog4j(false, startTime, 401, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
            }


        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            _mdlErrorSchema.error_code = "ERR-99-999";
            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
            _mdlResultFinal.value = ex.toString();
            logger.error(LogAdapter.logToLog4jException(startTime, url, apiMethod, systemFunction, jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal), ex.toString()), ex);
            // TODO 3 (UploadSbb) : Set Data exception untuk InsertLogApi
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
            //TODO 4 (UploadSbb) : Set Data jsonOut dan status_api untuk InsertLogApi
            String status_api = String.valueOf(response.getStatus()) + " ; " + String.valueOf(response.getBufferSize()) + " B";
            //

            //TODO 5 (UploadSbb) : Input data ke model.Log.mdlInsertLog modelInsertLogParam
            model.Log.mdlInsertLog modelInsertLogParam = new model.Log.mdlInsertLog();
            modelInsertLogParam.api_name = url;
            modelInsertLogParam.fuction_name = "UploadSbb";
            modelInsertLogParam.api_header = jsonHeader;
            modelInsertLogParam.api_input = gson.toJson(param);
            modelInsertLogParam.api_output = gson.toJson(_mdlResultFinal);
            modelInsertLogParam.api_status = status_api;
            modelInsertLogParam.exception = lException;
            modelInsertLogParam.created_by = "master_core";
            modelInsertLogParam.response_time = String.valueOf(startTime);
            modelInsertLogParam.status_code = _mdlResultFinal.status_code;
            modelInsertLogParam.db = db_name;
            //

            //TODO 6 (UploadSbb) : Input data yang telah diisi ke core.LogAdapter.InsertLog
            core.LogAdapter.InsertLogApi(modelInsertLogParam);
            //
        }
        return _mdlResultFinal;
    }

    //Upload SBB
    @RequestMapping(value = "/upload-sbb-qty", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal UploadSbbQty(
            HttpServletResponse response,
            HttpServletRequest request,
            @RequestHeader String authorization,
            @RequestHeader String key,
            @RequestHeader String timestamp,
            @RequestHeader String signature,
            @RequestBody String param) {
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

        String url = "/upload-sbb-qty";

        //TODO 1 (UploadAreaWeb) : Param untuk InsertLogApi
        String lException = "";
        String is_error = "";

        try {
            mdlSbbUomQty paramResult;
            paramResult = gson.fromJson(param, mdlSbbUomQty.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, String.valueOf(param), port);
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing

            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live

                mdlStockResult _mdlStockResult = new mdlStockResult();
                mdlStockResult dataCurrent = null;
                int currentStock = 0;
                int konversiStock = 0;
                int konversiSbb = 0;
                int resultStockDetail = 0;

                String uom = "";

                dataCurrent = SbbAdapter.GetMasterStockValue(paramResult.branch_id, paramResult.product_id, paramResult.uom, paramResult.qty_uom, db_name, port);

                if (dataCurrent != null) {

                    if (dataCurrent.uom != null) {

                        _mdlStockResult = SbbAdapter.GetReturnStockFromSBB(paramResult.sbb_id, paramResult.product_id, paramResult.batch_num, paramResult.uom, db_name, port);

                        konversiSbb = (int) _mdlStockResult.konversiStock;
                        currentStock = (int) dataCurrent.currentStock + konversiSbb;
                        konversiStock = (int) dataCurrent.konversiStock;
                        uom = dataCurrent.uom;

                        if (currentStock >= 0) {

                            double prevQuantityValue = SbbAdapter.GetTrxStockValue(paramResult.sbb_id, paramResult.product_id, db_name, port);
                            int prevKonversiStock = (int) prevQuantityValue;
                            int selisih = 0;

                            selisih = prevKonversiStock - konversiSbb;
                            konversiStock = konversiStock + selisih;

                            if (prevKonversiStock > 0) {
                                adapter.SbbAdapter.GetReturnStock(paramResult.product_id, konversiSbb, db_name, port);
                            }

                            boolean resultSbbqty = adapter.SbbAdapter.UploadSbbQtyVsUom(paramResult, konversiStock, db_name, port);

                            if (resultSbbqty == true) {

                                boolean result = adapter.SbbAdapter.UploadSbbQty(paramResult, db_name, port);
                                if (result == true) _mdlErrorSchema.error_code = "ERR-00-000";
                                else _mdlErrorSchema.error_code = "ERR-99-008";
                                response.setStatus(SC_OK);
                                } else {
                                _mdlErrorSchema.error_code = "ERR-99-008";
                                response.setStatus(SC_BAD_REQUEST);
                                logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
                            }

                        } else if (currentStock < 0) {
                            _mdlErrorSchema.error_code = "ERR-09-002";
                            response.setStatus(SC_BAD_REQUEST);
                            logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
                        }
                    } else if (currentStock < 0) {
                        _mdlErrorSchema.error_code = "ERR-09-002";
                    } else {
                        _mdlErrorSchema.error_code = "ERR-09-001";
                    }

                } else {
                    _mdlErrorSchema.error_code = "ERR-09-001";
                    response.setStatus(SC_BAD_REQUEST);
                    logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
                }

                _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                _mdlResultFinal.value = null;
//                return _mdlResultFinal;

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
            // TODO 3 (UploadSbbQty) : Set Data exception untuk InsertLogApi
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
            //TODO 4 (UploadSbbQty) : Set Data jsonOut dan status_api untuk InsertLogApi
            String status_api = String.valueOf(response.getStatus()) + " ; " + String.valueOf(response.getBufferSize()) + " B";
            //

            //TODO 5 (UploadSbbQty) : Input data ke model.Log.mdlInsertLog modelInsertLogParam
            model.Log.mdlInsertLog modelInsertLogParam = new model.Log.mdlInsertLog();
            modelInsertLogParam.api_name = url;
            modelInsertLogParam.fuction_name = "UploadSbbQty";
            modelInsertLogParam.api_header = jsonHeader;
            modelInsertLogParam.api_input = gson.toJson(param);
            modelInsertLogParam.api_output = gson.toJson(_mdlResultFinal);
            modelInsertLogParam.api_status = status_api;
            modelInsertLogParam.exception = lException;
            modelInsertLogParam.created_by = "master_core";
            modelInsertLogParam.response_time = String.valueOf(startTime);
            modelInsertLogParam.db = db_name;
            //

            //TODO 6 (UploadSbbQty) : Input data yang telah diisi ke core.LogAdapter.InsertLog
            core.LogAdapter.InsertLogApi(modelInsertLogParam);
            //

        }
        return _mdlResultFinal;
    }

    //GetSbbList
    @RequestMapping(value = "/get-sbb-list", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal GetSbb(
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

        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
        String url = "/get-sbb-list";


        try{
            mdlSbb paramResult1;
            paramResult1 = gson.fromJson(param, mdlSbb.class);

            mdlDataTableParam paramResult2;
            paramResult2 = gson.fromJson(param, mdlDataTableParam.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port); //only-live
//        _mdlResultFinal.status_code = "ERR-00-000"; //only-testing

            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//            db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live


                int _mdlSbbTotalList = SbbAdapter.GetSbbList(paramResult1, paramResult2, db_name, port);
                List<mdlSbb> _mdlSbbList = SbbAdapter.GetSbbWeb(paramResult1, paramResult2, db_name, port);

                    mdlDataTableResult _mdlDataTableResult = new mdlDataTableResult();
                    _mdlDataTableResult.records_total = _mdlSbbTotalList;
                    _mdlDataTableResult.records_filtered = _mdlSbbTotalList;
                    _mdlDataTableResult.draw = paramResult2.draw + 1;
                    _mdlDataTableResult.data = _mdlSbbList;

                    _mdlErrorSchema.error_code = "ERR-00-000";
                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                    _mdlResultFinal.value = _mdlDataTableResult;
                    response.setStatus(SC_OK);

            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.error(LogAdapter.logToLog4j(false, startTime, 401, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
            }
        }catch (Exception ex){
            _mdlErrorSchema.error_code = "ERR-99-999";
            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
            _mdlResultFinal.value = ex.toString();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error(LogAdapter.logToLog4jException(startTime, url, apiMethod, systemFunction, jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal), ex.toString()), ex);
        }

        return _mdlResultFinal;

    }

    @RequestMapping(value = "/get-sbb-uom-qty-list", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal GetSbbUomQty(
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

        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
        String url = "/get-sbb-uom-qty-list";

        try{
            mdlSbbUomQty paramResult1;
            paramResult1 = gson.fromJson(param, mdlSbbUomQty.class);

            mdlDataTableParam paramResult2;
            paramResult2 = gson.fromJson(param, mdlDataTableParam.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port); //only-live
//        _mdlResultFinal.status_code = "ERR-00-000"; //only-testing

            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//            db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live


                int _mdlSbbTotalList = SbbAdapter.GetSbbUomQtyList(paramResult1, paramResult2, db_name, port);
                List<mdlSbbUomQty> _mdlSbbList = SbbAdapter.GetSbbUomQtyWeb(paramResult1, paramResult2, db_name, port);

                    mdlDataTableResult _mdlDataTableResult = new mdlDataTableResult();
                    _mdlDataTableResult.records_total = _mdlSbbTotalList;
                    _mdlDataTableResult.records_filtered = _mdlSbbTotalList;
                    _mdlDataTableResult.draw = paramResult2.draw + 1;
                    _mdlDataTableResult.data = _mdlSbbList;

                    _mdlErrorSchema.error_code = "ERR-00-000";
                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                    _mdlResultFinal.value = _mdlDataTableResult;
                    response.setStatus(SC_OK);

            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.error(LogAdapter.logToLog4j(false, startTime, 401, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
            }
        }catch (Exception ex){
            _mdlErrorSchema.error_code = "ERR-99-999";
            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
            _mdlResultFinal.value = ex.toString();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error(LogAdapter.logToLog4jException(startTime, url, apiMethod, systemFunction, jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal), ex.toString()), ex);
        }

        return _mdlResultFinal;


    }

    //Delete sbb
    @RequestMapping(value = "/delete-sbb", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal DeleteSbb(
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

        String url = "/delete-sbb";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();

        try {
            mdlSbb paramResult;
            paramResult = gson.fromJson(param, mdlSbb.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, String.valueOf(param), port);
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing

            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live

                boolean result = adapter.SbbAdapter.DeleteSbb(paramResult, db_name, port);

                if(result == true){
                    _mdlErrorSchema.error_code = "ERR-00-000";
                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                    _mdlResultFinal.value =  null;
                }else {
                    _mdlErrorSchema.error_code = "ERR-99-008";
                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                    _mdlResultFinal.value =  null;
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
                }
            }else{
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.error(LogAdapter.logToLog4j(false, startTime, 401, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
            }

        }catch (Exception ex){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            _mdlErrorSchema.error_code = "ERR-99-999";
            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
            _mdlResultFinal.value =  ex.toString();
            logger.error(LogAdapter.logToLog4jException(startTime, url, apiMethod, systemFunction, jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal), ex.toString()), ex);
        }
        return _mdlResultFinal;
    }

    @RequestMapping(value = "/delete-sbb-uom-qty", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal DeleteSbbUomQty(
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

        String url = "/delete-sbb-uom-qty";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();

        try {
            mdlSbbUomQty paramResult;
            paramResult = gson.fromJson(param, mdlSbbUomQty.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, String.valueOf(param), port);
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing

            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live

                boolean result = adapter.SbbAdapter.DeleteSbbUomQty(paramResult, db_name, port);

                if(result == true){
                    _mdlErrorSchema.error_code = "ERR-00-000";
                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                    _mdlResultFinal.value =  null;
                    }else {
                    _mdlErrorSchema.error_code = "ERR-99-008";
                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                    _mdlResultFinal.value =  null;
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
                }
            }else{
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.error(LogAdapter.logToLog4j(false, startTime, 401, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
            }

        }catch (Exception ex){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            _mdlErrorSchema.error_code = "ERR-99-999";
            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
            _mdlResultFinal.value =  ex.toString();
            logger.error(LogAdapter.logToLog4jException(startTime, url, apiMethod, systemFunction, jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal), ex.toString()), ex);
        }
        return _mdlResultFinal;
    }

    //GetSbbList
    @RequestMapping(value = "/get-sbb-stock-by-product", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal GetSbbStockByProduct(
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

        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
        String url = "/get-sbb-stock-by-product";

        try{

            mdlSbb paramResult1;
            paramResult1 = gson.fromJson(param, mdlSbb.class);

            mdlDataTableParam paramResult2;
            paramResult2 = gson.fromJson(param, mdlDataTableParam.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port); //only-live

            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live

                double _mdlSbbTotalList = SbbAdapter.GetSbbStockByProduct(paramResult1.product_id, paramResult1.branch_id, db_name, port);
                int konversiStock = (int) _mdlSbbTotalList;

                if (konversiStock == 0) {

                    _mdlErrorSchema.error_code = "ERR-99-008";
                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                    _mdlResultFinal.value = 0;
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
                } else {

                    _mdlErrorSchema.error_code = "ERR-00-000";
                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                    _mdlResultFinal.value = konversiStock;
                    response.setStatus(SC_OK);
                }

            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.error(LogAdapter.logToLog4j(false, startTime, 401, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
            }
        }catch (Exception ex){
            _mdlErrorSchema.error_code = "ERR-99-999";
            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
            _mdlResultFinal.value = ex.toString();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error(LogAdapter.logToLog4jException(startTime, url, apiMethod, systemFunction, jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal), ex.toString()), ex);
        }

        return _mdlResultFinal;
    }

}
