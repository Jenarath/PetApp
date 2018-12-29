package com.lovepetapp.asyncTask;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.webkit.MimeTypeMap;

import com.lovepetapp.interfaces.LoginListener;
import com.lovepetapp.items.ItemUser;
import com.lovepetapp.pet.ActivityPetEditDetail;
import com.lovepetapp.utils.Constant;
import com.lovepetapp.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class LoadPetDetailUpdate extends AsyncTask<String, String, String> {

    private Context context;
    private String suc = "";
    private LoginListener loginListener;
    String UpdatePetAPI = "http://159.65.10.157/php_web_services/api_update_pet.php";

    public LoadPetDetailUpdate(Context context, LoginListener loginListener) {
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
        String pet_name = strings[0];
        String pet_gender = strings[1];
        String pet_type = strings[2];
        String pet_catagory = strings[3];
        String pet_birthday = strings[4];
        String pet_weight = strings[5];
        String pet_height = strings[6];
        String pet_colors = strings[7];
        String pet_doctor = strings[8];
        String pet_owner = strings[9];
        String imagePath = strings[10];
        String pet_id = strings[11];
        String check = strings[12];

        System.out.println("check strings " + check);

        if (check.equals("0")) {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("pet_id", pet_id)
                    .addFormDataPart("pet_name", pet_name)
                    .addFormDataPart("pet_gender", pet_gender)
                    .addFormDataPart("pet_type", pet_type)
                    .addFormDataPart("pet_catagory", pet_catagory)
                    .addFormDataPart("pet_birthday", pet_birthday)
                    .addFormDataPart("pet_weight", pet_weight)
                    .addFormDataPart("pet_height", pet_height)
                    .addFormDataPart("pet_colors", pet_colors)
                    .addFormDataPart("pet_doctor", pet_doctor)
                    .addFormDataPart("pet_owner", pet_owner)
                    .addFormDataPart("pet_image", imagePath);
            try {

                JsonUtils.okhttpPost(UpdatePetAPI, builder.build());

                return "1";
            } catch (Exception e) {
                e.printStackTrace();
                return "0";
            }

        }
        else {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("pet_id", pet_id)
                    .addFormDataPart("pet_name", pet_name)
                    .addFormDataPart("pet_gender", pet_gender)
                    .addFormDataPart("pet_type", pet_type)
                    .addFormDataPart("pet_catagory", pet_catagory)
                    .addFormDataPart("pet_birthday", pet_birthday)
                    .addFormDataPart("pet_weight", pet_weight)
                    .addFormDataPart("pet_height", pet_height)
                    .addFormDataPart("pet_colors", pet_colors)
                    .addFormDataPart("pet_doctor", pet_doctor)
                    .addFormDataPart("pet_owner", pet_owner);
            if (!imagePath.equals("")) {
            File file = new File(imagePath);
            System.out.print("imagePath pet" + imagePath);
            builder.addFormDataPart("pet_image", file.getName(), RequestBody.create(getMediaType(file.toURI()), file));
            }
            try {

                JsonUtils.okhttpPost(UpdatePetAPI, builder.build());

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
