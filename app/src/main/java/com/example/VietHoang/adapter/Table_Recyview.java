package .adapter;

import static .utilities.Constants.BOARD_ID;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import .Dao.BoardDAO;
import .Model.Board;
import .R;
import .activity.MainActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Table_Recyview extends RecyclerView.Adapter<Table_Recyview.MyViewHolderTable> implements Filterable {

    public List<Board> list;
    public List<Board> listOld;
    public Context context;
    public Fragment fragment;
    public DecimalFormat formater;
    private ImageView imageView2;
    private BoardDAO boardDAO;

    public Table_Recyview(List<Board> list, Context context, Fragment fragment) {
        this.list = list;
        this.context = context;
        this.fragment = fragment;
        this.listOld = list;
        boardDAO = new BoardDAO(context);
    }

    @NonNull
    @Override
    public MyViewHolderTable onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(context);
        View view = inf.inflate(R.layout.item_table, parent, false);
        return new MyViewHolderTable(view);
    }

    @Override
    public void onBindViewHolder(Table_Recyview.MyViewHolderTable holder, @SuppressLint("RecyclerView") int position) {
        formater = new DecimalFormat("###,###,### VND");
        holder.tv_table.setText("Bàn " + list.get(position).getBoard_number());
        holder.tv_gia.setText(formater.format(list.get(position).getBoard_price()));

        holder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BOARD_ID = list.get(position).getBoard_id();
                ((MainActivity)context).myIntent();
            }
        });
        holder.main.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Xóa Table");
                builder.setMessage("Do you want delete table");


                builder.setPositiveButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });

                builder.setNegativeButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Board board = list.get(position);
                        board.setBoard_status(0);
                        boardDAO.updateStutusBoard(board);
                        boardDAO.sapxep();
                        list.clear();

                        list.addAll(boardDAO.getAllBoard());
                        notifyDataSetChanged();

                    }
                });
                builder.show();
                return true;
            }

        });

        holder.imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.dialog_suatable);

                Button button_luu = dialog.findViewById(R.id.btn_create_table);
                EditText edNumberBoard = dialog.findViewById(R.id.edBoardNumber);

                edNumberBoard.setText(String.valueOf(list.get(position).getBoard_number()));

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
                        int numberBoard = Integer.parseInt(edNumberBoard.getText().toString());
                        Board board = list.get(position);
                        board.setBoard_number(numberBoard);
                        boardDAO.updatenameBoard(board);
                        list.clear();
                        list.addAll(boardDAO.getAllBoard());
                        notifyDataSetChanged();
                        boardDAO.sapxep();
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }

        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search = constraint.toString();
                if (search.isEmpty()){
                    list = listOld;
                }else {
                    List<Board> listBoard = new ArrayList<>();
                    for (Board board : listOld) {
//                        if (board.getBoard_number().contains(strSearch.toLowerCase())){
//                            list.add(user);
//                        }
                        if (String.valueOf(board.getBoard_number()).contains(search.toLowerCase())){
                            listBoard.add(board);
                        }
                    }

                    list = listBoard;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = list;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (List<Board>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class  MyViewHolderTable extends RecyclerView.ViewHolder{

        public TextView tv_gia, tv_table;
        public ImageView imageView4;
        public RelativeLayout main;

        public MyViewHolderTable( View itemView) {
            super(itemView);
            main = itemView.findViewById(R.id.main);
            imageView4 = itemView.findViewById(R.id.imageView4);
            tv_gia = itemView.findViewById(R.id.tv_gia);
            tv_table = itemView.findViewById(R.id.tv_table);
        }
    }
}
