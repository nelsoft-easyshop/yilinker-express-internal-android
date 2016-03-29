package com.yilinker.expressinternal.mvp.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by J.Bautista on 3/16/16.
 */
public class AccreditationRequirement {

    private int id;
    private String key;
    private String label;
    private int type;
    private List<AccreditationRequirementData> data;
    private boolean isRequired;
    private List<String> validations;
    private List<String> selectedValues;
    private String inputValue;
    private String inputKey;

    public AccreditationRequirement() {
    }

    public AccreditationRequirement(com.yilinker.core.model.express.internal.AccreditationRequirement object) {

        this.key = object.getId();
        this.label = object.getLabel();
        this.type = object.getType();
        this.validations = Arrays.asList(object.getValidation());
        this.isRequired = object.isRequired();

        List<com.yilinker.core.model.express.internal.AccreditationRequirementData> requirementData = object.getData();
        data = new ArrayList<>();

        int i = 0;
        AccreditationRequirementData requirementItem = null;
        for(com.yilinker.core.model.express.internal.AccreditationRequirementData item : requirementData ){

            requirementItem = new AccreditationRequirementData(item);
            requirementItem.setId(i);

            data.add(requirementItem);
            i++;
        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<AccreditationRequirementData> getData() {
        return data;
    }

    public void setData(List<AccreditationRequirementData> data) {
        this.data = data;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setIsRequired(boolean isRequired) {
        this.isRequired = isRequired;
    }

    public List<String> getValidations() {
        return validations;
    }

    public void setValidations(List<String> validations) {
        this.validations = validations;
    }

    public List<String> getSelectedValues() {
        return selectedValues;
    }

    public void setSelectedValues(List<String> selectedValues) {
        this.selectedValues = selectedValues;
    }

    public String getInputValue() {
        return inputValue;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }

    public String getInputKey() {
        return inputKey;
    }

    public void setInputKey(String inputKey) {
        this.inputKey = inputKey;
    }
}
