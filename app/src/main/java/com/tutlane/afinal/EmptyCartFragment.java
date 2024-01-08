package com.tutlane.afinal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class EmptyCartFragment extends Fragment {
    public EmptyCartFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        getActivity().setTitle("Cart is Empty");
        return inflater.inflate(R.layout.fragment_empty_cart,container,false);
    }
}
