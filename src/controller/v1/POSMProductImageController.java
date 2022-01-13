package controller.v1;

import adapter.EmployeeImageAdapter;
import adapter.POSMProductAdapter;
import adapter.POSMProductImageAdapter;
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
import model.POSM.mdlPOSMProductImage;
import model.Product.mdlProductImage;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1")
public class POSMProductImageController {

    final static Logger logger = LogManager.getLogger(POSMProductImageController.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();
    String db_name = "";
    int port = 0;

    @RequestMapping(value = "/get-posm-image-list", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal GetPOSMProductImage(
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

        String url = "/get-posm-image-list";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
        try {
            mdlDataTableParam paramResult = gson.fromJson(param, mdlDataTableParam.class);

            mdlPOSMProductImage paramResult2 = gson.fromJson(param, mdlPOSMProductImage.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port); //only-live
//        _mdlResultFinal.status_code = "ERR-00-000"; //only-testing

            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//            db_name = "db_ffa"; //only-testing
                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live

                List<mdlPOSMProductImage> _mdlPOSMProductImageList = POSMProductImageAdapter.GetProductImageWithPaging(paramResult, paramResult2, db_name, port);
                int _mdlPOSMProductImageListTotal = POSMProductImageAdapter.GetPOSMProductImageTotalList(paramResult, paramResult2, db_name, port);

                mdlDataTableResult _mdlDataTableResult = new mdlDataTableResult();

                _mdlDataTableResult.records_total = _mdlPOSMProductImageListTotal;
                _mdlDataTableResult.records_filtered = _mdlPOSMProductImageListTotal;
                _mdlDataTableResult.draw = paramResult.draw + 1;
                _mdlDataTableResult.data = _mdlPOSMProductImageList;

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


    @RequestMapping(value = "/upload-posm-product-image", method = RequestMethod.POST, consumes = "multipart/form-data")
    public @ResponseBody
    mdlResultFinal UploadPOSMProductImages(
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

        String url = "/upload-posm-product-image";
        String apiMethod = request.getMethod();
        String _app_name = "";
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema mdlErrorSchema = new mdlErrorSchema();
        try {
            port =  request.getLocalPort() ;

            boolean isValid = true;

            mdlPOSMProductImage paramResult = gson.fromJson(param, mdlPOSMProductImage.class);

//            mdlResultFinal.status_code = "ERR-00-000"; //only-testing
            mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port);
            if (mdlResultFinal.status_code.equals("ERR-00-000")) {
                db_name = ((mdlDB) mdlResultFinal.value).db_id;
                _app_name = ((mdlDB) mdlResultFinal.value).app_name;

//                System.out.println(paramResult.image_date.toString());
                String fileName = image.getOriginalFilename();
                String finalimageName = "";
//
////                String sDate1 = paramResult.image_date.toString();
//                String imageYear = sDate1.substring(0, 4);
//                String imageMonth = sDate1.substring(5, 7);
//                String imageDay = sDate1.substring(8, 10);

                String folderDir =  _app_name + "/" + "POSM"+ "/";
                //System.out.println(imageMonth);

                if (image != null) {
                    String ext = FilenameUtils.getExtension(fileName);
                    int IndexOf = fileName.indexOf(".");
                    String domainName = fileName.substring(IndexOf);
                    finalimageName = paramResult.posm_id + domainName;
                    if (ext != null && !ext.isEmpty() && (ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png"))) {
                        try {
                            Context context = (Context) new InitialContext().lookup("java:comp/env");
                            StringBuilder sb = new StringBuilder();

                            String paramFilePath = (String) context.lookup("path_web_server_for_upload_posm");
                            String directory = sb.append(paramFilePath).append(folderDir).toString();
                            String uploadedFileLocation = sb.append("/").append(finalimageName).toString();
                            FileHelper.writeToFile(image.getInputStream(), directory, uploadedFileLocation);

                            String urlWebServer = (String) context.lookup("url_web_server_for_upload_posm");

                            paramResult.image_name = new StringBuilder().append(paramResult.posm_id).append(".").append(ext).toString();
                            paramResult.image_path = new StringBuilder().append(urlWebServer).append(folderDir).append(paramResult.image_name).toString();
                        } catch (Exception e) {
                            isValid = false;
                        }
                    } else {
                        isValid = false;
                    }
                    if (isValid) {
                        boolean result = POSMProductImageAdapter.UploadPosmProductImage(paramResult, db_name, port);
                        if (result == true) {
                            mdlErrorSchema.error_code = "ERR-00-000";
                            mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(mdlErrorSchema, port);
                        } else {
                            mdlErrorSchema.error_code = "ERR-99-008";
                            mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(mdlErrorSchema, port);
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "", gson.toJson(param), gson.toJson(mdlResultFinal)));
                        }
                    } else {
                        mdlErrorSchema.error_code = "ERR-03-012";
                        mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(mdlErrorSchema, port);
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(mdlResultFinal)));
                    }

                }

            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                logger.error(LogAdapter.logToLog4j(false, startTime, 401, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(mdlResultFinal)));
                return mdlResultFinal;
            }
        } catch (Exception ex) {
            mdlErrorSchema.error_code = "ERR-99-999";
            mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema_V2(mdlErrorSchema, port);
            mdlResultFinal.value = ex.toString();
            logger.error(LogAdapter.logToLog4jException(startTime, url, apiMethod, systemFunction, jsonHeader, gson.toJson(param), gson.toJson(mdlResultFinal), ex.toString()), ex);
        }
        return mdlResultFinal;
    }

    @RequestMapping(value = "/delete-posm-image", method = RequestMethod.POST)
    public @ResponseBody
    mdlResultFinal DeletePOSMProductImage(
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

        String url = "/delete-posm-image";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
        try {
            mdlPOSMProductImage paramResult = gson.fromJson(param, mdlPOSMProductImage.class);

            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port);
            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {

                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live

                boolean result = POSMProductImageAdapter.DeletePOSMProductImage(paramResult, db_name, port);
                if (result) {
                    _mdlErrorSchema.error_code = "ERR-00-000";
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

    @RequestMapping(value = "/upload-posm-product-image-noimg", method = RequestMethod.POST, consumes = "multipart/form-data")
    public @ResponseBody
    mdlResultFinal UploadPOSMProductImagesNoImg(
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

        String url = "/upload-posm-product-image-noimg";
        String apiMethod = request.getMethod();
        // get all headers for logging purpose
        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
        String jsonHeader = gson.toJson(headerList);

        mdlResultFinal mdlResultFinal = new mdlResultFinal();
        mdlErrorSchema mdlErrorSchema = new mdlErrorSchema();
        try {
            boolean result = false;
            mdlPOSMProductImage paramResult = gson.fromJson(param, mdlPOSMProductImage.class);

            mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, param, port);
//            mdlResultFinal.status_code = "ERR-00-000";
            if (mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffs";
                db_name = ((mdlDB) mdlResultFinal.value).db_id; //only-live
                result = POSMProductImageAdapter.UploadPosmProductImageNoImage(paramResult, db_name, port);

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
