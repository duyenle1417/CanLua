package com.example.test;


public class itemMenu {
    public String nameItem;
    public int icon;

    public itemMenu(String nameItem, int icon) {
        this.nameItem = nameItem;
        this.icon = icon;
    }

    public String getNameItem() {
        return nameItem;
    }

    public void setNameItem(String nameItem) {
        this.nameItem = nameItem;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
