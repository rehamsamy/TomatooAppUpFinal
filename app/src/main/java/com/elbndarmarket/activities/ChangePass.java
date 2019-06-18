package com.elbndarmarket.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elbndarmarket.R;
import com.elbndarmarket.model.ChangePasswordModel;
import com.elbndarmarket.networking.ApiClient;
import com.elbndarmarket.networking.ApiServiceInterface;
import com.elbndarmarket.networking.NetworkAvailable;
import com.fourhcode.forhutils.FUtilsValidation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePass extends AppCompatActivity {

    @BindView(R.id.changePass_back_txtV_id)
    ImageView back;
    @BindView(R.id.old_pass_ed_id)
    EditText oldPass_ed;
    @BindView(R.id.new_pass_ed_id)
    EditText newPass_ed;
    @BindView(R.id.confirm_newPass_ed_id)
    EditText confirm_newPass_ed;
    @BindView(R.id.save_pass_btn_id)
    Button savePass_btn;

    private NetworkAvailable networkAvailable;
    private SpotsDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);

    }

    @OnClick(R.id.changePass_back_txtV_id)
    public void go_back() {
        finish();
    }

    @OnClick(R.id.save_pass_btn_id)
    public void save_newPass() {
        if (networkAvailable.isNetworkAvailable()) {
            if (!FUtilsValidation.isEmpty(oldPass_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(newPass_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(confirm_newPass_ed, getString(R.string.required))) {

                if (oldPass_ed.getText().toString().length() < 6) {
                    oldPass_ed.setError(getResources().getString(R.string.phone_not_correct));
                    oldPass_ed.requestFocus();
                    return;
                }
                if (newPass_ed.getText().toString().length() < 6) {
                    newPass_ed.setError(getResources().getString(R.string.phone_not_correct));
                    newPass_ed.requestFocus();
                    return;
                }

                dialog = new SpotsDialog(ChangePass.this, getString(R.string.updatting), R.style.Custom);
                dialog.show();
                Log.i("old_pass: ", oldPass_ed.getText().toString());
                Log.i("new_pass: ", newPass_ed.getText().toString());
                Log.i("con_pass: ", confirm_newPass_ed.getText().toString());
                Log.i("token: ", MainActivity.userData.getToken());
                Log.i("token: ", MainActivity.userData.getId() + "");
                ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
                Call<ChangePasswordModel> call = serviceInterface.changePassword(MainActivity.userData.getId(), oldPass_ed.getText().toString(), newPass_ed.getText().toString(), confirm_newPass_ed.getText().toString(), MainActivity.userData.getToken());
                call.enqueue(new Callback<ChangePasswordModel>() {
                    @Override
                    public void onResponse(Call<ChangePasswordModel> call, Response<ChangePasswordModel> response) {
                        ChangePasswordModel changePasswordModel = response.body();
                        dialog.dismiss();
                        if (changePasswordModel.getStatus()) {
                            Log.i("status: ", changePasswordModel.getStatus() + "");
                            Toast.makeText(ChangePass.this, changePasswordModel.getMessages(), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ChangePass.this, changePasswordModel.getMessages(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ChangePasswordModel> call, Throwable t) {
                        t.printStackTrace();
                        dialog.dismiss();
                    }
                });
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }
    }
}
