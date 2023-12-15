package .adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import .Dao.FoodBoardDAO;
import .Model.FoodBoard;
import .R;
import .activity.FoodBoardActivity;

import java.text.DecimalFormat;
import java.util.List;

public class FoodNumberAdapter extends RecyclerView.Adapter<FoodNumberAdapter.FoodNumberAdapterViewHolder>{

    private Context context;
    private List<FoodBoard> listFoodBoard;
    private DecimalFormat formater;
    private FoodBoardDAO foodBoardDAO;
    private Bitmap bitmap;

    public FoodNumberAdapter(Context context, List<FoodBoard> listFoodBoard) {
        this.context = context;
        this.listFoodBoard = listFoodBoard;
        foodBoardDAO = new FoodBoardDAO(context);
    }

    @NonNull
    @Override
    public FoodNumberAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(context);
        View view = inf.inflate(R.layout.item_food_board2, parent, false);
        return new FoodNumberAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodNumberAdapterViewHolder holder, @SuppressLint("RecyclerView") int position) {
        formater = new DecimalFormat("###,###,### VND");

        holder.tvNameFood.setText(listFoodBoard.get(position).getFoodName());
        holder.tvPrice.setText(formater.format(listFoodBoard.get(position).getFoodPrice()));
        holder.tvNumber.setText("x" + listFoodBoard.get(position).getFoodCount());

        bitmap = BitmapFactory.decodeByteArray(listFoodBoard.get(position).getFoodBoardImg(),0, listFoodBoard.get(position).getFoodBoardImg().length);
        holder.imgFood.setImageBitmap(bitmap);

        holder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodBoardDAO.deleteFoodBoard(listFoodBoard.get(position).getFoodBoardId());
                ((FoodBoardActivity)context).capNhatRecyFood2();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listFoodBoard.size();
    }

    public class FoodNumberAdapterViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgFood;
        public TextView tvNameFood, tvPrice, tvNumber;
        public RelativeLayout main;

        public FoodNumberAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.iv_food);
            tvNameFood = itemView.findViewById(R.id.tv_name_food);
            tvPrice = itemView.findViewById(R.id.tv_price_food);
            tvNumber = itemView.findViewById(R.id.tv_count);
            main = itemView.findViewById(R.id.main);
        }
    }
}
