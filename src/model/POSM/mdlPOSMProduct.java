package model.POSM;

public class mdlPOSMProduct {
    public String posm_id;
    public String posm_name;
    public String total_stock;
    public String created_by;
    public String created_date;
    public String updated_by;
    public String updated_date;
    public String is_active;

    public String getPosm_id() {
        return posm_id;
    }

    public void setPosm_id(String posm_id) {
        this.posm_id = posm_id;
    }

    public String getPosm_name() {
        return posm_name;
    }

    public void setPosm_name(String posm_name) {
        this.posm_name = posm_name;
    }

    public String getTotal_stock() {
        return total_stock;
    }

    public void setTotal_stock(String total_stock) {
        this.total_stock = total_stock;
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
}
