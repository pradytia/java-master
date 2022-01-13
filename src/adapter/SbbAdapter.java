package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.LogAdapter;
import database.QueryAdapter;
import model.DataTable.mdlDataTableParam;
import model.Query.mdlQueryExecute;
import model.Sbb.mdlSbb;
import model.Sbb.mdlSbbUomQty;
import model.Stock.mdlStockResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.List;

public class SbbAdapter {
    final static Logger logger = LogManager.getLogger(SbbAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static boolean UploadSbbQtyVsUom(mdlSbbUomQty param, int konversiStock, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        boolean resultExecTrxStockDetail = false;
        boolean resultExecMstStock = false;
        String user = param.created_by;
        String sql = "";
        String sqlTrxStockDetail = "";
        String sqlMstStock = "";

        List<mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_sbb_uom_vs_qty_upload(?,?,?,?,?,?,?)}";
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.call_plan_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.product_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.uom));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.batch_num));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.sbb_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.qty_uom));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.created_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, user, db_name, port);

            if (resultExec) {
                List<mdlQueryExecute> listMdlQueryExecuteTrxStockDetail = new ArrayList<mdlQueryExecute>();
                double availableStock = GetSbbStockByProduct(param.product_id, param.branch_id, db_name, port);
                int availableStockInt = (int) availableStock;
                int checkTotalSBB = CheckSbbList(param.sbb_id, db_name, port);

                listMdlQueryExecuteTrxStockDetail.add(QueryAdapter.QueryParam("string", param.sbb_id));
                listMdlQueryExecuteTrxStockDetail.add(QueryAdapter.QueryParam("string", param.product_id));
                listMdlQueryExecuteTrxStockDetail.add(QueryAdapter.QueryParam("int", konversiStock));
                if (checkTotalSBB == 0) {
                    sqlTrxStockDetail = "{call sp_trx_stock_detail_upload_(?,?,?,?)}";
                    listMdlQueryExecuteTrxStockDetail.add(QueryAdapter.QueryParam("int", availableStockInt));
                } else if (checkTotalSBB > 0) {
                    sqlTrxStockDetail = "{call sp_trx_stock_detail_upload(?,?,?)}";
                }

                resultExecTrxStockDetail = QueryAdapter.QueryManipulateWithDB(sqlTrxStockDetail, listMdlQueryExecuteTrxStockDetail, functionName, user, db_name, port);
                if (resultExecTrxStockDetail) {

                    List<mdlQueryExecute> listMdlQueryExecuteMstStock = new ArrayList<mdlQueryExecute>();
                    sqlMstStock = "{call sp_trx_stock_detail_update_stock(?,?,?,?)}";
                    listMdlQueryExecuteMstStock.add(QueryAdapter.QueryParam("string", param.product_id));
                    listMdlQueryExecuteMstStock.add(QueryAdapter.QueryParam("string", param.branch_id));
                    listMdlQueryExecuteMstStock.add(QueryAdapter.QueryParam("string", param.uom));
                    listMdlQueryExecuteMstStock.add(QueryAdapter.QueryParam("string", param.qty_uom));
                    resultExecMstStock = QueryAdapter.QueryManipulateWithDB(sqlMstStock, listMdlQueryExecuteMstStock, functionName, user, db_name, port);
                }
            }

        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listMdlQueryExecute), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean UploadSbbQty(mdlSbbUomQty param, String db_name, int port) {

        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        String user = param.created_by;
        String sql = "";

        List<mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_sbb_upload_qty(?,?,?,?,?)}";
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.call_plan_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.product_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.uom));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.batch_num));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.created_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, user, db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listMdlQueryExecute), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean UploadSbb(mdlSbb param, String db_name, int port) {

        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        boolean resultExecTrxStock = false;
        String user = param.created_by;
        String sql = "";
        String sqlStock = "";

        List<mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();


        try {

            sql = "{call sp_sbb_upload(?,?,?,?,?,?,?,?,?,?,?)}";

            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.sbb_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.branch_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.employee_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.call_plan_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.product_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.uom));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.qty_uom));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.batch_num));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.expired_date));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.qty_out));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.created_by));

            resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, user, db_name, port);

            if (db_name.equals("db_ffs")) {
                if (resultExec) {
                    List<mdlQueryExecute> listMdlQueryExecuteTrxStock = new ArrayList<mdlQueryExecute>();
                    sqlStock = "{call sp_trx_stock_upload(?,?,?,?,?,?)}";
                    listMdlQueryExecuteTrxStock.add(QueryAdapter.QueryParam("string", param.sbb_id));
                    listMdlQueryExecuteTrxStock.add(QueryAdapter.QueryParam("string", param.branch_id));
                    listMdlQueryExecuteTrxStock.add(QueryAdapter.QueryParam("string", param.employee_id));
                    listMdlQueryExecuteTrxStock.add(QueryAdapter.QueryParam("string", "OUT"));
                    listMdlQueryExecuteTrxStock.add(QueryAdapter.QueryParam("string", param.expired_date));
                    listMdlQueryExecuteTrxStock.add(QueryAdapter.QueryParam("string", param.created_by));

                    resultExecTrxStock = QueryAdapter.QueryManipulateWithDB(sqlStock, listMdlQueryExecuteTrxStock, functionName, user, db_name, port);

                    if (resultExecTrxStock) {

                    }
                }
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listMdlQueryExecute), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static boolean DeleteSbb(mdlSbb param, String db_name, int port) {

        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        boolean resultDetailExec = false;
        boolean resultStockExec = false;
        boolean resultTrxStockExec = false;
        boolean resultTrxStockDetailExec = false;
        String user = param.created_by;
        String sql = "";
        String sqlDetail = "";
        String sqlStock = "";
        String sqlTrxStock = "";
        String sqlTrxStockDetail = "";

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteStockList = new ArrayList<mdlQueryExecute>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteTrxStockDetailList = new ArrayList<mdlQueryExecute>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteDetailList = new ArrayList<mdlQueryExecute>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteTrxStockList = new ArrayList<mdlQueryExecute>();
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();


        try {
            //Get OLD Stock Value By Stock ID
            double prevQuantityValue = GetTrxStockValue(param.sbb_id, param.product_id, db_name, port);
            int konversiStock = (int) prevQuantityValue;

            //Update Master Stock
            sqlStock = "{call sp_trx_stock_detail_add_stock(?,?)}";
            _mdlQueryExecuteStockList.add(QueryAdapter.QueryParam("string", param.product_id));
            _mdlQueryExecuteStockList.add(QueryAdapter.QueryParam("int", konversiStock));
            resultStockExec = QueryAdapter.QueryManipulateWithDB(sqlStock, _mdlQueryExecuteStockList, functionName, user, db_name, port);

            //Delete Trx Stock
            if (resultStockExec) {
                try {
                    sqlTrxStockDetail = "{call sp_trx_stock_detail_delete(?)}";
                    _mdlQueryExecuteTrxStockDetailList.add(QueryAdapter.QueryParam("string", param.sbb_id));
                    resultTrxStockDetailExec = QueryAdapter.QueryManipulateWithDB(sqlTrxStockDetail, _mdlQueryExecuteTrxStockDetailList, functionName, user, db_name, port);

                    if (resultTrxStockDetailExec) {
                        try {
                            sqlTrxStock = "{call sp_trx_stock_delete(?)}";

                            _mdlQueryExecuteTrxStockList.add(QueryAdapter.QueryParam("string", param.sbb_id));

                            resultTrxStockExec = QueryAdapter.QueryManipulateWithDB(sqlTrxStock, _mdlQueryExecuteTrxStockList, functionName, user, db_name, port);
                        } catch (Exception ex) {
                            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sqlTrxStock, gson.toJson(_mdlQueryExecuteTrxStockList), "", ex.toString(), user, db_name), ex);
                        }
                    }
                } catch (Exception ex) {
                    logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sqlTrxStockDetail, gson.toJson(_mdlQueryExecuteTrxStockDetailList), "", ex.toString(), user, db_name), ex);
                }

            }

            //Delete SBB
            if (resultStockExec) {
                try {
                    sqlDetail = "{call sp_sbb_detail_delete(?)}";
                    _mdlQueryExecuteDetailList.add(QueryAdapter.QueryParam("string", param.sbb_id));
                    resultDetailExec = QueryAdapter.QueryManipulateWithDB(sqlDetail, _mdlQueryExecuteDetailList, functionName, user, db_name, port);
                    if (resultDetailExec) {
                        try {
                            sql = "{call sp_sbb_delete(?)}";
                            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.sbb_id));
                            resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
                        } catch (Exception ex) {
                            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteDetailList), "", ex.toString(), user, db_name), ex);
                        }
                    }
                } catch (Exception ex) {
                    logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sqlDetail, gson.toJson(_mdlQueryExecuteDetailList), "", ex.toString(), user, db_name), ex);
                }
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteStockList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static List<mdlSbb> GetSbbWeb(mdlSbb param1, mdlDataTableParam param2, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param2.module_id;

        List<mdlSbb> _mdlSbbList = new ArrayList<mdlSbb>();
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        CachedRowSet jrs = null;

        try {
            sql = "{call sp_sbb_get_with_paging_v2(?,?,?,?,?,?)}";
            String searchString = "%" + param2.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param1.call_plan_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param2.page_number - 1) * param2.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param2.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param2.order.name));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param2.order.dir));

            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Sbb", db_name, port);
            while (jrs.next()) {
                mdlSbb _mdlsbb = new mdlSbb();
                _mdlsbb.sbb_id = jrs.getString("sbb_id");
                _mdlsbb.branch_id = jrs.getString("branch_id");
                _mdlsbb.employee_id = jrs.getString("employee_id");
                _mdlsbb.call_plan_id = jrs.getString("call_plan_id");
                _mdlsbb.product_id = jrs.getString("product_id");
                _mdlsbb.product_name = jrs.getString("product_name");
                _mdlsbb.uom = jrs.getString("uom");
                _mdlsbb.qty_uom = jrs.getString("qty_uom");
                _mdlsbb.batch_num = jrs.getString("batch_num");
                _mdlsbb.expired_date = jrs.getString("expired_date");
                _mdlsbb.qty_out = jrs.getString("qty_out");
                _mdlsbb.is_active = jrs.getString("is_active");
                _mdlsbb.created_by = jrs.getString("created_by");
                _mdlsbb.created_date = jrs.getString("created_date");
                _mdlsbb.updated_by = jrs.getString("updated_by");
                _mdlsbb.updated_date = jrs.getString("updated_date");
                _mdlsbb.stock = jrs.getString("stock");
                _mdlSbbList.add(_mdlsbb);
            }
        } catch (Exception ex) {
            _mdlSbbList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlSbbList;
    }

    public static int GetSbbList(mdlSbb param1, mdlDataTableParam param2, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        int returnValue = 0;
        String sql = "";
        String user = param2.module_id;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

        try {
            sql = "{call sp_sbb_get_total_list(?,?)}";
            String searchString = "%" + param2.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param1.call_plan_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));

            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "sbb", db_name, port);
            while (crs.next()) {
                returnValue = crs.getInt("total");
            }
        } catch (Exception ex) {
            returnValue = 0;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return returnValue;
    }

    public static List<mdlSbbUomQty> GetSbbUomQtyWeb(mdlSbbUomQty param1, mdlDataTableParam param2, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String user = param2.module_id;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<mdlSbbUomQty> _mdlSbbUomQtyList = new ArrayList<mdlSbbUomQty>();
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        CachedRowSet jrs = null;

        try {
            sql = "{call sp_sbb_uom_qty_get_with_paging(?,?,?,?,?,?)}";
            String searchString = "%" + param2.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param1.call_plan_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param1.product_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param1.batch_num));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", (param2.page_number - 1) * param2.page_size));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("int", param2.page_size));

            jrs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "Sbb", db_name, port);
            while (jrs.next()) {
                mdlSbbUomQty _mdlSbbUomQty = new mdlSbbUomQty();
                _mdlSbbUomQty.uom = jrs.getString("uom");
                _mdlSbbUomQty.qty_uom = jrs.getString("qty_uom");
                _mdlSbbUomQty.description = jrs.getString("description");
                _mdlSbbUomQtyList.add(_mdlSbbUomQty);

            }
        } catch (Exception ex) {
            _mdlSbbUomQtyList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return _mdlSbbUomQtyList;
    }

    public static int GetSbbUomQtyList(mdlSbbUomQty param1, mdlDataTableParam param2, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        int returnValue = 0;
        String sql = "";
        String user = param2.module_id;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;
        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();

        try {
            sql = "{call sp_sbb_uom_vs_qty_get_total_list(?,?,?,?)}";
            String searchString = "%" + param2.search.trim().replace(" ", "%") + "%";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param1.call_plan_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param1.product_id));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param1.batch_num));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", searchString));
            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "sbb", db_name, port);
            while (crs.next()) {
                returnValue = crs.getInt("total");
            }
        } catch (Exception ex) {
            returnValue = 0;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }

        return returnValue;
    }

    public static boolean DeleteSbbUomQty(mdlSbbUomQty param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultExec = false;
        boolean resultStockExec = false;
        boolean resultTrxStockDetailExec = false;
        String user = param.created_by;
        String sql = "";
        String sqlStock = "";
        String sqlTrxStockDetail = "";

        List<model.Query.mdlQueryExecute> _mdlQueryExecuteStockList = new ArrayList<mdlQueryExecute>();
        try {
            double prevQuantityValue = GetTrxStockValue(param.sbb_id, param.product_id, db_name, port);
            int konversiTrxStock = (int) prevQuantityValue;
            int konversiSbb = 0;
            int resultStockDetail = 0;

            mdlStockResult _mdlStockResult = new mdlStockResult();
            _mdlStockResult = GetReturnStockFromSBB(param.sbb_id, param.product_id, param.batch_num, param.uom, db_name, port);

            konversiSbb = (int) _mdlStockResult.konversiStock;
            resultStockDetail = konversiTrxStock - konversiSbb;

            sqlStock = "{call sp_trx_stock_detail_add_stock(?,?)}";
            _mdlQueryExecuteStockList.add(QueryAdapter.QueryParam("string", param.product_id));
            _mdlQueryExecuteStockList.add(QueryAdapter.QueryParam("int", konversiSbb));

            resultStockExec = QueryAdapter.QueryManipulateWithDB(sqlStock, _mdlQueryExecuteStockList, functionName, user, db_name, port);
            if (resultStockExec) {
                List<mdlQueryExecute> _mdlQueryExecuteTrxStockDetailList = new ArrayList<mdlQueryExecute>();
                if (resultStockDetail == 0) {
                    sqlTrxStockDetail = "{call sp_trx_stock_detail_delete(?)}";
                    _mdlQueryExecuteTrxStockDetailList.add(QueryAdapter.QueryParam("string", param.sbb_id));
                } else {
                    sqlTrxStockDetail = "{call sp_trx_stock_detail_upload(?,?,?)}";
                    _mdlQueryExecuteTrxStockDetailList.add(QueryAdapter.QueryParam("string", param.sbb_id));
                    _mdlQueryExecuteTrxStockDetailList.add(QueryAdapter.QueryParam("string", param.product_id));
                    _mdlQueryExecuteTrxStockDetailList.add(QueryAdapter.QueryParam("int", resultStockDetail));
                }
                resultTrxStockDetailExec = QueryAdapter.QueryManipulateWithDB(sqlTrxStockDetail, _mdlQueryExecuteTrxStockDetailList, functionName, user, db_name, port);

                if (resultTrxStockDetailExec) {
                    List<model.Query.mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
                    sql = "{call sp_sbb_uom_qty_delete(?,?,?,?,?)}";
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.call_plan_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.product_id));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.batch_num));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.uom));
                    _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", param.created_by));
                    resultExec = QueryAdapter.QueryManipulateWithDB(sql, _mdlQueryExecuteList, functionName, user, db_name, port);
                }
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sqlStock, gson.toJson(_mdlQueryExecuteStockList), "", ex.toString(), user, db_name), ex);
        }
        return resultExec;
    }

    public static Double GetTrxStockValue(String stockID, String productID, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        double returnTotal = 0;
        String sql = "";
        String user = stockID;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;

        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_trx_stock_get_total(?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", stockID));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", productID));
            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "sbb", db_name, port);
            while (crs.next()) {
                returnTotal = crs.getDouble("total");
            }
        } catch (Exception ex) {
            returnTotal = 0;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }
        return returnTotal;
    }

    public static mdlStockResult GetMasterStockValue(String branchID, String productID, String uom, String stockValue, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        mdlStockResult dataResult = new mdlStockResult();
        String sql = "";
        String user = productID;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;

        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_mst_stock_get_total(?,?,?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", branchID));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", productID));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", uom));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", stockValue));

            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "sbb", db_name, port);
            while (crs.next()) {
                dataResult.currentStock = crs.getDouble("total");
                dataResult.konversiStock = crs.getDouble("out_stock");
                dataResult.uom = crs.getString("uom");
            }
        } catch (Exception ex) {
            dataResult = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), user, db_name), ex);
        }

        return dataResult;
    }

    public static boolean GetReturnStock(String productID, int konversiStock, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = productID;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        boolean resultStockExec = false;
        String sqlStock = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteStockList = new ArrayList<mdlQueryExecute>();
        try {
            sqlStock = "{call sp_trx_stock_detail_add_stock(?,?)}";
            _mdlQueryExecuteStockList.add(QueryAdapter.QueryParam("string", productID));
            _mdlQueryExecuteStockList.add(QueryAdapter.QueryParam("int", konversiStock));
            resultStockExec = QueryAdapter.QueryManipulateWithDB(sqlStock, _mdlQueryExecuteStockList, functionName, "user", db_name, port);
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sqlStock, gson.toJson(_mdlQueryExecuteStockList), "", ex.toString(), user, db_name), ex);
        }
        return resultStockExec;
    }

    public static mdlStockResult GetReturnStockFromSBB(String sbbID, String productID, String batchNum, String uom, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = sbbID;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        mdlStockResult dataStock = new mdlStockResult();
        CachedRowSet crs = null;

        String sqlStock = "";
        List<model.Query.mdlQueryExecute> _mdlQueryExecuteStockList = new ArrayList<mdlQueryExecute>();
        try {

            sqlStock = "{call sp_sbb_uom_qty_prev_stock_get(?,?,?,?)}";
            _mdlQueryExecuteStockList.add(QueryAdapter.QueryParam("string", sbbID));
            _mdlQueryExecuteStockList.add(QueryAdapter.QueryParam("string", productID));
            _mdlQueryExecuteStockList.add(QueryAdapter.QueryParam("string", batchNum));
            _mdlQueryExecuteStockList.add(QueryAdapter.QueryParam("string", uom));

            crs = QueryAdapter.QueryExecuteWithDB(sqlStock, _mdlQueryExecuteStockList, functionName, "user", db_name, port);
            while (crs.next()) {
                dataStock.currentStock = crs.getDouble("total");
                dataStock.konversiStock = crs.getDouble("out_stock");
                dataStock.uom = crs.getString("uom");
            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sqlStock, gson.toJson(_mdlQueryExecuteStockList), "", ex.toString(), user, db_name), ex);
        }
        return dataStock;
    }

    public static double GetSbbStockByProduct(String productID, String BranchID, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        double returnValue = 0;
        String sql = "";
        String user = productID;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;

        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_sbb_stock_get(?,?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", BranchID));
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", productID));
            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "sbb", db_name, port);
            while (crs.next()) {
                returnValue = crs.getDouble("stock");
            }

        } catch (Exception ex) {
            returnValue = 0;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "", db_name), ex);
        }
        return returnValue;
    }

    public static int CheckSbbList(String sbbID, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        int returnValue = 0;
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        CachedRowSet crs = null;

        List<mdlQueryExecute> _mdlQueryExecuteList = new ArrayList<mdlQueryExecute>();
        try {
            sql = "{call sp_sbb_check_list(?)}";
            _mdlQueryExecuteList.add(QueryAdapter.QueryParam("string", sbbID));
            crs = QueryAdapter.QueryExecuteWithDB(sql, _mdlQueryExecuteList, functionName, "sbb", db_name, port);
            while (crs.next()) {
                returnValue = crs.getInt("total");
            }
        } catch (Exception ex) {
            returnValue = 0;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(_mdlQueryExecuteList), "", ex.toString(), "sbb", db_name), ex);
        }

        return returnValue;
    }

}
