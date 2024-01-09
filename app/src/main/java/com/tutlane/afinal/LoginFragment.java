package com.tutlane.afinal;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {

    public LoginFragment(){}

    private EditText et1,et2;
    private Button b1;
    private MediMartDbHelper db;
    private SharedPreferences sp;
    private SharedPreferences.Editor edit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState){
        View vv=inflater.inflate(R.layout.fragment_login,container,false);
        et1=vv.findViewById(R.id.etuser1);
        et2=vv.findViewById(R.id.etpwd1);
        b1=vv.findViewById(R.id.blogin);
        db=new MediMartDbHelper(getContext());
        getActivity().setTitle("MediMart-Customer Login");
        sp= getActivity().getSharedPreferences("users", Context.MODE_PRIVATE);
        edit=sp.edit();
        b1.setOnClickListener((v)->{
            String userid=et1.getText().toString();
            String pwd=et2.getText().toString();
            if(userid.length()==0)
            {
                et1.setHintTextColor(Color.RED);
                et1.setError("User ID is required");
            }
            else {
                Cursor c=db.validate(userid,pwd);
                if(c.moveToNext())
                {
                    String role = c.getString(3);
                    Log.d("anand","onClick: -"+role+"-");
                    if(role.equals("admin"))
                    {
                        edit.putString("userid",userid);
                        edit.putString("role",role);
                        edit.commit();
                        MediMartUtils.loadwithoutHistoryFragment(getActivity(),new CategoryFragment());
                    }
                    else {
                        edit.putString("userid",userid);
                        edit.putString("role",role);
                        edit.commit();
                        MediMartUtils.loadwithoutHistoryFragment(getActivity(),new CategoryFragment());
                    }
                    ((MediMartUtils)getActivity()).loadmenu();
                }
                else {
                    Toast.makeText(getContext(),"Invalid Username or Password",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return vv;
    }
}
