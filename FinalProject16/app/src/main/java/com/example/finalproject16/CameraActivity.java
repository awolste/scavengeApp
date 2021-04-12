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
    private Vision vision;
    private TextView labelView;

    // For adding brightness
    private int mMultColor = 0xffffffff;
    private int mAddColor = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);

        mPhotoImageView = findViewById(R.id.photo);

        mSaveButton = findViewById(R.id.saveButton);
        mSaveButton.setEnabled(false);

        labelView = findViewById(R.id.myTextView);

        Vision.Builder visionBuilder = new Vision.Builder(
                new NetHttpTransport(),
                new AndroidJsonFactory(),
                null);

        visionBuilder.setVisionRequestInitializer(
                new VisionRequestInitializer("AIzaSyD9NrId_A2zpvpUZBlkE-lxnWXo4zQcoco"));

        vision = visionBuilder.build();

    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            displayPhoto();

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

                        // Count faces
                        int num = labels.size();
                        String descriptions = "You Scanned a ";
                        for(int i=0; i<5; i++) {
                            if (i == 4 ) {
                                descriptions += " and a " + labels.get(i).getDescription();
                            }
                            else {
                                descriptions += ", " + labels.get(i).getDescription();
                            }
                        }
                        descriptions += ".";


                        Log.i("RESULT", labels + "");

                        String finalDescriptions = descriptions;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                labelView.setText(finalDescriptions);
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // More code here
                }
            });

            mSaveButton.setEnabled(true);
        }
    }

    private File createImageFile() throws IOException {
        // Create a unique image filename
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFilename = "photo_" + timeStamp + ".jpg";

        // Get file path where the app can save a private image
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(storageDir, imageFilename);
    }

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

    public void savePhotoClick(View view) {
        // add points and mark task as completed
        // redirect to leaderboard
    }

}