package com.elbndarmarket.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.elbndarmarket.R;
import com.elbndarmarket.model.UserLoginModel;
import com.elbndarmarket.model.UserModel;
import com.elbndarmarket.networking.ApiClient;
import com.elbndarmarket.networking.ApiServiceInterface;
import com.elbndarmarket.networking.NetworkAvailable;
import com.fourhcode.forhutils.FUtilsValidation;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogIn extends AppCompatActivity {

    @BindView(R.id.email_ed_id)
    EditText userName_ed;
    @BindView(R.id.pass_ed_id)
    EditText pass_ed;
    @BindView(R.id.login_btn_id)
    Button login_btn;
    @BindView(R.id.signUp_txtV_id)
    TextView signUp_txtV;
    @BindView(R.id.forget_pass_txtV_id)
    TextView forget_pass_txtV;

    // put data to shared preferences ...
    SharedPreferences.Editor user_data_edito;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    public static final String MY_PREFS_NAME = "all_user_data";
    public static final String MY_PREFS_login = "login_data";

    private String TAG = this.getClass().getName();
    NetworkAvailable networkAvailable;

    private UserModel userModel;
    private SpotsDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);

        //   get data from shared preferences ...
        sharedPreferences = getSharedPreferences(MY_PREFS_login, MODE_PRIVATE);
        userName_ed.setText(sharedPreferences.getString("email", ""));//"No name defined" is the default value.
        pass_ed.setText(sharedPreferences.getString("password", "")); //0 is the default value.

        networkAvailable = new NetworkAvailable(this);
        pass_ed.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (pass_ed.getText().toString().length() < 6) {
                        Log.i(TAG, "Not Valid change listner ...");
                        pass_ed.setError("Password should be greater than 6 characters!");
                    } else {
                        //validation is true, so what to put here?
                        Log.i(TAG, "Valid change listner ...");
                        Drawable myIcon = getResources().getDrawable(R.drawable.ic_done_24dp);
                        myIcon.setBounds(0, 0, myIcon.getIntrinsicWidth(), myIcon.getIntrinsicHeight());
                        pass_ed.setError("Good", myIcon);
                    }
                }
            }
        });
    }


    @OnClick(R.id.login_btn_id)
    public void login() {
        if (networkAvailable.isNetworkAvailable()) {
            if (!FUtilsValidation.isEmpty(userName_ed, getString(R.string.required))
//                    && FUtilsValidation.isValidEmail(userName_ed, getString(R.string.enter_valid_email))
                    && !FUtilsValidation.isEmpty(pass_ed, getString(R.string.required))) {

                if (pass_ed.getText().toString().length() < 6) {
                    pass_ed.setError(getResources().getString(R.string.pass_is_weak));
                    pass_ed.requestFocus();
                    return;
                }
                dialog = new SpotsDialog(LogIn.this, getString(R.string.logining), R.style.Custom);
                dialog.show();

                ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
                Call<UserLoginModel> call = serviceInterface.userLogin(userName_ed.getText().toString(), pass_ed.getText().toString());
                call.enqueue(new Callback<UserLoginModel>() {
                    @Override
                    public void onResponse(Call<UserLoginModel> call, Response<UserLoginModel> response) {
                        userModel = new UserModel();
                        UserLoginModel userLoginModel = response.body();
                        dialog.dismiss();
                        if (userLoginModel.getStatus()) {
                            UserLoginModel.User user = userLoginModel.getUser();
                            userModel.setId(user.getId());
                            userModel.setName(user.getUsername());
                            userModel.setEmail(user.getEmail());
                            userModel.setPhone(user.getMobile());
                            userModel.setImage(user.getUser_image());
                            userModel.setToken(userLoginModel.getToken());

                            // Convert User Data to Gon OBJECT ...
                            Gson gson = new Gson();
                            String user_data = gson.toJson(userModel);
                            user_data_edito = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            user_data_edito.putString("user_data", user_data);
                            user_data_edito.commit();
                            user_data_edito.apply();

                            Intent intent = new Intent(LogIn.this, MainActivity.class);
                            intent.putExtra("user_data", userModel);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LogIn.this, userLoginModel.getMessages(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserLoginModel> call, Throwable t) {
                        t.printStackTrace();
                        dialog.dismiss();
                    }
                });
            }
        } else
            Toast.makeText(this, R.string.error_connection, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.signUp_txtV_id)
    public void register() {
        Intent intent = new Intent(LogIn.this, Register.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_right);
    }

    @OnClick(R.id.forget_pass_txtV_id)
    public void forgetPass() {
        Intent intent = new Intent(LogIn.this, ForgetPassword.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
    }

    @Override
    protected void onPause() {
        super.onPause();
        editor = sharedPreferences.edit();
        editor.putString("email", userName_ed.getText().toString());
        editor.putString("password", pass_ed.getText().toString());
        editor.commit();
        editor.apply();
    }
}
