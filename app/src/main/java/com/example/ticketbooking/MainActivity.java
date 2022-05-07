package com.example.ticketbooking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString(); //a package neve, asharedPreferences-hez fog kelleni
    private static final int SECRET_KEY=99; //ez a secret key, amit átadunk az intentnek

    EditText ETUserName;
    EditText ETPassword;

    Button loginButton;
    Button registerButton;

    //Ha a sessionök között szeretnénk tárolni értékeket, SQLite-os adatbázisba mentünk/SharedPreferences

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    //Az activity-k életciklusait különböző callback fgv-ekkel tudjuk kezelni; az onCreate is ilyen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ETUserName = findViewById(R.id.editTextUserName);
        ETPassword = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();

        //Random Async Task
        //Button button = findViewById(R.id.guestLoginButton);
        //new RandomAsyncTask(/*TODO*/).execute();

        //Random Async Loader
        getSupportLoaderManager().restartLoader(0,null, this);

        Log.i(LOG_TAG, "onCreate");
    }

    public void login(View view) {
        try{
            Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.blink);
            loginButton.startAnimation(animation);
            String userName = ETUserName.getText().toString();
            String password = ETPassword.getText().toString();
            //Log.i(LOG_TAG, "Bejelenkezett: " + userName + " jelszó: " + password);

            mAuth.signInWithEmailAndPassword(userName, password).addOnCompleteListener(this, task -> {
                if(task.isSuccessful() && ETUserName.length()!=0){
                    Log.d(LOG_TAG, "User logged in succesfully!");
                    startShopping();
                }else{
                    Log.d(LOG_TAG, "User login fail!");
                    Toast.makeText(MainActivity.this, "User login fail!" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }); //ezzel tudhatjuk meg mi volt az eredménye a műveletnek (sikerült vagy sem)
        } catch (Exception e){
            Toast.makeText(MainActivity.this, "Add meg az adataidat helyesen!", Toast.LENGTH_LONG).show();
        }
    }

    public void startShopping(){
        Intent intent = new Intent(this, BrowsingActivity.class);
        startActivity(intent);
    }

    public void register(View view) {
        Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.blink);
        registerButton.startAnimation(animation);
        Intent intent = new Intent(this, RegisterActivity.class); //kér egy kontextet és egy célt, hogy mit kell megnyitnia; üzenetküldésre alkalmas
        //Ha azt szeretnénk, hogy csak tegisztrált vásárlók tudjanak belépni a boltba, ADB shell kell, amit úgy oldunk meg, hogy egy secret key-t adunk meg az intentnek
        intent.putExtra("SECRET_KEY", SECRET_KEY); //A 99 értéket kapja meg ezáltal az intent, amit a registerActicity-ben olvasunk ki.

        startActivity(intent);
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(!isNetworkConnected()){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Nem megfelelő internetkapcsolat");
            builder.setCancelable(true);

            builder.setMessage("Megfelelő internetkapcsolat nélkül nem működik az applikáció...");
            builder.show();
        }
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

        //pause esetén lementjük a felhasználónevet és a jelszót (ha nincs üres stringet tesz bele), hogy folytatni tudjuk a sessiont
        //az itt elmentett adatokat eléhetjük egy másik activityből
        SharedPreferences.Editor editor =preferences.edit();
        editor.putString("userName", ETUserName.getText().toString());
        editor.putString("password", ETPassword.getText().toString());
        editor.apply();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume");
    }


    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        return new RandomAsyncLoader(this);
    }

    //Ezek a loadolós dolgok TODO átírni moddjuk a natúr bejelntkezésre
    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        Button button = findViewById(R.id.loginButton);
        button.setText(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}