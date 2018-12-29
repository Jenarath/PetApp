package com.lovepetapp.pet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterVacCat extends BaseAdapter  {
    private LayoutInflater inflater;

    public AdapterVacCat(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return ActivityVacCat.VacID.size();
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

        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_vac_cat, null);
            holder = new ViewHolder();
            holder.txtWeekCat = (TextView) convertView.findViewById(R.id.txtWeekcat);
            holder.txtVacCat = (TextView) convertView.findViewById(R.id.txtVaccat);
    
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }


        holder.txtWeekCat.setText(ActivityVacCat.WeekCat.get(position));
        holder.txtVacCat.setText(ActivityVacCat.VacCat.get(position));

        return convertView;
    }

    static class ViewHolder {
        TextView txtWeekCat, txtVacCat;
    }


}
