package com.example.ticketbooking;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;

public class SeatBookingActivity extends AppCompatActivity{
    private static final String LOG_TAG = SeatBookingActivity.class.getName();

    private TextView seatTextview;
    private String currentId;
    private String title;
    private TextView priceText;
    private boolean[] selectedSeat;
    private ArrayList<Integer> seatList = new ArrayList<>();
    private ArrayList<Integer> currentIndexes = new ArrayList<>();
    private String[] seats;
    private TheatrePlay currentItem;
    private ArrayList<Integer> seatsNotTaken;
    private Button buyButton;
    private Button cancelButton;

    private FirebaseFirestore mFirestore;
    private CollectionReference mTPlays;
    private NotificationHandler mNotificationHandler;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_booking);

        mFirestore = FirebaseFirestore.getInstance();
        mNotificationHandler = new NotificationHandler(this);
        mTPlays = mFirestore.collection("TheatrePlays");
        seatTextview = findViewById(R.id.chooseSeatTextView);
        priceText = findViewById(R.id.priceText);
        buyButton = findViewById(R.id.buy_ticket);
        cancelButton = findViewById(R.id.cancel);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentId = extras.getString("id", "nincs talalat");
            seats = extras.getStringArray("seats");
            title = extras.getString("title");
            seatsNotTaken = extras.getIntegerArrayList("seatsNotTaken");
        }

        mTPlays.document(currentId).get().addOnCompleteListener(documentSnapshot->{
            currentItem = documentSnapshot.getResult().toObject(TheatrePlay.class);
                });

        selectedSeat = new boolean[seats.length];
        currentIndexes = new ArrayList<>();

        seatTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SeatBookingActivity.this);

                builder.setTitle("Válasszon ülőhelyet!");

                builder.setCancelable(false);

                builder.setMultiChoiceItems(seats, selectedSeat, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {

                            seatList.add(i);
                            Collections.sort(seatList);

                        } else {

                            seatList.remove(Integer.valueOf(i));
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int price = 0;
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int j = 0; j < seatList.size(); j++) {
                            stringBuilder.append(seats[seatList.get(j)]);
                            currentIndexes.add(seatsNotTaken.get(seatList.get(j)));
                            price+=currentItem.getTheatre().getSeats().get(seatsNotTaken.get(seatList.get(j))).getPrice();
                            if (j != seatList.size() - 1) {
                                stringBuilder.append("\n");
                            }
                        }
                        seatTextview.setText(stringBuilder.toString());
                        priceText.setText(price + " Ft");
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        priceText.setText("");
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (int j = 0; j < selectedSeat.length; j++) {
                            selectedSeat[j] = false;
                            seatList.clear();
                            seatTextview.setText("");
                            priceText.setText("");
                        }
                    }
                });
                builder.show();
            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)SeatBookingActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!isNetworkConnected()){
            AlertDialog.Builder builder = new AlertDialog.Builder(SeatBookingActivity.this);
            builder.setTitle("Nem megfelelő internetkapcsolat");
            builder.setCancelable(true);

            builder.setMessage("Megfelelő internetkapcsolat nélkül nem működik az applikáció...");
            builder.show();
        }
    }

    public void nope(View view) {
        Animation animation = AnimationUtils.loadAnimation(SeatBookingActivity.this, R.anim.bounce);
        cancelButton.startAnimation(animation);
        Intent intent = new Intent(this, BrowsingActivity.class);
        startActivity(intent);
    }

    public void buyTicket(View view) {
        if(currentIndexes.size()!=0){
            Animation animation = AnimationUtils.loadAnimation(SeatBookingActivity.this, R.anim.bounce);
            buyButton.startAnimation(animation);
            for(int index : currentIndexes){
                currentItem.getTheatre().getSeats().get(index).setTaken(true);
                currentItem.setCurrentTicketsSold(currentItem.getCurrentTicketsSold()+1);
            }
            mTPlays.document(currentId).update("theatre", currentItem.getTheatre()).addOnFailureListener(queryDocumentSnapshots2 -> {
                Toast.makeText(this, "Item cannot be changed.", Toast.LENGTH_LONG).show();
            });
            mTPlays.document(currentId).update("currentTicketsSold", currentItem.getCurrentTicketsSold()).addOnFailureListener(queryDocumentSnapshots2 -> {
                Toast.makeText(this, "Item cannot be changed.", Toast.LENGTH_LONG).show();
            });
            mNotificationHandler.send(currentIndexes.size() + " jegyet vettél a " + title + "című darabra!");
            Intent intent = new Intent(this, BrowsingActivity.class);
            startActivity(intent);
        }

    }

   // public void buyAll(View view) {
     //   currentIndexes.clear();
       // Animation animation = AnimationUtils.loadAnimation(SeatBookingActivity.this, R.anim.bounce);
        //buyAllButton.startAnimation(animation);
        //for(int i=0; i<seatsNotTaken.size(); i++){
        //    currentIndexes.add(seatsNotTaken.get(i));
        //}
        //priceText.setText("Have you really thought about this!");
    //}
}