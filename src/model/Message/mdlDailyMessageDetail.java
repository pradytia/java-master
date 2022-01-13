package model.Message;

import java.util.List;

public class mdlDailyMessageDetail {

    public String message_id;
    public List<String> employee_id;
    public List<String> branch_id;

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }
    

}
