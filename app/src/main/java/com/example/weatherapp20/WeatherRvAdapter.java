package com.example.weatherapp20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherRvAdapter extends RecyclerView.Adapter<WeatherRvAdapter.ViewHolder> {

    private Context context;
    private ArrayList<WeatherRvModel> arrayList;

    @NonNull
    @Override
    public WeatherRvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherRvAdapter.ViewHolder holder, int position) {
        WeatherRvModel model = arrayList.get(position);
        holder.temp.setText(model.getTemp()+" °C");
        Picasso.get().load("http:".concat(model.getIcon())).into(holder.img);
        holder.wind.setText(model.getWindSpeed()+"Km/h");
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output = new SimpleDateFormat("hh:mm aa");
        try {
            Date t = input.parse(model.getTime());
            holder.time.setText(output.format(t));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        private TextView temp,time,wind;
        private ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            temp = itemView.findViewById(R.id.idTVTemp);
            time = itemView.findViewById(R.id.idTVTime);
            wind = itemView.findViewById(R.id.tvWINDSPEED);
            img = itemView.findViewById(R.id.TVCondition);

        }
    }
}
