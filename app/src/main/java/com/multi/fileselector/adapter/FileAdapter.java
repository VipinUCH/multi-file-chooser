package com.multi.fileselector.adapter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.bumptech.glide.Glide;
import com.multi.fileselector.Constants;
import com.multi.fileselector.R;
import com.multi.fileselector.Util.FileSelectionListener;
import com.multi.fileselector.adapter.viewholder.FileViewHolder;
import com.multi.fileselector.model.AppFiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vipin on 6/2/2017.
 */
public class FileAdapter extends FileBaseAdapter<FileViewHolder> {
//public class FileAdapter extends RecyclerView.Adapter<FileViewHolder> {

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
     * The package manager
     */
    private PackageManager packageManager;

    /**
     * The constructor
     * @param context the context
     * @param filesList the image list
     * @param fileSelectionListener the file selection listener
     */
    public FileAdapter(Context context, List<AppFiles> filesList, FileSelectionListener fileSelectionListener) {
        this.context = context;
        this.fileList = filesList;
        this.selectedFilesMap = new HashMap<>();
        this.fileSelectionListener = fileSelectionListener;
        this.packageManager = context.getPackageManager();
    }

    /**
     * Returns the selected files
     * @return the map of files
     */
    @Override
    public Map<String, AppFiles> getSelectedItems() {
        return selectedFilesMap;
    }

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
////                selectedFilesMap.put(position, buttonView.getTag(R.id.image_view).toString());
//            } else {
//                selectedFilesMap.remove(appFiles.getId());
//            }
        }
    };

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        if(viewType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
//
//        }
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_multi_file_item, parent, false);
        FileViewHolder holder = new FileViewHolder(itemView);
        holder.checkBox().setOnCheckedChangeListener(checkedChangeListener);
        return holder;
    }

//    @Override
//    public int getItemViewType(int position) {
//        AppFiles appFiles = getItem(position);
//        return appFiles.getMediaType();
//    }

    @Override
    public void onBindViewHolder(FileViewHolder holder, int position) {
        AppFiles appFiles = getItem(position);

        if(Constants.FileTypeConstants.APK == appFiles.getFileType()) {
            Glide.with(context)
                    .load("")
                    .centerCrop()
                    .placeholder(android.R.mipmap.sym_def_app_icon)
                    .error(getApkIconDrawable(appFiles.getPath()))
//                .error(R.drawable.ic_launcher)
                    .into(holder.imageView());
        } else {
            int resourceId = getResourceDrawable(appFiles.getFileType());
            Glide.with(context)
                    .load("")
                    .centerCrop()
                    .placeholder(resourceId)
                    .error(resourceId)
//                .error(R.drawable.ic_launcher)
                    .into(holder.imageView());
        }

        holder.checkBox().setTag(appFiles);
//        holder.checkBox().setTag(R.id.image_view, appFiles.getPath());
        holder.checkBox().setChecked(fileSelectionListener.isFileSelected(appFiles.getId()));
//        holder.checkBox().setChecked(selectedFilesMap.containsKey(appFiles.getId()));
        holder.fileName().setText(appFiles.getTitle());
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
            case MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO : return R.drawable.ic_music_file;
            case Constants.FileTypeConstants.PDF : return R.drawable.ic_pdf;
            case Constants.FileTypeConstants.DOCUMENT : return R.drawable.ic_doc;
            case Constants.FileTypeConstants.EXCEL : return R.drawable.ic_xls_1;
            case Constants.FileTypeConstants.PPT : return R.drawable.ic_ppt_1;
            case Constants.FileTypeConstants.TXT : return R.drawable.ic_txt;
            case Constants.FileTypeConstants.XML : return R.drawable.ic_xml_1;
            case Constants.FileTypeConstants.ZIP : return R.drawable.ic_zip;
            case Constants.FileTypeConstants.RAR : return R.drawable.ic_rar;
            case Constants.FileTypeConstants.GZIP : return R.drawable.ic_gzip;
            case Constants.FileTypeConstants.EXE : return R.drawable.ic_exe;
            case Constants.FileTypeConstants.DCM : return R.drawable.ic_dcm;
            case Constants.FileTypeConstants.UNKNOWN : return R.drawable.ic_file_unknown;
            default: return R.drawable.ic_file_unknown;
        }
    }

    /**
     * Returns the apk icon from the apk file path
     * @param apkFilePath the apk file path
     * @return the drawable of the apk
     */
    private Drawable getApkIconDrawable(String apkFilePath) {
//        PackageManager packageManager = context.getPackageManager();
        PackageInfo pi = packageManager.getPackageArchiveInfo(apkFilePath, 0);
        pi.applicationInfo.sourceDir = apkFilePath;
        pi.applicationInfo.publicSourceDir = apkFilePath;
//        String appName = (String) pi.applicationInfo.loadLabel(packageManager);
        return pi.applicationInfo.loadIcon(packageManager);
    }
}
