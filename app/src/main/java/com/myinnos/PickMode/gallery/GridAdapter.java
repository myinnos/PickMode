package com.myinnos.PickMode.gallery;

import android.content.Context;
import android.view.ViewGroup;

import com.myinnos.PickMode.adapters.RecyclerViewAdapterBase;
import com.myinnos.PickMode.adapters.ViewWrapper;

import java.io.File;

/**
 * Created by myinnos on 07/09/17.
 */

class GridAdapter extends RecyclerViewAdapterBase<File, MediaItemView> {

    private final Context context;

    GridAdapter(Context context) {
        this.context = context;
    }

    @Override
    protected MediaItemView onCreateItemView(ViewGroup parent, int viewType) {
        return new MediaItemView(context);
    }

    @Override
    public void onBindViewHolder(ViewWrapper<MediaItemView> viewHolder, final int position) {
        MediaItemView itemView = viewHolder.getView();
        itemView.bind(mItems.get(position));
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}