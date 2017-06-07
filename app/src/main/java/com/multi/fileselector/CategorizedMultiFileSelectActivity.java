package com.multi.fileselector;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.multi.fileselector.Util.FileSelectionListener;
import com.multi.fileselector.adapter.ViewPagerAdapter;
import com.multi.fileselector.fragments.AllFilesFragment;
import com.multi.fileselector.fragments.AudioFileFragment;
import com.multi.fileselector.fragments.BaseFragment;
import com.multi.fileselector.fragments.DocumentFilesFragment;
import com.multi.fileselector.fragments.ImageFileFragment;
import com.multi.fileselector.fragments.VideoFileFragment;
import com.multi.fileselector.model.AppFiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vipin on 6/2/2017.
 */
public class CategorizedMultiFileSelectActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener, FileSelectionListener {
    /**
     * The request code for read external storage
     */
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION = 123;
    /**
     * The view pager
     */
    private ViewPager viewPager;
    /**
     * The tab layout
     */
    private TabLayout tabLayout;
    /**
     * The view pager adapter
     */
    private ViewPagerAdapter viewPagerAdapter;
    /**
     * The map that holds the information about selected files
     */
    private Map<String, AppFiles> selectedFiles;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_file_select);
        initialize();
        register();
        restoreValuesInCaseOfConfigChange(savedInstanceState);
        checkPermissionAndLoadScreen();
    }

    /**
     * This saves the already selected files when a config change such as orientation change happens
     * @param outState the bundle to save the state
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Constants.KEY_SELECTED_FILES, (HashMap<String, AppFiles>) selectedFiles);
        super.onSaveInstanceState(outState);
    }

    /**
     * Returns the context
     * @return the activity context
     */
    private Context getContext() {
        return CategorizedMultiFileSelectActivity.this;
    }

    /**
     * Returns the activity
     * @return the activity
     */
    private Activity getActivity() {
        return CategorizedMultiFileSelectActivity.this;
    }

    /**
     * Initializes the variables
     */
    private void initialize() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        selectedFiles = new HashMap<>();
    }

    /**
     * Register the events
     */
    private void register() {
        tabLayout.setupWithViewPager(viewPager, false);
        viewPager.addOnPageChangeListener(CategorizedMultiFileSelectActivity.this);
        tabLayout.addOnTabSelectedListener(CategorizedMultiFileSelectActivity.this);
    }

    /**
     * Restore the value from config change if there is some selection saved
     * @param bundle the bundle
     */
    private void restoreValuesInCaseOfConfigChange(Bundle bundle) {
        if(bundle != null) {
            selectedFiles = getAlreadySelectedFiles(bundle);
        }
    }

    /**
     * Return the ids that are already plotted in the graph
     * @param bundle the bundle from which the extras need to fetch
     * @return the string array with ids that are plotted to graph
     */
    private Map<String, AppFiles> getAlreadySelectedFiles(Bundle bundle) {
        if(bundle != null && bundle.containsKey(Constants.KEY_SELECTED_FILES)) {
            return (Map<String, AppFiles>) bundle.getSerializable(Constants.KEY_SELECTED_FILES);
        }
        return new HashMap<String, AppFiles>();
    }

    /**
     * Check permissions and if permission granted load the screen or else requesting permission
     */
    private void checkPermissionAndLoadScreen() {
        if(isPermissionGrantedFor(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            setPagerAdapter();
        } else {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION);
        }
    }

    /**
     * Sets the view pager adapter
     */
    private void setPagerAdapter() {
        List<BaseFragment> fragments = getFragments();
        viewPagerAdapter = new ViewPagerAdapter(getContext(), getSupportFragmentManager(), fragments);
        if (viewPager != null && viewPagerAdapter != null) {
            viewPager.setAdapter(viewPagerAdapter);
        }
    }

//    private List<Fragment> getFragments() {
//        List<Fragment> fragmentList = new ArrayList<Fragment>();
//        Bundle bundle = new Bundle();
//        bundle.putInt(MediaStore.Files.FileColumns.MEDIA_TYPE, MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE);
//        Fragment imageFragment = new ImageFileFragment();
//        fragmentList.add(imageFragment);
//        Bundle bundleVideo = new Bundle();
//        bundleVideo.putInt(MediaStore.Files.FileColumns.MEDIA_TYPE, MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO);
//        Fragment videoFragment = new ImageFileFragment();
//        fragmentList.add(videoFragment);
//        fragmentList.add(new AudioFileFragment());
//        fragmentList.add(new DocumentFilesFragment());
//        fragmentList.add(new AllFilesFragment());
//        return null;
//    }

    /**
     * Returns the child fragments that need to show in the view pager
     * @return the fragments
     */
    private List<BaseFragment> getFragments() {
        List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();
        fragmentList.add(new ImageFileFragment());
        fragmentList.add(new VideoFileFragment());
        fragmentList.add(new AudioFileFragment());
        fragmentList.add(new DocumentFilesFragment());
        fragmentList.add(new AllFilesFragment());
        return fragmentList;
    }

    /**
     * Checks the permission after the permission request completed and if completed loads the screen
     * @param permission the permission
     */
    private void loadScreenIfPermissionGranted(String permission) {
        if(isPermissionGrantedFor(permission)) {
            setPagerAdapter();
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
    public void onTabSelected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onFileSelected(AppFiles appFiles, boolean isSelected) {
        if(isSelected) {
            selectedFiles.put(appFiles.getId(), appFiles);
        } else {
            selectedFiles.remove(appFiles.getId());
        }
    }

    @Override
    public boolean isFileSelected(String id) {
        return selectedFiles.containsKey(id);
    }
}
