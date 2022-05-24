package org.die6sheeshs.projectx.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputLayout;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.activities.MainActivity;
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
        LinearLayout searchLayout = view.findViewById(R.id.searches);
        String userId = getArguments().getString("userId");
        TextInputLayout search = view.findViewById(R.id.nickNameSearchField);
        ImageButton searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener((l)->{
            String nickName = search.getEditText().getText().toString();
            Observable<Friend> resp = FriendsPersistence.getInstance().getFriendByNickName(nickName);
            resp.subscribeOn(Schedulers.io())
                    .subscribe(friend ->{
                        getActivity().runOnUiThread(() ->{
                            FragmentManager fragMan = getChildFragmentManager();
                            FragmentTransaction fragTransaction = fragMan.beginTransaction();
                            View.OnClickListener buttonAction = view -> {
                                Fragment frag = new FriendInfo(friend);
                                ((MainActivity) getActivity()).replaceFragment(frag);
                            };
                            Fragment fragment = new FriendListItem(friend);

                            fragTransaction.add(searchLayout.getId(), fragment, "party#" + friend.getFriend_id());

                            fragTransaction.commit();
                        });
                    });
        });
    }
}
