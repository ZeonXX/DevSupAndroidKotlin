package com.sup.dev.android.views.screens;

import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.screens.SNavigator;
import com.sup.dev.android.libs.screens.Screen;
import com.sup.dev.android.views.views.ViewChip;
import com.sup.dev.java.classes.callbacks.simple.Callback;

public class SAlert extends Screen {

    public static void showNetwork(SNavigator.Action action, Callback onRetry) {
        SNavigator.action(action, new SAlert(
                SupAndroid.TEXT_APP_WHOOPS,
                SupAndroid.TEXT_ERROR_NETWORK,
                SupAndroid.TEXT_APP_RETRY,
                onRetry));
    }

    public static void showGone(SNavigator.Action action) {
        SNavigator.action(action, new SAlert(
                SupAndroid.TEXT_APP_WHOOPS,
                SupAndroid.TEXT_ERROR_GONE,
                SupAndroid.TEXT_APP_BACK,
                () -> SNavigator.back()));
    }

    private SAlert(String title, String text, String action, Callback onAction) {
        super(R.layout.screen_alert);

        TextView vTitle = findViewById(R.id.title);
        TextView vText = findViewById(R.id.text);
        ViewChip vAction = findViewById(R.id.action);

        vTitle.setText(title);
        vText.setText(text);
        vAction.setText(action);

        vAction.setOnClickListener(v -> {
            if (onAction != null) onAction.callback();
        });

    }

}