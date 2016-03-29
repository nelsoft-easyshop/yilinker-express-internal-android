package com.yilinker.expressinternal.mvp.model;

/**
 * Created by J.Bautista on 3/16/16.
 */
public class AccreditationRequirementData {

    private int id;
    private String key;
    private String label;
    private boolean isSelected;

    public AccreditationRequirementData(){


    }

    public AccreditationRequirementData(com.yilinker.core.model.express.internal.AccreditationRequirementData object){

        this.key = object.getId();
        this.label = object.getLabel();

    }

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
