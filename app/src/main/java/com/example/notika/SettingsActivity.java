package com.example.notika;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    Switch switch_darkmode;
    Button mlogout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        switch_darkmode=findViewById(R.id.switch1);
        mlogout=findViewById(R.id.btn_logout);
        mlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SettingsActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        switch_darkmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switch_darkmode.isChecked()){
                   AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


                }
            }
        });
        getSystemTheme();
    }
    public void  getSystemTheme(){
        int currentNightModeStatus=getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightModeStatus){
            case Configuration.UI_MODE_NIGHT_NO:
                Toast.makeText(getApplicationContext(), "Dark mode is not active", Toast.LENGTH_SHORT).show();
                switch_darkmode.setChecked(false);
                break;

            case Configuration.UI_MODE_NIGHT_YES:
                Toast.makeText(getApplicationContext(), "Welcome to the Darkside", Toast.LENGTH_SHORT).show();
                switch_darkmode.setChecked(true);
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                Toast.makeText(getApplicationContext(), "Welcome to the Darkside", Toast.LENGTH_SHORT).show();
                switch_darkmode.setChecked(true);
                break;

            case Configuration.UI_MODE_NIGHT_MASK:
                Toast.makeText(getApplicationContext(), "Welcome to the Darkside", Toast.LENGTH_SHORT).show();
                switch_darkmode.setChecked(true);
                break;

        }
    }
}