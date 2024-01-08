package com.tutlane.afinal;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.window.SplashScreen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;

import kotlinx.coroutines.scheduling.Task;

public class AddProductFragment extends Fragment {

    public AddProductFragment(){

    }
    private EditText et1,et2,et3;
    private ImageView b1;
    private Button b;
    private StorageReference sRef;
    private FirebaseFirestore ff;
    private Uri uri;
    private DbHelper db;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Intent.ACTION_PICK);
                Intent.setData();
                startActivityForResult(intent,100);
            }
        });

        b.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
               b.setEnabled(false);
               SplashScreen.plist.clear();
               final String photo = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()+".jpg");
               sref = FirebaseStorage.getInstance().getReference();
               sref.putfile(uri)
                       .continueWithTask(new Continuation<UploadTask.TaskSnapshot>, Task<Uri>(){
                   @Override
                   public Task<Uri> then(@NonNull Task<UploadTask.TakeSnapshot> task) throws Exception{
                       if(task.isSuccesful())
                           return sref.getDownloadUrl();
                       else
                           throw task.getException();
                   }
           }).addOnCompleteListener(new MediaPlayer.OnCompletionListener<Uri>(){
               @Override
               public void onComplete(@NonNull Task<Uri> task){
                   final String photoname=task.getResult().toString();
                   final String pname = et1.getText().toString();
                   final int price = Integer.parseInt(et2.getText().toString());
                   final String pcat = et3.getText().toString();
                   DocumentReference newpro = ff.collection("products").document();
                   Product p = new Product(pname,pcat,photoname,price);
                   p.setProdid(newpro.getId());
                   newpro.set(p).addOnCompleteListener(new OnCompleteListener<Void>(){
                       et1.setText("");
                       et2.setText("");
                       et3.setText("");
                       b1.setImageResource();
                       et1.requestFocus();
                       Toast.makeText(getContext(),"Product Saved",Toast.LENGTH_SHORT).show();
                       b.setEnabled(true);
                       //MediMartUtils.getList();
                       }
                   });
               }
               });
        }

        });
        return vv;
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==100&&resultCode==RESULT_OK){
            uri = data.getData();
            b1.setImageURI(uri);
        }

    }
    



}
