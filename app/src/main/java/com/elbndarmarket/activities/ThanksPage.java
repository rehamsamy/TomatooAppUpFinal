package com.elbndarmarket.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.elbndarmarket.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ThanksPage extends AppCompatActivity {

    @BindView(R.id.track_order_btn_id)
    Button track_order_btn;
    @BindView(R.id.back_to_shop_btn_id)
    Button back_to_shop_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks_page);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.track_order_btn_id)
    public void track_Order() {

//        Toast.makeText(this, "Track Order", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.back_to_shop_btn_id)
    public void back_ToShop() {
        finish();
    }
}
