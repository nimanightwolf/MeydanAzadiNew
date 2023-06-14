package com.app.dadepardazan.meydanazadi;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.snackbar.Snackbar;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Bamdad on 1/29/2019.
 */

public class TasviehActivity extends MainActivity {
    public static final String ACTIVITY_PACKING_RESULT_CODE = "result_code";
    TextView tas_description;
    TextView tas_emtiaz_kol;
    TextView tas_toman;
    TextView tas_doj;
    TextView tas_reple;
    EditText tas_dialog_name;
    EditText tas_dialog_cardbank;
    EditText tas_dialog_riple;
    EditText tas_dialog_doje;
    String st_tas_dialog_name;
    String st_tas_dialog_cardbank;
    String st_tas_dialog_riple;
    String st_tas_dialog_doje;
    Button tas_btn_info;
    Button call_btn_tasvieh;
    RadioButton radiobtn_doje;
    RadioButton radiobtn_reple;
    RadioButton radiobtn_toman;
    private MCrypt mcrypt;
    JSONObject data_profile;
    boolean tasvieh_is_ok = true;
    String st_ravesh_pardakht = "";
    View view;
    NestedScrollView nesscroll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasvieh);
        toolbar = (Toolbar) findViewById(R.id.toolbarshow);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TasviehActivity.this.finish();
            }
        });
        //AdListActivity.this.setTitle("میدان ازادی");

        // navigationView.getMenu().findItem(R.id.mnu_main).setChecked(true);
        BottomNavigationView.getMenu().findItem(R.id.navigation_account).setChecked(true);
//        data_profile = new String(getIntent().getStringExtra(ProFileUser.ACTIVITY_PACKING_RESULT_CODE));
        try {
            data_profile = new JSONObject(getIntent().getStringExtra("ad"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
//        Toast.makeText(getApplicationContext(),data_profile.toString(),Toast.LENGTH_SHORT).show();
        tas_description = (TextView) findViewById(R.id.tas_description);
        tas_emtiaz_kol = (TextView) findViewById(R.id.tas_emtiaz_kol);
        tas_toman = (TextView) findViewById(R.id.tas_toman);
        tas_doj = (TextView) findViewById(R.id.tas_doj);
        tas_reple = (TextView) findViewById(R.id.tas_reple);
        tas_btn_info = (Button) findViewById(R.id.tas_btn_info);
        call_btn_tasvieh = (Button) findViewById(R.id.call_btn_tasvieh);
        nesscroll=(NestedScrollView)findViewById(R.id.nesscroll);



//        nesscroll.setOnTouchListener(new OnSwipeTouchListener(TasviehActivity.this) {
//
//            public void onSwipeRight() {
//
//                // Toast.makeText(getApplicationContext(),"left to right  gesture",Toast.LENGTH_SHORT).show();
//                finish();
//                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//            }
//
//        });


        tas_btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_bankinfo_dialog();
            }
        });
        call_btn_tasvieh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!st_tas_dialog_name.isEmpty() || !st_tas_dialog_cardbank.isEmpty()) {
                    if (st_tas_dialog_name.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "لطفا شماره کارت بانکی را هم وارد کنید", Toast.LENGTH_SHORT).show();
                        show_bankinfo_dialog();
                        return;
                    }
                    if (st_tas_dialog_cardbank.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "لطفا نام صاحب شماره کارت بانکی را هم وارد کنید", Toast.LENGTH_SHORT).show();
                        show_bankinfo_dialog();
                        return;
                    }


                } else {

                    if (st_tas_dialog_riple.isEmpty() || st_tas_dialog_doje.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "لطفا حداقل یکی از فیدلدای پرداخت را پر کنید", Toast.LENGTH_SHORT).show();
                        show_bankinfo_dialog();
                        return;

                    }

                }


                show_way_tasvieh_dialog();


            }
        });

        try {
            st_tas_dialog_name = data_profile.getString("bank_name").trim();
            st_tas_dialog_cardbank = data_profile.getString("bankcard").trim();
            st_tas_dialog_riple = data_profile.getString("wallet_xrp").trim();
            st_tas_dialog_doje = data_profile.getString("wallet_doge").trim();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            tas_description.setText(data_profile.getString("description"));
            NumberFormat numberformat = NumberFormat.getNumberInstance(Locale.US);
            tas_emtiaz_kol.setText(PersianDigitConverter.PerisanNumber((data_profile.getString("points"))));
            tas_toman.setText(PersianDigitConverter.PerisanNumber(numberformat.format(data_profile.getInt("points_price"))));
            tas_doj.setText(PersianDigitConverter.PerisanNumber(numberformat.format(data_profile.getInt("dog_price"))));
            tas_reple.setText(PersianDigitConverter.PerisanNumber(numberformat.format(data_profile.getInt("xrp_price"))));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Typeface bnazanin = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        tas_toman.setTypeface(bnazanin);


    }

    public void show_bankinfo_dialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(TasviehActivity.this, R.style.DialogeTheme);
        alertDialog.setTitle("وارد کردن اطلاعات پرداخت");


//                        alertDialog.setMessage("نام دارخانه را وارد کنید و در صورت داشتن پیام , پیام خود را هم وارد کنید");

        // final EditText input = new EditText(ListBuyActivity.this);

        // view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_item_tasvieh_input_info, null);

        view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_item_tasvieh_input_info, null);

        tas_dialog_name = (EditText) view.findViewById(R.id.tas_dialog_name);
        tas_dialog_cardbank = (EditText) view.findViewById(R.id.tas_dialog_cardbank);
        tas_dialog_riple = view.findViewById(R.id.tas_dialog_riple);
        tas_dialog_doje = view.findViewById(R.id.tas_dialog_doje);

        try {
            if (!data_profile.getString("bank_name").equals("null")) {
                tas_dialog_name.setText(data_profile.getString("bank_name").trim());
            } else {
                data_profile.put("bank_name", "");
            }
            if (!data_profile.getString("bankcard").equals("null")) {
                tas_dialog_cardbank.setText(data_profile.getString("bankcard").trim());
            } else {
                data_profile.put("bankcard", "");
            }
            if (!data_profile.getString("wallet_xrp").equals("null")) {
                tas_dialog_riple.setText(data_profile.getString("wallet_xrp").trim());
            } else {
                data_profile.put("wallet_xrp", "");
            }
            if (!data_profile.getString("wallet_doge").equals("null")) {
                tas_dialog_doje.setText(data_profile.getString("wallet_doge").trim());
            } else {
                data_profile.put("wallet_doge", "");
            }

            if (view != null) alertDialog.setView(view);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //alertDialog.setView(input2);
        alertDialog.setIcon(R.drawable.logo_app);

        alertDialog.setPositiveButton("ثبت",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            data_profile.put("bank_name", tas_dialog_name.getText().toString().trim());
                            data_profile.put("bankcard", tas_dialog_cardbank.getText().toString().trim());
                            data_profile.put("wallet_xrp", tas_dialog_riple.getText().toString().trim());
                            data_profile.put("wallet_doge", tas_dialog_doje.getText().toString().trim());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (tas_dialog_name.getText().toString().isEmpty()) {

                            Toast.makeText(getApplicationContext(), "لطفا نام شماره کارت را وارد کنید", Toast.LENGTH_SHORT).show();
                            //show_sabt_sefarsh_dialog();
                            //   new send_bank_info().execute();
                        }
                        st_tas_dialog_name = tas_dialog_name.getText().toString().trim();
                        st_tas_dialog_cardbank = tas_dialog_cardbank.getText().toString().trim();
                        st_tas_dialog_riple = tas_dialog_riple.getText().toString().trim();
                        st_tas_dialog_doje = tas_dialog_doje.getText().toString().trim();
                        new send_payment_info_update().execute();
                    }


                });

        alertDialog.setNegativeButton("لغو",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog.cancel();
                    }
                });
        alertDialog.show();

    }

    public void show_way_tasvieh_dialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(TasviehActivity.this, R.style.DialogeTheme);
        alertDialog.setTitle("انتخاب روش پرداخت");


//                        alertDialog.setMessage("نام دارخانه را وارد کنید و در صورت داشتن پیام , پیام خود را هم وارد کنید");

        // final EditText input = new EditText(ListBuyActivity.this);

        View view2 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_item_pay_tasvieh, null);

        radiobtn_toman = (RadioButton) view2.findViewById(R.id.radiobtn_toman);
        radiobtn_reple = (RadioButton) view2.findViewById(R.id.radiobtn_reple);
        radiobtn_doje = (RadioButton) view2.findViewById(R.id.radiobtn_doje);
        try {
            if (data_profile.getString("card").equals("1")){
                radiobtn_toman.setVisibility(View.VISIBLE);
            }else {
                radiobtn_toman.setVisibility(View.GONE);
            }
            if (data_profile.getString("dog").equals("1")){
                radiobtn_doje.setVisibility(View.VISIBLE);
            }else {
                radiobtn_doje.setVisibility(View.GONE);
            }
            if (data_profile.getString("xrp").equals("1")){
                radiobtn_reple.setVisibility(View.VISIBLE);
            }else {
                radiobtn_reple.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        alertDialog.setView(view2);
        radiobtn_doje.setChecked(true);
        if (radiobtn_doje.isChecked()) {
            st_ravesh_pardakht = "doje";
        }

        radiobtn_doje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(), "khiiir", Toast.LENGTH_LONG).show();
                st_ravesh_pardakht = "doje";
            }
        });
        radiobtn_reple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(), "balleh", Toast.LENGTH_LONG).show();
                st_ravesh_pardakht = "reple";

            }

        });
        radiobtn_toman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(), "balleh", Toast.LENGTH_LONG).show();
                st_ravesh_pardakht = "toman";

            }

        });
        alertDialog.setIcon(R.drawable.logo_app);

        alertDialog.setPositiveButton("ثبت",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                           try {
                               if (!data_profile.getString("dog").equals("1")&!data_profile.getString("card").equals("1")&!data_profile.getString("xrp").equals("1")){
                                   tasvieh_is_ok=false;
                               }else{
                                   tasvieh_is_ok=true;
                               }
                           } catch (JSONException e) {
                               e.printStackTrace();
                           }

                        if (tasvieh_is_ok) {
                           new send_payment_request().execute();

                        } else {
                            Snackbar.make(call_btn_tasvieh,"موجودی شما به نصاب نرسیده است", Snackbar.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(), "موجودی شما به نصاب نرسیده است", Toast.LENGTH_SHORT).show();


                        }
                    }


                });

        alertDialog.setNegativeButton("لغو",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();

    }


    public class send_payment_info_update extends AsyncTask<Void, Void, String> {
        ProgressDialog pd = new ProgressDialog(TasviehActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("در حال دریافت اطلاعات");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
            final JSONObject get_ad_list = new JSONObject();
            mcrypt = new MCrypt();
            try {

                get_ad_list.put("command", MCrypt.bytesToHex(mcrypt.encrypt("payment_info_update")));
                //get_coment_list.put("category_filter", settings.getInt("category_filter", 0));

                get_ad_list.put("user_id", MCrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));


                get_ad_list.put("bankcard", MCrypt.bytesToHex(mcrypt.encrypt(st_tas_dialog_cardbank)));
                get_ad_list.put("wallet_xrp", MCrypt.bytesToHex(mcrypt.encrypt(st_tas_dialog_riple)));
                get_ad_list.put("wallet_doge", MCrypt.bytesToHex(mcrypt.encrypt(st_tas_dialog_doje)));
                String bank_name = Base64.encodeToString(st_tas_dialog_name.getBytes("UTF-8"), Base64.DEFAULT);

                get_ad_list.put("bank_name", MCrypt.bytesToHex(mcrypt.encrypt(bank_name)));

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Toast.makeText(getBaseContext(), get_ad_list.toString(), Toast.LENGTH_SHORT).show();
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(getBaseContext(), get_ad_list.toString(), Toast.LENGTH_SHORT).show();
//                }
//            });
            namevaluepairs.add(new BasicNameValuePair("myjson", get_ad_list.toString()));


            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://meydane-azadi.ir/api/mwwap/ecxa.php");
                httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs, HTTP.UTF_8));

                HttpResponse httpresponse = httpclient.execute(httppost);

                String response = EntityUtils.toString(httpresponse.getEntity());
//                final String finalResponse1 = response;
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//
//                        Toast.makeText(getBaseContext(), finalResponse1, Toast.LENGTH_SHORT).show();
//
//                    }
//                });

                if (response.startsWith("<azadi>") && response.endsWith("</azadi>")) {//response is valid

                    response = response.replace("<azadi>", "").replace("</azadi>", "");
                    response = new String(mcrypt.decrypt(response)).trim();


                    final String finalResponse = response;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           // Toast.makeText(getBaseContext(), finalResponse, Toast.LENGTH_SHORT).show();
                            if (finalResponse.trim().equals("\"oky\"")) {
                               // Toast.makeText(getApplicationContext(), "اطلاعات با موفقیت ارسال شد", Toast.LENGTH_SHORT).show();
                                Snackbar.make(call_btn_tasvieh, "اطلاعات با موفقیت ارسال شد", Snackbar.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "خطا در ارسال اطلاعات!", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });



                } else {


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(getBaseContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();

                        }
                    });


                }


            } catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(getBaseContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();

                    }
                });


            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.hide();
            pd.dismiss();
        }
    }
    public class send_payment_request extends AsyncTask<Void, Void, String> {
        ProgressDialog pd = new ProgressDialog(TasviehActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("در حال دریافت اطلاعات");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
            final JSONObject get_ad_list = new JSONObject();
            mcrypt = new MCrypt();
            try {

                get_ad_list.put("command", MCrypt.bytesToHex(mcrypt.encrypt("payment_request")));
                //get_coment_list.put("category_filter", settings.getInt("category_filter", 0));

                get_ad_list.put("user_id", MCrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));
                get_ad_list.put("way", MCrypt.bytesToHex(mcrypt.encrypt(st_ravesh_pardakht)));



            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Toast.makeText(getBaseContext(), get_ad_list.toString(), Toast.LENGTH_SHORT).show();
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(getBaseContext(), get_ad_list.toString(), Toast.LENGTH_SHORT).show();
//                }
//            });
            namevaluepairs.add(new BasicNameValuePair("myjson", get_ad_list.toString()));


            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://meydane-azadi.ir/api/mwwap/ecxa.php");
                httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs, HTTP.UTF_8));

                HttpResponse httpresponse = httpclient.execute(httppost);

                String response = EntityUtils.toString(httpresponse.getEntity());
//                final String finalResponse1 = response;
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//
//                        Toast.makeText(getBaseContext(), finalResponse1, Toast.LENGTH_SHORT).show();
//
//                    }
//                });

                if (response.startsWith("<azadi>") && response.endsWith("</azadi>")) {//response is valid

                    response = response.replace("<azadi>", "").replace("</azadi>", "");
                    response = new String(mcrypt.decrypt(response)).trim();


                    final String finalResponse = response;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          //  Toast.makeText(getBaseContext(), finalResponse, Toast.LENGTH_SHORT).show();
                            if (finalResponse.trim().equals("\"ok\"")) {
                                //Toast.makeText(getApplicationContext(), "در خواست شما  با موفقیت ارسال شد", Toast.LENGTH_SHORT).show();
                                Snackbar.make(call_btn_tasvieh,"در خواست شما  با موفقیت ارسال شد", Snackbar.LENGTH_LONG).show();
                            } else if(finalResponse.trim().equals("\"2\"")) {
                                Snackbar.make(call_btn_tasvieh,"شما یک در خواست در حال اقدام دارید! ", Snackbar.LENGTH_LONG).show();

                            }else{


                                Toast.makeText(getApplicationContext(), "خطا در ارسال اطلاعات!", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });



                } else {


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(getBaseContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();

                        }
                    });


                }


            } catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(getBaseContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();

                    }
                });


            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.hide();
            pd.dismiss();
        }
    }

}
