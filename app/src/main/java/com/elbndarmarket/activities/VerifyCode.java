package com.elbndarmarket.activities;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elbndarmarket.R;
import com.fourhcode.forhutils.FUtilsValidation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VerifyCode extends AppCompatActivity {

    @BindView(R.id.verify_code_btn_id)
    Button send_btn;
    @BindView(R.id.verifyCode_email_id)
    EditText verifyCode_ed;
    @BindView(R.id.verifyCode_back_txtV_id)
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.verifyCode_back_txtV_id)
    public void go_back() {
        finish();
    }

    @OnClick(R.id.verify_code_btn_id)
    public void send_code() {
        if (!FUtilsValidation.isEmpty(verifyCode_ed, getString(R.string.required))) {

        }
    }
}
