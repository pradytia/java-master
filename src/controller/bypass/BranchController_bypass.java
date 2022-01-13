package controller.bypass;

import adapter.BranchAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import core.AuthorizationAdapter;
import core.ErrorSchemaAdapter;
import model.Branch.*;
import model.Client.mdlDB;
import model.DataTable.mdlDataTableParam;
import model.DataTable.mdlDataTableResult;
import model.Employee.mdlEmployee;
import model.ErrorSchema.mdlErrorSchema;
import model.Result.mdlResultFinal;
import model.User.mdlUserBranch;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/bypass")
public class BranchController_bypass {

    String db_name = "";

//    @RequestMapping(value = "/get-branch-list", method = RequestMethod.POST)
//    public @ResponseBody
//    mdlResultFinal GetBranch_bypass(
//            @RequestHeader String authorization,
//            @RequestHeader String key,
//            @RequestHeader String timestamp,
//            @RequestHeader String signature,
//            @RequestBody String param,
//            HttpServletResponse response
//    ){
//
//        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
//        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
//        GsonBuilder builder = new GsonBuilder();
//        builder.serializeNulls();
//        Gson gson = builder.create();
//
//        String apiMethod = "POST";
//        String url = "/get-branch-list";
//
//        mdlDataTableParam _paramResult;
//        _paramResult = gson.fromJson(param, mdlDataTableParam.class);
//
//        //_mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v2(authorization, key, url, timestamp, signature, apiMethod, param); //only-live
//        _mdlResultFinal.status_code = "ERR-00-000"; //only-testing
//
//        if(_mdlResultFinal.status_code.equals("ERR-00-000")) {
//            db_name = "db_ffa"; //only-testing
//            //db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live
//
//            List<mdlBranch> _mdlBranch =  BranchAdapter.GetBranchWeb(_paramResult,db_name);
//            int _mdlBranch_total = BranchAdapter.GetBranchTotalList(_paramResult, db_name);
//
//                mdlDataTableResult _mdlDataTableResult = new mdlDataTableResult();
//                _mdlDataTableResult.records_total = _mdlBranch_total;
//                _mdlDataTableResult.records_filtered = _mdlBranch_total;
//                _mdlDataTableResult.draw = _paramResult.draw + 1;
//                _mdlDataTableResult.data = _mdlBranch;
//
//                _mdlErrorSchema.error_code = "ERR-00-000";
//                _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                _mdlResultFinal.value =  _mdlDataTableResult;
//
//            return _mdlResultFinal;
//        }
//        else {
//            return _mdlResultFinal;
//        }
//    }
}