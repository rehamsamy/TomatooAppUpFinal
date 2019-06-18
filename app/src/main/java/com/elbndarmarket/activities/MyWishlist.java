package com.elbndarmarket.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.elbndarmarket.R;
import com.elbndarmarket.adapters.WishlistAdapter;
import com.elbndarmarket.interfaces.RecyclerOnItemClickListner;
import com.elbndarmarket.model.AddToCartResponseModel;
import com.elbndarmarket.model.DelOrAddWishlistModel;
import com.elbndarmarket.model.FeaturedProductsModel;
import com.elbndarmarket.model.WishListModel;
import com.elbndarmarket.networking.ApiClient;
import com.elbndarmarket.networking.ApiServiceInterface;
import com.elbndarmarket.utils.DealingWithCartItem;
import com.elbndarmarket.utils.DealingWithWishListItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyWishlist extends AppCompatActivity {

    @BindView(R.id.wishlist_back_txtV_id)
    ImageView wishlist_back;
    @BindView(R.id.wishlist_cart_txtV_id)
    TextView wishlist_cart_icon;
    @BindView(R.id.wishlist_recyclerV_id)
    RecyclerView wishlist_recyclerV;
    @BindView(R.id.wishlist_progress_id)
    ProgressBar progressBar;
    @BindView(R.id.wishList_no_data_txtV)
    TextView wishList_no_data_txtV;

    private int current_page = 1;
    private int page_limit = 20;
    ArrayList<WishListModel.WishlistItem> list;
    WishlistAdapter adapter;
    private DealingWithCartItem dealingWithCartItem;
    private DealingWithWishListItem dealingWithWishListItem;
    private final String TAG = this.getClass().getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wishlist);
        ButterKnife.bind(this);
        dealingWithWishListItem = new DealingWithWishListItem(this);
        dealingWithCartItem = new DealingWithCartItem(this);

        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);

        list = new ArrayList<>();
        buildWishlistRecycler(current_page, page_limit);

    }

    private void buildWishlistRecycler(int current_page, int page_limit) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        wishlist_recyclerV.setLayoutManager(gridLayoutManager);
        wishlist_recyclerV.setHasFixedSize(true);
        wishlist_recyclerV.setItemAnimator(new DefaultItemAnimator());

        // Set Data to ArrayList And to Recycler ...
        adapter = new WishlistAdapter(MyWishlist.this, list);
        wishlist_recyclerV.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecyclerOnItemClickListner() {
            @Override
            public void OnItemClick(int position) {
                // Item Clicked ...
                WishListModel.WishlistItem itemModel = list.get(position);
                Intent intent = new Intent(MyWishlist.this, ProductDetails.class);
                intent.putExtra("prod_id", itemModel.getProduct_id());
                // Check For Lang First ...
                intent.putExtra("prod_name", itemModel.getProduct_name_en());
                intent.putExtra("prod_category", itemModel.getCategory_name_en());
                startActivity(intent);
            }

            @Override
            public void OnCartClick(int position, ImageView cart_image) {
                if (list.get(position).getCart() == 0) {
                    addTocart(MyWishlist.this, list.get(position), progressBar, cart_image, MainActivity.userData.getId(), 1, MainActivity.userData.getToken());
                } else {
                    removeFromCart(MyWishlist.this, list.get(position), progressBar, cart_image, 1, MainActivity.userData.getToken());
                }
            }

            @Override
            public void OnWishListClick(final int position, ImageView wishlist_image) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyWishlist.this);
                builder.setMessage(getString(R.string.delete_this_Item))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //super.onBackPressed();
                                dialogInterface.dismiss();
                                //-------------------------------------------------------
                                deleteWishListItem(list, adapter, position, MainActivity.userData.getId(), MainActivity.userData.getToken(), list.get(position).getProduct_id(), progressBar);
                                // -------------------------------------------------------
                            }
                        })
                        .setNegativeButton(getString(R.string.dismiss), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            }
        });
        Log.i(TAG, MainActivity.userData.getId() + "");
        Log.i(TAG, MainActivity.userData.getToken() + "");
        getWishList(MainActivity.userData.getId(), MainActivity.userData.getToken(), current_page, page_limit);
    }

    @OnClick(R.id.wishlist_back_txtV_id)
    public void go_back() {
        finish();
    }

    @OnClick(R.id.wishlist_cart_txtV_id)
    public void cart_go() {
        Intent intent = new Intent(MyWishlist.this, ShoppingCart.class);
        startActivity(intent);
        finish();
    }

    private void getWishList(int id, String token, int current_page, int page_limit) {
        progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<WishListModel> call = serviceInterface.getWishListData(id, token, current_page, page_limit);
        call.enqueue(new Callback<WishListModel>() {
            @Override
            public void onResponse(Call<WishListModel> call, Response<WishListModel> response) {
                WishListModel wishListModel = response.body();
                if (response.body().getStatus()) {
                    wishlist_recyclerV.setVisibility(View.VISIBLE);
                    wishList_no_data_txtV.setVisibility(View.GONE);
                    list.addAll(wishListModel.getProducts());
                    adapter.notifyItemRangeInserted(adapter.getItemCount(), list.size() - 1);
                } else {
                    wishlist_recyclerV.setVisibility(View.GONE);
                    wishList_no_data_txtV.setVisibility(View.VISIBLE);
//                    Toast.makeText(MyWishlist.this, wishListModel.getMessages(), Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<WishListModel> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void removeItem(int position) {
        list.remove(position);
        adapter.notifyItemRemoved(position);
    }

    public void addTocart(final Activity activity, final WishListModel.WishlistItem item_model, final ProgressBar progressBar, final ImageView cart_imageV, int user_id, int qty, String token) {
        progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<AddToCartResponseModel> call = serviceInterface.addItemToCart(item_model.getProduct_id(), user_id, qty, token);
        call.enqueue(new Callback<AddToCartResponseModel>() {
            @Override
            public void onResponse(Call<AddToCartResponseModel> call, Response<AddToCartResponseModel> response) {
                progressBar.setVisibility(View.GONE);
                AddToCartResponseModel addToCartResponseModel = response.body();
                if (addToCartResponseModel.getStatus()) {
                    int cart_id = addToCartResponseModel.getCart_id();
                    Log.i("cart_id", cart_id + "");
                    item_model.setCart(1);
                    MainActivity.cart_count++;
                    activity.invalidateOptionsMenu();
                    cart_imageV.setImageResource(R.drawable.cart_select);
                    Toast.makeText(activity, addToCartResponseModel.getMessages(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, addToCartResponseModel.getMessages(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddToCartResponseModel> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void removeFromCart(final Activity activity, final WishListModel.WishlistItem item_model, final ProgressBar progressBar, final ImageView cart_imageV, int qty, String token) {
        progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<AddToCartResponseModel> call = serviceInterface.delateCartItem(item_model.getProduct_id(), qty, token);
        call.enqueue(new Callback<AddToCartResponseModel>() {
            @Override
            public void onResponse(Call<AddToCartResponseModel> call, Response<AddToCartResponseModel> response) {
                progressBar.setVisibility(View.GONE);
                AddToCartResponseModel addToCartResponseModel = response.body();
                if (addToCartResponseModel.getStatus()) {
                    int cart_id = addToCartResponseModel.getCart_id();
                    Log.i("cart_id", cart_id + "");
                    item_model.setCart(0);
                    MainActivity.cart_count--;
                    activity.invalidateOptionsMenu();
                    cart_imageV.setImageResource(R.drawable.cart_not_select);
                    Toast.makeText(activity, addToCartResponseModel.getMessages(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, addToCartResponseModel.getMessages(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddToCartResponseModel> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void deleteWishListItem(final List<WishListModel.WishlistItem> list, final WishlistAdapter adapter, final int position, final int id, String token, int productId, final ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<DelOrAddWishlistModel> call = serviceInterface.deleteFromWishList(id, token, productId);
        call.enqueue(new Callback<DelOrAddWishlistModel>() {
            @Override
            public void onResponse(Call<DelOrAddWishlistModel> call, Response<DelOrAddWishlistModel> response) {
                if (response.body().getStatus()) {
                    removeItem(position);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(MyWishlist.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyWishlist.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DelOrAddWishlistModel> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}