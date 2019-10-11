package com.sup.dev.android.views.dialogs

import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatDialog
import com.sup.dev.android.R
import com.sup.dev.android.libs.screens.navigator.Navigator
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.views.layouts.LayoutCorned
import com.sup.dev.java.libs.debug.err

@Suppress("UNCHECKED_CAST")
open class DialogSheet(protected val view: View) : AppCompatDialog(view.context) {

    //
    //  Getters
    //

    var isEnabled: Boolean = false
        private set
    private var cancelable = true

    constructor(layoutRes: Int) : this(ToolsView.inflate<View>(layoutRes)) {}

    init {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setOnCancelListener { onHide() }

        val vRoot: ViewGroup = ToolsView.inflate(view.context, R.layout.dialog_sheet)
        val vContainer: LayoutCorned = vRoot.findViewById(R.id.vContainer)

        vContainer.setCornedSize(8)
        vContainer.setCornedBL(false)
        vContainer.setCornedBR(false)

        vContainer.isClickable = true // Чтоб не закрывался при нажатии на тело
        vContainer.addView(ToolsView.removeFromParent(view))
        setContentView(vRoot)

        window!!.setWindowAnimations(R.style.DialogSheetAnimation)
        window!!.setBackgroundDrawable(null)
        window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        if(Navigator.getCurrent() != null) window!!.navigationBarColor = Navigator.getCurrent()!!.navigationBarColor


        //  vRoot.y = (-ToolsAndroid.getBottomNavigationBarHeight()).toFloat()

        vRoot.setOnClickListener { if (cancelable && isEnabled && onTryCancelOnTouchOutside()) hide() }

        Navigator.addOnScreenChanged {
            hide()
            true
        }
    }

    override fun onBackPressed() {
        if (onTryCancelOnTouchOutside()) super.onBackPressed()
    }

    open fun onTryCancelOnTouchOutside(): Boolean {
        return true
    }

    @CallSuper
    protected open fun onShow() {

    }

    @CallSuper
    protected open fun onHide() {

    }

    override fun hide() {
        try {
            super.dismiss()
        } catch (e: IllegalArgumentException) {
            err(e)
        }
        onHide()
    }

    override fun show() {
        showDialog<DialogSheet>()
    }

    fun <K : DialogSheet> showDialog(): K {
        onShow()
        super.show()
        return this as K
    }

    //
    //  Setters
    //


    override fun setCancelable(cancelable: Boolean) {
        this.cancelable = cancelable
        setCanceledOnTouchOutside(cancelable && isEnabled)
        super.setCancelable(cancelable && isEnabled)
    }

    fun setDialogCancelable(cancelable: Boolean): DialogSheet {
        setCancelable(cancelable)
        return this
    }

    fun setEnabled(enabled: Boolean): DialogSheet {
        this.isEnabled = enabled
        setCanceledOnTouchOutside(cancelable && isEnabled)
        super.setCancelable(cancelable && isEnabled)
        return this
    }

    fun isCancelable(): Boolean {
        return cancelable
    }
}