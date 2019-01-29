package com.bilal.dzone.fiverr_security_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Belal on 1/27/2017.
 */

public class NameAdapter extends ArrayAdapter<Name> {

    //storing all the names in the list
    private List<Name> names;
    private Context context;
    Name name;

    //constructor
    public NameAdapter(Context context, int resource, List<Name> names) {
        super(context, resource, names);
        this.context = context;
        this.names = names;
    }


    public Name getItem(int position) {
        return names.get(position);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        //getting the layoutinflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //getting listview itmes
        View listViewItem = inflater.inflate(R.layout.names, null, true);
        TextView textViewShopName = (TextView) listViewItem.findViewById(R.id.name);
        TextView textViewAreaName2 = (TextView) listViewItem.findViewById(R.id.num);
        Button delete = listViewItem.findViewById(R.id.delete);


        //getting the current name
        name = names.get(position);

        //setting the name to textview
        textViewShopName.setText(name.getName());
        textViewAreaName2.setText(name.getNum());


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ((ListView)parent).performItemClick(v,position,1);
            }
        });


        return listViewItem;
    }




}
