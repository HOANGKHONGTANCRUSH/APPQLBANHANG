package .activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import .Dao.StaffDAO;
import .Model.Staff;
import .R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DecimalFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddStaffActivity extends AppCompatActivity {

    private EditText edtName, edtSDT, edtBirth, edtRole, edtWorkCalendar, edtPriceHour, edtTimeWork;
    private Button btnAdd, btnCancel;
    private TextView tvPrice;
    private CircleImageView imgStaff;
    private StaffDAO staffDAO;
    private int staffId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);

        init();

        staffDAO = new StaffDAO(this);

        String action = getIntent().getAction();
        if (action.equals("EditStaff")){
            Staff staff = getIntent().getParcelableExtra("staff");
            Bitmap bitmap = BitmapFactory.decodeByteArray(staff.getStaff_image(), 0, staff.getStaff_image().length);
            staffId = staff.getStaff_ID();
            imgStaff.setImageBitmap(bitmap);
            edtName.setText(staff.getStaff_Name());
            edtSDT.setText(staff.getStaff_SDT());
            edtBirth.setText(staff.getStaff_birth());
            edtRole.setText(staff.getStaff_Role());
            edtWorkCalendar.setText(staff.getStaff_Calender());
            edtPriceHour.setText(String.valueOf(staff.getStaff_Money()));
            edtTimeWork.setText(String.valueOf(staff.getStaff_Time()));

            double price = staff.getStaff_Time() * staff.getStaff_Money();
            DecimalFormat formater = new DecimalFormat("###,###,### VND");
            tvPrice.setText(formater.format(price));
        }

        imgStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hàm Lấy ảnh từ thiết bị
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                Intent image = new Intent(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
                Intent chooser = Intent.createChooser(intent, "CHOOSE");
                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{image});
                activityResultLauncher.launch(chooser);
            }
        });

        // Nếu hoạt động là sửa nhân viên thì đổi chữ của btnAdd
        if (action.equals("EditStaff")){
            btnAdd.setText("Edit");
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = checkNullFrom(); // Hàm kiểm tra lỗi trống form
                if (!check){ // Nếu check = fale thì hiện thông báo
                    Toast.makeText(AddStaffActivity.this, "Không để trống ô nhập", Toast.LENGTH_SHORT).show();
                    return;
                }

                String name = edtName.getText().toString();
                String sdt = edtSDT.getText().toString();
                String birth = edtBirth.getText().toString();
                String role = edtRole.getText().toString();
                String calendar = edtWorkCalendar.getText().toString();
                double priceHour = Double.parseDouble(edtPriceHour.getText().toString());
                int timeWork = Integer.parseInt(edtTimeWork.getText().toString());
                byte[] imageStaff = imageToByte(imgStaff);

                if (action.equals("AddStaff")){ // nếu acction = addstaff
                    Staff staff = new Staff(timeWork, name, sdt, birth, calendar, role, priceHour, imageStaff);
                    staffDAO.insertStaff(staff);
                    Toast.makeText(AddStaffActivity.this, "Thêm nhân viên thành công", Toast.LENGTH_SHORT).show();
                }else { // sửa nhân viên
                    Staff staff = new Staff(staffId, timeWork, name, sdt, birth, calendar, role, priceHour, imageStaff);
                    staffDAO.updateStaff(staff);
                    Toast.makeText(AddStaffActivity.this, "Sửa nhân viên thành công", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

    }

    // Kiểm tra lỗi trống from
    private boolean checkNullFrom (){
        if (edtName.getText().toString().isEmpty() || edtSDT.getText().toString().isEmpty() || edtBirth.getText().toString().isEmpty() ||
                edtRole.getText().toString().isEmpty() || edtWorkCalendar.getText().toString().isEmpty() || edtTimeWork.getText().toString().isEmpty() || edtPriceHour.toString().isEmpty()){
            return false;
        }
        return true;
    }

    private void init() {
        edtName = findViewById(R.id.edtNameStaff);
        edtSDT = findViewById(R.id.edtSdtStaff);
        edtBirth = findViewById(R.id.edtBirthStaff);
        edtRole = findViewById(R.id.edtRoleStaff);
        edtWorkCalendar = findViewById(R.id.edtWorkCalendar);
        edtPriceHour = findViewById(R.id.edtPriceHour);
        edtTimeWork = findViewById(R.id.edtTimeWork);
        btnAdd = findViewById(R.id.btnAddStaff);
        btnCancel = findViewById(R.id.btnCancelStaff);
        imgStaff = findViewById(R.id.imgStaff);
        tvPrice = findViewById(R.id.tvPriceStaff);
    }

    public byte[] imageToByte(ImageView iv){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) iv.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getData() != null){
                        Intent data = result.getData();
                        if (data.getExtras() != null){
                            Bundle bundle = data.getExtras();
                            Bitmap imageBitmap = (Bitmap) bundle.get("data");
                            imgStaff.setImageBitmap(imageBitmap);
                        }else {
                            Uri uri = data.getData();
                            try {
                                InputStream inputStream = getContentResolver().openInputStream(uri);
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inPreferredConfig = Bitmap.Config.RGB_565;
                                options.inSampleSize = 5;
                                Bitmap imageBitmap = BitmapFactory.decodeStream(inputStream, null, options);
                                imgStaff.setImageBitmap(imageBitmap);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
    );
}