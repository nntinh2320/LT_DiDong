package com.tutlane.afinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MediMartDbHelper extends SQLiteOpenHelper {
    public MediMartDbHelper(@Nullable Context context){super(context,"medimart.db",null,1);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS users(_id interger primary key autoincrement,"+
                "userid varchar(100),pwd varchar(30),role varchar(20))");
        db.execSQL("CREATE TABLE IF NOT EXISTS customers(_id interger primary key autoincrement,"+
                "cname varchar(100),city varchar(100),gender varchar(20),"+
                "email varchar(100),phone varchar(20))");
        db.execSQL("CREATE TABLE IF NOT EXISTS cart(_id interger primary key autoincrement,"+
                "userid varchar(100),orderdate date,status varchar(20),total int)");
        db.execSQL("CREATE TABLE IF NOT EXISTS order_details(_id interger primary key autoincrement,"+
                "orderid int ,pname varchar(100),qty int)");
        db.execSQL("INSERT INTO users(userid,pwd,role)"+
                "VALUES('admin','admind','admin')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void changeStatus(String orderid,String status){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("status",status);
        db.update("order",cv,"_id=?",new String[]{orderid});
    }

    public Cursor getOrders(String userid){
        SQLiteDatabase db=getReadableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM orders WHERE userid=? order by _id desc",new String[]{userid});
        return c;
    }

    public Cursor getALLOrders(){
        SQLiteDatabase db=getReadableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM orders WHERE status='Peding' order by _id desc",null);
        return c;
    }

    public Cursor getOrderDetails(String orderid){
        SQLiteDatabase db=getReadableDatabase();
        Cursor c=db.rawQuery("SELECT 0._id,orderid,pname,qtv,orderdate,userid,status,total FROM order_details od JOIN orders o on o._id=od.orderid WHERE orderid=?",new String[]{orderid});
        return c;
    }

    public void deletefromcart(String pname,String userid)
    {
        SQLiteDatabase db=getWritableDatabase();
        db.delete("cart","userid=? and product=?",new String[]{userid,pname});
    }

    public void updatecart(String pname, String userid,int qty)
    {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("qty",qty);
        db.update("cart",cv,"userid=? and product = ?",new String[]{userid,pname});
    }

    public boolean checkItemicart(String pname,String userid){
        SQLiteDatabase db=getReadableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM cart WHERE userid=? and product=?",new String[]{userid,pname});
        return c.moveToNext();
    }

    public void emptycart(String userid)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("cart","userid=?",new String []{userid});
    }

    public int gen_order_no(){
        SQLiteDatabase db= getReadableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM orders",null);
        return c.getCount()+1;
    }

    public void saveorder(String userid)
    {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        int orderno=gen_order_no();
        int total =0;
        Cursor c =getCartItems(userid);
        while(c.moveToNext())
        {
            String pname =c.getString(2);
            int qty =c.getInt(3);
            int price = findProduct(pname).getPrice();
            total+=(price*qty);
            ContentValues cvv=new ContentValues();
            cvv.put("pname",pname);
            cvv.put("orderid",orderno);
            cvv.put("qty",qty);
            db.insert("order_details",null,cvv);
        }
        cv.put("_id",orderno);
        cv.put("userid",userid);
        cv.put("status","Pending");
        cv.put("total",total);
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        cv.put("orderdate",sdf.format(new Date()));
        db.insert("orders",null,cv);
        emptycart(userid);
    }

    private Product findProduct (String pname)
    {
        List<Product> list = SplashScreen.plist;
        for(Product p :list){
            if(p.getPname().equals(pname))
                return p;
        }
        return null ;
    }

    public void addtocart(String userid,String pname,int qty)
    {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("userid",userid);
        cv.put("product",pname);
        cv.put("qty",qty);
        db.insert("cart",null,cv);
    }
    public Cursor getCartItems(String userid)
    {
        SQLiteDatabase db=getReadableDatabase();
        Cursor c=db.rawQuery("SELECT *FROM cart WHERE userid=? ",new String[]{userid});
        return c;
    }

    public void addCustomer(String cname,String city,String gender,String email,String phone,String pwd)
    {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("cname",cname);
        cv.put("city",city);
        cv.put("gender",gender);
        cv.put("email",email);
        cv.put("phone",phone);
        db.insert("customers",null,cv);
        ContentValues cv2=new ContentValues();
        cv2.put("userid",phone);
        cv2.put("pwd",pwd);
        cv2.put("role","customer");
        db.insert("users",null,cv2);
    }

    public Cursor validate(String userid,String pwd)
    {
        SQLiteDatabase db= getReadableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM users WHERE userid=? and pwd=?",new String[]{userid,pwd});
        return c;
    }

    public void updatePwd(String userid,String pwd)
    {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("pwd",pwd);
        db.update("user",cv,"userid=?",new String[]{userid});
    }

    public Cursor getCustomer(String userid)
    {
        SQLiteDatabase db= getReadableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM customers WHERE phong=?",new String[]{userid});
        return c;
    }

    public Cursor findcustomer(String userid)
    {
        SQLiteDatabase db= getReadableDatabase();
        Cursor c= db.rawQuery("SELECT * FROM customers WHERE phone=?",new String[]{userid});
        return c;
    }

    public Cursor findVendor(String userid)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM users WHERE userid=?",new String[]{userid});
        return c;
    }
}
