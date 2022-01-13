package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.EncryptAdapter;
import core.LogAdapter;
import database.QueryAdapter;
import model.DataTable.mdlDataTableParam;
import model.Query.mdlQueryExecute;
import model.User.mdlChangePasswordUser;
import model.User.mdlForgotPasswordUser;
import model.User.mdlUser;
import model.User.mdlUserBranch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Properties;
import javax.sql.rowset.CachedRowSet;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserAdapter {

    final static Logger logger = LogManager.getLogger(UserAdapter.class);
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static List<mdlUser> GetUser(mdlDataTableParam param, String db_name, int port) {

        long startTime = System.currentTimeMillis();
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param.module_id;

        List<mdlUser> mdlUserList = new ArrayList<mdlUser>();
        List<mdlQueryExecute> listParam = new ArrayList<mdlQueryExecute>();
        CachedRowSet rowset = null;

        try {
            sql = "{call sp_user_get_v2 (?,?,?,?,?)}";
            listParam.add(QueryAdapter.QueryParam("int", (param.page_number - 1) * param.page_size));
            listParam.add(QueryAdapter.QueryParam("int", param.page_size));
            listParam.add(QueryAdapter.QueryParam("string", param.search));
            listParam.add(QueryAdapter.QueryParam("string", param.order.name));
            listParam.add(QueryAdapter.QueryParam("string", param.order.dir));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "User", db_name, port);

            while (rowset.next()) {
                mdlUser mdlUser = new mdlUser();
                mdlUser.user_id = rowset.getString("user_id");
                mdlUser.email = rowset.getString("email");
                mdlUser.role_id = rowset.getString("role_id");
                mdlUser.role_name = rowset.getString("role_name");
                mdlUserList.add(mdlUser);
            }
        } catch (Exception ex) {
            mdlUserList = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), user, db_name), ex);
        }
        return mdlUserList;
    }

    public static boolean InsertUser(mdlUser param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = param.created_by;
        String sql = "";
        String subSql = "";
        boolean resultExec = false;
        boolean resultSubExec = false;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();
        List<mdlQueryExecute> listSubMdlQueryExecute = new ArrayList<mdlQueryExecute>();

        try {
            String passwordEncrypt = md5(param.password);

            sql = "{call sp_user_upload(?,?,?,?,?)}";
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.user_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.email));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", passwordEncrypt));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.role_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.created_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, "user", db_name, port);

            if (resultExec) {
                subSql = "{call sp_user_branch_upload(?,?,?,?,?,?)}";

                try {
                    for (int i = 0; i < param.branch_id.size(); i++) {
                        mdlUserBranch _mdlUserBranch = new mdlUserBranch();
                        _mdlUserBranch = GetRelationBranch(param.branch_id.get(i), db_name, port);

                        listSubMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.user_id));
                        listSubMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.branch_id.get(i)));
                        listSubMdlQueryExecute.add(QueryAdapter.QueryParam("string", _mdlUserBranch.region_id));
                        listSubMdlQueryExecute.add(QueryAdapter.QueryParam("string", _mdlUserBranch.district_id));
                        listSubMdlQueryExecute.add(QueryAdapter.QueryParam("string", _mdlUserBranch.area_id));
                        listSubMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.created_by));
                        resultSubExec = QueryAdapter.QueryManipulateWithDB(subSql, listSubMdlQueryExecute, functionName, "user", db_name, port);
                    }

                    if (resultSubExec) {

                    } else {

                    }

                } catch (Exception ex) {
                    logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, subSql, gson.toJson(listSubMdlQueryExecute), "", ex.toString(), user, db_name), ex);
                    return false;
                }

            } else {

            }
        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listMdlQueryExecute), "", ex.toString(), user, db_name), ex);
            return false;
        }
        return true;

    }

    public static boolean UpdateUser(mdlUser param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String sql = "";
        String detailInsertSql = "";
        String detailDeleteSql = "";
        String user = param.created_by;
        boolean resultExec = false;
        boolean resultExecInsertDetail = false;
        boolean resultExecDeleteDetail = false;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();

        List<mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();
        List<mdlQueryExecute> listMdlQueryExecuteDelete = new ArrayList<mdlQueryExecute>();
        List<mdlQueryExecute> listMdlQueryExecuteInsert = new ArrayList<mdlQueryExecute>();

        try {
            String passwordEncrypt = md5(param.password);

            sql = "{call sp_user_upload(?,?,?,?,?)}";
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.user_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.email));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", passwordEncrypt));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.role_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.created_by));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, "user", db_name, port);

            try {

                if (resultExec) {
                    detailDeleteSql = "{call sp_user_branch_delete (?)}";

                    listMdlQueryExecuteDelete.add(QueryAdapter.QueryParam("string", param.user_id));
                    resultExecDeleteDetail = QueryAdapter.QueryManipulateWithDB(detailDeleteSql, listMdlQueryExecuteDelete, functionName, "user", db_name, port);

                    if (resultExecDeleteDetail) {
                        detailInsertSql = "{call sp_user_branch_upload(?,?,?,?,?,?)}";
                        try {
                            for (int i = 0; i < param.branch_id.size(); i++) {
                                mdlUserBranch _mdlUserBranch = new mdlUserBranch();
                                _mdlUserBranch = GetRelationBranch(param.branch_id.get(i), db_name, port);
                                listMdlQueryExecuteInsert = new ArrayList<mdlQueryExecute>();
                                listMdlQueryExecuteInsert.add(QueryAdapter.QueryParam("string", param.user_id));
                                listMdlQueryExecuteInsert.add(QueryAdapter.QueryParam("string", param.branch_id.get(i)));
                                listMdlQueryExecuteInsert.add(QueryAdapter.QueryParam("string", _mdlUserBranch.region_id));
                                listMdlQueryExecuteInsert.add(QueryAdapter.QueryParam("string", _mdlUserBranch.district_id));
                                listMdlQueryExecuteInsert.add(QueryAdapter.QueryParam("string", _mdlUserBranch.area_id));
                                listMdlQueryExecuteInsert.add(QueryAdapter.QueryParam("string", param.created_by));
                                resultExecInsertDetail = QueryAdapter.QueryManipulateWithDB(detailInsertSql, listMdlQueryExecuteInsert, functionName, "user", db_name, port);


                            }

                            if (resultExecInsertDetail) {

                            } else {

                            }

                        } catch (Exception ex) {
                            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, detailDeleteSql, gson.toJson(listMdlQueryExecuteDelete), "", ex.toString(), user, db_name), ex);
                            return false;
                        }

                    } else {

                    }

                } else {

                }
            } catch (Exception ex) {
                logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, detailDeleteSql, gson.toJson(listMdlQueryExecuteDelete), "", ex.toString(), user, db_name), ex);
            }

        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listMdlQueryExecute), "", ex.toString(), user, db_name), ex);
            return false;
        }

        return true;

    }

    public static boolean DeleteUser(mdlUser param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = param.created_by;
        String sql = "";
        String sqlDetail = "";
        boolean resultExec = false;
        boolean resultExecDetail = false;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();

        List<mdlQueryExecute> listMdlQueryExecuteDetail = new ArrayList<mdlQueryExecute>();
        List<mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();


        try {
            sqlDetail = "{call sp_user_branch_delete (?)}";

            listMdlQueryExecuteDetail.add(QueryAdapter.QueryParam("string", param.user_id));
            resultExecDetail = QueryAdapter.QueryManipulateWithDB(sqlDetail, listMdlQueryExecuteDetail, functionName, "user", db_name, port);

            if (resultExecDetail) {

                try {
                    sql = "{call sp_user_delete (?)}";

                    listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.user_id));
                    resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, "user", db_name, port);

                    if (resultExec) {

                    } else {

                    }
                } catch (Exception ex) {
                    logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listMdlQueryExecute), "", ex.toString(), user, db_name), ex);
                }

            } else {

            }

        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sqlDetail, gson.toJson(listMdlQueryExecuteDetail), "", ex.toString(), user, db_name), ex);
            return false;
        }

        return true;

    }

    public static int GetTotalUser(mdlDataTableParam param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = param.module_id;
        int return_value = 0;
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();

        List<mdlUser> mdlUserList = new ArrayList<mdlUser>();
        List<mdlQueryExecute> listParam = new ArrayList<mdlQueryExecute>();
        CachedRowSet rowset = null;

        try {
            sql = "{call sp_user_total_get (?)}";
            listParam.add(QueryAdapter.QueryParam("string", param.search));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "User", db_name, port);

            while (rowset.next()) {
                return_value = rowset.getInt("total");
            }
        } catch (Exception ex) {
            return_value = 0;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), user, db_name), ex);
        }
        return return_value;
    }

    public static int CheckOldPasswordUser(mdlUser param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = param.created_by;
        int return_value = 0;
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();

        List<mdlUser> mdlUserList = new ArrayList<mdlUser>();
        List<mdlQueryExecute> listParam = new ArrayList<mdlQueryExecute>();
        CachedRowSet rowset = null;

        try {
            String passwordEncrypt = md5(param.password);

            sql = "{call sp_user_check_old_password (?,?)}";
            listParam.add(QueryAdapter.QueryParam("string", param.user_id));
            listParam.add(QueryAdapter.QueryParam("string", passwordEncrypt));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "User", db_name, port);

            while (rowset.next()) {
                return_value = rowset.getInt("total");
            }
        } catch (Exception ex) {
            return_value = 0;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), user, db_name), ex);
        }
        return return_value;
    }

    public static boolean UpdateOldPasswordUser(mdlUser param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = param.created_by;
        String sql = "";
        boolean resultExec = false;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();

        try {

            String passwordEncrypt = md5(param.password);

            sql = "{call sp_user_upload(?,?,?)}";
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.user_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", passwordEncrypt));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.role_id));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, "user", db_name, port);

            if (resultExec) {

            } else {

            }

        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listMdlQueryExecute), "", ex.toString(), user, db_name), ex);
            return false;
        }
        return true;
    }

    public static boolean ForgotPasswordUser(String user_email, String passwordEncrypt, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = user_email;
        String sql = "";
        boolean resultExec = false;
        boolean resultEmail = false;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();

        List<mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();


        try {
            sql = "{call sp_forgot_password_user(?,?)}";
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", user_email));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", passwordEncrypt));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, "user", db_name, port);

            if (resultExec) {
                return true;
            } else {
                return false;
            }

        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listMdlQueryExecute), "", ex.toString(), user, db_name), ex);
            return false;
        }
    }

    public static boolean ChangePasswordUser(mdlChangePasswordUser param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = param.user_id;
        String sql = "";
        boolean resultExec = false;
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();

        List<mdlQueryExecute> listMdlQueryExecute = new ArrayList<mdlQueryExecute>();

        try {
            String passwordEncrypt = md5(param.password);
            sql = "{call sp_change_password_user(?,?)}";
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", param.user_id));
            listMdlQueryExecute.add(QueryAdapter.QueryParam("string", passwordEncrypt));
            resultExec = QueryAdapter.QueryManipulateWithDB(sql, listMdlQueryExecute, functionName, "user", db_name, port);

            if (resultExec) {

            } else {

            }

        } catch (Exception ex) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listMdlQueryExecute), "", ex.toString(), user, db_name), ex);
            return false;
        }

        return true;

    }

    public static String md5(String password) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashInBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }

    public static boolean SendEmail(mdlForgotPasswordUser param, String db_name, int port) throws GeneralSecurityException, IOException {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date date = new Date();
        String timestamp = dateFormat.format(date);
        String user_id = GetUserEmail(param.user_email, db_name, port);
        String password_ = EncryptAdapter.encrypt(timestamp, "Invent123");
        String passwordEncrypt = md5(password_);
        boolean resultData = false;

        String username = EncryptAdapter.decrypt(param.invent_email, "Invent123");
        String password = EncryptAdapter.decrypt(param.invent_password, "Invent123");

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("from@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(param.user_email)
            );
            message.setSubject("Invent account password reset");
            message.setText("Yth. Bpk/Ibu " + user_id
                    + "\n\n Silahkan login ulang dengan password baru berikut ini : " + password_
                    + "\n Mohon untuk segera mengganti password Anda setelah berhasil login."
                    + "\n Terimakasih."
                    + "\n\n Salam,"
                    + "\n Invent Management");

            Transport.send(message);
            System.out.println("Done");

            resultData = ForgotPasswordUser(param.user_email, passwordEncrypt, db_name, port);

            if (resultData) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, "", gson.toJson(param), "", e.toString(), "", db_name), e);
            return false;
        }
    }

    public static boolean ValidateEmailUser(mdlUser param, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String user = param.created_by;
        int return_value = 0;
        boolean result = false;
        String sql = "";

        List<mdlUser> mdlUserList = new ArrayList<mdlUser>();
        List<mdlQueryExecute> listParam = new ArrayList<mdlQueryExecute>();
        CachedRowSet rowset = null;

        try {
            sql = "{call sp_validate_email_user_get (?)}";
            listParam.add(QueryAdapter.QueryParam("string", param.email));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "User", db_name, port);

            while (rowset.next()) {
                return_value = rowset.getInt("total");
                if (return_value > 0) {
                    result = true;
                } else {
                    result = false;
                }
            }
        } catch (Exception ex) {
            result = false;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), user, db_name), ex);
        }
        return result;
    }

    public static mdlUserBranch GetRelationBranch(String branch_id, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = branch_id;
        String sql = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();

        mdlUserBranch _mdlUserBranch = new mdlUserBranch();
        List<mdlQueryExecute> listParam = new ArrayList<mdlQueryExecute>();
        CachedRowSet rowset = null;

        try {
            sql = "{call sp_relation_branch_get (?)}";
            listParam.add(QueryAdapter.QueryParam("string", branch_id));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "User", db_name, port);

            while (rowset.next()) {
                _mdlUserBranch.region_id = rowset.getString("region_id");
                _mdlUserBranch.district_id = rowset.getString("district_id");
                _mdlUserBranch.area_id = rowset.getString("area_id");
            }
        } catch (Exception ex) {
            _mdlUserBranch = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), user, db_name), ex);
        }
        return _mdlUserBranch;

    }

    public static String GetUserEmail(String email, String db_name, int port) {
        long startTime = System.currentTimeMillis();
        String user = "email";
        String sql = "";
        String user_id = "";
        String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();

        mdlUserBranch _mdlUserBranch = new mdlUserBranch();
        List<mdlQueryExecute> listParam = new ArrayList<mdlQueryExecute>();
        CachedRowSet rowset = null;

        try {
            sql = "{call sp_user_email_get (?)}";
            listParam.add(QueryAdapter.QueryParam("string", email));
            rowset = QueryAdapter.QueryExecuteWithDB(sql, listParam, functionName, "User", db_name, port);
            while (rowset.next()) {
                user_id = rowset.getString("user_id");
            }
        } catch (Exception ex) {
            _mdlUserBranch = null;
            logger.error(LogAdapter.logToLog4jException(startTime, "", "", functionName, sql, gson.toJson(listParam), "", ex.toString(), user, db_name), ex);
        }
        return user_id;

    }
}
