package .Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import .Model.Board;
import .database.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class BoardDAO {
    private SQLiteDatabase db;

    public BoardDAO(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }
    private List<Board> listboard;
    // get all
    public List<Board> getAllData(){
        String sql = "SELECT * FROM BOARD";
        return getData(sql);
    }

    // get data theo id
    public Board getId(String id){
        String sql = "SELECT * FROM BOARD WHERE BOARD_ID = ?";
        List<Board> list = getData(sql,id);
        return list.get(0);
    }

    public boolean updateStutusBoard(Board board){
        db.beginTransaction();
        long rows = 0;
        try {
            ContentValues values = new ContentValues();
            values.put("BOARD_STATUS", board.getBoard_status());
            rows = db.update("BOARD", values, "BOARD_ID = ?", new String[]
                    {String.valueOf(board.getBoard_id())});

            //Transaction: kiem tra neu insert bi loi se callback lai
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.d("update: ", e.getMessage());
        }finally {
            db.endTransaction();
        }

        return rows == 1;
    }
    public boolean updatenameBoard(Board board){
        db.beginTransaction();
        long rows = 0;
        try {
            ContentValues values = new ContentValues();
            values.put("BOARD_NUMBER", board.getBoard_number());
            rows = db.update("BOARD", values, "BOARD_ID = ?", new String[]
                    {String.valueOf(board.getBoard_id())});

            //Transaction: kiem tra neu insert bi loi se callback lai
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.d("update: ", e.getMessage());
        }finally {
            db.endTransaction();
        }

        return rows == 1;
    }

    // get data voi cac tham so
    private List<Board> getData(String sql, String...selectionArgs){
        List<Board> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql,selectionArgs);
        while (cursor.moveToNext()){
            Board obj = new Board();
            obj.setBoard_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("BOARD_ID"))));
            obj.setBoard_number(Integer.parseInt(cursor.getString(cursor.getColumnIndex("BOARD_NUMBER"))));

            list.add(obj);
        }
        return list;
    }

    public List<Board> getAllBoard(){
        List<Board> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("" +
                "SELECT B.BOARD_ID ,B.BOARD_NUMBER ,SUM(F.FOOD_PRICE) FROM BOARD B " +

                "LEFT JOIN FOODBOARD FB " +
                "ON B.BOARD_ID = FB.BOARD_ID " +
                "LEFT JOIN FOOD F " +
                "ON FB.FOOD_ID = F.FOOD_ID " +
                "WHERE B.BOARD_STATUS = 1 " +
                "GROUP BY B.BOARD_ID"+
                " ORDER BY B.BOARD_ID DESC,B.BOARD_ID ASC",null);



        try{
            if (cursor.moveToFirst()){
                while (!cursor.isAfterLast()){
                    int id = cursor.getInt(0);
                    int number = cursor.getInt(1);
                    double price = cursor.getDouble(2);

                    Board board = new Board(id, number, price);
                    list.add(board);
                    cursor.moveToNext();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
        Log.e("List board", list.size()+"");
        return list;
    }

//    public int getNumberBoard(){
//
//    }

    // Lấy id lớn nhất từ bảng board
    public int getMaxIdBoard() {
        int boardId = 0;
        Cursor cursor = db.rawQuery("SELECT MAX(BOARD_ID) FROM BOARD", null);
        try{
            cursor.moveToFirst();
            boardId = cursor.getInt(0);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
        return boardId;
    }
    public int sapxep() {
        int boardId = 0;
        Cursor cursor = db.rawQuery("SELECT BOARD_ID FROM BOARD"+
                " ORDER BY BOARD_ID DESC ,BOARD_ID ASC",null);
        try{
            cursor.moveToFirst();
            boardId = cursor.getInt(0);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
        return boardId;
    }

    public void updateBoard(int boardId, int status) {
        ContentValues values = new ContentValues();
        values.put("BOARD_STATUS", status);
        db.update("BOARD", values, "BOARD_ID = ?", new String[]{String.valueOf(boardId)});
    }
    public  void updatename(int board_number,int board_id){
        ContentValues values = new ContentValues();
        values.put("BOARD_NUMBER",board_number);
        db.update("BOARD",values, "BOARD_ID = ?",new String[]{String.valueOf(board_id)});
    }
    public void insertBoard(Board board) {
        ContentValues values = new ContentValues();
        values.put("BOARD_NUMBER", board.getBoard_number());
        values.put("BOARD_STATUS", board.getBoard_status());
        db.insert("BOARD", null, values);
    }
    public void delete(int board_id) {
        db.delete("BOARD", "BOARD_ID = ?", new String[]{String.valueOf(board_id)});
    }

    // Kiểm tra lỗi trùng tên bàn
    public boolean checkBoardAgain(int boardNumber){
        Cursor cursor = db.rawQuery("SELECT BOARD_NUMBER FROM BOARD WHERE BOARD_NUMBER = ? AND BOARD_STATUS = 1",new String[]{String.valueOf(boardNumber)});
        try {
            cursor.moveToFirst();
            if (cursor.getCount() > 0){
                return true;
            }
        }catch (Exception e){
            Log.e("chekBoardAgain()", e.getMessage());
        }finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
        return false;
    }

}
