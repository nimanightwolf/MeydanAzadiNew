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

public abstract class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    ArrayList<JSONObject> data_list;
    Context context;
    CardView cardv;
    int one_click_intent = 1;


    public HomeAdapter(Context context, ArrayList<JSONObject> data_list) {
        this.context = context;
        this.data_list = data_list;
    }

    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeAdapter.ViewHolder holder, final int position) {
        try {
            holder.ad_text_home.setText(data_list.get(position).getString("title"));
            holder.number_like_home.setText(data_list.get(position).getString("like"));
            holder.number_coment_home.setText(data_list.get(position).getString("coment"));

            holder.ad_pobliser_home.setText(data_list.get(position).getString("source"));

//            int seconds = (int) ((System.currentTimeMillis() / 1000) - data_list.get(position).getInt("date_sm"));
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
//            else if (seconds >= 86400 && seconds < 2629743) {
//
//                temp = (seconds / 86400) + " روز پیش ";
//
//
//            }
//            else if (seconds >= 2629743 && seconds < 31556926)  {
//
//                temp = (seconds / 2629743) + " ماه پیش ";
//
//
//            }else {
//                temp = (seconds / 31556926) + " سال پیش ";
//            }
            //holder.ad_date_ago_home.setText(temp);
            if (!data_list.get(position).getString("date").isEmpty()) {
                holder.ad_date_home.setText(data_list.get(position).getString("date"));
            }
//
//
//            holder.ad_date.setText( data_list.get(position).getString("date"));


        //  holder.ad_price.setText(data_list.get(position).ad_price);
        //  holder.ad_location.setText(data_list.get(position).ad_location);
        //  Picasso.with(context).load(data_list.get(position).ad_image).resize(128,128).into(holder.img);
    } catch (JSONException e) {
        e.printStackTrace();
    }

        try {
            if (!data_list.get(position).getString("image1").equals("")) {

                Picasso.get().load("https://meydane-azadi.ir/photo/photo_news.php?image_name=" + data_list.get(position).getString("image1")).resize(800, 600).into(holder.img_home);

            } else {

                if (!data_list.get(position).getString("image2").equals("")) {

                    Picasso.get().load("https://meydane-azadi.ir/photo/photo_news.php?image_name=" + data_list.get(position).getString("image2")).resize(800, 600).into(holder.img_home);

                } else {

                    if (!data_list.get(position).getString("image3").equals("")) {

                        Picasso.get().load("https://meydane-azadi.ir/photo/photo_news.php?image_name=" + data_list.get(position).getString("image3")).resize(800, 600).into(holder.img_home);

                    } else {

                        holder.img_home.setImageResource(R.drawable.no_photo);


                    }


                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (position >= getItemCount() - 1) {

            load_more();
        }


        holder.cardv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ShowHomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                i.putExtra("ad", data_list.get(position).toString());
                if (one_click_intent==1) {
                    context.startActivity(i);
                    one_click_intent=2;
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        one_click_intent=1;
                    }
                },1000);
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
        TextView ad_text_home;
        TextView ad_pobliser_home;
        TextView ad_date_ago_home;
        TextView ad_date_home;
        TextView number_like_home;
        TextView number_coment_home;

        ImageView img_home;

        // TextView ad_location;
        CardView cardv;
        TextView ad_poblisher;



        public ViewHolder(View item) {
            super(item);
            ad_text_home = (TextView) item.findViewById(R.id.ad_text_home);
            ad_pobliser_home = (TextView) item.findViewById(R.id.ad_pobliser_home);
            ad_date_ago_home = (TextView) item.findViewById(R.id.ad_date_ago_home);
            ad_date_home= (TextView) item.findViewById(R.id.ad_date_home);
            number_like_home= (TextView) item.findViewById(R.id.number_like_home);
            number_coment_home = (TextView) item.findViewById(R.id.number_coment_home);

            //ad_location = (TextView) item.findViewById(R.id.ad_location);
            cardv = (CardView) item.findViewById(R.id.cardv);
            ad_poblisher = (TextView) item.findViewById(R.id.ad_poblisher);
            img_home = (ImageView) item.findViewById(R.id.img_home);



            //status.setBackgroundColor(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            //status.setVisibility(View.VISIBLE);
        }
    }
}
