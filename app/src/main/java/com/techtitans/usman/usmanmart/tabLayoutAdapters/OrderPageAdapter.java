package com.techtitans.usman.usmanmart.tabLayoutAdapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.techtitans.usman.usmanmart.fragments.CustomerOrderFragment;
import com.techtitans.usman.usmanmart.fragments.ManualOrdersFragment;

public class OrderPageAdapter extends FragmentPagerAdapter{
        public OrderPageAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        public OrderPageAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return new CustomerOrderFragment();
                case 1:
                    return new ManualOrdersFragment();
            }
            return new CustomerOrderFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Customer Orders";
                case 1:
                    return "Manual Orders";
            }

            return "Customer Orders";
        }
    }
