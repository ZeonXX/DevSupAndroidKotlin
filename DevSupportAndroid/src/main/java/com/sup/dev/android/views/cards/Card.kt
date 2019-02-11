package com.sup.dev.android.views.cards

import android.content.Context
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.view.View
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.support.adapters.CardAdapter


abstract class Card {

    var adapter: CardAdapter? = null
    private var view: View? = null

    var tag: Any? = null

    //
    //  Bind
    //

    @LayoutRes
    abstract fun getLayout(): Int

    fun update() {

        if(view == null) {
            if (adapter == null) return
            view = adapter!!.getView(this)
        }

        if (view != null) bindView(view!!)

    }

    @CallSuper
    open fun bindView(view: View) {
        this.view = view
        view.tag = this
    }

    protected open fun instanceView(): View {
        return View(SupAndroid.appContext)
    }

    protected fun getView():View?{
        if(view == null || view!!.tag != this){
            view = null
            return null
        }else{
            return view
        }
    }

    //
    //  Adapter
    //

    open fun instanceView(context: Context): View {
        val layout = getLayout()
        return if (layout > 0) ToolsView.inflate(context, getLayout()) else instanceView()
    }

    open fun setCardAdapter(adapter: CardAdapter?) {
        this.adapter = adapter
    }


}
