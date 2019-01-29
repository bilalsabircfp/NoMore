package com.bilal.dzone.fiverr_security_app;

import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Show_Contacts extends AppCompatActivity {

    ListView listView;
    List<Name> names;
    private DatabaseHelper db;
    private NameAdapter nameAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__contacts);

        db = new DatabaseHelper(this);
        listView = findViewById(R.id.list);
        names = new ArrayList<>();


        loadNames();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Name name; final int id_; final String name_;
                name = nameAdapter.getItem(i);

                id_= name.getID();
                name_= name.getName();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Show_Contacts.this);

                alertDialogBuilder.setTitle("Delete Contact");
                alertDialogBuilder.setIcon(R.drawable.alarm);
                alertDialogBuilder.setMessage("Are you sure you want to Delete"+ " "+ name_);
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                db.updateNameStatus(id_,1);
                                loadNames();

                            }
                        });

                alertDialogBuilder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });

                //Showing the alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });


    }





    private void loadNames() {
        names.clear();

        Cursor cursor = db.getNames();
        if (cursor.moveToFirst()) {
            do {
                Name name = new Name(

                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NUM)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID))

                );
                names.add(name);
            } while (cursor.moveToNext());
        }

//        Toast.makeText(this, names.size()+"", Toast.LENGTH_SHORT).show();
        nameAdapter = new NameAdapter(this, R.layout.names, names);
        listView.setAdapter(nameAdapter);
    }


}
