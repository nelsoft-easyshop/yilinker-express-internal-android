package com.yilinker.expressinternal.mvp.view.checklist;

import com.yilinker.expressinternal.mvp.model.*;
import com.yilinker.expressinternal.mvp.model.Package;

/**
 * Created by wagnavu on 3/22/16.
 */
public interface IChecklistPickupView extends IChecklistBase {

    public void showConfirmPackageScreen(Package selectedPackage);
    public void goToMainScreen();

}
