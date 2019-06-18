package com.elbndarmarket.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.elbndarmarket.R;
import com.elbndarmarket.model.UpdateAddressResponse;
import com.elbndarmarket.model.UserModel;
import com.elbndarmarket.networking.ApiClient;
import com.elbndarmarket.networking.ApiServiceInterface;
import com.elbndarmarket.networking.NetworkAvailable;
import com.fourhcode.forhutils.FUtilsValidation;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.oob.SignUp;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetLocationManually extends AppCompatActivity {

    @BindView(R.id.get_location_layout_id)
    LinearLayout get_location_layout;
    @BindView(R.id.address_val_txtV_id)
    TextView address_val_txtV;
    @BindView(R.id.building_num_ed_id)
    EditText building_num_ed;
    @BindView(R.id.floor_num_ed_id)
    EditText floor_num_ed;
    @BindView(R.id.apartment_num_ed_id)
    EditText apartment_num_ed;
    @BindView(R.id.villa_num_ed_id)
    EditText villa_num_ed;
    @BindView(R.id.other_ed_id)
    EditText other_ed;
    @BindView(R.id.setLocation_btn_id)
    Button setLocation_btn_id;
    @BindView(R.id.setLocation_progress_id)
    ProgressBar progressBar;
    @BindView(R.id.manually_address_ed_id)
    EditText manually_address_ed;
    @BindView(R.id.manually_country_ed_id)
    EditText manually_country_ed;
    @BindView(R.id.manually_city_ed_id)
    EditText manually_city_ed;

    private int PLACE_PICKER_REQUEST = 1;
    double latitude, longtitude = 0.0;
    String address, cityName, streetName, locality;
    String building_No, floor_No, apartment_No, villa_No, other_txt = "";
    public static UserModel userData;
    private NetworkAvailable networkAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location_manually);
        ButterKnife.bind(this);
        networkAvailable = new NetworkAvailable(this);

        if (getIntent().hasExtra("user_data")) {
            userData = getIntent().getExtras().getParcelable("user_data");
        }
    }

    @OnClick(R.id.get_location_layout_id)
    public void getLocation() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(SetLocationManually.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.setLocation_btn_id)
    public void setLocation() {
        if (networkAvailable.isNetworkAvailable()) {
            if (networkAvailable.isNetworkAvailable()) {
                if (!FUtilsValidation.isEmpty(manually_address_ed, getString(R.string.required))
                        && !FUtilsValidation.isEmpty(manually_country_ed, getString(R.string.required))
                        && !FUtilsValidation.isEmpty(manually_city_ed, getString(R.string.required))
                ) {
                    building_No = building_num_ed.getText().toString();
                    floor_No = floor_num_ed.getText().toString();
                    apartment_No = apartment_num_ed.getText().toString();
                    villa_No = villa_num_ed.getText().toString();
                    other_txt = other_ed.getText().toString();

                    progressBar.setVisibility(View.VISIBLE);
                    ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
                    Call<UpdateAddressResponse> call = serviceInterface.userAddress(manually_country_ed.getText().toString(), manually_city_ed.getText().toString(), manually_address_ed.getText().toString()
                            , building_No, floor_No, apartment_No, villa_No, other_txt, latitude, longtitude, userData.getId(), userData.getToken());
                    call.enqueue(new Callback<UpdateAddressResponse>() {
                        @Override
                        public void onResponse(Call<UpdateAddressResponse> call, Response<UpdateAddressResponse> response) {
                            if (response.body().getStatus()) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(SetLocationManually.this, response.body().getMessages(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SetLocationManually.this, MainActivity.class);
                                intent.putExtra("user_data", userData);
                                startActivity(intent);
                                finish();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(SetLocationManually.this, response.body().getMessages(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UpdateAddressResponse> call, Throwable t) {
                            t.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
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
                        locality = addresses.get(0).getLocality();
                        Log.e("cityName: ", addresses.get(0).getAddressLine(0));
                        address_val_txtV.setText(address);
                        Log.e("stret1: ", addresses.get(0).getCountryName());
                        Log.e("stret2: ", addresses.get(0).getLocality());
                        Log.e("stret3: ", addresses.get(0).getAdminArea());
                        Log.e("stret4: ", addresses.get(0).getAddressLine(maxAddressLine));
                        Log.e("stret5: ", addresses.get(0).getFeatureName());
                        Log.e("stret7: ", addresses.get(0).getSubAdminArea());
                    } else {
                        // do your stuff
                        Toast.makeText(this, "لم يتم تحديد الموقع بعد!, حاول مرة اخرى", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "لم يتم تحديد الموقع بعد!, حاول مرة اخرى", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
