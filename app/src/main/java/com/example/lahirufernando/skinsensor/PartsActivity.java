package com.example.lahirufernando.skinsensor;
/**
 * Created by lahirufernando on 30/11/2017.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lahirufernando.skinsensor.Adapters.ImageAdapter;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PartsActivity extends AppCompatActivity implements CropImageView.OnCropImageCompleteListener {

    //private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 6;
    private static final int GALLERY_PERMISSION_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    private static final int CAMERA_PERMISSIONS_REQUEST = 2;
    private static final int CAMERA_IMAGE_REQUEST = 3;

    private ImageView preview = null;
    private Button removeBtn = null;
    private Button finsihBtn = null;


    String bubbleName;
    //TODO: Storage Permissions From 23+ its compulsory
    //
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };
    File image;
    Uri imageUri = null;
    ArrayList<String> f = new ArrayList<String>();// list of file paths
    File[] listFile;
    GridView imagegrid;
    private ImageAdapter imageAdapter;
    static final int IMAGE_GRID = 1;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parts);

        db = new DatabaseHandler(this);


        FloatingActionButton homeButton = (FloatingActionButton) findViewById(R.id.homeButton);
        FloatingActionButton rotateButton = (FloatingActionButton) findViewById(R.id.rotateButton);
        FloatingActionButton camera = (FloatingActionButton) findViewById(R.id.camerabtn);
        FloatingActionButton cam2 = (FloatingActionButton) findViewById(R.id.transparentCam);
        cam2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    File fdelete = new File(imageUri.getPath());

                    if (fdelete.exists()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PartsActivity.this);
                        builder.setTitle("Confirm");
                        builder.setMessage("Are you sure you want to use this image as a Reference Image?");

                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(PartsActivity.this, CustomTransparentCamera.class);
                                intent.putExtra("uri", imageUri.toString());
                                intent.putExtra("part", bubbleName);
                                startActivityForResult(intent, 7);
                                dialog.dismiss();
                            }
                        });

                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();

                    }
                } else {
                    Toast.makeText(PartsActivity.this, "Select an image first.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        finsihBtn = (Button) findViewById(R.id.finishedit);
        removeBtn = (Button) findViewById(R.id.remove);

        verifyStoragePermissions(this);
        preview = (ImageView) findViewById(R.id.preview);
        TextView partNameView = (TextView) findViewById(R.id.partname);
        TextView partDescription = (TextView) findViewById(R.id.partdescription);
        Intent intent = getIntent();

        bubbleName = intent.getStringExtra("bodypart");
        bubbleName = bubbleName.toLowerCase();
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File fdelete = new File(imageUri.getPath());
                if (fdelete.exists()) {
                    if (fdelete.delete()) {
                        getPhotosList(bubbleName);
                        imagegrid.setAdapter(imageAdapter);
                        imagegrid.deferNotifyDataSetChanged();
                        imagegrid.setVisibility(View.VISIBLE);
                        preview.setVisibility(View.GONE);
                        finsihBtn.setVisibility(View.GONE);
                        removeBtn.setVisibility(View.GONE);
                        Toast.makeText(PartsActivity.this, "Image Deleted Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        imagegrid = (GridView) findViewById(R.id.PhoneImageGrid);
        imageAdapter = new ImageAdapter(this, f);
        imagegrid.setAdapter(imageAdapter);

        imagegrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final int pos = position;
                switch (view.getId()) {
                    case R.id.grid_layout:
                        imagegrid.setVisibility(View.GONE);
                        preview.setVisibility(View.VISIBLE);
                        //Toast.makeText(PartsActivity.this, "Testingggg", Toast.LENGTH_SHORT).show();
                        //preview.invalidate();
                        preview.refreshDrawableState();
                        finsihBtn.setVisibility(View.VISIBLE);
                        removeBtn.setVisibility(View.VISIBLE);
                        imageUri = Uri.parse("file://" + f.get(pos));
                        Log.d("Path Grid", imageUri.toString());
                        Picasso.with(getBaseContext()).load("file://" + f.get(pos)).into(preview);
                        break;

                }

            }
        });
        getPhotosList(bubbleName);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PartsActivity.this, HomePage.class);
                startActivity(intent);
            }
        });
        finsihBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                getPhotosList(bubbleName);
                imagegrid.setAdapter(imageAdapter);
                imagegrid.deferNotifyDataSetChanged();
                imagegrid.setVisibility(View.VISIBLE);
                preview.setVisibility(View.GONE);
                finsihBtn.setVisibility(View.GONE);
                removeBtn.setVisibility(View.GONE);


            }
        });
        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    startCropImageActivity(imageUri);
                }
            }

        });

        switch (bubbleName) {
            case "ear":
                partNameView.setText("Ear");
                partDescription.setText("Ear description");
                break;

            case "head":
                partNameView.setText("Head");
                partDescription.setText("Head description");
                break;

            case "forehead":
                partNameView.setText("Forehead");
                partDescription.setText("Forehead description");
                break;

            case "eye":
                partNameView.setText("Eye");
                partDescription.setText("Eye description");
                break;

            case "mouth":
                partNameView.setText("Mouth");
                partDescription.setText("Mouth description");
                break;

            case "cheek":
                partNameView.setText("Cheek");
                partDescription.setText("Cheek description");
                break;

            case "chin":
                partNameView.setText("Chin");
                partDescription.setText("Chin description");
                break;

            case "neck":
                partNameView.setText("Neck");
                partDescription.setText("Neck description");
                break;

            case "shoulder":
                partNameView.setText("Shoulder");
                partDescription.setText("Shoulder description");
                break;

            case "elbow":
                partNameView.setText("Elbow");
                partDescription.setText("Elbow description");
                break;

            case "arm":
                partNameView.setText("Arm");
                partDescription.setText("Arm description");
                break;

            case "forearm":
                partNameView.setText("Forearm");
                partDescription.setText("Forearm description");
                break;

            case "wrist":
                partNameView.setText("Wrist");
                partDescription.setText("Wrist description");
                break;

            case "hand":
                partNameView.setText("Hand");
                partDescription.setText("Hand description");
                break;

            case "fingers":
                partNameView.setText("Fingers");
                partDescription.setText("Fingers description");
                break;

            case "chest":
                partNameView.setText("Chest");
                partDescription.setText("Chest description");
                break;

            case "nose":
                partNameView.setText("Nose");
                partDescription.setText("Nose description");
                break;

            case "groin":
                partNameView.setText("Groin");
                partDescription.setText("Groin description");
                break;

            case "abdomen":
                partNameView.setText("Abdomen");
                partDescription.setText("Abdomen description");
                break;

            case "back":
                partNameView.setText("Back");
                partDescription.setText("Back description");
                break;

            case "bottom":
                partNameView.setText("Bottom");
                partDescription.setText("Bottom description");
                break;

            case "thigh":
                partNameView.setText("Thigh");
                partDescription.setText("Thigh description");
                break;

            case "knee":
                partNameView.setText("Knee");
                partDescription.setText("Knee description");
                break;

            case "calf":
                partNameView.setText("Calf");
                partDescription.setText("Calf description");
                break;

            case "leg":
                partNameView.setText("Leg");
                partDescription.setText("Leg description");
                break;

            case "ankle":
                partNameView.setText("Ankle");
                partDescription.setText("Ankle description");
                break;

            case "toes":
                partNameView.setText("Toes");
                partDescription.setText("Toes description");
                break;

            case "foot":
                partNameView.setText("Foot");
                partDescription.setText("Foot description");
                break;

            default:
                partNameView.setText("Undefined");
                partDescription.setText("Undefined description");
        }
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PartsActivity.this);
                builder
                        .setMessage("Camera")
                        .setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startGallery();
                            }
                        })
                        .setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startCamera();
                            }
                        });
                builder.create().show();

            }
        });


    }

    //TODO: Check if we have write permission
    public static void verifyStoragePermissions(Activity activity) {

        int permission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED || cameraPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST:
                if (Permissions.permissionGranted(requestCode, CAMERA_PERMISSIONS_REQUEST, grantResults)) {
                    startCamera();
                }
                break;
            case GALLERY_PERMISSION_REQUEST:
                if (Permissions.permissionGranted(requestCode, GALLERY_PERMISSION_REQUEST, grantResults)) {
                    startGallery();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7) {
            if (resultCode == RESULT_OK) {
                imageUri = Uri.parse("file://" + data.getStringExtra("path"));
                storeMetaData(imageUri.toString());

                preview.refreshDrawableState();
                Picasso.with(getBaseContext()).load(imageUri).into(preview);
                preview.refreshDrawableState();
            }
        } else if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            imageUri = data.getData();
            //Images can be taken but not imported.
            File imagesFolder = new File(Environment.getExternalStorageDirectory(), bubbleName);
            imagesFolder.mkdirs();

            File destFile = new File(imagesFolder, "bodypart_" + timeStamp + ".png");
            storeMetaData("file://" + destFile.getPath().toString());
            try {
                copyFile(new File(getPath(imageUri)), destFile);
                imagegrid.setVisibility(View.GONE);
                preview.setVisibility(View.VISIBLE);
                finsihBtn.setVisibility(View.VISIBLE);
                removeBtn.setVisibility(View.VISIBLE);
                Picasso.with(this).load(imageUri).into(preview);

                getPhotosList(bubbleName);
//              getContentResolver().delete(imageUri, null, null);
                imageAdapter.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {

            Uri fileProvider = FileProvider.getUriForFile(PartsActivity.this, "com.example.lahirufernando.skinsensor.provider", image);
            imageUri = fileProvider;
            imagegrid.setVisibility(View.GONE);
            preview.setVisibility(View.VISIBLE);
            finsihBtn.setVisibility(View.VISIBLE);
            removeBtn.setVisibility(View.VISIBLE);
            storeMetaData("file://" + image.getPath().toString());
            Picasso.with(this).load(fileProvider).into(preview);
            getPhotosList(bubbleName);
            //imageAdapter.notifyDataSetChanged();
        }

        // handle result of CropImageActivity
        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Log.d("Crop", result.getUri().toString());
                File fdelete = new File(imageUri.getPath());
                if (fdelete.exists()) {
                    if (fdelete.delete()) {
                        Log.d("file Deleted :", result.getUri().getPath().toString());
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        //Images can be imported and taken and can be viewed from user's device.
                        File imagesFolder = new File(Environment.getExternalStorageDirectory(), bubbleName);
                        imagesFolder.mkdirs();
                        File destFile = new File(imagesFolder, "bodypart_" + timeStamp + ".png");

                        imageUri = result.getUri();
                        storeMetaData("file://" + destFile.getPath().toString());
                        try {
                            copyFile(new File(imageUri.getPath()), destFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Picasso.with(this).load(destFile).into(preview);

                        //imageAdapter.notifyDataSetChanged();
                    } else {
                        System.out.println("file not Deleted :" + result.getUri().getPath());
                        //Picasso.with(this).load(result.getUri()).noFade().resize(preview.getWidth(), preview.getHeight()).centerCrop().into(preview);
                    }
                    getPhotosList(bubbleName);
                }

                //(findViewById(R.id.preview)).setImageURI();
                Toast.makeText(this, "Cropping successful, : " + result.getSampleSize(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
        }
    }

    public void startCamera() {
        //camera stuff
        Intent imageIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        //imageIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        //Picture would be taken will be shown in User's device but not in app.
        File imagesFolder = new File(Environment.getExternalStorageDirectory() + "/.myfolder", bubbleName);

        imagesFolder.mkdirs();

        image = new File(imagesFolder, "bodypart_" + timeStamp + "png");
        Uri uriSavedImage = FileProvider.getUriForFile(PartsActivity.this, "com.example.lahirufernando.skinsensor.provider", image);
        // Uri uriSavedImage = Uri.fromFile(image);
        Log.d("URI", image.toString());

        if (imagesFolder.exists()) {
            ExifInterface exifInterface = null;
            try {
                exifInterface = new ExifInterface(bubbleName);
                if (exifInterface != null) {
                    String date = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
                    //Log.d("Dated: " + date.toString());
                }

            } catch (Exception e) {

            }
        }

        imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
        startActivityForResult(imageIntent, CAMERA_IMAGE_REQUEST);
    }

    public void startGallery() {
        try {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI); //Intent to Call the Camera
            startActivityForResult(Intent.createChooser(i, "Select Image Please"), GALLERY_IMAGE_REQUEST);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No Content Found in the Gallery.", Toast.LENGTH_SHORT).show();
        }
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

            getPhotosList(bubbleName);
            imagegrid.deferNotifyDataSetChanged();


        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }

    }


    public void getPhotosList(String folderName) {
        f.clear();
        //can take pictures but doesn't save in app nor in device.
        File file = new File(Environment.getExternalStorageDirectory(), folderName);

        if (file.isDirectory()) {
            listFile = file.listFiles();

            for (int i = 0; i < listFile.length; i++) {
                f.add(listFile[i].getAbsolutePath());
                Log.d("path", f.get(i).toString());
            }
        }
        //imageAdapter.notifyDataSetChanged();
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getPhotosList(bubbleName);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPhotosList(bubbleName);
    }

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {

    }

    private void storeMetaData(String uri) {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        db.addMetaData(new MetaData(formattedDate, "", uri, ""));
        Log.d("Pathhhh", db.getMetaData(uri).toString());
    }
}
