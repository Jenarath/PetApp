package com.lovepetapp.pet;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class AdapterActivity extends BaseAdapter {

    private Activity activity;

    public AdapterActivity(Activity act) {
        this.activity = act;
    }

    public int getCount() {
        return ActivityActive.ActivityID.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        AdapterPromotion.ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_promotion, null);
            holder = new AdapterPromotion.ViewHolder();

            convertView.setTag(holder);
        } else {
            holder = (AdapterPromotion.ViewHolder) convertView.getTag();
        }
//        String test = AdapterPromotion.Image.get(position);
//        System.out.print("position = \n" + position);


//        holder.txtText = (TextView) convertView.findViewById(R.id.txtTextPro);
        holder.imgThumb = (ImageView) convertView.findViewById(R.id.imgThumb);

//        holder.txtText.setText(ActivityPromotion.PromotionName.get(position));

        Picasso.get().load(ActivityActive.ImageActivity.get(position)).into(holder.imgThumb);
//        Picasso.with(mContext).load(item.getImage()).into(holder.imageView);


        return convertView;
    }

    static class ViewHolder {
        //        TextView txtText;
        ImageView imgThumb;

    }


}
