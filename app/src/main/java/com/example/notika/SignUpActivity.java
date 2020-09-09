package com.example.notika;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notika.services.NotesService;
import com.example.notika.services.ServiceBuilder;
import com.example.notika.services.TokenRenewInterceptor;
import com.example.notika.services.models.ApiResponse;
import com.example.notika.services.models.Token;
import com.example.notika.services.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText mUserName;
    private EditText mEmail;
    private EditText mPassword; private EditText mConfirmPassword;
    private Button signUpButton;
    private TextView mErrorMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        //for the form
        mUserName = findViewById(R.id.username_editText);
        mEmail = findViewById(R.id.email_editText);
        mPassword = findViewById(R.id.password_signup_editText);
        mConfirmPassword = findViewById(R.id.conpassword_signup_editText);
        signUpButton = findViewById(R.id.button_signup);
        mErrorMessage = findViewById(R.id.error_message_signup);



        Log.d("Username",mUserName.getText().toString());

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User newUser = new User(mUserName.getText().toString(), mEmail.getText().toString(), mPassword.getText().toString(), mConfirmPassword.getText().toString());
            //    Log.d("Username",mUserName.getText().toString());

                NotesService notesService = ServiceBuilder.buildService(NotesService.class);
                Call<ApiResponse> call = notesService.signup(newUser);

                call.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {


                            if(response.code()==201){

                                Intent i  = new Intent(SignUpActivity.this, UploadImageActivity.class);
                                TokenRenewInterceptor.saveToken(response.body().getResponse(), getApplicationContext());
                                Log.d("Token", response.body().getResponse());
                                startActivity(i);
                                finish();
                            }else if(response.code()==400){
                                mErrorMessage.setText(R.string.check_input);
                                Log.d("MYRESPONSES", "Here");
                                mPassword.setText("");
                            }
                            else{
                                mErrorMessage.setText(R.string.wrong);
                                Log.d("MYRESPONSES", "Here");
                                mPassword.setText("");
                            }
                        }


                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Log.d("SignError" , t.getMessage());

                    }
                });


            }
        });




    }

    public void launchLogin(View view) {
        Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(i);
    }
}