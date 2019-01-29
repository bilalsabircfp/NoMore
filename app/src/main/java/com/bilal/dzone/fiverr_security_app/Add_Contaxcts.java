package com.bilal.dzone.fiverr_security_app;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class Add_Contaxcts extends AppCompatActivity {

        Button button ;
        TextView name,number,more;
        public  static final int RequestPermissionCode  = 1 ;
        Intent intent ;
        private DatabaseHelper db;
        List<Name> names;
        String name_, number_;
        FloatingActionButton floatingActionButton;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_select__contacts);

            db = new DatabaseHelper(this);


            button = (Button)findViewById(R.id.button);
            name = (TextView)findViewById(R.id.textView2);
            number = (TextView)findViewById(R.id.textView);
            more = findViewById(R.id.textView3);
            floatingActionButton = findViewById(R.id.fab);


            int PERMISSION_ALL = 1;
            String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS,
                    Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.INTERNET,Manifest.permission.ACCESS_NETWORK_STATE};

            if(!hasPermissions(this, PERMISSIONS)){
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            }



            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    intent = new Intent(Add_Contaxcts.this, MainActivity.class);
                    startActivity(intent);
                    Add_Contaxcts.this.finish();
                }
            });



            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, 7);

                }
            });
        }






    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }




    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

//                    Toast.makeText(Add_Contaxcts.this,"Permission Granted, Now your application can access CONTACTS.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(Add_Contaxcts.this,"Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }


    @Override
    public void onActivityResult(int RequestCode, int ResultCode, Intent ResultIntent) {

        super.onActivityResult(RequestCode, ResultCode, ResultIntent);

        switch (RequestCode) {

            case (7):
                if (ResultCode == Activity.RESULT_OK) {

                    Uri uri;
                    Cursor cursor1, cursor2;
                    String TempNameHolder, TempNumberHolder, TempContactID, IDresult = "" ;
                    int IDresultHolder ;

                    uri = ResultIntent.getData();

                    cursor1 = getContentResolver().query(uri, null, null, null, null);

                    if (cursor1.moveToFirst()) {

                        TempNameHolder = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        TempContactID = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));

                        IDresult = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        IDresultHolder = Integer.valueOf(IDresult) ;

                        if (IDresultHolder == 1) {

                            cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + TempContactID, null, null);

                            while (cursor2.moveToNext()) {

                                TempNumberHolder = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


                                more.setVisibility(View.VISIBLE);
                                floatingActionButton.setVisibility(View.VISIBLE);


                                name.setText(TempNameHolder);

                                number.setText(TempNumberHolder);



                                name_ = name.getText().toString().trim();
                                number_ = number.getText().toString().trim();



                            }
                            insertRecord(name_, number_);
                        }

                    }
                }
                break;
        }
    }


    //SQLITE
    public void insertRecord(String _name, String _num) {


        db.addName( _name, _num);

        Toast.makeText(this, "Contact Added", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onBackPressed() {

        Toast.makeText(this, "Complete the Process", Toast.LENGTH_SHORT).show();
    }
}

