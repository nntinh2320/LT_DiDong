package com.tutlane.afinal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import java.util.HashSet;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class SplashScreen extends Activity {

    private int counter;
    private ProgressBar pbar;
    private Handler handler = new Handler();


    private FirebaseFirestore ff;

    public final static List<Product> plist = new ArrayList<>();
    public final static List<String> catlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        pbar = findViewById(R.id.progressBar);
        handler.postDelayed(run, 1000);
        plist.clear();
        loadList();
    }

    private void loadList() {
        ff = FirebaseFirestore.getInstance();
        ff.collection("products")
                .get()
                .addOnCompleteListener((task) -> {
                    if (task.isSuccessful()) {
                        Set<String> cats = new HashSet<>();
                        for (QueryDocumentSnapshot ds : task.getResult()) {
                            Product pp = ds.toObject(Product.class);
                            plist.add(pp);
                            cats.add(MediMartUtils.toTitleCare(pp.getPcat().trim()));
                        }
                        catlist.addAll(cats);
                    }
                });
    }

    Runnable run = () -> {
        counter++;
        pbar.setProgress(counter);
        if (counter == 100) {
            finish();
            startActivity(new Intent(SplashScreen.this, HomeActivity.class));
        } else {
            handler.postDelayed(this, 50);
        }
    }

}
