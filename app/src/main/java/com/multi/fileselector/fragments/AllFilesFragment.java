package com.multi.fileselector.fragments;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.multi.fileselector.Constants;
import com.multi.fileselector.ItemOffsetDecoration;
import com.multi.fileselector.R;
import com.multi.fileselector.Util.AppStringUtil;
import com.multi.fileselector.adapter.FileAdapter;
import com.multi.fileselector.adapter.FileBaseAdapter;
import com.multi.fileselector.model.AppFiles;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

/**
 * Created by Vipin on 6/5/2017.
 */

public class AllFilesFragment extends BaseSelectionFragment {
//    private static final int REQUEST_FOR_STORAGE_PERMISSION = 123;
//    RecyclerView recyclerView;
//    FileAdapter imageAdapter;

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_file_selector, container, false);
//    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        initialize(view);
//        setAdapter(getFiles());
//    }

//    private void initialize(View view) {
//        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),4);
//        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new ItemOffsetDecoration(getContext(), R.dimen.item_offset));
//    }
//
//    private void setAdapter(List<AppFiles> appFiles) {
//        imageAdapter = new FileAdapter(getContext(), appFiles, fileSelectionListener);
//        recyclerView.setAdapter(imageAdapter);
//    }

    /**
     * Returns the corresponding adapter from the child classes
     * @return the file base adapter
     */
    @Override
    public FileBaseAdapter getAdapter() {
        return new FileAdapter(getContext(), getFiles(), fileSelectionListener);
    }

//    /**
//     * Populate whole files
//     */
//    private void populateFiles() {
//        if (!mayRequestExternalStorage()) {
//            return;
//        }
//        getFiles();
//    }

    /**
     * Returns the files that need to display
     * @return the list of files
     */
    @Override
    protected List<AppFiles> getFiles() {
        // Media type is not needed since we are not getting any media type files
//        final String[] columns = { MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.TITLE, MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.MEDIA_TYPE
        final String[] columns = { MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.TITLE, MediaStore.Files.FileColumns.DATA , MediaStore.Files.FileColumns.MIME_TYPE
                                    , MediaStore.Files.FileColumns.SIZE, MediaStore.Files.FileColumns.DATE_MODIFIED };
        final String orderBy = MediaStore.Files.FileColumns.DATE_MODIFIED;

        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_NONE;
        String[] selectionArgs = null;

        Cursor cursor = getContext().getContentResolver().query(
                MediaStore.Files.getContentUri("external"), columns, selection,
                selectionArgs, orderBy + " DESC");

        List<AppFiles> filesList = new ArrayList<AppFiles>();

        while(cursor.moveToNext()) {
            /** Fetching all files of the system. So need to remove the directories. Cannot able to put this condition in Content provider query */
            if (isFileADirectory(cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)))) {
                continue;
            }
            AppFiles appFiles = new AppFiles();
            appFiles.setId(cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)));
            appFiles.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE)));
            appFiles.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)));
            appFiles.setMimeType(cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE)));
            appFiles.setSize(cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE)));
            appFiles.setModifiedTimeStamp(cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED)));
            appFiles.setFileType(getFileType(getActualExtension(appFiles)));
            filesList.add(appFiles);
            System.out.println("AllFiles PROPERTIES =====> ID: " + appFiles.getId() + " TITLE: " + appFiles.getTitle() + " MIME_TYPE: " + appFiles.getMimeType() + " FILE_TYPE: " + appFiles.getFileType() + " PATH: " + appFiles.getPath());
        }

        return filesList;
    }

    /**
     * Checking whether the provided file path is a file or directory
     * @param path the path of the file
     * @return true or false saying directory or not
     */
    private boolean isFileADirectory(String path) {
        File file = new File(path);
        return file.isDirectory();
    }

//    /**
//     * TODO - Check this function and keep the process simple by checking only the path, and do not save the mime type to the object if not needed for any other purpose.
//     * Returns the file extension based on the MIME_TYPE and file extension.
//     * Here we are checking based on MIME_TYPE first because it seems finding from MIME_TYPE is more accurate. Not know whether it is correct.
//     * And if the extension returned from mime type is zero, it is going with the file extension retrieved from path
//     * @param appFiles the app file object
//     * @return the extension of the file
//     */
//    private String getActualExtension(AppFiles appFiles) {
//        String extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(appFiles.getMimeType());
//        return extension == null ? MimeTypeMap.getFileExtensionFromUrl(appFiles.getPath()) : extension;
//    }
//
//    /**
//     * Returns the file type from mime type
//     * @param extension the extension of the file
//     * @return the file type
//     */
//    private int getFileType(String extension) {
//        if (AppStringUtil.containsIgnoreCase("pdf", extension)) {
//            return Constants.FileTypeConstants.PDF;
//        } else if (AppStringUtil.containsIgnoreCase("doc", extension) || AppStringUtil.containsIgnoreCase("docx", extension)) {
//            return Constants.FileTypeConstants.DOCUMENT;
//        } else if (AppStringUtil.containsIgnoreCase("xls", extension) || AppStringUtil.containsIgnoreCase("xlsx", extension)) {
//            return Constants.FileTypeConstants.EXCEL;
//        } else if (AppStringUtil.containsIgnoreCase("ppt", extension) || AppStringUtil.containsIgnoreCase("pptx", extension)) {
//            return Constants.FileTypeConstants.PPT;
//        } else if (AppStringUtil.containsIgnoreCase("txt", extension)) {
//            return Constants.FileTypeConstants.TXT;
//        } else if (AppStringUtil.containsIgnoreCase("xml", extension)) {
//            return Constants.FileTypeConstants.XML;
//        } else if (AppStringUtil.containsIgnoreCase("zip", extension) || AppStringUtil.containsIgnoreCase("zipx", extension)) {
//            return Constants.FileTypeConstants.ZIP;
//        } else if (AppStringUtil.containsIgnoreCase("rar", extension) || AppStringUtil.containsIgnoreCase("rev", extension)
//                || AppStringUtil.containsIgnoreCase("r00", extension) || AppStringUtil.containsIgnoreCase("r01", extension)) {
//            return Constants.FileTypeConstants.RAR;
//        } else if (AppStringUtil.containsIgnoreCase("gz", extension)) {
//            return Constants.FileTypeConstants.GZIP;
//        } else if (AppStringUtil.containsIgnoreCase("apk", extension)) {
//            return Constants.FileTypeConstants.APK;
//        } else if (AppStringUtil.containsIgnoreCase("exe", extension)) {
//            return Constants.FileTypeConstants.EXE;
//        } else {
//            return Constants.FileTypeConstants.UNKNOWN;
//        }
//    }

//    /**
//     * Returns the file type from mime type
//     * @param mimeType the mime type
//     * @return the file type
//     */
//    private int getFileType(String mimeType) {
//        String extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
//        if (AppStringUtil.containsIgnoreCase("pdf", extension)) {
//            return Constants.FileTypeConstants.PDF;
//        } else if (AppStringUtil.containsIgnoreCase("doc", extension) || AppStringUtil.containsIgnoreCase("docx", extension)) {
//            return Constants.FileTypeConstants.DOCUMENT;
//        } else if (AppStringUtil.containsIgnoreCase("xls", extension) || AppStringUtil.containsIgnoreCase("xlsx", extension)) {
//            return Constants.FileTypeConstants.EXCEL;
//        } else if (AppStringUtil.containsIgnoreCase("ppt", extension) || AppStringUtil.containsIgnoreCase("pptx", extension)) {
//            return Constants.FileTypeConstants.PPT;
//        } else if (AppStringUtil.containsIgnoreCase("txt", extension)) {
//            return Constants.FileTypeConstants.TXT;
//        } else if (AppStringUtil.containsIgnoreCase("xml", extension)) {
//            return Constants.FileTypeConstants.XML;
//        } else if (AppStringUtil.containsIgnoreCase("zip", extension) || AppStringUtil.containsIgnoreCase("zipx", extension)) {
//            return Constants.FileTypeConstants.ZIP;
//        } else if (AppStringUtil.containsIgnoreCase("rar", extension) || AppStringUtil.containsIgnoreCase("rev", extension)
//                || AppStringUtil.containsIgnoreCase("r00", extension) || AppStringUtil.containsIgnoreCase("r01", extension)) {
//            return Constants.FileTypeConstants.RAR;
//        } else if (AppStringUtil.containsIgnoreCase("gz", extension)) {
//            return Constants.FileTypeConstants.GZIP;
//        } else if (AppStringUtil.containsIgnoreCase("apk", extension)) {
//            return Constants.FileTypeConstants.APK;
//        } else if (AppStringUtil.containsIgnoreCase("exe", extension)) {
//            return Constants.FileTypeConstants.EXE;
//        } else {
//            return Constants.FileTypeConstants.UNKNOWN;
//        }
//    }

//    private void showPermissionRationaleSnackBar() {
//        Snackbar.make(recyclerView, getString(R.string.permission_rationale),
//                Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Request the permission
//                ActivityCompat.requestPermissions(getActivity(),
//                        new String[]{READ_EXTERNAL_STORAGE},
//                        REQUEST_FOR_STORAGE_PERMISSION);
//            }
//        }).show();
//
//    }

//    private boolean mayRequestExternalStorage() {
//
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return true;
//        }
//
//        if (getContext().checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        }
//
//        if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
//            //promptStoragePermission();
//            showPermissionRationaleSnackBar();
//        } else {
//            requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, REQUEST_FOR_STORAGE_PERMISSION);
//        }
//
//        return false;
//    }

//    /**
//     * Callback received when a permissions request has been completed.
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//
//        switch (requestCode) {
//
//            case REQUEST_FOR_STORAGE_PERMISSION: {
//
//                if (grantResults.length > 0) {
//                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////                        populateImagesFromGallery();
//                        populateFiles();
//                    } else {
//                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), READ_EXTERNAL_STORAGE)) {
//                            showPermissionRationaleSnackBar();
//                        } else {
//                            Toast.makeText(getContext(), "Go to settings and enable permission", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                }
//
//                break;
//            }
//        }
//    }

    @Override
    public int getTitle() {
        return R.string.all;
    }

    @Override
    public Map<String, String> getSelectedItems() {
        return null;
    }
}
