package com.sup.dev.android.utils.implementations;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URLConnection;
import java.util.ArrayList;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsIntent;
import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.callbacks.simple.Callback2;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.items.Item2;
import com.sup.dev.java.tools.ToolsText;
import com.sup.dev.java.libs.debug.Debug;

public class UtilsIntentImpl implements UtilsIntent {

    private static final String SHARE_FOLDER = "sup_share_cash";

    private static int codeCounter = 0;
    private final ArrayList<Item2<Integer, Callback2<Integer, Intent>>> progressIntents = new ArrayList<>();

    public UtilsIntentImpl(){
        new File(getCashRoot()).delete();
    }

    public void startIntentForResult(Intent intent, Callback2<Integer, Intent> onResult) {
        if (codeCounter == 65000)
            codeCounter = 0;
        int code = codeCounter++;
        progressIntents.add(new Item2<>(code, onResult));
        SupAndroid.di.mvpActivity(mvpActivity -> ((Activity) mvpActivity).startActivityForResult(intent, code));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        for (Item2<Integer, Callback2<Integer, Intent>> pair : progressIntents)
            if (requestCode == pair.a1) {
                progressIntents.remove(pair);
                pair.a2.callback(resultCode, resultIntent);
                return;
            }

    }

    public void openApp(int stringID) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(SupAndroid.di.utilsResources().getString(stringID)));
        SupAndroid.di.appContext().startActivity(intent);
    }

    //
    //  Intents
    //

    public void startIntent(Intent intent, Callback onActivityNotFound) {
        try {
            SupAndroid.di.appContext().startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Debug.log(ex);
            if (onActivityNotFound != null)
                onActivityNotFound.callback();
        }
    }


    public void startWeb(String link, Callback onActivityNotFound) {
        startIntent(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(ToolsText.castToWebLink(link)))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), onActivityNotFound);
    }

    public void startApp(String packageName, Callback onActivityNotFound) {
        startApp(packageName, null, onActivityNotFound);
    }

    public void startApp(String packageName, Callback1<Intent> onIntentCreated, Callback onActivityNotFound) {
        PackageManager manager = SupAndroid.di.appContext().getPackageManager();
        Intent intent = manager.getLaunchIntentForPackage(packageName);
        if (intent == null) {
            if (onActivityNotFound != null) onActivityNotFound.callback();
            return;
        }
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        if (onIntentCreated != null) onIntentCreated.callback(intent);
        startIntent(intent, onActivityNotFound);
    }

    public void startPlayMarket(String packageName, Callback onActivityNotFound) {
        startIntent(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName + "&reviewId=0"))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), onActivityNotFound);
    }

    public void startMail(String link, Callback onActivityNotFound) {
        startIntent(new Intent(android.content.Intent.ACTION_SENDTO, Uri.parse("mailto:" + link))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), onActivityNotFound);
    }

    public void startPhone(String phone, Callback onActivityNotFound) {
        startIntent(new Intent(android.content.Intent.ACTION_DIAL, Uri.parse("tel:" + phone))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), onActivityNotFound);
    }

    public void shareImage(Bitmap bitmap, String text, String providerKey, Callback onActivityNotFound) {
        new File(getCashRoot()).mkdirs();

        String patch = getCashRoot() + System.currentTimeMillis() + ".png";

        Debug.log(new File(getCashRoot()).exists());
        Debug.log(patch);

        OutputStream out;
        File file = new File(patch);
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            Debug.log(e);
        }
        patch = file.getPath();

        Uri fileUti = FileProvider.getUriForFile(SupAndroid.di.appContext(), providerKey, new File(patch));
        shareFile(fileUti, "image/*", text, onActivityNotFound);
    }

    public void shareFile(String patch, String providerKey, Callback onActivityNotFound) {
        Uri fileUti = FileProvider.getUriForFile(SupAndroid.di.appContext(), providerKey, new File(patch));
        shareFile(fileUti, onActivityNotFound);
    }

    public void shareFile(String patch, String providerKey, String type, Callback onActivityNotFound) {
        Uri fileUti = FileProvider.getUriForFile(SupAndroid.di.appContext(), providerKey, new File(patch));
        shareFile(fileUti, type, onActivityNotFound);
    }

    public void shareFile(Uri uri, Callback onActivityNotFound) {
        shareFile(uri, URLConnection.guessContentTypeFromName(uri.toString()), onActivityNotFound);
    }

    public void shareFile(Uri uri, String type, Callback onActivityNotFound) {
        shareFile(uri, type, null, onActivityNotFound);
    }

    public void shareFile(Uri uri, String type, String text, Callback onActivityNotFound) {
        SupAndroid.di.mvpActivity(activity -> {
            try {
                Intent i = new Intent()
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .setAction(Intent.ACTION_SEND)
                        .putExtra(Intent.EXTRA_STREAM, uri)
                        .setType(type);
                if(text == null)i.putExtra(Intent.EXTRA_TEXT, text);
                ((Activity)activity).startActivity(Intent.createChooser(i, null));
            } catch (ActivityNotFoundException ex) {
                Debug.log(ex);
                if (onActivityNotFound != null) onActivityNotFound.callback();
            }
        });
    }

    public void shareText(String title, String text, Callback onActivityNotFound) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND)
                .setType("text/plain")
                .putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here")
                .putExtra(android.content.Intent.EXTRA_TEXT, text);
        startIntent(Intent.createChooser(sharingIntent, null), onActivityNotFound);
    }

    //
    //  Intents result
    //

    public void getGalleryImage(Callback1<Uri> onResult, Callback onError) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        try {
            startIntentForResult(Intent.createChooser(intent, null), (resultCode, resultIntent) -> {
                if (resultCode == Activity.RESULT_OK)
                    onResult.callback(resultIntent.getData());
                else if (onError != null) onError.callback();
            });
        } catch (ActivityNotFoundException ex) {
            Debug.log(ex);
            if (onError != null) onError.callback();
        }
    }

    //
    //  Services / Activities
    //

    public void startService(Class<? extends Service> serviceClass, Object... extras) {
        android.content.Intent intent = new android.content.Intent(SupAndroid.di.appContext(), serviceClass);

        addExtras(intent, extras);

        SupAndroid.di.appContext().startService(intent);
    }

    public void startActivity(Context viewContext, Class<? extends Activity> activityClass, Object... extras) {
        startActivity(viewContext, activityClass, null, extras);
    }

    public void startActivity(Context viewContext, Class<? extends Activity> activityClass, Integer flags, Object... extras) {
        android.content.Intent intent = new android.content.Intent(viewContext, activityClass);

        addExtras(intent, extras);

        if (flags != null)
            intent.addFlags(flags);

        viewContext.startActivity(intent);
    }

    public void addExtras(android.content.Intent intent, Object... extras) {
        for (int i = 0; i < extras.length; i += 2) {
            Object extra = extras[i + 1];
            if (extra instanceof Parcelable)
                intent.putExtra((String) extras[i], (Parcelable) extra);
            else if (extra instanceof Serializable)
                intent.putExtra((String) extras[i], (Serializable) extra);
            else
                throw new IllegalArgumentException("Extras must be instance of Parcelable or Serializable");
        }
    }

    public void sendSalient(PendingIntent intent) {
        try {
            intent.send();
        } catch (PendingIntent.CanceledException ex) {
            Debug.log(ex);
        }
    }

    //
    //  Support
    //

    private String getCashRoot(){
        return SupAndroid.di.appContext().getExternalCacheDir().getAbsolutePath() + SHARE_FOLDER;
    }


}
