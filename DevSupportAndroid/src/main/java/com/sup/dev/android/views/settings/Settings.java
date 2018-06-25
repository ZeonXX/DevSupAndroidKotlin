package com.sup.dev.android.views.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;

import java.util.ArrayList;

public abstract class Settings extends FrameLayout {

    private final View line;
    protected final View view;

    private ArrayList<Settings> subSettings;
    private boolean subSettingsEnabled = true;


    public Settings(@NonNull Context context, @Nullable AttributeSet attrs, @LayoutRes int layoutRes) {
        super(context, attrs);

        SupAndroid.initEditMode(this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Settings, 0, 0);
        setFocusable(a.getBoolean(R.styleable.Settings_android_focusable, true));
        a.recycle();

        view = ToolsView.inflate(this, layoutRes);
        addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        line = new View(context);
        addView(line, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        line.setBackgroundColor(ToolsResources.getColor(R.color.grey_600));
        ((MarginLayoutParams) line.getLayoutParams()).setMargins(ToolsView.dpToPx(8), 0, ToolsView.dpToPx(8), 0);
        ((LayoutParams) line.getLayoutParams()).gravity = Gravity.BOTTOM;
    }

    //
    //  Setters
    //

    public void addSubSettings(Settings settings) {
        if (subSettings == null) subSettings = new ArrayList<>();
        subSettings.add(settings);
        settings.setEnabled(subSettingsEnabled && isEnabled());
    }

    public void setEnabledSubSettings(boolean enabled) {
        subSettingsEnabled = enabled;
        if (subSettings != null)
            for (Settings settings : subSettings)
                settings.setEnabled(subSettingsEnabled && isEnabled());
    }

    public void setLineVisible(boolean b) {
        line.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @CallSuper
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        view.setEnabled(enabled);
        setEnabledSubSettings(enabled);
        if (subSettings != null)
            for (Settings settings : subSettings)
                settings.setEnabled(subSettingsEnabled && isEnabled());
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        view.setOnClickListener(l);
        view.setFocusable(l != null);
    }

    //
    //  Getters
    //

    public boolean isSubSettingsEnabled() {
        return subSettingsEnabled;
    }

    public View getView() {
        return view;
    }
}
