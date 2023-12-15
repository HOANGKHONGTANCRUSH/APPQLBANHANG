package .adapter;

import static .utilities.Constants.BOARD_ID;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import .Dao.FoodBoardDAO;
import .Model.Food;
import .Model.FoodBoard;
import .R;
import .activity.FoodBoardActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodAdapterViewHolder> implements Filterable {

    private Context context;
    private List<Food> listFood, listFoodOld;
    private FoodBoardDAO foodBoardDAO;
    private DecimalFormat formater;

    public FoodAdapter(Context context, List<Food> listFood) {
        this.context = context;
        this.listFood = listFood;
        this.listFoodOld = listFood;
        foodBoardDAO = new FoodBoardDAO(context);
        formater = new DecimalFormat("###,###,### VND");
    }

    @NonNull
    @Override
    public FoodAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_food_board1, parent, false);
        FoodAdapterViewHolder viewHolder = new FoodAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapterViewHolder holder, int position) {
        Food food = listFood.get(position);
        holder.tvNameFood.setText(food.getFood_name());
        holder.tvPrice.setText(formater.format(food.getFood_price()));

        Bitmap imageBitmap = BitmapFactory.decodeByteArray(food.getFood_image(), 0, food.getFood_image().length);
        holder.imgFood.setImageBitmap(imageBitmap);

        //Click item recyclerView
        holder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FoodBoard foodBoard = new FoodBoard(food.getFood_id(), BOARD_ID);
                foodBoardDAO.insertFoodBoard(foodBoard);
                ((FoodBoardActivity)context).capNhatRecyFood2();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listFood.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search = constraint.toString();
                if (search.isEmpty()){
                    listFood = listFoodOld;
                }else {
                    List<Food> list = new ArrayList<>();
                    for (Food foood : listFoodOld) {
                        if (foood.getFood_name().toLowerCase().contains(search.toLowerCase())){
                            list.add(foood);
                        }
                    }
                    listFood = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listFood;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listFood = (List<Food>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class FoodAdapterViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgFood;
        public TextView tvNameFood, tvPrice;
        public RelativeLayout main;

        public FoodAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.iv_food);
            tvNameFood = itemView.findViewById(R.id.tv_name_food);
            tvPrice = itemView.findViewById(R.id.tv_price_food);
            main = itemView.findViewById(R.id.main);
        }

    }
}
