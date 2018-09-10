package com.sup.dev.android.views.cards

import android.content.Context
import android.view.View
import com.sup.dev.android.views.widgets.Widget
import com.sup.dev.android.views.widgets.WidgetViewWrapper

class CardWidget(private val widget: Widget) : Card(), WidgetViewWrapper {

    override fun getLayout(): Int {
        return 0
    }

    override fun instanceView(context: Context): View? {
        return widget.view
    }

    override fun bindView(view: View) {
        widget.onShow()
    }

    override fun <K : WidgetViewWrapper> hideWidget(): K {
        adapter!!.remove(this)
        return this as K
    }

    override fun <K : WidgetViewWrapper> setWidgetCancelable(cancelable: Boolean): K {
        return this as K
    }

    override fun <K : WidgetViewWrapper> setWidgetEnabled(enabled: Boolean): K {
        return this as K
    }
}
