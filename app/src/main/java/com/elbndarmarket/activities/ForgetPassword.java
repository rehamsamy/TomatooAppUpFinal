package com.elbndarmarket.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.elbndarmarket.R;
import com.fourhcode.forhutils.FUtilsValidation;

import butterknife.BindFloat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetPassword extends AppCompatActivity {

    @BindView(R.id.forget_pass_email_id)
    EditText email_ed;
    @BindView(R.id.forget_pass_send_btn_id)
    Button send_btn;
    @BindView(R.id.forgetPass_back_txtV_id)
    TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.forget_pass_send_btn_id)
    public void send_email() {

        if (!FUtilsValidation.isEmpty(email_ed, getString(R.string.required))
                && FUtilsValidation.isValidEmail(email_ed, getString(R.string.enter_valid_email))) {

            Toast.makeText(this, "Send btn Clicked", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.forgetPass_back_txtV_id)
    public void go_back() {
        finish();
    }
}
