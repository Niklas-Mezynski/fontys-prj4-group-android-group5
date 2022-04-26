package org.die6sheeshs.projectx.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.helpers.SessionManager;

public class ProfileFriendsTab extends Fragment {

    private View view;

    public static ProfileFriendsTab getInstance() {
        return new ProfileFriendsTab();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_tab_friends, container, false);
        init();
        return view;
    }

    private void init(){
        LinearLayout linearLayoutV = view.findViewById(R.id.linlayV);
        String id = SessionManager.getInstance().getUserId();
        String jwt = SessionManager.getInstance().getToken();
    }
}
