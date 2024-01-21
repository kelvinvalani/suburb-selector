package com.example.suburbselectorfinal;

import static com.example.suburbselectorfinal.R.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class editDetails extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference reference;
    private FirebaseUser user;
    private String userID;
    private User userProfile;

    private TextView submitEdit;
    private EditText editTextFullName, editTextAge, editTextEmail;
    private ProgressBar progressBar;
    ImageView currentProfilePic;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.edit_details);

        currentProfilePic=findViewById(R.id.imageSlider);


        mAuth = FirebaseAuth.getInstance();

        submitEdit = (TextView) findViewById(id.submitEdit);
        submitEdit.setOnClickListener(this);
        currentProfilePic.setOnClickListener(this);

        editTextFullName = (EditText) findViewById(id.editFullName);
        editTextAge = (EditText) findViewById(id.editAge);
        editTextEmail = (EditText) findViewById(id.editEmail);


        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String fullName = userProfile.fullName;
                    String email = userProfile.email;
                    String age = userProfile.age;
                    String profilePic = userProfile.profilePic;

                    populateIcon(profilePic);

                    editTextFullName.setText(fullName);
                    editTextAge.setText(age);
                    editTextEmail.setText(email);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(editDetails.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });

        progressBar = (ProgressBar) findViewById(id.progressBar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case id.submitEdit:
                editDetails();
                break;
            case id.imageSlider:
                startActivity(new Intent(editDetails.this,GalleryActivity.class));
                break;

        }
    }
    private void populateIcon(String profilePic){
        if (Objects.equals(profilePic, "default")) {
            currentProfilePic.setImageResource(R.drawable.brimstone_icon);
        }
        else if (Objects.equals(profilePic, "brimstone")) {
            currentProfilePic.setImageResource(R.drawable.brimstone_icon);
        }
        else if (Objects.equals(profilePic, "sova")) {
            currentProfilePic.setImageResource(R.drawable.sova_icon);
        }
        else if (Objects.equals(profilePic, "killjoy")) {
            currentProfilePic.setImageResource(R.drawable.killjoy_icon);
        }
        else if (Objects.equals(profilePic, "jett")) {
            currentProfilePic.setImageResource(R.drawable.jett_icon);
        }
        else if (Objects.equals(profilePic, "viper")) {
            currentProfilePic.setImageResource(R.drawable.viper_icon);
        }
        else if (Objects.equals(profilePic, "sage")) {
            currentProfilePic.setImageResource(R.drawable.sage_icon);
        }
        else if (Objects.equals(profilePic, "chamber")) {
            currentProfilePic.setImageResource(R.drawable.chamber_icon);
        }
        else if (Objects.equals(profilePic, "reyna")) {
            currentProfilePic.setImageResource(R.drawable.reyna_icon);
        }
        else{currentProfilePic.setImageResource(R.drawable.brimstone_icon);}
    }

    private void editDetails() {
        String email = editTextEmail.getText().toString().trim();
        String fullName = editTextFullName.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();

        if(fullName.isEmpty()) {
            editTextFullName.setError("Full name is required!");
            editTextFullName.requestFocus();
            return;
        }

        if(age.isEmpty()) {
            editTextAge.setError("Age is required!");
            editTextAge.requestFocus();
            return;
        }

        if(email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide a valid email!");
            editTextEmail.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);
        reference = reference.child(userID);
        reference.child("fullName").setValue(fullName);
        reference.child("age").setValue(age);
        reference.child("email").setValue(email);
        Toast.makeText(editDetails.this, "You have successfully updated your details", Toast.LENGTH_LONG).show();
        progressBar.setVisibility(View.GONE);
        startActivity(new Intent(editDetails.this,ProfileActivity.class));

    }
}