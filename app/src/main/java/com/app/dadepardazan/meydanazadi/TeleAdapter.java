package com.app.dadepardazan.meydanazadi;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Bamdad on 12/8/2017.
 */

public abstract class TeleAdapter extends RecyclerView.Adapter<TeleAdapter.ViewHolder> {
    ArrayList<JSONObject> data_list;
    Context context;
    CardView cardv;

    public TeleAdapter(Context context, ArrayList<JSONObject> data_list) {
        this.context = context;
        this.data_list = data_list;
    }

    @Override
    public TeleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tele_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TeleAdapter.ViewHolder holder, final int position) {
        try {
            holder.ad_text.setText(data_list.get(position).getString("text"));
            holder.ad_manba.setText(data_list.get(position).getString("source"));


            holder.ad_date.setText( data_list.get(position).getString("date"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //  holder.ad_price.setText(data_list.get(position).ad_price);
        //  holder.ad_location.setText(data_list.get(position).ad_location);
        //  Picasso.with(context).load(data_list.get(position).ad_image).resize(128,128).into(holder.img);


        if (position >= getItemCount() - 1) {

            load_more();
        }


        holder.cardv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(context, ShowHomeActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                i.putExtra("ad", data_list.get(position).toString());
//
//                context.startActivity(i);
            }
        });

    }

    public abstract void load_more();


    @Override
    public int getItemCount() {
        return data_list.size();
    }

    public void insert(int position, JSONArray ad_list) {


        try {


            for (int i = 0; i < ad_list.length(); i++) {


                data_list.add(ad_list.getJSONObject(i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        notifyItemInserted(position);


    }


    public void clear_list() {

        int size = data_list.size();
        data_list.clear();
        notifyItemRangeRemoved(0, size);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ad_text;
        TextView ad_manba;
        TextView ad_date;

        // TextView ad_location;
        CardView cardv;
        TextView ad_poblisher;


        public ViewHolder(View item) {
            super(item);
            ad_text = (TextView) item.findViewById(R.id.ad_text);
            ad_manba = (TextView) item.findViewById(R.id.ad_manba);
            ad_date = (TextView) item.findViewById(R.id.ad_date);

            //ad_location = (TextView) item.findViewById(R.id.ad_location);
            cardv = (CardView) item.findViewById(R.id.cardv);
            ad_poblisher = (TextView) item.findViewById(R.id.ad_poblisher);

            //status.setBackgroundColor(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            //status.setVisibility(View.VISIBLE);
        }
    }
}
