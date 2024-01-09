package com.tutlane.afinal;

public class CartItem {
    private Product product;
    private int qty;
    private String userId;
    public CartItem(Product product,int qty, String userid){
        this.product = product;
        this.qty = qty;
        this.userId = userid;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
