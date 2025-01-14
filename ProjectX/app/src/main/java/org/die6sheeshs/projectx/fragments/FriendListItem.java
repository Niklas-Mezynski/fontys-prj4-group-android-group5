package org.die6sheeshs.projectx.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.activities.MainActivity;
import org.die6sheeshs.projectx.entities.Friend;
import org.die6sheeshs.projectx.helpers.ImageConversion;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendListItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendListItem extends Fragment {

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

    public FriendListItem() {}

    public FriendListItem(Friend friend) {
        this.friend = friend;
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FriendListItem.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendListItem newInstance(Friend friend) {
        FriendListItem fragment = new FriendListItem(friend);
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
        this.view = inflater.inflate(R.layout.fragment_friend_list_item, container, false);
        init();
        return view;
    }

    private void init(){
        setPicture();
        setUserName();
        setAction();
    }

    private void setUserName(){
        TextView username = view.findViewById(R.id.userName);
        username.setText(friend.getNick_name());
    }
    private void setPicture(){
        ImageView profile_pic = view.findViewById(R.id.image_profilepic);
        Bitmap image = ImageConversion.base64ToBitmap(friend.getProfile_pic());
        if(image!=null){
            profile_pic.setImageBitmap(image);
        }
    }

    public void setAction(){
        LinearLayout wrapper = view.findViewById(R.id.wrapper);
        wrapper.setOnClickListener(view -> {
            Fragment frag = new FriendInfo(friend);
            ((MainActivity)getActivity()).replaceFragment(frag);
          });
    }
}