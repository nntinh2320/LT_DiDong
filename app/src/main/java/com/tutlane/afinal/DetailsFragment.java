package com.tutlane.afinal;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;

public class DetailsFragment extends Fragment {
    public DetailsFragment(){

    }
    private TextView tv,tv2;
    private TextView et;
    private ImageView iv;
    private Button b1,b2,b3;
    private SharedPreferences sp;
    private SharedPreferences.Editor edit;
    private MediMartDbHelper db;
    private DatabaseReference dref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View vv=inflater.inflate(R.layout.fragment_details,container,false);
        tv=vv.findViewById(R.id.textView);
        tv2=vv.findViewById(R.id.textView2);
        iv=vv.findViewById(R.id.imageView2);
        b1=vv.findViewById(R.id.baddtocart);
        b2=vv.findViewById(R.id.bqminus);
        b3=vv.findViewById(R.id.bqadd);
        tv=vv.findViewById(R.id.etcqty);
        b3.setOnClickListener(v->{
            int qty=Integer.parseInt(et.getText().toString());
            et.setText(""+(qty-1));
        });
        b2.setOnClickListener(v->{
            int qty=Integer.parseInt(et.getText().toString());
            if(qty==1){
                Snackbar.make(v,"Minimum Quantity 1", BaseTransientBottomBar.LENGTH_LONG).show();
            }
            else{
                et.setText(""+(qty-1));
            }
        });

        sp=getActivity().getSharedPreferences("users", Context.MODE_PRIVATE);
        db=new MediMartDbHelper(getContext());
        final String userid=sp.getString("userid","guest");
        Bundle b=this.getArguments();
        final String pname=b.getString("pname");
        findproduct(pname);
        if(db.checkItemincart(pname,userid)){
            b1.setEnabled(false);
            b1.setBackgroundColor(Color.GRAY);
            b1.setText("Already in Cart");
            Snackbar.make(getActivity().getCurrentFocus(),"Already in Cart",Snackbar.LENGTH_LONG).show();
        }
        else{
            b1.setEnabled(true);
            b1.setText("Add to Cart");
        }
        b1.setOnClickListener(v ->{
            if(userid.equals("guest")){
                Toast.makeText(getContext(), "Please login first", Toast.LENGTH_SHORT).show();
                MediMartUtils.loadFragment(getActivity(),new LoginFragment());
            }
            else{
                String pname2=tv.getText().toString();
                int qty = Integer.parseInt(et.getText().toString());
                db.addtocart(userid,pname2,qty);

                final Snackbar sn= Snackbar.make(v,"Product Added to cart",Snackbar.LENGTH_LONG);
                sn.show();
                MediMartUtils.loadwithoutHistoryFragment(getActivity(),new CategoryFragment());
            }
        });
        return vv;
    }

    private void findproduct(String pname) {
        for(Product pp: SplashScreen.plist){
            if(pp.getPname().equals(pname)){
                tv.setText(pp.getPname());
                tv2.setText(""+pp.getPrice());
                Glide.with(getContext())
                        .load(pp.getPic())
                        .into(iv);
                break;
            }
        }
    }

}
