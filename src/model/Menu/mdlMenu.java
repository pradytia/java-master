package model.Menu;

public class mdlMenu {

    public String menu_id;
    public String menu_name;
    public String menu_url;
    public String type;
    public Boolean is_view;
    public Boolean is_insert;
    public Boolean is_update;
    public Boolean is_delete;
    public Boolean is_print;

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getMenu_url() {
        return menu_url;
    }

    public void setMenu_url(String menu_url) {
        this.menu_url = menu_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIs_view() {
        return is_view;
    }

    public void setIs_view(Boolean is_view) {
        this.is_view = is_view;
    }

    public Boolean getIs_insert() {
        return is_insert;
    }

    public void setIs_insert(Boolean is_insert) {
        this.is_insert = is_insert;
    }

    public Boolean getIs_update() {
        return is_update;
    }

    public void setIs_update(Boolean is_update) {
        this.is_update = is_update;
    }

    public Boolean getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(Boolean is_delete) {
        this.is_delete = is_delete;
    }

    public Boolean getIs_print() {
        return is_print;
    }

    public void setIs_print(Boolean is_print) {
        this.is_print = is_print;
    }
}
