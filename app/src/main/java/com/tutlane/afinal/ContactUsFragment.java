package com.tutlane.afinal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class ContactUsFragment extends Fragment {
    public ContactUsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        getActivity().setTitle("Contact Us");
        return inflater.inflate(R.layout.fragment_contactus,container,false);
    }
}
