package com.sup.dev.android.app;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.libs.eventbus_multi_process.EventBusMultiProcess;
import com.sup.dev.android.libs.screens.activity.SActivity;
import com.sup.dev.android.tools.ToolsAndroid;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.java.libs.debug.Debug;
import com.sup.dev.java.tools.ToolsThreads;

public class SupAndroid {

    public static int SERVICE_FOREGROUND = 4000;

    public static String TEXT_APP_NAME;
    public static String TEXT_APP_WHOOPS;
    public static String TEXT_APP_RETRY;
    public static String TEXT_APP_BACK;
    public static String TEXT_APP_DONT_SHOW_AGAIN;
    public static String TEXT_APP_ATTENTION;
    public static String TEXT_APP_DOWNLOADING;
    public static String TEXT_APP_DOWNLOADED;

    public static String TEXT_ERROR_PERMISSION_READ_FILES;
    public static String TEXT_ERROR_CANT_LOAD_IMAGE;
    public static String TEXT_ERROR_NETWORK;
    public static String TEXT_ERROR_GONE;

    public static int IMG_ERROR_NETWORK;
    public static int IMG_ERROR_GONE;


    public static boolean editMode;
    public static Context appContext;
    public static SActivity activity;
    public static boolean activityIsVisible;

    public static void initEditMode(View view) {
        if (!view.isInEditMode()) return;
        editMode = true;
        init(view.getContext());
    }

    public static void init(Context appContext) {
        SupAndroid.appContext = appContext;
        ToolsThreads.setOnMain((onNextTime, runnable) -> {
            if (!onNextTime && ToolsAndroid.isMainThread()) runnable.run();
            else new Handler(Looper.getMainLooper()).post(runnable);
        });

        Debug.printer = s -> Log.e("Debug", s);
        Debug.printerInfo = (tag,s) -> Log.i(tag, s);
        Debug.exceptionPrinter = th -> Log.e("Debug", "", th);

        EventBusMultiProcess.init();

        TEXT_APP_NAME = ToolsResources.getString("app_name");
        TEXT_APP_WHOOPS = ToolsResources.getString("app_whoops");
        TEXT_APP_RETRY = ToolsResources.getString("app_retry");
        TEXT_APP_BACK = ToolsResources.getString("app_back");
        TEXT_APP_DONT_SHOW_AGAIN = ToolsResources.getString("app_dont_show_again");
        TEXT_APP_ATTENTION = ToolsResources.getString("app_attention");
        TEXT_ERROR_PERMISSION_READ_FILES = ToolsResources.getString("error_permission_files");
        TEXT_ERROR_CANT_LOAD_IMAGE = ToolsResources.getString("error_cant_load_image");
        TEXT_ERROR_NETWORK = ToolsResources.getString("error_network");
        TEXT_ERROR_GONE = ToolsResources.getString("error_gone");
        TEXT_APP_DOWNLOADING = ToolsResources.getString("app_downloading");
        TEXT_APP_DOWNLOADED = ToolsResources.getString("app_downloaded");

        IMG_ERROR_NETWORK = ToolsResources.getDrawableId("error_network");
        IMG_ERROR_GONE = ToolsResources.getDrawableId("error_gone");


    }


}
