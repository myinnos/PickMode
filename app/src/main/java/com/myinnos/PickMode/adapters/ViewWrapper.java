package com.myinnos.PickMode.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by myinnos on 07/09/17.
 */

public class ViewWrapper<V extends View> extends RecyclerView.ViewHolder {

    private final V view;

    public ViewWrapper(V itemView) {
        super(itemView);
        view = itemView;
    }

    public V getView() {
        return view;
    }
}
