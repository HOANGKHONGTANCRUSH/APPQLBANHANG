package .activity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import .database.DbHelper;
import .Dao.FoodDAO;
import .R;
import .fragment.FragmentFood;
import .fragment.FragmentOption;
import .fragment.FragmentOrder;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import .fragment.FragmentTable;

public class MainActivity extends AppCompatActivity {

    public void myIntent() {
        Intent intent = new Intent(MainActivity.this, FoodBoardActivity.class);
        startActivityForResult(intent, 999);
    }

    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    FoodDAO foodDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        foodDAO = new FoodDAO(this);

        // Ánh xạ
        init();

        // Đổ fragment_table vào FragmentContainerView
        fragmentManager.beginTransaction().replace(R.id.fragmnet_context, FragmentTable.newInstance(null, null)).commit();

        // Bắt sự kiện bottomNavigation
        bottomNavigationOnclick();

        // Khởi tạo và đặt người nghe sự kiện cho nút xóa dữ liệu
        Button xoaAllDataButton = findViewById(R.id.XoaAllData);
        xoaAllDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi phương thức clearAllData khi nút được nhấn
                DbHelper dbHelper = DbHelper.getDbInstance(MainActivity.this);
                dbHelper.clearAllData();
                // Thông báo cho người dùng rằng dữ liệu đã được xóa (có thể sử dụng Toast hoặc AlertDialog)
                Toast.makeText(MainActivity.this, "Dữ liệu đã được xóa.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Ánh xạ
    private void init() {
        bottomNavigationView = findViewById(R.id.bottom_navi);
        fragmentManager = getSupportFragmentManager();
    }

    // Bắt sự kiện bottomNavigation
    public void bottomNavigationOnclick() {
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.nav_Fast_Food:
                    fragmentManager.beginTransaction().replace(R.id.fragmnet_context, FragmentTable.newInstance(null, null)).commit();
                    break;
                case R.id.nav_Water:
                    fragmentManager.beginTransaction().replace(R.id.fragmnet_context, FragmentFood.newInstance(null, null)).commit();
                    break;
                case R.id.nav_order:
                    fragmentManager.beginTransaction().replace(R.id.fragmnet_context, FragmentOrder.newInstance(null, null)).commit();
                    break;
                case R.id.nav_Combo:
                    fragmentManager.beginTransaction().replace(R.id.fragmnet_context, FragmentOption.newInstance(null, null)).commit();
                    break;
            }
            return true;
        });
    }
}
