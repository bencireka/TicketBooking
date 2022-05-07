package com.example.ticketbooking;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TheatrePlay {
    private String id;
    private String title;
    private String date;

    private  Theatre theatre;


    private int currentTicketsSold;

    public TheatrePlay() {
    }

    public TheatrePlay(String title, String date, String location) {

        this.title = title;
        this.date = date;
        this.theatre = new Theatre(location);
        this.currentTicketsSold = 0;
    }

    public String _getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    /*public String getEarliestDate() {
        LocalDateTime earliest = LocalDateTime.MAX;
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy–MM–dd HH:mm");

        for (String date : date) {
            LocalDateTime dateTime = LocalDateTime.parse(date, formatter1);
            if (dateTime.isBefore(earliest) && !dateTime.isBefore(LocalDateTime.now())) {
                earliest = dateTime;
            }
        }
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MM-dd HH:mm");
        return earliest.format(formatter2);
    }*/

    public String getDate() {
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy–MM–dd HH:mm");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MM-dd HH:mm");
        return date;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean isTooLate(){
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy–MM–dd HH:mm");
        return LocalDateTime.parse(date, formatter1).isBefore(LocalDateTime.now());
    }

    public int getCurrentTicketsSold() {
        return currentTicketsSold;
    }
    public void setId(String id) {
        this.id = id;
    }

    public Theatre getTheatre() {
        return theatre;
    }

    public void setCurrentTicketsSold(int currentTicketsSold) {
        this.currentTicketsSold = currentTicketsSold;
    }
}
