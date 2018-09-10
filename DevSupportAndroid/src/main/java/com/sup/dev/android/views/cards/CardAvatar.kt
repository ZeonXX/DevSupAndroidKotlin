package com.sup.dev.android.views.cards

import android.graphics.Bitmap
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.view.View
import com.sup.dev.android.R
import com.sup.dev.android.views.views.ViewAvatarTitle


class CardAvatar : Card() {

    private var onClick: () -> Unit = {}
    private var dividerVisible = false
    private var enabled = true
    private var background = 0
    private var title: String? = null
    private var subtitle: String? = null
    private var chipText: String? = null
    private var image: Bitmap? = null
    @DrawableRes
    private var chipIcon: Int = 0
    private var chipIconPaddingDp: Int = 0
    @ColorInt
    private var chipBackground: Int = 0
    private var chipUseIconBackground: Boolean = false
    private var onClickAvatar: (() -> Unit)? = null

    override val layout: Int
        get() = R.layout.card_avatar

    override fun bindView(view: View) {
        val vTouch = view.findViewById<View>(R.id.touch)
        val vDivider = view.findViewById<View>(R.id.divider)
        val vAvatar = view.findViewById<ViewAvatarTitle>(R.id.avatar)

        vDivider.visibility = if (dividerVisible) View.VISIBLE else View.GONE
        vTouch.isFocusable = true && enabled
        vTouch.isClickable = true && enabled
        vTouch.isEnabled = true && enabled
        if (enabled) vTouch.setOnClickListener { v -> onClick.invoke() }
        else vTouch.setOnClickListener(null)
        if (background != 0) view.setBackgroundColor(background)
        vAvatar.isEnabled = isEnabled()
        vAvatar.title = title
        vAvatar.setSubtitle(subtitle)
        vAvatar.isClickable = false
        if (onClickAvatar != null) vAvatar.viewAvatar.setOnClickListener { v -> onClickAvatar!!.invoke() }
        else vAvatar.viewAvatar.setOnClickListener(null)
        vAvatar.viewAvatar.setImage(image)
        vAvatar.viewAvatar.chip.text = chipText
        vAvatar.viewAvatar.chip.setIcon(chipIcon)
        vAvatar.viewAvatar.chip.setIconPadding(chipIconPaddingDp)
        vAvatar.viewAvatar.chip.setChipBackground(chipBackground)
        vAvatar.viewAvatar.chip.setUseIconBackground(chipUseIconBackground)

        onBind(vAvatar)
    }

    protected fun onBind(vAvatar: ViewAvatarTitle) {

    }

    //
    //  Setters
    //

    fun setOnCLickAvatar(onClickAvatar: () -> Unit): CardAvatar {
        this.onClickAvatar = onClickAvatar
        update()
        return this
    }

    fun setTitle(title: String): CardAvatar {
        this.title = title
        update()
        return this
    }

    fun setSubtitle(subtitle: String): CardAvatar {
        this.subtitle = subtitle
        update()
        return this
    }

    fun setImage(image: Bitmap): CardAvatar {
        this.image = image
        update()
        return this
    }

    fun setChipIcon(@DrawableRes icon: Int): CardAvatar {
        this.chipIcon = icon
        update()
        return this
    }

    fun setChipIconPadding(dp: Int): CardAvatar {
        chipIconPaddingDp = dp
        update()
        return this
    }

    fun setChipBackground(@ColorInt chipBackground: Int): CardAvatar {
        this.chipBackground = chipBackground
        update()
        return this
    }

    fun setChipText(avatarText: String): CardAvatar {
        this.chipText = avatarText
        update()
        return this
    }

    fun setEnabled(enabled: Boolean): CardAvatar {
        this.enabled = enabled
        update()
        return this
    }

    fun setOnClick(onClick: () -> Unit): CardAvatar {
        this.onClick = onClick
        update()
        return this
    }

    fun setDividerVisible(dividerVisible: Boolean): CardAvatar {
        this.dividerVisible = dividerVisible
        update()
        return this
    }

    fun setBackground(background: Int): CardAvatar {
        this.background = background
        update()
        return this
    }

    fun setChipUseIconBackground(chipUseIconBackground: Boolean): CardAvatar {
        this.chipUseIconBackground = chipUseIconBackground
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