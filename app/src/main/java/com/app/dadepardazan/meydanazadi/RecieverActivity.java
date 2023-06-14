package com.app.dadepardazan.meydanazadi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Bamdad on 7/6/2019.
 */
public class RecieverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reciever_activity);


        Intent in = getIntent();
        Uri data = in.getData();
// دو سطر بالا دیتاهای ارسال شده به اکتیویتی را دریافت مینماید و به data کپی میکند.
        if (data != null) {

// در سطر زیر عبارت "varchar://" را از داده های دریافتی حذف مینماییم این عبارت همان بخش scheme موجود در منیفست می باشد.
            String rdata = data.toString().replace("myapp://com.app.dadepardazan.MeydanAzadi:", "");

// حال rdata شامل داده هاییست که بعد intent:// در قسمت آموزش سمت وب فرستاده شده است می باشد.
//برای این آموزش فقط عدد ارسال کرده ایم. و حالت های مختلف را با توست هندل میکنیم.
            if (rdata.equals("1")) {
                Toast.makeText(getBaseContext(), "موفقیت", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseContext(), "نا موفق", Toast.LENGTH_LONG).show();
            }
        }


    }
}
