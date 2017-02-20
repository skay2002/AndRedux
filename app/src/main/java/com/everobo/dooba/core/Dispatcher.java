package com.everobo.dooba.core;

import com.everobo.dooba.core.aciton.Action;
import com.everobo.dooba.core.store.Store;
import java.util.ArrayList;
import java.util.List;

public class Dispatcher {

  private static Dispatcher instance;
  private final List<Store> stores = new ArrayList<>();

  public static Dispatcher getInstance() {
    if (instance == null) {
      instance = new Dispatcher();
    }
    return instance;
  }

  private Dispatcher() {
  }

  void register(final Store store) {
    if (!stores.contains(store)) {
      stores.add(store);
    }
  }

  void unregister(final Store store) {
    stores.remove(store);
  }

  public void dispatch(Action action) {
    post(action);
  }

  private void post(final Action action) {
    for (Store store : stores) {
      store.onAction(action);
    }
  }
}
