package model.Message;

import java.util.List;

public class mdlDailyMessage {

    public String message_id;
    public String branch_id;
    public String message_name;
    public String message_desc;
    public String message_img;
    public String img_path;
    public String start_date;
    public String end_date;
    public String message_type;
    public String created_by;
    public String created_date;
    public String updated_by;
    public String updated_date;
    public String is_active;


    public List<mdlDailyMessageDetail> mdlDailyMessageDetailsParam;

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getMessage_name() {
        return message_name;
    }

    public void setMessage_name(String message_name) {
        this.message_name = message_name;
    }

    public String getMessage_desc() {
        return message_desc;
    }

    public void setMessage_desc(String message_desc) {
        this.message_desc = message_desc;
    }

    public String getMessage_img() {
        return message_img;
    }

    public void setMessage_img(String message_img) {
        this.message_img = message_img;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public List<mdlDailyMessageDetail> getMdlDailyMessageDetailsParam() {
        return mdlDailyMessageDetailsParam;
    }

    public void setMdlDailyMessageDetailsParam(List<mdlDailyMessageDetail> mdlDailyMessageDetailsParam) {
        this.mdlDailyMessageDetailsParam = mdlDailyMessageDetailsParam;
    }
}