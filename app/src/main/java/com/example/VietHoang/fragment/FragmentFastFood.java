package .fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import .Dao.FoodDAO;
import .Model.Food;
import .R;
import .adapter.FastFoodAdapter;
import .adapter.ItemClickListener;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class FragmentFastFood extends Fragment implements ItemClickListener {

    RecyclerView rvFastFood;
    FoodDAO foodDAO;
    List<Food> list = new ArrayList<>();
    FastFoodAdapter adapter;
    ImageView ivAddFood, imageView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvFastFood = view.findViewById(R.id.rv_fast_food);
        ivAddFood = view.findViewById(R.id.ivAddFood);

        ivAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvFastFood.setLayoutManager(layoutManager);

        //datasource
        foodDAO = new FoodDAO(getContext());
        list = foodDAO.getAllFoodType("0", "1");

        //Adapter
        adapter = new FastFoodAdapter(getContext(), (ArrayList<Food>) list, FragmentFastFood.this);
        rvFastFood.setAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fast_food, container, false);
    }

    public byte[] imageToByte(ImageView iv){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) iv.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public void openDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialong_add_item, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);

        //anh xa
        imageView = view.findViewById(R.id.iv_photo);
        EditText edtNameItem = view.findViewById(R.id.edtNameItem);
        EditText edtPriceItem = view.findViewById(R.id.edtPriceItem);
        AppCompatButton btnChoosePhoto = view.findViewById(R.id.btnChoosePhoto);
        AppCompatButton btnAddItem = view.findViewById(R.id.btnAddItem);
        AppCompatButton btnCancelItem = view.findViewById(R.id.btnCancelItem);



        //event
        btnChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hàm Lấy ảnh từ thiết bị
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                Intent image = new Intent(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
                Intent chooser = Intent.createChooser(intent, "CHOOSE");
                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{image});
                startActivityForResult(chooser, 999);
            }
        });


        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(edtNameItem.getText()) || TextUtils.isEmpty(edtPriceItem.getText())){
                    edtNameItem.setError("Enter Name!");
                    edtPriceItem.setError("Enter Price");
                    edtNameItem.requestFocus();
                }else {
                    String name = edtNameItem.getText().toString();
                    double price = Double.parseDouble(edtPriceItem.getText().toString());
                    byte[] image = imageToByte(imageView);
                    Food food = new Food(0, 1, name, price, image);
                    foodDAO.insertFood(food);
                    list.clear();
                    list.addAll(foodDAO.getAllFoodType("0", "1"));
                    adapter.notifyDataSetChanged();
                    rvFastFood.smoothScrollToPosition(list.size());
                    dialog.dismiss();
                }



            }
        });

        btnCancelItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void intentImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        Intent image = new Intent(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
        Intent chooser = Intent.createChooser(intent, "CHOOSE");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{image});
        startActivityForResult(chooser, 555);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999 && data != null) {
            if (data.getExtras() != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);
            } else {
                Uri uri = data.getData();
                try {
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    options.inSampleSize = 3;
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                    imageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }else if (requestCode == 555 && data != null) {
            if (data.getExtras() != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmao = (Bitmap) extras.get("data");
                adapter.setBitmap(imageBitmao);
                adapter.notifyDataSetChanged();
            } else {
                Uri uri = data.getData();
                try {
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    options.inSampleSize = 3;
                    Bitmap imagebitmap = BitmapFactory.decodeStream(inputStream, null, options);
                    adapter.setBitmap(imagebitmap);
                    adapter.notifyDataSetChanged();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onClick(View view, int position, boolean isLongClick) {

    }
}