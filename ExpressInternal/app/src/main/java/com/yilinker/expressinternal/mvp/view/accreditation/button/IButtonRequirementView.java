package com.yilinker.expressinternal.mvp.view.accreditation.button;

import com.yilinker.expressinternal.mvp.model.AccreditationRequirement;
import com.yilinker.expressinternal.mvp.model.ButtonRequirementImage;

import java.util.List;

/**
 * Created by J.Bautista on 3/29/16.
 */
public interface IButtonRequirementView {

    public void addImageItem(ButtonRequirementImage item);
    public void deleteImageItem(ButtonRequirementImage item);
    public void triggerUploadButton(AccreditationRequirement item);
    public void loadImageData(List<ButtonRequirementImage> items);


}
