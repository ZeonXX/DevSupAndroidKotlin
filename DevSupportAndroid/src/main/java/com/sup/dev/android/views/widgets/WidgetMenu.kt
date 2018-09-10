package com.sup.dev.android.views.widgets

import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.support.v7.widget.LinearLayoutManager
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.views.adapters.recycler_view.RecyclerCardAdapter
import com.sup.dev.android.views.cards.CardDivider
import com.sup.dev.android.views.cards.CardDividerTitle
import com.sup.dev.android.views.cards.CardMenu
import com.sup.dev.java.classes.providers.Provider


class WidgetMenu : WidgetRecycler() {

    private val adapter: RecyclerCardAdapter = RecyclerCardAdapter()
    private var onGlobalSelected: (WidgetMenu, String?) -> Unit = {w,s -> }

    private var prefCount = 0

    //
    //  Item
    //

    private var buildItem: Item? = null
    private var skipThisItem = false
    private var skipGroup = false

    init {
        vRecycler.layoutManager = LinearLayoutManager(view!!.context)
        setAdapter<WidgetRecycler>(adapter)
    }

    override fun onShow() {
        super.onShow()
        finishItemBuilding()
    }

    fun clear() {
        finishItemBuilding()
        adapter.clear()
        prefCount = 0
    }

    private fun add(item: Item) {

        item.card = CardMenu()
        item.card?.text = item.text
        item.card?.setIcon(item.icon)
        item.card?.setBackground(item.bg)
        item.card?.setOnClick { v, x, y ->
            hide()
            item.onClick.invoke(this)
            onGlobalSelected.invoke(this, item.text)
        }


        if (item.preferred) {
            if (prefCount == 0) adapter.add(0, CardDivider())
            adapter.add(prefCount, item.card)
            prefCount++
        } else {
            adapter.add(item.card)
        }

    }

    fun group(@StringRes title: Int): WidgetMenu {
        return group(ToolsResources.getString(title))
    }

    fun group(@StringRes title: Int, divider: Boolean): WidgetMenu {
        return group(ToolsResources.getString(title), divider)
    }

    @JvmOverloads
    fun group(title: String?, divider: Boolean = true): WidgetMenu {
        finishItemBuilding()
        adapter.add(CardDividerTitle().setText(title).setDivider(divider))
        return this
    }

    fun setOnGlobalSelected(onGlobalSelected: (WidgetMenu, String) -> Unit) {
        this.onGlobalSelected = onGlobalSelected
    }

    private fun finishItemBuilding() {
        if (buildItem != null) {
            val i = buildItem
            buildItem = null
            if (!skipThisItem && !skipGroup) add(i!!)
            skipThisItem = false
        }
    }

    fun add(@StringRes text: Int): WidgetMenu {
        return add(ToolsResources.getString(text))
    }

    fun add(@StringRes text: Int, onClick: (WidgetMenu) -> Unit = {}): WidgetMenu {
        return add(ToolsResources.getString(text), onClick)
    }

    @JvmOverloads
    fun add(text: String?, onClick: (WidgetMenu) -> Unit = {}): WidgetMenu {
        finishItemBuilding()
        buildItem = Item()
        buildItem!!.text = text
        buildItem!!.onClick = onClick
        return this
    }

    fun text(@StringRes text: Int): WidgetMenu {
        return text(ToolsResources.getString(text))
    }

    fun text(text: String?): WidgetMenu {
        buildItem!!.text = text
        return this
    }

    fun icon(icon: Int): WidgetMenu {
        buildItem!!.icon = icon
        return this
    }

    fun backgroundRes(@ColorRes color: Int): WidgetMenu {
        return background(ToolsResources.getColor(color))
    }

    fun backgroundRes(@ColorRes color: Int, condition: Provider<Boolean>): WidgetMenu {
        return if (condition.provide()!!)
            background(ToolsResources.getColor(color))
        else
            this
    }

    fun background(@ColorInt color: Int): WidgetMenu {
        buildItem!!.bg = color
        return this
    }

    fun preferred(b: Boolean): WidgetMenu {
        buildItem!!.preferred = b
        return this
    }

    fun onClick(onClick: (WidgetMenu) -> Unit): WidgetMenu {
        buildItem!!.onClick = onClick
        return this
    }

    fun condition(b: Boolean): WidgetMenu {
        skipThisItem = !b
        return this
    }

    fun groupCondition(b: Boolean): WidgetMenu {
        finishItemBuilding()
        skipGroup = !b
        return this
    }

    fun reverseGroupCondition(): WidgetMenu {
        finishItemBuilding()
        skipGroup = !skipGroup
        return this
    }

    fun clearGroupCondition(): WidgetMenu {
        finishItemBuilding()
        skipGroup = false
        return this
    }

    //
    //  Item
    //

    private inner class Item {

        var card: CardMenu? = null
        var onClick: (WidgetMenu) -> Unit = {}
        var text: String? = null
        var icon: Int = 0
        var bg: Int = 0
        var preferred: Boolean = false

    }


}