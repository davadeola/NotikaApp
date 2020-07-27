package com.example.notika;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.notika.services.NotesService;
import com.example.notika.services.ServiceBuilder;
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



        Log.d("Username",mUserName.getText().toString());

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User newUser = new User(mUserName.getText().toString(), mEmail.getText().toString(), mPassword.getText().toString(), mConfirmPassword.getText().toString());
                Log.d("Username",mUserName.getText().toString());

                NotesService notesService = ServiceBuilder.buildService(NotesService.class);
                Call<Token> call = notesService.signup(newUser);

                call.enqueue(new Callback<Token>() {
                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {
                        if (response.isSuccessful()){

                            Intent i  = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(i);
                                Log.d("Token", response.body().getToken());

                        }
                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {
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