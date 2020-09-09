package com.example.notika;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.notika.services.FileUtils;
import com.example.notika.services.NotesService;
import com.example.notika.services.ServiceBuilder;
import com.example.notika.services.TokenRenewInterceptor;
import com.example.notika.services.models.ApiResponse;

//import net.gotev.uploadservice.data.UploadNotificationConfig;
//import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.SimpleFormatter;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadImageActivity extends AppCompatActivity {
    public ImageView imgUpload;
    public Button btnUpload;
    private String TAG = "IN UPLOAD";

    private int PICK_IMAGE_REQUEST = 1;

    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath = null;

    //file to be uploaded
    private  File originalFile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        imgUpload = findViewById(R.id.img_uploadProfile);
        btnUpload = findViewById(R.id.btn_upload);


        imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadMultipart(filePath);


            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null){
            filePath = data.getData();
            String imageStringPath = FileUtils.getPath(getApplicationContext(), filePath);

            if (filePath != null && imageStringPath!= null){
                originalFile = new File(imageStringPath);
            }

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgUpload.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public void uploadMultipart(Uri fileUri) {


        String token = TokenRenewInterceptor.getToken(getApplicationContext());



       if (originalFile != null){
           RequestBody filePart = RequestBody.create(MediaType.parse(Objects.requireNonNull(getContentResolver().getType(fileUri))), originalFile );

           MultipartBody.Part file = MultipartBody.Part.createFormData("image", originalFile.getName(), filePart);

           //making api call
           NotesService notesService = ServiceBuilder.buildService(NotesService.class);
           Call<ApiResponse> uploadImage = notesService.upload(String.format("Bearer %s", token), file);

           uploadImage.enqueue(new Callback<ApiResponse>() {
               @Override
               public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                   if (response.isSuccessful()){
                       Toast.makeText(getApplicationContext(), response.body().getResponse(), Toast.LENGTH_SHORT);
                       Log.d(TAG, "onResponse: "+response.body().getResponse());

                       Intent intent=new Intent(UploadImageActivity.this,LoginActivity.class);
                       startActivity(intent);
                       finish();
                   }
               }

               @Override
               public void onFailure(Call<ApiResponse> call, Throwable t) {
                   Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                   Log.e(TAG, "onFailure: ", t);
               }
           });
       }else{
           Toast.makeText(getApplicationContext(), "Please Upload a photo", Toast.LENGTH_SHORT);
       }
    }





}