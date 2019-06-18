package com.elbndarmarket.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.elbndarmarket.R;
import com.elbndarmarket.activities.MainActivity;
import com.elbndarmarket.model.AddToCartResponseModel;
import com.elbndarmarket.model.FeaturedProductsModel;
import com.elbndarmarket.networking.ApiClient;
import com.elbndarmarket.networking.ApiServiceInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DealingWithCartItem {

    private Context mcontext;
    public static int Cart_id;

    public DealingWithCartItem(Context mcontext) {
        this.mcontext = mcontext;
    }


    public void addTocart(final Activity activity, final FeaturedProductsModel.ProductData item_model, final ProgressBar progressBar, final ImageView cart_imageV, int prod_id, int user_id, int qty, String token) {
        progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<AddToCartResponseModel> call = serviceInterface.addItemToCart(prod_id, user_id, qty, token);
        call.enqueue(new Callback<AddToCartResponseModel>() {
            @Override
            public void onResponse(Call<AddToCartResponseModel> call, Response<AddToCartResponseModel> response) {
                progressBar.setVisibility(View.GONE);
                AddToCartResponseModel addToCartResponseModel = response.body();
                if (addToCartResponseModel.getStatus()) {
                    Cart_id = addToCartResponseModel.getCart_id();
                    Log.i("cart_id", Cart_id + "");
                    item_model.setCart(1);
                    MainActivity.cart_count++;
                    activity.invalidateOptionsMenu();
                    cart_imageV.setImageResource(R.drawable.cart_select);
                    Toast.makeText(mcontext, addToCartResponseModel.getMessages(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mcontext, addToCartResponseModel.getMessages(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddToCartResponseModel> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void removeFromCart(final Activity activity, final FeaturedProductsModel.ProductData item_model, final ProgressBar progressBar, final ImageView cart_imageV, int prod_id, int cart_id, String token) {
        progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<AddToCartResponseModel> call = serviceInterface.delateCartItem(prod_id, cart_id, token);
        call.enqueue(new Callback<AddToCartResponseModel>() {
            @Override
            public void onResponse(Call<AddToCartResponseModel> call, Response<AddToCartResponseModel> response) {
                progressBar.setVisibility(View.GONE);
                AddToCartResponseModel addToCartResponseModel = response.body();
                if (addToCartResponseModel.getStatus()) {
                    Cart_id = addToCartResponseModel.getCart_id();
                    item_model.setCart(0);
                    MainActivity.cart_count--;
                    activity.invalidateOptionsMenu();
                    cart_imageV.setImageResource(R.drawable.cart_not_select);
                    Toast.makeText(mcontext, addToCartResponseModel.getMessages(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mcontext, addToCartResponseModel.getMessages(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddToCartResponseModel> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
