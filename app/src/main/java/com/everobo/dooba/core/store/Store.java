package com.everobo.dooba.core.store;

import com.squareup.otto.Bus;
import com.everobo.dooba.core.aciton.Action;


public abstract class Store<T extends Action> {
  private static final Bus bus = new Bus();

  public Store() {
  }

  public void register(final Object view) {
    this.bus.register(view);
  }

  public void unregister(final Object view) {
    this.bus.unregister(view);
  }

  protected void emitStoreChange() {
    this.bus.post(changeEvent());
  }

  public abstract StoreChangeEvent changeEvent();

  public abstract void onAction(T action);

  public static class StoreChangeEvent {
  }
}
