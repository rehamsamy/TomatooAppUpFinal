package com.elbndarmarket.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.elbndarmarket.R;
import com.elbndarmarket.model.UserModel;
import com.elbndarmarket.utils.PreferencesHelper;
import com.google.gson.Gson;

import java.util.Locale;

import butterknife.BindView;

public class Splash extends AppCompatActivity {

    Runnable runnable;
    Handler handler;

    ImageView logo_imageV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo_imageV = findViewById(R.id.splash_logo_imageV);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        logo_imageV.setAnimation(animation1);

        Log.i("lang: ", PreferencesHelper.getSomeStringValue(Splash.this));
        Log.i("lang: ", PreferencesHelper.getSomeStringValue(Splash.this));

        setConfig(PreferencesHelper.getSomeStringValue(Splash.this), Splash.this);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences prefs = getSharedPreferences(LogIn.MY_PREFS_NAME, MODE_PRIVATE);
                String user_data = prefs.getString("user_data", "");

                if (user_data != null && !user_data.equals("")) {
                    // Retrive Gson Object from Shared Prefernces ....
                    Gson gson = new Gson();
                    UserModel userModel = gson.fromJson(user_data, UserModel.class);
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    intent.putExtra("user_data", userModel);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
                } else {
                    Intent intent = new Intent(Splash.this, LogIn.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
                }
            }
        }, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    public void setConfig(String language, Context context) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
}
