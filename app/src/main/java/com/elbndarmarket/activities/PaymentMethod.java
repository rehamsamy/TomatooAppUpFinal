package com.elbndarmarket.activities;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.elbndarmarket.R;
import com.elbndarmarket.networking.NetworkAvailable;
import com.paytabs.paytabs_sdk.utils.PaymentParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaymentMethod extends AppCompatActivity {

    @BindView(R.id.credit_card_info_layout_id)
    ConstraintLayout cardLayoutInfo;
    @BindView(R.id.card_number_ed_id)
    EditText card_number_ed;
    @BindView(R.id.expireDate_ed_id)
    EditText expireDate_ed;
    @BindView(R.id.CVV_ed_id)
    EditText CVV_ed;
    @BindView(R.id.cardHolder_name_ed_id)
    EditText cardHolder_name_ed;
    @BindView(R.id.checkBox_save_card_info)
    CheckBox save_card_info_chBox;
    @BindView(R.id.creditCart_done_btn_id)
    Button done_btn;

    private final String TAG = this.getClass().getSimpleName();
    private int paymentMethod_state = 1;
    private NetworkAvailable networkAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        if (paymentMethod_state == 1 || paymentMethod_state == 2) {
            cardLayoutInfo.setVisibility(View.GONE);
        }

        save_card_info_chBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }

    @OnClick(R.id.creditCart_done_btn_id)
    public void setDone_btn() {
        // Put the String to pass back into an Intent and close this activity
        Intent intent = new Intent();
        intent.putExtra("payment_state", String.valueOf(paymentMethod_state));
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onRadioPaymentClicked(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.onCach_btn_id:
                paymentMethod_state = 1;
                cardLayoutInfo.setEnabled(false);
                cardLayoutInfo.setVisibility(View.INVISIBLE);
                break;
            case R.id.onDelivery_btn_id:
                paymentMethod_state = 2;
                cardLayoutInfo.setEnabled(false);
                cardLayoutInfo.setVisibility(View.INVISIBLE);
                break;
            case R.id.onVisa_btn_clicked:
                paymentMethod_state = 3;
                cardLayoutInfo.setEnabled(false);
                cardLayoutInfo.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @OnClick(R.id.payment_back_txtV_id)
    public void goBack() {
        finish();
    }
}
