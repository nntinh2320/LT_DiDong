package com.tutlane.afinal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class ChangePwdFragment extends Fragment {
    public ChangePwdFragment(){

    }

    private EditText et1,et2,et3;
    private Button b;
    private TextView tv;
    private SharedPreferences sp;
    private MediMartDbHelper db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View vv=inflater.inflate(R.layout.fragment_change_pwd,container,false);
        et1=vv.findViewById(R.id.editText6);
        et2=vv.findViewById(R.id.editText7);
        et3=vv.findViewById(R.id.editText8);
        tv=vv.findViewById(R.id.textView15);
        b=vv.findViewById(R.id.button2);
        getActivity().setTitle("Change Password");
        sp=getActivity().getSharedPreferences("users", Context.MODE_PRIVATE);
        db=new MediMartDbHelper(getContext());

        final String userid=sp.getString("userid","guest");
        final String role=sp.getString("role","no");

        Cursor c=null;
        if(role.equals("customer")){
            c=db.findcustomer(userid);
        }
        else{
            c=db.findVendor(userid);
        }
        c.moveToNext();
        tv.setText(c.getString(1));

        b.setOnClickListener(v -> {
            final String pwd=et1.getText().toString();
            final String npwd=et2.getText().toString();
            final String cpwd=et3.getText().toString();
            Cursor c=db.validate(userid,pwd);
            if(c.moveToNext()){
                if(npwd.equals(cpwd)){
                    db.updatePwd(userid,npwd);
                    MediMartUtils.loadwithoutHistoryFragment(getActivity(),new ProfileFragment());
                    Toast.makeText(getContext(),"Password Updated",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "Password not confirmed", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(getContext(), "Incorrect current password", Toast.LENGTH_SHORT).show();
            }
        });
        return vv;
    }
}
