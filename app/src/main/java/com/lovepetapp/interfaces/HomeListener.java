package com.lovepetapp.interfaces;

import com.lovepetapp.items.ItemRestaurant;

import java.util.ArrayList;

public interface HomeListener {
    void onStart();
    void onEnd(String success, ArrayList<ItemRestaurant> arrayList_latest, ArrayList<ItemRestaurant> arrayList_featured);
}
