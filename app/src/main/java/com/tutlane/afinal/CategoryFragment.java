package com.tutlane.afinal;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {
    public CategoryFragment(){

    }

    private RecyclerView rv;
    private SearchView.OnQueryTextListener queryTextListener;
    private SearchView searchView=null;
    private RVAdapter adp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View vv= inflater.inflate(R.layout.fragment_category,container,false);
        rv=vv.findViewById(R.id.rvgrid);
        rv.setLayoutManager(new GridLayoutManager(getActivity(),3));
        rv.setHasFixedSize(true);
        Bundle b=getArguments();

        List<Product> plist=new ArrayList<>();

        if(b!=null){
            String cat=b.getString("cat").trim();
            for(Product p: SplashScreen.plist){
                if(p.getPcat().trim().equalsIgnoreCase(cat)){
                    plist.add(p);
                }
            }
        }
        else{
            plist= SplashScreen.plist;
        }
        adp=new RVAdapter(plist);
        rv.setAdapter(adp);
        return vv;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater){
        menu.clear();
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem searchItem=menu.findItem(R.id.app_bar_search);
        if(searchItem!=null){
            Log.d("anand","onCreateOptiosnMenu");
            searchView=(SearchView) searchItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.d("anand","onQueryTextSubmit:"+query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.d("anand","onQueryTextSubmit:"+newText);
                    adp.getFilter().filter(newText);
                    return true;
                }
            });
        }
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item){return true;}
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    class RVViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivpic;
        public TextView tvpname;
        public TextView tvprice;
        public RVViewHolder(@NonNull View itemView){
            super(itemView);
            tvpname=itemView.findViewById(R.id.tvpname);
            tvprice=itemView.findViewById(R.id.tvprice);
            ivpic=itemView.findViewById(R.id.ivprod);
        }
        public void setData(Product p){
            tvpname.setText(p.getPname());
            tvprice.setText(""+p.getPrice());
            Glide.with(getContext()).load(p.getPic())
                    .into(ivpic);
        }
    }
    class RVAdapter extends RecyclerView.Adapter<RVViewHolder> implements Filterable{
        private List<Product> list;
        private List<Product> templist=new ArrayList<>();
        public RVAdapter(List<Product> list){
            this.list=list;
            templist.addAll(list);
        }
        @NonNull
        @Override
        public RVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            View vv= LayoutInflater.from(getContext()).inflate(R.layout.gv_product_item,parent,false);
            RVViewHolder rvViewHolder=new RVViewHolder(vv);
            return rvViewHolder;
        }
        @Override
        public void onBindViewHolder(@NonNull RVViewHolder holder, final int position){
            holder.setData(list.get(position));
            holder.itemView.setOnClickListener(v -> {
                DetailsFragment df=new DetailsFragment();
                Bundle bundle=new Bundle();
                bundle.putString("pname",list.get(position).getPname());
                df.setArguments(bundle);
                MediMartUtils.loadFragment(getActivity(),df);
            });
        }
        @Override
        public int getItemCount(){return list.size();}

        @Override
        public Filter getFilter(){
            return new Filter(){
                @Override
                protected FilterResults performFiltering(CharSequence charSequence){
                    Log.d("anand","performFiltering: "+charSequence);
                    List<Product> filteredList=new ArrayList<>();
                    if(charSequence==null || charSequence.length()==0){
                        filteredList.addAll(templist);
                    }
                    else{
                        String pattern=charSequence.toString().trim().toLowerCase();
                        for(Product p : templist){
                            if(p.getPname().toLowerCase().trim().contains(pattern)){
                                filteredList.add(p);
                            }
                        }
                    }
                    FilterResults results = new FilterResults();
                    results.values=filteredList;
                    return results;
                }
                @Override
                protected void publishResults(CharSequence charSequence,FilterResults filterResults){
                    list.clear();
                    list.addAll((List<Product>)filterResults.values);
                    notifyDataSetChanged();
                }
            };
        }
    }
}
