package org.die6sheeshs.projectx.fragments;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.entities.Party;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PartyPictures extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private final Party p;
    private View v;

    private List<byte[]> images = new ArrayList<>();

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
        initElements();
        return v;
    }

    private void initElements(){
        initAddPictureButton();
        loadPicturesOfParty();
        initUploadPictures();
        updateImageViews();
    }

    private int mainImageIndex = 0;

    private void updateImageViews(){
        LinearLayout linearLayout = v.findViewById(R.id.party_pictures_linear_layout);

        FragmentManager fragMan = getChildFragmentManager();
        FragmentTransaction fragTransaction = fragMan.beginTransaction();

        for(int i = 0; i < images.size(); i++){
            final int index = i;
            View.OnClickListener deleter = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    images.remove(index);
                    updateImageViews();
                }
            };

            View.OnClickListener mainImageSetter = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainImageIndex = index;
                }
            };


            Fragment imageFragment = new PartyPictureItem(images.get(i), deleter, mainImageSetter);
            fragTransaction.add(linearLayout.getId(), imageFragment, "image#"+i);

        }

        fragTransaction.commit();
    }

    private void loadPicturesOfParty(){
        /**
        Bitmap bmp = Bitmap.createBitmap(1920,1080, Bitmap.Config.ARGB_8888);
        for(int x = 0; x < 1919; x++){
            for(int y = 0; y < 1079; y++){
                bmp.setPixel(x,y, Color.rgb(x%256,y%256,0));
            }
        }


         Bitmap bmp1 = Bitmap.createBitmap(1920,1080, Bitmap.Config.ARGB_8888);
         for(int x = 0; x < 1919; x++){
         for(int y = 0; y < 1079; y++){
         bmp1.setPixel(x,y, Color.rgb(x%256,y%256,x%256));
         }
         }
         Bitmap bmp2 = Bitmap.createBitmap(1920,1080, Bitmap.Config.ARGB_8888);
         for(int x = 0; x < 1919; x++){
         for(int y = 0; y < 1079; y++){
         bmp2.setPixel(x,y, Color.rgb(x%256,0,x%256));
         }
         }
         Bitmap bmp3 = Bitmap.createBitmap(1920,1080, Bitmap.Config.ARGB_8888);
         for(int x = 0; x < 1919; x++){
         for(int y = 0; y < 1079; y++){
         bmp3.setPixel(x,y, Color.rgb(0,y%256,x%256));
         }
         }

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
         bmp.compress(Bitmap.CompressFormat.PNG, 100, bout);
        images.add(bout.toByteArray());

        ByteArrayOutputStream bout1 = new ByteArrayOutputStream();
        bmp1.compress(Bitmap.CompressFormat.PNG, 100, bout1);
        images.add(bout1.toByteArray());

        ByteArrayOutputStream bout2 = new ByteArrayOutputStream();
        bmp2.compress(Bitmap.CompressFormat.PNG, 100, bout2);
        images.add(bout2.toByteArray());

        ByteArrayOutputStream bout3 = new ByteArrayOutputStream();
        bmp3.compress(Bitmap.CompressFormat.PNG, 100, bout3);
        images.add(bout3.toByteArray());
        **/

        //todo load pictures from backend
    }

    private void initUploadPictures(){
        Button uploadPicsBtn = (Button) v.findViewById(R.id.uploadPictures);
        uploadPicsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo upload images
                for(byte[] bytes: images){

                }
            }
        });

    }

    private void initAddPictureButton(){
        Button addPicBtn = (Button) v.findViewById(R.id.addPictureButton);

    }
}
