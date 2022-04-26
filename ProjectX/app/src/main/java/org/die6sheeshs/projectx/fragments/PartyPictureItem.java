package org.die6sheeshs.projectx.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.entities.Party;

import java.util.ArrayList;
import java.util.List;

public class PartyPictureItem extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View v;

    private List<byte[]> images = new ArrayList<>();

    private final Bitmap image;

    private final View.OnClickListener deleteHandler, mainImageHandler;

    PartyPictureItem(byte[] image, View.OnClickListener deleteHandler, View.OnClickListener mainImageHandler){
        this.image = BitmapFactory.decodeByteArray(image, 0, image.length);
        this.deleteHandler = deleteHandler;
        this.mainImageHandler = mainImageHandler;
    }


    public static PartyPictureItem newInstance(String param1, String param2, byte[] image, View.OnClickListener deleteHandler, View.OnClickListener mainImageHandler) {
        PartyPictureItem fragment = new PartyPictureItem(image, deleteHandler, mainImageHandler);
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
        initView();
        return v;
    }

    private void initView(){
        initImageView();
        initDeleteButton();
        initSetMainImageButton();
    }

    private void initImageView(){
        ImageView imgView = (ImageView) v.findViewById(R.id.eventImageView);
        imgView.setImageBitmap(this.image);
    }
    private void initDeleteButton(){
        Button deleteButton = (Button) v.findViewById(R.id.removePictureButton);
        deleteButton.setOnClickListener(deleteHandler);
    }
    private void initSetMainImageButton(){
        Button setMainImgBtn = (Button) v.findViewById(R.id.setToMainImageButton);
        setMainImgBtn.setOnClickListener(mainImageHandler);

    }
}
