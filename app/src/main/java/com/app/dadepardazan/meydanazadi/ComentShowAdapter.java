package com.app.dadepardazan.meydanazadi;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Bamdad on 12/8/2017.
 */

public abstract class ComentShowAdapter extends RecyclerView.Adapter<ComentShowAdapter.ViewHolder> {
    ArrayList<JSONObject> data_list;
    Context context;
    CardView cardv;

    public ComentShowAdapter(Context context, ArrayList<JSONObject> data_list) {
        this.context = context;
        this.data_list = data_list;
    }

    @Override
    public ComentShowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coment_show, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ComentShowAdapter.ViewHolder holder, final int position) {
        try {
            holder.coment_user_title.setText(data_list.get(position).getString("name"));
            holder.coment_description.setText(data_list.get(position).getString("text"));

            int seconds = (int) ((System.currentTimeMillis() / 1000) - data_list.get(position).getInt("date"));
            String temp = "";
            if (seconds < 60) {
                temp = "چند ثانیه پیش";


            } else if (seconds >= 60 && seconds < 3600) {

                temp = (seconds / 60) + " دقیقه پیش ";

            } else if (seconds >= 3600 && seconds < 86400) {

                temp = (seconds / 3600) + " ساعت پیش ";


            }
            else if (seconds >= 86400 && seconds < 2629743) {

                temp = (seconds / 86400) + " روز پیش ";


            }
            else if (seconds >= 2629743 && seconds < 31556926)  {

                temp = (seconds / 2629743) + " ماه پیش ";


            }else {
                temp = (seconds / 31556926) + " سال پیش ";
            }
            holder.coment_date.setText(temp);

//
//
//            holder.ad_date.setText( data_list.get(position).getString("date"));


        //  holder.ad_price.setText(data_list.get(position).ad_price);
        //  holder.ad_location.setText(data_list.get(position).ad_location);
        //  Picasso.with(context).load(data_list.get(position).ad_image).resize(128,128).into(holder.img);
    } catch (JSONException e) {
        e.printStackTrace();
    }



        if (position >= getItemCount() - 1) {

            load_more();
        }



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
        TextView coment_user_title;
        TextView coment_description;
        TextView coment_date;


        ImageView img_home;

        // TextView ad_location;
        CardView cardv;
        TextView ad_poblisher;


        public ViewHolder(View item) {
            super(item);
            coment_user_title = (TextView) item.findViewById(R.id.coment_user_title);
            coment_description = (TextView) item.findViewById(R.id.coment_description);
            coment_date= (TextView) item.findViewById(R.id.coment_date);


            //status.setBackgroundColor(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            //status.setVisibility(View.VISIBLE);
        }
    }
}
