package com.zhangyao.suburb_selector_mobile_application.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zhangyao.suburb_selector_mobile_application.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void eyeClick(View view) {
    }

    public void login(View view) {
        startActivity(new Intent(LoginActivity.this, MapActivity.class));
        finish();
    }
}