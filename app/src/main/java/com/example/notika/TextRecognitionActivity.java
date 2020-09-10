package com.example.notika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notika.services.NotesService;
import com.example.notika.services.ServiceBuilder;
import com.example.notika.services.TokenRenewInterceptor;
import com.example.notika.services.models.Notes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TextRecognitionActivity extends AppCompatActivity {
    private Button btnSnap,btnFetch,btnNote;
    private TextView mRecText;
    private ImageView imgCapture;
    private String category="Personal", title, body;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_recognition);
        btnSnap=findViewById(R.id.btn_snap);
        btnFetch=findViewById(R.id.btn_fetch);
        btnNote=findViewById(R.id.btn_note);
        mRecText=findViewById(R.id.tv_RecogText);
        imgCapture=findViewById(R.id.img_capture);
        btnNote.setVisibility(View.GONE);

        toolbar = findViewById(R.id.include);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Image to Text");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnSnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnNote.setVisibility(View.GONE);
               dispatchTakePictureIntent();
               mRecText.setText("");
            }
        });
        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecText.setText("Scannning...");
              detectTextFromImage();
                btnNote.setVisibility(View.VISIBLE);
            }
        });

        btnNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Long tsLong= System.currentTimeMillis()/1000;
                String ts=tsLong.toString();

                String token = TokenRenewInterceptor.getToken(getApplicationContext());
                title = ts;
                body = mRecText.getText().toString();
                NotesService notesService = ServiceBuilder.buildService(NotesService.class);
                Call<Notes> addNoteCall = notesService.addNote(String.format("Bearer %s", token), title, body, category);

                addNoteCall.enqueue(new Callback<Notes>() {
                    @Override
                    public void onResponse(@NotNull Call<Notes> call, @NotNull Response<Notes> response) {
                        if (response.isSuccessful()){
                            startActivity(new Intent(TextRecognitionActivity.this, MainActivity.class));
                            Toast.makeText(getApplicationContext(), "Your note has been added", Toast.LENGTH_SHORT).show();
                            // Log.d("ADDED NOTE", response.body().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<Notes> call, Throwable t) {
                        if (t instanceof IOException){
                            Toast.makeText(getApplicationContext(), "Check your internet connectivity", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        }





    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }
public Bitmap imageBitmap=null;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imgCapture.setImageBitmap(imageBitmap);
        }
    }

    //Detect Image
    private void detectTextFromImage() {
        InputImage inputImage=InputImage.fromBitmap(imageBitmap,0);
        TextRecognizer recognizer = TextRecognition.getClient();
        recognizer.process(inputImage).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(Text text) {
displayTextFromImage(text);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TextRecognitionActivity.this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Error: ",e.getMessage());
            }
        });
    }

    private void displayTextFromImage(Text text) {
        String resultText = text.getText();
        String finalText ="";
        for (Text.TextBlock block : text.getTextBlocks()) {
            String blockText = block.getText();
            finalText = finalText + blockText;

            Point[] blockCornerPoints = block.getCornerPoints();
            Rect blockFrame = block.getBoundingBox();
            for (Text.Line line : block.getLines()) {
                String lineText = line.getText();
                Point[] lineCornerPoints = line.getCornerPoints();
                Rect lineFrame = line.getBoundingBox();
                for (Text.Element element : line.getElements()) {
                    String elementText = element.getText();
                    Point[] elementCornerPoints = element.getCornerPoints();
                    Rect elementFrame = element.getBoundingBox();
                }
            }
        }
        mRecText.setText(finalText);
    }

}