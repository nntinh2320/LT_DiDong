package com.tutlane.afinal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
    public CartFragment(){

    }

    private MediMartDbHelper db;
    private SharedPreferences sp;
    private ListView lv;
    private TextView tv,tv5,tv6;
    private Button b;
    private CustomAdapter adp;
    private List<CartItem> list;
    private int qty;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        db=new MediMartDbHelper(getContext());
        getActivity().setTitle("Your Cart");
        sp=getActivity().getSharedPreferences("users", Context.MODE_PRIVATE);
        View vv=inflater.inflate(R.layout.fragment_cart,container,false);
        lv=vv.findViewById(R.id.lvcart);
        tv=vv.findViewById(R.id.textView9);
        tv5=vv.findViewById(R.id.tvvat);
        tv6=vv.findViewById(R.id.tvnet);
        b=vv.findViewById(R.id.fabcheckout);
        b.setVisibility(View.INVISIBLE);
        list=getList();
        lv.setOnItemClickListener((parent, view, position, id) -> {
            final String pname=list.get(position).getProduct().getPname();
            qty=list.get(position).getQty();
            final String userid=sp.getString("userid","guest");
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            View vv = getLayoutInflater().inflate(R.layout.cart_item,null,false);
            TextView tv1=vv.findViewById(R.id.tvcpname);
            final TextView et1=vv.findViewById(R.id.etcqty);
            Button b1=vv.findViewById(R.id.bqminus);
            Button b2=vv.findViewById(R.id.bqadd);
            b2.setOnClickListener((v)->{
                qty=Integer.parseInt(et1.getText().toString())+1;
                et1.setText(""+(qty));
            });
            b1.setOnClickListener((v)->{
                qty=Integer.parseInt(et1.getText().toString());
                if(qty==1){
                    Snackbar.make(v,"Minimum Quantity 1", BaseTransientBottomBar.LENGTH_LONG).show();
                }
                else{
                    qty--;
                    et1.setText(""+(qty));
                }
            });
            tv1.setText(pname);
            et1.setText(""+qty);
            builder.setView(vv);
            builder.setTitle("Cart Item Update");
            builder.setNegativeButton("Update Quantity",(dialog, which) -> {
               db.updatecart(pname,userid,qty);
               lv.setAdapter(new CustomAdapter(getList(),inflater));
               calc();
            });
            builder.setPositiveButton("Delete Now",(dialog, which) -> {
                Toast.makeText(getContext(),"Product Removed",Toast.LENGTH_SHORT).show();
                db.deletefromcart(pname,userid);
                lv.setAdapter(new CustomAdapter(getList(),inflater));
                calc();
            });
            builder.show();
        });
        if(list.size()==0){
            MediMartUtils.loadwithoutHistoryFragment(getActivity(),new EmptyCartFragment());
        }
        else{
            adp=new CustomAdapter(list,inflater);
            lv.setAdapter(adp);
            calc();
            b.setVisibility(View.VISIBLE);
            b.setOnClickListener(v -> {
                MediMartUtils.loadFragment(getActivity(),new PaymentFragment());
            });
        }
        return vv;
    }

    private void calc(){
        float amount=totalBill();
        tv.setText(String.format("%.2f",amount));
        float gst=amount*.10f;
        float net=amount+gst;
        tv5.setText(String.format("%.2f",gst));
        tv6.setText(String.format("%.2f",net));
    }
    private class CustomAdapter extends BaseAdapter{
        private List<CartItem> list;
        private LayoutInflater inflater;
        public CustomAdapter(List<CartItem> list, LayoutInflater inflater){
            this.list=list;
            this.inflater= inflater;
        }
        @Override
        public int getCount(){return list.size();}
        @Override
        public Object getItem(int positon){return list.get(positon);}
        @Override
        public long getItemId(int position){return 0;}
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View vv=inflater.inflate(R.layout.cart_item_layout,parent,false);
            TextView tv1=vv.findViewById(R.id.tvcpname);
            TextView tv2=vv.findViewById(R.id.tvcprice);
            TextView tv3=vv.findViewById(R.id.);
            TextView tv4=vv.findViewById(R.id.tvcamt);

            tv1.setText(list.get(position).getProduct().getPname());
            float price=list.get(position).getProduct().getPrice();
            int qty=list.get(position).getQty();
            float amount=price*qty;
            tv2.setText(String.format("%.2f",price));
            tv3.setText(String.valueOf(qty));
            tv4.setText(String.format("%.2f",amount));

            if(position%2==0){
                vv.setBackgroundResource(R.color.fav1);
            }
            else{
                vv.setBackgroundResource(R.color.fav2);
            }
            return vv;
        }

    }
    public List<CartItem> getList(){
        String userid=sp.getString("userid","guest");
        List<CartItem> list=null;
        if(userid.equals("guest")){
            Toast.makeText(getContext(),"Please Login First",Toast.LENGTH_SHORT).show();
        }
        else{
            Cursor c = db.getCartItems(userid);
            list=new ArrayList<>();
            while(c.moveToNext()){
                String pname=c.getString(2);
                Product product=findByName(pname);
                int qty=c.getInt(3);
                CartItem item=new CartItem(product,qty,userid);
                list.add(item);
            }
        }
        return list;
    }
    private Product findByName(String pname){
        List<Product> list= SplashScreen.plist;
        for(Product p: list){
            if(p.getPname().equals(pname)){
                return p;
            }
        }
        return null;
    }
    public float totalBill(){
        List<CartItem> list=getList();
        float total=0;
        for(CartItem item:list){
            total+=(item.getQty()*item.getProduct().getPrice());
        }
        return total;
    }
}
