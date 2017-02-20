package com.everobo.dooba.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

class AbsViewHolder extends RecyclerView.ViewHolder {

  private SparseArray<View> views;
  private View itemView;

  AbsViewHolder(View itemView) {
    super(itemView);
    this.itemView = itemView;
    views = new SparseArray<>();
  }

  <T extends View> T getView(int id) {
    View view;
    if (null != views.get(id)) {
      view = views.get(id);
    } else {
      view = itemView.findViewById(id);
      views.put(id, view);
    }
    return (T) view;
  }

  AbsViewHolder setText(int id, CharSequence charSequence) {
    View view;
    TextView textView;
    if (charSequence == null) {
      return this;
    }

    if (null != (view = getView(id))) {
      textView = (TextView) view;
      textView.setText(charSequence);
    }
    return this;
  }

  AbsViewHolder setCheck(int id, boolean checked) {
    View view;
    CheckBox checkBox;
    if (null != (view = getView(id))) {
      checkBox = (CheckBox) view;
      checkBox.setChecked(checked);
    }

    return this;
  }

  void setClickable(boolean clickable){
    itemView.setClickable(clickable);
  }

  void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
    if (onLongClickListener != null) {
      itemView.setOnLongClickListener(onLongClickListener);
    }
  }

  void setOnClickListener(View.OnClickListener onClickListener) {
    if (onClickListener != null) {
      itemView.setOnClickListener(onClickListener);
    }
  }

}
