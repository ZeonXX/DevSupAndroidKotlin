package com.sup.dev.android.views.adapters.recycler_view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.adapters.CardAdapter
import com.sup.dev.android.views.adapters.NotifyItem
import com.sup.dev.android.views.cards.Card
import com.sup.dev.java.classes.callbacks.CallbacksList
import com.sup.dev.java.classes.collections.HashList
import com.sup.dev.java.tools.ToolsClass
import java.util.ArrayList


open class RecyclerCardAdapter : RecyclerView.Adapter<RecyclerCardAdapter.Holder>(), CardAdapter {

    private val viewCash = HashList<Class<out Card>, View>()
    private val items = ArrayList<Card>()
    protected val holders = ArrayList<Holder>()
    private val onItemsChangeListeners = CallbacksList()

    private var notifyCount = 0

    val isScrolledToLastItem: Boolean
        get() = if (isEmpty) true else isVisible(get(size() - 1))

    val isEmpty: Boolean
        get() = itemCount == 0

    //
    //  Bind
    //

    override fun onBindViewHolder(holder: Holder, position: Int) {
        var i = position
        while (i < position + notifyCount && i < items.size) {
            if (items[i] is NotifyItem)
                (items[i] as NotifyItem).notifyItem()
            i++
        }
        removeItemFromHolders(items[position])
        holder.item = items[position]

        val card = items[position]
        val frame = holder.itemView as FrameLayout

        val tag = frame.tag as Class<out Card>

        if (frame.childCount != 0)
            viewCash.add(tag, frame.getChildAt(0))
        frame.removeAllViews()

        var cardView = viewCash.removeOne(card::class.java)
        if (cardView == null)
            cardView = card.instanceView(frame.context)


        frame.addView(ToolsView.removeFromParent(cardView!!))
        frame.tag = card::class.java

        card.bindView(cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = FrameLayout(parent.context)
        view.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
        val holder = Holder(view)
        holders.add(holder)
        return holder
    }

    private fun removeItemFromHolders(item: Card) {
        for (h in holders)
            if (h.item === item)
                h.item = null
    }

    //
    //  Items
    //

    fun add(list: List<Card>) {
        for (card in list)
            add(card)
    }

    fun add(card: Card) {
        add(items.size, card)
    }

    override fun add(p: Int, card: Card) {
        card.setCardAdapter(this)
        items.add(p, card)
        notifyItemInserted(p)
        onItemsChangeListeners.callback()
    }

    override fun remove(card: Card) {
        card.setCardAdapter(null)
        val position = indexOf(card)
        if (position == -1) return
        remove(position)
    }

    fun remove(c: Class<out Card>) {
        var i = 0
        while (i < itemCount) {
            if (ToolsClass.instanceOf(get(i)::class.java, c))
                remove(i--)
            i++
        }
    }

    fun remove(list: List<Card>) {
        for (card in list) remove(card)
    }

    open fun remove(position: Int) {
        val card = items.removeAt(position)
        notifyItemRemoved(position)
        removeItemFromHolders(card)
        onItemsChangeListeners.callback()
    }

    fun replace(index: Int, o: Card) {
        removeItemFromHolders(items[index])
        items[index] = o
        notifyItemChanged(index)
    }

    fun clear() {
        val count = items.size
        items.clear()
        notifyItemRangeRemoved(0, count)
        for (h in holders)
            h.item = null
    }


    //
    //  Listeners
    //

    fun addItemsChangeListener(callback: () -> Unit) {
        onItemsChangeListeners.add(callback)
    }

    fun removeItemsChangeListener(callback: () -> Unit) {
        onItemsChangeListeners.remove(callback)
    }

    //
    //  Notify
    //

    fun notifyAllChanged() {
        notifyItemRangeChanged(0, itemCount)
    }

    //
    //  Setters
    //

    open fun setNotifyCount(notifyCount: Int): RecyclerCardAdapter {
        this.notifyCount = notifyCount
        return this
    }

    //
    //  Getters
    //


    override fun isVisible(card: Card): Boolean {
        return getView(card) != null
    }

    fun isScrolledToLastItems(count: Int): Boolean {
        if (size() < count) return true
        for (i in 0 until count)
            if (isVisible(get(size() - count)))
                return true
        return false
    }

    operator fun contains(c: Class<out Card>): Boolean {
        for (i in 0 until itemCount)
            if (ToolsClass.instanceOf(get(i)::class.java, c))
                return true
        return false
    }

    fun size(c: Class<out Card>): Int {
        var x = 0
        for (i in 0 until itemCount)
            if (ToolsClass.instanceOf(get(i)::class.java, c))
                x++
        return x
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun size(): Int {
        return itemCount
    }


    override operator fun get(index: Int): Card {
        return items[index]
    }

    override fun indexOf(o: Card): Int {
        return items.indexOf(o)
    }

    override operator fun contains(o: Card): Boolean {
        return items.contains(o)
    }

    fun getByTag(tag: Any?): ArrayList<Card> {
        val list = ArrayList<Card>()
        for (i in 0 until itemCount)
            if (get(i).tag == null && tag == null || tag != null && get(i).tag != null && get(i).tag!!.equals(tag))
                list.add(get(i))
        return list
    }

    operator fun <K : Card> get(c: Class<K>): ArrayList<K> {
        val list = ArrayList<K>()
        for (i in 0 until itemCount)
            if (ToolsClass.instanceOf(get(i)::class.java, c))
                list.add(get(i) as K)
        return list
    }

    override fun getView(item: Card): View? {
        var view: View? = null

        for (h in holders)
            if (h.item === item)
                view = h.itemView

        if (view != null) {
            val frame = view as FrameLayout?
            if (frame!!.childCount == 1)
                return frame.getChildAt(0)
        }

        return null
    }

    //
    //  Holder
    //

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var item: Card? = null
    }

}