package .activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import .R;
import .Dao.DoanhThuDAO;

import java.text.DecimalFormat;

public class DoanhThuActivity extends AppCompatActivity {

    private TextView tvDay, tvMonth, tvYear, tvLastDay, tvLastMonth, tvLastYear;
    private ProgressBar prDay, prMonth, prYear, prLastDay, prLastMonth, prLastYear;
    private DoanhThuDAO doanhThuDAO;
    private double priceDay, priceLastDay, priceMonth, priceLastMonth, priceYear, priceLastYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doanh_thu);

        // Hàm ánh xạ
        init();

        doanhThuDAO = new DoanhThuDAO(this);

        DecimalFormat formater = new DecimalFormat("###,###,###");

        priceDay = doanhThuDAO.getDoanhThuDay();
        priceLastDay = doanhThuDAO.getDoanhThuLastDay();
        tvDay.setText(formater.format(priceDay));
        tvLastDay.setText(formater.format(priceLastDay));

        priceMonth = doanhThuDAO.getDoanhThuMonth();;
        priceLastMonth = doanhThuDAO.getDoanhThuLastMonth();
        tvMonth.setText(formater.format(priceMonth));
        tvLastMonth.setText(formater.format(priceLastMonth));

        priceYear = doanhThuDAO.getDoanhThuYear();
        priceLastYear = doanhThuDAO.getDoanhThuLastYear();
        tvYear.setText(formater.format(priceYear));
        tvLastYear.setText(formater.format(priceLastYear));

        double max = 0;
        max = Math.max(priceDay, priceLastDay);
        settingProgressBar(prDay, max, priceDay, R.color.blue2);
        settingProgressBar(prLastDay, max, priceLastDay, R.color.yellow2);

        max = Math.max(priceMonth, priceLastMonth);
        settingProgressBar(prMonth, max, priceMonth, R.color.green2);
        settingProgressBar(prLastMonth, max, priceLastMonth, R.color.red2);

        max = Math.max(priceYear, priceLastYear);
        settingProgressBar(prYear, max, priceYear, R.color.purple2);
        settingProgressBar(prLastYear, max, priceLastYear, R.color.pink2);

    }

    private void settingProgressBar(ProgressBar progressBar, double max, double getProgress, int color) {
        progressBar.setMax((int) max);
        progressBar.setProgress((int) getProgress);

        progressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(color)));
        progressBar.setProgressBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
    }

    // Ánh xạ
    private void init(){
        tvDay = findViewById(R.id.tvDay);
        tvLastDay = findViewById(R.id.tvLastDay);
        tvMonth = findViewById(R.id.tvMonth);
        tvLastMonth = findViewById(R.id.tvLastMonth);
        tvYear = findViewById(R.id.tvYear);
        tvLastYear = findViewById(R.id.tvLastYear);
        prDay = findViewById(R.id.prDate);
        prLastDay = findViewById(R.id.prLastDate);
        prMonth = findViewById(R.id.prMonth);
        prLastMonth = findViewById(R.id.prLastMonth);
        prYear = findViewById(R.id.prYear);
        prLastYear = findViewById(R.id.prLastYear);
    }

}