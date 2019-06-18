package com.elbndarmarket;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by Ma7MouD on 4/30/2018.
 */

public class FCMRegistrationService extends IntentService {

    SharedPreferences preferences;
    String ADD_TOKEN_URL = "";

    public FCMRegistrationService() {
        super("DisplayNotification");
    }

    public FCMRegistrationService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

//        Toast.makeText(this, "yessss", Toast.LENGTH_SHORT).show();
        // get Default Shard Preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // get token from Firebase
        String token = FirebaseInstanceId.getInstance().getToken();
        sendTokenToServer(token);
    }

    // method use volley to send token to server and stop the service when done or error happened
    private void sendTokenToServer(final String token) {

//        StringRequest request = new StringRequest(Request.Method.POST, ADD_TOKEN_URL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    boolean success = jsonObject.getBoolean("success");
//                    if (success) {
//                        preferences.edit().putBoolean("token_sent", true).apply();
//                        Log.e("Registration Service", "Response : Send Token Success");
//                        stopSelf();
//
//                    } else {
//                        preferences.edit().putBoolean("token_sent", false).apply();
//                        Log.e("Registration Service", "Response : Send Token Failed");
//                        stopSelf();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                preferences.edit().putBoolean("token_sent", false).apply();
//                Log.e("Registration Service", "Error :Send Token Failed");
//                stopSelf();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("token", token);
//                Log.d("token", token);
////                params.put("user_id", MainActivity.customer_id);
////                Log.d("user_id", MainActivity.customer_id);
//                return params;
//            }
//        };
//
//        Volley.newRequestQueue(this).add(request);

    }
}
