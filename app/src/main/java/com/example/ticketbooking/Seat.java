package com.example.ticketbooking;

public class Seat {
    private boolean taken = false;
    private String sector;
    private int row;
    private int seatNum;
    private int price;

    public Seat() {
    }

    public Seat(String sector, int row, int seatNum, int price) {
        this.taken = false;
        this.sector = sector;
        this.row = row;
        this.seatNum = seatNum;
        this.price = price;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    public boolean isTaken() {
        return taken;
    }

    public String getSector() {
        return sector;
    }

    public int getRow() {
        return row;
    }

    public int getSeatNum() {
        return seatNum;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Szék: " + seatNum + ", Sor: "+row+", Szektor: "+sector+", Jegyár: "+ price + " Ft " + (taken?"(foglalt)":"");
    }
}
