package com.yilinker.expressinternal.mvp.view.accreditation.button;

import com.yilinker.expressinternal.mvp.model.ButtonRequirementImage;

/**
 * Created by J.Bautista on 3/29/16.
 */
public interface IButtonRequirementListener {

    public void onView(String filePath);
    public void onDelete(ButtonRequirementImage item);

}
