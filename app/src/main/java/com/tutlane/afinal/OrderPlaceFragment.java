package com.tutlane.afinal;

import android.os.Bundle;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class OrderPlaceFragment extends Fragment
{
    public OrderPlaceFragment()
    {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("MediMart-Order Placed");
        return  inflater.inflate(R.layout.fragment_order_placed,container,false);
    }
}
