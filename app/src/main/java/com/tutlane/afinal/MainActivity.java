package com.tutlane.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bnav;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MediMartUtils.loadFragment(this,new HomeFragment());
        bnav=findViewById(R.id.bnavar);
        bnav.setOnNavigationItemSelectedListener(this);
        sp=getSharedPreferences("users",MODE_PRIVATE);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            if(checkSelfPermission(Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE},100);
            }
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},200);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment f=null;
        switch (item.getItemId()){
            case R.id.mhome:
                f=new HomeFragment();
                break;
            case R.id.mnoti:
                f=new OrderHistoryFragment();
                break;
            case R.id.maccount:
                String user = sp.getString("userid","guest");
                if(user.equals("guest")){
                    f=new AccountFragment();
                }
                else{
                    String role = sp.getString("role","no");
                    if(role.equals("customer")){
                        f=new ProfileFragment();
                    }
                    else{
                        f=new VendorProfile();
                    }
                }
                break;
            case R.id.mcart:
                f=new CartFragment();
                break;
        }
        return MediMartUtils.loadFragment(this,f);
    }

    public void login(View view){
        MediMartUtils.loadFragment(this,new LoginFragment());
    }
    public void register(View view){
        MediMartUtils.loadFragment(this,new RegisterFragment());
    }
    public void contactus(View view ){
        MediMartUtils.loadFragment(this,new ContactUsFragment());
    }
    public void aboutus(View view){
        MediMartUtils.loadFragment(this,new AboutUsFragment());
    }

}