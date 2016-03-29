package com.yilinker.expressinternal.mvp.view.accreditation.button;

import android.app.DialogFragment;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.yilinker.expressinternal.R;

import java.io.FileNotFoundException;

/**
 * Created by J.Bautista on 3/29/16.
 */
public class DialogImagePreview extends DialogFragment {

    private static final String ARG_IMAGE = "image";

    private ImageView ivImage;

    public static DialogImagePreview createInstance(String filePath){

        DialogImagePreview fragment = new DialogImagePreview();

        Bundle bundle = new Bundle();

        bundle.putString(ARG_IMAGE, filePath);

        fragment.setArguments(bundle);

        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dialog_imagepreview, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);


    }

    private void initializeViews(View parent){

        ivImage = (ImageView) parent.findViewById(R.id.ivImage);

        setImageResource(getData());
    }

    private String getData(){

        Bundle arguments = getArguments();

        String filePath = arguments.getString(ARG_IMAGE);

        return  filePath;

    }

    private void setImageResource(String filePath){

        ivImage.setImageBitmap(decodeFromUri(filePath));
    }

    private Bitmap decodeFromUri(String path){

        Uri uri = Uri.parse(path);

        Bitmap actuallyUsableBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;

        AssetFileDescriptor fileDescriptor = null;
        try {

            fileDescriptor = getActivity().getContentResolver().openAssetFileDescriptor( uri, "r");

            actuallyUsableBitmap
                    = BitmapFactory.decodeFileDescriptor(
                    fileDescriptor.getFileDescriptor(), null, options);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return  actuallyUsableBitmap;
    }
}
