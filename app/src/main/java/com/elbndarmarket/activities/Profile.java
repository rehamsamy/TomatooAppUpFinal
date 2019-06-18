package com.elbndarmarket.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.elbndarmarket.R;
import com.elbndarmarket.utils.PreferencesHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Profile extends AppCompatActivity {

    @BindView(R.id.profile_user_img_id)
    ImageView profile_image;
    @BindView(R.id.profile_name_txtV_id)
    TextView userName_txtV;
    @BindView(R.id.profile_phone_txtV_id)
    TextView userPhone_txtV;
    @BindView(R.id.profile_email_txtV_id)
    TextView userEmail_txtV;
    @BindView(R.id.profile_edit_btn_id)
    ImageView edit_imgV;
    @BindView(R.id.address_val_txtV_id)
    TextView address_txtV;
    @BindView(R.id.address_eng_icon_imageV_id)
    TextView address_eng_icon_imageV;
    @BindView(R.id.address_arb_icon_imageV_id)
    TextView address_arb_icon_imageV;
    @BindView(R.id.payment_eng_icon_imageV_id)
    TextView payment_eng_icon_imageV;
    @BindView(R.id.payment_arb_icon_imageV_id)
    TextView payment_arb_icon_imageV;
    @BindView(R.id.password_eng_icon_imageV_id)
    TextView password_eng_icon_imageV;
    @BindView(R.id.password_arb_icon_imageV_id)
    TextView password_arb_icon_imageV;
    @BindView(R.id.payment_val_txtV_id)
    TextView payment_txtV;
    @BindView(R.id.password_val_txtV_id)
    TextView password_txtV;
    @BindView(R.id.profile_back_txtV_id)
    ImageView back;
    @BindView(R.id.profile_password_layout_id)
    RelativeLayout password_layout;
    @BindView(R.id.profile_address_layout_id)
    RelativeLayout address_layout;
    @BindView(R.id.profile_payment_layout_id)
    RelativeLayout paymentMethod_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        userName_txtV.setText(MainActivity.userData.getName());
        userEmail_txtV.setText(MainActivity.userData.getEmail());
        userPhone_txtV.setText(MainActivity.userData.getPhone());
        Glide.with(this)
                .load(MainActivity.userData.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                .centerCrop()
                .crossFade()
                .into(profile_image);

        if (PreferencesHelper.getSomeStringValue(Profile.this).equals("ar")) {
            Log.i("lang: ", PreferencesHelper.getSomeStringValue(this));
            address_arb_icon_imageV.setVisibility(View.VISIBLE);
            payment_arb_icon_imageV.setVisibility(View.VISIBLE);
            password_arb_icon_imageV.setVisibility(View.VISIBLE);
            address_eng_icon_imageV.setVisibility(View.GONE);
            payment_eng_icon_imageV.setVisibility(View.GONE);
            password_eng_icon_imageV.setVisibility(View.GONE);
        } else {
            address_arb_icon_imageV.setVisibility(View.GONE);
            payment_arb_icon_imageV.setVisibility(View.GONE);
            password_arb_icon_imageV.setVisibility(View.GONE);
            address_eng_icon_imageV.setVisibility(View.VISIBLE);
            payment_eng_icon_imageV.setVisibility(View.VISIBLE);
            password_eng_icon_imageV.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.profile_edit_btn_id)
    public void edit_profile() {
        Intent intent = new Intent(Profile.this, EditProfile.class);
        startActivity(intent);
    }

    @OnClick(R.id.profile_back_txtV_id)
    public void go_back() {
        finish();
    }

    @OnClick(R.id.profile_password_layout_id)
    public void passwordLayoutClicked() {
        Intent intent = new Intent(Profile.this, ChangePass.class);
        startActivity(intent);
    }

    @OnClick(R.id.profile_address_layout_id)
    public void addressLayoutClicked() {
        Intent intent = new Intent(Profile.this, UpdateUserLocation.class);
        startActivity(intent);
    }

    @OnClick(R.id.profile_payment_layout_id)
    public void paymentLayoutClicked() {

    }
}
