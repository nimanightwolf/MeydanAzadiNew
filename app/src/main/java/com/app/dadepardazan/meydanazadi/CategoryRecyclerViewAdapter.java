package com.app.dadepardazan.meydanazadi;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Teacher on 2017/08/05.
 */

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.BookViewHolder> {
    Context cntx = null;
    ArrayList<JSONObject> daro = null;
    public static final String ACTIVITY_PACKING_RESULT_CODE = "result_code";


    public CategoryRecyclerViewAdapter(Context cntx, ArrayList<JSONObject> daro) {
        this.cntx = cntx;
        this.daro = daro;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(cntx).inflate(R.layout.item_cat, null);
        TextView tv_item_cat = (TextView) root.findViewById(R.id.tv_item_cat);
        LinearLayout liner_item = (LinearLayout) root.findViewById(R.id.liner_items);

        BookViewHolder viewHolder = new BookViewHolder(root);
        viewHolder.tv_item_cat = tv_item_cat;
        viewHolder.liner_item=liner_item;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, final int position) {
        //BookEntity bookItem = daro.get(position);

//        holder.liner_item.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent i=new Intent(cntx,AdListActivity.class);
////                try {
////                    i.putExtra(ACTIVITY_PACKING_RESULT_CODE,daro.get(position).getString("title"));
////                } catch (JSONException e) {
////                    e.printStackTrace();
////                }
////                cntx.startActivity(i);
//
//            }
//        });
        try {
            holder.tv_item_cat.setText(daro.get(position).getString("title"));
           // holder.tv_item_cat.setTextColor(Color.BLUE);
          //  holder.tv_item_cat.setText(daro.get(position).getString("title"));


        } catch (JSONException e) {
        e.printStackTrace();
    }

    }

    @Override
    public int getItemCount() {
        return daro.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_item_cat;
        public LinearLayout liner_item;

        public BookViewHolder(View itemView) {
            super(itemView);

        }
    }


}
