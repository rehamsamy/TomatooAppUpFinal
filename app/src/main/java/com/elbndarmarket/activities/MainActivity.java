package com.elbndarmarket.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.elbndarmarket.R;
import com.elbndarmarket.adapters.NavMenuAdapter;
import com.elbndarmarket.fragments.MainFragment;
import com.elbndarmarket.interfaces.NavRecyclerClickListner;
import com.elbndarmarket.model.NavItemModel;
import com.elbndarmarket.model.UserLoginModel;
import com.elbndarmarket.model.UserModel;
import com.elbndarmarket.networking.ApiClient;
import com.elbndarmarket.networking.ApiServiceInterface;
import com.elbndarmarket.networking.NetworkAvailable;
import com.elbndarmarket.utils.PreferencesHelper;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.elbndarmarket.activities.LogIn.MY_PREFS_NAME;

public class MainActivity extends AppCompatActivity {

    FragmentTransaction ft;
    ArrayList<NavItemModel> menuList;
    DrawerLayout drawerLayout;
    NavMenuAdapter nav_adapter;
    NetworkAvailable networkAvailable;
    RecyclerView recyclerView;
    TextView header_title, header_back_txtV, header_back_txtV2;
    ImageView nav_header_img;

    public static int cart_count = 0;


    public static UserModel userData;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        networkAvailable = new NetworkAvailable(this);
        header_title = findViewById(R.id.nav_profile_txtV);
        nav_header_img = findViewById(R.id.nav_profile_image);
        header_back_txtV = findViewById(R.id.header_back_txtV);
        header_back_txtV2 = findViewById(R.id.header_back_txtV2);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (PreferencesHelper.getSomeStringValue(MainActivity.this).equals("ar")) {
            Log.i("lang: ", PreferencesHelper.getSomeStringValue(MainActivity.this));
            header_back_txtV2.setVisibility(View.GONE);
            header_back_txtV.setVisibility(View.VISIBLE);
        } else {
            header_back_txtV2.setVisibility(View.VISIBLE);
            header_back_txtV.setVisibility(View.GONE);
        }

        if (getIntent().hasExtra("user_data")) {
            userData = getIntent().getExtras().getParcelable("user_data");
        }
        if (networkAvailable.isNetworkAvailable()) {
            ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
            Call<UserLoginModel> call = serviceInterface.getUserInfo(userData.getId(), userData.getToken());
            call.enqueue(new Callback<UserLoginModel>() {
                @Override
                public void onResponse(Call<UserLoginModel> call, Response<UserLoginModel> response) {
                    UserLoginModel userLoginModel = response.body();
                    if (userLoginModel.getStatus()) {
                        UserLoginModel.User user = userLoginModel.getUser();
                        userData.setId(user.getId());
                        userData.setName(user.getUsername());
                        userData.setEmail(user.getEmail());
                        userData.setPhone(user.getMobile());
                        userData.setImage(user.getUser_image());
                        userData.setToken(userLoginModel.getToken());
                        userData.setBalanceAmount(user.getBalanceAmount());
                        userData.setAddress(user.getAddress());
                        userData.setCountry(user.getCountry());
                        userData.setCity(user.getCity());
                        userData.setBuilding_no(user.getBuilding_no());
                        userData.setFloor_no(user.getFloor_no());
                        userData.setApartment_no(user.getApartment_no());
                        userData.setVilla_no(user.getVilla_no());
                        userData.setOther(user.getOther());

                        header_title.setText(user.getUsername());
                        Glide.with(MainActivity.this)
                                .load(userData.getImage())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                                .centerCrop()
                                .crossFade()
                                .into(nav_header_img);

                        // Convert User Data to Gon OBJECT ...
                        Gson gson = new Gson();
                        String user_data = gson.toJson(userData);
                        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("user_data", user_data);
                        editor.commit();
                        editor.apply();
                    }
                }

                @Override
                public void onFailure(Call<UserLoginModel> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        }

        header_back_txtV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        header_back_txtV2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        createNavListItems();
        buildMenuRecycler();

        //Select HomeFragment by default
        MainFragment mainFragment = new MainFragment();
//        ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.main_frame_id, mainFragment);
//        ft.addToBackStack(null);
//        ft.commit();
        displaySelectedFragment(mainFragment);
    }

    private void createNavListItems() {
        menuList = new ArrayList<>();
        menuList.add(new NavItemModel(getResources().getString(R.string.home), R.drawable.ic_home_black_24dp));
        menuList.add(new NavItemModel(getResources().getString(R.string.profile), R.drawable.ic_person_black_24dp));
        menuList.add(new NavItemModel(getResources().getString(R.string.myCart), R.drawable.sidemenu_cart));
        menuList.add(new NavItemModel(getResources().getString(R.string.my_wishlist), R.drawable.ic_favorite_black_24dp));
        menuList.add(new NavItemModel(getResources().getString(R.string.my_orders), R.drawable.sidemenu_order));
        menuList.add(new NavItemModel(getResources().getString(R.string.notifications), R.drawable.ic_notifications_black_24dp));
        menuList.add(new NavItemModel(getResources().getString(R.string.setings), R.drawable.ic_settings_black_24dp));
        menuList.add(new NavItemModel(getResources().getString(R.string.logout), R.drawable.ic_ogout));
    }

    private View getHeader() {
        View view = getLayoutInflater().inflate(R.layout.nav_header_main, null);
        ImageView navHeader_imageView = view.findViewById(R.id.nav_profile_image);
        TextView navHeader_txtV = view.findViewById(R.id.nav_profile_txtV);

        Glide.with(this)
                .load(userData.getImage())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        // image ready, hide progress now
                        return false;   // return false if you want Glide to handle everything else.
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                .centerCrop()
                .crossFade()
                .into(navHeader_imageView);
        navHeader_txtV.setText(userData.getName());
        return view;
    }

    private void buildMenuRecycler() {
        recyclerView = findViewById(R.id.nav_menu_recyclerV_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        nav_adapter = new NavMenuAdapter(MainActivity.this, menuList);
        recyclerView.setAdapter(nav_adapter);

        nav_adapter.setOnItemClickListener(new NavRecyclerClickListner() {
            @Override
            public void OnItemClick(int position) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                Intent intent = null;
                drawer.closeDrawer(GravityCompat.START);
                switch (position) {
                    case 0:
                        recreate();
                        break;
                    case 1:
                        intent = new Intent(MainActivity.this, Profile.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(MainActivity.this, ShoppingCart.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(MainActivity.this, MyWishlist.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(MainActivity.this, MyOrders.class);
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(MainActivity.this, MyNotificationsActivity.class);
                        startActivity(intent);
                        break;
                    case 6:
                        intent = new Intent(MainActivity.this, Settings.class);
                        startActivity(intent);
                        break;
                    case 7:
                        logoutOfApp();
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
//            logoutOfApp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_action);
        menuItem.setIcon(Converter.convertLayoutToImage(MainActivity.this, cart_count, R.drawable.cart));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.cart_action) {
            Intent intent = new Intent(MainActivity.this, ShoppingCart.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void logoutOfApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(getString(R.string.outofApp))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //super.onBackPressed();
                        dialogInterface.dismiss();
                        //-------------------------------------------------------
                        pref = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                        editor = pref.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(MainActivity.this, LogIn.class));
                        finish();
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

    /**
     * Loads the specified fragment to the frame
     *
     * @param fragment
     */
    private void displaySelectedFragment(Fragment fragment) {
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_frame_id, fragment);
        ft.commit();
    }

}
