package com.sup.dev.android.views.cards

import android.support.annotation.StringRes
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsResources


class CardMessage : Card() {

    private var dividerVisible = false
    private var enabled = true
    private var background = 0

    private var text: String? = null
    private var customColor: Boolean = false
    private var textColor: Int = 0
    private var actionText: String? = null
    private var onActionClicked: (()->Unit)? = null

    override fun getLayout(): Int {
        return R.layout.card_message
    }

    override fun bindView(view: View) {
        val vDivider = view.findViewById<View>(R.id.divider)
        val vText = view.findViewById<TextView>(R.id.text)
        val vAction = view.findViewById<Button>(R.id.action)

        vDivider.visibility = if (dividerVisible) View.VISIBLE else View.GONE
        if (background != 0) view.setBackgroundColor(background)

        vText.visibility = if (text == null) View.GONE else View.VISIBLE
        vText.text = text
        vText.isEnabled = isEnabled()

        vAction.isEnabled = isEnabled()
        vAction.visibility = if (actionText == null) View.GONE else View.VISIBLE
        vAction.text = actionText
        vAction.setOnClickListener { v -> if (onActionClicked != null) onActionClicked!!.invoke() }

        if (customColor)
            vText.setTextColor(textColor)
    }

    //
    //  Setters
    //


    fun setDividerVisible(dividerVisible: Boolean): CardMessage {
        this.dividerVisible = dividerVisible
        update()
        return this
    }

    fun setEnabled(enabled: Boolean): CardMessage {
        this.enabled = enabled
        update()
        return this
    }

    fun setBackground(background: Int): CardMessage {
        this.background = background
        update()
        return this
    }

    fun setText(@StringRes text: Int): CardMessage {
        return setText(ToolsResources.getString(text))
    }

    fun setText(text: String?): CardMessage {
        this.text = text
        update()
        return this
    }

    fun setAction(@StringRes text: Int, onActionClicked: ()->Unit): CardMessage {
        return setAction(ToolsResources.getString(text), onActionClicked)
    }

    fun setAction(actionText: String?, onActionClicked: ()->Unit): CardMessage {
        this.actionText = actionText
        this.onActionClicked = onActionClicked
        return this
    }

    fun setTextColor(color: Int): CardMessage {
        customColor = true
        textColor = color
        update()
        return this
    }

    //
    //  Getters
    //

    fun isEnabled(): Boolean {
        return enabled
    }
}