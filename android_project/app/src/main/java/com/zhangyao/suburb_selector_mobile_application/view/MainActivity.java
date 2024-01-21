package com.zhangyao.suburb_selector_mobile_application.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zhangyao.suburb_selector_mobile_application.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goMap(View view) {
        startActivity(new Intent(MainActivity.this, EnterLocationsActivity.class));
        finish();
    }
}