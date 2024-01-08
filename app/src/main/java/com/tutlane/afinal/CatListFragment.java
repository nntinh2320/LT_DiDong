package com.tutlane.afinal;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.window.SplashScreen;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CatListFragment extends Fragment {
    public CatListFragment(){

    }

    private RecyclerView rv;
    private FirebaseFirestore ff;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View vv=inflater.inflate(R.layout.fragment_catlist,container,false);
        rv=vv.findViewById(R.id.rvcats);
        rv.setLayoutManager(new GridLayoutManager(getActivity(),2));
        rv.setHasFixedSize(true);
        rv.setAdapter(new RVAdapter(SplashScreen.catlist));
        return vv;
    }
    class RVViewHolder extends RecyclerView.ViewHolder{
        public TextView tvpname;
        public RVViewHolder(@NonNull View itemView){
            super(itemView);
            tvpname=itemView.findViewById(R.id.textView27);
        }
    }
    class RVAdapter extends RecyclerView.Adapter<RVViewHolder>{
        private List<String> list;
        public RVAdapter(List<String> list){
            this.list=list;
            Log.d("anand","RVAdapter: "+list.size());
        }
        @NonNull
        @Override
        public RVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            View vv=LayoutInflater.from(getContext()).inflate(R.layout.cat_item,parent,false);
            RVViewHolder rvViewHolder=new RVViewHolder(vv);
            return rvViewHolder;
        }
        @Override
        public void onBindViewHolder(@NonNull RVViewHolder holder, final int position){
            holder.tvpname.setText(list.get(position));
            holder.itemView.setOnClickListener(view ->{
                CategoryFragment cf=new CategoryFragment();
                Bundle b=new Bundle();
                b.putString("cat",list.get(position));
                cf.setArguments(b);
                MediMartUtils.loadFragment(getActivity(),cf);
            });
        }
        @Override
        public int getItemCount(){return list.size();}
    }
}
