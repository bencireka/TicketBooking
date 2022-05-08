package com.example.ticketbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity{

    private static final String LOG_TAG = RegisterActivity.class.getName();
    private static final String PREF_KEY = RegisterActivity.class.getPackage().toString();

    EditText userEmailET;
    EditText passwordET;
    EditText passwordConfirmedET;
    EditText phoneET;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth; //ezt fogjuk felhasználni arra, hogy a bekért emailcím és jelszó alapján fiókot hozzunk létre; regisztáljuk a usert firebase-be

    @Override
    protected void onCreate(Bundle savedInstanceState) { //A memóriába ezzel lehet mentéseket visszahozni, ha a sessionök között szeretnénk tárolni értékeket, SQLite-os adatbázisba mentünk/SharedPreferences
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0); //ha tudjuk, hogy csak egy db extrat akarunk elérni

        if(secret_key!=99){
            finish(); //visszatér az előző activitybe
        }
        userEmailET = findViewById(R.id.userEmailEditText);
        passwordET = findViewById(R.id.passwordEditText);
        passwordConfirmedET = findViewById(R.id.passwordAgainEditText);
        phoneET = findViewById(R.id.phoneEditText);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        String password = preferences.getString("password", "");

        passwordET.setText(password);
        passwordConfirmedET.setText(password);

        mAuth=FirebaseAuth.getInstance(); //ezzel érjük el az egyes firebase adta szolgáltatásokat

        Log.i(LOG_TAG, "onCreate");

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)RegisterActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!isNetworkConnected()){
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setTitle("Nem megfelelő internetkapcsolat");
            builder.setCancelable(true);

            builder.setMessage("Megfelelő internetkapcsolat nélkül nem működik az applikáció...");
            builder.show();
        }
    }

    public void register(View view) {
        try {
            String email = userEmailET.getText().toString();
            String password = passwordET.getText().toString();
            String passwordConfirmed = passwordConfirmedET.getText().toString();
            String phoneNumber = phoneET.getText().toString();

            if (!password.equals(passwordConfirmed)) {
                Log.e(LOG_TAG, "Nem egyenlő jelszó és a megerősítés.");
            } else {
                Log.i(LOG_TAG, "Regisztrált a következő emailcímmel rendelkező felhasználó: " + email);
            }

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(LOG_TAG, "User created succesfully!");
                        startBrowsing();
                    } else {
                        Log.d(LOG_TAG, "User wasn't created succesfully!");
                        Toast.makeText(RegisterActivity.this, "User wasn't created succesfully!" + task.getException().getMessage(), Toast.LENGTH_LONG).show(); //ezzel iratom ki, hogy a nem sikeresnál mi a gebasz

                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(RegisterActivity.this, "Add meg az összes kért adatot!", Toast.LENGTH_LONG).show();
        }
    }




    public void cancel(View view) {
        finish();
    }

    public void startBrowsing(/*registered user data*/){
        Intent intent = new Intent(this, BrowsingActivity.class);
        //intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
