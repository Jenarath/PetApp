package com.lovepetapp.asyncTask;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.webkit.MimeTypeMap;

import com.lovepetapp.interfaces.LoginListener;
import com.lovepetapp.items.ItemUser;
import com.lovepetapp.utils.Constant;
import com.lovepetapp.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class LoadProfileUpdate extends AsyncTask<String, String, String> {

    private Context context;
    private String suc = "";
    private LoginListener loginListener;
    String api = "http://159.65.10.157/php_web_services/user_profile_update_api.php";

    public LoadProfileUpdate(Context context, LoginListener loginListener) {
        this.context = context;
        this.loginListener = loginListener;
    }

    @Override
    protected void onPreExecute() {
        loginListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String name = strings[0];
        String email = strings[1];
        String phone = strings[2];
        String password = strings[3];
        String address = strings[4];
        String imagePath = strings[5];
        String check = strings[6];

        System.out.println("imagePath check " + check);
        System.out.println("imagePath1 check " + imagePath);
        System.out.println("imagePath1 nm " + name);
        System.out.println("imagePath1 email " + email);
        System.out.println("imagePath1 phone " + phone);
        System.out.println("imagePath1 password " + password);
        System.out.println("imagePath1 id " + Constant.itemUser.getId());


        if (check.equals("0")) {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("user_id", Constant.itemUser.getId())
                    .addFormDataPart("name", name)
                    .addFormDataPart("email", email)
                    .addFormDataPart("password", password)
                    .addFormDataPart("phone", phone)
                    .addFormDataPart("address", address)
                    .addFormDataPart("user_image", imagePath);

//            String json_strng = JsonUtils.okhttpPost(Constant.URL_PROFILE_EDIT, builder.build());

            try {

                JsonUtils.okhttpPost(api, builder.build());

//                JSONObject jsonObj = new JSONObject(json_strng);
//
//                JSONArray jsonArray = jsonObj.getJSONArray(Constant.TAG_ROOT);
//                JSONObject jsonObject = jsonArray.getJSONObject(0);
//
//                String id = Constant.itemUser.getId();
//                String name = jsonObject.getString(Constant.TAG_USER_NAME);
//                String email = jsonObject.getString(Constant.TAG_USER_EMAIL);
//                String mobile = jsonObject.getString(Constant.TAG_USER_PHONE);
//                String address = jsonObject.getString(Constant.TAG_USER_ADDRESS);
//                String image = jsonObject.getString(Constant.TAG_USER_IMAGE);
//                suc = jsonObject.getString(Constant.TAG_SUCCESS);
//
//                Constant.itemUser = new ItemUser(id, name, email, mobile, image, address);

                return "1";
            } catch (Exception e) {
                e.printStackTrace();
                return "0";
            }

        }else {

            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("user_id", Constant.itemUser.getId())
                    .addFormDataPart("name", name)
                    .addFormDataPart("email", email)
                    .addFormDataPart("password", password)
                    .addFormDataPart("phone", phone)
                    .addFormDataPart("address", address);
            if (!imagePath.equals("")) {
                File file = new File(imagePath);
                System.out.print("imagePath2" + imagePath);
                builder.addFormDataPart("user_image", file.getName(), RequestBody.create(getMediaType(file.toURI()), file));
            }

//            String json_strng = JsonUtils.okhttpPost(Constant.URL_PROFILE_EDIT, builder.build());

            try {

                JsonUtils.okhttpPost(api, builder.build());

//                JSONObject jsonObj = new JSONObject(json_strng);
//
//                JSONArray jsonArray = jsonObj.getJSONArray(Constant.TAG_ROOT);
//                JSONObject jsonObject = jsonArray.getJSONObject(0);
//
//                String id = Constant.itemUser.getId();
//                String name = jsonObject.getString(Constant.TAG_USER_NAME);
//                String email = jsonObject.getString(Constant.TAG_USER_EMAIL);
//                String mobile = jsonObject.getString(Constant.TAG_USER_PHONE);
//                String address = jsonObject.getString(Constant.TAG_USER_ADDRESS);
//                String image = jsonObject.getString(Constant.TAG_USER_IMAGE);
//                suc = jsonObject.getString(Constant.TAG_SUCCESS);
//
//                Constant.itemUser = new ItemUser(id, name, email, mobile, image, address);

                return "1";
            } catch (Exception e) {
                e.printStackTrace();
                return "0";
            }
        }
    }

    @Override
    protected void onPostExecute(String s) {
        loginListener.onEnd(s, suc);
        super.onPostExecute(s);
    }

    private okhttp3.MediaType getMediaType(URI uri1) {
        Uri uri = Uri.parse(uri1.toString());
        String mimeType;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return okhttp3.MediaType.parse(mimeType);
    }
}
