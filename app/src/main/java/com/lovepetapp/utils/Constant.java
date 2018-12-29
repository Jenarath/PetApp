package com.lovepetapp.utils;

import com.lovepetapp.pet.BuildConfig;
import com.lovepetapp.items.ItemAbout;
import com.lovepetapp.items.ItemCart;
import com.lovepetapp.items.ItemMenuCat;
import com.lovepetapp.items.ItemOrderList;
import com.lovepetapp.items.ItemRestaurant;
import com.lovepetapp.items.ItemUser;

import java.io.Serializable;
import java.util.ArrayList;

public class Constant implements Serializable{

	private static String SERVER_URL = BuildConfig.SERVER_URL;


	public static final String URL_REGISTER = SERVER_URL + "user_register_api.php";

	public static final String URL_LOGIN_1 = SERVER_URL + "user_login_api.php?email=";
	public static final String URL_LOGIN_2 = "&password=";

	public static final String URL_FORGOT_PASS = SERVER_URL + "user_forgot_pass_api.php?email=";

	public static final String URL_PROFILE = SERVER_URL + "user_profile_api.php?id=";
	public static final String URL_PROFILE_EDIT = SERVER_URL + "user_profile_update_api.php";



	public static final String TAG_ROOT = "PET_APP";

	public static final String TAG_REST_NAME = "restaurant_name";


	public static final String TAG_MENU_NAME="menu_name";

	public static final String TAG_MENU_PRICE="menu_price";
	public static final String TAG_MENU_IMAGE="menu_image";
	public static final String TAG_MENU_REST_ID="rest_id";
	public static final String TAG_MENU_QYT="menu_qty";


	public static final String TAG_MSG="msg";
	public static final String TAG_SUCCESS="success";

	public static final String TAG_NAME_USER="user_name";

	public static final String TAG_USER_ID="user_id";
	public static final String TAG_USER_NAME="name";
	public static final String TAG_USER_EMAIL="email";
	public static final String TAG_USER_PHONE="phone";
	public static final String TAG_USER_ADDRESS="address";
	public static final String TAG_USER_IMAGE="user_image";

	public static final String TAG_CART_ID = "cart_id";
	public static final String TAG_CART_MENU_ID = "menu_id";
	public static final String TAG_CART_COUNT = "cart_items";

	public static final String SHARED_PREF_LOGIN="login";
	public static final String SHARED_PREF_EMAIL="email";
	public static final String SHARED_PREF_PASSWORD="password";


	public static ItemUser itemUser;

	public static Boolean isUpdate = false;
	public static Boolean isLogged = false;


	public static ArrayList<ItemCart> arrayList_cart = new ArrayList<>();


	public static int menuCount = 0;

}