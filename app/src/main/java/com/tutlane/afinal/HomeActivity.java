package com.tutlane.afinal;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout dlayout;
    private NavigationView nav;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private SharedPreferences sp;
    private MediMartDbHelper db;
    private TextView tv;
    @Override

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        drawerToggle=new ActionBarDrawerToggle(this.getParent(),dlayout,toolbar,"On","Off");
        dlayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        nav.setNavigationItemSelectedListener(this);
        permissions();
        MediMartUtils.loadFragment(this,new CategoryFragment());
        loadmenu();
    }

    public void init(){
        dlayout=findViewById(R.id.dlayout);
        nav=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        tv=findViewById(R.id.tvusername);
        sp=getSharedPreferences("users",MODE_PRIVATE);
        db=new MediMartDbHelper(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mega Mart");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.search_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId()==R.id.mcart){
            String user=sp.getString("userid","guest");
            if(user.equals("guest")){
                MediMartUtils.loadFragment(this, new LoginFragment());
                Snackbar.make(getCurrentFocus(),"Please login first", BaseTransientBottomBar.LENGTH_LONG).show();
            }
            else{
                MediMartUtils.loadFragment(this,new CartFragment());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadmenu(){
        String user=sp.getString("userid","guest");
        Menu menu=nav.getMenu();
        switch(user){
            case "guest":
                Log.d("anand","loadmenu as "+user);
                menu.setGroupVisible(R.id.gadmin,false);
                menu.setGroupVisible(R.id.guser,false);
                menu.setGroupVisible(R.id.gcustomer,false);
                tv.setText("Welcome Guest");
                break;
            case "admin":
                Log.d("anand","loadmenu as "+user);
                menu.setGroupVisible(R.id.gadmin,true);
                menu.setGroupVisible(R.id.guser,true);
                menu.setGroupVisible(R.id.gcustomer,false);
                tv.setText("Welcome Admin");
                break;
            default:
                Log.d("anand","loadmenu as "+user);
                menu.setGroupVisible(R.id.gadmin,false);
                menu.setGroupVisible(R.id.guser,false);
                menu.setGroupVisible(R.id.gcustomer,false);
                Cursor c=db.findcustomer(user);
                c.moveToNext();
                tv.setText("Welcome "+c.getString(1));
                break;
        }
    }

    private void permissions(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 100);
            }
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        Fragment f=null;
        switch (item.getItemId()){
            case R.id.mhome:
                f=new HomeFragment();
                break;
            case R.id.mcat:
                f=new CatListFragment();
                break;
            case R.id.mnoti:
                f=new OrderHistoryFramgnet();
                break;
            case R.id.mproducts:
                f=new CategoryFragment();
                break;
            case R.id.morders:
                f=new OrdersFragment();
                break;
            case R.id.mchangepwd:
                f=new ChangePwdFragment();
                break;
            case R.id.maddpro:
                f=new AddProductFragment();
                break;
            case R.id.mshowall:
                f=new AllProductsFragment();
                break;
            case R.id.mlogout:
                SharedPreferences.Editor edit=sp.edit();
                edit.remove("userid");
                edit.remove("role");
                edit.commit();
                f=new CategoryFragment();
                Toast.makeText(this, "Sign out successfully!", Toast.LENGTH_SHORT).show();
                dlayout.closeDrawer(GravityCompat.START);
                return MediMartUtils.loadwithoutHistoryFragment(this,f);
            case R.id.mcart:
                f=new CartFragment();
                break;
        }
        dlayout.closeDrawer(GravityCompat.START);
        return MediMartUtils.loadFragment(this,f);
    }

    @Override
    public void onBackPressed(){
        if(dlayout.isDrawerOpen(GravityCompat.START)){
            dlayout.closeDrawer(GravityCompat.START);
        }
        if(getSupportFragmentManager().getBackStackEntryCount()==0){
            super.onBackPressed();
        }
        else{
            getSupportFragmentManager().popBackStack();
        }
    }

    public void login(View view){MediMartUtils.loadFragment(this,new LoginFragment());}
    public void register(View view){MediMartUtils.loadFragment(this,new RegisterFragment());}

    public void contactus(View view){MediMartUtils.loadFragment(this,new ContactUsFragment());}
    public void aboutus(View view){MediMartUtils.loadFragment(this,new AboutUsFragment());}


}
