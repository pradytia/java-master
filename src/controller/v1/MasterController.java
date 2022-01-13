package controller.v1;

import adapter.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import core.*;
import model.Client.mdlDB;
import model.Download.mdlDownloadResult;
import model.ErrorSchema.mdlErrorSchema;
import model.Result.mdlResultFinal;
import model.Download.mdlDownloadParam;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static javax.servlet.http.HttpServletResponse.SC_OK;

@RestController
@RequestMapping("/v1")
public class MasterController {

    final static Logger logger = LogManager.getLogger(MasterController.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();
    String db_name = "";
    int port = 0;

//    @RequestMapping(value = "/download-all-data", method = RequestMethod.POST)
//    public @ResponseBody mdlResultFinal DownloadAllData(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            @RequestHeader String authorization,
//            @RequestHeader String key,
//            @RequestHeader String timestamp,
//            @RequestHeader String signature,
//            @RequestBody String param
//    ) {
//        port = request.getLocalPort();
//        if(port==80) port = 443;
//
//        long startTime = System.currentTimeMillis();
//        String systemFunction = Thread.currentThread().getStackTrace()[1].getMethodName();
//
//        String apiMethod = request.getMethod();
//        // get all headers for logging purpose
//        Map<String, String> headerList = Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, request::getHeader));
//        String jsonHeader = gson.toJson(headerList);
//
//        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
//        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
//        String url = "/download-all-data";
//        try {
//            Type listType = new TypeToken<mdlDownloadParam>(){}.getType();
//            mdlDownloadParam paramResult = gson.fromJson(param, listType);
//
//
//            _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v3(authorization, key, url, timestamp, signature, apiMethod, String.valueOf(param), port);
////            _mdlResultFinal.status_code = "ERR-00-000"; //only-testing
//
//            if(_mdlResultFinal.status_code.equals("ERR-00-000")) {
////                db_name = "db_ffa"; //only-testing
//                db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live
//
//
//                if(!paramResult.employee_id.equals("")) {
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
//                        response.setStatus(SC_OK);
//                        } else {
//                        _mdlErrorSchema.error_code = "ERR-99-008";
//                        _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                        _mdlResultFinal.value = null;
//                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                        logger.error(LogAdapter.logToLog4j(false, startTime, 400, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
//                    }
//                }else {
//                    _mdlErrorSchema.error_code = "ERR-99-001";
//                    _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                    _mdlResultFinal.value = null;
//                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                    logger.error(LogAdapter.logToLog4j(false, startTime, 401, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
//                }
//            }else {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                logger.error(LogAdapter.logToLog4j(false, startTime, 401, url, apiMethod, systemFunction, "header : " + jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal)));
//            }
//        }catch (Exception ex){
//            _mdlErrorSchema.error_code = "ERR-99-999";
//            _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//            _mdlResultFinal.value = ex.toString();
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            logger.error(LogAdapter.logToLog4jException(startTime, url, apiMethod, systemFunction, jsonHeader, gson.toJson(param), gson.toJson(_mdlResultFinal), ex.toString()), ex);
//        }
//
//        return _mdlResultFinal;
//
//    }
}
