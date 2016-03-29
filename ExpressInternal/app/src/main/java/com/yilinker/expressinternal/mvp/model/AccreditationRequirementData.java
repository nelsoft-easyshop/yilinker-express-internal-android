package com.yilinker.expressinternal.mvp.model;

/**
 * Created by J.Bautista on 3/16/16.
 */
public class AccreditationRequirementData {

    private int id;
    private String label;
    private boolean isSelected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
