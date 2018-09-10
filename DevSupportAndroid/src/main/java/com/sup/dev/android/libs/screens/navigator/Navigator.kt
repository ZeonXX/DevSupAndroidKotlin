package com.sup.dev.android.libs.screens.navigator

import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.libs.screens.Screen
import com.sup.dev.java.classes.callbacks.CallbacksList2
import com.sup.dev.java.classes.providers.Provider
import java.util.ArrayList


object Navigator {

    var currentStack = NavigatorStack()
        private set

    val TO = NavigationAction.to().immutable()
    val SET = NavigationAction.set().immutable()
    val REPLACE = NavigationAction.replace().immutable()
    val REORDER = NavigationAction.reorder().immutable()

    //
    //  Listeners
    //

    private val onBack = CallbacksList2<Screen, Screen>()
    val onBackCallbacks = ArrayList<Provider<Boolean>>()

    enum class Animation {
        IN, OUT, ALPHA, NONE
    }

    //
    //  Views
    //

    fun removeScreen(screen: Screen) {
        screen.onDestroy()
        currentStack.backStack.remove(screen)
    }

    //
    //  Navigation
    //

    fun action(action: NavigationAction, screen: Screen) {
        action.doAction(screen)
    }

    @JvmOverloads
    fun to(screen: Screen, animation: Animation = Animation.IN) {
        if (!currentStack.backStack.isEmpty()) {
            if (!getCurrent()!!.isBackStackAllowed) {
                removeScreen(getCurrent()!!)
            } else {
                getCurrent()!!.onPause()
            }
            if (screen.isSingleInstanceInBackstack) {
                removeAll(screen.javaClass)
            }
        }
        currentStack.backStack.add(screen)
        setCurrentView(animation)
    }

    fun replace(screen: Screen, newScreen: Screen) {
        if (currentStack.backStack.isEmpty()) return
        if (getCurrent() == screen) {
            replace(newScreen)
            return
        }
        for (i in currentStack.backStack.indices) if (currentStack.backStack[i] == screen) currentStack.backStack[i] = newScreen
    }

    fun replace(screen: Screen) {
        if (!currentStack.backStack.isEmpty()) removeScreen(getCurrent()!!)
        to(screen, Animation.ALPHA)
    }

    @JvmOverloads
    fun set(screen: Screen, animation: Animation = Animation.ALPHA) {
        while (currentStack.backStack.size != 0) removeScreen(currentStack.backStack[0])
        to(screen, animation)
    }

    fun reorder(screen: Screen) {
        currentStack.backStack.remove(screen)
        to(screen)
    }

    fun reorderOrCreate(viewClass: Class<out Screen>, provider: Provider<Screen>) {

        if (getCurrent() != null && getCurrent()!!.javaClass == viewClass)
            return

        for (i in currentStack.backStack.size - 1 downTo -1 + 1)
            if (currentStack.backStack[i].javaClass == viewClass) {
                reorder(currentStack.backStack[i])
                return
            }

        to(provider.provide())
    }

    fun removeAllEqualsAndTo(view: Screen) {

        var i = 0
        while (i < currentStack.backStack.size) {
            if (currentStack.backStack[i].equalsNView(view))
                remove(currentStack.backStack[i--])
            i++
        }

        to(view)
    }

    fun removeAll(viewClass: Class<out Screen>) {
        val current = getCurrent()
        val needUpdate = current != null && current.javaClass == viewClass

        var i = 0
        while (i < currentStack.backStack.size) {
            if (currentStack.backStack[i].javaClass == viewClass) {
                remove(currentStack.backStack[i--])
            }
            i++
        }

        if (needUpdate) setCurrentView(Animation.OUT)
    }


    fun back(): Boolean {
        if (!hasBackStack()) return false

        val current = getCurrent()
        removeScreen(current!!)
        setCurrentView(Animation.OUT)

        onBack.callback(current, current)

        return true
    }

    fun remove(view: Screen) {
        if (hasBackStack() && getCurrent() == view)
            back()
        else
            removeScreen(view)
    }

    fun setStack(stack: NavigatorStack) {
        if (currentStack == stack) return
        currentStack = stack
        if (!currentStack.backStack.isEmpty()) setCurrentView(Animation.ALPHA)
    }

    //
    //  Activity Callbacks
    //

    fun resetCurrentView() {
        setCurrentView(Animation.NONE)
    }

    private fun setCurrentView(animation: Animation) {
        if (getCurrent() == null) return
        SupAndroid.activity!!.setScreen(getCurrent(), animation)
        if (getCurrent() != null) getCurrent()!!.onResume()
    }

    fun onActivityStop() {
        if (getCurrent() != null) getCurrent()!!.onPause()
    }

    fun onActivityConfigChanged() {
        if (getCurrent() != null) getCurrent()!!.onConfigChanged()
    }

    fun onBackPressed(): Boolean {

        for (i in onBackCallbacks.size - 1 downTo -1 + 1) {
            val onBack = onBackCallbacks.removeAt(i)
            if (onBack.provide()!!) return true
        }

        return getCurrent() != null && getCurrent()!!.onBackPressed() || back()
    }

    //
    //  Getters
    //


    fun getStackSize(): Int {
        return currentStack.backStack.size
    }

    fun getPrevious(): Screen? {
        return if (hasPrevious()) currentStack.backStack[currentStack.backStack.size - 2] else null
    }

    fun getCurrent(): Screen? {
        return if (currentStack.backStack.isEmpty()) null else currentStack.backStack[currentStack.backStack.size - 1]
    }

    fun isEmpty(): Boolean {
        return currentStack.backStack.isEmpty()
    }


    fun hasBackStack(): Boolean {
        return currentStack.backStack.size > 1
    }

    fun hasPrevious(): Boolean {
        return currentStack.backStack.size > 1
    }

    fun addOnBackScreenListener(onBack: (Screen?, Screen?) -> Unit) {
        Navigator.onBack.remove(onBack)
        Navigator.onBack.add(onBack)
    }

    fun removeOnBackScreenListener(onBack: (Screen?, Screen?) -> Unit) {
        Navigator.onBack.remove(onBack)
    }


    fun addOnBack(onBack: Provider<Boolean>) {
        if (onBackCallbacks.contains(onBack)) onBackCallbacks.remove(onBack)
        onBackCallbacks.add(onBack)
    }

    fun removeOnBack(onBack: Provider<Boolean>) {
        onBackCallbacks.remove(onBack)
    }

}