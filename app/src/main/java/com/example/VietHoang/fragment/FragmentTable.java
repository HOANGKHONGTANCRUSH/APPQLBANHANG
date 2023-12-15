package .fragment;

import android.app.Dialog;
import android.os.Bundle;

import static .utilities.Constants.BOARD_ID;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import .Dao.BoardDAO;
import .Model.Board;
import .R;
import .activity.MainActivity;
import .adapter.Table_Recyview;

import java.util.List;

public class FragmentTable extends Fragment {

    private ImageButton imageButton;
    private RecyclerView recy_table;
    private BoardDAO boardDAO;
    private List<Board> listBoard;
    private Table_Recyview adapter;
    private SearchView searchTable;


    public FragmentTable() {
        // Required empty public constructor
    }

    public static FragmentTable newInstance(String param1, String param2) {
        FragmentTable fragment = new FragmentTable();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tablee, container, false);
        recy_table = view.findViewById(R.id.recy_table);
        imageButton = view.findViewById(R.id.imageButton);
        searchTable = view.findViewById(R.id.searchTable);

        boardDAO = new BoardDAO(getContext());
        listBoard = boardDAO.getAllBoard();

        adapter = new Table_Recyview(listBoard, getContext(), FragmentTable.this);
        recy_table.setLayoutManager(new LinearLayoutManager(getContext()));
        recy_table.setAdapter(adapter);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_themtable);

                Button button_luu = dialog.findViewById(R.id.btn_create_table);
                EditText edNumberBoard = dialog.findViewById(R.id.edBoardNumber);
                button_luu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edNumberBoard.getText().toString().trim().isEmpty()){
                            // Nếu edit trống thì báo lỗi
                            edNumberBoard.setError("Nhập số bàn");
                            return;
                        }

                        int boardNumber = Integer.parseInt(edNumberBoard.getText().toString());
                        boolean checkBoardAgain = boardDAO.checkBoardAgain(boardNumber);

                        if (checkBoardAgain == true){
                            // nếu chekBoardAgain = true là bàn đó đã tồn tại
                            edNumberBoard.setError("Bàn này đã tồn tại");
                            return;
                        }
                        // Nếu ko có lỗi thì insert bàn
                        // inser board vào database
                        int numberBoard = Integer.parseInt(edNumberBoard.getText().toString());

                        Board board = new Board(numberBoard, 1);
                        boardDAO.insertBoard(board);

                        listBoard.clear();

                        listBoard.addAll(boardDAO.getAllBoard());
                        adapter.notifyDataSetChanged();

                        BOARD_ID = boardDAO.getMaxIdBoard();
                        ((MainActivity)getContext()).myIntent();
                        dialog.dismiss();

                    }
                });
                dialog.show();
            }
        });
        searchTable.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        return view;
    }

}