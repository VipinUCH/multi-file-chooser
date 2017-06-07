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
    private int mediaType;
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
     * @param mediaType the media type of the file
     */
    public AppFiles(String id, String title, String path, int mediaType) {
//    public AppFiles(String id, String title, String path, String mediaType) {
        this.id = id;
        this.title = title;
        this.path = path;
        this.mediaType = mediaType;
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
    public int getMediaType() {
        return mediaType;
    }

    /**
     * Sets the mime type of the file
     * @param mediaType the mime type of the file
     */
    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }
}
