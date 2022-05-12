package org.die6sheeshs.projectx.fragments;

import androidx.annotation.NonNull;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ProfileTabConfigurationStrategy implements TabLayoutMediator.TabConfigurationStrategy {

    private ProfileTabAdapter profileTabAdapter;

    public ProfileTabConfigurationStrategy(ProfileTabAdapter profileTabAdapter) {
        this.profileTabAdapter = profileTabAdapter;
    }

    @Override
    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
        tab.setText(profileTabAdapter.getPageTitle(position));
    }
}
