package com.sup.dev.android.libs.image_loader

import android.graphics.BitmapFactory
import android.widget.ImageView


abstract class ImageLoaderA {

    private var key: String = "key"

    internal var vImage: ImageView? = null
    internal var onLoaded: (ByteArray?) -> Unit = {}
    internal var onSetHolder: () -> Unit = {}
    internal var cropSquareCenter: Boolean = false
    internal var options: BitmapFactory.Options? = null
    internal var w: Int = 0
    internal var h: Int = 0
    internal var holder: Int = 0
    internal var fade = true
    internal var cashScaledBytes: Boolean = false
    internal var noCash: Boolean = false
    internal var noLoadFromCash: Boolean = false
    internal var keyPrefix: String? = ""

    fun setImage(vImage: ImageView?): ImageLoaderA {
        this.vImage = vImage
        if (vImage != null) vImage.tag = key
        return this
    }

    fun keyPrefix(keyPrefix: String): ImageLoaderA {
        if (this.keyPrefix != null) key = key!!.substring(keyPrefix.length)
        this.keyPrefix = keyPrefix
        setKey(key!!)
        return this
    }

    fun sizes(w: Int, h: Int): ImageLoaderA {
        this.w = w
        this.h = h
        return this
    }

    protected fun setKey(key: Any): ImageLoaderA {
        this.key = keyPrefix!! + key.toString()
        return this
    }

    fun onLoaded(onLoaded: (ByteArray?) -> Unit): ImageLoaderA {
        this.onLoaded = onLoaded
        return this
    }

    fun setOnSetHolder(onSetHolder: () -> Unit): ImageLoaderA {
        this.onSetHolder = onSetHolder
        return this
    }

    fun cropSquare(): ImageLoaderA {
        this.cropSquareCenter = true
        return this
    }

    fun options(options: BitmapFactory.Options): ImageLoaderA {
        this.options = options
        return this
    }

    fun cashScaledBytes(): ImageLoaderA {
        this.cashScaledBytes = true
        return this
    }

    fun holder(holder: Int): ImageLoaderA {
        this.holder = holder
        return this
    }

    fun noFade(): ImageLoaderA {
        this.fade = false
        return this
    }

    fun noCash(): ImageLoaderA {
        this.noCash = false
        return this
    }

    fun noLoadFromCash(): ImageLoaderA {
        this.noLoadFromCash = true
        return this
    }

    fun isKey(key: Any?): Boolean {
        return key === this.key || key != null && key == this.key
    }

    abstract fun load(): ByteArray?

    fun getKey(): Any {
        return key
    }

    fun clearCash() {
        ImageLoader.bitmapCash.remove(key)
    }

    fun replace(bytes: ByteArray) {
        ImageLoader.bitmapCash.replace(key, bytes)
    }


}