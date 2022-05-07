package com.example.ticketbooking;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class BrowsingActivity extends AppCompatActivity {
    private static final String LOG_TAG = BrowsingActivity.class.getName();
    private FirebaseUser user;
    private RecyclerView mRecyclerView;
    private ArrayList<TheatrePlay> mTheatrePlayData;
    private TheatrePlayAdapter mAdapter;

    private FirebaseFirestore mFirestore;
    private CollectionReference mTPlays;

    private int gridNumber = 1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browsing);

        user = FirebaseAuth.getInstance().getCurrentUser(); //lekérjünk annak a usernek az adatait aki be van jelentkezve
        if (user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }
        mRecyclerView = findViewById(R.id.recycleView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mTheatrePlayData = new ArrayList<>();
        mAdapter = new TheatrePlayAdapter(this, mTheatrePlayData);

        mRecyclerView.setAdapter(mAdapter);

        mFirestore = FirebaseFirestore.getInstance();
        mTPlays = mFirestore.collection("TheatrePlays");
        checkAndQueryData();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)BrowsingActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!isNetworkConnected()){
            AlertDialog.Builder builder = new AlertDialog.Builder(BrowsingActivity.this);
            builder.setTitle("Nem megfelelő internetkapcsolat");
            builder.setCancelable(true);

            builder.setMessage("Megfelelő internetkapcsolat nélkül nem működik az applikáció...");
            builder.show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void deleteItem(TheatrePlay theatrePlay){
        DocumentReference ref = mTPlays.document(theatrePlay._getId());

        ref.delete().addOnSuccessListener(succes->{
            Log.d(LOG_TAG, "Item is successfully deleted!" + theatrePlay._getId());
        })
                .addOnFailureListener(failure->{
                    Toast.makeText(this, "Item " + theatrePlay._getId() + "cannot be deleted.", Toast.LENGTH_LONG).show();
                });
        //mNotificationHandler.cancel();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkAndQueryData() {
        mTheatrePlayData.clear();

        mTPlays.orderBy("date").limit(20).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot document: queryDocumentSnapshots){
                TheatrePlay tp = document.toObject(TheatrePlay.class);
                tp.setId(document.getId());
                if(tp.isTooLate()){
                    deleteItem(tp);
                }else{
                    mTheatrePlayData.add(tp);
                }
            }
            if (mTheatrePlayData.size() == 0) {

                initializeData();
                checkAndQueryData();
            }
            mAdapter.notifyDataSetChanged();
        });
    }

    private void initializeData() {
        // Get the resources from the XML file.
        String[] showList = getResources()
                .getStringArray(R.array.theatre_play_titles);
        String[] showLocation = getResources()
                .getStringArray(R.array.theatre_play_locations);
        String[] showDates =  getResources().
                getStringArray(R.array.theatre_play_dates);



        for (int i = 0; i < showList.length; i++) {
            mTPlays.add(new TheatrePlay(
                        showList[i],
                        showDates[i],
                        showLocation[i]));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.browsing_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(LOG_TAG, s);
                mAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out_button:
                Log.d(LOG_TAG, "Logout clicked!");
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.location_button:
                Log.d(LOG_TAG, "Location clicked!");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startBooking(TheatrePlay currentPlay){
        Intent intent;
        ArrayList<Integer> seatsNotTaken = new ArrayList<>();
        Bundle bundle = new Bundle();
        bundle.putString("title", currentPlay.getTitle());
        bundle.putString("id", currentPlay._getId());

        if (currentPlay.getCurrentTicketsSold()==currentPlay.getTheatre().getMaxCapacity()){
            intent = new Intent(this, NoSeatAvailableActivity.class);
        }else{
            intent = new Intent(this, SeatBookingActivity.class);
            bundle.putStringArray("seats", currentPlay.getTheatre().toStringArray());
            for(int index=0; index<currentPlay.getTheatre().getSeats().size();index++){
                if(!currentPlay.getTheatre().getSeats().get(index).isTaken()){
                    seatsNotTaken.add(index);
                }
            }
            bundle.putIntegerArrayList("seatsNotTaken", seatsNotTaken);
        }
        intent.putExtras(bundle);
        startActivity(intent);
    }
}