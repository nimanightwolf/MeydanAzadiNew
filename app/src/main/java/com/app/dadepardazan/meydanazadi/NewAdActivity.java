package com.app.dadepardazan.meydanazadi;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;

import com.app.dadepardazan.meydanazadi.login.LoginActivity;
import com.google.android.material.textfield.TextInputLayout;
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

public class NewAdActivity extends MainActivity {


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

    JSONObject new_ad;


    ArrayList<ImageView> images = new ArrayList<ImageView>();
    int current_image;
    Uri uri;
    Button submit_bt;
    Boolean[] fill_image = new Boolean[3];
    String[] address_images= new String[3];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout content_frame;
        content_frame = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.new_ad, content_frame);
        toolbar.setTitle("ثبت اگهی ");


        spinner_province = (Spinner) findViewById(R.id.spinner_province);
        spinner_city = (Spinner) findViewById(R.id.spinner_city);
        spinner_category = (Spinner) findViewById(R.id.spinner_category);
        spinner_price_type = (Spinner) findViewById(R.id.spinner_price_type);


        price_layout = (TextInputLayout) findViewById(R.id.price_layout);
        price_text = (EditText) findViewById(R.id.price_text);
        price_text.addTextChangedListener(onTextChangedListener());

        title_text = (EditText) findViewById(R.id.title_text);
        title_layout = (TextInputLayout) findViewById(R.id.title_layout);


        district_layout = (TextInputLayout) findViewById(R.id.district_layout);
        district_text = (EditText) findViewById(R.id.district_text);


        description_layout = (TextInputLayout) findViewById(R.id.description_layout);
        description_text = (EditText) findViewById(R.id.description_text);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // Drawable arrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
       // getSupportActionBar().setHomeAsUpIndicator(arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewAdActivity.this.finish();
            }
        });






        ArrayAdapter<CharSequence> myadapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.province, R.layout.row);
        spinner_province.setAdapter(myadapter);


        spinner_province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<CharSequence> myadapter = ArrayAdapter.createFromResource(getApplicationContext(), getResources().getIdentifier("array/city" + spinner_province.getSelectedItemPosition(), null, getApplicationContext().getPackageName()), R.layout.row);
                spinner_city.setAdapter(myadapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        myadapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.category, R.layout.row);
        spinner_category.setAdapter(myadapter);

        myadapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.price_type, R.layout.row);
        spinner_price_type.setAdapter(myadapter);


        images.add((ImageView) findViewById(R.id.image1));
        images.add((ImageView) findViewById(R.id.image2));
        images.add((ImageView) findViewById(R.id.image3));


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


        Arrays.fill(fill_image, false);
        Arrays.fill(address_images,"");


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

        submit_bt = (Button) findViewById(R.id.submit_bt);
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

                if (is_validate)
                {





                    new_ad=new JSONObject();

                    try {
                        if(fill_image[0]==true){
                            new_ad.put("image1",address_images[0]);
                        }else
                        {
                            new_ad.put("image1","");
                        }

                        if(fill_image[1]==true){
                            new_ad.put("image2",address_images[1]);
                        }else
                        {
                            new_ad.put("image2","");
                        }

                        if(fill_image[2]==true){
                            new_ad.put("image3",address_images[2]);
                        }else
                        {
                            new_ad.put("image3","");
                        }

                        new_ad.put("user_id",settings.getInt("user_id",0));

                        new_ad.put("title",title_text.getText().toString().trim());

                        new_ad.put("description",description_text.getText().toString().trim());

                        new_ad.put("category",spinner_category.getSelectedItemPosition());

                        new_ad.put("province",spinner_province.getSelectedItemPosition());

                        new_ad.put("city",spinner_city.getSelectedItemPosition());

                        new_ad.put("district",district_text.getText().toString().trim());

                        new_ad.put("price_type",spinner_price_type.getSelectedItemPosition());

                        if(spinner_price_type.getSelectedItemPosition()==3){

                           //Toast.makeText(getApplicationContext(),price_text.toString(),Toast.LENGTH_SHORT).show();
                          new_ad.put("price",Integer.parseInt( price_text.getText().toString().replaceAll(",", "").trim()));

                        }else
                        {
                            new_ad.put("price",0);
                        }

                        new_ad.put("command","new_ad");



                    }catch (JSONException e){

                        e.printStackTrace();
                    }
                    if(other==2){
                        other=0;
                        new send_ad().execute();

                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewAdActivity.this);
                    builder.setMessage("ایا می خواهید با همین شماره اگهی خود را ثبت کنید");
                    builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                    //Toast.makeText(getApplicationContext(),new_ad.toString(), Toast.LENGTH_SHORT).show();
                    if(settings.getInt("user_id",0)!=0){
                        new send_ad().execute();
                    }else {
                        other=1;
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        i.putExtra("others", other);

                        startActivity(i);

                    }


                        }
                    });

                    builder.setNegativeButton("خیر با شماره ی دیگر", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putInt("user_id",0);
                            editor.commit();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(getBaseContext(), "شما به قفس وارد شدید", Toast.LENGTH_SHORT).show();
//                                    other=1;
//                                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
//                                    startActivity(i);
//                                    finish();

//                                    Intent back=new Intent(getApplicationContext(), LoginActivity.class);
//                                    Bundle cuntainer=new Bundle();
//                                    cuntainer.putInt("ID", other);
//                                    back.putExtras(cuntainer);
//                                    setResult(RESULT_OK,back);
//                                    startActivityForResult(back,other);
//                                    finish();
                                    other=1;
                                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                    i.putExtra("others", other);

                                    startActivity(i);

                                }
                            });

                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();


                } else {
                    Toast.makeText(getApplicationContext(), "خطا در ورود اطلاعات لطفا اطلاعات را به صورت کامل وارد کنید", Toast.LENGTH_SHORT).show();
                }



            }

        });



    }

    ///

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




    ///








    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.help_menu, menu);
        return (true);
    }




    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.help) {

            show_help_dialog();


        }


        return super.onOptionsItemSelected(item);

    }







    public void show_dialog() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("انتخاب عکس از گالری");
        list.add("گرفتن عکس با دوربین");

        AlertDialog.Builder builder = new AlertDialog.Builder(NewAdActivity.this);
        builder.setAdapter(new ArrayAdapter<String>(NewAdActivity.this, R.layout.row, R.id.mytext, list), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {//galerry

                    Intent galerry_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(Intent.createChooser(galerry_intent, "لطفا یک عکس را انتخاب کنید"), 2);

                } else if(i==1) {//camera
                    CropImage.activity(uri).setAspectRatio(1,1).setRequestedSize(512,512).start(NewAdActivity.this);



                  //  Intent camera_intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    //File file=new File(Environment.getExternalStorageDirectory(),"file"+String.valueOf(System.currentTimeMillis()+".jpg"));

                   // uri= Uri.fromFile(file);

                  //  camera_intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);

                  //  camera_intent.putExtra("return-data",true);

                  //  startActivityForResult(camera_intent,1);













                }

            }
        });

        builder.show();
    }


    public void show_delete_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewAdActivity.this);
        builder.setMessage("ایا مطمئن به حدف عکس هستید");
        builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                images.get(current_image).setImageResource(R.drawable.select_image);
                fill_image[current_image] = false;

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

    //
    public void show_help_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewAdActivity.this);
        builder.setMessage("برای ارسال اگهی ابتدا عکس پرندگان خود را قرار دهید و توضیحات مربوط به آن را و استان. شهر. و محله سکونت خود را وارد کنید و بر روی دکمه ی ثبت اگهی بزنید اگهی شما پس از تایید کارشناسان به نمایش در می اید .  در قسمت عنوان و محله, کیبورد خود را بر روی زیان فارسی بگذارید");
        builder.setPositiveButton("باشه", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });



        AlertDialog dialog = builder.create();
        dialog.show();


    }



    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==1&&resultCode==RESULT_OK)
        {//camera


          //  CropImage.activity(uri).setAspectRatio(1,1).setRequestedSize(512,512).start(this);


        }else  if (requestCode==2&&resultCode==RESULT_OK) {//gallery

            uri=data.getData();

            CropImage.activity(uri).setAspectRatio(1,1).setRequestedSize(512,512).start(this);
            uri=null;



        } else if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE&&resultCode==RESULT_OK)
        {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            Uri resutlUri=result.getUri();

            images.get(current_image).setImageURI(resutlUri);


            fill_image[current_image]=true;
            resutlUri=null;







            BitmapDrawable bd=((BitmapDrawable) images.get(current_image).getDrawable());
            Bitmap bm=bd.getBitmap();

            ByteArrayOutputStream bao=new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG,90,bao);

            image_base64=Base64.encodeToString(bao.toByteArray(),Base64.DEFAULT);


            new upload_image().execute();


        }


        super.onActivityResult(requestCode, resultCode, data);
    }


    public class upload_image extends AsyncTask<Void, Void, String> {

        ProgressDialog pd = new ProgressDialog(NewAdActivity.this);


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


                if(response.startsWith("<ghafas>")&&response.endsWith("</ghafas>"))
                {//response is valid

                    response=response.replace("<ghafas>","").replace("</ghafas>","");

                    if(!response.trim().equals("0")){//upload ok

                        address_images[current_image]=response.trim();

                    }else
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                Toast.makeText(getBaseContext(), "خطا در اپلود تصویر", Toast.LENGTH_SHORT).show();

                                images.get(current_image).setImageResource(R.drawable.select_image);
                                fill_image[current_image]=false;
                            }
                        });

                    }

                }else
                {//error

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(getBaseContext(), "خطا در اپلود تصویر", Toast.LENGTH_SHORT).show();

                            images.get(current_image).setImageResource(R.drawable.select_image);
                            fill_image[current_image]=false;
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
                        fill_image[current_image]=false;
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


    public  class send_ad extends AsyncTask<Void,Void,String>
    {
        ProgressDialog pd=new ProgressDialog(NewAdActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("در حال ارسال اگهی");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> namevaluepairs=new ArrayList<NameValuePair>();
            namevaluepairs.add(new BasicNameValuePair("myjson",new_ad.toString()));


            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://ghafas.net/app/command.php");
                httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs,HTTP.UTF_8));

                HttpResponse httpresponse = httpclient.execute(httppost);

                String response = EntityUtils.toString(httpresponse.getEntity());


                if(response.startsWith("<ghafas>")&&response.endsWith("</ghafas>")) {//response is valid

                    response = response.replace("<ghafas>", "").replace("</ghafas>", "");

                    if(response.trim().equals("ok"))
                    {


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                Toast.makeText(getBaseContext(), "اگهی با موفقیت ارسال شد", Toast.LENGTH_SHORT).show();

                            }
                        });


                        NewAdActivity.this.finish();


                    }else
                    {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                Toast.makeText(getBaseContext(), "خطا در ارسال اگهی", Toast.LENGTH_SHORT).show();

                            }
                        });


                    }



                }else
                {



                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(getBaseContext(), "خطا در ارسال اگهی", Toast.LENGTH_SHORT).show();

                        }
                    });


                }




            }catch (Exception e)
            {
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





















}
