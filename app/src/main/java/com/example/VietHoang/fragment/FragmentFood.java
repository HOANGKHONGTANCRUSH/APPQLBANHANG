package .fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import .R;
import .viewpager.ViewPagerFragmentFood;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class FragmentFood extends Fragment {

    private ChipNavigationBar chipNavigationBar;
    private ViewPager viewPager;

    public FragmentFood() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chipNavigationBar = view.findViewById(R.id.chipNavFood);
        viewPager = view.findViewById(R.id.viewPagerFragmentFood);

        ViewPagerFragmentFood adapter = new ViewPagerFragmentFood(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        chipNavigationBar.setItemSelected(R.id.nav_Fast_Food, true);

        // Hàm bắt sự kiện ChipNaigationBar
        chipNavigationBarOnclick();

        // Hàm bắt sự kiện khi thay đổi trang của viewPager
        viewPagerAddOnPageChangeListener();
    }

    private void viewPagerAddOnPageChangeListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        chipNavigationBar.setItemSelected(R.id.nav_Fast_Food, true);
                        break;
                    case 1:
                        chipNavigationBar.setItemSelected(R.id.nav_Water, true);
                        break;
                    case 2:
                        chipNavigationBar.setItemSelected(R.id.nav_Combo, true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    // bắt sự kiện ChipNaigationBar
    private void chipNavigationBarOnclick() {
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.nav_Fast_Food:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.nav_Water:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.nav_Combo:
                        viewPager.setCurrentItem(2);
                        break;
                }
            }
        });
    }

    public static FragmentFood newInstance(String param1, String param2) {
        FragmentFood fragment = new FragmentFood();
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
        return inflater.inflate(R.layout.fragment_food, container, false);
    }
}