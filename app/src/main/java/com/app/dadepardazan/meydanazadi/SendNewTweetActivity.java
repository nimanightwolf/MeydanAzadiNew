package com.app.dadepardazan.meydanazadi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.dadepardazan.meydanazadi.all_tweet.AllTweetActivity;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.hendraanggrian.widget.SocialEditText;
import com.theartofdev.edmodo.cropper.CropImage;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class SendNewTweetActivity extends MainActivity {
    MCrypt mcrypt;
    Uri uri;
    Uri resutlUri;
    String str_android_id;
    String str_repost_id = "0";
    ImageAdapter2 adapter;
    String image_base64;

    RecyclerView recyclerview;
    ImageView image_camera;
    ImageView image_gallery;
    SocialEditText et_text_tweet;
    EditText et_text_tweet_v21;
    CardView card_insert_tweet;
    ImageView image_cancel;
    int i = 0;

    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;
    private String TYPE_PERMISION = Manifest.permission.CAMERA;
    public static final String ACTIVITY_PACKING_RESULT_CODE = "result_code";
    public static final int RESULT_NEW_POST = 592;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_new_tweet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle("  "+getResources().getString(R.string.logo_type_meydanazadi)+"  ");
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/IRANSans_Bold.ttf");
        toolBarLayout.setCollapsedTitleTypeface(typeface);
        toolBarLayout.setExpandedTitleTypeface(typeface);
        toolBarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.red_toolbar));
        toolBarLayout.setExpandedTitleColor(getResources().getColor(R.color.red_toolbar));
        findviewbyid();
        show_dialog_permission();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview.setLayoutManager(layoutManager);
        final ArrayList<JSONObject> data_list = new ArrayList<JSONObject>();
        adapter = new ImageAdapter2(this, data_list);
        recyclerview.setAdapter(adapter);
        recyclerview.setHasFixedSize(false);

        str_android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);


        image_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity(uri).setAspectRatio(16, 9).setRequestedSize(512, 512).start(SendNewTweetActivity.this);
            }
        });
        image_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gallery
                Intent galerry_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(galerry_intent, "لطفا یک عکس را انتخاب کنید"), 2);
            }
        });
        card_insert_tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.LOLLIPOP) {
                    // Do something for lollipop and above versions
                    if (!et_text_tweet_v21.getText().toString().isEmpty() | adapter.getItemCount() != 0) {
                        new insert_post().execute();
                    } else {

                    }
                } else {
                    if (!et_text_tweet.getText().toString().isEmpty() | adapter.getItemCount() != 0) {
                        new insert_post().execute();
                    } else {

                    }
                }

            }
        });
        image_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void findviewbyid() {
        recyclerview = findViewById(R.id.recyclerview);
        image_gallery = findViewById(R.id.image_gallery);
        image_camera = findViewById(R.id.image_camera);
        if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.LOLLIPOP) {
            // Do something for lollipop and above versions
            et_text_tweet_v21 = findViewById(R.id.et_text_tweet);
        } else {
            et_text_tweet = findViewById(R.id.et_text_tweet);
        }

        card_insert_tweet = findViewById(R.id.card_insert_tweet);
        image_cancel = findViewById(R.id.image_cancel);

    }

    private void show_dialog_permission() {
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        if (ActivityCompat.checkSelfPermission(SendNewTweetActivity.this, TYPE_PERMISION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SendNewTweetActivity.this, TYPE_PERMISION)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(SendNewTweetActivity.this, R.style.DialogeTheme);
                builder.setTitle("درخواست مجوز");
                builder.setMessage("برای استفاده از این نرم افزار نیاز به مجوز دوربین است\n بر روی تایید بزنید ");
                builder.setPositiveButton("تایید", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(SendNewTweetActivity.this, new String[]{TYPE_PERMISION}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                    }
                });
                builder.setNegativeButton("خروج", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
                builder.setCancelable(false);
                builder.show();
            } else if (permissionStatus.getBoolean(TYPE_PERMISION, false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(SendNewTweetActivity.this, R.style.DialogeTheme);
                builder.setTitle("درخواست مجوز");
                builder.setMessage("برای استفاده از این نرم افزار نیاز به مجوز دوربین است\n بر روی تایید بزنید ");
                builder.setPositiveButton("تایید", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "رفتن به تنظیمات برای دادن مجوز دوربین", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("خروج", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
                builder.setCancelable(false);
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(SendNewTweetActivity.this, new String[]{TYPE_PERMISION}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(TYPE_PERMISION, true);
            editor.commit();


        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                proceedAfterPermission();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(SendNewTweetActivity.this, TYPE_PERMISION)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(SendNewTweetActivity.this, R.style.DialogeTheme);
                    builder.setTitle("درخواست مجوز");
                    builder.setMessage("برای استفاده از این نرم افزار نیاز به مجوز دوربین است\n بر روی تایید بزنید ");
                    builder.setPositiveButton("تایید", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                            ActivityCompat.requestPermissions(SendNewTweetActivity.this, new String[]{TYPE_PERMISION}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);

                        }
                    });
                    builder.setNegativeButton("خروج", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                } else {
                    Toast.makeText(getBaseContext(), "قادر به دریافت مجوز نیستیم", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(SendNewTweetActivity.this, TYPE_PERMISION) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    private void proceedAfterPermission() {
        //We've got the permission, now we can proceed further

        // Toast.makeText(getBaseContext(), "We got the Storage Permission", Toast.LENGTH_LONG).show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(SendNewTweetActivity.this, TYPE_PERMISION) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
        if (requestCode == 1 && resultCode == RESULT_OK) {//camera
            //  CropImage.activity(uri).setAspectRatio(1,1).setRequestedSize(512,512).start(this);
        } else if (requestCode == 2 && resultCode == RESULT_OK) {//gallery
            uri = data.getData();
            CropImage.activity(uri).setAspectRatio(16, 9).setRequestedSize(512, 512).start(this);
            uri = null;
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            resutlUri = result.getUri();
            //images.get(current_image).setImageURI(resutlUri);

            BitmapDrawable bd = null;

            try {
                InputStream inputStream = getContentResolver().openInputStream(resutlUri);
                bd = (BitmapDrawable) Drawable.createFromStream(inputStream, resutlUri.toString());
            } catch (FileNotFoundException e) {
                // yourDrawable = getResources().getDrawable(R.drawable.default_image);
            }
            // BitmapDrawable bd = ((BitmapDrawable) images.get(current_image).getDrawable());
            Bitmap bm = bd.getBitmap();
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, bao);

            image_base64 = Base64.encodeToString(bao.toByteArray(), Base64.DEFAULT);
            new upload_image().execute();


        }


        super.onActivityResult(requestCode, resultCode, data);
    }


    public class ImageAdapter2 extends RecyclerView.Adapter<ImageAdapter2.ViewHolder> {
        ArrayList<JSONObject> data_list;
        Context context;

        public ImageAdapter2(Context context, ArrayList<JSONObject> data_list) {
            this.context = context;
            this.data_list = data_list;
        }

        @Override
        public ImageAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_send_new_tweet, parent, false);
            return new ImageAdapter2.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ImageAdapter2.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {


            try {
                holder.image_item.setImageURI((Uri) data_list.get(position).get("image"));


                holder.image_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delete_item(data_list.get(position), position);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {

            }
        }

        @Override
        public int getItemCount() {
            return data_list.size();
        }

        public ArrayList<JSONObject> getItem() {
            return data_list;
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

        public void delete_item(JSONObject job, int posi) {
            try {
                data_list.remove(job);
                notifyItemRemoved(posi);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();

            }

        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            ImageView image_delete;
            ImageView image_item;


            public ViewHolder(View item) {
                super(item);
                image_item = item.findViewById(R.id.image_item);
                image_delete = (ImageView) item.findViewById(R.id.image_delete);


            }
        }
    }

    public class upload_image extends AsyncTask<Void, Void, String> {

        ProgressDialog pd = new ProgressDialog(SendNewTweetActivity.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("لطفا منتظر بمانید");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... Void) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();

            final JSONObject get_ad_list = new JSONObject();

            // mcrypt = new MCrypt();
            mcrypt = new MainActivity.MCrypt();
            try {

                get_ad_list.put("command", mcrypt.bytesToHex(mcrypt.encrypt("upload_media")));
                //get_coment_list.put("category_filter", settings.getInt("category_filter", 0));
                get_ad_list.put("user_id", mcrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));

                get_ad_list.put("image", image_base64);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            namevaluepairs.add(new BasicNameValuePair("myjson", get_ad_list.toString()));
            Log.e("image send:", get_ad_list.toString());

//            namevaluepairs.add(new BasicNameValuePair("image", image_base64));


            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://meydane-azadi.ir/api/mwwap/upload_media.php");
                httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs));

                HttpResponse httpresponse = httpclient.execute(httppost);

                String response = EntityUtils.toString(httpresponse.getEntity());

                Log.e("data get", response);
                if (response.startsWith("<azadi>") && response.endsWith("</azadi>")) {//response is valid

                    response = response.replace("<azadi>", "").replace("</azadi>", "");
                    final String finalResponse = response;
                    //response=new String( mcrypt.decrypt(response)).trim();


                    if (!response.trim().equals("0")) {//upload ok

                        //   address_images[current_image] = response.trim();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                try {
                                    JSONArray jarr_send = new JSONArray();
                                    JSONObject job_send = new JSONObject();
                                    job_send.put("id", i + 1);
                                    job_send.put("image", resutlUri);
                                    job_send.put("image_name", finalResponse);
                                    jarr_send.put(job_send);
                                    Log.e("data adapter", jarr_send.toString());

                                    adapter.insert(adapter.getItemCount(), jarr_send);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });


                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                Toast.makeText(getBaseContext(), "خطا در اپلود تصویر", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }

                } else {//error

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(getBaseContext(), "خطا در اپلود تصویر", Toast.LENGTH_SHORT).show();

                        }
                    });


                }


            } catch (
                    Exception e) {
                e.printStackTrace();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "خطا در اپلود تصویر", Toast.LENGTH_SHORT).show();

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

    public class insert_post extends AsyncTask<Void, Void, String> {
        ProgressDialog pd = new ProgressDialog(SendNewTweetActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("در حال دریافت اطلاعات");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
            final JSONObject get_ad_list = new JSONObject();
            mcrypt = new MainActivity.MCrypt();
            try {

                get_ad_list.put("command", mcrypt.bytesToHex(mcrypt.encrypt("insert_post")));
                //get_coment_list.put("category_filter", settings.getInt("category_filter", 0));
                get_ad_list.put("user_id", mcrypt.bytesToHex(mcrypt.encrypt(String.valueOf(settings.getInt("user_id", 0)))));

                if (android.os.Build.VERSION.SDK_INT== android.os.Build.VERSION_CODES.LOLLIPOP) {
                    // Do something for lollipop and above versions
                    get_ad_list.put("text", mcrypt.bytesToHex(mcrypt.encrypt(Base64.encodeToString(et_text_tweet_v21.getText().toString().getBytes("UTF-8"), Base64.DEFAULT))));
                }else{
                    get_ad_list.put("text", mcrypt.bytesToHex(mcrypt.encrypt(Base64.encodeToString(et_text_tweet.getText().toString().getBytes("UTF-8"), Base64.DEFAULT))));
                }

                JSONArray jarr_image_name = new JSONArray();
                String str_image_name = "";
                for (int i = 0; i < adapter.getItem().size(); i++) {
                    str_image_name = str_image_name + adapter.getItem().get(i).getString("image_name") + "-";

                }
                get_ad_list.put("image", str_image_name);
                get_ad_list.put("android_id", mcrypt.bytesToHex(mcrypt.encrypt(str_android_id)));
                get_ad_list.put("repost_id", mcrypt.bytesToHex(mcrypt.encrypt(str_repost_id)));


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


            namevaluepairs.add(new BasicNameValuePair("myjson", get_ad_list.toString()));
            Log.e("data sended", get_ad_list.toString());

            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://meydane-azadi.ir/api/mwwap/ecxa.php");
                httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs, HTTP.UTF_8));

                HttpResponse httpresponse = httpclient.execute(httppost);

                String response = EntityUtils.toString(httpresponse.getEntity());

                Log.e("data respose with code", response);
                if (response.startsWith("<azadi>") && response.endsWith("</azadi>")) {//response is valid

                    response = response.replace("<azadi>", "").replace("</azadi>", "").replace("\"", "");
                    response = new String(mcrypt.decrypt(response)).trim();
                    response = response.replace("\"", "");
                    Log.e("data respose", response);


                    final String finalResponse = response;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (finalResponse.equals("ok")) {
                                Toast.makeText(SendNewTweetActivity.this, "اطلاعات شما با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(SendNewTweetActivity.this, AllTweetActivity.class);
                                setResult(RESULT_OK);
                                startActivity(i);
                                finish();
                            }
                            // jarr_tag = job_all_data.getJSONArray("tag");


                        }
                    });

                } else {


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(SendNewTweetActivity.this, "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    });


                }


            } catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(SendNewTweetActivity.this, "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                        finish();

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