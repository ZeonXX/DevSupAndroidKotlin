package com.sup.dev.android.tools

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.support.annotation.*
import android.util.TypedValue
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.java.tools.ToolsColor


object ToolsResources {

    fun getString(@StringRes r: Int, vararg args: Any): String? {
        return if (r < 0) null else String.format(getString(r)!!, *args)
    }

    fun getStringId(name: String): Int {
        return SupAndroid.appContext!!.resources.getIdentifier(name, "string", SupAndroid.appContext!!.packageName)
    }

    fun getString(name: String): String? {
        return getString(getStringId(name))
    }

    fun getString(@StringRes r: Int): String? {
        return if (r <= 0) null else SupAndroid.appContext!!.resources.getString(r)
    }

    fun getPlural(@PluralsRes r: Int, value: Int): String? {
        return if (r <= 0) null else SupAndroid.appContext!!.resources.getQuantityString(r, value)
    }

    fun getStringArray(@ArrayRes r: Int): Array<String>? {
        return if (r <= 0) null else SupAndroid.appContext!!.resources.getStringArray(r)
    }

    fun getDrawable(@DrawableRes r: Int): Drawable? {
        return if (r <= 0) null else SupAndroid.appContext!!.resources.getDrawable(r)
    }

    fun getDrawableFromAttr(@AttrRes r: Int): Drawable? {
        val attrs = intArrayOf(r)
        val ta = SupAndroid.activity!!.obtainStyledAttributes(attrs)
        val drawable = ta.getDrawable(0)
        ta.recycle()
        return drawable
    }

    fun getDrawableFromAttrId(@AttrRes r: Int): Int {
        val attrs = intArrayOf(r)
        val ta = SupAndroid.activity!!.obtainStyledAttributes(attrs)
        val id = ta.getResourceId(0, 0)
        ta.recycle()
        return id
    }

    fun getColorFromAttr(@AttrRes r: Int): Int {
        val attrs = intArrayOf(r)
        val ta = SupAndroid.activity!!.obtainStyledAttributes(attrs)
        val color = ta.getColor(0, 0x00000000)
        ta.recycle()
        return color
    }


    fun getColorId(name: String): Int {
        return SupAndroid.appContext!!.resources.getIdentifier(name, "color", SupAndroid.appContext!!.packageName)
    }

    fun getColor(name: String): Int {
        return getColor(getColorId(name))
    }


    fun getColor(@ColorRes r: Int): Int {
        return if (r <= 0) 0 else SupAndroid.appContext!!.resources.getColor(r)
    }

    fun getAccentColor(context: Context): Int {
        val value = TypedValue()
        context.theme.resolveAttribute(R.attr.colorAccent, value, true)
        return value.data
    }

    fun getPrimaryColor(context: Context): Int {
        val value = TypedValue()
        context.theme.resolveAttribute(R.attr.colorPrimary, value, true)
        return value.data
    }

    fun getPrimaryDarkColor(context: Context): Int {
        val value = TypedValue()
        context.theme.resolveAttribute(R.attr.colorPrimaryDark, value, true)
        return value.data
    }

    fun getAccentAlphaColor(context: Context): Int {
        return ToolsColor.setAlpha(106, getAccentColor(context))
    }

    fun getBackgroundColor(context: Context): Int {
        val value = TypedValue()
        context.theme.resolveAttribute(android.R.attr.windowBackground, value, true)
        return value.data
    }

    fun getBitmap(@DrawableRes res: Int): Bitmap? {
        val options = BitmapFactory.Options()
        options.inMutable = true
        val bitmap = BitmapFactory.decodeResource(SupAndroid.appContext!!.resources, res, options)

        return bitmap ?: ToolsBitmap.getFromDrawable(getDrawable(res))

    }


    fun getDrawable(name: String): Drawable? {
        return getDrawable(getDrawableId(name))
    }

    fun getDrawableId(name: String): Int {
        return SupAndroid.appContext!!.resources.getIdentifier(name, "drawable", SupAndroid.appContext!!.packageName)
    }

    fun getBitmap(name: String): Bitmap? {
        return getBitmap(getDrawableId(name))
    }

}
