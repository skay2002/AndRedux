package com.everobo.dooba.core.store

import com.squareup.otto.Bus
import com.everobo.dooba.core.aciton.Action


abstract class Store<in T : Action<*>> {

    fun register(view: Any) {
        this.bus.register(view)
    }

    fun unregister(view: Any) {
        this.bus.unregister(view)
    }

    protected fun emitStoreChange() {
        this.bus.post(changeEvent())
    }

    abstract fun changeEvent(): StoreChangeEvent

    abstract fun onAction(action: T)

    class StoreChangeEvent

    private val bus = Bus()
}
