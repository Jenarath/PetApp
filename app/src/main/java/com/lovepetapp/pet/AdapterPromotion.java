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


public class AdapterPromotion  extends BaseAdapter {

    private Activity activity;

    public AdapterPromotion(Activity act) {
        this.activity = act;
    }

    public int getCount() {
        return ActivityPromotion.PromotionID.size();
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
            convertView = inflater.inflate(R.layout.item_promotion, null);
            holder = new ViewHolder();

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        String test = AdapterPromotion.Image.get(position);
//        System.out.print("position = \n" + position);


//        holder.txtText = (TextView) convertView.findViewById(R.id.txtTextPro);
        holder.imgThumb = (ImageView) convertView.findViewById(R.id.imgThumb);

//        holder.txtText.setText(ActivityPromotion.PromotionName.get(position));

        Picasso.get().load(ActivityPromotion.Image.get(position)).into(holder.imgThumb);
//        Picasso.with(mContext).load(item.getImage()).into(holder.imageView);


        return convertView;
    }

    static class ViewHolder {
//        TextView txtText;
        ImageView imgThumb;

    }


}
