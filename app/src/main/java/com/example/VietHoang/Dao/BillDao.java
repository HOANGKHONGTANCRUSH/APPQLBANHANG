package .Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import .Model.Bill;
import .database.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class BillDao {
    private SQLiteDatabase db;

    public BillDao(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }
    public List<Bill> getAllData(){
        String sql = "SELECT * FROM BILL";
        return getData(sql);
    }

    // get data theo id
    public Bill getId(String id){
        String sql = "SELECT * FROM BILL WHERE BILL_ID = ?";
        List<Bill> list = getData(sql,id);
        return list.get(0);
    }

    // get data voi cac tham so
    private List<Bill> getData(String sql, String...selectionArgs){
        List<Bill> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql,selectionArgs);
        while (cursor.moveToNext()){
            Bill obj = new Bill();
            obj.setBoard_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("BOARD_ID"))));
            obj.setBill_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("BILL_ID"))));
            obj.setBill_price(Double.parseDouble(cursor.getString(cursor.getColumnIndex("BILL_PRICE"))));
            obj.setBill_date(String.valueOf(cursor.getString(cursor.getColumnIndex("BILL_DATE"))));

            list.add(obj);
        }
        return list;
    }
    public List<Bill> getAllBill(){
        List<Bill> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("" +
                "SELECT B.BILL_ID, FB.BOARD_NUMBER, B.BILL_PRICE, B.BILL_DATE, B.BOARD_ID FROM BILL B " +
                "INNER JOIN BOARD FB " +
                "ON B.BOARD_ID = FB.BOARD_ID " +
                "ORDER BY B.BILL_ID DESC",null);
        try{
            if (cursor.moveToFirst()){
                while (!cursor.isAfterLast()){
                    int id = cursor.getInt(0);
                    int number = cursor.getInt(1);
                    double price = cursor.getDouble(2);
                    String date = cursor.getString(3);
                    int boardId = cursor.getInt(4);

                    Bill bill = new Bill(id, number, date, price, boardId);
                    list.add(bill);
                    cursor.moveToNext();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
        Log.e("List Bill", list.size()+"");
        return list;
    }
    public double getdanhthuboard() {
        double sum = 0;

        Cursor cursor = db.rawQuery("SELECT BILL_PRICE FROM BILL" , null);
        try {
            if (cursor.moveToFirst()){
                sum = cursor.getDouble(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
        return sum;
    }
    public String getdate(){
        String sum1 = null;
        Cursor cursor = db.rawQuery("SELECT BILL_DATE FROM BILL" , null);
        try {
            if (cursor.moveToFirst()){
                sum1 = cursor.getString(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
        return sum1;
    }
    public void insertBill(Bill bill){
        ContentValues values = new ContentValues();
        values.put("BILL_DATE", bill.getBill_date());
        values.put("BILL_PRICE", bill.getBill_price());
        values.put("BOARD_ID", bill.getBoard_id());
        db.insert("BILL", null, values);
    }
}
