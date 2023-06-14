package com.app.dadepardazan.meydanazadi.view_pager.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.dadepardazan.meydanazadi.R;
import com.app.dadepardazan.meydanazadi.all_tweet.AllTweetActivity;
import com.app.dadepardazan.meydanazadi.view_pager.post.ActiviteAndShowTweeetActivity;
import com.app.dadepardazan.meydanazadi.view_pager.profile.ProfileAndShowToitActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Bamdad on 12/8/2017.
 */

public abstract class SearchFragmentAdapter extends RecyclerView.Adapter<SearchFragmentAdapter.ViewHolder> {
    ArrayList<JSONObject> data_list;
    Context context;
    SharedPreferences settings;
    boolean is_tag;


    public SearchFragmentAdapter(Context context, ArrayList<JSONObject> data_list, boolean is_tag) {
        this.context = context;
        this.data_list = data_list;
        settings = PreferenceManager.getDefaultSharedPreferences(this.context);
        this.is_tag = is_tag;
    }

    @Override
    public SearchFragmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SearchFragmentAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        try {
            if (is_tag) {
                holder.image_user_search.setImageResource(R.drawable.icon_hashtag);
                holder.tv_name_user_search.setText(data_list.get(position).getString("name"));
              //  holder.tv_bio_user_search.setText(data_list.get(position).getString("number")+"پست");
                holder.tv_bio_user_search.setText("");
            } else {
                holder.tv_name_user_search.setText(data_list.get(position).getString("name"));
                holder.tv_bio_user_search.setText(data_list.get(position).getString("username"));
                if (!data_list.get(position).getString("image_address").equals("")) {
                    Picasso.get().load(data_list.get(position).getString("image_address")).resize(512, 512).into(holder.image_user_search);
                  //  https://meydane-azadi.ir/photo/photo.php?image_name="+
                    //  Picasso.with(context).load(data_list.get(position).getString("image_address")).resize(512, 512).into(holder.image_user_search);
                } else {

                    holder.image_user_search.setImageResource(R.drawable.icon_user);


                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (position >= getItemCount() - 1) {

            load_more();
        }


        holder.card_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if (is_tag){
                        Intent i = new Intent(context, AllTweetActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("hashtag", data_list.get(position).getString("name"));
                        context.startActivity(i);

                    }else {
                        Log.e("data general:", data_list.get(position).toString());
                        Intent i = new Intent(context, ActiviteAndShowTweeetActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("ad", data_list.get(position).toString());
                        i.putExtra("user_token", data_list.get(position).getString("user_token"));
                        i.putExtra("username", data_list.get(position).getString("username"));
                        ////
                        holder.image_user_search.buildDrawingCache();
                        Bitmap image = holder.image_user_search.getDrawingCache();
                        Bundle extras = new Bundle();
                        extras.putParcelable("imagebitmap", image);
                        i.putExtras(extras);
                        if (data_list.get(position).getString("user_token").equals(settings.getString("token", ""))) {

                            i = new Intent(context, ProfileAndShowToitActivity.class);
                        }

                        //                BitmapDrawable bd = ((BitmapDrawable) holder.image_user_search.getDrawable());
                        //                Bitmap bm = bd.getBitmap();
                        //                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                        //                bm.compress(Bitmap.CompressFormat.PNG, 90, bao);
                        //                String image_base64 = Base64.encodeToString(bao.toByteArray(), Base64.DEFAULT);
                        //
                        //                i.putExtras("image",image_base64);


                        context.startActivity(i);
                    }
                } catch (
                        JSONException e) {
                    e.printStackTrace();
                }

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
        TextView tv_name_user_search;
        TextView tv_bio_user_search;
        ImageView image_user_search;
        LinearLayout card_search;


        public ViewHolder(View item) {
            super(item);
            tv_name_user_search = (TextView) item.findViewById(R.id.tv_name_user_search);
            tv_bio_user_search = (TextView) item.findViewById(R.id.tv_bio_user_search);
            //ad_location = (TextView) item.findViewById(R.id.ad_location);
            image_user_search = (ImageView) item.findViewById(R.id.image_user_search);
            card_search = (LinearLayout) item.findViewById(R.id.card_search);


        }
    }
}
