package .activity;

import static .utilities.Constants.BOARD_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import .Dao.BillDao;
import .Dao.BoardDAO;
import .Dao.FoodBoardDAO;
import .Dao.FoodDAO;
import .Model.Bill;
import .Model.Food;
import .Model.FoodBoard;
import .R;
import .adapter.FoodAdapter;
import .adapter.FoodNumberAdapter;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FoodBoardActivity extends AppCompatActivity {

    private EditText edTra;
    private TextView tvTra, tvPrice;
    private Button btThanhToan, btSave;
    private RecyclerView recyFood, recyFood2;
    private ChipNavigationBar chipNavigationBar;
    private List<Food> listFood;
    private List<FoodBoard> listFoodBoard;
    private FoodDAO foodDAO;
    private BoardDAO boardDAO;
    private BillDao billDao;
    private FoodBoardDAO foodBoardDAO;
    private FoodAdapter adapter;
    private FoodNumberAdapter adapter2;
    private DecimalFormat formater;
    private SearchView searchFood;
    private double tongTien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_board);

        innit();

        // Đổ dữ liệu vào recyclerView
        foodDAO = new FoodDAO(this);
        listFood = foodDAO.getAllFoodType("0", "1");
        adapter = new FoodAdapter(this, listFood);
        recyFood.setLayoutManager(new LinearLayoutManager(this));
        recyFood.setAdapter(adapter);

        // Đổ dữ liệu vào recyclerView2
        foodBoardDAO = new FoodBoardDAO(this);
        listFoodBoard = foodBoardDAO.getFoodByBoardId(BOARD_ID);
        adapter2 = new FoodNumberAdapter(FoodBoardActivity.this, listFoodBoard);
        recyFood2.setLayoutManager(new LinearLayoutManager(this));
        recyFood2.setAdapter(adapter2);

        // Hiện Tổng tiền của 1 bàn lên textView
        formater = new DecimalFormat("###,###,### VND");
        tongTien = foodBoardDAO.getPriceBoard(BOARD_ID);
        tvPrice.setText(formater.format(tongTien));

        // Hiện tiền thối lại cho khách hàng
        caculatePrice();

        // Bắt sự kiện nút lưu
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentResul();
            }
        });

        // Bắt sự kiện của button thanh toán
        btThanhToanOnlick();

        // Bắt sự kiện chipnavigationBar
        chipnavOnclick();

        // Baắt sự kiện khi thay đổi nội dung của edit text
        edTraTextChanged();

        // Bắt sự kiện search food
        searchFoodEvent();

    }

    private void searchFoodEvent() {
        searchFood.setQueryHint("Search food...");
        searchFood.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void edTraTextChanged() {
        edTra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Tính tiền thối lại cho khách hàng
                caculatePrice();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void caculatePrice() {
        double tienNhan = 0;
        try {
            tienNhan = Double.parseDouble(edTra.getText().toString());
        }catch (Exception e){

        }
        double tienTra = tienNhan - tongTien;
        tvTra.setText(formater.format(tienTra));
    }

    private void chipnavOnclick() {
        chipNavigationBar.setItemSelected(R.id.nav_Fast_Food, true);
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.nav_Fast_Food:
                        listFood.clear();
                        listFood.addAll(foodDAO.getAllFoodType("0", "1"));
                        adapter.notifyDataSetChanged();
                        break;
                    case R.id.nav_Water:
                        listFood.clear();
                        listFood.addAll(foodDAO.getAllFoodType("1", "1"));
                        adapter.notifyDataSetChanged();
                        break;
                    case R.id.nav_Combo:
                        listFood.clear();
                        listFood.addAll(foodDAO.getAllFoodType("2", "1"));
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        });
    }

    private void btThanhToanOnlick() {
        btThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardDAO = new BoardDAO(FoodBoardActivity.this);
                billDao = new BillDao(FoodBoardActivity.this);

                // update status của BOARD_STATUS từ 1 thành 0
                boardDAO.updateBoard(BOARD_ID, 0);

                // Lấy thời gian hiện tại
                Date day = new Date();
                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(day);

                Bill bill = new Bill(BOARD_ID, date, tongTien);
                billDao.insertBill(bill);

                Toast.makeText(FoodBoardActivity.this, "Check out successfully!", Toast.LENGTH_SHORT).show();

                intentResul();

            }
        });
    }

    private void innit() {
        edTra = findViewById(R.id.ed_tra);
        tvTra = findViewById(R.id.tv_tra);
        tvPrice = findViewById(R.id.tv_price);
        btThanhToan = findViewById(R.id.bt_thanhtoan);
        btSave = findViewById(R.id.bt_save);
        recyFood = findViewById(R.id.recy_food);
        recyFood2 = findViewById(R.id.recy_food2);
        searchFood = findViewById(R.id.searchView);
        chipNavigationBar = findViewById(R.id.chipnavibar_foodboard);
    }

    public void capNhatRecyFood2() {
        // Load lại dữ liệu listFoodBoard
        listFoodBoard.clear();
        listFoodBoard.addAll(foodBoardDAO.getFoodByBoardId(BOARD_ID));
        adapter2.notifyDataSetChanged();

        // Hiện Tổng tiền của 1 bàn lên textView
        formater = new DecimalFormat("###,###,### VND");
        tongTien = foodBoardDAO.getPriceBoard(BOARD_ID);
        tvPrice.setText(formater.format(tongTien));
        // Hiện số tiền thối của khách hàng
        caculatePrice();
    }

    // Intent về Main Activity
    private void intentResul(){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intentResul();
    }
}