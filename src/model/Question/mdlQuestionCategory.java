package model.Question;

public class mdlQuestionCategory{

    public String question_category_id;
    public String question_category_text;
    public String is_active;
    public String created_by;
    public String updated_by;
    public String created_date;
    public String updated_date;

    public String getQuestion_category_id() {
        return question_category_id;
    }

    public void setQuestion_category_id(String question_category_id) {
        this.question_category_id = question_category_id;
    }

    public String getQuestion_category_text() {
        return question_category_text;
    }

    public void setQuestion_category_text(String question_category_text) {
        this.question_category_text = question_category_text;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getLast_update_by() {
        return updated_by;
    }

    public void setLast_update_by(String last_update_by) {
        this.updated_by = last_update_by;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getLast_date() {
        return updated_date;
    }

    public void setLast_date(String last_date) {
        this.updated_date = last_date;
    }
}