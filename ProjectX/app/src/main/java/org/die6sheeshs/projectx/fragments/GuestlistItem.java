package org.die6sheeshs.projectx.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.entities.Party;
import org.die6sheeshs.projectx.entities.Ticket;
import org.die6sheeshs.projectx.entities.User;
import org.die6sheeshs.projectx.helpers.ImageConversion;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuestlistItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuestlistItem extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String INDEX_PARAM = "list_index";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LinearLayout linearLayout;
    private View view;
    private User user;
    private Ticket ticket;

    public GuestlistItem(User user) {
        // Required empty public constructor
        this.user = user;
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Index of list item.
     * @return A new instance of fragment GuestlistItem.
     */
    // TODO: Rename and change types and number of parameters
    public static GuestlistItem newInstance(User user) {
        GuestlistItem fragment = new GuestlistItem(user);
        Bundle args = new Bundle();
        args.putString(INDEX_PARAM, "user");
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_guestlist_item, container, false);
        initializeContent(user);
        return view;
    }

    private void initializeContent(User user){
        setUsername(user.getNick_name());
        setProfilePicture(user.getProfile_pic());
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
    }

    private void setUsername(String username){
        TextView textView = view.findViewById(R.id.userNameGuestList);
        textView.setText(username);
    }

    private void setProfilePicture(String base64){
        ImageView imageView = view.findViewById(R.id.guestListImageView);
        Bitmap bitmap = ImageConversion.base64ToBitmap(base64);
        imageView.setImageBitmap(bitmap);
    }

    private void setFirstName(String firstName){
        TextView textView = view.findViewById(R.id.guestFName);
        textView.setText("First Name: " + firstName);
    }

    private void setLastName(String lastName){
        TextView textView = view.findViewById(R.id.guestLName);
        textView.setText("First Name: " + lastName);
    }
}