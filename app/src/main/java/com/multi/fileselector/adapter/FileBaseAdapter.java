package com.multi.fileselector.adapter;

import android.support.v7.widget.RecyclerView;

import com.multi.fileselector.model.AppFiles;

import java.util.Map;

/**
 * Created by Vipin on 6/7/2017.
 */

public abstract class FileBaseAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    /**
     * Returns the selected files
     * @return the map of files
     */
    public abstract Map<String, AppFiles> getSelectedItems();
    /**
     * Returns the item associated with it
     * @param position the position
     * @return the item
     */
    public abstract AppFiles getItem(int position);
}

