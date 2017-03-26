package com.project.locateme;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.BindView;

/**
 * @author Andrew
 * @since 16/12/2016
 * @version 1.0
 */


public class ImagePickerActivity extends AppCompatActivity {
    @BindView(R.id.activity_image_picker_exist_image)
    TextView existImage;
    String imagePath;

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
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i , 1);
            }
        });
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
            else{
                Log.i("error" , "request code");
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
