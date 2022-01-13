package model.Competitor;

import java.util.List;

public class mdlCompetitor{

    public String competitor_id;
    public String competitor_name;
    public String created_date;
    public String created_by;
    public String updated_date;
    public String updated_by;
    public String is_active;

    public String getCompetitor_id() {
        return competitor_id;
    }

    public void setCompetitor_id(String competitor_id) {
        this.competitor_id = competitor_id;
    }

    public String getCompetitor_name() {
        return competitor_name;
    }

    public void setCompetitor_name(String competitor_name) {
        this.competitor_name = competitor_name;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }
}