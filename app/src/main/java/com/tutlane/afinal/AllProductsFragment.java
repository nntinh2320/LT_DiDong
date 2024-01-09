package com.tutlane.afinal;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class AllProductsFragment extends Fragment {
    public AllProductsFragment(){

    }

    private RecyclerView lv;
    private List<Product> list;
    private CustomAdapter adp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View vv=inflater.inflate(R.layout.fragment_all_product,container,false);
        lv=vv.findViewById(R.id.lvp);
        lv.setLayoutManager(new LinearLayoutManager(getContext()));
        lv.setHasFixedSize(true);
        return vv;
    }

    private void deleteProduct(final String pname,final int position){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure to delete this record ?");
        builder.setIcon(android.R.drawable.ic_delete);
        builder.setPositiveButton("Delete Now",(dialog, which) -> {
            removeProduct(pname,position);
        });
        builder.show();
    }
    private void updateProduct(final String pname, final int price, final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Update Product");
        View vv=getLayoutInflater().inflate(R.layout.edit_item,null,false);
        builder.setView(vv);
        final EditText et1=vv.findViewById(R.id.eteitname);
        final EditText et2=vv.findViewById(R.id.eteditprice);
        et1.setText(pname);
        et2.setText(""+price);
        builder.setIcon(android.R.drawable.ic_menu_edit);
        builder.setPositiveButton("Save Now",(dialog, which) -> {
            final String newname=et1.getText().toString();
            final int newprice=Integer.parseInt(et2.getText().toString());
            updatePro(pname,newname,newprice,position);
        });
        builder.show();
    }

    private void updatePro(String pname, final String nname,final int nprice, final int position){
        final FirebaseFirestore ff=FirebaseFirestore.getInstance();
        ff.collection("products").whereEqualTo("pname",pname)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        final Product pp=task.getResult().toObjects(Product.class)
                                .get(0);
                        pp.setPname(nname);
                        pp.setPrice(nprice);
                        ff.collection("products").document(pp.getProid())
                                .set(pp)
                                .addOnCompleteListener(task1 -> {
                                    Toast.makeText(getContext(), "Product Updated", Toast.LENGTH_SHORT).show();
                                    SplashScreen.plist.set(position,pp);
                                    adp.notifyItemChanged(position);
                                });
                    }
                });

    }
    @Override
    public void onStart(){
        super.onStart();
        list=SplashScreen.plist;
        adp=new CustomAdapter(list);
        lv.setAdapter(adp);
    }

    private void removeProduct(final String pname, final int position){
        final FirebaseFirestore ff=FirebaseFirestore.getInstance();
        ff.collection("products").whereEqualTo("pname",pname).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Product p=task.getResult().toObjects(Product.class).get(0);
                        StorageReference sref=FirebaseFirestore.getInstance()
                                .getReferenceFromUrl(p.getPic());
                        sref.delete()
                                .addOnSuccessListener((OnSuccessListener)aVoid->{
                                    SplashScreen.plist.remove(position);
                                    adp.notifyItemRemoved(position);
                                } );
                        ff.collection("products").document(p.getProid())
                                .delete()
                                .addOnCompleteListener(task1 -> {
                                    Toast.makeText(getContext(),"Product Deleted",Toast.LENGTH_SHORT).show();
                                });
                    }
                });
    }

    class RVHolder extends RecyclerView.ViewHolder{
        private TextView tv1,tv2;
        private ImageButton bdel,bup;
        public RVHolder(@NonNull View itemView){
            super(itemView);
            tv1=itemView.findViewById(R.id.tvapname);
            tv2=itemView.findViewById(R.id.tvaprice);
            bdel=itemView.findViewById(R.id.badel);
            bup=itemView.findViewById(R.id.baupdate);
        }
    }

    class CustomAdapter extends RecyclerView.Adapter<RVHolder>{
        @NonNull
        @Override
        public RVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            View v1=getActivity().getLayoutInflater().inflate(R.layout.list_item,parent,false);
            RVHolder rv=new RVHolder(v1);
            return rv;
        }

        @Override
        public void onBindViewHolder(@NonNull RVHolder holder,final int position){
            final Product p=list.get(position);
            holder.tv1.setText(p.getPname());
            holder.tv2.setText(String.format("Price: %.2f",(float)p.getPrice()));
            holder.bdel.setOnClickListener(v -> {
                deleteProduct(p.getPname(),position);
            });
            holder.bup.setOnClickListener(v -> {
                updateProduct(p.getPname(),p.getPrice(),position);
            });
        }

        @Override
        public int getItemCount(){return list.size();}
        private List<Product> list;
        public CustomAdapter(List<Product> list){this.list=list;}
    }

}
