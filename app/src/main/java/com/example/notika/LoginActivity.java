package com.example.notika;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notika.services.NotesService;
import com.example.notika.services.ServiceBuilder;
import com.example.notika.services.TokenRenewInterceptor;
import com.example.notika.services.models.Token;
import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private Button mLogin;
    private TextView mErrorMessage;
    private Toolbar toolbar;
    private ProgressBar progressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail= findViewById(R.id.login_editText_email);
        mPassword = findViewById(R.id.login_editText_password);
        mLogin = findViewById(R.id.button_login);
        mErrorMessage = findViewById(R.id.error_message_login);
        toolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);






        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                NotesService notesService = ServiceBuilder.buildService(NotesService.class);
                Call<Token> loginRequest = notesService.login(mEmail.getText().toString(), mPassword.getText().toString());

                loginRequest.enqueue(new Callback<Token>() {
                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (response.isSuccessful()){

                            Intent i  = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);

                            TokenRenewInterceptor.saveToken(response.body().getToken(), getApplicationContext());

                            String  token = TokenRenewInterceptor.getToken(getApplicationContext());



                        }else if(response.code() == 400) {
                            mErrorMessage.setText(R.string.wrong_credentials);
                            mPassword.setText("");
                        }else if (response.code() == 401){
                            mErrorMessage.setText(R.string.already_in_use);
                            mPassword.setText("");
                        }else {
                            mErrorMessage.setText(R.string.something_went_wrong);
                            mPassword.setText("");
                        }


                    }


                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {
                        Log.e("Bad", t.getMessage());
                        Toast.makeText(getApplicationContext(), "Failed to login", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    public void launchSignUp(View view) {
        Intent i  = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(i);
    }
}