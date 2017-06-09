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
import android.widget.Toast;

import com.multi.fileselector.ItemOffsetDecoration;
import com.multi.fileselector.R;
import com.multi.fileselector.adapter.FileBaseAdapter;
import com.multi.fileselector.adapter.MediaAdapter;
import com.multi.fileselector.model.AppFiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

/**
 * Created by Vipin on 6/5/2017.
 */

public class VideoFileFragment extends BaseSelectionFragment {
//    private static final int REQUEST_FOR_STORAGE_PERMISSION = 123;
//    RecyclerView recyclerView;
//    MediaAdapter imageAdapter;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_file_selector, container, false);
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        initialize(view);
//        setAdapter(getAllFiles());
//    }
//
//    private void initialize(View view) {
//        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),4);
//        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new ItemOffsetDecoration(getContext(), R.dimen.item_offset));
//    }
//
//    private void setAdapter(List<AppFiles> appFiles) {
//        imageAdapter = new MediaAdapter(getContext(), appFiles, fileSelectionListener);
//        recyclerView.setAdapter(imageAdapter);
//    }
//
//    /**
//     * Populate whole files
//     */
//    private void populateFiles() {
//        if (!mayRequestExternalStorage()) {
//            return;
//        }
//        getAllFiles();
//    }

    /**
     * Returns the corresponding adapter from the child classes
     * @return the file base adapter
     */
    @Override
    public FileBaseAdapter getAdapter() {
        return new MediaAdapter(getContext(), getFiles(), fileSelectionListener);
    }

    /**
     * Returns the files that need to display
     * @return the list of files
     */
    @Override
    protected List<AppFiles> getFiles() {
        final String[] columns = { MediaStore.Video.Media._ID, MediaStore.Images.Media.TITLE, MediaStore.Video.Media.DATA, MediaStore.Video.Media.MIME_TYPE
                                            , MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DATE_MODIFIED };
        final String orderBy = MediaStore.Video.Media.DATE_MODIFIED;

        Cursor cursor = getContext(). getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy + " DESC");
//        Cursor cursor = managedQuery(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
//                null, orderBy + " DESC");

        List<AppFiles> filesList = new ArrayList<AppFiles>();

        while(cursor.moveToNext()) {
            AppFiles appFiles = new AppFiles();
            appFiles.setId(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID)));
            appFiles.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE)));
            appFiles.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)));
            appFiles.setMimeType(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE)));
            appFiles.setSize(cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE)));
            appFiles.setModifiedTimeStamp(cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED)));
            appFiles.setFileType(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO);
            filesList.add(appFiles);
            System.out.println("VideoFiles PROPERTIES =====> ID: " + appFiles.getId() + " TITLE: " + appFiles.getTitle() + " MIME_TYPE: " + appFiles.getMimeType() + " FILE_TYPE: " + appFiles.getFileType() + " PATH: " + appFiles.getPath());
        }

//        List<String> fileUrls = new ArrayList<String>();
//
//        for (int i = 0; i < cursor.getCount(); i++) {
//            cursor.moveToPosition(i);
//            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
//            fileUrls.add(cursor.getString(dataColumnIndex));
//
//            System.out.println("Video =====> Array path => " + fileUrls.get(i));
//        }
//
//        return fileUrls;

        return filesList;
    }

//    private List<AppFiles> getFiles() {
//        final String[] columns = { MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns._ID };
//        final String orderBy = MediaStore.Files.FileColumns.DATE_MODIFIED;
//
////        String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + " IN (?)";
//////        String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
//        String mimeTypePdf = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf");
//        String mimeTypeDoc = MimeTypeMap.getSingleton().getMimeTypeFromExtension("doc");
//        String mimeTypeDocx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("docx");
//        String mimeTypeXml = MimeTypeMap.getSingleton().getMimeTypeFromExtension("xml");
//        String mimeTypeXls = MimeTypeMap.getSingleton().getMimeTypeFromExtension("xls");
//        String mimeTypeXlsx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("xlsx");
//        String mimeTypeTxt = MimeTypeMap.getSingleton().getMimeTypeFromExtension("txt");
////        String mimeTypeDcm = MimeTypeMap.getSingleton().getMimeTypeFromExtension("dcm");
//        String mimeTypePpt = MimeTypeMap.getSingleton().getMimeTypeFromExtension("ppt");
//        String mimeTypePptx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pptx");
//        String[] selectionArgsPdf = new String[]{ mimeTypePdf, mimeTypeDocx, mimeTypeDoc, mimeTypeXml, mimeTypeXls, mimeTypeXlsx, mimeTypeTxt, mimeTypePpt, mimeTypePptx };
////        String[] selectionArgsPdf = new String[]{ mimeTypePdf, mimeTypeDocx, mimeTypeDoc, mimeTypeXml, mimeTypeXls, mimeTypeXlsx, mimeTypeTxt, mimeTypeDcm, mimeTypePpt, mimeTypePptx };
////
//        String[] selectionArgs = {TextUtils.join(", ", selectionArgsPdf)};
//
////        String types = mimeTypePdf + ", " + mimeTypeDoc + ", " + mimeTypeDocx + ", " + mimeTypePpt + ", " + mimeTypePptx;
//        String types = "\"" + mimeTypePdf + "\", " + "\"" + mimeTypeDoc + "\", " +"\"" + mimeTypeDocx + "\", "
//                + "\"" + mimeTypeXml + "\", " + "\"" + mimeTypeXls + "\""
//                + "\"" + mimeTypeXlsx + "\", " + "\"" + mimeTypeTxt + "\""
//                + "\"" + mimeTypePpt + "\", " + "\"" + mimeTypePptx + "\"";
//
//        String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + " IN (" + types + " )";
////        String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
////        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("doc");
//////        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf");
////        String[] selectionArgs = new String[]{ mimeType };
//
////        Cursor cursor = getContentResolver().query(
////                MediaStore.Files.getContentUri("external"), columns, selectionMimeType,
////                selectionArgs, orderBy + " DESC");
//
//        Cursor cursor = getContext().getContentResolver().query(
//                MediaStore.Files.getContentUri("external"), columns, selectionMimeType,
//                null, orderBy + " DESC");
//
//        List<AppFiles> filesList = new ArrayList<AppFiles>();
//
//        while(cursor.moveToNext()) {
//            AppFiles appFiles = new AppFiles(cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)),
//                    cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE)),
//                    cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)), cursor.getInt(cursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE)));
//            filesList.add(appFiles);
//            System.out.println("Files PROPERTIES =====> ID: " + appFiles.getId() + " TITLE: " + appFiles.getTitle() + "MEDIA_TYPE: " + appFiles.getMediaType() + " PATH: " + appFiles.getPath());
//        }
//
////        for (int i = 0; i < cursor.getCount(); i++) {
////            cursor.moveToPosition(i);
////            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
////            filesList.add(cursor.getString(dataColumnIndex));
////
////            System.out.println("Files =====> Array path => " + filesList.get(i));
////        }
//
//        return filesList;
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
//
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
//
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
        return R.string.video;
    }

    @Override
    public Map<String, String> getSelectedItems() {
        return null;
    }
}
