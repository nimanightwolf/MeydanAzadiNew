package com.app.dadepardazan.meydanazadi;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Bamdad on 12/8/2017.
 */

public abstract class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    ArrayList<JSONObject> data_list;
    Context context;
    CardView cardv;



    public DataAdapter(Context context, ArrayList<JSONObject> data_list) {
        this.context = context;
        this.data_list = data_list;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, final int position) {
        try {
            holder.ad_title.setText(data_list.get(position).getString("title"));

//            String[] province_list = context.getResources().getStringArray(R.array.province);
//
            //holder.ad_location.setText("استان " );

//            ArrayAdapter<CharSequence> myadapter = ArrayAdapter.createFromResource(context, context.getResources().getIdentifier("array/city" + data_list.get(position).getInt("province"), null, context.getPackageName()), R.layout.row);
//            holder.ad_location.setText(holder.ad_location.getText() + " , " + myadapter.getItem(data_list.get(position).getInt("city")) + " \n " + data_list.get(position).getString("district"));

//
//            int seconds = (int) ((System.currentTimeMillis() / 1000) - data_list.get(position).getInt("date"));
//            String temp = "";
//            if (seconds < 60) {
//                temp = "چند ثانیه پیش";
//
//
//            } else if (seconds >= 60 && seconds < 3600) {
//
//                temp = (seconds / 60) + " دقیقه پیش ";
//
//            } else if (seconds >= 3600 && seconds < 86400) {
//
//                temp = (seconds / 3600) + " ساعت پیش ";
//
//
//            }
//            else if (seconds >= 86400 && seconds < 2592000) {
//
//                temp = (seconds / 86400) + " روز پیش ";
//
//
//            }
//            else  {
//
//                temp = (seconds / 2592000) + " ماه پیش ";
//
//
//            }
//
//
//            holder.ad_location.setText(holder.ad_location.getText() + " \n " + temp);


            holder.ad_date.setText(data_list.get(position).getString("hours") + " - " + data_list.get(position).getString("date"));


            holder.ad_poblisher.setText(data_list.get(position).getString("publisher"));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (!data_list.get(position).getString("image1").equals("")) {
                Picasso.get().load(data_list.get(position).getString("image1")).resize(128, 128).into(holder.img);

              //  Picasso.with(context).load(data_list.get(position).getString("image1")).resize(128, 128).into(holder.img);

            } else {

                if (!data_list.get(position).getString("image2").equals("")) {
                    Picasso.get().load(data_list.get(position).getString("image2")).resize(128, 128).into(holder.img);
                  //  Picasso.with(context).load(data_list.get(position).getString("image2")).resize(128, 128).into(holder.img);

                } else {

                    if (!data_list.get(position).getString("image3").equals("")) {
                        Picasso.get().load(data_list.get(position).getString("image3")).resize(128, 128).into(holder.img);
                      //  Picasso.with(context).load(data_list.get(position).getString("image3")).resize(128, 128).into(holder.img);

                    } else {

                        holder.img.setImageResource(R.drawable.no_photo);


                    }


                }

            }
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

                Intent i = new Intent(context, ShowHomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                i.putExtra("ad", data_list.get(position).toString());
                context.startActivity(i);


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
        TextView ad_title;
        TextView ad_date;
        // TextView ad_location;
        ImageView img;
        CardView cardv;
        TextView ad_poblisher;
        Handler handler;


        public ViewHolder(View item) {
            super(item);
            ad_title = (TextView) item.findViewById(R.id.ad_title);
            ad_date = (TextView) item.findViewById(R.id.ad_date);
            //ad_location = (TextView) item.findViewById(R.id.ad_location);
            img = (ImageView) item.findViewById(R.id.img);
            cardv = (CardView) item.findViewById(R.id.cardv);
            ad_poblisher = (TextView) item.findViewById(R.id.ad_poblisher);


            //status.setBackgroundColor(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            //status.setVisibility(View.VISIBLE);
        }
    }
}
