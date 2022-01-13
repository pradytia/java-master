package controller.bypass;

import adapter.EmployeeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.AuthorizationAdapter;
import core.ErrorSchemaAdapter;
import model.Client.mdlDB;
import model.DataTable.mdlDataTableParam;
import model.DataTable.mdlDataTableResult;
import model.Employee.mdlEmployee;
import model.Employee.mdlEmployeeParam;
import model.ErrorSchema.mdlErrorSchema;
import model.Result.mdlResultFinal;
import model.Select2.mdlSelect2Param;
import model.User.mdlUserBranch;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/bypass")
public class EmployeeController_bypass {

    String db_name = "";
//
//    @RequestMapping(value = "/get-employee-list", method = RequestMethod.POST)
//    public @ResponseBody
//    mdlResultFinal GetEmployee_bypass(
//            @RequestHeader String authorization,
//            @RequestHeader String key,
//            @RequestHeader String timestamp,
//            @RequestHeader String signature,
//            @RequestBody String param,
//            HttpServletResponse response){
//
//        mdlResultFinal _mdlResultFinal = new mdlResultFinal();
//        mdlErrorSchema _mdlErrorSchema = new mdlErrorSchema();
//        GsonBuilder builder = new GsonBuilder();
//        builder.serializeNulls();
//        Gson gson = builder.create();
//
//        String apiMethod = "POST";
//        String url = "/get-employee-list";
//
//        mdlDataTableParam paramResult = gson.fromJson(param, mdlDataTableParam.class);
//
//       // _mdlResultFinal = AuthorizationAdapter.CheckAuthorization_v2(authorization, key, url, timestamp, signature, apiMethod, param); //only-live
//        _mdlResultFinal.status_code = "ERR-00-000"; //only-testing
//
//        if(_mdlResultFinal.status_code.equals("ERR-00-000")) {
//             db_name = "db_ffa"; //only-testing
//            //db_name = ((mdlDB) _mdlResultFinal.value).db_id; //only-live
//
//            List<mdlEmployee> _mdlEmployeeList = EmployeeAdapter.GetEmployeeWeb(paramResult, db_name);
//            int _mdlEmployeeTotal = EmployeeAdapter.GetEmployeeTotalList(paramResult, db_name);
//
//                mdlDataTableResult _mdlDataTableResult = new mdlDataTableResult();
//                _mdlDataTableResult.records_total = _mdlEmployeeTotal;
//                _mdlDataTableResult.records_filtered = _mdlEmployeeTotal;
//                _mdlDataTableResult.draw = paramResult.draw + 1;
//                _mdlDataTableResult.data = _mdlEmployeeList;
//
//                _mdlErrorSchema.error_code = "ERR-00-000";
//                _mdlResultFinal = ErrorSchemaAdapter.GetErrorSchema(_mdlErrorSchema);
//                _mdlResultFinal.value =  _mdlDataTableResult;
//
//
//            return _mdlResultFinal;
//        }
//        else {
//            return _mdlResultFinal;
//        }
//    }

}
