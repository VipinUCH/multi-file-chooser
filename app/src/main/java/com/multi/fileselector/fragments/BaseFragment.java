package com.multi.fileselector.fragments;

import android.support.v4.app.Fragment;

import java.util.Map;

/**
 * Created by Vipin on 6/6/2017.
 */

public abstract class BaseFragment extends Fragment {
    /**
     * The method to get the individual title for the fragment
     * @return the resource id of the page title
     */
    public abstract int getTitle();

    /**
     * Return the selected file items.
     * @return the selected files
     */
    public abstract Map<String, String> getSelectedItems();
}
