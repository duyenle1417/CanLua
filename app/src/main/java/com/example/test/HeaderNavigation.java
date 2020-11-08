package com.example.test;

import android.widget.TextView;

public class HeaderNavigation {
    String headerName, headerVersion;

    public HeaderNavigation(String headerName, String headerVersion) {
        this.headerName = headerName;
        this.headerVersion = headerVersion;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getHeaderVersion() {
        return headerVersion;
    }

    public void setHeaderVersion(String headerVersion) {
        this.headerVersion = headerVersion;
    }
}