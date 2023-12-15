package .adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import .Dao.FoodBoardDAO;
import .Model.FoodBoard;
import .R;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> implements Filterable {

    private Context context;
    private List<FoodBoard> listFoodBoard;
    private List<FoodBoard> listFoodBoardOld;
    private FoodBoardDAO foodBoardDAO;

    public OrderAdapter(Context context, List<FoodBoard> listFoodBoard) {
        this.context = context;
        this.listFoodBoard = listFoodBoard;
        this.listFoodBoardOld = listFoodBoard;
        this.foodBoardDAO = new FoodBoardDAO(context);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        View view = inf.inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, @SuppressLint("RecyclerView") int position) {
        FoodBoard foodBoard = listFoodBoard.get(position);
        holder.tvFoodName.setText(foodBoard.getFoodName());
        holder.tvBoardNumber.setText("Bàn số " + foodBoard.getBoardNumber());

        holder.chkOrder.setChecked(foodBoard.getFoodBoardStatus() == 2);

        holder.chkOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.chkOrder.isChecked()){
                    notifyChanged(foodBoard, foodBoard.getFoodBoardId(), 2, position);
                }else {
                    notifyChanged(foodBoard, foodBoard.getFoodBoardId(), 1, position);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.chkOrder.toggle();
                if (holder.chkOrder.isChecked()){
                    notifyChanged(foodBoard, foodBoard.getFoodBoardId(), 2, position);
                }else {
                    notifyChanged(foodBoard, foodBoard.getFoodBoardId(), 1, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listFoodBoard.size();
    }

    private void notifyChanged(FoodBoard foodBoard, int id, int status, int position) {
        foodBoardDAO.updateStatus(id, status);
        foodBoard.setFoodBoardStatus(status);
        notifyItemChanged(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search = constraint.toString();
                if (search.isEmpty()){
                    listFoodBoard = listFoodBoardOld;
                }else {
                    List<FoodBoard> list = new ArrayList<>();
                    for (FoodBoard item : listFoodBoardOld) {
                        if (item.getFoodName().toLowerCase().contains(search.toLowerCase())){
                            list.add(item);
                        }
                    }
                    listFoodBoard = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listFoodBoard;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listFoodBoard = (List<FoodBoard>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder{

        protected TextView tvFoodName, tvBoardNumber;
        protected CheckBox chkOrder;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            tvBoardNumber = itemView.findViewById(R.id.tvBoardNumber);
            chkOrder = itemView.findViewById(R.id.chkOrder);
        }
    }
}
