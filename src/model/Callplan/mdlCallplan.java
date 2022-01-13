package model.Callplan;

public class mdlCallplan {
    public String call_plan_id;
    public String employee_id;
    public String branch_id;
    public String vehicle_id;
    public String date;
    public String created_by;
    public String created_date;
    public String last_updated_by;
    public String last_date;
    public String is_finish;
    public String is_download;

    public mdlCallplan() {
    }

    public String getCall_plan_id() {
        return call_plan_id;
    }

    public void setCall_plan_id(String call_plan_id) {
        this.call_plan_id = call_plan_id;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getLast_updated_by() {
        return last_updated_by;
    }

    public void setLast_updated_by(String last_updated_by) {
        this.last_updated_by = last_updated_by;
    }

    public String getLast_date() {
        return last_date;
    }

    public void setLast_date(String last_date) {
        this.last_date = last_date;
    }

    public String getIs_finish() {
        return is_finish;
    }

    public void setIs_finish(String is_finish) {
        this.is_finish = is_finish;
    }

    public String getIs_download() {
        return is_download;
    }

    public void setIs_download(String is_download) {
        this.is_download = is_download;
    }
}
