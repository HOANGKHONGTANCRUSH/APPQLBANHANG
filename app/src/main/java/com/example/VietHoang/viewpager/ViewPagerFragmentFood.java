package .viewpager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import .fragment.FragmentCombo;
import .fragment.FragmentFastFood;
import .fragment.FragmentWater;

public class ViewPagerFragmentFood extends FragmentStatePagerAdapter {

    public ViewPagerFragmentFood(@NonNull FragmentManager fm) {
        super(fm);
    }

    private int selectedPosition;

    public FastFoodViewHolder(@NonNull View itemView) {
        super(itemView);
        // ...
    }

    public void setItemClickListener(ItemClickListener itemClickListener, int position) {
        this.itemClickListener = itemClickListener;
        this.selectedPosition = position;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new FragmentFastFood();
            case 1: return new FragmentWater();
            case 2: return new FragmentCombo();
            default: return new FragmentFastFood();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
