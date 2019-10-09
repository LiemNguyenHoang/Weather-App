package com.example.wt.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
private List<Fragment> fragmentList = new ArrayList<>();
private List<String>fragmentListTitle=new ArrayList<>();

    public ViewPagerAdapter(@NonNull FragmentManager fm, Integer behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void AddFragment(Fragment fragment,String title){
        fragmentList.add(fragment);
        fragmentListTitle.add(title);


    }

    public CharSequence getPageTitle(int i){
        return fragmentListTitle.get(i);
    }


}
