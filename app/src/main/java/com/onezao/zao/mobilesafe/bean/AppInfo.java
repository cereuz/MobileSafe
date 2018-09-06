package com.onezao.zao.mobilesafe.bean;

import android.graphics.drawable.Drawable;

public class AppInfo {
    public Drawable icon;
    public String name;
    public String packageName;
    public boolean isSdcard;
    public boolean isSystem;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isSdcard() {
        return isSdcard;
    }

    public void setSdcard(boolean sdcard) {
        isSdcard = sdcard;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }
}
