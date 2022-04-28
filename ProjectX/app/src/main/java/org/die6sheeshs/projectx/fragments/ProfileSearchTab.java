package org.die6sheeshs.projectx.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.entities.Friend;
import org.die6sheeshs.projectx.helpers.SessionManager;
import org.die6sheeshs.projectx.restAPI.FriendsPersistence;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class ProfileSearchTab extends Fragment {

    private View view;

    public static ProfileSearchTab newInstance(String userId) {
        ProfileSearchTab profileSearchTab = new ProfileSearchTab();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        profileSearchTab.setArguments(args);
        return profileSearchTab;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_tab_search, container);
        init();
        return view;
    }

    private void init() {
        LinearLayout searchLayout = view.findViewById(R.id.searchItems_layout);
        String userId = getArguments().getString("userId");
    }
}
