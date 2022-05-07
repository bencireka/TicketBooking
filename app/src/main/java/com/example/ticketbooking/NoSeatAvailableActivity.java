package com.example.ticketbooking;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class NoSeatAvailableActivity extends AppCompatActivity {

    private static final String LOG_TAG = NoSeatAvailableActivity.class.getName();

    private Button cancelButton;
    private TextView noMoreDates;
    private String title;
    private String currentId;

    private FirebaseFirestore mFirestore;
    private CollectionReference mTPlays;
    private ArrayList<TheatrePlay> mTheatrePlayData;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_seat_available);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            title = extras.getString("title");
            currentId = extras.getString("id");
        }

        mFirestore = FirebaseFirestore.getInstance();
        mTPlays = mFirestore.collection("TheatrePlays");
        mTheatrePlayData = new ArrayList<>();

        cancelButton = findViewById(R.id.understood);
        noMoreDates = findViewById(R.id.noMoreDates);

        noMoreDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOtherDates();
            }
        });

        query();
    }

    public void query(){
        mTheatrePlayData.clear();
        mTPlays.whereEqualTo("title", title).orderBy("date").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot document: queryDocumentSnapshots){
                TheatrePlay tp = document.toObject(TheatrePlay.class);
                tp.setId(document.getId());
                if(!tp._getId().equals(currentId)){
                    mTheatrePlayData.add(tp);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showOtherDates(){
        StringBuilder stringBuilder = new StringBuilder();
        for(TheatrePlay tp : mTheatrePlayData){
            stringBuilder.append(tp.getDate() + "\n");
        }
        noMoreDates.setText(stringBuilder);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nope(View view) {
        Animation animation = AnimationUtils.loadAnimation(NoSeatAvailableActivity.this, R.anim.blink);
        cancelButton.startAnimation(animation);
        Intent intent = new Intent(this, BrowsingActivity.class);

        startActivity(intent);
    }
}