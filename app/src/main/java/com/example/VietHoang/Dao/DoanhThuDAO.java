package .Dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import .database.DbHelper;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DoanhThuDAO {

    private SQLiteDatabase db;
    private Date date;
    private Calendar calendar;
    private int month;
    private int year;

    public DoanhThuDAO(Context context) {
        DbHelper dbHelper = DbHelper.getDbInstance(context);
        db = dbHelper.getWritableDatabase();

        date = new Date();

        calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
    }

    // Lấy doanh thu theo ngày hiện tại
    public double getDoanhThuDay() {
        String date1 = new SimpleDateFormat("yyyy/MM/dd").format(date) + " 00:00";
        String date2 = new SimpleDateFormat("yyyy/MM/dd").format(date) + " 23:59";
        return getDataDoanhThu(date1, date2);
    }

    // Lấy doanh thu theo ngày hôm qua
    public double getDoanhThuLastDay() {
        String date1 = new SimpleDateFormat("yyyy/MM/dd").format(date);
        String date2 = new SimpleDateFormat("yyyy/MM/dd").format(date);

        date1 = getYesterday(date1, "00:00");
        date2 = getYesterday(date2, "23:59");

        return getDataDoanhThu(date1, date2);
    }

    public String getYesterday(String day, String time) {
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.parse(day, formatters);

        LocalDate yesterday = localDate.plusDays(-1);
        return formatters.format(yesterday) + " " +time;
    }

    // Lấy doanh thu tháng hiện tại
    public double getDoanhThuMonth() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        if (month != 0){
            String date1 = year + "/" + (month + 1) + "/01 00:00";
            String date2 = year + "/" + (month + 1) + "/31 23:59";
            return getDataDoanhThu(date1, date2);
        }else {
            String date1 = (year - 1) + "/12/01 00:00";
            String date2 = (year - 2) + "/12/31 23:59";
            return getDataDoanhThu(date1, date2);
        }
    }

    // Lấy doanh thu tháng trước
    public double getDoanhThuLastMonth() {
        if (month != 0){
            String date1 = year + "/" + month + "/01 00:00";
            String date2 = year + "/" + month + "/31 23:59";
            return getDataDoanhThu(date1, date2);
        }else {
            String date1 = (year - 1) + "/12/01 00:00";
            String date2 = (year - 1) + "/12/31 23:59";
            return getDataDoanhThu(date1, date2);
        }
    }

    // Lấy doanh thu của năm hiện tại
    public double getDoanhThuYear() {
        String date1 = year + "/01/01 00:00";
        String date2 = year + "/12/31 23:59";
        return getDataDoanhThu(date1, date2);
    }

    // Lấy doanh thu của năm trước
    public double getDoanhThuLastYear(){
        String date1 = (year - 1) + "/01/01 00:00";
        String date2 = (year - 1) + "/12/31 23:59";
        return getDataDoanhThu(date1, date2);
    }

    public double getDataDoanhThu(String date1, String date2) {
        double sum = 0;

        Cursor cursor = db.rawQuery("SELECT SUM(BILL_PRICE) FROM BILL WHERE BILL_DATE >= ? AND BILL_DATE <= ?" , new String[]{date1, date2});
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

}
