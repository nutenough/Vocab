package app.mueller.schiller.weber.com.vicab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class EditVocabAdapter extends FragmentPagerAdapter {

    String[] tabTitleArray = {"Vokabel", "Meta"};

    public EditVocabAdapter(FragmentManager manager) {
        super(manager);
    }

    // Refers the fragment to the ViewPager
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new EditVocabFragmentOne();
            case 1: return new EditVocabFragmentTwo();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitleArray[position];
    }
}
