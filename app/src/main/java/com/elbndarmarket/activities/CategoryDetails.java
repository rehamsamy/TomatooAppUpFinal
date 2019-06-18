package com.elbndarmarket.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.elbndarmarket.R;
import com.elbndarmarket.adapters.FeaturedProductsAdapter;
import com.elbndarmarket.interfaces.RecyclerOnItemClickListner;
import com.elbndarmarket.model.Category;
import com.elbndarmarket.model.CategoryDataModel;
import com.elbndarmarket.model.DelOrAddWishlistModel;
import com.elbndarmarket.model.FeaturedProductsModel;
import com.elbndarmarket.networking.ApiClient;
import com.elbndarmarket.networking.ApiServiceInterface;
import com.elbndarmarket.utils.DealingWithCartItem;
import com.elbndarmarket.utils.DealingWithWishListItem;
import com.elbndarmarket.utils.EndlessRecyclerViewScrollListener;
import com.elbndarmarket.utils.PreferencesHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryDetails extends AppCompatActivity {

    @BindView(R.id.categoryDetails_back_txtV_id)
    ImageView back_imageV;
    @BindView(R.id.category_details_recyclerV)
    RecyclerView recyclerView;
    @BindView(R.id.categoryDetails_no_products_txtV)
    TextView no_products_txtV;
    @BindView(R.id.progressBar_id)
    ProgressBar progressBar;
    @BindView(R.id.details_subCat_tabLayout_id)
    TabLayout tabLayout;
    @BindView(R.id.catDetails_title_txtV_id)
    TextView catDetails_title_txtV;

    FeaturedProductsAdapter products_adapter;
    int category_id;
    int subCat_position;
    String cat_name;
    private boolean cat_prod;
    private boolean subCat_prod;
    private int current_page = 1;
    private ArrayList<Category> sub_CategorylList;
    private ArrayList<FeaturedProductsModel.ProductData> productslList;

    DealingWithWishListItem dealingWithWishListItem;
    DealingWithCartItem dealingWithCartItem;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);
        ButterKnife.bind(this);

        productslList = new ArrayList<>();
        dealingWithWishListItem = new DealingWithWishListItem(this);
        dealingWithCartItem = new DealingWithCartItem(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        products_adapter = new FeaturedProductsAdapter(this, productslList);
        recyclerView.setAdapter(products_adapter);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (cat_prod) {
                    current_page++;
                    getCategoryProducts(current_page, category_id);
                } else {
                    current_page++;
                    getSubCategoryProducts(current_page, sub_CategorylList.get(subCat_position).getCategory_id());
                }
            }
        });

        products_adapter.setOnItemClickListener(new RecyclerOnItemClickListner() {
            @Override
            public void OnItemClick(int position) {
                Intent intent = new Intent(CategoryDetails.this, ProductDetails.class);
                intent.putExtra("prod_id", productslList.get(position).getProduct_id());
                intent.putExtra("prod_name", productslList.get(position).getProduct_name_en());
                startActivity(intent);
            }

            @Override
            public void OnCartClick(int position, ImageView cart_image) {
                if (productslList.get(position).getCart() == 0) {
                    dealingWithCartItem.addTocart(CategoryDetails.this, productslList.get(position), progressBar, cart_image, productslList.get(position).getProduct_id(), MainActivity.userData.getId(), 1, MainActivity.userData.getToken());
                } else {
                    dealingWithCartItem.removeFromCart(CategoryDetails.this, productslList.get(position), progressBar, cart_image, productslList.get(position).getProduct_id(), dealingWithCartItem.Cart_id, MainActivity.userData.getToken());
                }
            }

            @Override
            public void OnWishListClick(int position, ImageView wishlist_image) {
                if (productslList.get(position).getWishlists() == 0)
                    dealingWithWishListItem.addWishListItem(productslList.get(position), progressBar, wishlist_image);
                else
                    removeWishListItem(productslList.get(position).getProduct_id(), position);
            }
        });

        if (getIntent().hasExtra("category_id")) {
            category_id = getIntent().getExtras().getInt("category_id");
            cat_name = getIntent().getExtras().getString("cat_name");
            getSubCategory(category_id);
            catDetails_title_txtV.setText(cat_name);
        }

//        if (productslList.isEmpty()) {
//            no_products_txtV.setVisibility(View.VISIBLE);
//            recyclerView.setVisibility(View.GONE);
//        } else {
//            no_products_txtV.setVisibility(View.GONE);
//            recyclerView.setVisibility(View.VISIBLE);
//        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                progressBar.setVisibility(View.VISIBLE);
                productslList.clear();
                current_page = 1;
                subCat_position = tab.getPosition();
                Log.i("sub_pos:", sub_CategorylList.get(tab.getPosition()).getCategory_id() + "");
                getSubCategoryProducts(current_page, sub_CategorylList.get(tab.getPosition()).getCategory_id());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void getSubCategory(final int category_id) {
        sub_CategorylList = new ArrayList<>();
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<CategoryDataModel> call = serviceInterface.getSubCategory(1, 20, category_id);
        call.enqueue(new Callback<CategoryDataModel>() {
            @Override
            public void onResponse(Call<CategoryDataModel> call, Response<CategoryDataModel> response) {
                if (response.body().getStatus()) {
                    cat_prod = false;
                    subCat_prod = true;
                    sub_CategorylList.addAll(response.body().getCategories());
                    tabLayout.removeAllTabs();
                    tabLayout.setVisibility(View.VISIBLE);
                    for (int i = 0; i < sub_CategorylList.size(); i++) {
                        if (PreferencesHelper.getSomeStringValue(CategoryDetails.this).equals("ar"))
                            tabLayout.addTab(
                                    tabLayout.newTab()
                                            .setText(sub_CategorylList.get(i).getCategory_name_ar()));
                        else
                            tabLayout.addTab(
                                    tabLayout.newTab()
                                            .setText(sub_CategorylList.get(i).getCategory_name_en()));
                    }
                } else {
                    cat_prod = true;
                    subCat_prod = false;
                    getCategoryProducts(current_page, category_id);
                    tabLayout.removeAllTabs();
                    tabLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<CategoryDataModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getCategoryProducts(int currentPage, int category_id) {
        progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<FeaturedProductsModel> call = serviceInterface.getCategoryProducts(currentPage, 20, category_id);
        call.enqueue(new Callback<FeaturedProductsModel>() {
            @Override
            public void onResponse(Call<FeaturedProductsModel> call, Response<FeaturedProductsModel> response) {
                if (response.body().getStatus()) {
                    productslList.addAll(response.body().getProducts());
                    recyclerView.setVisibility(View.VISIBLE);
                    no_products_txtV.setVisibility(View.GONE);
                    products_adapter.notifyDataSetChanged();
                } else {
                    if (productslList.size() == 0) {
                        no_products_txtV.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        no_products_txtV.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<FeaturedProductsModel> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getSubCategoryProducts(int currentPage, int subCat_id) {
        progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<FeaturedProductsModel> call = serviceInterface.subCategoryProducts(currentPage, 20, subCat_id);
        call.enqueue(new Callback<FeaturedProductsModel>() {
            @Override
            public void onResponse(Call<FeaturedProductsModel> call, Response<FeaturedProductsModel> response) {
                if (response.body().getStatus()) {
                    productslList.addAll(response.body().getProducts());
                    recyclerView.setVisibility(View.VISIBLE);
                    no_products_txtV.setVisibility(View.GONE);
                    products_adapter.notifyDataSetChanged();
                } else {
                    if (productslList.size() == 0) {
                        no_products_txtV.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        no_products_txtV.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<FeaturedProductsModel> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void removeWishListItem(int productId, final int position) {
        progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<DelOrAddWishlistModel> call = serviceInterface.deleteFromWishList(MainActivity.userData.getId(), MainActivity.userData.getToken(), productId);
        call.enqueue(new Callback<DelOrAddWishlistModel>() {
            @Override
            public void onResponse(Call<DelOrAddWishlistModel> call, Response<DelOrAddWishlistModel> response) {
                if (response.body().getStatus()) {
                    productslList.get(position).setWishlists(0);
                    products_adapter.notifyDataSetChanged();
                    Toast.makeText(CategoryDetails.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CategoryDetails.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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


    @OnClick(R.id.categoryDetails_back_txtV_id)
    public void goBack() {
        finish();
    }
}
