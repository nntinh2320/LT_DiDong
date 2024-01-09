package com.tutlane.afinal;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;

public class OrderHistoryFragment extends Fragment {
    public OrderHistoryFragment()
    {}
    private ListView lv;
    private SharedPreferences sp;
    private MediMartDbHelper db;

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,
                              Bundle savedInstanceState) {
       View vv =inflater.inflate(R.layout.fragment_order_history,container,false);
       lv=vv.findViewById(R.id.lv_history);
       sp=getActivity().getSharedPreferences("users", Context.MODE_PRIVATE);
       db=new MediMartDbHelper(getContext());
       getActivity().setTitle("MediMart-Order History");
       String userid=sp.getString("userid","guest");
       final Cursor cc=db.getOrders(userid);
       if(cc.getCount()>0)
       {
           CustomAdapter adp =new CustomAdapter(cc,inflater);
           lv.setAdapter(adp);
           lv.setOnItemClickListener(((parent, view, position, id) -> {
               cc.moveToPosition(position);
               OrderDetailFragment odf = new OrderDetailFragment();
               Bundle bundle=new Bundle();
               bundle.putString("orderid",cc.getString(0));
               odf.setArguments(bundle);
               MediMartUtils.loadFragment(getActivity(),odf);
           }));
       }
       return vv;
    }
    private class CustomAdapter extends BaseAdapter
    {
        private Cursor c;
        private LayoutInflater inflater;

        private CustomAdapter(Cursor c , LayoutInflater inflater)
        {
            this.c=c;
            this.inflater=inflater;
        }
        @Override
        public int getCount() {
            return c.getCount();
        }


        @Override
        public String getItem(int position) {
            c.moveToPosition(position);
            return c.getString(2);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vv=inflater.inflate(R.layout.order_lv_item,parent,false);
            TextView tv1 =vv.findViewById(R.id.textView17);
            TextView tv2 =vv.findViewById(R.id.textView18);
            TextView tv3 =vv.findViewById(R.id.tvordate);
            TextView tv4 =vv.findViewById(R.id.tvoramt);
            c.moveToPosition(position);
            String orderid=c.getString(0);
            tv1.setText("OrderID"+orderid);
            SimpleDateFormat sdf =new SimpleDateFormat("dd-MMM-yyyy");
            try{
                String orddate =sdf.format(new SimpleDateFormat("yyyy-MM-dd").parse(c.getString(2)));
                tv3.setText("Order Date:"+orddate);
            }catch (Exception ex)
            {

            }
            tv2.setText("Order Status: "+c.getString(3));
            tv4.setText("Amount Payable: Rs."+c.getString(4));
            return vv;
        }
    }
}
