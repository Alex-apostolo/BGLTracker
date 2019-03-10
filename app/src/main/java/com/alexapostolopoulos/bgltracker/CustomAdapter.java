package com.alexapostolopoulos.bgltracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<CustomRowData> {

    CustomAdapter(Context context, CustomRowData[] customRowData) {
        super(context, R.layout.custom_row,customRowData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_row, parent, false);

        CustomRowData rowData = getItem(position);

        TextView title = customView.findViewById(R.id.title1);
        TextView subtitle = customView.findViewById(R.id.subtitle);
        TextView date = customView.findViewById(R.id.date);
        TextView time = customView.findViewById(R.id.time);

        title.setText(rowData.getTitle());
        subtitle.setText(rowData.getSubtitle());

        String displayDate =  "Date: " + rowData.getDate().toString();
        date.setText(displayDate);
        String displayTime = "Time: " + rowData.getTime().toString();
        time.setText(displayTime);

        return customView;
    }
}