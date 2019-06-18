package com.elbndarmarket.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.elbndarmarket.R;
import com.elbndarmarket.model.UpdateAddressResponse;
import com.elbndarmarket.networking.ApiClient;
import com.elbndarmarket.networking.ApiServiceInterface;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserLocation extends AppCompatActivity {

    @BindView(R.id.updateLocation_back_txtV_id)
    ImageView back_txtV;
    @BindView(R.id.get_curLocation_layout_id)
    LinearLayout get_curLocation_txtV;
    @BindView(R.id.updateAddress_val_txtV_id)
    TextView updateAddress_val_txtV;
    @BindView(R.id.updateBuilding_num_ed_id)
    EditText updateBuilding_num_ed;
    @BindView(R.id.updateFloor_num_ed_id)
    EditText updateFloor_num_ed;
    @BindView(R.id.updateApartment_num_ed_id)
    EditText updateApartment_num_ed;
    @BindView(R.id.updateVilla_num_ed_id)
    EditText updateVilla_num_ed;
    @BindView(R.id.updateOther_ed_id)
    EditText updateOther_ed;
    @BindView(R.id.updateLocation_btn_id)
    Button updateLocation_btn;
    @BindView(R.id.updateLocation_progress_id)
    ProgressBar progressBar;
    @BindView(R.id.update_address_ed_id)
    EditText update_address_ed;
    @BindView(R.id.update_country_ed_id)
    EditText update_country_ed;
    @BindView(R.id.update_city_ed_id)
    EditText update_city_ed;

    private int PLACE_PICKER_REQUEST = 110;
    String address, cityName, streetName, locality;
    String building_No, floor_No, apartment_No, villa_No, other_txt = "";
    double latitude, longtitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_location);
        ButterKnife.bind(this);

        update_address_ed.setText(MainActivity.userData.getAddress());
        update_country_ed.setText(MainActivity.userData.getCountry());
        update_city_ed.setText(MainActivity.userData.getCity());
        updateBuilding_num_ed.setText(MainActivity.userData.getBuilding_no());
        updateFloor_num_ed.setText(MainActivity.userData.getFloor_no());
        updateApartment_num_ed.setText(MainActivity.userData.getApartment_no());
        updateVilla_num_ed.setText(MainActivity.userData.getVilla_no());
        updateOther_ed.setText(MainActivity.userData.getOther());

    }

    @OnClick(R.id.updateLocation_btn_id)
    public void setUpdateLocation() {
        address = update_address_ed.getText().toString();
        locality = update_country_ed.getText().toString();
        cityName = update_city_ed.getText().toString();
        floor_No = updateFloor_num_ed.getText().toString();
        apartment_No = updateApartment_num_ed.getText().toString();
        villa_No = updateVilla_num_ed.getText().toString();
        other_txt = updateOther_ed.getText().toString();

        progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<UpdateAddressResponse> call = serviceInterface.userAddress(locality, cityName, address, building_No, floor_No, apartment_No, villa_No, other_txt, latitude, longtitude, MainActivity.userData.getId(), MainActivity.userData.getToken());
        call.enqueue(new Callback<UpdateAddressResponse>() {
            @Override
            public void onResponse(Call<UpdateAddressResponse> call, Response<UpdateAddressResponse> response) {
                if (response.body().getStatus()) {
                    Toast.makeText(UpdateUserLocation.this, response.body().getMessages(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UpdateUserLocation.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(UpdateUserLocation.this, response.body().getMessages(), Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<UpdateAddressResponse> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @OnClick(R.id.get_curLocation_layout_id)
    public void getLocation() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(UpdateUserLocation.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.updateLocation_back_txtV_id)
    public void setBack_txtV() {
        finish();
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
                        updateAddress_val_txtV.setText(address);
                        Log.e("stret1: ", addresses.get(0).getCountryName());
                        Log.e("stret2: ", addresses.get(0).getLocality());
                        Log.e("stret3: ", addresses.get(0).getAdminArea());
                        Log.e("stret4: ", addresses.get(0).getAddressLine(maxAddressLine));
                        Log.e("stret5: ", addresses.get(0).getFeatureName());
                        Log.e("stret7: ", addresses.get(0).getSubAdminArea());

                    } else {
                        // do your stuff
                        Toast.makeText(this, getString(R.string.try_location_again), Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, getString(R.string.try_location_again), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
