package com.tutlane.afinal;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MediMartUtils {
    public static String toTitleCare(String str) {
        if (str == null) {
            return null;
        }
        boolean space = true;
        StringBuilder builder = new StringBuilder(str);
        final int len = builder.length();

        for (int i = 0; i < len; i++) {
            char c = builder.charAt(i);
            if (space)
            {
                if (!Character.isWhitespace(c))
                {
                    builder.setCharAt(i, Character.toTitleCase(c));
                    space = false;
                }
                else if (Character.isWhitespace(c))
                {
                    space = true;
                }
                else
                {
                    builder.setCharAt(i, Character.toLowerCase(c));
                }
            }
        }
        return builder.toString();
    }

    public static void getList()
    {
        FirebaseFirestore ff= FirebaseFirestore.getInstance();
        SplashScreen.plit.clear();
        ff.collection("products")
                .get()
                .addOnCompleteListener((task -> {
                    if(task.isSuccessful())
                    {
                        for(QueryDocumentSnapshot ds : task.getResult())
                        {
                            Product pp = ds.toObject(Product.class);
                            SplashScreen.plit.add(pp);
                        }
                    }
                }));
    }
    public static boolean loadFragment(FragmentActivity ctx, Fragment fragment)
    {
        if(fragment!=null)
        {
            ctx.getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.container,fragment)
                    .commit();
            return true;
        }
        return false ;
    }

    public static boolean loadwithoutHistoryFragment(FragmentActivity ctx, Fragment fragment)
    {
        if(fragment!=null)
        {
            ctx.getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.container,fragment)
                    .commit();
            return true;
        }
        return false ;
    }

}
