package model.Question;

import java.util.List;

public class mdlQuestion{

    public String question_id;
    public String question_text;
    public String answer_type_id;
    public String is_sub_question;
    public int sequence;
    public String question_set_id;
    public String question_category_id;
    public String answer_id;
    public String no;
    public String mandatory;
    public String is_active;
    public String created_by;
    public String updated_by;
    public String created_date;
    public String updated_date;
    public String is_efective_call;
    public List<String> employee_type_id;

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getQuestion_text() {
        return question_text;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }

    public String getAnswer_type_id() {
        return answer_type_id;
    }

    public void setAnswer_type_id(String answer_type_id) {
        this.answer_type_id = answer_type_id;
    }

    public String getIs_sub_question() {
        return is_sub_question;
    }

    public void setIs_sub_question(String is_sub_question) {
        this.is_sub_question = is_sub_question;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getQuestion_set_id() {
        return question_set_id;
    }

    public void setQuestion_set_id(String question_set_id) {
        this.question_set_id = question_set_id;
    }

    public String getQuestion_category_id() {
        return question_category_id;
    }

    public void setQuestion_category_id(String question_category_id) {
        this.question_category_id = question_category_id;
    }

    public String getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(String answer_id) {
        this.answer_id = answer_id;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getMandatory() {
        return mandatory;
    }

    public void setMandatory(String mandatory) {
        this.mandatory = mandatory;
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

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public String getIs_efective_call() {
        return is_efective_call;
    }

    public void setIs_efective_call(String is_efective_call) {
        this.is_efective_call = is_efective_call;
    }
}