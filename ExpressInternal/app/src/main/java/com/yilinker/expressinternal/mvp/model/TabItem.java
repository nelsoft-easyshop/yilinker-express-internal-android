package com.yilinker.expressinternal.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Patrick on 3/1/2016.
 *
 * Modified by J.Bautista on 3/2/2016
 */
public class TabItem {

    private int id;
    private String title;
    private boolean isSelected;
    private int resourceIcon;
    private int selectedIcon;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getResourceIcon() {
        return resourceIcon;
    }

    public void setResourceIcon(int resourceIcon) {
        this.resourceIcon = resourceIcon;

    }

    public int getSelectedIcon() {
        return selectedIcon;
    }

    public void setSelectedIcon(int selectedIcon) {
        this.selectedIcon = selectedIcon;
    }
}
