package com.example.lahirufernando.skinsensor;
/**
 * Created by lahirufernando on 30/11/2017.
 */
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomTransparentCamera extends AppCompatActivity {
    private static final String TAG = "Image Capture";
    private static String bodyPart;
    private ImageSurfaceView mImageSurfaceView;
    private android.hardware.Camera camera;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private FrameLayout cameraPreviewLayout;


    ImageView image;

    /**
     * set transparency to 50%, can be changed whlist taking a picture
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_camera);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        bodyPart = getIntent().getStringExtra("part");
        SeekBar imageTransparency = new SeekBar(this);
        imageTransparency.setMax(100);// Maximum value

        imageTransparency.setProgress(50);// Middle starting value
        cameraPreviewLayout = (FrameLayout) findViewById(R.id.camera_preview);

        camera = checkDeviceCamera();
        mImageSurfaceView = new ImageSurfaceView(CustomTransparentCamera.this, camera);
        cameraPreviewLayout.addView(mImageSurfaceView);
        image = new ImageView(CustomTransparentCamera.this);

        // Implementation of Picasso library.
        Picasso.with(CustomTransparentCamera.this)
                .load(Uri.parse(getIntent().getStringExtra("uri"))).fit()
                .centerInside()
                .into(image);
        image.setAlpha(0.5f);
        cameraPreviewLayout.addView(image);
        cameraPreviewLayout.addView(imageTransparency);

        imageTransparency.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                image.setAlpha((float) progress / 100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //takes picture
        Button captureButton = (Button) findViewById(R.id.button);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, pictureCallback);
            }
        });
    }
    //
    private android.hardware.Camera checkDeviceCamera() {
        android.hardware.Camera mCamera = null;
        try {
            mCamera = android.hardware.Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mCamera;
    }

    android.hardware.Camera.PictureCallback pictureCallback = new android.hardware.Camera.PictureCallback() {

        public Uri getImageUri(Context inContext, Bitmap inImage) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
            return Uri.parse(path);
        }

        @Override
        public void onPictureTaken(byte[] data, android.hardware.Camera camera) {
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null) {
                Log.d(TAG, "Error creating media file, check storage permissions: "
                );
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                Intent intent = new Intent();
                intent.putExtra("path", pictureFile.toString());
                setResult(RESULT_OK, intent);
                finish();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }

        }
    };

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.


        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), bodyPart);
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            //File destFile = new File(imagesFolder, "bodypart_" + timeStamp + ".png");

            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }

        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();// if you are using MediaRecorder, release it first
        //camera.setPreviewCallback(null);
        cameraPreviewLayout.removeView(mImageSurfaceView);
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.release();        // release the camera for other applications
            camera = null;
        }
    }

}
