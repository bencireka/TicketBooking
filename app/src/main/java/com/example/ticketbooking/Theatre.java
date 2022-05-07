package com.example.ticketbooking;

import java.util.ArrayList;

public class Theatre {
    final private int NAGY_MAX = 428;
    final private int KIS_MAX = 180;
    private int maxCapacity;
    private String location;
    private ArrayList<Seat> seats = new ArrayList<>();

    public Theatre() {
    }

    public Theatre(String location) {
        this.location = location;
        if(location.equals("NAGYSZÍNHÁZ")){
            initNagy();
        }else{
           initKicsit();
        }
    }

    public String[] toStringArray(){
        int meret=0;
        for(Seat seat : seats){
            if(!seat.isTaken()){
                meret++;
            }
        }
        String[] rs = new String[meret];
        int i=0;
        for (Seat seat : seats){
            if(!seat.isTaken()) {
                rs[i] = seat.toString();
                i++;
            }
        }
        return rs;
    }

    public void initKicsit(){
        maxCapacity = KIS_MAX;
        for (int i=1; i<=6; i++){
            for (int j=1; j<=10; j++){
                seats.add(new Seat(j<7?"BAL Földszint1":"JOBB Földszint1",
                        i,
                        j<=5?j:(11-j),
                        i<2?4000:3600));
            }
        }
        for (int i=1; i<=10; i++){
            for (int j=1; j<=12; j++){
                seats.add(new Seat(j<7?"BAL Földszint2":"JOBB Földszint2",
                        i,
                        j<=6?j:(13-j),
                        i<6?3200:2800));
            }
        }
    }

    public void initNagy(){
        maxCapacity = NAGY_MAX;
        for (int i=1; i<=7; i++){
            for (int j=1; j<=12; j++){
                seats.add(new Seat(j<7?"BAL Földszint1":"JOBB Földszint1",
                        i,
                        j<7?j:(13-j),
                        i<4?4800:4400));
            }
        }

        for (int i=1; i<=12; i++){
            for (int j=1; j<=13; j++){
                seats.add(new Seat(j<8?"BAL Földszint2":"JOBB Földszint2",
                        i,
                        j<8?j:(14-j),
                        i<6?4000:3600));
            }
        }
        for (int i=1; i<=5; i++){
            for (int j=1; j<=12; j++){
                seats.add(new Seat(j<7?"BAL Emelet":"JOBB Emelet",
                        i,
                        j<7?j:(13-j),
                        i<4?4800:4400));
            }
        }
        for (int i=1; i<=4; i++){
            for (int j=1; j<=6; j++){
                String sector;
                if(i>2 && j>3){
                    sector="BAL Erkély páholy2";
                }else if (i>2 && j<=3){
                    sector="BAL Erkély páholy1";
                }else if (i<=2 && j>3) {
                    sector = "BAL Emeleti páholy2";
                }else{
                    sector = "BAL Emeleti páholy1";
                }
                int seatNum;
                if(i%2!=0 && j<=3){
                    seatNum=j;
                }else if(i%2!=0 && j>3){
                    seatNum=j-3;
                }else if(i%2==0 && j<=3){
                    seatNum=j+3;
                }else{
                    seatNum=j;
                }
                seats.add(new Seat(sector,
                        i<=2?i:i-2,
                        seatNum,
                        j<=3?2400:3200));
            }
        }
        for (int i=1; i<=4; i++){
            for (int j=1; j<=6; j++){
                String sector;
                if(i>2 && j>3){
                    sector="JOBB Emeleti páholy2";
                }else if (i>2 && j<=3){
                    sector="JOBB Emeleti páholy1";
                }else if (i<=2 && j>3) {
                    sector = "JOBB Erkély páholy2";
                }else{
                    sector = "JOBB Erkély páholy1";
                }
                int seatNum;
                if(i%2!=0 && j<=3){
                    seatNum=j;
                }else if(i%2!=0 && j>3){
                    seatNum=j-3;
                }else if(i%2==0 && j<=3){
                    seatNum=j+3;
                }else{
                    seatNum=j;
                }
                seats.add(new Seat(sector,
                        i<=2?i:i-2,
                        seatNum,
                        j<=3?2400:3200));
            }
        }
        for (int i=1; i<=5; i++){
            for (int j=1; j<=8; j++){
                seats.add(new Seat("BAL Emelet",
                        i,
                        j,
                        (j<=4 && i>3)?4400:2400));
            }
        }
        for (int i=1; i<=5; i++){
            for (int j=1; j<=8; j++){
                seats.add(new Seat("JOBB Emelet",
                        i,
                        j,
                        (j<=4 && i<3)?4400:2400));
            }
        }
        for (int i=1; i<=8; i++){
            for (int j=1; j<=12; j++){
                seats.add(new Seat(j<7?"BAL Szemközti Emelet":"JOBB Szemközti Emelet",
                        i,
                        j<7?j:(13-j),
                        i<4?4800:4000));
            }
        }
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public String getLocation() {
        return location;
    }

    public ArrayList<Seat> getSeats() {
        return seats;
    }

    public void modifySeatIndexes(){

    }
}
