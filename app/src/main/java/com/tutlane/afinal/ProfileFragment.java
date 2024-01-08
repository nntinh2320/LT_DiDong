package com.tutlane.afinal;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProfileFragment extends Fragment
{
    public ProfileFragment ()
    {

    }

    private TextView tv1,tv2,tv3,tv4,tv5;
    private FloatingActionButton fab1,fab2;
    private SharedPreferences sp;
    private SharedPreferences.Editor edit;
    private MediMartDbHelper db;


    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
       View vv= inflater.inflate(R.layout.fragment_profile,container,false);
       tv1=vv.findViewById(R.id.tvprousername);
       tv2=vv.findViewById(R.id.tvproemail);
       tv3=vv.findViewById(R.id.tvprocity);
       tv4=vv.findViewById(R.id.tvprophone);
       tv5=vv.findViewById(R.id.tvprogender);
       fab1=vv.findViewById(R.id.fablogout);
       fab2=vv.findViewById(R.id.fabchangepwd);
       getActivity().setTitle("MediMart-Customer Profile");
       sp=getActivity().getSharedPreferences("users", Context.MODE_PRIVATE);
       edit=sp.edit();
       db=new MediMartDbHelper(getContext());
       String userid=sp.getString("userid","guest");
       tv4.setText("Contect Nummber\n"+userid);
       Cursor c= db.findcustomer(userid);
       c.moveToNext();
       tv1.setText(c.getString(1));
       tv3.setText("City"+c.getString(2));
       tv2.setText("Email\n"+c.getString(4));
       tv5.setText("Gender:"+c.getString(3));

       fab2.setOnClickListener((v)->{
           MediMartUtils.loadFragment(getActivity(),new ChangePwdFragment());
        });
       fab1.setOnClickListener((v)->{
           edit.remove("userid");
           edit.commit();
           MediMartUtils.loadFragment(getActivity(),new HomeFragment());
           Toast.makeText(getContext(),"Sign out successfully",Toast.LENGTH_SHORT).show();
        });
        return vv;
    }
}
