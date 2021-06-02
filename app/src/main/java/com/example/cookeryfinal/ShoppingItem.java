package com.example.cookeryfinal;

public class ShoppingItem {
    private String title;
    private boolean is_checked;

    public ShoppingItem(){
    }

    public ShoppingItem(String title, boolean is_checked){
        this.title = title;
        this.is_checked = is_checked;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }


    public boolean isIs_checked() {
        return is_checked;
    }
    public void setIs_checked(boolean is_checked) {
        this.is_checked = is_checked;
    }
}
