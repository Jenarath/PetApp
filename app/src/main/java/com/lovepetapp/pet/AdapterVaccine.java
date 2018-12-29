package com.lovepetapp.pet;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class AdapterVaccine extends BaseAdapter {
    private LayoutInflater inflater;

    public AdapterVaccine(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return ActivityVacDog.VacID.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_vac_dog, null);
            holder = new ViewHolder();
            holder.txtWeek = (TextView) convertView.findViewById(R.id.txtWeek);
            holder.txtVacc = (TextView) convertView.findViewById(R.id.txtVacc);
            holder.txtNote = (TextView) convertView.findViewById(R.id.txtNote);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.txtWeek.setText(ActivityVacDog.Week.get(position));
        holder.txtVacc.setText(ActivityVacDog.Vac.get(position));
        holder.txtNote.setText(ActivityVacDog.Note.get(position));


        return convertView;
    }

    static class ViewHolder {
        TextView txtWeek, txtVacc, txtNote;
    }
}