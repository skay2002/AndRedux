package com.everobo.dooba.core.aciton;

import android.support.v4.util.ArrayMap;

public class ArrayMapAction implements Action<ArrayMap<String, Object>> {

  private String type;
  private ArrayMap<String, Object> datas;

  public void init(String type, Object... params) {
    this.type = type;
    this.datas = new ArrayMap<>();

    try {
      int i = 0;
      while (i < params.length) {
        String key = (String) params[i++];
        Object value = params[i++];
        datas.put(key, value);
      }
    }catch (IndexOutOfBoundsException e){
      throw new IllegalArgumentException("do you forgot the key?");
    }
  }

  @Override public ArrayMap<String, Object> getDatas() {
    return datas;
  }

  @Override public String getType() {
    return type;
  }
}
