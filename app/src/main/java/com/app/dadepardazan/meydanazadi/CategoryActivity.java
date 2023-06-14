package com.app.dadepardazan.meydanazadi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class CategoryActivity extends MainActivity {
    Button bn_1;
    Button bn_2;
    Button bn_3;
    Button bn_4;
    Button bn_5;
    Button bn_6;
    Button bn_7;
    Button bn_8;
    Button bn_9;
    Button bn_10;
    Button bn_11;
    Button bn_12;
    Button bn_13;
    Button bn_14;
    Button bn_15;
    TextView toolbar_title;
    public static final String ACTIVITY_PACKING_RESULT_CODE = "result_code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout content_frame;
        content_frame = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.category, content_frame);
        BottomNavigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);
        //setContentView(R.layout.category);

        toolbar_title= (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("انتخاب دسته بندی");
        toolbar_title.setVisibility(View.VISIBLE);

        bn_1= (Button) findViewById(R.id.bn_1);
        bn_2= (Button) findViewById(R.id.bn_2);
        bn_3= (Button) findViewById(R.id.bn_3);
        bn_4= (Button) findViewById(R.id.bn_4);
        bn_5= (Button) findViewById(R.id.bn_5);
        bn_6= (Button) findViewById(R.id.bn_6);
        bn_7= (Button) findViewById(R.id.bn_7);
        bn_8= (Button) findViewById(R.id.bn_8);
        bn_9= (Button) findViewById(R.id.bn_9);
        bn_10= (Button) findViewById(R.id.bn_10);
        bn_11= (Button) findViewById(R.id.bn_11);
        bn_12= (Button) findViewById(R.id.bn_12);
        bn_13= (Button) findViewById(R.id.bn_13);
        bn_14= (Button) findViewById(R.id.bn_14);
        bn_15= (Button) findViewById(R.id.bn_15);





        bn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"اولی",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),AdListActivity.class);
                i.putExtra(ACTIVITY_PACKING_RESULT_CODE,"1");
                startActivity(i);

            }
        });
        bn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(),"دومی",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),AdListActivity.class);
                i.putExtra(ACTIVITY_PACKING_RESULT_CODE,"2");
                startActivity(i);

            }
        });
        bn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(),"سومی",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),AdListActivity.class);
                i.putExtra(ACTIVITY_PACKING_RESULT_CODE,"3");
                startActivity(i);

            }
        });
        bn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"چهارمی",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),AdListActivity.class);
                i.putExtra(ACTIVITY_PACKING_RESULT_CODE,"4");
                startActivity(i);


            }
        });
        bn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(),"پنجمی",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),AdListActivity.class);
                i.putExtra(ACTIVITY_PACKING_RESULT_CODE,"5");
                startActivity(i);

            }
        });
        bn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"شیشمی",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),AdListActivity.class);
                i.putExtra(ACTIVITY_PACKING_RESULT_CODE,"6");
                startActivity(i);

            }
        });
        bn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"هفتمی",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),AdListActivity.class);
                i.putExtra(ACTIVITY_PACKING_RESULT_CODE,"7");
                startActivity(i);

            }
        });
        bn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"هشتمی",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),AdListActivity.class);
                i.putExtra(ACTIVITY_PACKING_RESULT_CODE,"8");
                startActivity(i);

            }
        });
        bn_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"هشتمی",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),AdListActivity.class);
                i.putExtra(ACTIVITY_PACKING_RESULT_CODE,"9");
                startActivity(i);

            }
        });
        bn_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"هشتمی",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),AdListActivity.class);
                i.putExtra(ACTIVITY_PACKING_RESULT_CODE,"10");
                startActivity(i);

            }
        });
        bn_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"هشتمی",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),AdListActivity.class);
                i.putExtra(ACTIVITY_PACKING_RESULT_CODE,"11");
                startActivity(i);

            }
        });
        bn_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"هشتمی",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),AdListActivity.class);
                i.putExtra(ACTIVITY_PACKING_RESULT_CODE,"12");
                startActivity(i);

            }
        });
        bn_13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"هشتمی",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),AdListActivity.class);
                i.putExtra(ACTIVITY_PACKING_RESULT_CODE,"13");
                startActivity(i);

            }
        });
        bn_14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"هشتمی",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),AdListActivity.class);
                i.putExtra(ACTIVITY_PACKING_RESULT_CODE,"14");
                startActivity(i);

            }
        });
        bn_15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"هشتمی",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),AdListActivity.class);
                i.putExtra(ACTIVITY_PACKING_RESULT_CODE,"15");
                startActivity(i);

            }
        });
    }
}
