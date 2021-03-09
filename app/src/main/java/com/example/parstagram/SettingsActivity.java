package com.example.parstagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.parstagram.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    public static final String TAG = "SettingsActivity";
//    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 36;

//    private File photoFile;
//    private String photoFileName = "photo.jpg";

//    private Button btnCapturePicture;
//    private ImageView ivProfilePictureImage;
//    private Button btnChangeProfilePic;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

//        btnCapturePicture = findViewById(R.id.btnCapture);
//        btnCapturePicture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                launchCamera();
//            }
//        });
//
//        ivProfilePictureImage = findViewById(R.id.ivProfilePictureImage);
//        btnChangeProfilePic = findViewById(R.id.btnChangeProfilePicture);
//
//        btnChangeProfilePic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i(TAG, "btnSubmit onClick");
//                if(photoFile == null || ivProfilePictureImage.getDrawable() == null) {
//                    Toast.makeText(getApplicationContext(), "Missing image.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                saveProfilePicture(photoFile);
//            }
//        });

        btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "btnLogout onClick");
                setResult(RESULT_OK);
                finish();
            }
        });
    }

//    private void saveProfilePicture(File photoFile) {
//        ParseQuery<ParseUser> query = new ParseQuery<ParseUser>(ParseUser.class);
//
//
//        query.whereEqualTo(User.KEY_ID, ParseUser.getCurrentUser().getObjectId());
//
//        query.findInBackground(new FindCallback<ParseUser>() {
//            @Override
//            public void done(List<ParseUser> usersFound, ParseException e) {
//                if (e != null) {
//                    Log.e(TAG, "Error setting profile picture", e);
//                    Toast.makeText(getApplicationContext(), "Error setting profile picture", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (usersFound.size() != 1) {
//                    Log.e(TAG, "Expected to find 1 user, instead found " + usersFound.size());
//                } else {
//                    ParseUser user = usersFound.get(0);
//                    Log.i(TAG, "Found user: " + user.getUsername());
//                    user.put("profilePicture", photoFile);
//
//                    user.saveInBackground(new SaveCallback() {
//                        @Override
//                        public void done(ParseException e) {
//                            if (e != null) {
//                                Log.e(TAG, "Error saving profile picture", e);
//                                Toast.makeText(getApplicationContext(), "Error saving profile picture", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//
//                            Log.i(TAG, "Profile picture saved successfully");
//                            Toast.makeText(getApplicationContext(), "Profile picture saved!", Toast.LENGTH_SHORT).show();
//                            ivProfilePictureImage.setImageResource(0);
//                        }
//                    });
//                }
//
//
//            }
//        });
//    }
//
//
//    private void launchCamera() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        photoFile = getPhotoFileUri(photoFileName);
//
//        // wrap File object into a content provider
//        // required for API >= 24
//        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
//        Uri fileProvider = FileProvider.getUriForFile(this, "com.codepath.fileprovider", photoFile);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
//
//        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
//        // So as long as the result is not null, it's safe to use the intent.
//        if (intent.resolveActivity(this.getPackageManager()) != null) {
//            // Start the image capture intent to take photo
//            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
//        }
//    }
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                // by this point we have the camera photo on disk
//                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
//                // RESIZE BITMAP, see section below
//                // Load the taken image into a preview
//                ivProfilePictureImage.setImageBitmap(takenImage);
//            } else { // Result was a failure
//                Toast.makeText(getApplicationContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private File getPhotoFileUri(String fileName) {
//        // Get safe storage directory for photos
//        // Use `getExternalFilesDir` on Context to access package-specific directories.
//        // This way, we don't need to request external read/write runtime permissions.
//        File mediaStorageDir = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);
//
//        // Create the storage directory if it does not exist
//        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
//            Log.d(TAG, "failed to create directory");
//        }
//
//        // Return the file target for the photo based on filename
//        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
//
//        return file;
//    }


}