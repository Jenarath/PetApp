package com.lovepetapp.pet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lovepetapp.asyncTask.LoadLoginLocal;
import com.lovepetapp.interfaces.LoginListener;
import com.lovepetapp.sharedPref.SharePref;
import com.lovepetapp.utils.Constant;
import com.lovepetapp.utils.Methods;

import java.io.IOException;

import cn.refactor.library.SmoothCheckBox;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {

    LoadLoginLocal loadLoginLocal;
    SharePref sharePref;
    EditText editText_email, editText_password;
    Button button_login, button_skip,button_forgetpass;
    TextView textView_register, textView_forgotpass;
    Methods methods;
    ProgressDialog progressDialog;
    LinearLayout ll_checkbox;
    SmoothCheckBox cb_rememberme;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharePref = new SharePref(this);
        methods = new Methods(this);
        methods.forceRTLIfSupported(getWindow());
        methods.setStatusColor(getWindow(), null);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.login_in));
        progressDialog.setCancelable(false);

        editText_email = findViewById(R.id.et_login_email);
        editText_password = findViewById(R.id.et_login_password);
        button_login = findViewById(R.id.button_login);
        button_forgetpass = findViewById(R.id.button_forgetpass);




        button_forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (methods.isNetworkAvailable()) {
                    attemptLogin();
                } else {
                    methods.showToast(getString(R.string.net_not_conn));
                }
            }
        });
    }

    private void attemptLogin() {
        editText_email.setError(null);
        editText_password.setError(null);

        // Store values at the time of the login attempt.
        String email = editText_email.getText().toString();
        String password = editText_password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !methods.isPasswordValid(password)) {
            editText_password.setError(getString(R.string.error_invalid_password));
            focusView = editText_password;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            editText_email.setError(getString(R.string.error_field_required));
            focusView = editText_email;
            cancel = true;
        } else if (!methods.isEmailValid(email)) {
            editText_email.setError(getString(R.string.error_invalid_email));
            focusView = editText_email;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            loadLogin();
        }
    }

    private void openMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void loadLogin() {
        loadLoginLocal = new LoadLoginLocal(this, new LoginListener() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onEnd(String success, String message) {
                progressDialog.dismiss();
                if (success.equals("true")) {
                    if (message.equals("0")) {
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.email_pass_nomatch), Toast.LENGTH_SHORT).show();
                    } else {
                        Constant.isLogged = true;
                        sharePref.setSharedPreferences(editText_email.getText().toString(), editText_password.getText().toString());
                        methods.showToast(getString(R.string.login_success));
                        openMainActivity();
                    }
                } else {
                    methods.showToast(getString(R.string.error_login));
                }
            }
        });

        loadLoginLocal.execute(Constant.URL_LOGIN_1 + editText_email.getText().toString() + Constant.URL_LOGIN_2 + editText_password.getText().toString());
    }


}