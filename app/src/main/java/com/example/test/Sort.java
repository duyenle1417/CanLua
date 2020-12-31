package com.example.test;

public enum Sort{
    Sort1("Theo tên (A -> Z)"),
    Sort2("Theo thời gian");

    private String option;

    Sort(String option) {
        this.option = option;
    }

    public String getOption() {
        return option;
    }
}
