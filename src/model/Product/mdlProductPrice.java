package model.Product;

import java.util.List;

public class mdlProductPrice {
    public String product_id;
    public String price;
    public String uom;
    public String module_id;
    public String created_by;
    public String created_date;
    public String updated_by;
    public String updated_date;
    public String is_active;
    public String branch_id;

    public String product_name;
    public String branch_name;

//    public List<String> branch_id_list;


    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }



    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }


    public void setPrice(String price) {
        this.price = price;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public void setModule_id(String module_id) {
        this.module_id = module_id;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }


}
