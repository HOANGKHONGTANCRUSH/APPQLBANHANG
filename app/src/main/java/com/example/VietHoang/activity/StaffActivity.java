package .activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import .Dao.StaffDAO;
import .Model.Staff;
import .R;
import .adapter.StaffAdapter;

import java.util.ArrayList;
import java.util.List;

public class StaffActivity extends AppCompatActivity {

    RecyclerView rvStaff;
    StaffDAO dao;
    List<Staff> list = new ArrayList<>();
    StaffAdapter adapter;
    ImageView ivAddStaff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);

        rvStaff = findViewById(R.id.rv_Staff);
        ivAddStaff = findViewById(R.id.ivAddStaff);

        dao = new StaffDAO(this);
        list = dao.getAllStaff();

        adapter = new StaffAdapter(this, (ArrayList<Staff>) list, this);
        rvStaff.setLayoutManager(new LinearLayoutManager(this));
        rvStaff.setAdapter(adapter);

        ivAddStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StaffActivity.this, AddStaffActivity.class);
                intent.setAction("AddStaff");
                activityResultLauncher.launch(intent);
            }
        });
    }

    public ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getData() != null){
                        list.clear();
                        list.addAll(dao.getAllStaff());
                        adapter.notifyDataSetChanged();
                    }
                }
            }
    );
}