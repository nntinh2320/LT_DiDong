package com.tutlane.afinal;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.concurrent.atomic.AtomicReference;

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

        AtomicReference<Cursor> c=null;
        if(role.equals("customer")){
            c.set(db.findcustomer(userid));
        }
        else{
            c.set(db.findVendor(userid));
        }
        c.get().moveToNext();
        tv.setText(c.get().getString(1));

        b.setOnClickListener(v -> {
            final String pwd=et1.getText().toString();
            final String npwd=et2.getText().toString();
            final String cpwd=et3.getText().toString();
            c.set(db.validate(userid, pwd));
            if(c.get().moveToNext()){
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
