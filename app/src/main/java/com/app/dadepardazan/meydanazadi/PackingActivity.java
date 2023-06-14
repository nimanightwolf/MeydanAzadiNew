package com.app.dadepardazan.meydanazadi;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class PackingActivity extends MainActivity  {
    Button bn_mavad_shim;
    Button bn_roghan;
    Button bn_zorof;
    Button bn_salamat_ab;
    Button bn_dandan;
    Button bn_ghaza;
    Button bn_sanat;
    Button bn_khas_daro;

    public static final String ACTIVITY_PACKING_RESULT_CODE = "result_code";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout content_frame;
        content_frame = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.packing, content_frame);



        bn_mavad_shim= (Button) findViewById(R.id.bn_mavad_shim);
        bn_roghan= (Button) findViewById(R.id.bn_roghan);
        bn_zorof= (Button) findViewById(R.id.bn_zorof);
        bn_salamat_ab= (Button) findViewById(R.id.bn_salamat_ab);
        bn_dandan= (Button) findViewById(R.id.bn_dandan);
        bn_ghaza= (Button) findViewById(R.id.bn_ghaza);
        bn_sanat= (Button) findViewById(R.id.bn_sanat);
        bn_khas_daro= (Button) findViewById(R.id.bn_khas_daro);


        bn_mavad_shim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"اولی",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),AdListActivity.class);
                i.putExtra(ACTIVITY_PACKING_RESULT_CODE,"1");
                startActivity(i);

            }
        });
        bn_roghan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(),"دومی",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),AdListActivity.class);
                i.putExtra(ACTIVITY_PACKING_RESULT_CODE,"2");
                startActivity(i);

            }
        });
        bn_zorof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(),"سومی",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),AdListActivity.class);
                i.putExtra(ACTIVITY_PACKING_RESULT_CODE,"3");
                startActivity(i);

            }
        });
        bn_salamat_ab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"چهارمی",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),AdListActivity.class);
                i.putExtra(ACTIVITY_PACKING_RESULT_CODE,"4");
                startActivity(i);


            }
        });
        bn_dandan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(),"پنجمی",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),AdListActivity.class);
                i.putExtra(ACTIVITY_PACKING_RESULT_CODE,"5");
                startActivity(i);

            }
        });
        bn_ghaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"شیشمی",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),AdListActivity.class);
                i.putExtra(ACTIVITY_PACKING_RESULT_CODE,"6");
                startActivity(i);

            }
        });
        bn_sanat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"هفتمی",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),AdListActivity.class);
                i.putExtra(ACTIVITY_PACKING_RESULT_CODE,"7");
                startActivity(i);

            }
        });
        bn_khas_daro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"هشتمی",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),AdListActivity.class);
                i.putExtra(ACTIVITY_PACKING_RESULT_CODE,"8");
                startActivity(i);

            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.help_menu,menu);

        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.help) {




        }


        return super.onOptionsItemSelected(item);

    }


}
