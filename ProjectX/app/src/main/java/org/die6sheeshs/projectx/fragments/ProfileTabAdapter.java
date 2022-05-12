package org.die6sheeshs.projectx.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProfileTabAdapter extends FragmentStateAdapter {

    private List<String> titleList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();

    public ProfileTabAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return this.fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return this.titleList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        this.titleList.add(title);
        this.fragmentList.add(fragment);
    }

    public CharSequence getPageTitle(int position) {
        return this.titleList.get(position);
    }
}
