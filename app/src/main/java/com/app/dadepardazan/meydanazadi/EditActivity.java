package com.app.dadepardazan.meydanazadi;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/**
 * Created by Bamdad on 12/6/2017.
 */

public class EditActivity extends MainActivity {


    Spinner spinner_province;
    Spinner spinner_city;
    Spinner spinner_category;

    Spinner spinner_price_type;
    TextInputLayout price_layout;
    EditText price_text;

    TextInputLayout title_layout;
    EditText title_text;


    TextInputLayout district_layout;
    EditText district_text;


    TextInputLayout description_layout;
    EditText description_text;

    String image_base64;
    String price;

    JSONObject new_ad;
    JSONObject delto;
    JSONObject ad;





    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    private static final int CAMERA_REQUEST = 1888;

    int per=0;
















    ArrayList<ImageView> images = new ArrayList<ImageView>();
    int current_image;
    Uri uri;
    Button submit_bt;
    Boolean[] fill_image = new Boolean[3];
    Boolean[] edit_image = new Boolean[3];

    String[] address_images = new String[3];


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout content_frame;
        content_frame = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.edit, content_frame);

        try {
            ad = new JSONObject(getIntent().getStringExtra("ad"));
           // Toast.makeText(getApplicationContext(), ad.getString("title"), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        toolbar.setTitle("ویرایش اگهی ");




        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);



        spinner_province = (Spinner) findViewById(R.id.edit_spinner_province);
        spinner_city = (Spinner) findViewById(R.id.edit_spinner_city);
        spinner_category = (Spinner) findViewById(R.id.edit_spinner_category);
        spinner_price_type = (Spinner) findViewById(R.id.edit_spinner_price_type);


        price_layout = (TextInputLayout) findViewById(R.id.edit_price_layout);
        price_text = (EditText) findViewById(R.id.edit_price_text);
        price_text.addTextChangedListener(onTextChangedListener());


        title_text = (EditText) findViewById(R.id.edit_title_text);
        title_layout = (TextInputLayout) findViewById(R.id.edit_title_layout);


        district_layout = (TextInputLayout) findViewById(R.id.edit_district_layout);
        district_text = (EditText) findViewById(R.id.edit_district_text);


        description_layout = (TextInputLayout) findViewById(R.id.edit_description_layout);
        description_text = (EditText) findViewById(R.id.edit_description_text);


        images.add((ImageView) findViewById(R.id.edit_image1));
        images.add((ImageView) findViewById(R.id.edit_image2));
        images.add((ImageView) findViewById(R.id.edit_image3));


        try {
            title_text.setText(ad.getString("title"));
            description_text.setText(ad.getString("description"));
            district_text.setText(ad.getString("district"));
            price_text.setText(ad.getString("price"));


        } catch (JSONException e) {
            e.printStackTrace();
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // Drawable arrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
       // getSupportActionBar().setHomeAsUpIndicator(arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditActivity.this.finish();
            }
        });
        Arrays.fill(edit_image, false);
        Arrays.fill(fill_image, false);
        Arrays.fill(address_images,"");
        try {
            if(!ad.getString("image1").equals("")) {
                current_image=0;
                Glide.with(this).load("http://ghafas.net/photoads/" + ad.getString("image1").trim()).into(images.get(0));
                fill_image[current_image]=true;



            }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        try {
            if(!ad.getString("image2").equals("")) {
                current_image=1;
                Glide.with(this).load("http://ghafas.net/photoads/" + ad.getString("image2").trim()).into(images.get(1));






                fill_image[current_image]=true;

            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        try {
            if (!ad.getString("image3").equals("")) {
                current_image=2;
            Glide.with(this).load("http://ghafas.net/photoads/" + ad.getString("image3").trim()).into(images.get(2));


                fill_image[current_image]=true;





        }

        } catch (JSONException e) {
            e.printStackTrace();
        }





/*

        Bitmap bm = null;
        bm = BitmapFactory.decodeResource(getResources(), R.drawable.select_image);
        int width = bm.getWidth();
        int height = bm.getHeight();
        int newWidth = 80;
        int newHeight = 65;
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);

        BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);

        images.get(2).setImageDrawable(bmd);


*/






/*


         class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
            ImageView bmImage;

            public DownloadImageTask(ImageView bmImage) {
                this.bmImage = bmImage;
            }

            protected Bitmap doInBackground(String... urls) {
                String urldisplay = urls[0];
                Bitmap mIcon11 = null;
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mIcon11 = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                return mIcon11;
            }

            protected void onPostExecute(Bitmap result) {
                bmImage.setImageBitmap(result);
            }
        }
        try {
            new DownloadImageTask(images.get(0)).execute("http://birdbaz.ir/app/" + ad.getString("image1"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Drawable d = images.get(0).getDrawable();
         d=images.get(0).getDrawable();;
        //Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.select_image);
        Bitmap bm = ((BitmapDrawable) d).getBitmap();
        int width = bm.getWidth();
        int height = bm.getHeight();
        int newWidth = 150;
        int newHeight = 150;
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);

        BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);




        try {
            new DownloadImageTask(images.get(0)).execute("http://birdbaz.ir/app/" + ad.getString("image1"));
             d = images.get(0).getDrawable();
            images.get(1).setImageDrawable(bmd);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        */


        ArrayAdapter<CharSequence> myadapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.province, R.layout.row);
        spinner_province.setAdapter(myadapter);
        try {
            spinner_province.setSelection(ad.getInt("province"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        spinner_province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<CharSequence> myadapter = ArrayAdapter.createFromResource(getApplicationContext(), getResources().getIdentifier("array/city" + spinner_province.getSelectedItemPosition(), null, getApplicationContext().getPackageName()), R.layout.row);
                spinner_city.setAdapter(myadapter);
                try {
                    spinner_city.setSelection(ad.getInt("city"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        myadapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.category, R.layout.row);
        spinner_category.setAdapter(myadapter);
        try {
            spinner_category.setSelection(ad.getInt("category"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        myadapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.price_type, R.layout.row);
        spinner_price_type.setAdapter(myadapter);
        try {
            spinner_price_type.setSelection(ad.getInt("price_type"));


        } catch (JSONException e) {
            e.printStackTrace();
        }









        images.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_image = 0;
                if (!fill_image[current_image]) {
                    show_dialog();
                } else {
                    show_delete_dialog();
                }
            }
        });


        images.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_image = 1;
                if (!fill_image[current_image]) {
                    show_dialog();
                } else {
                    show_delete_dialog();
                }
            }
        });


        images.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_image = 2;
                if (!fill_image[current_image]) {
                    show_dialog();
                } else {
                    show_delete_dialog();
                }
            }
        });



    //    try {
      //      Arrays.fill(address_images,"http://birdbaz.ir/app/" + ad.getString("image"));
     //   } catch (JSONException e) {
     //       e.printStackTrace();
     //   }


        spinner_price_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner_price_type.getSelectedItemPosition() == 3) {
                    price_layout.setVisibility(View.VISIBLE);

                } else {
                    price_layout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        title_text.requestFocus();




        submit_bt = (Button) findViewById(R.id.edit_submit_bt);
        submit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Boolean is_validate = true;

                if (title_text.getText().toString().trim().length() >= 5 &&(title_text.getText().toString().trim().length() <= 30)) {
                    title_layout.setErrorEnabled(false);


                } else {
                    title_layout.setError("عنوان اگهی حداقل باید بین 5 تا 30 حرف باشد");
                    title_layout.setErrorEnabled(true);


                    is_validate = false;

                }
                if (description_text.getText().toString().trim().length() >= 10) {

                    description_layout.setErrorEnabled(false);

                } else {


                    description_layout.setError("توضیحات اگهی حداقل باید 10 حرف باشد");
                    description_layout.setErrorEnabled(true);


                    is_validate = false;
                }


                if (district_text.getText().toString().trim().length() >= 3&& (district_text.getText().toString().trim().length() <= 20)) {

                    district_layout.setErrorEnabled(false);

                } else {


                    district_layout.setError("عنوان محله حداقل باید بین  3 تا 20 حرف باشد");
                    district_layout.setErrorEnabled(true);


                    is_validate = false;


                }


                if (spinner_category.getSelectedItemPosition() == 0) {


                    ((TextView) spinner_category.getSelectedView()).setError("");

                    is_validate = false;


                }

                if (spinner_province.getSelectedItemPosition() == 0) {

                    ((TextView) spinner_province.getSelectedView()).setError("");

                    is_validate = false;

                }

                if (spinner_city.getSelectedItemPosition() == 0) {

                    ((TextView) spinner_city.getSelectedView()).setError("");

                    is_validate = false;


                }

                if (spinner_price_type.getSelectedItemPosition() == 3) {

                    if ((price_text.getText().toString().replaceAll(",", "")).trim().length() == 0 || Integer.parseInt((price_text.getText().toString().replaceAll(",", "")).trim()) <= 0) {
                        price_layout.setError("قیمت وارد شده اشتباه است");
                        price_layout.setErrorEnabled(true);

                        is_validate = false;


                    } else {
                        price_layout.setErrorEnabled(false);
                    }

                } else {
                    price_layout.setErrorEnabled(false);
                }

                if (is_validate) {

                    new_ad = new JSONObject();

                    try {
                        if(edit_image[0]==true) {
                            if (fill_image[0] == true) {
                                new_ad.put("image1", address_images[0]);
                            } else {
                                new_ad.put("image1", "");
                            }
                        }else{
                            new_ad.put("image1",ad.getString("image1").trim());

                        }

                        if(edit_image[1]==true) {
                            if (fill_image[1] == true) {
                                new_ad.put("image2", address_images[1]);
                            } else {
                                new_ad.put("image2", "");
                            }
                        }else{
                            new_ad.put("image2",ad.getString("image2").trim());

                        }

                        if(edit_image[2]==true) {
                            if (fill_image[2] == true) {
                                new_ad.put("image3", address_images[2]);
                            } else {
                                new_ad.put("image3", "");
                            }
                        }else{
                            new_ad.put("image3",ad.getString("image3").trim());

                        }

                        new_ad.put("id", ad.getInt("id"));



                        new_ad.put("user_id", settings.getInt("user_id", 0));



                        new_ad.put("title", title_text.getText().toString().trim());

                        new_ad.put("description", description_text.getText().toString().trim());

                        new_ad.put("category", spinner_category.getSelectedItemPosition());

                        new_ad.put("province", spinner_province.getSelectedItemPosition());

                        new_ad.put("city", spinner_city.getSelectedItemPosition());

                        new_ad.put("district", district_text.getText().toString().trim());

                        new_ad.put("price_type", spinner_price_type.getSelectedItemPosition());

                        if (spinner_price_type.getSelectedItemPosition() == 3) {

                            new_ad.put("price",Integer.parseInt( price_text.getText().toString().replaceAll(",", "").trim()));

                        } else {
                            new_ad.put("price", 0);
                        }

                        new_ad.put("command", "edit");




                    } catch (JSONException e) {

                        e.printStackTrace();
                    }

                    //Toast.makeText(getApplicationContext(),new_ad.toString(), Toast.LENGTH_SHORT).show();

                    new send_ad().execute();


                } else {
                    Toast.makeText(getApplicationContext(), "خطا در ورود اطلاعات لطفا اطلاعات را به صورت کامل وارد کنید", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }
    ////

    private TextWatcher onTextChangedListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                price_text.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    price_text.setText(formattedString);
                    price_text.setSelection(price_text.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                price_text.addTextChangedListener(this);
            }
        };
    }

    ////




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.delete) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                builder.setMessage("ایا می خواهید این اگهی را حذف کنید");
                builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //delete agahi
                        try {
                            delto = new JSONObject();

                            delto.put("id", ad.getInt("id"));

                            delto.put("user_id", settings.getInt("user_id", 0));

                            delto.put("command", "delto");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        new delete().execute();
                        EditActivity.this.finish();



                    }
                });

                builder.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();


            }






        return super.onOptionsItemSelected(item);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CALLBACK_CONSTANT){
            //check if all permissions are granted
            boolean allgranted = false;
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if(allgranted){
                proceedAfterPermission();
            } else if(ActivityCompat.shouldShowRequestPermissionRationale(EditActivity.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(EditActivity.this,permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(EditActivity.this,permissionsRequired[2])){
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                builder.setTitle("درخواست مجوز");
                builder.setMessage("برای انتخاب عکس نیاز به مجوز دوربین و فایل است بر روی تایید بزنید");
                builder.setPositiveButton("تایید", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(EditActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getBaseContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
            }
        }
    }


    private void proceedAfterPermission() {


        per=2;

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(EditActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }












    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_menu, menu);
        return (true);
    }


    public void show_dialog() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("انتخاب عکس از گالری");
        list.add("گرفتن عکس با دوربین");
        if(ActivityCompat.checkSelfPermission(EditActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(EditActivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(EditActivity.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(EditActivity.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(EditActivity.this,permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(EditActivity.this,permissionsRequired[2])){
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                builder.setTitle("درخواست مجوز");
                builder.setMessage("برای انتخاب عکس نیاز به مجوز دوربین و فایل است بر روی تایید بزنید");
                builder.setPositiveButton("تایید", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(EditActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(permissionsRequired[0],false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                builder.setTitle("در خواست مجوز");
                builder.setMessage("برای انتخاب عکس نیاز به مجوز دوربین و فایل است بر روی تایید بزنید");
                builder.setPositiveButton("تایید", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "مجوز دوربین و فایل را تایید کنید", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }  else {
                //just request the permission
                ActivityCompat.requestPermissions(EditActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
            }


            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0],true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }

        if(per==2) {

            AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
            builder.setAdapter(new ArrayAdapter<String>(EditActivity.this, R.layout.row, R.id.mytext, list), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {


                    if (i == 0) {//galerry

                        Intent galerry_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(Intent.createChooser(galerry_intent, "لطفا یک عکس را انتخاب کنید"), 2);

                    } else if(i==1) {//camera




                        CropImage.activity(uri).setAspectRatio(1, 1).setRequestedSize(512, 512).start(EditActivity.this);

                       // Intent camera_intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        //File file=new File(Environment.getExternalStorageDirectory(),"file"+String.valueOf(System.currentTimeMillis()+".jpg"));

                       // uri= Uri.fromFile(file);

                     //   camera_intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);

                      //  camera_intent.putExtra("data",true);

                        //startActivityForResult(camera_intent,CAMERA_REQUEST);


                    }

                }
            });

            builder.show();
        }
    }



    public void show_delete_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
        builder.setMessage("ایا مطمئن به حدف عکس هستید");
        builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                images.get(current_image).setImageResource(R.drawable.select_image);

                fill_image[current_image] = false;
                edit_image[current_image]=true;


            }
        });

        builder.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(EditActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            uri = data.getData();

               // CropImage.activity(uri).setAspectRatio(1, 1).setRequestedSize(512, 512).start(this);





        }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri resulte1Uri = result.getUri();
            images.get(current_image).setImageURI(resulte1Uri);
            edit_image[current_image] = true;
            fill_image[current_image] = true;
        }




        if (requestCode == 2 && resultCode == RESULT_OK) {//gallery
            uri = data.getData();
            CropImage.activity(uri).setAspectRatio(1, 1).setRequestedSize(512, 512).start(this);
            uri=null;


        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri resulte1Uri = result.getUri();
            images.get(current_image).setImageURI(resulte1Uri);
            edit_image[current_image]=true;
            fill_image[current_image] = true;



            //upload_image
            BitmapDrawable bd = ((BitmapDrawable) images.get(current_image).getDrawable());
            Bitmap bm = bd.getBitmap();

            ByteArrayOutputStream bao = new ByteArrayOutputStream();

            bm.compress(Bitmap.CompressFormat.PNG, 90, bao);

            image_base64 = Base64.encodeToString(bao.toByteArray(), Base64.DEFAULT);

            new upload_image().execute();


        }


        super.onActivityResult(requestCode, resultCode, data);
    }


    public class upload_image extends AsyncTask<Void, Void, String> {

        ProgressDialog pd = new ProgressDialog(EditActivity.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("لطفا منتظر بمانید");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... Void) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
            namevaluepairs.add(new BasicNameValuePair("image", image_base64));

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://ghafas.net/app/command.php");
                httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs));

                HttpResponse httpresponse = httpclient.execute(httppost);

                String response = EntityUtils.toString(httpresponse.getEntity());


                if (response.startsWith("<ghafas>") && response.endsWith("</ghafas>")) {//response is valid

                    response = response.replace("<ghafas>", "").replace("</ghafas>", "");

                    if (!response.trim().equals("0")) {//upload ok

                        address_images[current_image] = response.trim();

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                Toast.makeText(getBaseContext(), "خطا در اپلود تصویر", Toast.LENGTH_SHORT).show();

                                images.get(current_image).setImageResource(R.drawable.select_image);
                                fill_image[current_image] = false;
                            }
                        });

                    }

                } else {//error

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(getBaseContext(), "خطا در اپلود تصویر", Toast.LENGTH_SHORT).show();

                            images.get(current_image).setImageResource(R.drawable.select_image);
                            fill_image[current_image] = false;
                        }
                    });


                }


            } catch (Exception e) {
                e.printStackTrace();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(getBaseContext(), "خطا در اپلود تصویر", Toast.LENGTH_SHORT).show();

                        images.get(current_image).setImageResource(R.drawable.select_image);
                        fill_image[current_image] = false;
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


    public class send_ad extends AsyncTask<Void, Void, String> {
        ProgressDialog pd = new ProgressDialog(EditActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("در حال ارسال اگهی");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
            namevaluepairs.add(new BasicNameValuePair("myjson", new_ad.toString()));


            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://ghafas.net/app/command.php");
                httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs, HTTP.UTF_8));

                HttpResponse httpresponse = httpclient.execute(httppost);

                String response = EntityUtils.toString(httpresponse.getEntity());


                if (response.startsWith("<ghafas>") && response.endsWith("</ghafas>")) {//response is valid

                    response = response.replace("<ghafas>", "").replace("</ghafas>", "");

                    if (response.trim().equals("ok")) {


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                Toast.makeText(getBaseContext(), "اگهی با موفقیت ارسال شد", Toast.LENGTH_SHORT).show();

                            }
                        });


                        EditActivity.this.finish();



                    } else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                Toast.makeText(getBaseContext(), "خطا در ارسال اگهی", Toast.LENGTH_SHORT).show();

                            }
                        });


                    }


                } else {


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(getBaseContext(), "خطا در ارسال اگهی", Toast.LENGTH_SHORT).show();

                        }
                    });


                }


            } catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(getBaseContext(), "خطا در ارسال اگهی", Toast.LENGTH_SHORT).show();

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








    public class delete extends AsyncTask<Void, Void, String> {
        ProgressDialog pd = new ProgressDialog(EditActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("در حال ارسال اگهی");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
            namevaluepairs.add(new BasicNameValuePair("myjson", delto.toString()));


            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://ghafas.net/app/command.php");
                httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs, HTTP.UTF_8));

                HttpResponse httpresponse = httpclient.execute(httppost);

                String response = EntityUtils.toString(httpresponse.getEntity());


                if (response.startsWith("<ghafas>") && response.endsWith("</ghafas>")) {//response is valid

                    response = response.replace("<ghafas>", "").replace("</ghafas>", "");

                    if (response.trim().equals("ok")) {


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                Toast.makeText(getBaseContext(), "اگهی با موفقیت حذف شد", Toast.LENGTH_SHORT).show();

                            }
                        });


                        EditActivity.this.finish();



                    } else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                Toast.makeText(getBaseContext(), "خطا در حذف اگهی", Toast.LENGTH_SHORT).show();

                            }
                        });


                    }


                } else {


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(getBaseContext(), "خطا در حذف  اگهی", Toast.LENGTH_SHORT).show();

                        }
                    });


                }


            } catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(getBaseContext(), "خطا در حذف اگهی", Toast.LENGTH_SHORT).show();

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
