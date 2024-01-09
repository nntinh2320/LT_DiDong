package com.tutlane.afinal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class VendorProfile extends Fragment  {

    public VendorProfile()
    {}
    private TextView tv1;
    private Button b,b1,b2,b3;
    private SharedPreferences sp;
    private SharedPreferences.Editor edit;
    private MediMartDbHelper db;


    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View vv=inflater.inflate(R.layout.fragment_vendor_profile,container,false);
        tv1=vv.findViewById(R.id.tvprousername );
        b=vv.findViewById(R.id.blogout);
        b1=vv.findViewById(R.id.bchangepwd);
        b2=vv.findViewById(R.id.baddpro);
        b3=vv.findViewById(R.id.ballprod);
        sp=getActivity().getSharedPreferences("users", Context.MODE_PRIVATE);
        edit=sp.edit();
        getActivity().setTitle("MediMart-Admin Home");
        db=new MediMartDbHelper(getContext());
        String userid=sp.getString("userid","guest");
        tv1.setText("Welcome"+userid);
        b1.setOnClickListener((v)->{
            MediMartUtils.loadFragment(getActivity(),new ChangePwdFragment());
        });
        b3.setOnClickListener((v)->{
            MediMartUtils.loadFragment(getActivity(),new AllProductsFragment());
        });
        b2.setOnClickListener((v)->{
            MediMartUtils.loadFragment(getActivity(),new AddProductFragment());
        });
        b.setOnClickListener((v)-> {
            edit.remove("userid");
            edit.remove("role");
            edit.commit();
            MediMartUtils.loadFragment(getActivity(),new HomeFragment());
            Toast.makeText(getContext(),"Sign out successfully",Toast.LENGTH_SHORT).show();
        });
        return vv;
    }
}
