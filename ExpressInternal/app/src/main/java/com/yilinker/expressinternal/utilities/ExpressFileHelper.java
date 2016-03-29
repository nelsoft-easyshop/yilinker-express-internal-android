package com.yilinker.expressinternal.utilities;

import android.content.Context;

import com.yilinker.core.helper.FileHelper;

import java.io.IOException;

/**
 * Created by J.Bautista on 3/21/16.
 */
public class ExpressFileHelper {


    public static void saveToLocal(Context context, String fileString, String fileName){

        String filePath = String.format("%s/%s", context.getFilesDir(),fileName);

        try {

            FileHelper.writeFile(context, filePath, fileString);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(Context context, String fileName){

        String filePath = String.format("%s/%s", context.getFilesDir(),fileName);
        String fileString = null;

        try {

            fileString = FileHelper.readFile(context, filePath);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileString;
    }
}
