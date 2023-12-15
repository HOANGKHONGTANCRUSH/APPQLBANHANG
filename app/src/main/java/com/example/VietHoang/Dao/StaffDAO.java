package .Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import .Model.Staff;
import .database.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class StaffDAO {

    private SQLiteDatabase db;

    public StaffDAO(Context context){
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public List<Staff> getAllStaff(){
        String sql = "SELECT * FROM STAFF";
        return getData(sql);
    }

    public void insertStaff(Staff staff){
        ContentValues values = new ContentValues();
        values.put("STAFF_NAME", staff.getStaff_Name());
        values.put("STAFF_SDT", staff.getStaff_SDT());
        values.put("STAFF_BIRTH", staff.getStaff_birth());
        values.put("STAFF_MONEY", staff.getStaff_Money());
        values.put("STAFF_CALENDAR", staff.getStaff_Calender());
        values.put("STAFF_TIME", staff.getStaff_Time());
        values.put("STAFF_ROLE", staff.getStaff_Role());
        values.put("STAFF_PASSWORD", "1");
        values.put("STAFF_IMAGE", staff.getStaff_image());

        db.insert("STAFF", null, values);
    }

    public boolean updateStaff(Staff staff){
        Log.e("Staff_id", ""+staff.getStaff_ID());
        db.beginTransaction();
        long rows = 0;
        try {
            ContentValues values = new ContentValues();
            values.put("STAFF_NAME", staff.getStaff_Name());
            values.put("STAFF_SDT", staff.getStaff_SDT());
            values.put("STAFF_BIRTH", staff.getStaff_birth());
            values.put("STAFF_MONEY", staff.getStaff_Money());
            values.put("STAFF_CALENDAR", staff.getStaff_Calender());
            values.put("STAFF_TIME", staff.getStaff_Time());
            values.put("STAFF_ROLE", staff.getStaff_Role());
            values.put("STAFF_IMAGE", staff.getStaff_image());
            rows = db.update("STAFF", values, "STAFF_ID = ?", new String[]
                    {String.valueOf(staff.getStaff_ID())});

            //Transaction: kiem tra neu insert bi loi se callback lai
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.d("update: ", e.getMessage());
        }finally {
            db.endTransaction();
        }
        return rows == 1;
    }

    // Kiểm tra password nhập vào có đúng hay ko
    public boolean checkPassword(String role, String password) {
        Cursor cursor = db.rawQuery("SELECT STAFF_ID FROM STAFF WHERE STAFF_ROLE = ? AND STAFF_PASSWORD = ?", new String[]{role, password});
        try {
            if (cursor.getCount() > 0) return true;
        }catch (Exception e){
            Log.e("checkPassword", e.getMessage());
        }finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }

        String queryAllStaff = "SELECT * FROM STAFF";
        List<Staff> allStaff = getData(queryAllStaff);
        for (Staff staff : allStaff) {
            System.out.println("Staff password: " + staff.getStaff_Password());
            System.out.println("Staff Name: " + staff.getStaff_Name());
        }
        return false;
    }

    public void changePassword(String role, String password) {
        ContentValues values = new ContentValues();
        values.put("STAFF_PASSWORD", password);
        db.update("STAFF", values, "STAFF_ROLE = ?", new String[]{role});
    }

    private List<Staff> getData(String sql, String...selectionArgs){
        List<Staff> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        while (cursor.moveToNext()){
            Integer id = cursor.getInt(cursor.getColumnIndex("STAFF_ID"));
            String name = cursor.getString(cursor.getColumnIndex("STAFF_NAME"));
            String phone = cursor.getString(cursor.getColumnIndex("STAFF_SDT"));
            String dob = cursor.getString(cursor.getColumnIndex("STAFF_BIRTH"));
            Double money = cursor.getDouble(cursor.getColumnIndex("STAFF_MONEY"));
            String calender = cursor.getString(cursor.getColumnIndex("STAFF_CALENDAR"));
            Integer time = cursor.getInt(cursor.getColumnIndex("STAFF_TIME"));
            String password = cursor.getString(cursor.getColumnIndex("STAFF_PASSWORD"));
            String role = cursor.getString(cursor.getColumnIndex("STAFF_ROLE"));
            byte[] image = cursor.getBlob(cursor.getColumnIndex("STAFF_IMAGE"));

            Staff staff = new Staff(id, time, role, name, phone, dob, calender, password, money, image);

            list.add(staff);
        }
        return list;
    }

    public void deleteStaff(int staffID) {
        db.delete("STAFF", "STAFF_ID = ?", new String[]{String.valueOf(staffID)});
    }
}
