package org.die6sheeshs.projectx.fragments;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;<<<<<<<HEAD=======
import android.se.omapi.Session;>>>>>>>a1cc3f7(Add"remove friend"button to FriendInfo fragment)
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.activities.MainActivity;
import org.die6sheeshs.projectx.entities.Friend;
import org.die6sheeshs.projectx.helpers.ImageConversion;
import org.die6sheeshs.projectx.helpers.SessionManager;
import org.die6sheeshs.projectx.restAPI.FriendsPersistence;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendInfo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Friend friend;
    private boolean isFriend;
    private View view;
    private FriendsPersistence friendsPersistence = FriendsPersistence.getInstance();

    public FriendInfo(Friend friend) {
        this.friend = friend;
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FriendInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendInfo newInstance(Friend friend) {
        FriendInfo fragment = new FriendInfo(friend);
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
        this.view = inflater.inflate(R.layout.fragment_friend_info, container, false);
        init();
        return view;
    }

    private void init() {
        isFriend();
        setPicture();
        setNickname();
        setAboutMe();

    }

    private void isFriend() {
        String userId = SessionManager.getInstance().getUserId();
        String jwt = SessionManager.getInstance().getToken();
        Observable<List<Friend>> resp = FriendsPersistence.getInstance().getFriendsOfUser(userId);
        isFriend = false;
        resp.subscribeOn(Schedulers.io())
                .subscribe(friends -> {
                    getActivity().runOnUiThread(() -> {
                        System.out.println(friend.toString());
                        for (Friend f : friends) {
                            if (f.equals(friend)) {
                                isFriend = true;
                                break;
                            }
                        }
                        setButton();
                    });

                }, error -> Log.e("FriendTabs", error.getMessage()));

    }

    private void setNickname() {
        TextView nick = view.findViewById(R.id.nickname);
        nick.setText(friend.getNick_name());
    }

    private void setAboutMe() {
        TextView about = view.findViewById(R.id.aboutMeText);
        about.setText(friend.getAbout_me());
    }

    private void setButton() {
        Button addOdel = view.findViewById(R.id.addORemove);
        if (isFriend) {
            addOdel.setText("Remove friend");
            addOdel.setOnClickListener((l) -> {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Remove Friend")
                        .setMessage("Are you sure you want to remove this friend?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                String userId = SessionManager.getInstance().getUserId();
                                String friendId = friend.getFriend_id();
                                Log.e("Remove friend", userId + " : " + friendId);
                                friendsPersistence.deleteFriend(userId, friendId);
                                Fragment fragment = new Profile();
                                ((MainActivity) getActivity()).replaceFragment(fragment);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            });
        } else {
            addOdel.setText("add as friend");
            addOdel.setOnClickListener((l) -> {

            });
        }
    }

    private void setPicture() {
        ImageView profile_pic = view.findViewById(R.id.friend_profile_pic);
        Bitmap image = ImageConversion.base64ToBitmap(friend.getProfile_pic());
        if (image != null) {
            profile_pic.setImageBitmap(image);
        }
    }
}