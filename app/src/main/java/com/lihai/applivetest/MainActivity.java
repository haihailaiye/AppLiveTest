package com.lihai.applivetest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lihai.applivetest.service.LocalService;
import com.lihai.applivetest.service.RemoteService;
import com.lihai.applivetest.service.RestartService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(getApplicationContext(),LocalService.class));
        startService(new Intent(getApplicationContext(),RemoteService.class));
        startService(new Intent(getApplicationContext(),RestartService.class));
    }
}
