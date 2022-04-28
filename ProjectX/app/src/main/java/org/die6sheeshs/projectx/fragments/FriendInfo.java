package org.die6sheeshs.projectx.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.entities.Friend;
import org.die6sheeshs.projectx.helpers.ImageConversion;

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

    public FriendInfo(Friend friend, boolean isFriend) {
        this.friend = friend;
        this.isFriend = isFriend;
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FriendInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendInfo newInstance(Friend friend,boolean isFriend) {
        FriendInfo fragment = new FriendInfo(friend,isFriend);
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

    private void init(){
        setPicture();
        setButton();
        setNickname();
        setAboutMe();
    }

    private void setNickname(){
        TextView nick = view.findViewById(R.id.nickname);
        nick.setText(friend.getNick_name());
    }

    private void setAboutMe(){
        TextView about = view.findViewById(R.id.aboutMeText);
        about.setText(friend.getAbout_me());
    }

    private void setButton(){
        Button addOdel = view.findViewById(R.id.addORemove);
        if(isFriend){
            addOdel.setText("Remove friend");
            addOdel.setOnClickListener((l)->{

            });
        }else{
            addOdel.setText("add as Friend");
            addOdel.setOnClickListener((l)->{

            });
        }
    }

    private void setPicture(){
        ImageView profile_pic = view.findViewById(R.id.friend_profile_pic);
        Bitmap image = ImageConversion.base64ToBitmap(friend.getProfile_pic());
        if(image!=null){
            profile_pic.setImageBitmap(image);
        }
    }
}