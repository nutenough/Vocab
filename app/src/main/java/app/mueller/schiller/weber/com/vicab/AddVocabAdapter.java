package app.mueller.schiller.weber.com.vicab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AddVocabAdapter extends FragmentPagerAdapter {

    String[] tabTitleArray = {"Vokabel", "Medien", "Meta"};

    public AddVocabAdapter(FragmentManager manager) {
        super(manager);
    }

    // Refers the fragment to the ViewPager
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new AddVocabFragmentOne();
            case 1: return new AddVocabFragmentTwo();
            case 2: return new AddVocabFragmentThree();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitleArray[position];
    }
}
