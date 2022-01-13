package controller.v1;

import adapter.EmployeeImageAdapter;
import adapter.ProductImageAdapter;
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
import model.Product.mdlProductImage;
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

import static core.LogAdapter.GetIsActiveLogApiConfig;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_OK;

@RestController
@RequestMapping("/v1")
public class ProductImageController {

    final static Logger logger = LogManager.getLogger(ProductImageController.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();
    String db_name = "";
    int port = 0;


    @RequestMapping(value = "/get-product-image-list", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal GetProductImage (
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

        String api_method = request.getMethod();

        String url = "/get-product-image-list";
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
        try {
            mdlDataTableParam paramResult;
            paramResult = gson.fromJson(param, mdlDataTableParam.class);

            mdlProductImage paramResult2;
            paramResult2 = gson.fromJson(param, mdlProductImage.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, api_method, param, port); //only-live
//        _mdlResultFinal.status_code = "ERR-00-000"; //only-testing

            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//            db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live

                List<mdlProductImage> _mdlProductImageList = ProductImageAdapter.GetProductImageWithPaging(paramResult, paramResult2, db_name, port);
                int _mdlProductImageListTotal = ProductImageAdapter.GetProductImageTotalList(paramResult, paramResult2, db_name, port);

                    mdlDataTableResult _mdlDataTableResult = new mdlDataTableResult();
                    _mdlDataTableResult.records_total = _mdlProductImageListTotal;
                    _mdlDataTableResult.records_filtered = _mdlProductImageListTotal;
                    _mdlDataTableResult.draw = paramResult.draw + 1;
                    _mdlDataTableResult.data = _mdlProductImageList;

                    _mdlErrorSchema.error_code = "ERR-00-000";
                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                    _mdlResultFinal.value = _mdlDataTableResult;
                    response.setStatus(SC_OK);

            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.error(LogAdapter.logToLog4j(false, startTime, 401, url, api_method, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
            }
        }catch (Exception ex){
            _mdlErrorSchema.error_code = "ERR-99-999";
            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
            _mdlResultFinal.value = ex.toString();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error(LogAdapter.logToLog4jException(startTime, url, api_method, systemFunction, jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal), ex.toString()), ex);
        }

        return _mdlResultFinal;
    }

    @RequestMapping(value = "/upload-product-image", method = RequestMethod.POST, consumes = "multipart/form-data")
    public @ResponseBody
    mdlResultFinal UploadProductImages(
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

        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();

        String url = "/upload-product-image";

        //TODO 1 (UploadAreaWeb) : Param untuk InsertLogApi
        String lException = "";
        String is_error = "";

        try {

            boolean isValid = true;
            String StorageDir = "";
            String finalimageName = "";

            mdlProductImage paramResult;
            paramResult = gson.fromJson(param, mdlProductImage.class);


            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port);

            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live
                if (image != null) {
                    try {
                        Context context = (Context) new InitialContext().lookup("java:comp/env");
                        StorageDir = (String) context.lookup("url_web_server_for_upload_product_image");


                        String fileName = image.getOriginalFilename();
                        int IndexOf = fileName.indexOf(".");
                        String domainName = fileName.substring(IndexOf);
                        finalimageName = paramResult.product_id + "_" +  DateHelper.GetDateTimeNowCustomFormat("yyyyMMddHHmmss") + domainName;
                        String uploadedFileLocation = StorageDir + finalimageName;

                        FileHelper.writeToFile(image.getInputStream(), StorageDir, uploadedFileLocation);
                        isValid = ProductImageAdapter.UploadProductImage(paramResult, StorageDir, finalimageName, db_name, port);

                    } catch (Exception e) {
                        isValid = false;
                        e.getMessage();
                    }

                }

                if (isValid == false) {
                    _mdlErrorSchema.error_code = "ERR-99-008";
                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                    _mdlResultFinal.value = null;
                    response.setStatus(SC_BAD_REQUEST);
                    logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
                } else {
                    _mdlErrorSchema.error_code = "ERR-00-000";
                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
                    _mdlResultFinal.value = null;
                    response.setStatus(SC_OK);
                    }

            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.error(LogAdapter.logToLog4j(false, startTime, 401, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));

            }
        }catch (Exception ex){
            _mdlErrorSchema.error_code = "ERR-99-999";
            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
            _mdlResultFinal.value =  ex.toString();
            // TODO 3 (UploadProductImages) : Set Data exception untuk InsertLogApi
            lException = "master_core - " + ex.toString();
            //
            is_error = "1";
//            return mdlResultFinal;
        }
        String core_name = "master_core";
        model.Log.mdlLogApiConfig mdlLogApiConfig = new model.Log.mdlLogApiConfig();
        mdlLogApiConfig = GetIsActiveLogApiConfig(core_name, db_name);
        //is_active ambil dari main_core
        if(is_error.equals("1")  || mdlLogApiConfig.is_active == true) {
            //TODO 4 (UploadProductImages) : Set Data jsonOut dan status_api untuk InsertLogApi
            String status_api = String.valueOf(response.getStatus()) + " ; " + String.valueOf(response.getBufferSize()) + " B";
            //

            //TODO 5 (UploadProductImages) : Input data ke model.Log.mdlInsertLog modelInsertLogParam
            model.Log.mdlInsertLog modelInsertLogParam = new model.Log.mdlInsertLog();
            modelInsertLogParam.api_name = url;
            modelInsertLogParam.fuction_name = "UploadProductImages";
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

            //TODO 6 (UploadProductImages) : Input data yang telah diisi ke core.LogAdapter.InsertLog
            core.LogAdapter.InsertLogApi(modelInsertLogParam);
            //
        }


        return _mdlResultFinal;

    }

    @RequestMapping(value = "/delete-product-image", method = RequestMethod.POST)
    public @ResponseBody mdlResultFinal DeleteProductImage(
            HttpServletResponse response,
            HttpServletRequest request,
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


        String url = "/delete-product-image";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
        try {

            mdlProductImage paramResult;
            paramResult = gson.fromJson(param, mdlProductImage.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port);

            if(_mdlResultFinal.status_code.equals("ERR-00-000")){

                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live

                boolean result = ProductImageAdapter.DeleteProductImage(paramResult, db_name, port);

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

    @RequestMapping( value = "/update-status-product-image", method = RequestMethod.POST)
    public @ResponseBody mdlResultFinal updateStatusProduct (
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


        String url = "/update-status-product-image";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();

        try {
            mdlProductImage paramResult;
            paramResult = gson.fromJson(param, mdlProductImage.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, String.valueOf(param), port);
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing

            if(_mdlResultFinal.status_code.equals("ERR-00-000")){
//                db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live

                boolean result = adapter.ProductImageAdapter.UpdateStatusProductImage(paramResult, db_name, port);

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

        }catch (Exception ex){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            _mdlErrorSchema.error_code = "ERR-99-999";
            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(_mdlErrorSchema, port);
            _mdlResultFinal.value =  ex.toString();
            logger.error(LogAdapter.logToLog4jException(startTime, url, apiMethod, systemFunction, jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal), ex.toString()), ex);
        }

        return _mdlResultFinal;
    }

    @RequestMapping(value = "/upload-product-image-noimg", method = RequestMethod.POST, consumes = "multipart/form-data")
    public @ResponseBody
    mdlResultFinal UploadProductImagesNoImg(
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

        String url = "/upload-product-image-noimg";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema mdlErrorSchema = new mdlErrorSchema();
        try {
            boolean result = false;
            mdlProductImage paramResult = gson.fromJson(param, mdlProductImage.class);

            mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port);
//            mdlResultFinal.status_code = "ERR-00-000";
            if (mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffs";
                db_name = ((mdlDB) mdlResultFinal.value).db_id; //only-live
                result = ProductImageAdapter.UploadProductNoImage(paramResult, db_name, port);

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
