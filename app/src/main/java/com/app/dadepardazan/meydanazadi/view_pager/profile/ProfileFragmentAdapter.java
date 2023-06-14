package com.app.dadepardazan.meydanazadi.view_pager.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.dadepardazan.meydanazadi.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Bamdad on 12/8/2017.
 */

public abstract class ProfileFragmentAdapter extends RecyclerView.Adapter<ProfileFragmentAdapter.ViewHolder> {
    ArrayList<JSONObject> data_list;
    Context context;

    public ProfileFragmentAdapter(Context context, ArrayList<JSONObject> data_list) {
        this.context = context;
        this.data_list = data_list;
    }

    @Override
    public ProfileFragmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProfileFragmentAdapter.ViewHolder holder, final int position) {
        try {
            holder.tv_profile_name_fragment.setText(data_list.get(position).getString("name"));
            holder.tv_profile_bio_fragment.setText(data_list.get(position).getString("bio"));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (!data_list.get(position).getString("image1").equals("")) {

                Picasso.get().load(data_list.get(position).getString("image1")).resize(128, 128).into(holder.image_profile_image_fragment);


            } else {

                holder.image_profile_image_fragment.setImageResource(R.drawable.no_photo);


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


//        holder.card_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent i = new Intent(context, ShowHomeActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                i.putExtra("ad", data_list.get(position).toString());
//                context.startActivity(i);
//
//
//            }
//        });

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
        TextView tv_profile_name_fragment;
        TextView tv_profile_bio_fragment;
        ImageView image_profile_image_fragment;
        TextView tv_time_and_date_fragment;
        TextView tv_text_profile_fragment;


        public ViewHolder(View item) {
            super(item);
            tv_profile_name_fragment = (TextView) item.findViewById(R.id.tv_profile_name_fragment);
            tv_profile_bio_fragment = (TextView) item.findViewById(R.id.tv_profile_bio_fragment);
            //ad_location = (TextView) item.findViewById(R.id.ad_location);
            image_profile_image_fragment = (ImageView) item.findViewById(R.id.image_profile_image_fragment);
            tv_time_and_date_fragment=(TextView)item.findViewById(R.id.tv_time_and_date_fragment);
            tv_text_profile_fragment=(TextView)item.findViewById(R.id.tv_text_profile_fragment);


        }
    }
}
