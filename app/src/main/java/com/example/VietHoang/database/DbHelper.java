package .database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;


import java.io.ByteArrayOutputStream;

public class DbHelper extends SQLiteOpenHelper{

    private static DbHelper dbInstance;
    private Context context;
    private SQLiteStatement statement;
    private Bitmap bitmap;

    public static synchronized DbHelper getDbInstance(Context ctx){
        if (dbInstance == null) dbInstance = new DbHelper(ctx);
        return dbInstance;
    }

    public DbHelper(@Nullable Context context) {
        super(context, "MiniBar", null, 1);
        this.context = context;
    }
    public void clearAllData() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM FOOD");
        db.execSQL("DELETE FROM BOARD");
        db.execSQL("DELETE FROM BILL");
        db.execSQL("DELETE FROM STAFF");
        db.execSQL("DELETE FROM FOODBOARD");
        db.close();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng FOOD
        db.execSQL("CREATE TABLE IF NOT EXISTS FOOD(" +
                "FOOD_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "FOOD_NAME TEXT," +
                "FOOD_PRICE DOUBLE," +
                "FOOD_TYPE INTEGER," +
                "FOOD_STATUS INTEGER," +
                "FOOD_IMAGE BLOB)");

        // Tạo bảng BOARD
        db.execSQL("CREATE TABLE IF NOT EXISTS BOARD(" +
                "BOARD_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "BOARD_NUMBER INTEGER," +
                "BOARD_STATUS INTEGER)");

        // Tạo bảng BILL
        db.execSQL("CREATE TABLE IF NOT EXISTS BILL(" +
                "BILL_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "BILL_DATE TEXT," +
                "BILL_PRICE DOUBLE,"+
                "BOARD_ID INTEGER REFERENCES BOARD(BOARD_ID))");

        // Tạo bảng FOODBOARD
        db.execSQL("CREATE TABLE IF NOT EXISTS FOODBOARD(" +
                "FOODBOARD_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "FOOD_ID INTEGER REFERENCES FOOD(FOOD_ID)," +
                "BOARD_ID INTEGER REFERENCES BOARD(BOARD_ID)," +
                "FOODBOARD_STATUS INTEGER)");

        // Tạo bảng STAFF
        db.execSQL("CREATE TABLE IF NOT EXISTS STAFF(" +
                "STAFF_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "STAFF_NAME TEXT," +
                "STAFF_SDT TEXT," +
                "STAFF_BIRTH TEXT," +
                "STAFF_MONEY DOUBLE," +
                "STAFF_CALENDAR TEXT," +
                "STAFF_TIME INTEGER," +
                "STAFF_PASSWORD TEXT," +
                "STAFF_ROLE TEXT," +
                "STAFF_IMAGE BLOB)");
    }

    public void insertDataFood(String foodName, double foodPrice, int foodType, int foodStatus, int foodImage, SQLiteDatabase db) {
        statement = db.compileStatement("INSERT INTO FOOD (FOOD_NAME, FOOD_PRICE, FOOD_TYPE, FOOD_STATUS, FOOD_IMAGE) " +
                "VALUES ('"+foodName+"', "+foodPrice+", "+foodType+", "+foodStatus+", ?)");

        //truyền ảnh vào database
        bitmap = BitmapFactory.decodeResource(context.getResources(), foodImage);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        byte[] data = stream.toByteArray();

        statement.bindBlob(1, data);
        statement.execute();
    }

    public void insertDataStaff(String staffName, String staffSdt, String staffBirth, double staffMoney, String staffCalendar, int staffTime, String staffPassword, String staffRole, int staffImage, SQLiteDatabase db) {
        statement = db.compileStatement("INSERT INTO STAFF (STAFF_NAME, STAFF_SDT, STAFF_BIRTH, STAFF_MONEY, STAFF_CALENDAR, STAFF_TIME, STAFF_PASSWORD, STAFF_ROLE, STAFF_IMAGE) VALUES " +
                "('"+staffName+"', '"+staffSdt+"', '"+staffBirth+"', "+staffMoney+", '"+staffCalendar+"', "+staffTime+", '"+staffPassword+"', '"+staffRole+"', ?)");

        //Truyền ảnh vào database
        bitmap = BitmapFactory.decodeResource(context.getResources(), staffImage);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        byte[] data = stream.toByteArray();

        statement.bindBlob(1, data);
        statement.execute();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion){
            db.execSQL("DROP TABLE IF EXISTS FOOD");
            db.execSQL("DROP TABLE IF EXISTS BOARD");
            db.execSQL("DROP TABLE IF EXISTS BILL");
            db.execSQL("DROP TABLE IF EXISTS STAFF");
            db.execSQL("DROP TABLE IF EXISTS FOODBOARD");
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
}
