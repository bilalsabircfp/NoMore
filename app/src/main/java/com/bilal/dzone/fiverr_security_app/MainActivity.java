package com.bilal.dzone.fiverr_security_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView add_con, slc_con, alarm;
    SharedPreferences sharedPreferences;
    String name;
    TextView textView;
    ImageView toggle;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        sharedPreferences = this.getSharedPreferences("DataStore" , Context.MODE_PRIVATE);
        name = sharedPreferences.getString("name", "Ni Mila Kuch");

        textView = findViewById(R.id.textView3);
        add_con = findViewById(R.id.a1);
        slc_con = findViewById(R.id.a2);
        alarm = findViewById(R.id.a3);
        toggle = findViewById(R.id.toggle);

        //change on off button image
        if (sharedPreferences.getString("toggle", "Ni Mila Kuch").equals("on")){

            toggle.setImageResource(R.drawable.on);
        }
        else if (sharedPreferences.getString("toggle", "Ni Mila Kuch").equals("off")) {
            toggle.setImageResource(R.drawable.off);
        }


        textView.setText("Welcome" + " " + name);


        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sharedPreferences.getString("toggle", "Ni Mila Kuch").equals("off")){
                    flag = 1;
                    toggle.setImageResource(R.drawable.on);
                    Toast.makeText(MainActivity.this, "Nearby Users Notifications Turned On", Toast.LENGTH_SHORT).show();
                    //Creating a shared preference
                    sharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("toggle", "on");
                    editor.apply();

                }
                else if (sharedPreferences.getString("toggle", "Ni Mila Kuch").equals("on")){
                    flag = 0;
                    toggle.setImageResource(R.drawable.off);
                    Toast.makeText(MainActivity.this, "Nearby Users Notifications Turned Off", Toast.LENGTH_SHORT).show();
                    //Creating a shared preference
                    sharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("toggle", "off");
                    editor.apply();
                }


            }
        });



        add_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, Add_Contaxcts2.class);
                startActivity(intent);
            }
        });



        slc_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, Show_Contacts.class);
                startActivity(intent);
            }
        });



        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, Alarm2.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });



    }


    @Override
    public void onBackPressed() {

        this.finish();
    }
}
