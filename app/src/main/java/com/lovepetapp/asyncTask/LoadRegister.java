package com.lovepetapp.asyncTask;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.webkit.MimeTypeMap;

import com.lovepetapp.interfaces.LoginListener;
import com.lovepetapp.utils.Constant;
import com.lovepetapp.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class LoadRegister extends AsyncTask<String, String, String> {

    private Context context;
    private String msg = "";
    private String suc = "";
    private LoginListener loginListener;

    public LoadRegister(LoginListener loginListener) {
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
        String pass = strings[2];
        String phone = strings[3];
        String address = strings[4];
        String imagePath = strings[5];

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", name)
                .addFormDataPart("email", email)
                .addFormDataPart("password", pass)
                .addFormDataPart("phone", phone)
                .addFormDataPart("address", address);
        if (!imagePath.equals("")) {
            File file = new File(imagePath);
            System.out.print("imagePath" + imagePath);
            builder.addFormDataPart("user_image", file.getName(), RequestBody.create(getMediaType(file.toURI()), file));
        }

        try {
//            JSONObject jsonObject = JsonUtils.makeHttpRequest(Constant.URL_REGISTER,"POST",nameValuePairs);
            String json = JsonUtils.okhttpPost(Constant.URL_REGISTER, builder.build());
            JSONObject jsonObject = new JSONObject(json);

            JSONArray jsonArray = jsonObject.getJSONArray(Constant.TAG_ROOT);
            JSONObject obj = jsonArray.getJSONObject(0);

            msg = obj.getString(Constant.TAG_MSG);
            suc = obj.getString(Constant.TAG_SUCCESS);

            return "1";

        } catch (JSONException e) {
            e.printStackTrace();
            return "0";
        } catch (Exception ee) {
            ee.printStackTrace();
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        System.out.println("succs" + s);

        loginListener.onEnd(s, msg);
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
