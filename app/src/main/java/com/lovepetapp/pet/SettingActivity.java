package com.lovepetapp.pet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class SettingActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button btn_logout,btn_edit_profile;
    String selectedFragment = "";
    FragmentManager fm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = this.findViewById(R.id.toolbar_settingg);

        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        btn_logout = findViewById(R.id.btnLogout);
        btn_edit_profile = findViewById(R.id.btnEditProfile);

        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, ShowProfileActivity.class);
                startActivity(intent);

            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitDialog();
            }
        });

    }

        private void exitDialog() {
        AlertDialog.Builder alert;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alert = new AlertDialog.Builder(SettingActivity.this, R.style.ThemeDialog);
        } else {
            alert = new AlertDialog.Builder(SettingActivity.this);
        }

        alert.setTitle(getString(R.string.exit));
        alert.setMessage(getString(R.string.sure_exit));
        alert.setPositiveButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(SettingActivity.this, FirstLoginActivity.class);
                startActivity(intent);            }
        });
        alert.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.show();
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
