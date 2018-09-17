package com.sup.dev.android.views.widgets

import android.support.annotation.StringRes
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import java.util.ArrayList


class WidgetRadioButtons : Widget(R.layout.widget_container) {

    private val items = ArrayList<Item?>()
    private val vOptionsContainer: LinearLayout = view.findViewById(R.id.content_container)
    private val vCancel: Button = view.findViewById(R.id.cancel)
    private val vEnter: Button = view.findViewById(R.id.enter)

    private var autoHideOnEnter = true


    //
    //  Item
    //

    private var buildItem: Item? = null
    private var skipThisItem = false
    private var skipGroup = false

    init {

        vCancel.visibility = View.GONE
        vEnter.visibility = View.GONE
    }

    override fun onShow() {
        super.onShow()
        finishItemBuilding()
    }

    private fun add(item: Item?) {
        items.add(item)
    }

    private fun finishItemBuilding() {
        if (buildItem != null) {
            val i = buildItem
            buildItem = null
            if (!skipThisItem && !skipGroup) add(i)
        }
    }

    fun add(@StringRes text: Int): WidgetRadioButtons {
        return add(ToolsResources.getString(text))
    }

    fun add(text: String?): WidgetRadioButtons {
        finishItemBuilding()
        buildItem = Item()
        buildItem!!.v.text = text
        return this
    }

    fun onChange(onChange: ((WidgetRadioButtons, Boolean) -> Unit)?): WidgetRadioButtons {
        buildItem!!.onChange = onChange
        return this
    }

    fun onSelected(onSelected: (WidgetRadioButtons) -> Unit): WidgetRadioButtons {
        buildItem!!.onSelected = onSelected
        return this
    }

    fun onNotSelected(onNotSelected: (WidgetRadioButtons) -> Unit): WidgetRadioButtons {
        buildItem!!.onNotSelected = onNotSelected
        return this
    }

    fun text(@StringRes text: Int): WidgetRadioButtons {
        return text(ToolsResources.getString(text))
    }

    fun text(text: String?): WidgetRadioButtons {

        buildItem!!.v.text = text
        return this
    }

    fun checked(b: Boolean): WidgetRadioButtons {
        buildItem!!.v.isChecked = b
        return this
    }

    fun condition(b: Boolean): WidgetRadioButtons {
        skipThisItem = !b
        return this
    }

    fun groupCondition(b: Boolean): WidgetRadioButtons {
        skipGroup = !b
        return this
    }

    fun reverseGroupCondition(): WidgetRadioButtons {
        skipGroup = !skipGroup
        return this
    }

    fun clearGroupCondition(): WidgetRadioButtons {
        skipGroup = false
        return this
    }

    //
    //  Setters
    //

    fun setOnEnter(@StringRes s: Int): WidgetRadioButtons {
        return setOnEnter(ToolsResources.getString(s))
    }

    fun setOnEnter(s: String?): WidgetRadioButtons {
        ToolsView.setTextOrGone(vEnter, s)
        vEnter.setOnClickListener { vi ->
            if (autoHideOnEnter)
                hide()
            else
                setEnabled<Widget>(false)
            for (i in 0 until vOptionsContainer.childCount) {
                val v = vOptionsContainer.getChildAt(i) as RadioButton
                val item = v.tag as Item
                if (item.onChange != null) item.onChange!!.invoke(this, v.isChecked)
                if (v.isChecked && item.onSelected != null) item.onSelected!!.invoke(this)
                if (!v.isChecked && item.onNotSelected != null) item.onNotSelected!!.invoke(this)
            }
        }
        return this
    }

    fun setAutoHideOnEnter(autoHideOnEnter: Boolean): WidgetRadioButtons {
        this.autoHideOnEnter = autoHideOnEnter
        return this
    }

    fun setOnCancel(@StringRes s: Int): WidgetRadioButtons {
        return setOnCancel(ToolsResources.getString(s))
    }

    fun setOnCancel(onCancel: (WidgetRadioButtons) -> Unit): WidgetRadioButtons {
        return setOnCancel(null, onCancel)
    }

    fun setOnCancel(@StringRes s: Int, onCancel:  (WidgetRadioButtons)->Unit = {}): WidgetRadioButtons {
        return setOnCancel(ToolsResources.getString(s), onCancel)
    }

    @JvmOverloads
    fun setOnCancel(s: String?, onCancel: (WidgetRadioButtons)->Unit = {}): WidgetRadioButtons {
        super.setOnHide<Widget>{ b -> onCancel?.invoke(this) }
        ToolsView.setTextOrGone(vCancel, s)
        vCancel.setOnClickListener { v ->
            hide()
            onCancel.invoke(this)
        }

        return this
    }

    //
    //  Item
    //

    private inner class Item {

        var v: RadioButton = RadioButton(SupAndroid.activity!!)

        var onChange: ((WidgetRadioButtons, Boolean) -> Unit)? = null
        var onSelected: ((WidgetRadioButtons)->Unit)? = null
        var onNotSelected: ((WidgetRadioButtons) -> Unit)? = null

        init {
            v.tag = this
            v.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked)
                    for (i in 0 until vOptionsContainer.childCount)
                        if (vOptionsContainer.getChildAt(i) !== v)
                            (vOptionsContainer.getChildAt(i) as RadioButton).isChecked = false
            }
            vOptionsContainer.addView(v)
            if (vOptionsContainer.childCount > 1)
                (v.layoutParams as ViewGroup.MarginLayoutParams).topMargin = ToolsView.dpToPx(16f)

        }

    }


}