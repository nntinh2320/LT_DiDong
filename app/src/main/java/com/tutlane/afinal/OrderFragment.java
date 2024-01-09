package com.tutlane.afinal;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;

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
        if(cc.getCount()>0){
            CustomAdapter adp=new CustomAdapter(cc,inflater);
            lv.setAdapter(adp);
            lv.setLayoutManager(new LinearLayoutManager(getContext()));
            lv.setHasFixedSize(true);
        }
        return vv;
    }

    class RViewHolder extends RecyclerView.ViewHolder{
        public TextView tv1,tv2,tv3,tv4,tv5;
        public RViewHolder(@NotNull View vv){
            super(vv);
            tv1 = vv.findViewById(R.id.textView17);
            tv2 = vv.findViewById(R.id.textView18);
            tv3 = vv.findViewById(R.id.tvordate);
            tv4 = vv.findViewById(R.id.tvoramt);
            tv5 = vv.findViewById(R.id.tvcust);
        }
    }
    public class CustomAdapter extends RecyclerView.Adapter<RViewHolder>
    {
        private Cursor c;
        private LayoutInflater inflater;
        public CustomAdapter(Cursor c,LayoutInflater inflater)
        {
            this.c=c;
            this.inflater=inflater;
        }

        @NonNull
        @Override
        public RViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View vv= inflater.inflate(R.layout.allorder_lv_item,parent,false);
            RViewHolder rv=new RViewHolder(vv);
            return rv;
        }

        @Override
        public void onBindViewHolder(@NonNull RViewHolder holder, int position) {
            c.moveToPosition(position);
            final String orderid=c.getString(0);
            holder.tv1.setText("OrderID"+orderid);
            Cursor cc = db.findcustomer(c.getString(1));
            cc.moveToNext();
            String customer=cc.getString(1);
            holder.tv5.setText("CustomerID"+customer);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            try{
                String ordate = sdf.format(new SimpleDateFormat("yyyy-MM-dd").parse(c.getString(2)));
                holder.tv3.setText("Order Date"+ordate);
            }catch (Exception ex){}
            holder.tv2.setText("Order Status"+c.getString(3));
            holder.tv4.setText("Amount Payable Rs"+c.getString(4));
            holder.itemView.setOnClickListener((v)->{
                OrderDetailFragment odf = new OrderDetailFragment();
                Bundle bundle = new Bundle( );
                bundle.putString("orderid",orderid);
                odf.setArguments(bundle);
                MediMartUtils.loadFragment(getActivity(),odf);
            });
        }

        @Override
        public int getItemCount() {
            return c.getCount();
        }


    }
}
