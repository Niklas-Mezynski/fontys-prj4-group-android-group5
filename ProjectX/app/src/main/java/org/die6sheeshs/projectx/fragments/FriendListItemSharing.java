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
import org.die6sheeshs.projectx.helpers.Toaster;

public class FriendListItemSharing extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Friend friend;
    private boolean isFriend;
    private String partyId;
    private View view;

    public FriendListItemSharing() {}

    public FriendListItemSharing(Friend friend,boolean isFriend, String partyId) {
        this.friend = friend;
        this.isFriend = isFriend;
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FriendListItem.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendListItem newInstance(Friend friend,boolean isFriend) {
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
        this.view = inflater.inflate(R.layout.fragment_friend_list_item_sharing, container, false);
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

    private void setAction(){
        LinearLayout wrapper = view.findViewById(R.id.wrapper);
        wrapper.setOnClickListener(view -> {
            sendInvitation();
        });
    }

    private void sendInvitation(){
        System.out.println("Invitation method invoked");
        //todo Api call to send invitation
    }

    private void setInvitationSent(boolean sent){
        Toaster.makeToast(this.getContext(), "Invitation to "+friend.getNick_name()+" sent");


    }
}
