package model.Question;

public class mdlQuestionSet{

    public String question_set_id;
    public String question_set_text;
    public String created_by;
    public String updated_by;
    public String created_date;
    public String updated_date;
    public String is_active;

    public String getQuestion_set_id() {
        return question_set_id;
    }

    public void setQuestion_set_id(String question_set_id) {
        this.question_set_id = question_set_id;
    }

    public String getQuestion_set_text() {
        return question_set_text;
    }

    public void setQuestion_set_text(String question_set_text) {
        this.question_set_text = question_set_text;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getLast_updated_by() {
        return updated_by;
    }

    public void setLast_updated_by(String last_updated_by) {
        this.updated_by = last_updated_by;
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

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }
}