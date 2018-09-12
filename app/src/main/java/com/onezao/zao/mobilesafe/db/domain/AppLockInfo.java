package com.onezao.zao.mobilesafe.db.domain;

public class AppLockInfo {
    private String packagename;
    private String time;

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String mode) {
        this.packagename = mode;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
