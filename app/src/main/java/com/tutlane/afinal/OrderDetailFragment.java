package com.tutlane.afinal;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class OrderDetailFragment extends Fragment
{
    public OrderDetailFragment()
    {}

    private ListView lv;
    private MediMartDbHelper db;
    private TextView tv;
    private LinearLayout ll;
    private SharedPreferences sp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vv =inflater(R.layout.fragment_order_details,container,false);
        lv=vv.findViewById(R.id.lvsub);
        db=new MediMartDbHelper(getContext());
        tv=vv.findViewById(R.id.textview30);
        ll=vv.findViewById(R.id.linearLayout3);
        sp=getActivity().getSharedPreferences("users", Context.MODE_PRIVATE);
        final  String orderid=getArguments().getString("orderid");
        tv.setText("Order Details of"+orderid);
        Cursor cx=db.getOrderDetails(orderid);
        CustomAdapter adp=new CustomAdapter(cx);
        String role =sp.getString ("role","no");
        cx.moveToFirst();
        String status=cx.getString(6);
        if(role.equals("admin"))
        {
            Button b1=new Button(getContext())  ;
            b1.setText("Confirm Order");
            b1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,1));
            b1.setBackgroundColor(Color.BLUE);
            b1.setTextColor(Color.WHITE);
            b1.setOnClickListener((view)->{
                Toast.makeText(getContext(),"Order Confirm",Toast.LENGTH_SHORT).show();
                db.changeStatus(orderid,"Confirmed");
                MediMartUtils.loadwithoutHistoryFragment(getActivity(),new OrderFragment());
            });
            Button b2=new Button(getContext())  ;
            b2.setText("Cancel Order");
            b2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,1));
            b2.setBackgroundColor(Color.RED);
            b2.setTextColor(Color.WHITE);
            b2.setOnClickListener((view)->{
                db.changeStatus(orderid,"Cancelled due to Out of Stock");
                Toast.makeText(getContext(),"Order Cancelled",Toast.LENGTH_SHORT).show();
                MediMartUtils.loadwithoutHistoryFragment(getActivity(),new OrderFragment());
            });
            ll.addView(b1);
            ll.addView(b2);
        }
        else{
            TextView tv=new TextView(getContext());
            tv.setLayoutParams(
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,1));
            tv.setText("Order Status"+status);
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv.setPadding(10,10,10,10);
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(16);
            ll.addView(tv);
            }
        lv.setAdapter(adp);
        return vv;
        }

        class CustomAdapter extends BaseAdapter{

        private Cursor c;

        private  CustomAdapter(Cursor c)
        {
            this.c=c;
        }
            @Override
            public int getCount() {
                return c.getCount();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View vv =getLayoutInflater().inflate(R.layout.inner_lv_item,parent,false);
                TextView tv1=vv.findViewById(R.id.textView19);
                TextView tv2=vv.findViewById(R.id.textView20);
                TextView tv3=vv.findViewById(R.id.textView29);
                c.moveToNext();
                String pname=c.getString(2);
                tv1.setText("Product: "+pname);
                tv2.setText("Qty"+c.getString(3));
                tv3.setText("Price Rs:"+getPrice(pname));
                return vv;
            }
            private int getPrice(String pname)
            {
                for(Product p :SplashScreen.plist)
                {
                    if(p.getPname().equal(pname))
                    {
                        return p.getPrice();
                    }
                }
                return 0;
            }
        }


    }

