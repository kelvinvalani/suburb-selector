package com.example.suburbselectorfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseUser user;
    private DatabaseReference reference;

    private String userID;

    private Button logout, findSuburb;

    User userProfile;

    RecyclerView recyclerView;
    RecyclerViewAdapter mAdapter;
    ArrayList<String> stringArrayList = new ArrayList<>();
    CoordinatorLayout coordinatorLayout;
    ImageView displayIcon,edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        edit = findViewById(R.id.edit);
        edit.setOnClickListener(this);
        displayIcon = findViewById(R.id.profilePicture);

        recyclerView = findViewById(R.id.recyclerView);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        logout = (Button) findViewById(R.id.logOut);
        logout.setOnClickListener(this);

        findSuburb = (Button) findViewById(R.id.findSuburb);
        findSuburb.setOnClickListener(this);

        refreshUserData();

    }

    private void refreshUserData() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView profileNameTextView = (TextView) findViewById(R.id.profileName);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String fullName = userProfile.fullName;
                    String email = userProfile.email;
                    String age = userProfile.age;
                    List<String> favourites = userProfile.favourites;
                    String profilePic = userProfile.profilePic;

                    populateIcon(profilePic);
                    profileNameTextView.setText(fullName);

                    populateRecyclerView(favourites);
                    enableSwipeToDeleteAndUndo();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void populateRecyclerView(List<String> favourites) {
        stringArrayList.clear();

        if(favourites != null) {
            for(String favourite: favourites) {
                stringArrayList.add(MapsActivity.capitalise(favourite));
            }
        }

        mAdapter = new RecyclerViewAdapter(stringArrayList);
        recyclerView.setAdapter(mAdapter);
    }

    private void populateIcon(String profilePic){
        if (Objects.equals(profilePic, "default")) {
            displayIcon.setImageResource(R.drawable.brimstone_icon);
        }
        else if (Objects.equals(profilePic, "brimstone")) {
            displayIcon.setImageResource(R.drawable.brimstone_icon);
        }
        else if (Objects.equals(profilePic, "sova")) {
            displayIcon.setImageResource(R.drawable.sova_icon);
        }
        else if (Objects.equals(profilePic, "killjoy")) {
            displayIcon.setImageResource(R.drawable.killjoy_icon);
        }
        else if (Objects.equals(profilePic, "jett")) {
            displayIcon.setImageResource(R.drawable.jett_icon);
        }
        else if (Objects.equals(profilePic, "viper")) {
            displayIcon.setImageResource(R.drawable.viper_icon);
        }
        else if (Objects.equals(profilePic, "sage")) {
            displayIcon.setImageResource(R.drawable.sage_icon);
        }
        else if (Objects.equals(profilePic, "chamber")) {
            displayIcon.setImageResource(R.drawable.chamber_icon);
        }
        else if (Objects.equals(profilePic, "reyna")) {
            displayIcon.setImageResource(R.drawable.reyna_icon);
        }
        else{displayIcon.setImageResource(R.drawable.brimstone_icon);}
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAbsoluteAdapterPosition();
                final String item = mAdapter.getData().get(position);

                mAdapter.removeItem(position);

                reference.child(userID).child("favourites").setValue(mAdapter.getData());

                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mAdapter.restoreItem(item, position);
                        reference.child(userID).child("favourites").setValue(mAdapter.getData());
                        recyclerView.scrollToPosition(position);
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                break;

            case R.id.findSuburb:
                startActivity(new Intent(ProfileActivity.this, MapsActivity.class));
                break;
            case R.id.edit:
                startActivity(new Intent(ProfileActivity.this, editDetails.class));
                break;
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        refreshUserData();
    }
}