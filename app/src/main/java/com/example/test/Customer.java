package com.example.test;

public class Customer {
    public String HoTen, SDT, date;
    public int ID;

    public void setHoTen(String hoTen) {
        HoTen = hoTen;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getDate() {
        return date;
    }

    public String getHoTen() {
        return HoTen;
    }

    public String getSDT() {
        return SDT;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
