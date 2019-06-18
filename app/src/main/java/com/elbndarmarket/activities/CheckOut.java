package com.elbndarmarket.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.elbndarmarket.R;
import com.elbndarmarket.adapters.CheckOutRecyclerAdapter;
import com.elbndarmarket.model.CartProductsModel;
import com.elbndarmarket.model.CheckOutListModel;
import com.elbndarmarket.model.CheckOutResponseModel;
import com.elbndarmarket.model.DeleteCartModel;
import com.elbndarmarket.model.FeaturedProductsModel;
import com.elbndarmarket.networking.ApiClient;
import com.elbndarmarket.networking.ApiServiceInterface;
import com.elbndarmarket.utils.PreferencesHelper;
import com.fourhcode.forhutils.FUtilsValidation;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.paytabs.paytabs_sdk.payment.ui.activities.PayTabActivity;
import com.paytabs.paytabs_sdk.utils.PaymentParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOut extends AppCompatActivity {

    @BindView(R.id.checkout_back_txtV_id)
    ImageView back_txtV;
    @BindView(R.id.checkout_items_recyclerV_id)
    RecyclerView order_items_recyclerV;
    @BindView(R.id.checkOut_payment_layout_id)
    RelativeLayout paymentMethod_layout;
    @BindView(R.id.checkOut_address_layout_id)
    RelativeLayout currency_laytout;

    @BindView(R.id.deliveryAddress_val_txtV_id)
    TextView address_val_txtV;
    @BindView(R.id.checkOut_btn_id)
    Button checkOut_btn;
    @BindView(R.id.checkout_items_price_val_txtV)
    TextView items_price_txtV;
    @BindView(R.id.checkout_delivery_fee_val_txtV)
    TextView delivery_fee_txtV;
    @BindView(R.id.checkout_total_price_val_txtV)
    TextView order_totalPrice_txtV;
    @BindView(R.id.deliveryTime_txtV_id)
    TextView detectTime_txtV;
    @BindView(R.id.deliveryDate_txtV_id)
    TextView detectDate_txtV;
    private static TextView date_txtV_val;
    @BindView(R.id.checkout_delTime_val_txtV_id)
    TextView time_txtV_val;
    @BindView(R.id.payment_val_txtV_id)
    TextView payment_val_txtV;

    @BindView(R.id.checkout_address_eng_icon_imageV)
    TextView checkout_address_eng_icon;
    @BindView(R.id.checkout_address_arb_icon_imageV)
    TextView checkout_address_arb_icon;
    @BindView(R.id.checkout_payment_eng_icon_imageV)
    TextView checkout_payment_eng_icon;
    @BindView(R.id.checkout_payment_arb_icon_imageV)
    TextView checkout_payment_arb_icon;

    ArrayList<CheckOutListModel> cart_list;
    ArrayList<CartProductsModel.Product> show_list;
    double items_price, cart_total_cost, delivery_fee;
    private CheckOutRecyclerAdapter checkOutAdapter;
    double latitude, longtitude;
    String address, cityName, streetName;
    String list_obj;
    public static int order_id;
    private SpotsDialog dialog;

    private int PLACE_PICKER_REQUEST = 10;
    private int payment_request_code = 20;
    private String payment_response = "";
    private int Cart_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        ButterKnife.bind(this);

        date_txtV_val = findViewById(R.id.deliveryDate_val_txtV_id);
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            cart_list = bundle.getParcelableArrayList("card_list");
            show_list = bundle.getParcelableArrayList("show_list");
            items_price = bundle.getDouble("items_price");
            cart_total_cost = bundle.getDouble("order_cost");
            delivery_fee = bundle.getDouble("delivery_fee");
            Cart_id = bundle.getInt("Cart_id");
            Gson gson = new Gson();
            list_obj = gson.toJson(cart_list);
            Log.i("list: ", list_obj);
            Log.i("items_price: ", items_price + "");
            Log.i("cart_total_cost: ", cart_total_cost + "");

            buildOrdersRecycler(cart_list);

            // Set Data To Views ...
            String items_price_AsString = String.format("%.2f", items_price);
            items_price_txtV.setText(items_price_AsString);
            items_price_txtV.append("$");
            delivery_fee_txtV.setText(String.valueOf(delivery_fee));
            delivery_fee_txtV.append("$");
            cart_total_cost = items_price + delivery_fee;
            String cart_total_cost_AsString = String.format("%.2f", cart_total_cost);
            order_totalPrice_txtV.setText(cart_total_cost_AsString);
            order_totalPrice_txtV.append("$");
        }

        if (PreferencesHelper.getSomeStringValue(CheckOut.this).equals("ar")) {
            checkout_address_arb_icon.setVisibility(View.VISIBLE);
            checkout_payment_arb_icon.setVisibility(View.VISIBLE);
            checkout_address_eng_icon.setVisibility(View.GONE);
            checkout_payment_eng_icon.setVisibility(View.GONE);
        } else {
            checkout_address_arb_icon.setVisibility(View.GONE);
            checkout_payment_arb_icon.setVisibility(View.GONE);
            checkout_address_eng_icon.setVisibility(View.VISIBLE);
            checkout_payment_eng_icon.setVisibility(View.VISIBLE);
        }
    }

    private void buildOrdersRecycler(ArrayList<CheckOutListModel> cart_list) {
        order_items_recyclerV.setLayoutManager(new LinearLayoutManager(this));
        order_items_recyclerV.setHasFixedSize(true);

        // Set Adapter ..
        checkOutAdapter = new CheckOutRecyclerAdapter(CheckOut.this, show_list);
        order_items_recyclerV.setAdapter(checkOutAdapter);
    }

    @OnClick(R.id.checkout_back_txtV_id)
    public void checkOut_Back() {
        finish();
    }

    @OnClick(R.id.checkOut_address_layout_id)
    public void setCurrency_laytout() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(CheckOut.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.checkOut_payment_layout_id)
    public void setPaymentMethod_layout() {
        Intent intent = new Intent(CheckOut.this, PaymentMethod.class);
        startActivityForResult(intent, payment_request_code);
    }

    @OnClick(R.id.checkOut_btn_id)
    public void checkOutOrder() {
        if (payment_request_code == 0 || payment_val_txtV.getText().toString().equals(getString(R.string.payment_method))) {
            Toast.makeText(this, getString(R.string.choose_payment_first), Toast.LENGTH_LONG).show();
            return;
        }
        if (!payment_response.equals("100") && payment_val_txtV.getText().toString().equals(getString(R.string.online_byCreditCard))) {
            Toast.makeText(this, getString(R.string.complete_payment_first), Toast.LENGTH_LONG).show();
            return;
        }
        dialog = new SpotsDialog(CheckOut.this, getString(R.string.sending), R.style.Custom);
        dialog.show();

        Log.i("user_id", MainActivity.userData.getId() + "");
        Log.i("token", MainActivity.userData.getToken());
        Log.i("items_price", items_price + "");
        Log.i("cart_list", cart_list.size() + "");
        Log.i("paymethod", payment_val_txtV.getText().toString());
        Log.i("cart_total_cost", cart_total_cost + "");
        Log.i("list_obj", list_obj);
        Log.i("cart_id", Cart_id + "");
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<CheckOutResponseModel> call = serviceInterface.makeCheckOut(MainActivity.userData.getId(), items_price, cart_list.size(), payment_val_txtV.getText().toString(), cart_total_cost + delivery_fee, MainActivity.userData.getToken(), list_obj, Cart_id);
        call.enqueue(new Callback<CheckOutResponseModel>() {
            @Override
            public void onResponse(Call<CheckOutResponseModel> call, Response<CheckOutResponseModel> response) {
                CheckOutResponseModel checkOutResponseModel = response.body();
                dialog.dismiss();
                if (checkOutResponseModel.getStatus()) {
                    order_id = checkOutResponseModel.getOrder_id();
                    MainActivity.cart_count = 0;
                    Intent intent = new Intent(CheckOut.this, ThanksPage.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<CheckOutResponseModel> call, Throwable t) {
                t.printStackTrace();
                dialog.dismiss();
            }
        });
    }

    @OnClick(R.id.deliveryTime_txtV_id)
    public void getTime() {
        // Inflate Time Picker ....
        Calendar mcurrenttime = Calendar.getInstance();
        int hour = mcurrenttime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrenttime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(CheckOut.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                String needed_order_time = selectedHour + ":" + selectedMinute;
                time_txtV_val.setText(needed_order_time);
            }
        }, hour, minute, false);
        mTimePicker.setTitle(getString(R.string.detect_delivery_time));
        mTimePicker.show();
    }

    @OnClick(R.id.deliveryDate_txtV_id)
    public void getDate() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                address = String.format("%s", place.getAddress());
                latitude = place.getLatLng().latitude;
                longtitude = place.getLatLng().longitude;

                Log.e("lat", latitude + "");
                Log.e("lan", longtitude + "");
                Log.e("address", address);
                Geocoder gcd = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = gcd.getFromLocation(latitude, longtitude, 1);

                    if (addresses.size() > 0 && addresses != null) {

                        cityName = addresses.get(0).getSubAdminArea();

                        int maxAddressLine = addresses.get(0).getMaxAddressLineIndex();
                        streetName = addresses.get(0).getFeatureName();
//                        locality = addresses.get(0).getSubLocality();
                        String admin_adrea = addresses.get(0).getAdminArea();

                        address_val_txtV.setText(address);
                    } else {
                        // do your stuff
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == payment_request_code) {
            if (resultCode == RESULT_OK) {
                // Get String data from Intent
                String payment_state = data.getStringExtra("payment_state");
                Log.i("payment: ", payment_state);
                if (payment_state.equals("1")) {
                    payment_val_txtV.setText(getResources().getString(R.string.cache_on_delivery));
                } else if (payment_state.equals("2")) {
                    openPaymentPage(Double.parseDouble(cart_total_cost + ""));
//                    payment_val_txtV.setText(getResources().getString(R.string.online_byCreditCard));
                } else if (payment_state.equals("3")) {
                    payment_val_txtV.setText(getResources().getString(R.string.by_Visa));
                }
            }
        } else if (resultCode == RESULT_OK && requestCode == PaymentParams.PAYMENT_REQUEST_CODE && data != null) {
            Log.e("RESPONSE_CODE: ", data.getStringExtra(PaymentParams.RESPONSE_CODE));
            Log.e("TRANSACTION_ID: ", data.getStringExtra(PaymentParams.TRANSACTION_ID));
            payment_response = data.getStringExtra(PaymentParams.RESPONSE_CODE);
//            Log.e("AMOUNT: ", data.getDoubleExtra(PaymentParams.AMOUNT, 0.0) + "");
//            if (data.hasExtra(PaymentParams.TOKEN) && !data.getStringExtra(PaymentParams.TOKEN).isEmpty()) {
//                Log.e("TOKEN: ", data.getStringExtra(PaymentParams.TOKEN));
//                Log.e("CUSTOMER_EMAIL: ", data.getStringExtra(PaymentParams.CUSTOMER_EMAIL));
//                Log.e("CUSTOMER_PASSWORD: ", data.getStringExtra(PaymentParams.CUSTOMER_PASSWORD));
//            }
            if (payment_response.equals("100")) {
                payment_val_txtV.setText(getResources().getString(R.string.online_byCreditCard));
                Toast.makeText(this, getString(R.string.payment_successfully), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.payment_failed), Toast.LENGTH_SHORT).show();
                deleteCart(Cart_id);
                return;
            }
        } else {
            Toast.makeText(this, getString(R.string.payment_failed), Toast.LENGTH_SHORT).show();
        }
    }


    /// ***********************************************************
    public static class DatePickerFragment extends android.app.DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        public static String date;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
//            Toast.makeText(getActivity(), year + " / " + month + " / " + day , Toast.LENGTH_SHORT).show();
            date = day + "/" + month + "/" + year;
            date_txtV_val.setText(date);
        }
    }

    //InFlate Payment Page ...
    private void openPaymentPage(Double cart_total_cost) {
//        Toast.makeText(this, "" + cart_total_cost, Toast.LENGTH_SHORT).show();
        Intent in = new Intent(getApplicationContext(), PayTabActivity.class);
        in.putExtra(PaymentParams.MERCHANT_EMAIL, "eng.tahaalaa@gmail.com"); //this a demo account for testing the sdk
        in.putExtra(PaymentParams.SECRET_KEY, "k8r2dbtF0SCLcNBAHfBHiv7ppbL9tCVNLnQZyjEzbhko5WYMbAut6BTvNEFhhB53YV2xfH6rZqP4nORypl2vXV1LAG3Si8Vg4YOj");//Add your Secret Key Here
        in.putExtra(PaymentParams.LANGUAGE, PaymentParams.ENGLISH);
        in.putExtra(PaymentParams.TRANSACTION_TITLE, "PayMent");
        in.putExtra(PaymentParams.AMOUNT, cart_total_cost);

        in.putExtra(PaymentParams.CURRENCY_CODE, "SAR");
        in.putExtra(PaymentParams.CUSTOMER_PHONE_NUMBER, MainActivity.userData.getPhone());
        in.putExtra(PaymentParams.CUSTOMER_EMAIL, MainActivity.userData.getEmail());
        in.putExtra(PaymentParams.ORDER_ID, "123456");
        in.putExtra(PaymentParams.PRODUCT_NAME, "Product 1, Product 2");

        //Billing Address
        in.putExtra(PaymentParams.ADDRESS_BILLING, "Flat 1,Building 123, Road 2345");
        in.putExtra(PaymentParams.CITY_BILLING, "Manama");
        in.putExtra(PaymentParams.STATE_BILLING, "Manama");
        in.putExtra(PaymentParams.COUNTRY_BILLING, "BHR");
        in.putExtra(PaymentParams.POSTAL_CODE_BILLING, "00973"); //Put Country Phone code if Postal code not available '00973'

        //Shipping Address
        in.putExtra(PaymentParams.ADDRESS_SHIPPING, "Flat 1,Building 123, Road 2345");
        in.putExtra(PaymentParams.CITY_SHIPPING, "Manama");
        in.putExtra(PaymentParams.STATE_SHIPPING, "Manama");
        in.putExtra(PaymentParams.COUNTRY_SHIPPING, "BHR");
        in.putExtra(PaymentParams.POSTAL_CODE_SHIPPING, "00973"); //Put Country Phone code if Postal code not available '00973'

        //Payment Page Style
        in.putExtra(PaymentParams.PAY_BUTTON_COLOR, getResources().getColor(R.color.colorPrimary));
        in.putExtra(PaymentParams.THEME, PaymentParams.THEME_LIGHT);

        //Tokenization
        in.putExtra(PaymentParams.IS_TOKENIZATION, true);
        startActivityForResult(in, PaymentParams.PAYMENT_REQUEST_CODE);
    }

    private void deleteCart(int cart_id) {
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<DeleteCartModel> call = serviceInterface.deleteCartProducts(cart_id, MainActivity.userData.getToken());
        call.enqueue(new Callback<DeleteCartModel>() {
            @Override
            public void onResponse(Call<DeleteCartModel> call, Response<DeleteCartModel> response) {
                DeleteCartModel deleteCartModel = response.body();
                if (deleteCartModel.getStatus()) {
                    Toast.makeText(CheckOut.this, deleteCartModel.getMessages(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CheckOut.this, deleteCartModel.getMessages(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteCartModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}

