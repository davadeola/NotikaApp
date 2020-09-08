package com.example.notika;

import androidx.annotation.FontRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    Switch switch_darkmode;
    Button mlogout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        switch_darkmode=findViewById(R.id.switch1);
        mlogout=findViewById(R.id.btn_logout);
        Spinner spinner = findViewById(R.id.spinner2);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Select");
        arrayList.add("Default");
        arrayList.add("Montserrat");
        arrayList.add("Dancing Script");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String font = parent.getItemAtPosition(position).toString();
                switch (font){
                    case "Default":
                        setTheme(R.style.AppThemeSyne);
                        break;
                    case "Montserrat":
                        setTheme(R.style.AppThemeMontesrat);
                        break;
                    case "Dancing Script":
                        setTheme(R.style.AppThemeDancing);
                        break;

                }

                
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
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