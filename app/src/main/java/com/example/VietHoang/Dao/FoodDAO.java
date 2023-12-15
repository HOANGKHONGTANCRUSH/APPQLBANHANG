package .Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import .Model.Food;
import .database.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class FoodDAO {

    private SQLiteDatabase db;

    public FoodDAO(Context context){
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    //getAllListFood
    public List<Food> getAllFood(){
        String sql = "SELECT * FROM FOOD";
        return getData(sql);
    }

    //get food theo type and status
    public List<Food> getAllFoodType(String type, String status){
        String sql = "SELECT * FROM FOOD WHERE FOOD_TYPE = ? AND FOOD_STATUS = ?";
        List<Food> list = getData(sql, type, status);
        return list;
    }

    public void insertFood(Food food) {
        ContentValues values = new ContentValues();
        values.put("FOOD_NAME", food.getFood_name());
        values.put("FOOD_PRICE", food.getFood_price());
        values.put("FOOD_TYPE", food.getFood_type());
        values.put("FOOD_STATUS", food.getFood_status());
        values.put("FOOD_IMAGE", food.getFood_image());
        db.insert("FOOD", null, values);
    }

    //update food
    public boolean updateFood(Food food){
        db.beginTransaction();
        long rows = 0;
        try {
            ContentValues values = new ContentValues();
            values.put("FOOD_NAME", food.getFood_name());
            values.put("FOOD_PRICE", food.getFood_price());
            values.put("FOOD_IMAGE", food.getFood_image());
            rows = db.update("FOOD", values, "FOOD_ID = ?", new String[]
                    {String.valueOf(food.getFood_id())});

            //Transaction: kiem tra neu insert bi loi se callback lai
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.d("update: ", e.getMessage());
        }finally {
            db.endTransaction();
        }

        return rows == 1;
    }

    public boolean updateStatusFood(Food food){
        db.beginTransaction();
        long rows = 0;
        try {
            ContentValues values = new ContentValues();
            values.put("FOOD_STATUS", food.getFood_status());
            rows = db.update("FOOD", values, "FOOD_ID = ?", new String[]
                    {String.valueOf(food.getFood_id())});

            //Transaction: kiem tra neu insert bi loi se callback lai
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.d("update: ", e.getMessage());
        }finally {
            db.endTransaction();
        }

        return rows == 1;
    }

    //getData
    private List<Food> getData(String sql, String...selectionArgs){
        List<Food> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        while (cursor.moveToNext()){
            Integer id = cursor.getInt(cursor.getColumnIndex("FOOD_ID"));
            Integer type = cursor.getInt(cursor.getColumnIndex("FOOD_TYPE"));
            Integer status = cursor.getInt(cursor.getColumnIndex("FOOD_STATUS"));
            String name = cursor.getString(cursor.getColumnIndex("FOOD_NAME"));
            Double price = cursor.getDouble(cursor.getColumnIndex("FOOD_PRICE"));
            byte[] image = cursor.getBlob(cursor.getColumnIndex("FOOD_IMAGE"));

            Food food = new Food(id, type, status, name, price, image);

            list.add(food);
        }
        return list;
    }

    public void xoaTatCaDuLieu() {
        db.beginTransaction();
        try {
            // Xóa tất cả dữ liệu trong bảng FOOD
            db.delete("FOOD", null, null);
            // Xóa tất cả dữ liệu trong bảng BOARD
            db.delete("BOARD", null, null);
            // Xóa tất cả dữ liệu trong bảng BILL
            db.delete("BILL", null, null);
            // Xóa tất cả dữ liệu trong bảng STAFF
            db.delete("STAFF", null, null);
            // Xóa tất cả dữ liệu trong bảng FOODBOARD
            db.delete("FOODBOARD", null, null);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("xoaTatCaDuLieu: ", e.getMessage());
        } finally {
            db.endTransaction();
        }
    }
}
