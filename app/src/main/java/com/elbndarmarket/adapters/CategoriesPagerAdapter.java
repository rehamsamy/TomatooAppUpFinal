package com.elbndarmarket.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.elbndarmarket.fragments.AllCategoriesFragment;
import com.elbndarmarket.model.FeaturedProductsModel;

import java.util.ArrayList;


public class CategoriesPagerAdapter extends FragmentPagerAdapter {

    private int numOfTabs;
    private ArrayList<FeaturedProductsModel> allCategoriesList;
    private ArrayList<FeaturedProductsModel> fruitCategoriesList;
    private ArrayList<FeaturedProductsModel> bakeryCategoriesList;
    private ArrayList<FeaturedProductsModel> forthCategoriesList;

    public CategoriesPagerAdapter(FragmentManager fm, int numOfTabs, ArrayList<FeaturedProductsModel> all_list, ArrayList<FeaturedProductsModel> fruit_list, ArrayList<FeaturedProductsModel> bakery_list, ArrayList<FeaturedProductsModel> fourth_list) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.allCategoriesList = all_list;
        this.fruitCategoriesList = fruit_list;
        this.bakeryCategoriesList = bakery_list;
        this.forthCategoriesList = fourth_list;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
//                fragment = new AllCategoriesFragment();
//                Bundle bundle = new Bundle();
//                bundle.putParcelableArrayList("all_list", allCategoriesList);
//                fragment.setArguments(bundle);
                return fragment;
            case 1:
//                fragment = new FruitsCategoriesFragment();
//                Bundle bundle2 = new Bundle();
//                bundle2.putParcelableArrayList("fruit_list", fruitCategoriesList);
//                fragment.setArguments(bundle2);
                return fragment;
            case 2:
//                fragment = new CategoryItemsFragment();
//                Bundle bundle3 = new Bundle();
//                bundle3.putParcelableArrayList("bakery_list", bakeryCategoriesList);
//                fragment.setArguments(bundle3);
                return fragment;
            case 3:
//                fragment = new SubCategoriesFragment();
//                Bundle bundle4 = new Bundle();
//                bundle4.putParcelableArrayList("fourth_list", forthCategoriesList);
//                fragment.setArguments(bundle4);
                return fragment;
            case 4:
//                fragment = new FivethFragment();
                return fragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
