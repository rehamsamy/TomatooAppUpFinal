package com.elbndarmarket.activities;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elbndarmarket.R;
import com.elbndarmarket.model.UserLoginModel;
import com.elbndarmarket.networking.ApiClient;
import com.elbndarmarket.networking.ApiServiceInterface;
import com.elbndarmarket.networking.NetworkAvailable;
import com.fourhcode.forhutils.FUtilsValidation;

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

public class EditProfile extends AppCompatActivity {

    @BindView(R.id.edit_user_img_id)
    ImageView user_img;
    @BindView(R.id.edit_img_id)
    ImageView add_NewImage;
    @BindView(R.id.edit_userName_ed_id)
    EditText userName_ed;
    @BindView(R.id.edit_email_ed_id)
    EditText email_ed;
    @BindView(R.id.edit_phone_ed_id)
    EditText phone_ed;
    @BindView(R.id.save_edit_btn_id)
    Button save_btn;
    @BindView(R.id.editProfile_back_txtV_id)
    TextView back;

    private final String TAG = this.getClass().getSimpleName();
    private NetworkAvailable networkAvailable;

    String camera_permission[];
    String storage_permission[];
    private static final int camera_request_code = 10;
    private static final int storage_request_code = 20;
    private static final int image_pick_gallery_code = 20;
    private static final int image_pick_camera_code = 40;

    Uri image_uri;
    MultipartBody.Part body = null;
    SpotsDialog spotsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        networkAvailable = new NetworkAvailable(this);

        userName_ed.setText(MainActivity.userData.getName());
        email_ed.setText(MainActivity.userData.getEmail());
        phone_ed.setText(MainActivity.userData.getPhone());

        userName_ed.setInputType(0);
        userName_ed.setEnabled(false);
    }

    @OnClick(R.id.editProfile_back_txtV_id)
    public void go_back() {
        finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
    }

    @OnClick(R.id.save_edit_btn_id)
    public void saveData() {
        if (networkAvailable.isNetworkAvailable()) {
            if (!FUtilsValidation.isEmpty(userName_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(email_ed, getString(R.string.required))
                    && FUtilsValidation.isValidEmail(email_ed, getString(R.string.enter_valid_email))
                    && !FUtilsValidation.isEmpty(phone_ed, getString(R.string.required))) {

                String phone = phone_ed.getText().toString();
//                if (phone.length() != 11 && (phone.startsWith("011") || phone.startsWith("012") || phone.startsWith("015") || phone.startsWith("010"))) {
//                    phone_ed.setError(getResources().getString(R.string.phone_not_correct));
//                    phone_ed.requestFocus();
//                    return;
//                }
//             if (body == null) {
//                    Toast.makeText(this, getString(R.string.should_select_image_first), Toast.LENGTH_SHORT).show();
//                       return;
//                }
                spotsDialog = new SpotsDialog(EditProfile.this, getString(R.string.registering), R.style.Custom);
                spotsDialog.show();

                Log.i("name: ", userName_ed.getText().toString());
                Log.i("email: ", email_ed.getText().toString());
                Log.i("phone: ", phone_ed.getText().toString());
                Log.i("id: ", MainActivity.userData.getId() + "");
                Log.i("token: ", MainActivity.userData.getToken());
                RequestBody NamePart = RequestBody.create(MultipartBody.FORM, userName_ed.getText().toString().trim());
                ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
                Call<UserLoginModel> call = serviceInterface.updateProfile(NamePart, email_ed.getText().toString(), phone_ed.getText().toString(), MainActivity.userData.getToken(), MainActivity.userData.getId(), body);
                call.enqueue(new Callback<UserLoginModel>() {
                    @Override
                    public void onResponse(Call<UserLoginModel> call, Response<UserLoginModel> response) {
                        UserLoginModel userLoginModel = response.body();
                        if (userLoginModel.getStatus()) {
                            UserLoginModel.User user = userLoginModel.getUser();
                            MainActivity.userData.setName(user.getUsername());
                            MainActivity.userData.setEmail(user.getEmail());
                            MainActivity.userData.setPhone(user.getMobile());
//                            userModel.setImage(user.get());

                            spotsDialog.dismiss();
                            Toast.makeText(EditProfile.this, userLoginModel.getMessages(), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            spotsDialog.dismiss();
                            Toast.makeText(EditProfile.this, userLoginModel.getMessages(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserLoginModel> call, Throwable t) {
                        spotsDialog.dismiss();
                        t.printStackTrace();
                    }
                });
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.edit_img_id)
    public void addImage() {
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
                        pickCamera();
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

    private void pickCamera() {
        // intent to take image from Camera, it also will be save to storage to get high quality image
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(camera_intent, image_pick_camera_code);
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
            case camera_request_code:
                if (grantResults.length > 0) {
                    boolean write_storage_accepted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
                    boolean camera_accepted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);

                    if (camera_accepted && write_storage_accepted) {
                        pickCamera();
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
                    user_img.setImageBitmap(bitmap);
                    InputStream is = getContentResolver().openInputStream(data.getData());
                    createMultiPartFile(getBytes(is));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (requestCode == image_pick_camera_code) {
                // get image from camera now and crop it
//                CropImage.activity(image_uri).setGuidelines(CropImageView.Guidelines.ON).start(this);
                try {
                    android.net.Uri selectedImage = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    user_img.setImageBitmap(bitmap);
                    InputStream is = getContentResolver().openInputStream(data.getData());
                    createMultiPartFile(getBytes(is));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
