package com.multi.fileselector.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import com.multi.fileselector.Util.FileSelectionListener;
import com.multi.fileselector.adapter.FileAdapter;
import com.multi.fileselector.adapter.FileBaseAdapter;
import com.multi.fileselector.model.AppFiles;

import java.util.List;

/**
 * Created by Vipin on 6/6/2017.
 */

public abstract class BaseSelectionFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    /**
     * The request code for read external storage
     */
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION = 123;
    /**
     * The file selection listener
     */
    protected FileSelectionListener fileSelectionListener;
    /**
     * The swipe refresh layout
     */
    private SwipeRefreshLayout swipeRefreshLayout;
    /**
     * The recycler view
     */
    private RecyclerView recyclerView;
    /**
     * The file adapter
     */
    private FileBaseAdapter fileBaseAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fileSelectionListener = (FileSelectionListener) context;
        } catch (ClassCastException ex) {
            throw new ClassCastException(context.getString(R.string.error_activity_not_implemented_file_selection_listener));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_file_selector, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
        register();
//        fileBaseAdapter = getAdapter();
        checkPermissionAndLoadScreen();
    }

    /**
     * Initializes the variables
     * @param view the view
     */
    private void initialize(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),4);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new ItemOffsetDecoration(getContext(), R.dimen.item_offset));
    }

    /**
     * Register the events
     */
    private void register() {
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    /**
     * The file base adapter
     * @param fileBaseAdapter the file base adapter
     */
    private void setAdapter(FileBaseAdapter fileBaseAdapter) {
        this.fileBaseAdapter = fileBaseAdapter;
        recyclerView.setAdapter(fileBaseAdapter);
    }

    /**
     * Check permissions and if permission granted load the screen or else requesting permission
     */
    private void checkPermissionAndLoadScreen() {
        if(isPermissionGrantedFor(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            setAdapter(getAdapter());
        } else {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION);
        }
    }

    /**
     * Checks the permission after the permission request completed and if completed loads the screen
     * @param permission the permission
     */
    private void loadScreenIfPermissionGranted(String permission) {
        if(isPermissionGrantedFor(permission)) {
            setAdapter(getAdapter());
        }
    }

    /**
     * Shows a toast message
     * @param message the message to show
     */
    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    /**
     * Tells whether the requested permission is granted or not
     * @param permission the permission requested
     * @return the boolean true or not
     */
    private boolean isPermissionGrantedFor(String permission) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(getContext(), permission) == PackageManager.PERMISSION_GRANTED;
        }
        /** Assuming that the permission is provided in the manifest. And for the builds less than 23,
         *  the permission will ask at the time of installation/ updation of the application and user is
         *  not able to revoke at any point. Only way to revoke is through un install of the application. */
        return true;
    }

    /**
     * Request the specified permission with a request code
     * @param permission the permission
     * @param requestCode the request code
     */
    private void requestPermission(final String permission, final int requestCode) {
        if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            /** Checking whether the user has already denied the request. If so show a dialog about the necessity about the permission. */
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    permission)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle(R.string.information);
                alertBuilder.setMessage(R.string.permission_rationale);
                alertBuilder.setPositiveButton(R.string.allow, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        /** Requesting the permission. */
                        ActivityCompat.requestPermissions(getActivity(), new String[]{ permission }, requestCode);
                    }
                });
                alertBuilder.setNegativeButton(R.string.deny, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showToast(getContext().getString(R.string.permission_rationale_neglect_text));
                    }
                });
                alertBuilder.create().show();
            } else {
                /** Requesting the permission. */
                ActivityCompat.requestPermissions(getActivity(), new String[]{ permission }, requestCode);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION: loadScreenIfPermissionGranted(permissions[0]);
                break;
        }
    }

    @Override
    public void onRefresh() {
        setAdapter(getAdapter());
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * TODO - Check this function and keep the process simple by checking only the path, and do not save the mime type to the object if not needed for any other purpose.
     * Returns the file extension based on the MIME_TYPE and file extension.
     * Here we are checking based on MIME_TYPE first because it seems finding from MIME_TYPE is more accurate. Not know whether it is correct.
     * And if the extension returned from mime type is zero, it is going with the file extension retrieved from path
     * @param appFiles the app file object
     * @return the extension of the file
     */
    protected String getActualExtension(AppFiles appFiles) {
        String extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(appFiles.getMimeType());
        return extension == null ? AppStringUtil.getFileExtensionFromUrl(appFiles.getPath()) : extension;
//        return extension == null ? MimeTypeMap.getFileExtensionFromUrl(appFiles.getPath()) : extension;
    }

    /**
     * Returns the file type from mime type
     * @param extension the extension of the file
     * @return the file type
     */
    protected int getFileType(String extension) {
        if ("pdf".equalsIgnoreCase(extension)) {
            return Constants.FileTypeConstants.PDF;
        } else if ("doc".equalsIgnoreCase(extension) || "docx".equalsIgnoreCase(extension)) {
            return Constants.FileTypeConstants.DOCUMENT;
        } else if ("xls".equalsIgnoreCase(extension) || "xlsx".equalsIgnoreCase(extension)) {
            return Constants.FileTypeConstants.EXCEL;
        } else if ("ppt".equalsIgnoreCase(extension) || "pptx".equalsIgnoreCase(extension)) {
            return Constants.FileTypeConstants.PPT;
        } else if ("txt".equalsIgnoreCase(extension)) {
            return Constants.FileTypeConstants.TXT;
        } else if ("xml".equalsIgnoreCase(extension)) {
            return Constants.FileTypeConstants.XML;
        } else if ("zip".equalsIgnoreCase(extension) || "zipx".equalsIgnoreCase(extension)) {
            return Constants.FileTypeConstants.ZIP;
        } else if ("rar".equalsIgnoreCase(extension) || "rev".equalsIgnoreCase(extension)
                || "r00".equalsIgnoreCase(extension) || "r01".equalsIgnoreCase(extension)) {
            return Constants.FileTypeConstants.RAR;
        } else if ("gz".equalsIgnoreCase(extension)) {
            return Constants.FileTypeConstants.GZIP;
        } else if ("apk".equalsIgnoreCase(extension)) {
            return Constants.FileTypeConstants.APK;
        } else if ("exe".equalsIgnoreCase(extension)) {
            return Constants.FileTypeConstants.EXE;
        } else if ("dcm".equalsIgnoreCase(extension)) {
            return Constants.FileTypeConstants.DCM;
        } else {
            return Constants.FileTypeConstants.UNKNOWN;
        }
    }

//    /**
//     * Returns the file type from mime type
//     * @param extension the extension of the file
//     * @return the file type
//     */
//    protected int getFileType(String extension) {
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

    /**
     * Returns the corresponding adapter from the child classes
     * @return the file base adapter
     */
    protected abstract FileBaseAdapter getAdapter();

    /**
     * Returns the files that need to display
     * @return the list of files
     */
    protected abstract List<AppFiles> getFiles();
}
