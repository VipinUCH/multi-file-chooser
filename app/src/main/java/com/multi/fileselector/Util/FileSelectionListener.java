package com.multi.fileselector.Util;

import com.multi.fileselector.model.AppFiles;

/**
 * Created by Vipin on 6/6/2017.
 */

public interface FileSelectionListener {
    /**
     * The file selection listener
     * @param appFiles the file property object that is selected
     * @param isSelected the boolean which tells whether the object is selected or not
     */
    void onFileSelected(AppFiles appFiles, boolean isSelected);

    /**
     * Tells whether the file with the given id is selected or not
     * @param id the id of the file
     * @return true or false - selected or not
     */
    boolean isFileSelected(String id);
}
