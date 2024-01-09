package com.tutlane.afinal;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Product {
    private String proid;
    private String pname;
    private String pcat;
    private String pic;
    private int price;
    public Product (String pname, String pcat,String pic,int price)
    {
        this.pname=pname;
        this.pcat=pcat;
        this.pic=pic;
        this.price=price;
    }
    @Exclude
    public Map<String ,Object> toMap(){
        HashMap<String,Object>result = new HashMap<>();
        result.put("pname",pname);
        result.put("price",price);
        result.put("pic",pic);
        result.put("proid",proid);
        result.put("pcat",pcat);
        return result;

    }

    public Product(){}

    public String getProid() {
        return proid;
    }

    public String getPname() {
        return pname;
    }

    public String getPcat() {
        return pcat;
    }

    public String getPic() {
        return pic;
    }

    public int getPrice() {
        return price;
    }

    public void setProid(String proid) {
        this.proid = proid;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public void setPcat(String pcat) {
        this.pcat = pcat;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @NonNull
    @Override
    public String toString()
    {
        return getPname();
    }
}
