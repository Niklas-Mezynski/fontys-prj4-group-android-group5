package org.die6sheeshs.projectx.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.activities.MainActivity;
import org.die6sheeshs.projectx.entities.Friend;
import org.die6sheeshs.projectx.helpers.SessionManager;
import org.die6sheeshs.projectx.restAPI.FriendsPersistence;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class ProfileFriendsTab extends Fragment {

    private View view;

    public static ProfileFriendsTab newInstance(String userId) {
        ProfileFriendsTab profileFriendsTab = new ProfileFriendsTab();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        profileFriendsTab.setArguments(args);
        return profileFriendsTab;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_tab_friends, container);
        init();
        return view;
    }

    private void init() {
        LinearLayout linearLayoutV = view.findViewById(R.id.friendItems_layout);
        //String id = SessionManager.getInstance().getUserId();
        String userId = getArguments().getString("userId");
        String jwt = SessionManager.getInstance().getToken();
        Observable<List<Friend>> resp = FriendsPersistence.getInstance().getFriendsOfUser(userId);
        resp.subscribeOn(Schedulers.io())
                .subscribe(friends -> {
                    for (Friend f :friends) {
                        System.out.println("NewFriendfsfdsfs: "+f.toString());
                        getActivity().runOnUiThread(()->{
                            FragmentManager fragMan = getChildFragmentManager();
                            FragmentTransaction fragTransaction = fragMan.beginTransaction();
                            Fragment fragment = FriendListItem.newInstance(f);

                            fragTransaction.add(linearLayoutV.getId(), fragment, "friend#" + f.getFriend_id());

                            fragTransaction.commit();
                        });
                    }
                }, error -> Log.e("FriendTabs", error.getMessage()));
    }
}
