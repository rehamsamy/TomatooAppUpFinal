package com.elbndarmarket.activities;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elbndarmarket.R;
import com.elbndarmarket.model.RegisterationModel;
import com.elbndarmarket.model.UserModel;
import com.elbndarmarket.networking.ApiClient;
import com.elbndarmarket.networking.ApiServiceInterface;
import com.elbndarmarket.networking.NetworkAvailable;
import com.fourhcode.forhutils.FUtilsValidation;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    @BindView(R.id.regis_userName_ed_id)
    EditText userName_ed;
    @BindView(R.id.regis_email_ed_id)
    EditText email_ed;
    @BindView(R.id.regis_phone_ed_id)
    EditText phone_ed;
    @BindView(R.id.regis_password_ed_id)
    EditText password_ed;
    @BindView(R.id.regis_conPass_ed_id)
    EditText confirmPass_ed;
    @BindView(R.id.register_btn_id)
    Button register_btn;
    @BindView(R.id.signIn_txtV_id)
    TextView signIn_txtV;
    @BindView(R.id.regis_user_img_id)
    ImageView user_image;
    @BindView(R.id.add_img_id)
    ImageView add_image;

    private String TAG = this.getClass().getName();
    private int PICK_IMAGE = 100;
    MultipartBody.Part body = null;
    NetworkAvailable networkAvailable;
    private UserModel userModel;
    private SpotsDialog spotsDialog;

    SharedPreferences.Editor user_data_edito;
    String camera_permission[];
    String storage_permission[];
    private static final int camera_request_code = 200;
    private static final int storage_request_code = 300;
    private static final int image_pick_gallery_code = 500;
    private static final int image_pick_camera_code = 1000;

    Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        // camera Permission
        camera_permission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //Storage Permission
        storage_permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        password_ed.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (password_ed.getText().toString().length() < 6) {
                        Log.i(TAG, "Not Valid change listner ...");
                        password_ed.setError("Password should be greater than 6 characters!");
                    } else {
                        //validation is true, so what to put here?
                        Log.i(TAG, "Valid change listner ...");
                        Drawable myIcon = getResources().getDrawable(R.drawable.ic_done_24dp);
                        myIcon.setBounds(0, 0, myIcon.getIntrinsicWidth(), myIcon.getIntrinsicHeight());
                        password_ed.setError("Good", myIcon);
                    }
                }
            }
        });
    }

    @OnClick(R.id.register_btn_id)
    public void register() {
        if (networkAvailable.isNetworkAvailable()) {
            if (!FUtilsValidation.isEmpty(userName_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(email_ed, getString(R.string.required))
                    && FUtilsValidation.isValidEmail(email_ed, getString(R.string.enter_valid_email))
                    && !FUtilsValidation.isEmpty(phone_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(password_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(confirmPass_ed, getString(R.string.required))
                    && FUtilsValidation.isPasswordEqual(password_ed, confirmPass_ed, getString(R.string.pass_not_equal))) {

                if (password_ed.getText().toString().length() < 6) {
                    password_ed.setError(getResources().getString(R.string.pass_is_weak));
                    password_ed.requestFocus();
                    return;
                }
                String phone = phone_ed.getText().toString();
                if (phone.length() != 11 && (phone.startsWith("011") || phone.startsWith("012") || phone.startsWith("015") || phone.startsWith("010"))) {
                    phone_ed.setError(getResources().getString(R.string.phone_not_correct));
                    phone_ed.requestFocus();
                    return;
                }
                if (body == null) {
                    Toast.makeText(this, getString(R.string.should_select_image_first), Toast.LENGTH_SHORT).show();
                    return;
                }

                RequestBody NamePart = RequestBody.create(MultipartBody.FORM, userName_ed.getText().toString().trim());
                spotsDialog = new SpotsDialog(Register.this, getString(R.string.registering), R.style.Custom);
                spotsDialog.show();

                ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
                Call<RegisterationModel> call = serviceInterface.Register(NamePart, email_ed.getText().toString(), phone_ed.getText().toString(), password_ed.getText().toString(), confirmPass_ed.getText().toString(), body);
                call.enqueue(new Callback<RegisterationModel>() {
                    @Override
                    public void onResponse(Call<RegisterationModel> call, Response<RegisterationModel> response) {
                        userModel = new UserModel();
                        RegisterationModel registerationModel = response.body();
                        spotsDialog.dismiss();
                        if (registerationModel.getStatus()) {
                            RegisterationModel.User userData = registerationModel.getUser();
                            userModel.setId(userData.getId());
                            userModel.setName(userData.getUsername());
                            userModel.setEmail(userData.getEmail());
                            userModel.setPhone(userData.getMobile());
                            userModel.setImage(userData.getUser_image());
                            userModel.setToken(registerationModel.getToken());

                            // Convert User Data to Gon OBJECT ...
                            Gson gson = new Gson();
                            String user_data = gson.toJson(userModel);
                            user_data_edito = getSharedPreferences(LogIn.MY_PREFS_NAME, MODE_PRIVATE).edit();
                            user_data_edito.putString("user_data", user_data);
                            user_data_edito.commit();
                            user_data_edito.apply();

                            Intent intent = new Intent(Register.this, SetLocationScreen.class);
                            intent.putExtra("user_data", userModel);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Register.this, registerationModel.getMessages().getUsername() + "\n" +
                                    registerationModel.getMessages().getEmail() + "\n" + registerationModel.getMessages().getMobile(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterationModel> call, Throwable t) {
                        t.printStackTrace();
                        spotsDialog.dismiss();
                    }
                });
            } else {
                Toast.makeText(this, getResources().getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick(R.id.signIn_txtV_id)
    public void go_login() {
        Intent intent = new Intent(Register.this, LogIn.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
    }

    @OnClick(R.id.add_img_id)
    public void selectUserImage() {
        if (!checkStoragePermission()) {
            // Camera permission isnot allowed
            requestStoragePermission();
        } else {
            // Camera Permission is allowed
            pickGallery();
        }
    }

    private void showImageImportDialog() {
        // Items to display in Dialog
        String[] items = {getResources().getString(R.string.camera), getResources().getString(R.string.gallery)};

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getResources().getString(R.string.select_image));
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {
                    // Camera Item is Slected
                    if (!checkCameraPermission()) {
                        // Camera permission isnot allowed
                        requestCameraPermission();
                    } else {
                        // Camera Permission is allowed
//                        pickCamera();
                    }

                } else if (which == 1) {
                    // Gallery Item is Selected
                    if (!checkStoragePermission()) {
                        // Camera permission isnot allowed
                        requestStoragePermission();
                    } else {
                        // Camera Permission is allowed
                        pickGallery();
                    }
                }
            }
        });
        dialog.create().show(); // show Dialog...
    }


    private void pickGallery() {
        Intent gallery_intent = new Intent(Intent.ACTION_PICK);
        gallery_intent.setType("image/*");
        startActivityForResult(gallery_intent, image_pick_gallery_code);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storage_permission, storage_request_code);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }


    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, camera_permission, camera_request_code);
    }

    private boolean checkCameraPermission() {

        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    // Handle permission result...
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case image_pick_camera_code:
                if (grantResults.length > 0) {
                    boolean write_storage_accepted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
                    boolean camera_accepted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);

                    if (camera_accepted && write_storage_accepted) {
//                        pickCamera();
                    } else {
                        Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case storage_request_code:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == image_pick_gallery_code) {
                // get image from camera now and crop it
//                CropImage.activity(data.getData()).setGuidelines(CropImageView.Guidelines.ON).start(this);
                try {
                    android.net.Uri selectedImage = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    user_image.setImageBitmap(bitmap);
                    InputStream is = getContentResolver().openInputStream(data.getData());
                    createMultiPartFile(getBytes(is));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

//        // get Crooped Image
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                Uri result_uri = result.getUri();
//                // set image Uri
//                imageView.setImageURI(result_uri);
//
//                // get Drawable Bitmap for text recognition...
//                BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
//                Bitmap bitmap = bitmapDrawable.getBitmap();
//
//                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();
//                if (!recognizer.isOperational()) {
//                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
//                } else {
//
//                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
//                    SparseArray<TextBlock> items = recognizer.detect(frame);
//                    StringBuilder sb = new StringBuilder();
//
//                    // get Text from sb until there is no text
//                    for (int i = 0; i < items.size(); i++) {
//                        TextBlock myItem = items.valueAt(i);
//                        sb.append(myItem.getValue());
//                        sb.append("\n");
//                    }
//                    // set text to edit text..
//                    result_ed.setText(sb.toString());
//                }
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                // if there is any error
//                Exception error = result.getError();
//                Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
//            }
//        }
    }

    private byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();
        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }
        return byteBuff.toByteArray();
    }

    private void createMultiPartFile(byte[] imageBytes) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
        body = MultipartBody.Part.createFormData("user_image", "image.jpg", requestFile);
    }
}
