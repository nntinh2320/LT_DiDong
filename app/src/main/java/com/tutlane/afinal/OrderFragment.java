package com.tutlane.afinal;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class OrderFragment extends Fragment {
    public OrderFragment()
    {

    }
    private RecyclerView lv;
    private SharedPreferences sp;
    private MediMartDbHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vv=inflater.inflate(R.layout.fragment_orders,container,false);
        lv=vv.findViewById(R.id.rv_history);
        db=new MediMartDbHelper(getContext());
        getActivity().setTitle("MediMart-Order History");
        final Cursor cc= db.getALLOrders();
    }
}
