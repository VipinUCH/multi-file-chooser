package com.multi.fileselector.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.multi.fileselector.R;

/**
 * Created by Vipin on 6/5/2017.
 */

public class MediaViewHolder extends RecyclerView.ViewHolder {
    /**
     * The image view
     */
    private ImageView imageView;
    /**
     * The play icon
     */
    private ImageView imvPlayIcon;
    /**
     * The checkbox
     */
    private CheckBox checkBox;

    /**
     * The constructor
     * @param view the row view
     */
    public MediaViewHolder(View view) {
        super(view);
        imageView = (ImageView) view.findViewById(R.id.image_view);
        imvPlayIcon = (ImageView) view.findViewById(R.id.video_icon);
        checkBox = (CheckBox) view.findViewById(R.id.check_box);
    }

    /**
     * Returns the image view
     * @return the image view
     */
    public ImageView imageView() {
        return imageView;
    }

    /**
     * The play icon
     * @return the play icon
     */
    public ImageView playIcon() {
        return imvPlayIcon;
    }

    /**
     * The check box
     * @return the check box
     */
    public CheckBox checkBox() {
        return checkBox;
    }
}
