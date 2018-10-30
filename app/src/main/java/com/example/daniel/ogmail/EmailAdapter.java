package com.example.daniel.ogmail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class EmailAdapter extends ArrayAdapter<InboxEmail> {

    private final Context context;
    private final ArrayList<InboxEmail> emails;

    public EmailAdapter(Context context, ArrayList<InboxEmail> emails){
        super(context, R.layout.email, emails);
        this.context = context;
        this.emails = emails;
    }

    @Override
    public View getView(int position, View contextView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.email, parent, false);

        TextView name = (TextView) rowView.findViewById(R.id.name);
        TextView subject = (TextView) rowView.findViewById(R.id.subject);
        TextView weekday = (TextView) rowView.findViewById(R.id.weekday);
        TextView time = (TextView) rowView.findViewById(R.id.time);
        TextView date = (TextView) rowView.findViewById(R.id.date);
        ImageView read = (ImageView) rowView.findViewById(R.id.read);

        name.setText(emails.get(position).from);
        subject.setText(emails.get(position).subject);
        weekday.setText(DateManager.weekday(emails.get(position).date));
        time.setText(DateManager.time(emails.get(position).date));
        date.setText(DateManager.date(emails.get(position).date));
        if(!emails.get(position).wasRead) read.setImageResource(R.drawable.new_email);
        else read.setImageResource(R.drawable.transparent);

        return rowView;
    }
}
