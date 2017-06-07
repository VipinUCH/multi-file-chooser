package com.multi.fileselector.adapter;

import android.content.Context;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.bumptech.glide.Glide;
import com.multi.fileselector.R;
import com.multi.fileselector.Util.FileSelectionListener;
import com.multi.fileselector.adapter.viewholder.MediaViewHolder;
import com.multi.fileselector.model.AppFiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vipin on 6/2/2017.
 */
public class MediaAdapter extends FileBaseAdapter<MediaViewHolder> {
//public class MediaAdapter extends RecyclerView.Adapter<MediaViewHolder> {
    /**
     * The context
     */
    private Context context;
    /**
     * The file list
     */
    private List<AppFiles> fileList;
    /**
     * The selected files
     */
    private Map<String, AppFiles> selectedFilesMap;
    /**
     * The file selection listener
     */
    private FileSelectionListener fileSelectionListener;

    /**
     * The constructor
     * @param context the context
     * @param fileList the image list
     * @param fileSelectionListener the file selection listener
     */
    public MediaAdapter(Context context, List<AppFiles> fileList, FileSelectionListener fileSelectionListener) {
        this.context = context;
//        mSparseBooleanArray = new SparseBooleanArray();
        this.fileList = fileList;
        selectedFilesMap = new HashMap<>();
        this.fileSelectionListener = fileSelectionListener;

    }

    /**
     * Returns the selected files
     * @return the map of files
     */
    @Override
    public Map<String, AppFiles> getSelectedItems() {
        return selectedFilesMap;
    }

//    public ArrayList<String> getCheckedItems() {
//        ArrayList<String> mTempArry = new ArrayList<String>();
//
//        for(int i=0;i<mImagesList.size();i++) {
//            if(mSparseBooleanArray.get(i)) {
//                mTempArry.add(mImagesList.get(i));
//            }
//        }
//
//        return mTempArry;
//    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * The check box change listener
     */
    CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            AppFiles appFiles = (AppFiles) buttonView.getTag();
            if(appFiles != null && fileSelectionListener != null ) {
                fileSelectionListener.onFileSelected(appFiles, isChecked);
            }
//            if(isChecked) {
//                selectedFilesMap.put(appFiles.getId(), appFiles);
//            } else {
//                selectedFilesMap.remove(appFiles.getId());
//            }
        }
    };

//    CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
//
//        @Override
//        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//            mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
//        }
//    };

    @Override
    public MediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.row_multi_photo_item, parent, false);
//
//        return new MyViewHolder(itemView);

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_multi_photo_item, parent, false);
        MediaViewHolder holder = new MediaViewHolder(itemView);
        holder.checkBox().setOnCheckedChangeListener(checkedChangeListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(MediaViewHolder holder, int position) {

        AppFiles appFiles = fileList.get(position);
//        String imageUrl = mImagesList.get(position);

//        Glide.with(context)
//                .load("file://" + appFiles.getPath())
//                .centerCrop()
//                .placeholder(R.drawable.ic_launcher)
//                .error(R.drawable.ic_launcher)
//                .into(holder.imageView());
//
//        holder.checkBox.setTag(position);
//        holder.checkBox.setChecked(mSparseBooleanArray.get(position));
//        holder.checkBox.setOnCheckedChangeListener(mCheckedChangeListener);

        int resourceId = getResourceDrawable(appFiles.getMediaType());
        Glide.with(context)
                .load("file://" + appFiles.getPath())
                .centerCrop()
                .placeholder(resourceId)
                .error(resourceId)
//                .error(R.drawable.ic_launcher)
                .into(holder.imageView());

        holder.checkBox().setTag(appFiles);
//        holder.checkBox().setTag(R.id.image_view, appFiles.getPath());
        holder.checkBox().setChecked(fileSelectionListener.isFileSelected(appFiles.getId()));
//        holder.checkBox().setChecked(selectedFilesMap.containsKey(appFiles.getId()));
        holder.playIcon().setVisibility(appFiles.getMediaType() == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE ? View.GONE : View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    /**
     * Returns the item associated with it
     * @param position the position
     * @return the item
     */
    @Override
    public AppFiles getItem(int position) {
        return fileList.get(position);
    }

    /**
     * Returns the media resource type to show in the image view
     * @param mediaType the media type
     * @return the resource id
     */
    private int getResourceDrawable(int mediaType) {
        switch (mediaType) {
            case MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE : return R.drawable.ic_picture;
            case MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO : return R.drawable.ic_video;
            default: return R.drawable.ic_file_unknown;
        }
    }

//    public class MyViewHolder extends RecyclerView.ViewHolder {
//
//        public CheckBox checkBox;
//        public ImageView imageView;
//
//        public MyViewHolder(View view) {
//            super(view);
//
//            checkBox = (CheckBox) view.findViewById(R.id.check_box);
//            imageView = (ImageView) view.findViewById(R.id.image_view);
//        }
//    }

}
