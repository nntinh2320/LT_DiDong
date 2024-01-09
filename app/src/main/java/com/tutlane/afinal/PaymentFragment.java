package com.tutlane.afinal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PaymentFragment extends Fragment    {
    public PaymentFragment(){}
    private Button bpay;
    private SharedPreferences sp;
    private MediMartDbHelper db;

    @Override
    public View onCreateView(final LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
       View vv=inflater.inflate(R.layout.fragment_payment,container,false);
       sp=getActivity().getSharedPreferences("users", Context.MODE_PRIVATE);
       db= new MediMartDbHelper(getContext());
       bpay = vv.findViewById(R.id.bpay);
       getActivity().setTitle("MediMart-Payment Gateway");
       bpay.setOnClickListener((v)->{
           String userid=sp.getString("userid","guest");
           db.saveorder(userid);
           showSuccess(inflater);
       });
       return vv;
    }
    private void showSuccess(LayoutInflater inflater){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        View vv = inflater.inflate(R.layout.fragment_order_placed,null,false);
        builder.setView(vv);
        builder.setCancelable(false);
        builder.setPositiveButton("OK",(diaLogInterface,i)->{
           MediMartUtils.loadwithoutHistoryFragment(getActivity(),new CategoryFragment());
        });
        builder.show();
    }

}
