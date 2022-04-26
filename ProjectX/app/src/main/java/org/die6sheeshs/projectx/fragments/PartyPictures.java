package org.die6sheeshs.projectx.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.entities.Party;

public class PartyPictures extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private final Party p;
    private View v;

    PartyPictures(Party p){
        this.p = p;
    }

    public static PartyPictures newInstance(String param1, String param2, Party p) {
        PartyPictures fragment = new PartyPictures(p);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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

        View v = inflater.inflate(R.layout.fragment_party_pictures, container, false);
        this.v = v;
        // Inflate the layout for this fragment
        return v;
    }

    private void initElements(){
        initAddPictureButton();
    }

    private void initAddPictureButton(){
        Button addPicBtn = (Button) v.findViewById(R.id.addPictureButton);
        
    }
}
