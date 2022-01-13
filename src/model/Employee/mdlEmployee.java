package model.Employee;

import java.util.List;

public class mdlEmployee {

    public String employee_id;
    public String employee_name;
    public String employee_type_id;
    public String employee_type_name;
    public String branch_id;
    public List<String> branch_id_list;
    public String entry_date;
    public String out_date;
    public String created_by;
    public String created_date;
    public String updated_by;
    public String updated_date;
    public String is_active;
    public String gender;
    public String email;
    public String phone;
    public String is_register;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getBranch_id_list() {
        return branch_id_list;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getEmployee_type_id() {
        return employee_type_id;
    }

    public void setEmployee_type_id(String employee_type_id) {
        this.employee_type_id = employee_type_id;
    }

    public String getEmployee_type_name() {
        return employee_type_name;
    }

    public void setEmployee_type_name(String employee_type_name) {
        this.employee_type_name = employee_type_name;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public void setBranch_id_list(List<String> branch_id_list) {
        this.branch_id_list = branch_id_list;
    }

    public String getEntry_date() {
        return entry_date;
    }

    public void setEntry_date(String entry_date) {
        this.entry_date = entry_date;
    }

    public String getOut_date() {
        return out_date;
    }

    public void setOut_date(String out_date) {
        this.out_date = out_date;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
