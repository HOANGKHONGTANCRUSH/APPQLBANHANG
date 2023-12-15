package .adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import .Dao.StaffDAO;
import .Model.Staff;
import .R;
import .activity.AddStaffActivity;
import .activity.StaffActivity;

import java.util.ArrayList;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder>{

    private static final int REQUEST_CODE_CALL_PHONE = 555;

    Context context;
    ArrayList<Staff> listStaff;
    StaffDAO staffDAO;
    Activity activity;

    public StaffAdapter(Context context, ArrayList<Staff> listStaff, Activity activity){
        this.context = context;
        this.listStaff = listStaff;
        staffDAO = new StaffDAO(context);
        this.activity = activity;
    }

    @NonNull
    @Override
    public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_staff, null);
        StaffViewHolder viewHolder = new StaffViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Staff staff = listStaff.get(position);
        holder.tvNameStaff.setText(staff.getStaff_Name());
        holder.tvPositionStaff.setText(staff.getStaff_Role());

        holder.btnCallStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPermissionAndCall(staff.getStaff_SDT());
            }
        });

        Bitmap bitmap = BitmapFactory.decodeByteArray(staff.getStaff_image(), 0, staff.getStaff_image().length);
        holder.imgStaff.setImageBitmap(bitmap);

        holder.main.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Delete Staff");
                builder.setMessage("Do you want delete staff?");

                builder.setPositiveButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });

                builder.setNegativeButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int id = staff.getStaff_ID();

                        staffDAO.deleteStaff(id);

                        listStaff.clear();
                        listStaff.addAll(staffDAO.getAllStaff());
                        notifyDataSetChanged();
                    }
                });
                builder.show();
                return false;
            }
        });

        holder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddStaffActivity.class);
                intent.putExtra("staff", (Parcelable) listStaff.get(position));
                intent.setAction("EditStaff");
                ((StaffActivity)context).activityResultLauncher.launch(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listStaff.size();
    }

    public class StaffViewHolder extends RecyclerView.ViewHolder{

        TextView tvNameStaff, tvPositionStaff;
        ImageView imgStaff;
        AppCompatButton btnCallStaff;
        RelativeLayout main;

        public StaffViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameStaff = itemView.findViewById(R.id.tv_name_staff);
            tvPositionStaff = itemView.findViewById(R.id.tv_position);
            imgStaff = itemView.findViewById(R.id.iv_staff);
            btnCallStaff = itemView.findViewById(R.id.btn_call_staff);
            main = itemView.findViewById(R.id.rltMain);
        }
    }

    public void askPermissionAndCall(String staffSDT){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 23

            // Check if we have Call permission
            int sendSmsPermisson = ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.CALL_PHONE);

            if (sendSmsPermisson != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                ((Activity)context).requestPermissions(
                        new String[]{Manifest.permission.CALL_PHONE},
                        REQUEST_CODE_CALL_PHONE
                );
                return;
            }
        }
        callNow(staffSDT);
    }

    private void callNow(String staffSDT) {
        String phoneNumber = staffSDT;
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        try {
            context.startActivity(callIntent);
        } catch (Exception ex) {
            Toast.makeText(context,"Your call failed... " + ex.getMessage(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
}
