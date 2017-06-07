package com.multi.fileselector;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

/**
 * Created by Vipin on 6/2/2017.
 */
public class ChooserActivity extends AppCompatActivity {
	private static final int REQUEST_FOR_STORAGE_PERMISSION = 123;
	/**
	 * To select the image
	 */
	private Button btnSelectImage;
	/**
	 * To select the document
	 */
	private Button btnSelectDocument;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options_chooser);
		initialize();
		register();
	}

	/**
	 * Initializes the views
	 */
	private void initialize() {
		btnSelectImage = (Button) findViewById(R.id.select_image);
		btnSelectDocument = (Button) findViewById(R.id.select_documents);
	}

	/**
	 * Initializes the views
	 */
	private void register() {
		btnSelectImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mayRequestExternalStorage()) {
					return;
				}
				navigateTo(MultiPhotoSelectActivity.class);
			}
		});
		btnSelectDocument.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mayRequestExternalStorage()) {
					return;
				}
				navigateTo(CategorizedMultiFileSelectActivity.class);
			}
		});
	}


	private void navigateTo(Class classToNavigate) {
		Intent intent = new Intent(ChooserActivity.this, classToNavigate);
		startActivity(intent);
	}

	private void showPermissionRationaleSnackBar() {
		Snackbar.make(btnSelectImage, getString(R.string.permission_rationale),
				Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Request the permission
				ActivityCompat.requestPermissions(ChooserActivity.this,
						new String[]{READ_EXTERNAL_STORAGE},
						REQUEST_FOR_STORAGE_PERMISSION);
			}
		}).show();

	}

	private boolean mayRequestExternalStorage() {

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			return true;
		}

		if (checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
			return true;
		}

		if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
			//promptStoragePermission();
			showPermissionRationaleSnackBar();
		} else {
			requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, REQUEST_FOR_STORAGE_PERMISSION);
		}

		return false;
	}

	/**
	 * Callback received when a permissions request has been completed.
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
										   @NonNull int[] grantResults) {

		switch (requestCode) {

			case REQUEST_FOR_STORAGE_PERMISSION: {

				if (grantResults.length > 0) {
					if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
						navigateTo(CategorizedMultiFileSelectActivity.class);
					} else {
						if (ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
							showPermissionRationaleSnackBar();
						} else {
							Toast.makeText(this, "Go to settings and enable permission", Toast.LENGTH_LONG).show();
						}
					}
				}

				break;
			}
		}
	}
}