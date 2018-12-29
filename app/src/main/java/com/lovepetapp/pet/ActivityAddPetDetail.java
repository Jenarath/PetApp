package com.lovepetapp.pet;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.lovepetapp.asyncTask.LoadPetDetail;
import com.lovepetapp.interfaces.LoginListener;
import com.lovepetapp.utils.Methods;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityAddPetDetail extends AppCompatActivity {


    LoadPetDetail loadPetDetail;
    ArrayList<ArrayList<Object>> data;
    Spinner SpinnerPetType;
    static Button btnDateBirth,btnDateOwner;
    private RadioGroup radioType,radioGenger;
    EditText edt_pet_name, edt_pet_weight, edt_pet_height, edt_pet_color, edt_pet_doc;
    String petName = "-", petWeight = "-", petHeight = "-", petColor = "-", petDoctor = "-", petType = "dog",petGender = "male",petCategory = "เกรตเดน",petBirthDate,petDateOwner,imagePath = "";
    String Result;
    int IOConnect = 0;
    String AddPetAPI= "http://159.65.10.157/php_web_services/api_add_pet.php";

    private Context context;

    Methods methods;
    Button btnSave;
    ImageButton btnPhoto;
    private int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;

    // declare static int variables to store date and time
    private static int mYear;
    private static int mMonth;
    private static int mDay;


    public static final String DATE_DIALOG_ID = "datePicker";

    final Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_detail);
        requestStoragePermission();


        Toolbar toolbar = this.findViewById(R.id.toolbar_pet_detail);
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        methods = new Methods(this);
        methods.forceRTLIfSupported(getWindow());
        methods.setStatusColor(getWindow(), null);

        Intent iGet = getIntent();

        long PetID = iGet.getLongExtra("pet_id", 0);
        Toast.makeText(ActivityAddPetDetail.this, "PetID "+ PetID, Toast.LENGTH_SHORT).show();



        SpinnerPetType = (Spinner) findViewById(R.id.spinner_pet);
        btnPhoto = (ImageButton)findViewById(R.id.btn_photo);
        btnDateBirth = (Button) findViewById(R.id.btnDateBirth);
        btnDateOwner = (Button) findViewById(R.id.btnDateOwner);
        btnSave = (Button) findViewById(R.id.button_save);
        radioGenger = (RadioGroup) findViewById(R.id.radioGender);
        radioType = (RadioGroup) findViewById(R.id.radioType);
        edt_pet_name = (EditText) findViewById(R.id.pet_name);
        edt_pet_weight = (EditText) findViewById(R.id.pet_weight);
        edt_pet_height = (EditText) findViewById(R.id.pet_height);
        edt_pet_color = (EditText) findViewById(R.id.pet_color);
        edt_pet_doc = (EditText) findViewById(R.id.pet_doctor);



            btnPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                }
            });


            btnDateBirth.setText(new StringBuilder()
                    .append(year).append("-")
                    .append(month + 1).append("-")
                    .append(day).append(" "));
            btnDateOwner.setText(new StringBuilder()
                    .append(year).append("-")
                    .append(month + 1).append("-")
                    .append(day).append(" "));

            radioGenger.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.radioFemale:
//                        Toast.makeText(ActivityAddPetDetail.this, "เพศเมีย", Toast.LENGTH_SHORT).show();
                            petGender = "female";
                            break;
                        case R.id.radioMale:
//                        Toast.makeText(ActivityAddPetDetail.this, "เพศผู้", Toast.LENGTH_SHORT).show();
                            petGender = "male";
                            break;
                    }
                }
            });
        dogSpinner();

            radioType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.radioDog:
//                        Toast.makeText(ActivityAddPetDetail.this, "Dog", Toast.LENGTH_SHORT).show();
                            petType = "dog";
                            dogSpinner();
                            break;
                        case R.id.radioCat:
//                        Toast.makeText(ActivityAddPetDetail.this, "Cat", Toast.LENGTH_SHORT).show();
                            petType = "cat";
                            catSpinner();
//                            Toast.makeText(ActivityAddPetDetail.this, petType, Toast.LENGTH_SHORT).show();

                            break;
                    }
                }
            });


            // event listener to handle date button when pressed
            btnDateBirth.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    // show date picker dialog
                    DialogFragment newFragment = new DatePickerBirth();
                    newFragment.show(getSupportFragmentManager(), DATE_DIALOG_ID);

                }
            });

            btnDateOwner.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    // show date picker dialog
                    DialogFragment picker = new DatePickerOwner();
                    picker.show(getSupportFragmentManager(), DATE_DIALOG_ID);

                }
            });
            // event listener to handle send button when pressed
            btnSave.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                loadPetDetail();

                }
            });




    }
    public void dogSpinner()
    {

        final String[] petTypeDog = getResources().getStringArray(R.array.petTypeDog);
        ArrayAdapter<String> adapterPetType = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, petTypeDog);
        SpinnerPetType.setAdapter(adapterPetType);

        SpinnerPetType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                petCategory = petTypeDog[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void catSpinner()
    {

        final String[] petTypeCat = getResources().getStringArray(R.array.petTypeCat);
        ArrayAdapter<String> adapterPetType = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, petTypeCat);
        SpinnerPetType.setAdapter(adapterPetType);

        SpinnerPetType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                petCategory = petTypeCat[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void loadPetDetail() {
        loadPetDetail = new LoadPetDetail(new LoginListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onEnd(String success, String message) {
                if (success.equals("1")) {

                    Toast.makeText(ActivityAddPetDetail.this, getString(R.string.upload_success), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ActivityAddPetDetail.this, ActivityMenuPets.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(ActivityAddPetDetail.this, getString(R.string.error_invalid_password), Toast.LENGTH_SHORT).show();

                }
            }
        });
        System.out.println("imagePath loadPetDetail " + imagePath);

        if(imagePath == "")
        {
            btnPhoto.requestFocus();
            Toast.makeText(ActivityAddPetDetail.this, "กรุณาเพิ่มรูปภาพ", Toast.LENGTH_SHORT).show();

        }else
        {
            loadPetDetail.execute(edt_pet_name.getText().toString(),petGender,petType,petCategory,btnDateBirth.getText().toString(),edt_pet_weight.getText().toString(),edt_pet_height.getText().toString(),edt_pet_color.getText().toString(),edt_pet_doc.getText().toString(),btnDateOwner.getText().toString(),imagePath);

        }

//        loadRegister.execute(editText_name.getText().toString(), editText_email.getText().toString(), editText_pass.getText().toString(), editText_phone.getText().toString(),editText_address.getText().toString(),imagePath);
    }





    // asynctask class to send data to server in background
    public class sendData extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;

        // show progress dialog
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            dialog= ProgressDialog.show(ActivityAddPetDetail.this, "",
                    getString(R.string.loading), true);

        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            // send data to server and store result to variable
            System.out.println("imagePath " + imagePath);

            Result = getRequest(petName, petGender, petType, petCategory, petBirthDate, petWeight, petHeight, petColor, petDoctor,imagePath,petDateOwner);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            // if finish, dismis progress dialog and show toast message
            dialog.dismiss();
            resultAlert(Result);

        }
    }

    // method to post data to server
    public String getRequest(String petName, String petGender, String petType, String petCategory, String petBirthDate, String petWeight, String petHeight, String petColor,String petDoctor,String imagePath,String petOwner){
        String result = "";

        HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost(AddPetAPI);

        try{
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("pet_name", petName));
            nameValuePairs.add(new BasicNameValuePair("pet_gender", petGender));
            nameValuePairs.add(new BasicNameValuePair("pet_type", petType));
            nameValuePairs.add(new BasicNameValuePair("pet_catagory", petCategory));
            nameValuePairs.add(new BasicNameValuePair("pet_birthday", petBirthDate));
            nameValuePairs.add(new BasicNameValuePair("pet_weight", petWeight));
            nameValuePairs.add(new BasicNameValuePair("pet_height", petHeight));
            nameValuePairs.add(new BasicNameValuePair("pet_colors", petColor));
            nameValuePairs.add(new BasicNameValuePair("pet_doctor", petDoctor));
            nameValuePairs.add(new BasicNameValuePair("pet_owner", petOwner));

            System.out.println("imagePath1 " + imagePath);



           if (!imagePath.equals("")) {
                File file = new File(imagePath);
                System.out.println("imagePath2 " + file.getName());
                nameValuePairs.add(new BasicNameValuePair("pet_image", file.getName()));
//                RequestBody.create(getMediaType(file.toURI()), file);
            }


            request.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));
            HttpResponse response = client.execute(request);
            result = request(response);
        }catch(Exception ex){
//            result = "Unable to connect.";
            ex.printStackTrace();

        }
        return result;
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

    public static String request(HttpResponse response){
        String result = "";
        try{
            InputStream in = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder str = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null){
                str.append(line + "\n");
            }
            in.close();
            result = str.toString();
        }catch(Exception ex){
            result = "Error";
        }
        return result;
    }

    // method to show toast message
    public void resultAlert(String HasilProses){
        if(HasilProses.trim().equalsIgnoreCase("OK")){
            Toast.makeText(ActivityAddPetDetail.this, R.string.ok, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(ActivityAddPetDetail.this, ActivityMenuPets.class);
            startActivity(i);
            finish();
        }else if(HasilProses.trim().equalsIgnoreCase("Failed")){
            Toast.makeText(ActivityAddPetDetail.this, R.string.error_server, Toast.LENGTH_SHORT).show();
        }else{
            Log.d("HasilProses", HasilProses);
        }
    }


    // method to create date picker dialog
    public static class DatePickerBirth extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // set default date
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // get selected date
            mYear = year;
            mMonth = month;
            mDay = day;

            // show selected date to date button
            btnDateBirth.setText(new StringBuilder()
                    .append(mYear).append("-")
                    .append(mMonth + 1).append("-")
                    .append(mDay).append(" "));
        }
    }

    // method to create date picker dialog
    public static class DatePickerOwner extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // set default date
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // get selected date
            mYear = year;
            mMonth = month;
            mDay = day;

            // show selected date to date button
            btnDateOwner.setText(new StringBuilder()
                    .append(mYear).append("-")
                    .append(mMonth + 1).append("-")
                    .append(mDay).append(" "));
        }
    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {

            Uri uri = data.getData();
            imagePath = methods.getPathImage(uri);
            System.out.println("image add  " + methods.getPathImage(uri));

            try {
                Bitmap bitmap_upload = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                btnPhoto.setImageBitmap(bitmap_upload);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

}
