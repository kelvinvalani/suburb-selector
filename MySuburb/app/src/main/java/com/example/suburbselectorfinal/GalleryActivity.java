package com.example.suburbselectorfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GalleryActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView sova,reyna,killjoy,jett,brimstone,chamber,sage,viper;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private FirebaseUser user;
    private String userID;
    private User userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        sova = findViewById(R.id.sova);
        reyna = findViewById(R.id.reyna);
        killjoy = findViewById(R.id.killjoy);
        jett = findViewById(R.id.jett);
        brimstone = findViewById(R.id.brimstone);
        chamber = findViewById(R.id.chamber);
        sage = findViewById(R.id.sage);
        viper = findViewById(R.id.viper);

        sova.setOnClickListener(this);
        reyna.setOnClickListener(this);
        killjoy.setOnClickListener(this);
        jett.setOnClickListener(this);
        brimstone.setOnClickListener(this);
        chamber.setOnClickListener(this);
        sage.setOnClickListener(this);
        viper.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sova:
                setProfilePic("sova");
                break;
            case R.id.reyna:
                setProfilePic("reyna");
                break;
            case R.id.killjoy:
                setProfilePic("killjoy");
                break;
            case R.id.jett:
                setProfilePic("jett");
                break;
            case R.id.brimstone:
                setProfilePic("brimstone");
                break;
            case R.id.chamber:
                setProfilePic("chamber");
                break;
            case R.id.sage:
                setProfilePic("sage");
                break;
            case R.id.viper:
                setProfilePic("viper");
                break;
        }
    }

    private void setProfilePic(String val) {
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference = reference.child(userID);
        reference.child("profilePic").setValue(val);
        Toast.makeText(this,"Successfully changed profile picture",Toast.LENGTH_LONG);
        startActivity(new Intent(GalleryActivity.this,editDetails.class));

    }

}
