package .adapter;

import static .utilities.Constants.BOARD_ID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import .Model.Bill;
import .R;
import .activity.InfoHoaDon;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class Billadapter extends RecyclerView.Adapter<Billadapter.BillViewHolder> {

    public List<Bill> list;
    public Context context;
    public Fragment fragment;
    public DecimalFormat formater;

    public Billadapter(List<Bill> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_bill, parent, false);
        BillViewHolder viewHolder = new BillViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        formater = new DecimalFormat("###,###,### VND");
        holder.tv_table.setText("Bàn số: " + list.get(position).getBoard_number());
        holder.tv_gia.setText(formater.format(list.get(position).getBill_price()));
        holder.tv_date.setText(sdfDate(list.get(position).getBill_date()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BOARD_ID = list.get(position).getBoard_id();
                Intent intent = new Intent(context.getApplicationContext(), InfoHoaDon.class);
                intent.putExtra("numberBoard", holder.tv_table.getText().toString()); // put số bàn
                intent.putExtra("billPrice", holder.tv_gia.getText().toString()); // put giá thức ăn
                intent.putExtra("billDate", holder.tv_date.getText().toString()); // put ngày tháng của hóa đơn
                context.startActivity(intent);
            }
        });
    };

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BillViewHolder extends RecyclerView.ViewHolder{

        TextView tv_table, tv_gia,tv_date;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_table = itemView.findViewById(R.id.tv_table);
            tv_gia = itemView.findViewById(R.id.tv_gia);
            tv_date = itemView.findViewById(R.id.tv_date);
        }
    }

    // Chuyển định dạng yyyy/MM/dd sang dd/MM/yyyy
    public String sdfDate(String date) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        try {
            return sdf2.format(sdf1.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }
}
