package com.example.ticketbooking;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class RandomAsyncLoader extends AsyncTaskLoader<String> {
    public RandomAsyncLoader(@NonNull Context context) {
        super(context);
    }

    @Nullable
    @Override
    public String loadInBackground() {
        return null;
    }
}
