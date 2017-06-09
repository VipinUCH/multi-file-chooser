package com.multi.fileselector.model;

import java.io.Serializable;

/**
 * Created by Vipin on 6/5/2017.
 */

public class AppFiles implements Serializable {
    /**
     * The id of the file
     */
    private String id;
    /**
     * The title of the file
     */
    private String title;
    /**
     * The path of the file
     */
    private String path;
    /**
     * The mime type
     */
    private String mimeType;
    /**
     * The file type of the file
     */
    private int fileType;
    /**
     * The size of the file
     */
    private long size;
    /**
     * The file modified time
     */
    private long modifiedTimeStamp;
//    private String mediaType;

    /**
     * The default constructor
     */
    public AppFiles() {
    }

    /**
     * The constructor
     * @param id the id of the file
     * @param title the title of the file
     * @param path the path of the file
     * @param mimeType the mime type of the file
     */
    public AppFiles(String id, String title, String path, String mimeType) {
//    public AppFiles(String id, String title, String path, String mediaType) {
        this.id = id;
        this.title = title;
        this.path = path;
        this.mimeType = mimeType;
    }

    /**
     * Return the id of the file
     * @return the id of the file
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id of the file
     * @param id the id of the file
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Return the title of the file
     * @return the title of the file
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the file
     * @param title the title of the file
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Return the path of the file
     * @return the path of the file
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the path of the file
     * @param path the path of the file
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Return the mime type of the file
     * @return the mime type of the file
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Sets the mime type of the file
     * @param mimeType the mime type of the file
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * Return the file type of the file
     * @return the file type of the file
     */
    public int getFileType() {
        return fileType;
    }

    /**
     * Sets the file type of the file
     * @param fileType the file type of the file
     */
    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    /**
     * Return the size of the file
     * @return the size of the file
     */
    public long getSize() {
        return size;
    }

    /**
     * Sets the size of the file
     * @return the size of the file
     */
    public void setSize(long size) {
        this.size = size;
    }

    /**
     * Return the modified time stamp of the file
     * @return the modified time stamp of the file
     */
    public long getModifiedTimeStamp() {
        return modifiedTimeStamp;
    }

    /**
     * Sets the modified time stamp of the file
     * @return the modified time stamp of the file
     */
    public void setModifiedTimeStamp(long modifiedTimeStamp) {
        this.modifiedTimeStamp = modifiedTimeStamp;
    }
}
