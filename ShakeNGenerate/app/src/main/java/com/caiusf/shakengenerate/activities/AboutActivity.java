package com.caiusf.shakengenerate.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.caiusf.shakengenerate.BuildConfig;
import com.caiusf.shakengenerate.R;

/**
 * Created by caius.florea on 03-Jan-17.
 */

public class AboutActivity extends AppCompatActivity {

    TextView tView;
    ImageView imgView;

    TextView appVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_about);

        tView = (TextView) findViewById(R.id.visitFbProfileText);
        imgView = (ImageView) findViewById(R.id.visitFbProfileIcon);
        appVersion = (TextView) findViewById(R.id.appVersion);

        appVersion.setText(getResources().getString(R.string.version) + " " + BuildConfig.VERSION_NAME);

        tView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchFacebook();
            }
        });

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchFacebook();
            }
        });

    }

    private final void launchFacebook() {
        String YourPageURL = "https://www.facebook.com/n/?caius.florea";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YourPageURL));

        startActivity(browserIntent);
    }
}
