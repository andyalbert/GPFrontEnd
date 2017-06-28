package com.project.locateme;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.security.Permission;

import butterknife.BindView;

/**
 * @author Andrew, Khaled
 * @since 16/12/2016
 * @version 1.2
 */


public class ImagePickerActivity extends AppCompatActivity {
    @BindView(R.id.activity_image_picker_exist_image)
    TextView existImage;
    private String imagePath;
    private TextView cameraTake;
    private final int STORAGE_REQUEST = 3;
    private final int CAMERA_REQUEST = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        getWindow().setLayout((int)(metrics.widthPixels * 0.6), (int)(metrics.heightPixels * 0.2));
        existImage =(TextView) findViewById(R.id.activity_image_picker_exist_image);
        existImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!permissionGranted(STORAGE_REQUEST))
                    return;
                loadImage();
            }
        });
        cameraTake = (TextView) findViewById(R.id.camera_take);
        cameraTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!permissionGranted(CAMERA_REQUEST))
                    return;
                getPhoto();
            }
        });

    }

    private void getPhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 2);
    }

    private void loadImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i , 1);
    }

    private boolean permissionGranted(int requestCode) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            switch (requestCode){
                case STORAGE_REQUEST:
                    if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_REQUEST);
                    else
                        return true;
                    break;
                case CAMERA_REQUEST:
                    if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST);
                    else
                        return true;

            }
        else
            return true;
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == STORAGE_REQUEST){
            if(!(grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED))
                Toast.makeText(this, "Sorry, you must grant access in order to add custom image", Toast.LENGTH_SHORT).show();
            else
                loadImage();
        } else if(requestCode == CAMERA_REQUEST){
            if(!(grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED))
                Toast.makeText(this, "Sorry, you must grant access in order to add custom image", Toast.LENGTH_SHORT).show();
            else
                getPhoto();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode , resultCode , data);
        Log.e("Here" , "result code:"+resultCode+"requestCode:"+requestCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                //Uri selectedImageUri = data.getData();
                //imagePath = getPath(selectedImageUri);
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                 imagePath = cursor.getString(columnIndex);
                cursor.close();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("path",imagePath);
                returnIntent.putExtra("URIPath" , selectedImage);
                setResult(Activity.RESULT_OK,returnIntent);
                Log.i("PathPicker" , imagePath);
                finish();
            }
            else if(requestCode == 2){
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), photo, "image", null);
                Uri returnPath =  Uri.parse(path);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("path",path);
                returnIntent.putExtra("URIPath" , returnPath);
                setResult(Activity.RESULT_OK,returnIntent);
                Log.i("PathPicker" , imagePath);
                finish();
            }
        }
        else{

                Log.i("error" , "rsult not ok");
        }
    }
    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            return null;
        }
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        // this is our fallback here
        return uri.getPath();
    }




}
