package controller.bypass;

import adapter.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import core.AuthorizationAdapter;
import core.ErrorSchemaAdapter;
import model.Area.mdlArea;
import model.Branch.mdlBranch;
import model.Callplan.mdlCallplan;
import model.Client.mdlDB;
import model.Competitor.mdlCompetitor;
import model.Customer.mdlCustomer;
import model.Customer.mdlCustomerCoordinate;
import model.Customer.mdlCustomerType;
import model.District.mdlDistrict;
import model.Download.mdlDownloadParam;
import model.Download.mdlDownloadResult;
import model.ECatalog.mdlECatalog;
import model.Employee.mdlEmployee;
import model.Employee.mdlEmployeeType;
import model.ErrorSchema.mdlErrorSchema;
import model.POSM.mdlPOSMProduct;
import model.Product.*;
import model.Reason.mdlReason;
import model.Region.mdlRegion;
import model.Result.mdlResultFinal;
import model.Uom.mdlUom;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bypass")
public class MasterController_bypass {

    String db_name = "";
//
//    @RequestMapping(value = "/check-reason", method = RequestMethod.POST)
//    public @ResponseBody
//    mdlResultFinal GetReasonCheck(
//            HttpServletResponse response,
////            @RequestHeader String authorization,
////            @RequestHeader String key,
////            @RequestHeader String timestamp,
////            @RequestHeader String signature,
//            @RequestBody String param
//    ) {
//
//        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
//        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
//
//        try {
//            String api_method = "POST";
//            String url = "/check-reason";
//
//            GsonBuilder builder = new GsonBuilder();
//            builder.serializeNulls();
//            Gson gson = builder.create();
//
////            Type list_type = new TypeToken<List<mdlReason>>(){}.getType();
////            List<mdlReason> paramResult = gson.fromJson(param, list_type);
//            mdlReason paramResult;
//            paramResult = gson.fromJson(param, mdlReason.class);
//
////            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v2(authorization, key, url, timestamp, signature, api_method, String.valueOf(param));
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing
//
//            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffa"; //only-testing
////                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live
//
//                List<mdlReason> _mdlReasonList = adapter.ReasonAdapter.GetReasonCheck(paramResult, db_name);
//
//                if (!paramResult.reason_id.equals("")) {
//                    if (_mdlReasonList.size() == 0 || _mdlReasonList == null) {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = false;
//                    } else {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = true;
//                    }
//                } else {
//                    _mdlErrorSchema.error_code = "ERR-99-008";
//                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                    _mdlResultFinal.value = null;
//                }
//                return _mdlResultFinal;
//
//            } else {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return _mdlResultFinal;
//            }
//
//        } catch (Exception ex) {
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            _mdlErrorSchema.error_code = "ERR-99-999";
//            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//            _mdlResultFinal.value = ex.toString();
//            return _mdlResultFinal;
//        }
//
//    }
//
//    @RequestMapping(value = "/check-area", method = RequestMethod.POST)
//    public @ResponseBody
//    mdlResultFinal GetAreaCheck(
//            HttpServletResponse response,
////            @RequestHeader String authorization,
////            @RequestHeader String key,
////            @RequestHeader String timestamp,
////            @RequestHeader String signature,
//            @RequestBody String param
//    ) {
//
//        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
//        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
//
//        try {
//            String api_method = "POST";
//            String url = "/check-area";
//
//            GsonBuilder builder = new GsonBuilder();
//            builder.serializeNulls();
//            Gson gson = builder.create();
//
////            Type list_type = new TypeToken<List<mdlReason>>(){}.getType();
////            List<mdlReason> paramResult = gson.fromJson(param, list_type);
//            mdlArea paramResult;
//            paramResult = gson.fromJson(param, mdlArea.class);
//
////            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v2(authorization, key, url, timestamp, signature, api_method, String.valueOf(param));
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing
//
//            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffa"; //only-testing
////                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live
//
//                List<mdlArea> _mdlAreaList = adapter.AreaAdapter.GetAreaCheck(paramResult, db_name);
//
//                if (!paramResult.area_id.equals("")) {
//                    if (_mdlAreaList.size() == 0 || _mdlAreaList == null) {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = false;
//                    } else {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = true;
//                    }
//                } else {
//                    _mdlErrorSchema.error_code = "ERR-99-008";
//                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                    _mdlResultFinal.value = null;
//                }
//                return _mdlResultFinal;
//
//            } else {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return _mdlResultFinal;
//            }
//
//        } catch (Exception ex) {
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            _mdlErrorSchema.error_code = "ERR-99-999";
//            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//            _mdlResultFinal.value = ex.toString();
//            return _mdlResultFinal;
//        }
//
//    }
//
//    @RequestMapping(value = "/check-branch", method = RequestMethod.POST)
//    public @ResponseBody
//    mdlResultFinal GetBranchCheck(
//            HttpServletRequest request,
//            HttpServletResponse response,
////            @RequestHeader String authorization,
////            @RequestHeader String key,
////            @RequestHeader String timestamp,
////            @RequestHeader String signature,
//            @RequestBody String param
//    ) {
//
//        //testing code
//        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
//        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
//
//        try {
//            String api_method = "POST";
//            String url = "/check-branch";
//
//            GsonBuilder builder = new GsonBuilder();
//            builder.serializeNulls();
//            Gson gson = builder.create();
//
//            String jsonIn = gson.toJson(param);
//            String jsonOut = "";
//            long startTime = System.currentTimeMillis();
//            long stopTime = 0;
//            long elapsedTime = 0;
//            Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
//            String jsonHeader = gson.toJson(headerList);
//
////            Type list_type = new TypeToken<List<mdlReason>>(){}.getType();
////            List<mdlReason> paramResult = gson.fromJson(param, list_type);
//            mdlBranch paramResult;
//            paramResult = gson.fromJson(param, mdlBranch.class);
//
////            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v2(authorization, key, url, timestamp, signature, api_method, String.valueOf(param));
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing
//
//            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffa"; //only-testing
////                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live
//
//                List<mdlBranch> _mdlBranchList = adapter.BranchAdapter.GetBranchCheck(paramResult, db_name);
//
//                if (!paramResult.branch_id.equals("")) {
//                    if (_mdlBranchList.size() == 0 || _mdlBranchList == null) {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = false;
//                    } else {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = true;
//                    }
//                } else {
//                    _mdlErrorSchema.error_code = "ERR-99-008";
//                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                    _mdlResultFinal.value = null;
//                }
////                jsonOut = gson.toJson(_mdlResultFinal);
////                elapsedTime = stopTime - startTime;
////                String response_time = String.valueOf(elapsedTime);
////                String status_api = String.valueOf(response.getStatus());
////                core.LogAdapter.InsertLog("GetBranchCheck", "GetBranchCheck", jsonHeader, jsonIn, jsonOut,
////                        status_api, " ", "tster", response_time, db_name);
//                return _mdlResultFinal;
//
//            } else {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
////                core.LogAdapter.InsertLog("Get BranchCheck", "test", "test", "test", "test",
////                        "test", "test", "test", "test", db_name);
//                return _mdlResultFinal;
//            }
//
//        } catch (Exception ex) {
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            _mdlErrorSchema.error_code = "ERR-99-999";
//            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//            _mdlResultFinal.value = ex.toString();
//            return _mdlResultFinal;
//        }
//
//    }
//
//    @RequestMapping(value = "/check-competitor", method = RequestMethod.POST)
//    public @ResponseBody
//    mdlResultFinal GetCompetitorCheck(
//            HttpServletResponse response,
////            @RequestHeader String authorization,
////            @RequestHeader String key,
////            @RequestHeader String timestamp,
////            @RequestHeader String signature,
//            @RequestBody String param
//    ) {
//
//        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
//        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
//
//        try {
//            String api_method = "POST";
//            String url = "/check-competitor";
//
//            GsonBuilder builder = new GsonBuilder();
//            builder.serializeNulls();
//            Gson gson = builder.create();
//
////            Type list_type = new TypeToken<List<mdlReason>>(){}.getType();
////            List<mdlReason> paramResult = gson.fromJson(param, list_type);
//            mdlCompetitor paramResult;
//            paramResult = gson.fromJson(param, mdlCompetitor.class);
//
////            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v2(authorization, key, url, timestamp, signature, api_method, String.valueOf(param));
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing
//
//            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffa"; //only-testing
////                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live
//
//                List<mdlCompetitor> _mdlCompetitorList = adapter.CompetitorAdapter.GetCompetitorCheck(paramResult, db_name);
//
//                if (!paramResult.competitor_id.equals("")) {
//                    if (_mdlCompetitorList.size() == 0 || _mdlCompetitorList == null) {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = false;
//                    } else {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = true;
//                    }
//                } else {
//                    _mdlErrorSchema.error_code = "ERR-99-008";
//                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                    _mdlResultFinal.value = null;
//                }
//                return _mdlResultFinal;
//
//            } else {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return _mdlResultFinal;
//            }
//
//        } catch (Exception ex) {
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            _mdlErrorSchema.error_code = "ERR-99-999";
//            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//            _mdlResultFinal.value = ex.toString();
//            return _mdlResultFinal;
//        }
//
//    }
//
//    @RequestMapping(value = "/check-district", method = RequestMethod.POST)
//    public @ResponseBody
//    mdlResultFinal GetDistrictCheck(
//            HttpServletResponse response,
////            @RequestHeader String authorization,
////            @RequestHeader String key,
////            @RequestHeader String timestamp,
////            @RequestHeader String signature,
//            @RequestBody String param
//    ) {
//
//        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
//        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
//
//        try {
//            String api_method = "POST";
//            String url = "/check-district";
//
//            GsonBuilder builder = new GsonBuilder();
//            builder.serializeNulls();
//            Gson gson = builder.create();
//
////            Type list_type = new TypeToken<List<mdlReason>>(){}.getType();
////            List<mdlReason> paramResult = gson.fromJson(param, list_type);
//            mdlDistrict paramResult;
//            paramResult = gson.fromJson(param, mdlDistrict.class);
//
////            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v2(authorization, key, url, timestamp, signature, api_method, String.valueOf(param));
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing
//
//            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffa"; //only-testing
////                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live
//
//                List<mdlDistrict> _mdlDistrictList = adapter.DistrictAdapter.GetDistrictCheck(paramResult, db_name);
//
//                if (!paramResult.district_id.equals("")) {
//                    if (_mdlDistrictList.size() == 0 || _mdlDistrictList == null) {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = false;
//                    } else {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = true;
//                    }
//                } else {
//                    _mdlErrorSchema.error_code = "ERR-99-008";
//                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                    _mdlResultFinal.value = null;
//                }
//                return _mdlResultFinal;
//
//            } else {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return _mdlResultFinal;
//            }
//
//        } catch (Exception ex) {
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            _mdlErrorSchema.error_code = "ERR-99-999";
//            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//            _mdlResultFinal.value = ex.toString();
//            return _mdlResultFinal;
//        }
//
//    }
//
//    @RequestMapping(value = "/check-customer", method = RequestMethod.POST)
//    public @ResponseBody
//    mdlResultFinal GetCustomerCheck(
//            HttpServletResponse response,
////            @RequestHeader String authorization,
////            @RequestHeader String key,
////            @RequestHeader String timestamp,
////            @RequestHeader String signature,
//            @RequestBody String param
//    ) {
//
//        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
//        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
//
//        try {
//            String api_method = "POST";
//            String url = "/check-customer";
//
//            GsonBuilder builder = new GsonBuilder();
//            builder.serializeNulls();
//            Gson gson = builder.create();
//
////            Type list_type = new TypeToken<List<mdlReason>>(){}.getType();
////            List<mdlReason> paramResult = gson.fromJson(param, list_type);
//            mdlCustomer paramResult;
//            paramResult = gson.fromJson(param, mdlCustomer.class);
//
////            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v2(authorization, key, url, timestamp, signature, api_method, String.valueOf(param));
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing
//
//            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffa"; //only-testing
////                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live
//
//                List<mdlCustomer> _mdlCustomerList = adapter.CustomerAdapter.GetCustomerCheck(paramResult, db_name);
//
//                if (!paramResult.customer_id.equals("")) {
//                    if (_mdlCustomerList.size() == 0 || _mdlCustomerList == null) {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = false;
//                    } else {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = true;
//                    }
//                } else {
//                    _mdlErrorSchema.error_code = "ERR-99-008";
//                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                    _mdlResultFinal.value = null;
//                }
//                return _mdlResultFinal;
//
//            } else {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return _mdlResultFinal;
//            }
//
//        } catch (Exception ex) {
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            _mdlErrorSchema.error_code = "ERR-99-999";
//            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//            _mdlResultFinal.value = ex.toString();
//            return _mdlResultFinal;
//        }
//
//    }
//
//
//    @RequestMapping(value = "/check-e-catalog", method = RequestMethod.POST)
//    public @ResponseBody
//    mdlResultFinal GetECatalogCheck(
//            HttpServletResponse response,
////            @RequestHeader String authorization,
////            @RequestHeader String key,
////            @RequestHeader String timestamp,
////            @RequestHeader String signature,
//            @RequestBody String param
//    ) {
//
//        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
//        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
//
//        try {
//            String api_method = "POST";
//            String url = "/check-e-catalog";
//
//            GsonBuilder builder = new GsonBuilder();
//            builder.serializeNulls();
//            Gson gson = builder.create();
//
////            Type list_type = new TypeToken<List<mdlReason>>(){}.getType();
////            List<mdlReason> paramResult = gson.fromJson(param, list_type);
//            mdlECatalog paramResult;
//            paramResult = gson.fromJson(param, mdlECatalog.class);
//
////            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v2(authorization, key, url, timestamp, signature, api_method, String.valueOf(param));
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing
//
//            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffa"; //only-testing
////                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live
//
//                List<mdlECatalog> _mdlECatalogList = adapter.ECatalogAdapter.GetECatalogheck(paramResult, db_name);
//
//                if (!paramResult.e_catalog_id.equals("")) {
//                    if (_mdlECatalogList.size() == 0 || _mdlECatalogList == null) {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = false;
//                    } else {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = true;
//                    }
//                } else {
//                    _mdlErrorSchema.error_code = "ERR-99-008";
//                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                    _mdlResultFinal.value = null;
//                }
//                return _mdlResultFinal;
//
//            } else {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return _mdlResultFinal;
//            }
//
//        } catch (Exception ex) {
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            _mdlErrorSchema.error_code = "ERR-99-999";
//            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//            _mdlResultFinal.value = ex.toString();
//            return _mdlResultFinal;
//        }
//
//    }
//
//    @RequestMapping(value = "/check-product-price", method = RequestMethod.POST)
//    public @ResponseBody
//    mdlResultFinal GetProductPriceCheck(
//            HttpServletResponse response,
////            @RequestHeader String authorization,
////            @RequestHeader String key,
////            @RequestHeader String timestamp,
////            @RequestHeader String signature,
//            @RequestBody String param
//    ) {
//
//        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
//        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
//
//        try {
//            String api_method = "POST";
//            String url = "/check-product-price";
//
//            GsonBuilder builder = new GsonBuilder();
//            builder.serializeNulls();
//            Gson gson = builder.create();
//
////            Type list_type = new TypeToken<List<mdlReason>>(){}.getType();
////            List<mdlReason> paramResult = gson.fromJson(param, list_type);
//            mdlProductPrice paramResult;
//            paramResult = gson.fromJson(param, mdlProductPrice.class);
//
////            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v2(authorization, key, url, timestamp, signature, api_method, String.valueOf(param));
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing
//
//            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffa"; //only-testing
////                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live
//
//                List<mdlProductPrice> _mdlProductPriceList = adapter.ProductPriceAdapter.GetProductPriceCheck(paramResult, db_name);
//
//                if (!paramResult.product_id.equals("") || !paramResult.branch_id.equals("")) {
//                    if (_mdlProductPriceList.size() == 0 || _mdlProductPriceList == null) {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = false;
//                    } else {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = true;
//                    }
//                } else {
//                    _mdlErrorSchema.error_code = "ERR-99-008";
//                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                    _mdlResultFinal.value = null;
//                }
//                return _mdlResultFinal;
//
//            } else {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return _mdlResultFinal;
//            }
//
//        } catch (Exception ex) {
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            _mdlErrorSchema.error_code = "ERR-99-999";
//            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//            _mdlResultFinal.value = ex.toString();
//            return _mdlResultFinal;
//        }
//
//    }
//
//    @RequestMapping(value = "/check-employee", method = RequestMethod.POST)
//    public @ResponseBody
//    mdlResultFinal GetEmployeeCheck_2(
//            HttpServletResponse response,
////            @RequestHeader String authorization,
////            @RequestHeader String key,
////            @RequestHeader String timestamp,
////            @RequestHeader String signature,
//            @RequestBody String param
//    ) {
//
//        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
//        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
//
//        try {
//            String api_method = "POST";
//            String url = "/check-employee";
//
//            GsonBuilder builder = new GsonBuilder();
//            builder.serializeNulls();
//            Gson gson = builder.create();
//
////            Type list_type = new TypeToken<List<mdlReason>>(){}.getType();
////            List<mdlReason> paramResult = gson.fromJson(param, list_type);
//            mdlEmployee paramResult;
//            paramResult = gson.fromJson(param, mdlEmployee.class);
//
////            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v2(authorization, key, url, timestamp, signature, api_method, String.valueOf(param));
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing
//
//            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffa"; //only-testing
////                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live
//
//                List<mdlEmployee> _mdlEmployeeList = adapter.EmployeeAdapter.GetEmployeeCheck(paramResult, db_name);
//
//                if (!paramResult.employee_id.equals("")) {
//                    if (_mdlEmployeeList.size() == 0 || _mdlEmployeeList == null) {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = false;
//                    } else {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = true;
//                    }
//                } else {
//                    _mdlErrorSchema.error_code = "ERR-99-008";
//                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                    _mdlResultFinal.value = null;
//                }
//                return _mdlResultFinal;
//
//            } else {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return _mdlResultFinal;
//            }
//
//        } catch (Exception ex) {
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            _mdlErrorSchema.error_code = "ERR-99-999";
//            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//            _mdlResultFinal.value = ex.toString();
//            return _mdlResultFinal;
//        }
//
//    }
//
//
//    @RequestMapping(value = "/check-product", method = RequestMethod.POST)
//    public @ResponseBody
//    mdlResultFinal GetProductCheck(
//            HttpServletResponse response,
////            @RequestHeader String authorization,
////            @RequestHeader String key,
////            @RequestHeader String timestamp,
////            @RequestHeader String signature,
//            @RequestBody String param
//    ) {
//
//        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
//        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
//
//        try {
//            String api_method = "POST";
//            String url = "/check-product";
//
//            GsonBuilder builder = new GsonBuilder();
//            builder.serializeNulls();
//            Gson gson = builder.create();
//
////            Type list_type = new TypeToken<List<mdlReason>>(){}.getType();
////            List<mdlReason> paramResult = gson.fromJson(param, list_type);
//            mdlProduct paramResult;
//            paramResult = gson.fromJson(param, mdlProduct.class);
//
//
////            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v2(authorization, key, url, timestamp, signature, api_method, String.valueOf(param));
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing
//
//            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffa"; //only-testing
////                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live
//
//                List<mdlProduct> _mdlProductList = adapter.ProductAdapter.GetProductCheck(paramResult, db_name);
//
//                if (!paramResult.product_id.equals("")) {
//                    if (_mdlProductList.size() == 0 || _mdlProductList == null) {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = false;
//                    } else {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = true;
//                    }
//                } else {
//                    _mdlErrorSchema.error_code = "ERR-99-008";
//                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                    _mdlResultFinal.value = null;
//                }
//                return _mdlResultFinal;
//
//            } else {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return _mdlResultFinal;
//            }
//
//        } catch (Exception ex) {
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            _mdlErrorSchema.error_code = "ERR-99-999";
//            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//            _mdlResultFinal.value = ex.toString();
//            return _mdlResultFinal;
//        }
//
//    }
//
//
//    @RequestMapping(value = "/check-product-group", method = RequestMethod.POST)
//    public @ResponseBody
//    mdlResultFinal GetProductGroupCheck(
//            HttpServletResponse response,
////            @RequestHeader String authorization,
////            @RequestHeader String key,
////            @RequestHeader String timestamp,
////            @RequestHeader String signature,
//            @RequestBody String param
//    ) {
//
//        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
//        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
//
//        try {
//            String api_method = "POST";
//            String url = "/check-product-group";
//
//            GsonBuilder builder = new GsonBuilder();
//            builder.serializeNulls();
//            Gson gson = builder.create();
//
////            Type list_type = new TypeToken<List<mdlReason>>(){}.getType();
////            List<mdlReason> paramResult = gson.fromJson(param, list_type);
//            mdlProductGroup paramResult;
//            paramResult = gson.fromJson(param, mdlProductGroup.class);
//
//
////            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v2(authorization, key, url, timestamp, signature, api_method, String.valueOf(param));
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing
//
//            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffa"; //only-testing
////                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live
//
//                List<mdlProductGroup> _mdlProductGroupList = adapter.ProductGroupAdapter.GetProductGroupCheck(paramResult, db_name);
//
//                if (!paramResult.product_group_id.equals("")) {
//                    if (_mdlProductGroupList.size() == 0 || _mdlProductGroupList == null) {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = false;
//                    } else {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = true;
//                    }
//                } else {
//                    _mdlErrorSchema.error_code = "ERR-99-008";
//                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                    _mdlResultFinal.value = null;
//                }
//                return _mdlResultFinal;
//
//            } else {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return _mdlResultFinal;
//            }
//
//        } catch (Exception ex) {
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            _mdlErrorSchema.error_code = "ERR-99-999";
//            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//            _mdlResultFinal.value = ex.toString();
//            return _mdlResultFinal;
//        }
//
//    }
//
//    @RequestMapping(value = "/check-product-uom", method = RequestMethod.POST)
//    public @ResponseBody
//    mdlResultFinal GetProductUOMCheck(
//            HttpServletResponse response,
////            @RequestHeader String authorization,
////            @RequestHeader String key,
////            @RequestHeader String timestamp,
////            @RequestHeader String signature,
//            @RequestBody String param
//    ) {
//
//        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
//        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
//
//        try {
//            String api_method = "POST";
//            String url = "/check-product-uom";
//
//            GsonBuilder builder = new GsonBuilder();
//            builder.serializeNulls();
//            Gson gson = builder.create();
//
////            Type list_type = new TypeToken<List<mdlReason>>(){}.getType();
////            List<mdlReason> paramResult = gson.fromJson(param, list_type);
//            mdlProductUom paramResult;
//            paramResult = gson.fromJson(param, mdlProductUom.class);
//
//
////            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v2(authorization, key, url, timestamp, signature, api_method, String.valueOf(param));
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing
//
//            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffa"; //only-testing
////                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live
//
//                List<mdlProductUom> _mdlProductUomList = adapter.ProductUomAdapter.GetProductUomCheck(paramResult, db_name);
//
//                if (!paramResult.product_id.equals("") || !paramResult.uom.equals("")) {
//                    if (_mdlProductUomList.size() == 0 || _mdlProductUomList == null) {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = false;
//                    } else {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = true;
//                    }
//                } else {
//                    _mdlErrorSchema.error_code = "ERR-99-008";
//                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                    _mdlResultFinal.value = null;
//                }
//                return _mdlResultFinal;
//
//            } else {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return _mdlResultFinal;
//            }
//
//        } catch (Exception ex) {
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            _mdlErrorSchema.error_code = "ERR-99-999";
//            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//            _mdlResultFinal.value = ex.toString();
//            return _mdlResultFinal;
//        }
//
//    }
//
//
//    @RequestMapping(value = "/check-employee-type", method = RequestMethod.POST)
//    public @ResponseBody
//    mdlResultFinal GetEmployeeTypeCheck(
//            HttpServletResponse response,
////            @RequestHeader String authorization,
////            @RequestHeader String key,
////            @RequestHeader String timestamp,
////            @RequestHeader String signature,
//            @RequestBody String param
//    ) {
//
//        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
//        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
//
//        try {
//            String api_method = "POST";
//            String url = "/check-employee-type";
//
//            GsonBuilder builder = new GsonBuilder();
//            builder.serializeNulls();
//            Gson gson = builder.create();
//
////            Type list_type = new TypeToken<List<mdlReason>>(){}.getType();
////            List<mdlReason> paramResult = gson.fromJson(param, list_type);
//            mdlEmployeeType paramResult;
//            paramResult = gson.fromJson(param, mdlEmployeeType.class);
//
//
////            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v2(authorization, key, url, timestamp, signature, api_method, String.valueOf(param));
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing
//
//            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffa"; //only-testing
////                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live
//
//                List<mdlEmployeeType> _mdlEmployeeTypeList = adapter.EmployeeTypeAdapter.GetEmployeeTypeCheck(paramResult, db_name);
//
//                if (!paramResult.employee_type_id.equals("")) {
//                    if (_mdlEmployeeTypeList.size() == 0 || _mdlEmployeeTypeList == null) {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = false;
//                    } else {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = true;
//                    }
//                } else {
//                    _mdlErrorSchema.error_code = "ERR-99-008";
//                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                    _mdlResultFinal.value = null;
//                }
//                return _mdlResultFinal;
//
//            } else {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return _mdlResultFinal;
//            }
//
//        } catch (Exception ex) {
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            _mdlErrorSchema.error_code = "ERR-99-999";
//            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//            _mdlResultFinal.value = ex.toString();
//            return _mdlResultFinal;
//        }
//
//    }
//
//
//    @RequestMapping(value = "/check-customer-type", method = RequestMethod.POST)
//    public @ResponseBody
//    mdlResultFinal GetCustomerTypeCheck(
//            HttpServletResponse response,
////            @RequestHeader String authorization,
////            @RequestHeader String key,
////            @RequestHeader String timestamp,
////            @RequestHeader String signature,
//            @RequestBody String param
//    ) {
//
//        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
//        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
//
//        try {
//            String api_method = "POST";
//            String url = "/check-customer-type";
//
//            GsonBuilder builder = new GsonBuilder();
//            builder.serializeNulls();
//            Gson gson = builder.create();
//
////            Type list_type = new TypeToken<List<mdlReason>>(){}.getType();
////            List<mdlReason> paramResult = gson.fromJson(param, list_type);
//            mdlCustomerType paramResult;
//            paramResult = gson.fromJson(param, mdlCustomerType.class);
//
//
////            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v2(authorization, key, url, timestamp, signature, api_method, String.valueOf(param));
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing
//
//            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffa"; //only-testing
////                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live
//
//                List<mdlCustomerType> _mdlCustomerTypeList = adapter.CustomerTypeAdapter.GetCustomerTypeCheck(paramResult, db_name);
//
//                if (!paramResult.customer_type_id.equals("")) {
//                    if (_mdlCustomerTypeList.size() == 0 || _mdlCustomerTypeList == null) {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = false;
//                    } else {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = true;
//                    }
//                } else {
//                    _mdlErrorSchema.error_code = "ERR-99-008";
//                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                    _mdlResultFinal.value = null;
//                }
//                return _mdlResultFinal;
//
//            } else {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return _mdlResultFinal;
//            }
//
//        } catch (Exception ex) {
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            _mdlErrorSchema.error_code = "ERR-99-999";
//            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//            _mdlResultFinal.value = ex.toString();
//            return _mdlResultFinal;
//        }
//
//    }
//
//    @RequestMapping(value = "/check-product-type", method = RequestMethod.POST)
//    public @ResponseBody
//    mdlResultFinal GetProductTypeCheck(
//            HttpServletResponse response,
////            @RequestHeader String authorization,
////            @RequestHeader String key,
////            @RequestHeader String timestamp,
////            @RequestHeader String signature,
//            @RequestBody String param
//    ) {
//
//        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
//        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
//
//        try {
//            String api_method = "POST";
//            String url = "/check-product-type";
//
//            GsonBuilder builder = new GsonBuilder();
//            builder.serializeNulls();
//            Gson gson = builder.create();
//
////            Type list_type = new TypeToken<List<mdlReason>>(){}.getType();
////            List<mdlReason> paramResult = gson.fromJson(param, list_type);
//            mdlProductType paramResult;
//            paramResult = gson.fromJson(param, mdlProductType.class);
//
//
////            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v2(authorization, key, url, timestamp, signature, api_method, String.valueOf(param));
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing
//
//            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffa"; //only-testing
////                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live
//
//                List<mdlProductType> _mdlProductTypeList = adapter.ProductTypeAdapter.GetProductTypeCheck(paramResult, db_name);
//
//                if (!paramResult.product_type_id.equals("")) {
//                    if (_mdlProductTypeList.size() == 0 || _mdlProductTypeList == null) {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = false;
//                    } else {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = true;
//                    }
//                } else {
//                    _mdlErrorSchema.error_code = "ERR-99-008";
//                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                    _mdlResultFinal.value = null;
//                }
//                return _mdlResultFinal;
//
//            } else {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return _mdlResultFinal;
//            }
//
//        } catch (Exception ex) {
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            _mdlErrorSchema.error_code = "ERR-99-999";
//            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//            _mdlResultFinal.value = ex.toString();
//            return _mdlResultFinal;
//        }
//
//    }
//
//    @RequestMapping(value = "/check-uom", method = RequestMethod.POST)
//    public @ResponseBody
//    mdlResultFinal GetUOMCheck(
//            HttpServletResponse response,
////            @RequestHeader String authorization,
////            @RequestHeader String key,
////            @RequestHeader String timestamp,
////            @RequestHeader String signature,
//            @RequestBody String param
//    ) {
//
//        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
//        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
//
//        try {
//            String api_method = "POST";
//            String url = "/check-uom";
//
//            GsonBuilder builder = new GsonBuilder();
//            builder.serializeNulls();
//            Gson gson = builder.create();
//
////            Type list_type = new TypeToken<List<mdlReason>>(){}.getType();
////            List<mdlReason> paramResult = gson.fromJson(param, list_type);
//            mdlUom paramResult;
//            paramResult = gson.fromJson(param, mdlUom.class);
//
//
////            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v2(authorization, key, url, timestamp, signature, api_method, String.valueOf(param));
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing
//
//            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffa"; //only-testing
////                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live
//
//                List<mdlUom> _mdlUomList = adapter.UomAdapter.GetUomCheck(paramResult, db_name);
//
//                if (!paramResult.uom.equals("")) {
//                    if (_mdlUomList.size() == 0 || _mdlUomList == null) {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = false;
//                    } else {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = true;
//                    }
//                } else {
//                    _mdlErrorSchema.error_code = "ERR-99-008";
//                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                    _mdlResultFinal.value = null;
//                }
//                return _mdlResultFinal;
//
//            } else {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return _mdlResultFinal;
//            }
//
//        } catch (Exception ex) {
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            _mdlErrorSchema.error_code = "ERR-99-999";
//            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//            _mdlResultFinal.value = ex.toString();
//            return _mdlResultFinal;
//        }
//
//    }
//
//    @RequestMapping(value = "/check-region", method = RequestMethod.POST)
//    public @ResponseBody
//    mdlResultFinal GetRegionCheck(
//            HttpServletResponse response,
////            @RequestHeader String authorization,
////            @RequestHeader String key,
////            @RequestHeader String timestamp,
////            @RequestHeader String signature,
//            @RequestBody String param
//    ) {
//
//        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
//        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
//
//        try {
//            String api_method = "POST";
//            String url = "/check-region";
//
//            GsonBuilder builder = new GsonBuilder();
//            builder.serializeNulls();
//            Gson gson = builder.create();
//
////            Type list_type = new TypeToken<List<mdlReason>>(){}.getType();
////            List<mdlReason> paramResult = gson.fromJson(param, list_type);
//            mdlRegion paramResult;
//            paramResult = gson.fromJson(param, mdlRegion.class);
//
//
////            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v2(authorization, key, url, timestamp, signature, api_method, String.valueOf(param));
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing
//
//            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffa"; //only-testing
////                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live
//
//                List<mdlRegion> _mdlRegionList = adapter.RegionAdapter.GetRegionCheck(paramResult, db_name);
//
//                if (!paramResult.region_id.equals("")) {
//                    if (_mdlRegionList.size() == 0 || _mdlRegionList == null) {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = false;
//                    } else {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = true;
//                    }
//                } else {
//                    _mdlErrorSchema.error_code = "ERR-99-008";
//                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                    _mdlResultFinal.value = null;
//                }
//                return _mdlResultFinal;
//
//            } else {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return _mdlResultFinal;
//            }
//
//        } catch (Exception ex) {
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            _mdlErrorSchema.error_code = "ERR-99-999";
//            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//            _mdlResultFinal.value = ex.toString();
//            return _mdlResultFinal;
//        }
//
//    }
//
//    @RequestMapping(value = "/check-customer-coordinate", method = RequestMethod.POST)
//    public @ResponseBody
//    mdlResultFinal GetCustomerCoordinateCheck(
//            HttpServletResponse response,
////            @RequestHeader String authorization,
////            @RequestHeader String key,
////            @RequestHeader String timestamp,
////            @RequestHeader String signature,
//            @RequestBody String param
//    ) {
//
//        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
//        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
//
//        try {
//            String api_method = "POST";
//            String url = "/check-customer-coordinate";
//
//            GsonBuilder builder = new GsonBuilder();
//            builder.serializeNulls();
//            Gson gson = builder.create();
//
////            Type list_type = new TypeToken<List<mdlReason>>(){}.getType();
////            List<mdlReason> paramResult = gson.fromJson(param, list_type);
//            mdlCustomerCoordinate paramResult;
//            paramResult = gson.fromJson(param, mdlCustomerCoordinate.class);
//
//
////            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v2(authorization, key, url, timestamp, signature, api_method, String.valueOf(param));
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing
//
//            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffa"; //only-testing
////                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live
//
//                List<mdlCustomerCoordinate> _mdlCustomerCoordinateList = adapter.CustomerCoordinateAdapter.GetCustomerCoordinateCheck(paramResult, db_name);
//
//                if (!paramResult.customer_id.equals("")) {
//                    if (_mdlCustomerCoordinateList.size() == 0 || _mdlCustomerCoordinateList == null) {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = false;
//                    } else {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = true;
//                    }
//                } else {
//                    _mdlErrorSchema.error_code = "ERR-99-008";
//                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                    _mdlResultFinal.value = null;
//                }
//                return _mdlResultFinal;
//
//            } else {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return _mdlResultFinal;
//            }
//
//        } catch (Exception ex) {
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            _mdlErrorSchema.error_code = "ERR-99-999";
//            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//            _mdlResultFinal.value = ex.toString();
//            return _mdlResultFinal;
//        }
//
//    }
//
//    @RequestMapping(value = "/check-posm-product", method = RequestMethod.POST)
//    public @ResponseBody
//    mdlResultFinal GetPOSMProductCheck(
//            HttpServletResponse response,
////            @RequestHeader String authorization,
////            @RequestHeader String key,
////            @RequestHeader String timestamp,
////            @RequestHeader String signature,
//            @RequestBody String param
//    ) {
//
//        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
//        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
//
//        try {
//            String api_method = "POST";
//            String url = "/check-posm-product";
//
//            GsonBuilder builder = new GsonBuilder();
//            builder.serializeNulls();
//            Gson gson = builder.create();
//
////            Type list_type = new TypeToken<List<mdlReason>>(){}.getType();
////            List<mdlReason> paramResult = gson.fromJson(param, list_type);
//            mdlPOSMProduct paramResult;
//            paramResult = gson.fromJson(param, mdlPOSMProduct.class);
//
//
////            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v2(authorization, key, url, timestamp, signature, api_method, String.valueOf(param));
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing
//
//            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffa"; //only-testing
////                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live
//
//                List<mdlPOSMProduct> _mdlPOSMProductList = adapter.POSMProductAdapter.GetPOSMProductCheck(paramResult, db_name);
//
//                if (!paramResult.posm_id.equals("")) {
//                    if (_mdlPOSMProductList.size() == 0 || _mdlPOSMProductList == null) {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = false;
//                    } else {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = true;
//                    }
//                } else {
//                    _mdlErrorSchema.error_code = "ERR-99-008";
//                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                    _mdlResultFinal.value = null;
//                }
//                return _mdlResultFinal;
//
//            } else {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return _mdlResultFinal;
//            }
//
//        } catch (Exception ex) {
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            _mdlErrorSchema.error_code = "ERR-99-999";
//            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//            _mdlResultFinal.value = ex.toString();
//            return _mdlResultFinal;
//        }
//
//    }
//
//
//    @RequestMapping(value = "/download-all-data", method = RequestMethod.POST)
//    public @ResponseBody
//    mdlResultFinal DownloadAllData(
//            HttpServletResponse response,
//            @RequestBody String param
//    ) {
//
//        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
//        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
//
//        try {
//
//            GsonBuilder builder = new GsonBuilder();
//            builder.serializeNulls();
//            Gson gson = builder.create();
//
//            Type listType = new TypeToken<mdlDownloadParam>() {
//            }.getType();
//            mdlDownloadParam paramResult = gson.fromJson(param, listType);
//
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing
//
//            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//
//                db_name = "db_grc0"; //only-testing
//
//                if (!paramResult.employee_id.equals("")) {
//
//                    mdlDownloadResult _mdlDownloadResult = new mdlDownloadResult();
//
//                    List<model.Callplan.mdlCallplan> _mdlCallplanList;
//                    List<model.Customer.mdlCustomer> _mdlCustomerList;
//                    List<model.Customer.mdlCustomerType> _mdlCustomerTypeList;
//                    List<model.Product.mdlProduct> _mdlProductList;
//                    List<model.Product.mdlProductUom> _mdlProductUomList;
//                    List<model.Branch.mdlBranch> _mdlBranchList;
//                    List<model.Cost.mdlCost> _mdlCostList;
//                    List<model.Message.mdlDailyMessage> _mdlDailyMessageList;
//                    List<model.Reason.mdlReason> _mdlReasonList;
//                    List<model.Question.mdlQuestion> _mdlQuestionList;
//                    List<model.Question.mdlQuestionSet> _mdlQuestionSetList;
//                    List<model.Question.mdlQuestionCategory> _mdlQuestionCategoryList;
//                    List<model.Answer.mdlAnswer> _mdlAnswerList;
//                    List<model.Answer.mdlAnswerType> _mdlAnswerTypeList;
//                    List<model.Competitor.mdlCompetitor> _mdlCompetitorList;
//                    List<model.Promo.mdlPromo> _mdlPromoList;
//
//                    _mdlCallplanList = CallplanAdapter.GetCallplan(paramResult, db_name);
//                    _mdlCustomerList = CustomerAdapter.GetCustomer(paramResult, db_name);
//                    _mdlCustomerTypeList = CustomerAdapter.GetCustomerType(paramResult, db_name);
//                    _mdlProductList = ProductAdapter.GetProduct(paramResult, db_name);
//                    _mdlProductUomList = ProductAdapter.GetProductUom(paramResult, db_name);
//                    _mdlBranchList = BranchAdapter.GetBranch(paramResult, db_name);
//                    _mdlCostList = CostAdapter.GetCost(paramResult, db_name);
//                    _mdlDailyMessageList = DailyMessageAdapter.GetDailyMessage(paramResult, db_name);
//                    _mdlReasonList = ReasonAdapter.GetReason(paramResult, db_name);
//                    _mdlQuestionList = QuestionAdapter.GetQuestion(paramResult, db_name);
//                    _mdlQuestionSetList = QuestionAdapter.GetQuestionSet(paramResult, db_name);
//                    _mdlQuestionCategoryList = QuestionAdapter.GetQuestionCategory(paramResult, db_name);
//                    _mdlAnswerList = AnswerAdapter.GetAnswer(paramResult, db_name);
//                    _mdlAnswerTypeList = AnswerAdapter.GetAnswerType(paramResult, db_name);
//                    _mdlCompetitorList = CompetitorAdapter.GetCompetitor(paramResult, db_name);
//                    _mdlPromoList = PromoAdapter.GetPromo(paramResult, db_name);
//
//
//                    _mdlDownloadResult.call_plan_list = _mdlCallplanList;
//                    _mdlDownloadResult.customer_list = _mdlCustomerList;
//                    _mdlDownloadResult.customer_type = _mdlCustomerTypeList;
//                    _mdlDownloadResult.product = _mdlProductList;
//                    _mdlDownloadResult.product_uom = _mdlProductUomList;
//                    _mdlDownloadResult.branch = _mdlBranchList;
//                    _mdlDownloadResult.cost = _mdlCostList;
//                    _mdlDownloadResult.daily_message = _mdlDailyMessageList;
//                    _mdlDownloadResult.reason = _mdlReasonList;
//                    _mdlDownloadResult.question = _mdlQuestionList;
//                    _mdlDownloadResult.question_category = _mdlQuestionCategoryList;
//                    _mdlDownloadResult.question_set = _mdlQuestionSetList;
//                    _mdlDownloadResult.answer = _mdlAnswerList;
//                    _mdlDownloadResult.answer_type = _mdlAnswerTypeList;
//                    _mdlDownloadResult.promo = _mdlPromoList;
//                    _mdlDownloadResult.competitor = _mdlCompetitorList;
//
//                    if (_mdlDownloadResult.call_plan_list != null || _mdlDownloadResult.customer_list != null) {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = _mdlDownloadResult;
//                    } else {
//                        _mdlErrorSchema.error_code = "ERR-99-008";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = null;
//                    }
//                } else {
//                    _mdlErrorSchema.error_code = "ERR-99-001";
//                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                    _mdlResultFinal.value = null;
//                }
//                return _mdlResultFinal;
//            } else {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return _mdlResultFinal;
//            }
//        } catch (Exception ex) {
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            _mdlErrorSchema.error_code = "ERR-99-999";
//            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//            _mdlResultFinal.value = ex.toString();
//            return _mdlResultFinal;
//        }
//    }
//
//    //Upload Product Price
//    @RequestMapping(value = "/upload-product-price",
//            method = RequestMethod.POST)
//
//    public @ResponseBody
//    mdlResultFinal UploadProductPrice(
//            HttpServletResponse response,
////            @RequestHeader String authorization,
////            @RequestHeader String key,
////            @RequestHeader String timestamp,
////            @RequestHeader String signature,
//            @RequestBody String param) {
//
//        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
//        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
//
//        try {
//            String apiMethod = "POST";
//            String url = "/upload-product-price";
//
//            GsonBuilder builder = new GsonBuilder();
//            builder.serializeNulls();
//            Gson gson = builder.create();
//
//            mdlProductPriceUpload paramResult;
//            paramResult = gson.fromJson(param, mdlProductPriceUpload .class);
//
////            mdlProductPrice paramResult;
////            paramResult = gson.fromJson(param, mdlProductPrice.class);
//
////            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v2(authorization, key, url, timestamp, signature, apiMethod, String.valueOf(param));
//            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing
//
//            if (_mdlResultFinal.status_code.equals("ERR-00-000")) {
//                db_name = "db_ffs"; //only-testing
////                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live
//
////                List<mdlProductPrice> branchs = param_result;
////
////                for (mdlProductPrice branch : branchs) {
//
//                    boolean result = adapter.ProductPriceAdapter.UploadProductPrice(paramResult, db_name);
//
//                    if (result == true) {
//                        _mdlErrorSchema.error_code = "ERR-00-000";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = paramResult;
//                    } else {
//                        _mdlErrorSchema.error_code = "ERR-99-008";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = paramResult;
//                    }
//
//                    return _mdlResultFinal;
////                }
//
//
//            } else {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return _mdlResultFinal;
//            }
//
//        } catch (Exception ex) {
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            _mdlErrorSchema.error_code = "ERR-99-999";
//            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//            _mdlResultFinal.value = ex.toString();
//            return _mdlResultFinal;
//        }
//
//    }

}
