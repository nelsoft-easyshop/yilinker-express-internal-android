package com.yilinker.expressinternal.mvp.presenter.accreditation;

import com.yilinker.expressinternal.mvp.model.ButtonRequirementImage;
import com.yilinker.expressinternal.mvp.view.accreditation.button.ButtonRequirementViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by J.Bautista on 3/16/16.
 */
public class ButtonRequirementPresenter extends AccreditationRequirementPresenter<ButtonRequirementViewHolder>{


    @Override
    protected void updateView() {

        view().setLabelText(model.getLabel());

        List<ButtonRequirementImage> images = createImageItems();

        if(images != null) {
            view().loadImageData(images);
        }
    }

    @Override
    public void onValueChanged(Object object) {

    }

    public void OnUploadButtonClick(){

        view().triggerUploadButton(model);
    }

    public void onDeleteData(ButtonRequirementImage item){

        model.getSelectedValues().remove(item.getFilePath());
    }

    private List<ButtonRequirementImage> createImageItems(){

        List<ButtonRequirementImage> items = null;
        List<String> images = model.getSelectedValues();

        if(images != null){

            if(images.size() == 0){

                return null;
            }

            items = new ArrayList<>();
            int i = 0;
            ButtonRequirementImage imageItem = null;

            for(String file : images){

                imageItem = new ButtonRequirementImage();
                imageItem.setId(i);
                imageItem.setLabel(getFileName(file));
                imageItem.setFilePath(file);

                items.add(imageItem);

                i++;
            }

        }

        return items;

    }

    private String getFileName(String filePath){

        File file = new File(filePath);
        String fileName = file.getName();

        return fileName;
    }
}
