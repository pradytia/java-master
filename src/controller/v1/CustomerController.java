package controller.v1;

import adapter.CustomerAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.AuthorizationAdapter;
import core.ErrorSchemaAdapter;
import core.LogAdapter;
import model.Client.mdlDB;
import model.Customer.mdlCustomer;
import model.Customer.mdlCustomerParam;
import model.Customer.mdlCustomerVisitTracking;
import model.DataTable.mdlDataTableParam;
import model.DataTable.mdlDataTableResult;
import model.ErrorSchema.mdlErrorSchema;
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
public class CustomerController {
    final static Logger logger = LogManager.getLogger(CustomerController.class);
    Gson gson = new GsonBuilder().serializeNulls().create();
    String db_name = "";
    int port = 0;

    @RequestMapping(value = "/check-customer", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal GetCustomerCheck(
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

        String url = "/check-customer";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
        try {
            mdlCustomer paramResult = gson.fromJson(param, mdlCustomer.class);
            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, String.valueOf(param), port);
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing
            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live

                List<mdlCustomer> _mdlCustomerList = CustomerAdapter.GetCustomerCheck(paramResult, db_name, port);
                if (!paramResult.customer_id.equals("")) {
                    if (_mdlCustomerList == null || _mdlCustomerList.size() == 0) {
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

    @RequestMapping(value = "/get-customer-list", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal GetCustomer(
            @RequestHeader String authorization,
            @RequestHeader String key,
            @RequestHeader String timestamp,
            @RequestHeader String signature,
            @RequestBody String param,
            HttpServletRequest request,
            HttpServletResponse response) {
        port = request.getLocalPort();
        if(port==80) port = 443;

        long startTime = System.currentTimeMillis();
        String systemFunction = Thread.currentThread().getStackTrace()[1].getMethodName();

        String url = "/get-customer-list";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
        try {
            mdlDataTableParam paramResult = gson.fromJson(param, mdlDataTableParam.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port); //only-live
//        mdlResultFinal.statusCode = "ERR-00-000"; //only-testing

            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//            db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live

                List<model.Customer.mdlCustomer> _mdlCustomerList = CustomerAdapter.GetCustomerWeb(paramResult, db_name, port);
                int _mdlCustomerListTotal = CustomerAdapter.GetCustomerTotalList(paramResult, db_name, port);

                mdlDataTableResult _mdlDataTableResult = new mdlDataTableResult();

                _mdlDataTableResult.records_total = _mdlCustomerListTotal;
                _mdlDataTableResult.records_filtered = _mdlCustomerListTotal;
                _mdlDataTableResult.draw = paramResult.draw + 1;
                _mdlDataTableResult.data = _mdlCustomerList;

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

    // Upload Customer
    @RequestMapping(value = "/upload-customer", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal UploadCustomer(
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

        String url = "/upload-customer";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();

        //TODO 1 (UploadCustomer) : Param untuk InsertLogApi
        String lException = "";
        String is_error = "";
        try {
            mdlCustomer paramResult = gson.fromJson(param, mdlCustomer.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, String.valueOf(param), port);
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing

            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live

                boolean result = CustomerAdapter.UploadCustomer(paramResult, db_name, port);

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
            // TODO 3 (UploadCustomer) : Set Data exception untuk InsertLogApi
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
            //TODO 5 (UploadCustomer) : Input data ke model.Log.mdlInsertLog modelInsertLogParam
            model.Log.mdlInsertLog modelInsertLogParam = new model.Log.mdlInsertLog();
            modelInsertLogParam.api_name = url;
            modelInsertLogParam.fuction_name = "UploadCustomer";
            modelInsertLogParam.api_header = jsonHeader;
            modelInsertLogParam.api_input = param;
            modelInsertLogParam.api_output = gson.toJson(_mdlResultFinal);
            modelInsertLogParam.api_status = response.getStatus() + " ; " + response.getBufferSize() + " B";
            modelInsertLogParam.exception = lException;
            modelInsertLogParam.created_by = "master_core";
            modelInsertLogParam.response_time = String.valueOf(startTime);
            modelInsertLogParam.status_code = _mdlResultFinal.status_code;
            modelInsertLogParam.db = db_name;

            //TODO 6 (UploadCustomer) : Input data yang telah diisi ke core.LogAdapter.InsertLog
            core.LogAdapter.InsertLogApi(modelInsertLogParam);
        }


        return _mdlResultFinal;
    }

    @RequestMapping(value = "/update-status-customer", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal UpdateStatusCustomer(
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

        String url = "/update-status-customer";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();

        try {
            mdlCustomer paramResult = gson.fromJson(param, mdlCustomer.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, String.valueOf(param), port);
//            _mdlResultFinal.statusCode = "ERR-00-000"; //only-testing

            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live

                boolean result = CustomerAdapter.UpdateStatusCustomer(paramResult, db_name, port);
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

    @RequestMapping(value = "/delete-customer", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal DeleteCustomer(
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

        String url = "/delete-customer";
        String apiMethod = request.getMethod();
//        boolean vst_result = false;
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
        try {
            mdlCustomer paramResult = gson.fromJson(param, mdlCustomer.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, String.valueOf(param), port);
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing

            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live

                boolean clpn_result = CustomerAdapter.CheckCallPlanByCstID(paramResult, db_name, port);
                boolean vst_result = CustomerAdapter.CheckVisitByCstID(paramResult, db_name, port);
                boolean npwp_result = CustomerAdapter.CheckCustomerNpwpByCstID(paramResult, db_name, port);
                boolean coordinate_result = CustomerAdapter.CheckCustomerCoordinateByCstID(paramResult, db_name, port);
                boolean info_result = CustomerAdapter.CheckCustomerInfoByCstID(paramResult, db_name, port);

                if (vst_result == true && clpn_result == true && npwp_result == true && coordinate_result == true && info_result == true){

                    boolean result = CustomerAdapter.DeleteCustomer(paramResult, db_name, port);
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
                    if (clpn_result == false){
                        _mdlErrorSchema.error_code = "ERR-99-015";
                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                        _mdlResultFinal.value =  null;
                    }else if (vst_result == false){
                        _mdlErrorSchema.error_code = "ERR-99-022";
                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                        _mdlResultFinal.value =  null;
                    }else if (npwp_result == false){
                        _mdlErrorSchema.error_code = "ERR-99-016";
                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                        _mdlResultFinal.value =  null;
                    }else if (coordinate_result == false){
                        _mdlErrorSchema.error_code = "ERR-99-017";
                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                        _mdlResultFinal.value =  null;
                    }else if (info_result == false){
                        _mdlErrorSchema.error_code = "ERR-99-018";
                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                        _mdlResultFinal.value =  null;
                    }
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

    @RequestMapping(value = "/get-dropdown-customer-by-branch-exc-cpdetail", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal GetDropDownCustomerByBranchExcCpDetail(
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

        String url = "/get-dropdown-customer-by-branch-exc-cpdetail";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
        try {
            mdlCustomerParam paramResult = gson.fromJson(param, mdlCustomerParam.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port);
//        mdlResultFinal.status_code = "ERR-00-000"; // only-testing
            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//            db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) _mdlResultFinal.value).db_id;
                List<mdlCustomer> customerList = CustomerAdapter.GetDropDownCustomerByBranchExcCPDet(paramResult, db_name, port);
                _mdlErrorSchema.error_code = "ERR-00-000";
                _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                _mdlResultFinal.value = customerList;

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

    @RequestMapping(value = "/get-dropdown-customer-by-branch", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal GetDropDownCustomerByBranch(
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

        String url = "/get-dropdown-customer-by-branch";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema mdlErrorSchema = new mdlErrorSchema();
        try {
            mdlCustomerParam paramResult = gson.fromJson(param, mdlCustomerParam.class);

            mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port);
//        mdlResultFinal.status_code = "ERR-00-000"; // only-testing
            if (mdlResultFinal.status_code.equals("ERR-00-000")) {
//            db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) mdlResultFinal.value).db_id;
                List<mdlCustomer> customerList;
                customerList = CustomerAdapter.GetDropDownCustomerByBranch(paramResult, db_name, port);

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

    @RequestMapping(value = "/get-customer-coordinate", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal GetCustomerorWarehouseCoordinate(
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

        String url = "/get-customer-coordinate";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
        try {
            mdlCustomerParam paramResult = gson.fromJson(param, mdlCustomerParam.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port);
            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
                db_name = ((mdlDB) _mdlResultFinal.value).db_id;

                List<mdlCustomerVisitTracking> _mdlCustomerVisitTrackingList = CustomerAdapter.GetCustomerCoordinate(paramResult, db_name, port);

                _mdlErrorSchema.error_code = "ERR-00-000";
                _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                _mdlResultFinal.value = _mdlCustomerVisitTrackingList;

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

    @RequestMapping(value = "/get-customer-for-callplan", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal GetCustomerDropdownforCallPlan(
            @RequestHeader String authorization,
            @RequestHeader String key,
            @RequestHeader String timestamp,
            @RequestHeader String signature,
            @RequestBody String param,
            HttpServletRequest request,
            HttpServletResponse response) {
        port = request.getLocalPort();
        if(port==80) port = 443;

        long startTime = System.currentTimeMillis();
        String systemFunction = Thread.currentThread().getStackTrace()[1].getMethodName();

        String url = "/get-customer-for-callplan";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
        try {
            mdlDataTableParam paramResult = gson.fromJson(param, mdlDataTableParam.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port); //only-live
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing

            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//            db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live

                List<model.Customer.mdlCustomer> _mdlCustomerList = CustomerAdapter.GetCustomerDropdownforCallplan(paramResult, db_name, port);
                int _mdlCustomerListTotal = CustomerAdapter.GetCustomerTotalListCallplan(paramResult, db_name, port);

                mdlDataTableResult _mdlDataTableResult = new mdlDataTableResult();
                    _mdlDataTableResult.records_total = _mdlCustomerListTotal;
                    _mdlDataTableResult.records_filtered = _mdlCustomerListTotal;
                    _mdlDataTableResult.draw = paramResult.draw + 1;
                    _mdlDataTableResult.data = _mdlCustomerList;

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


}
