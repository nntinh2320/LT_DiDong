package com.tutlane.afinal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

public class HomeFragment extends Fragment {
    public HomeFragment(){

    }

    private List<Integer> pics;
    private ViewPager vp;
    private int currentpage,totalpage;
    private AutoCompleteTextView tv;
    private ImageButton ib;
    private Button b1,b2;
    private MediMartDbHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        pics= Arrays.asList(R.drawable.pic5,R.drawable.pic3,R.drawable.pic4,R.drawable.pic1);
        View vv=inflater.inflate(R.layout.fragment_home2,container,false);
        vp=vv.findViewById(R.id.vpslider);
        tv=vv.findViewById(R.id.actvpnames);
        b1=vv.findViewById(R.id.bgetstarted);
        b2=vv.findViewById(R.id.bcallnow);
        ib=vv.findViewById(R.id.imbsearch);
        db=new MediMartDbHelper(getContext());

        tv.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,getProductName()));

        ib.setOnClickListener(v -> {
            DetailsFragment df= new DetailsFragment();
            Bundle bundle= new Bundle();
            String pname=tv.getText().toString();
            bundle.putString("pname",pname);
            df.setArguments(bundle);
            MediMartUtils.loadFragment(getActivity(),df);
        });
        b2.setOnClickListener(v -> {
            Intent call=new Intent(Intent.ACTION_CALL);
            String phone="2421412";
            call.setData(Uri.parse("tel:+81"+phone));
            startActivity(call);
        });b1.setOnClickListener(v -> {
            MediMartUtils.loadFragment(getActivity(),new CategoryFragment());
        });
        currentpage=0;
        totalpage=pics.size();
        vp.setAdapter(new SlidngAdapter());
        Timer timer=new Timer();
        final Handler handler=new Handler();
        timer.schedule({handler.post(runnable)},3000,4000);
        return vv;
    }

    private List<String> getProductName(){
        List<String> names=new ArrayList<>();
        for(Product p : SplashScreen.plist){
            names.add(p.getPname());
        }
        return names;
    }

    Runnable runnable = ()->{
        if(currentpage==totalpage){
            currentpage=0;
        }
        vp.setCurrentItem(currentpage++,true);
    };
    class SlidngAdapter extends PagerAdapter
    {
        @Override
        public int getCount(){return pics.size();}
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position){
            View vv=LayoutInflater.from(getActivity()).inflate(R.layout.sliding_layout,container,false));
            ImageView iv=vv.findViewById(R.id.imageView);
            iv.setImageResource(pics.get(position));
            container.addView(vv);
            return vv;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object){
            return view.equals(object);
        }
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object){
            container.removeView((View)object);
        }
    }

}
