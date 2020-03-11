package rs.ac.uns.ftn.eventsapp.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import rs.ac.uns.ftn.eventsapp.fragments.EventListFragment;

public class EventListPagerAdapter extends FragmentStatePagerAdapter {

    public EventListPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return EventListFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Why this Tab " + position;
    }
}
