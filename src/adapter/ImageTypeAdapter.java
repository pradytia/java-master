package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.ImageType.mdlImageType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class ImageTypeAdapter {
    final static Logger logger = LogManager.getLogger(ImageTypeAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlImageType> GetDropDownImageType(String db_name, int port) {
        String sql = "";
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();

        List<mdlImageType> mdlImageTypeList = new ArrayList<mdlImageType>();
        List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
        CachedRowSet rowset = null;

        try {
            sql = "{call sp_image_type_dropdown_get}";
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "image_type", db_name, port);

            while (rowset.next()) {
                mdlImageType _mdlImageType = new mdlImageType();
                _mdlImageType.image_type_id = rowset.getString("image_type_id");
                _mdlImageType.image_type_name = rowset.getString("image_type_name");
                mdlImageTypeList.add(_mdlImageType);
            }
        } catch (Exception ex) {
            mdlImageTypeList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", "", "", ex.toString(), "image_type", db_name), ex);
        }
        return mdlImageTypeList;
    }

}
