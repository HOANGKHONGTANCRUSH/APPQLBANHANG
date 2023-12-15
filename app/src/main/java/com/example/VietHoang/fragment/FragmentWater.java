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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class FragmentWater extends Fragment {

    RecyclerView rvWater;
    List<Food> list = new ArrayList<>();
    FoodDAO dao;
    FastFoodAdapter adapter;
    ImageView ivAddWater, imageView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvWater = view.findViewById(R.id.rv_water);
        ivAddWater = view.findViewById(R.id.iv_add_water);

        ivAddWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvWater.setLayoutManager(layoutManager);

        dao = new FoodDAO(getContext());
        list = dao.getAllFoodType("1", "1");

        adapter = new FastFoodAdapter(getContext(), (ArrayList<Food>) list, FragmentWater.this);
        rvWater.setAdapter(adapter);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_water, container, false);
    }

    public byte[] imageToByte(ImageView iv){
        BitmapDrawable drawable = (BitmapDrawable) iv.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public void openDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = this.getLayoutInflater().inflate(R.layout.dialong_add_item, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);

        imageView = view.findViewById(R.id.iv_photo);
        EditText edtNameItem = view.findViewById(R.id.edtNameItem);
        EditText edtPriceItem = view.findViewById(R.id.edtPriceItem);
        AppCompatButton btnChoosePhoto = view.findViewById(R.id.btnChoosePhoto);
        AppCompatButton btnAddItem = view.findViewById(R.id.btnAddItem);
        AppCompatButton btnCancelItem = view.findViewById(R.id.btnCancelItem);

        btnChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    edtNameItem.setError("Enter Name");
                    edtPriceItem.setError("Enter Price");
                    edtNameItem.requestFocus();
                }else {
                    String name = edtNameItem.getText().toString();
                    double price = Double.parseDouble(edtPriceItem.getText().toString());
                    byte[] image = imageToByte(imageView);

                    Food food = new Food(1, 1, name, price, image);
                    dao.insertFood(food);
                    list.clear();
                    list.addAll(dao.getAllFoodType("1", "1"));
                    adapter.notifyDataSetChanged();
                    rvWater.smoothScrollToPosition(list.size());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 999 && data != null){
            if (data.getExtras() != null){
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);
            }else {
                Uri uri = data.getData();
                try {
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    options.inSampleSize = 3;
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }
            }
        }else if (requestCode == 555 && data != null) {
            if (data.getExtras() != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                adapter.setBitmap(imageBitmap);
            } else {
                Uri uri = data.getData();
                try {
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    options.inSampleSize = 3;
                    Bitmap imageBitmap = BitmapFactory.decodeStream(inputStream, null, options);
                    adapter.setBitmap(imageBitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}