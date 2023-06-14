package com.app.dadepardazan.meydanazadi;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.dadepardazan.meydanazadi.login.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

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
 * Created by Bamdad on 12/6/2017.
 */

public class BookActivity extends MainActivity {

    JSONObject ad;
    TextView book_name;
    TextView book_description;
    TextView book_name_writer;
    TextView book_publisher;
    TextView book_gheteh;
    TextView book_number_Paper;
    com.app.dadepardazan.meydanazadi.SeparatorTextView book_price;
    TextView book_price_off;
    Button book_buy;
    ImageView book_image;



    Menu menu;
    JSONObject user_contact_details;
    String slug;


    RecyclerView recyclerView;
    ComentShowAdapter adapter;
    int last_ad_id = 0;

    private static final int PERMISSION_CALLBACK_CONSTANT = 101;
    private static final int REQUEST_PERMISSION_SETTING = 102;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    String IMEI_Number_Holder;
    TelephonyManager telephonyManager;
    TextView toolbar_title;

    private MCrypt mcrypt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout content_frame;

//        content_frame = (FrameLayout) findViewById(R.id.content_frame);
//        getLayoutInflater().inflate(R.layout.show_home, content_frame);
        setContentView(R.layout.book);
        toolbar = (Toolbar) findViewById(R.id.toolbarbook);
        setSupportActionBar(toolbar);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("خرید کتاب");

        book_buy = (Button) findViewById(R.id.book_buy);
        book_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), BuyingBookActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                  i.putExtra("ad", ad.toString());

                startActivity(i);
            }
        });

        BottomNavigationView = (com.google.android.material.bottomnavigation.BottomNavigationView) findViewById(R.id.navigationView);
        BottomNavigationView.setOnNavigationItemSelectedListener(new com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id = item.getItemId()) {
                    case R.id.navigation_today_news:
                        //Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), TelenewsActivity.class);
                        startActivity(i);


                        break;
//                    case R.id.navigation_category:
//                        // Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_SHORT).show();
//                        i = new Intent(getApplicationContext(), AdListActivity.class);
//                        i.putExtra(ACTIVITY_PACKING_RESULT_CODE, "");
//                        startActivity(i);
//
//                        break;
                    case R.id.navigation_home:
                        i = new Intent(getApplicationContext(), AdListActivity.class);
                        startActivity(i);
                        break;
                    case R.id.navigation_account:
                        if (settings.getInt("user_id", 0) != 0) {

                            i = new Intent(getApplicationContext(), ProFileUser.class);
                            startActivity(i);


                        } else {
                            i = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(i);


                        }
                        break;
//                    case R.id.navigation_book:
//                        // Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_SHORT).show();
//                        i = new Intent(getApplicationContext(), BookActivity.class);
//                        startActivity(i);
//
//                        break;
                }

                return true;
            }
        });
        //// TODO: uncomment
        //BottomNavigationView.getMenu().findItem(R.id.navigation_book).setChecked(true);
        book_name = (TextView) findViewById(R.id.book_name);
        book_description = (TextView) findViewById(R.id.book_description);
        book_name_writer = (TextView) findViewById(R.id.book_name_writer);
        book_publisher = (TextView) findViewById(R.id.book_publisher);
        book_number_Paper = (TextView) findViewById(R.id.book_number_Paper);
        book_gheteh = (TextView) findViewById(R.id.book_gheteh);
        book_price= (SeparatorTextView) findViewById(R.id.book_price);
        book_price_off= (TextView) findViewById(R.id.book_price_off);
        book_image= (ImageView) findViewById(R.id.book_image);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Drawable arrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        // getSupportActionBar().setHomeAsUpIndicator(arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookActivity.this.finish();
            }
        });


        FloatingActionButton call_bt = (FloatingActionButton) findViewById(R.id.call_bt);
        call_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //   new get_contact_details().execute();

            }
        });


        new get_book_details().execute();
        proceedAfterPermission();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        //BottomNavigationView.getMenu().findItem(R.id.navigation_book).setChecked(true);
        //// TODO: uncomment
    }



//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
//            //check if all permissions are granted
//            boolean allgranted = false;
//            for (int i = 0; i < grantResults.length; i++) {
//                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
//                    allgranted = true;
//                } else {
//                    allgranted = false;
//                    break;
//                }
//            }
//
//            if (allgranted) {
//                proceedAfterPermission();
//            } else if (ActivityCompat.shouldShowRequestPermissionRationale(ShowHomeActivity.this, permissionsRequired[0])) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(ShowHomeActivity.this);
//                builder.setTitle("درخواست مجوز ");
//                builder.setMessage("این نرم افزار نیاز به مجوز تماس دارد تایید را بزنید.");
//                builder.setPositiveButton("تایید", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                        ActivityCompat.requestPermissions(ShowHomeActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
//                    }
//                });
//                builder.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//                builder.show();
//            } else {
//                Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
//            }
//        }
//    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_PERMISSION_SETTING) {
//            if (ActivityCompat.checkSelfPermission(ShowHomeActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
//                //Got Permission
//                proceedAfterPermission();
//            }
//        }
//    }

    //    private void proceedAfterPermission() {
//
//        // Toast.makeText(getBaseContext(), "همه ی مجوز ها گرفته شد", Toast.LENGTH_LONG).show();
//        try {
//            Intent callintent = new Intent(Intent.ACTION_CALL);
//            callintent.setData(Uri.parse("tel:" + user_contact_details.getString("mobile")));
//            startActivity(callintent);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        if (sentToSettings) {
//            if (ActivityCompat.checkSelfPermission(ShowHomeActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
//                //Got Permission
//                proceedAfterPermission();
//            }
//        }
//    }
    public class get_book_details extends AsyncTask<Void, Void, String> {
        ProgressDialog pd = new ProgressDialog(BookActivity.this);

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

                get_ad_list.put("command", MCrypt.bytesToHex(mcrypt.encrypt("book_gt")));


                //get_ad_list.put("location","");
                //get_coment_list.put("category_filter", settings.getInt("category_filter", 0));

                get_ad_list.put("user_id", MCrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


            namevaluepairs.add(new BasicNameValuePair("myjson", get_ad_list.toString()));


            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://meydane-azadi.ir/api/mwwap/ecxa.php");
                httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs, HTTP.UTF_8));

                HttpResponse httpresponse = httpclient.execute(httppost);

                String response = EntityUtils.toString(httpresponse.getEntity());


                if (response.startsWith("<azadi>") && response.endsWith("</azadi>")) {//response is valid

                    response = response.replace("<azadi>", "").replace("</azadi>", "");
                    response = new String(mcrypt.decrypt(response)).trim();
                    final String finalResponse = response;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                NumberFormat numberformat = NumberFormat.getNumberInstance(Locale.US);
                                ad = new JSONObject(finalResponse);
                               // Toast.makeText(getBaseContext(), ad.toString(), Toast.LENGTH_SHORT).show();
                                book_name.setText(ad.getString("name"));
                                book_name_writer.setText("نویسنده: "+ad.getString("author"));
                                book_price.setText(PersianDigitConverter.PerisanNumber(numberformat.format(ad.getInt("price"))));

                                book_price_off.setText(PersianDigitConverter.PerisanNumber(numberformat.format(ad.getInt("price_off")))+" تومان  ");
                                book_publisher.setText("ناشر: \n"+ad.getString("publisher"));
                                book_number_Paper.setText("تعداد صفحات: \n"+PersianDigitConverter.PerisanNumber(ad.getString("papers")));
                                book_gheteh.setText("قطع: \n"+ad.getString("format"));
                                book_description.setText(ad.getString("text"));
                                Picasso.get().load("http://meydane-azadi.ir/photo/photo.php?image_name=" +ad.getString("image")).into(book_image);

                            } catch (JSONException e) {
                                e.printStackTrace();
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



    public void get_permistion() {
        permissionStatus = BookActivity.this.getSharedPreferences("permissionStatus", BookActivity.this.MODE_PRIVATE);

        if (ActivityCompat.checkSelfPermission(BookActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(BookActivity.this, Manifest.permission.READ_PHONE_STATE)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(BookActivity.this, R.style.DialogeTheme);
                builder.setTitle("در خواست مجوز");
                builder.setMessage("این نرم افزار برای ارسال کامنت نیاز به این مجوز دارد.");
                builder.setPositiveButton("قبول کردن", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_CALLBACK_CONSTANT);
                        }
                    }
                });
                builder.setNegativeButton("لغو", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            } else if (permissionStatus.getBoolean(Manifest.permission.READ_PHONE_STATE, false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(BookActivity.this, R.style.DialogeTheme);
                builder.setTitle("در خواست مجوز");
                builder.setMessage("این نرم افزار برای ارسال کامنت نیاز به این مجوز دارد.");
                builder.setPositiveButton("قبول کردن", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", BookActivity.this.getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(BookActivity.this, "رفتن به تنظیمات نرم افزار", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("لغو", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_CALLBACK_CONSTANT);
                }
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.READ_PHONE_STATE, true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(BookActivity.this, Manifest.permission.READ_PHONE_STATE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BookActivity.this, R.style.DialogeTheme);
                builder.setTitle("در خواست مجوز");
                builder.setMessage("این نرم افزار برای ارسال کامنت نیاز به این مجوز دارد");
                builder.setPositiveButton("قبول کردن", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_CALLBACK_CONSTANT);
                        }
                    }
                });
                builder.setNegativeButton("لغو", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(BookActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(BookActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }


    private void proceedAfterPermission() {
        //We've got the permission, now we can proceed further
        telephonyManager = (TelephonyManager) this.getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        //IMEI_Number_Holder = telephonyManager.getDeviceId();
         IMEI_Number_Holder = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
       // Toast.makeText(getApplicationContext(),IMEI_Number_Holder,Toast.LENGTH_LONG).show();


    }


}