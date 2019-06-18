package com.elbndarmarket.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.elbndarmarket.R;
import com.elbndarmarket.model.AddToCartResponseModel;
import com.elbndarmarket.model.DelOrAddWishlistModel;
import com.elbndarmarket.model.FeaturedProductsModel;
import com.elbndarmarket.model.ProductDetailsModel;
import com.elbndarmarket.model.WishListModel;
import com.elbndarmarket.networking.ApiClient;
import com.elbndarmarket.networking.ApiServiceInterface;
import com.elbndarmarket.utils.DealingWithCartItem;
import com.elbndarmarket.utils.DealingWithWishListItem;
import com.elbndarmarket.utils.PreferencesHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetails extends AppCompatActivity {

    @BindView(R.id.details_back_txtV_id)
    TextView back_btn;
    @BindView(R.id.item_main_imageV_id)
    ImageView item_Image;
    @BindView(R.id.add_to_wishlist_img_id)
    ImageView wishlist_image;
    @BindView(R.id.item_Cart_imageV_id)
    ImageView cart_image;
    @BindView(R.id.minus_item_count_img_id)
    ImageView minus_item_count;
    @BindView(R.id.plus_item_count_img_id)
    ImageView plus_item_count;
    @BindView(R.id.item_name_txtV_id)
    TextView item_name;
    @BindView(R.id.item_price_txtV_id)
    TextView item_unit_price;
    @BindView(R.id.item_category_txtV_id)
    TextView item_category;
    @BindView(R.id.item_description_txtV_id)
    TextView item_description;
    @BindView(R.id.item_total_price_txtV_id)
    TextView item_total_price;
    @BindView(R.id.item_count_txtV_id)
    TextView itemCount_txtV;
    @BindView(R.id.details_addToCart_btn_id)
    Button addToCart_btn;
    @BindView(R.id.prod_Details_layout_id)
    ConstraintLayout prodDetails_layout;
    @BindView(R.id.details_progress_id)
    ProgressBar progressBar;
    Toolbar toolbar;

    private int prod_id;
    private String prod_name, prod_cat;
    private String TAG = this.getClass().getSimpleName();
    ProductDetailsModel.ItemDetail itemDetail;
    ProductDetailsModel.ItemImages itemImage;
    private DealingWithWishListItem dealingWithWishListItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        ButterKnife.bind(this);
        dealingWithWishListItem = new DealingWithWishListItem(this);

        if (getIntent() != null) {
            prod_id = getIntent().getExtras().getInt("prod_id");
            prod_name = getIntent().getExtras().getString("prod_name");
            prod_cat = getIntent().getExtras().getString("prod_category");
            Log.i(TAG, prod_id + "");
            Log.i(TAG, prod_name + "");
            Log.i(TAG, PreferencesHelper.getSomeStringValue(ProductDetails.this));
        }

        setUpToolBar(prod_name);
        getProductDetails(prod_id, MainActivity.userData.getId());
    }

    private void getProductDetails(int product_id, int user_id) {
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<ProductDetailsModel> call = serviceInterface.productDetails(product_id, user_id);
        call.enqueue(new Callback<ProductDetailsModel>() {
            @Override
            public void onResponse(Call<ProductDetailsModel> call, Response<ProductDetailsModel> response) {
                ProductDetailsModel productModel = response.body();
                if (response.body().getStatus()) {
                    prodDetails_layout.setVisibility(View.VISIBLE);
                    itemDetail = productModel.getProducts().getDetails();
                    itemImage = productModel.getProducts().getImages();
                    // Set Data To Views
                    if (PreferencesHelper.getSomeStringValue(ProductDetails.this).equals("ar")) {
                        item_name.setText(itemDetail.getProduct_name_ar());
                        item_unit_price.setText(String.valueOf(itemDetail.getPrice()));
                        item_unit_price.append("$");
                        item_total_price.setText(String.valueOf(Integer.parseInt(itemCount_txtV.getText().toString()) * itemDetail.getPrice()));
                        item_total_price.append("$");
                        item_category.setText(prod_cat);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            item_description.setText(Html.fromHtml(itemDetail.getProduct_desc_ar(), Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            item_description.setText(Html.fromHtml(itemDetail.getProduct_desc_ar()));
                        }
                    } else {
                        item_name.setText(itemDetail.getProduct_name_en());
                        item_unit_price.setText(String.valueOf(itemDetail.getPrice()));
                        item_unit_price.append("$");
                        item_total_price.setText(String.valueOf(Integer.parseInt(itemCount_txtV.getText().toString()) * itemDetail.getPrice()));
                        item_total_price.append("$");
                        item_category.setText(prod_cat);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            item_description.setText(Html.fromHtml(itemDetail.getProduct_desc_en(), Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            item_description.setText(Html.fromHtml(itemDetail.getProduct_desc_en()));
                        }
                    }
                    // Load Product Image
                    Glide.with(ProductDetails.this)
                            .load(itemImage.getProduct_image())
                            .fitCenter()
                            .into(item_Image);

                    Log.i("wishh: ", itemDetail.getWishlists() + "");
                    if (itemDetail.getWishlists() == 0) {
                        wishlist_image.setImageResource(R.drawable.wishlist_not_select);
                    } else {
                        wishlist_image.setImageResource(R.drawable.wishlist_select);
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ProductDetailsModel> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setUpToolBar(String itemName) {
        toolbar = (Toolbar) findViewById(R.id.itemDetails_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        TextView textView = findViewById(R.id.itemDetails_title_txtV);
        textView.setText(itemName);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_action);
        menuItem.setIcon(Converter.convertLayoutToImage(this, MainActivity.cart_count, R.drawable.cart));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.cart_action) {
            Intent intent = new Intent(ProductDetails.this, ShoppingCart.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.details_addToCart_btn_id})
    public void addToCart() {
        addTocart(ProductDetails.this, itemDetail.getProduct_id(), MainActivity.userData.getId(), Integer.parseInt(itemCount_txtV.getText().toString()), MainActivity.userData.getToken());
    }

    @OnClick(R.id.details_back_txtV_id)
    public void go_back() {
        finish();
    }

    @OnClick(R.id.add_to_wishlist_img_id)
    public void addToWishlist() {
        if (itemDetail.getWishlists() == 0) {
            addWishListItem(itemDetail, wishlist_image);
        } else {
            removeWishListItem(itemDetail, wishlist_image);
        }
    }

    @OnClick(R.id.plus_item_count_img_id)
    public void plusCount() {
        int item_count = Integer.parseInt(itemCount_txtV.getText().toString());
        Log.i(" count", item_count + "");
        item_count++;
        Log.i(" count", item_count + "");
        float total_price = item_count * itemDetail.getPrice();
        itemCount_txtV.setText(String.valueOf(item_count));
        item_total_price.setText(String.valueOf(total_price));
        item_total_price.append("$");
    }

    @OnClick(R.id.minus_item_count_img_id)
    public void minusCount() {
        int item_count = Integer.parseInt(itemCount_txtV.getText().toString());
        if (item_count > 1) {
            Log.i(" count", item_count + "");
            item_count--;
            Log.i(" count", item_count + "");
            float total_price = item_count * itemDetail.getPrice();
            itemCount_txtV.setText(String.valueOf(item_count));
            itemCount_txtV.setText(String.valueOf(item_count));
            item_total_price.setText(String.valueOf(total_price));
            item_total_price.append("$");
        }
    }

    // For Wishlist....
    private void addWishListItem(final ProductDetailsModel.ItemDetail item_model, final ImageView imageView) {
        progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<DelOrAddWishlistModel> call = serviceInterface.addToWishList(MainActivity.userData.getId(), item_model.getProduct_id(), MainActivity.userData.getToken());
        call.enqueue(new Callback<DelOrAddWishlistModel>() {
            @Override
            public void onResponse(Call<DelOrAddWishlistModel> call, Response<DelOrAddWishlistModel> response) {
                if (response.body().getStatus()) {
                    item_model.setWishlists(1);
                    imageView.setImageResource(R.drawable.wishlist_select);
                    Toast.makeText(ProductDetails.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductDetails.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

    private void removeWishListItem(final ProductDetailsModel.ItemDetail itemDetail, final ImageView wishlist_imageV) {
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<DelOrAddWishlistModel> call = serviceInterface.deleteFromWishList(MainActivity.userData.getId(), MainActivity.userData.getToken(), itemDetail.getProduct_id());
        call.enqueue(new Callback<DelOrAddWishlistModel>() {
            @Override
            public void onResponse(Call<DelOrAddWishlistModel> call, Response<DelOrAddWishlistModel> response) {
                if (response.body().getStatus()) {
                    itemDetail.setWishlists(0);
                    wishlist_imageV.setImageResource(R.drawable.wishlist_not_select);
                    Toast.makeText(ProductDetails.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductDetails.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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


    // For Cart ....
    private void addTocart(final Activity activity, int prod_id, int user_id, int qty, String token) {
        progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<AddToCartResponseModel> call = serviceInterface.addItemToCart(prod_id, user_id, qty, token);
        call.enqueue(new Callback<AddToCartResponseModel>() {
            @Override
            public void onResponse(Call<AddToCartResponseModel> call, Response<AddToCartResponseModel> response) {
                progressBar.setVisibility(View.GONE);
                AddToCartResponseModel addToCartResponseModel = response.body();
                if (addToCartResponseModel.getStatus()) {
                    int cart_id = addToCartResponseModel.getCart_id();
                    Log.i("cart_id", cart_id + "");
                    MainActivity.cart_count++;
                    activity.invalidateOptionsMenu();
                    cart_image.setImageResource(R.drawable.cart_select);
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
}
