package .fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import .Dao.FoodBoardDAO;
import .Model.FoodBoard;
import .R;
import .adapter.OrderAdapter;

import java.util.List;

public class FragmentOrder extends Fragment {

    private RecyclerView rcvOrder;
    private ImageView imgDelete;
    private List<FoodBoard> listFoodBoard;
    private SearchView searchOrder;

    public FragmentOrder() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rcvOrder = view.findViewById(R.id.rcvOrder);
        imgDelete = view.findViewById(R.id.imgDelete);
        searchOrder = view.findViewById(R.id.searchOrder);

        FoodBoardDAO foodBoardDAO = new FoodBoardDAO(getContext());
        listFoodBoard = foodBoardDAO.getFoodBoardStatus();

        OrderAdapter orderAdapter = new OrderAdapter(getContext(), listFoodBoard);
        rcvOrder.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvOrder.setAdapter(orderAdapter);

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodBoardDAO.deleteStatus(2);
                listFoodBoard.clear();
                listFoodBoard.addAll(foodBoardDAO.getFoodBoardStatus());
                orderAdapter.notifyDataSetChanged();
            }
        });

        searchOrder.setQueryHint("Search...");
        searchOrder.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                orderAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                orderAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    public static FragmentOrder newInstance(String param1, String param2) {
        FragmentOrder fragment = new FragmentOrder();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false);
    }
}