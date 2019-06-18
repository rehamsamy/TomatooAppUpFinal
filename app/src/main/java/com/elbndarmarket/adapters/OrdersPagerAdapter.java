package com.elbndarmarket.adapters;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.elbndarmarket.fragments.OrderCompletedFragment;
import com.elbndarmarket.fragments.OrderProcessFragment;
import com.elbndarmarket.fragments.OrderWaitingFragment;
import com.elbndarmarket.model.UserOrderModel;

import java.util.ArrayList;
import java.util.List;

public class OrdersPagerAdapter extends FragmentPagerAdapter {

    private int numOfTabs;
    private List<UserOrderModel.OrderData> processing_list;
    private List<UserOrderModel.OrderData> waiting_list;
    private List<UserOrderModel.OrderData> completed_list;

    public OrdersPagerAdapter(FragmentManager fm, int numOfTabs, List<UserOrderModel.OrderData> process_list, List<UserOrderModel.OrderData> wait_list, List<UserOrderModel.OrderData> completed_list) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.processing_list = process_list;
        this.waiting_list = wait_list;
        this.completed_list = completed_list;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new OrderProcessFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("process_list", (ArrayList<? extends Parcelable>) processing_list);
                fragment.setArguments(bundle);
                return fragment;
            case 1:
                fragment = new OrderWaitingFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putParcelableArrayList("wait_list", (ArrayList<? extends Parcelable>) waiting_list);
                fragment.setArguments(bundle2);
                return fragment;
            case 2:
                fragment = new OrderCompletedFragment();
                Bundle bundle3 = new Bundle();
                bundle3.putParcelableArrayList("complete_list", (ArrayList<? extends Parcelable>) completed_list);
                fragment.setArguments(bundle3);
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
