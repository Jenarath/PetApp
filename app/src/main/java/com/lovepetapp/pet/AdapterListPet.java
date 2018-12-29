package com.lovepetapp.pet;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lovepetapp.pet.R;
import com.squareup.picasso.Picasso;

public class AdapterListPet  extends BaseAdapter {

    private Activity activity;

    public AdapterListPet(Activity act) {
        this.activity = act;
    }

    public int getCount() {
        return ActivityMenuPets.PetID.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.items_pet, null);
            holder = new ViewHolder();

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        String test = ActivityMenuPets.Image.get(position);
        System.out.print("position = \n" + position);

//        System.out.print("testtttttttttttt = \n" + test);

        holder.imgThumbPet = (ImageView) convertView.findViewById(R.id.imgThumbPet);


        Picasso.get().load(ActivityMenuPets.PetImage.get(position)).into(holder.imgThumbPet);
//        Picasso.with(mContext).load(item.getImage()).into(holder.imageView);

        return convertView;
    }

    static class ViewHolder {
        ImageView imgThumbPet;
    }


}
