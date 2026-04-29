package com.techtitans.usman.usmanmart.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techtitans.usman.usmanmart.databinding.FragmentAboutBinding;
import com.techtitans.usman.usmanmart.databinding.FragmentOrderBinding;
import com.techtitans.usman.usmanmart.tabLayoutAdapters.OrderPageAdapter;

public class OrderFragment extends Fragment {

    public OrderFragment() {
        // Required empty public constructor
    }
    FragmentOrderBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentOrderBinding.inflate(inflater, container, false);

        OrderPageAdapter orderPageAdapter=new OrderPageAdapter(getChildFragmentManager());
        binding.viewPager.setAdapter(orderPageAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        return binding.getRoot();
    }
}