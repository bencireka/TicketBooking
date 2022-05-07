package com.example.ticketbooking;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
class TheatrePlayAdapter extends RecyclerView .Adapter<TheatrePlayAdapter.ViewHolder> implements Filterable{
    private ArrayList<TheatrePlay> mTheatrePlaysData;
    private ArrayList<TheatrePlay> mTheatrePlaysDataAll; //a szűrőhöz fog kelleni
    private Context context;
    private int lastPosition =-1;

    public TheatrePlayAdapter(Context context, ArrayList<TheatrePlay> itemsData) {
        this.mTheatrePlaysData=itemsData;
        this.mTheatrePlaysDataAll=itemsData;
        this.context=context;
    }


    //Ezzel csatoljuk hozzá a layouthoz az adaptert
    @Override
    public TheatrePlayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.table_row_item, parent, false)); //a korábban létrehozott listItem-et inicializáljuk itt
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull TheatrePlayAdapter.ViewHolder holder, int position) {
        TheatrePlay currentPlay=mTheatrePlaysData.get(position); //

        holder.bindTo(currentPlay);
    }

    @Override
    public int getItemCount() {
        //A shoppingdata méretét adja vissza
        return mTheatrePlaysData.size();
    }

    @Override
    public Filter getFilter() {
        return shoppingFilter;
    }

    private Filter shoppingFilter = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<TheatrePlay> filteredList = new ArrayList<>();
            FilterResults results=new FilterResults();

            if(charSequence == null || charSequence.length()==0){
                results.count=mTheatrePlaysDataAll.size();
                results.values = mTheatrePlaysDataAll;
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(TheatrePlay item : mTheatrePlaysDataAll){
                    if(item.getTitle().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
                results.count=filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mTheatrePlaysData = (ArrayList) filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mTitleText;
        private TextView mDateText;
        private TextView mLocationText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleText = itemView.findViewById(R.id.showTitle);
            mDateText = itemView.findViewById(R.id.showDate);
            mLocationText= itemView.findViewById(R.id.showLocation);

        }
        //A bindolt elemeket feltölti a megfelelő értékkel
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bindTo(TheatrePlay currentPlay) {
            mTitleText.setText(currentPlay.getTitle());
            mDateText.setText(currentPlay.getDate());
            mLocationText.setText(currentPlay.getTheatre().getLocation());

            itemView.findViewById(R.id.book_ticket).setOnClickListener(view -> ((BrowsingActivity)context).startBooking(currentPlay));
        }
    }
}
