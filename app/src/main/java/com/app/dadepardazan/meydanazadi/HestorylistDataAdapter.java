package com.app.dadepardazan.meydanazadi;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Teacher on 2017/08/05.
 */

public class HestorylistDataAdapter extends RecyclerView.Adapter<HestorylistDataAdapter.BookViewHolder> {
    Context context = null;
    ArrayList<JSONObject> data_list = null;
    public static final String ACTIVITY_PACKING_RESULT_CODE = "result_code";


    public HestorylistDataAdapter(Context context, ArrayList<JSONObject> data_list) {
        this.context = context;
        this.data_list = data_list;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       // View item = LayoutInflater.from(context).inflate(R.layout.item_hess, null);
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hess, parent, false);
        TextView hes_date;
        TextView time_ago;
        TextView hes_status;
        TextView hes_adress;
        TextView hes_price;
        TextView hes_sefarsh;
        TextView hes_shomare_tamas;
        TextView hes_name_book;
        CardView cardv;
        LinearLayout item_layout=(LinearLayout) item.findViewById(R.id.item_layout);
        hes_date = (TextView) item.findViewById(R.id.hes_date);
        hes_status = (TextView) item.findViewById(R.id.hes_status);
        hes_adress = (TextView) item.findViewById(R.id.hes_adress);
        hes_price = (TextView) item.findViewById(R.id.hes_price);
        time_ago = (TextView) item.findViewById(R.id.time_ago);
        hes_sefarsh = (TextView) item.findViewById(R.id.hes_sefarsh);
        hes_shomare_tamas = (TextView) item.findViewById(R.id.hes_shomare_tamas);
        hes_name_book = (TextView) item.findViewById(R.id.hes_name_book);
        cardv = (CardView) item.findViewById(R.id.cardv1);
        BookViewHolder viewHolder = new BookViewHolder(item);
        viewHolder.hes_date=hes_date;
        viewHolder.hes_status = hes_status;
        viewHolder.time_ago = time_ago;
        viewHolder.hes_adress=hes_adress;
        viewHolder.hes_price=hes_price;
        viewHolder.hes_sefarsh=hes_sefarsh;
        viewHolder.hes_shomare_tamas=hes_shomare_tamas;
        viewHolder.hes_name_book=hes_name_book;
        viewHolder.cardv=cardv;

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, final int position) {
        //BookEntity bookItem = daro.get(position);
        try {
            int seconds = (int) ((System.currentTimeMillis() / 1000) - data_list.get(position).getInt("date"));
            String temp = "";
            if (seconds < 60) {
                temp = "چند ثانیه پیش";


            } else if (seconds >= 60 && seconds < 3600) {

                temp = (seconds / 60) + " دقیقه پیش ";

            } else if (seconds >= 3600 && seconds < 86400) {

                temp = (seconds / 3600) + " ساعت پیش ";


            } else if (seconds >= 86400 && seconds < 2629743) {

                temp = (seconds / 86400) + " روز پیش ";


            } else if (seconds >= 2629743 && seconds < 31556926) {

                temp = (seconds / 2629743) + " ماه پیش ";


            } else {
                temp = (seconds / 31556926) + " سال پیش ";
            }
            holder.hes_date.setText("تاریخ: " + data_list.get(position).getString("date_show"));
            holder.time_ago.setText(temp);
            if (data_list.get(position).getString("number_order").toString().trim().equals("null")) {
                holder.hes_sefarsh.setVisibility(View.GONE);
            } else {
                holder.hes_sefarsh.setVisibility(View.VISIBLE);
                holder.hes_sefarsh.setText("شماره سفارش: " + data_list.get(position).getString("number_order"));
            }


            holder.hes_status.setText("وضعیت: " + data_list.get(position).getString("status_text"));
            if (data_list.get(position).getString("adress").trim().isEmpty()) {
                holder.hes_adress.setVisibility(View.GONE);
            } else {
                holder.hes_adress.setVisibility(View.VISIBLE);
                holder.hes_adress.setText("ادرس: " + data_list.get(position).getString("adress"));

            }
            NumberFormat numberformat = NumberFormat.getNumberInstance(Locale.US);
            String price_string_formatag = (numberformat.format(data_list.get(position).getInt("price_final"))) + "ریال";
            holder.hes_price.setText("قیمت: " + price_string_formatag);

            //holder.hes_price.setText("قیمت: "+ data_list.get(position).getString("price"));
            if (data_list.get(position).getInt("status") == 0) {
                Toast.makeText(this.context,"sjkahkfj",Toast.LENGTH_LONG).show();
//                holder.cardv.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext(),R.style.DialogeTheme);
//                        alertbox.setMessage("ایا می خواهید کتاب را خریداری کنید");
//                        alertbox.setTitle("پرداخت مبلغ کتاب");
//                        //alertbox.setIcon(R.drawable.trn_03);
//
//                        alertbox.setPositiveButton("بله", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                String url = null;
//                                try {
//                                    url = "http://meydane-azadi.ir/paying/pay.php?id_order="+ data_list.get(position).getString("number_order");
//
//                                    Intent i = new Intent(Intent.ACTION_VIEW);
//                                    i.setData(Uri.parse(url));
//                                    context.startActivity(i);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//                        alertbox.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        });
//                        alertbox.setNeutralButton("لغو خرید", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        });
//
//                        alertbox.show();
//                    }
              //  }
              //  );

            } else if (data_list.get(position).getInt("status") == 3) {
                // holder.relative_item.setBackgroundColor(Color.BLUE);
            }
            holder.hes_name_book.setText("نام کتاب: " + data_list.get(position).getString("name_book"));

            if (data_list.get(position).getString("telephone").trim().isEmpty()) {
                holder.hes_shomare_tamas.setVisibility(View.GONE);
            } else {
                holder.hes_shomare_tamas.setVisibility(View.VISIBLE);
                holder.hes_shomare_tamas.setText("شماره تماس: " + data_list.get(position).getString("telephone"));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void addHestoryList( JSONArray ad_list) {


        try {


            for (int i = 0; i < ad_list.length(); i++) {


                data_list.add(ad_list.getJSONObject(i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        notifyItemInserted(data_list.indexOf(ad_list));


    }

    @Override
    public int getItemCount() {
        return data_list.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView hes_date;
        TextView time_ago;
        TextView hes_status;
        TextView hes_adress;
        TextView hes_price;
        TextView hes_sefarsh;
        TextView hes_shomare_tamas;
        TextView hes_name_book;
        CardView cardv;
        LinearLayout item_layout;

        public BookViewHolder(View itemView) {
            super(itemView);

        }
    }


}
