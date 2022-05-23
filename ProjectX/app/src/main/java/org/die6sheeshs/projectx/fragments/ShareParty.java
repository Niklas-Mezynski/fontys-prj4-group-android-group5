package org.die6sheeshs.projectx.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.entities.Friend;
import org.die6sheeshs.projectx.entities.Party;
import org.die6sheeshs.projectx.helpers.SessionManager;
import org.die6sheeshs.projectx.restAPI.FriendsPersistence;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class ShareParty extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String partyId;
    private View view;

    public ShareParty() {}

    public ShareParty(String partyId) {
        this.partyId=partyId;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FriendListItem.
     */
    // TODO: Rename and change types and number of parameters
    public static ShareParty newInstance(String partyId) {
        ShareParty fragment = new ShareParty(partyId);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_share_party, container, false);
        init();
        return view;
    }

    private void init(){
        setFriends();
    }

    private void setFriends() {
        LinearLayout linearLayout = view.findViewById(R.id.friendLinearLayout);
        String uid = SessionManager.getInstance().getUserId();
        Observable<List<Friend>> friendsOfUserObs = FriendsPersistence.getInstance().getFriendsOfUser(uid);
        friendsOfUserObs.subscribeOn(Schedulers.io()).
                subscribe((friends)->{
                    FragmentManager fragMgr = getChildFragmentManager();
                    FragmentTransaction fragTransaction = fragMgr.beginTransaction();


                    for(Friend f : friends){
                        Fragment fragment = new FriendListItemSharing(f, true, this.partyId);
                        fragTransaction.add(linearLayout.getId(), fragment, "friend#" + f.getFriend_id());
                    }

                    fragTransaction.commit();
                },(error)->{
                    Log.v("Friends", "unable to load friends: "+ error.getMessage());
                });

    }
}
