package com.example.ticketbooking;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.google.type.Color;

public class NotificationHandler {
    private Context mContext;
    private NotificationManager mManager;

    private static final String CHANNEL_ID = "shop_notification_channel";
    private final int NOTIFICATION_ID = 0;

    public NotificationHandler(Context context) {
        this.mContext = context;
        this.mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createChannel();

    }
    private void createChannel(){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.O){
            return;
        }
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "TicketBooking Notification", NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(Color.GREEN_FIELD_NUMBER);
        channel.setDescription("Notifications from TicketBooking application.");
        this.mManager.createNotificationChannel(channel);

    }
    public void send(String message){
        Intent intent = new Intent(mContext, SeatBookingActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setContentTitle("TicketBooking Application")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_theaters)
                .setContentIntent(pendingIntent);

        this.mManager.notify(NOTIFICATION_ID, builder.build());
    }

}
