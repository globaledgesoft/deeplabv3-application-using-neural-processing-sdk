package com.qdn.segmentation.Utils;

import android.Manifest;

public class Constants {

    //Permission related constants
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    public static final int REQUEST_CAMERA_PERMISSION = 1;

    //Image related constants
    public static final int BITMAP_WIDTH = 513;
    public static final int BITMAP_HEIGHT = 513;

    //Bundle related constants
    public static final String BUNDLE_KEY_IMAGE = "Image";

    //SNPE constants
    public static final String MNETSSD_OUTPUT_LAYER = "ArgMax:0";
    public static final String MNETSSD_INPUT_LAYER = "sub_7:0";
}
