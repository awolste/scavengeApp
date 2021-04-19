/**
 * Andrew Wolstenholme (awolste) C11132861
 * Nick Provost (nprovos) C13162083
 * CameraActivity.java
 *
 * Sources: Zybooks 9
 * */
package com.example.finalproject16;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.android.volley.VolleyError;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CameraActivity extends AppCompatActivity {

    private final int REQUEST_TAKE_PHOTO = 1;

    private File mPhotoFile;

    private ImageView mPhotoImageView;
    private Button mSaveButton;
    private Button mCancelButton;
    private Vision vision;
    private TextView labelView;
    private double taskLat;
    private double taskLong;
    private double userLat;
    private double userLong;
    private boolean completed;

    private InfoFetcher mInfoFetcher;

    // For adding brightness
    private int mMultColor = 0xffffffff;
    private int mAddColor = 0;

    /**
     * @pre an intent has been started with the target activity as CameraActivity
     * @post activity is set to CameraActivity
     *
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);

        completed = false;

        mPhotoImageView = findViewById(R.id.photo);

        mSaveButton = findViewById(R.id.saveButton);
        mSaveButton.setEnabled(false);

        mCancelButton = findViewById(R.id.cancel);
        mCancelButton.setOnClickListener(mButtonClickListener);

        Intent intent = getIntent();
        String barName = intent.getStringExtra("BARNAME");
        String task = intent.getStringExtra("TASK");
        TextView barNameText = findViewById(R.id.barNameText);
        barNameText.setText(barName);
        TextView barTask = findViewById(R.id.barTask);
        barTask.setText(task);

        taskLat = intent.getDoubleExtra("TASKLAT", 0);
        taskLong = intent.getDoubleExtra("TASKLONG", 0);
        userLat = intent.getDoubleExtra("USERLAT", 0);
        userLong = intent.getDoubleExtra("USERLONG", 0);

        labelView = findViewById(R.id.myTextView);

        Vision.Builder visionBuilder = new Vision.Builder(
                new NetHttpTransport(),
                new AndroidJsonFactory(),
                null);

        visionBuilder.setVisionRequestInitializer(
                new VisionRequestInitializer("AIzaSyD9NrId_A2zpvpUZBlkE-lxnWXo4zQcoco"));

        vision = visionBuilder.build();

    }

    /**
     * @pre the CameraActivity has been started
     * @post a photo is taken
     *
     * */
    public void takePhotoClick(View view) {
        // Create implicit intent
        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (photoCaptureIntent.resolveActivity(getPackageManager()) != null) {

            // Create the File where the photo should go
            try {
                mPhotoFile = createImageFile();
            }
            catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }

            // If the File was successfully created, start camera app
            if (mPhotoFile != null) {

                // Create content URI to grant camera app write permission to photoFile
                Uri photoUri = FileProvider.getUriForFile(this,
                        "com.example.finalproject16.fileprovider",
                        mPhotoFile);

                // Add content URI to intent
                photoCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                // Start camera app
                startActivityForResult(photoCaptureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    /**
     * @pre the CameraActivity has returned
     * @post the photo is displayed and the labels are detected
     *
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            displayPhoto();
            labelView.setText("Scanning Item");

            //String url = data.getStringExtra(MediaStore.EXTRA_OUTPUT);
            //Log.i("URL", url);
            //mInfoFetcher.fetchSubjects(mFetchListener, url);

            // Create new thread
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        byte[] bytes = Files.readAllBytes(mPhotoFile.toPath());
                        Image inputImage = new Image();
                        inputImage.encodeContent(bytes);

                        Feature desiredFeature = new Feature();
                        desiredFeature.setType("LABEL_DETECTION");

                        AnnotateImageRequest request = new AnnotateImageRequest();
                        request.setImage(inputImage);
                        request.setFeatures(Arrays.asList(desiredFeature));

                        BatchAnnotateImagesRequest batchRequest =
                                new BatchAnnotateImagesRequest();

                        batchRequest.setRequests(Arrays.asList(request));

                        BatchAnnotateImagesResponse batchResponse =
                                vision.images().annotate(batchRequest).execute();

                        List<EntityAnnotation> labels = batchResponse.getResponses()
                                .get(0).getLabelAnnotations();
                        Log.i("LABELS", labels+"");

                        // Count labels
                        for(int i=0; i<labels.size(); i++) {
                            if (labels.get(i).getDescription().contains("Food") || labels.get(i).getDescription().contains("Drink")) {
                                completed = true;
                                Log.i("SEARCHINGLABELS", "True");
                            }
                        }


                        Log.i("RESULT", labels + "");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                labelView.setText("Item Scanned\nComplete for Points");
                                labelView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            mSaveButton.setEnabled(true);
            mSaveButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @pre a photo has been taken
     * @post an image file has been created
     *
     * */
    private File createImageFile() throws IOException {
        // Create a unique image filename
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFilename = "photo_" + timeStamp + ".jpg";

        // Get file path where the app can save a private image
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(storageDir, imageFilename);
    }

    /**
     * @pre an image has been taken
     * @post the image is displayed on the screen
     *
     * */
    private void displayPhoto() {
        // Get ImageView dimensions
        int targetW = mPhotoImageView.getWidth();
        int targetH = mPhotoImageView.getHeight();

        // Get bitmap dimensions
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath(), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a smaller bitmap that fills the ImageView
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath(), bmOptions);

        // Display smaller bitmap
        mPhotoImageView.setImageBitmap(bitmap);
    }

    /**
     * @pre a photo has been taken
     * @post the photo is checked for item recognition
     *
     * */
    public void savePhotoClick(View view) {
        // add points and mark task as completed
        // redirect to leaderboard
        //for now we will not check what the item is as the recognition is not perfect
        // we check location to ensure the person is where the task is
        Log.i("CHECKINGLOC", ""+ taskLat + ", "+taskLong+ " vs " + userLat+ ", "+userLong);
        if (Math.abs(taskLat - userLat) > .001 || Math.abs(taskLong - userLong) > .001){
            Toast.makeText(CameraActivity.this, "Not in Range of Task", Toast.LENGTH_LONG).show();
        }
        else {
            if (completed){
                labelView.setText("Item Found!");
                //Toast.makeText(CameraActivity.this, "Completed Task!", Toast.LENGTH_LONG).show();
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

                if (acct != null) {

                    mInfoFetcher = new InfoFetcher(this);

                    try {
                        Log.i("SAVING", "saving points for " + acct.getEmail());
                        mInfoFetcher.updateUser(mFetchListener, acct.getEmail());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //Toast.makeText(CameraActivity.this, "Completed Task!", Toast.LENGTH_LONG).show();
                //startActivity(new Intent(CameraActivity.this, Leaderboard.class));
            }
            else {
                labelView.setText("Item Scanned Does Not Match Task\nTry Again or Try Different Task");
                labelView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
        }
    }

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        /**
         * onClick is called when the learn more button is pressed
         * this function changes the activity page to the bio page
         * @pre activity is set to camera activity
         * @post activity is set to maps page
         *
         * */
        public void onClick(View v) {
            startActivity(new Intent(CameraActivity.this, MapsActivity.class));
        }
    };

    /**
     * @Pre
     *      fetcher is called
     * @Post
     *      data is recieved
     *      leaderboard is loaded
     *
     * Source Zybooks 5.2, 5.3
     * */
    private InfoFetcher.OnDataReceivedListener mFetchListener = new InfoFetcher.OnDataReceivedListener() {

        @Override
        public void onDataReceived(List<String> info) {
            Log.i("UPDATED", "points updated");
            startActivity(new Intent(CameraActivity.this, Leaderboard.class));
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.i("ERROR", "points update error");
        }
    };

}