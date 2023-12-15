package .Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import .Model.Board;
import .Model.FoodBoard;
import .database.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class FoodBoardDAO {

    private SQLiteDatabase db;
    private DbHelper dbHelper;
    private List<Board> listboard;
    public FoodBoardDAO(Context context) {
        dbHelper = DbHelper.getDbInstance(context);
        db = dbHelper.getWritableDatabase();
    }

    // Lấy ra danh sách thực ăn mà bàn đó gọi
    public List<FoodBoard> getFoodByBoardId(int boardId) {
        List<FoodBoard> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(
                "SELECT F.FOOD_ID, F.FOOD_NAME, F.FOOD_PRICE, FB.FOODBOARD_ID, COUNT(F.FOOD_ID), F.FOOD_IMAGE " +
                        "FROM FOOD F INNER JOIN FOODBOARD FB " +
                        "ON F.FOOD_ID = FB.FOOD_ID " +
                        "WHERE FB.BOARD_ID = ? " +
                        "GROUP BY F.FOOD_ID", new String[]{String.valueOf(boardId)});
        try{
            if (cursor.moveToFirst()){
                do {
                    int foodId = cursor.getInt(0);
                    String foodName = cursor.getString(1);
                    double foodPrice = cursor.getDouble(2);
                    int foodBoardId = cursor.getInt(3);
                    int foodCount = cursor.getInt(4);
                    byte[] foodImage = cursor.getBlob(5);

                    FoodBoard foodBoard = new FoodBoard(foodName, foodBoardId, foodId, foodCount, foodPrice, foodImage);
                    list.add(foodBoard);
                }while (cursor.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
        return list;
    }

    // Tính tổng tiền của một bàn
    public double getPriceBoard(int boardId){
        double sum = 0;
        Cursor cursor = db.rawQuery("" +
                "SELECT SUM(F.FOOD_PRICE) FROM " +
                "BOARD B INNER JOIN FOODBOARD FB " +
                "ON B.BOARD_ID = FB.BOARD_ID " +
                "INNER JOIN FOOD F " +
                "ON FB.FOOD_ID = F.FOOD_ID " +
                "WHERE B.BOARD_ID = ?", new String[]{String.valueOf(boardId)});
        try{
            cursor.moveToFirst();
            sum = cursor.getInt(0);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
        return sum;
    }

    // Lấy những bảng có status > 0
    public List<FoodBoard> getFoodBoardStatus() {
        List<FoodBoard> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT FB.FOODBOARD_ID, F.FOOD_NAME, B.BOARD_NUMBER, FB.FOODBOARD_STATUS FROM FOOD F INNER JOIN FOODBOARD FB ON F.FOOD_ID = FB.FOOD_ID INNER JOIN BOARD B ON FB.BOARD_ID = B.BOARD_ID WHERE FB.FOODBOARD_STATUS > 0 ORDER BY FB.FOODBOARD_STATUS ASC", null);
        try{
            if (cursor.moveToFirst()){
                do {
                    int foodBoardId = cursor.getInt(0);
                    String foodName = cursor.getString(1);
                    int boardNumber = cursor.getInt(2);
                    int foodBoardStatus = cursor.getInt(3);

                    FoodBoard foodBoard = new FoodBoard(foodBoardId, foodName, boardNumber, foodBoardStatus);
                    list.add(foodBoard);
                }while (cursor.moveToNext());
            }
        }catch (Exception e){
            Log.e("getFoodBoardStatus()", e.getMessage());
        }finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
        return list;
    }

    public void updateStatus(int id, int status) {
        ContentValues values = new ContentValues();
        values.put("FOODBOARD_STATUS", status);
        db.update("FOODBOARD", values, "FOODBOARD_ID = ?", new String[]{String.valueOf(id)});
    }

    public void deleteStatus(int status){
        ContentValues values = new ContentValues();
        values.put("FOODBOARD_STATUS", 0);
        db.update("FOODBOARD", values, "FOODBOARD_STATUS = ?", new String[]{String.valueOf(status)});
    }

    public void insertFoodBoard(FoodBoard foodBoard){
        ContentValues values = new ContentValues();
        values.put("FOOD_ID", foodBoard.getFoodId());
        values.put("BOARD_ID", foodBoard.getBoardId());
        values.put("FOODBOARD_STATUS", 1);
        db.insert("FOODBOARD", null, values);
    }

    public void deleteFoodBoard(int FoodBoardId) {
        db.delete("FOODBOARD", "FOODBOARD_ID = ?", new String[]{String.valueOf(FoodBoardId)});
    }
}
