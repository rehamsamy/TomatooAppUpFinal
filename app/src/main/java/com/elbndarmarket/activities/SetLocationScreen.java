package com.elbndarmarket.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.elbndarmarket.R;
import com.elbndarmarket.model.UpdateAddressResponse;
import com.elbndarmarket.model.UserModel;
import com.elbndarmarket.networking.ApiClient;
import com.elbndarmarket.networking.ApiServiceInterface;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetLocationScreen extends AppCompatActivity {

    @BindView(R.id.useCurrentLocation_btn_id)
    Button useCurrentLocation_btn;
    @BindView(R.id.setLocationManually_txtV)
    TextView setLocationManually_txtV;
    @BindView(R.id.setLocation_back_txtV_id)
    TextView back_txtV;
    @BindView(R.id.setLocation_progress_id)
    ProgressBar progressBar;

    private String building_no, floor_no, apartment_no, villa_no, other = "";
    String address, city, state, country, postalCode, knownName = "";
    Geocoder geocoder;
    List<Address> addresses;

    private GpsTracker gpsTracker;
    public static double latitude, longitude;
    public static UserModel userData;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location_screen);
        ButterKnife.bind(this);
        geocoder = new Geocoder(this, Locale.getDefault());
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        if (getIntent().hasExtra("user_data")) {
            userData = getIntent().getExtras().getParcelable("user_data");
        }
        gpsTracker = new GpsTracker(this);
    }

    @OnClick(R.id.useCurrentLocation_btn_id)
    public void useCurrentLocation() {
        if (gpsTracker.canGetLocation) {
            Location location = gpsTracker.getLocation();
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.i(TAG, String.valueOf(latitude));
            Log.i(TAG, String.valueOf(longitude));

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String stateName = addresses.get(0).getFeatureName();
                state = addresses.get(0).getAdminArea();
                country = addresses.get(0).getCountryName();
                postalCode = addresses.get(0).getPostalCode();
                knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                int maxAddressLine = addresses.get(0).getMaxAddressLineIndex();

                Log.i("address: ", address);
                Log.i("state: ", state);
                Log.i("country: ", country);
                Log.e("cityName: ", knownName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.VISIBLE);
            ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
            Call<UpdateAddressResponse> call = serviceInterface.userAddress(country, state, address, building_no, floor_no, apartment_no, villa_no, other, latitude, longitude, userData.getId(), userData.getToken());
            call.enqueue(new Callback<UpdateAddressResponse>() {
                @Override
                public void onResponse(Call<UpdateAddressResponse> call, Response<UpdateAddressResponse> response) {
                    if (response.body().getStatus()) {
                        Toast.makeText(SetLocationScreen.this, response.body().getMessages(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SetLocationScreen.this, MainActivity.class);
                        intent.putExtra("user_data", userData);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SetLocationScreen.this, response.body().getMessages(), Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<UpdateAddressResponse> call, Throwable t) {
                    t.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                }
            });
            Toast.makeText(SetLocationScreen.this, getString(R.string.get_location_success), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.setLocationManually_txtV)
    public void setLocationManually() {
        Intent intent = new Intent(SetLocationScreen.this, SetLocationManually.class);
        intent.putExtra("user_data", userData);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.setLocation_back_txtV_id)
    public void setBack_txtV() {
        finish();
    }

}
