package .activity;

import static .utilities.Constants.BOARD_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import .Dao.BillDao;
import .Dao.DoanhThuDAO;
import .Dao.FoodBoardDAO;
import .Model.Food;
import .Model.FoodBoard;
import .R;
import .adapter.InfoAdapter;

import java.util.List;

public class InfoHoaDon extends AppCompatActivity {
    private FoodBoardDAO foodBoardDAO;
    private TextView tv_number_board, tv_price_bill,tv_datebill;
    private List<Food> listFood;
    private DoanhThuDAO doanhThuDAO;
    private BillDao billDao;
    private List<FoodBoard> listFoodBoard;
    RecyclerView Recy_info;
    private InfoAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_hoa_don);
        tv_number_board = findViewById(R.id.tv_number_board);
        tv_datebill= findViewById(R.id.tv_datebill);
        tv_price_bill= findViewById(R.id.tv_price_bill);
        Recy_info= findViewById(R.id.Recy_info);
        foodBoardDAO = new FoodBoardDAO(this);

        tv_number_board.setText(getIntent().getStringExtra("numberBoard"));
        tv_price_bill.setText(getIntent().getStringExtra("billPrice"));
        tv_datebill.setText(getIntent().getStringExtra("billDate"));

        listFoodBoard = foodBoardDAO.getFoodByBoardId(BOARD_ID);
        adapter = new InfoAdapter(InfoHoaDon.this, listFoodBoard);
        Recy_info.setLayoutManager(new LinearLayoutManager(this));
        Recy_info.setAdapter(adapter);

    }
}