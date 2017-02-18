package com.everobo.dooba.core.aciton;


public interface ActionCreator<T extends Action> {
  T createAction(String type, Object... params);
}
