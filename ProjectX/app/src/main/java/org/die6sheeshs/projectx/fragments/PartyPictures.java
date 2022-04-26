package org.die6sheeshs.projectx.fragments;

import static androidx.core.content.FileProvider.getUriForFile;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.entities.Party;
import org.die6sheeshs.projectx.entities.Pictures;
import org.die6sheeshs.projectx.restAPI.PartyPersistence;

import java.io.File;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class PartyPictures extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private final Party p;
    private View v;
    private Button takePicture;
    private ActivityResultLauncher<Uri> takePhotoActivity;
    private Uri takeImageUri;
    private Button submitPictures;

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your app.
                    takePhotoActivity.launch(takeImageUri);

                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                    Toast.makeText(getContext(), "Cannot take a new profile picture without permission", Toast.LENGTH_SHORT).show();
                }
            });

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
        //TODO generate filenames dependent on how many pictures are taken
        File newFile = new File(getContext().getExternalFilesDir("my_images"), "fileName.jpg");
        this.takeImageUri = getUriForFile(getContext(), "org.die6sheeshs.projectx.fileprovider", newFile);
        takePhotoActivity = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
            if(result){
                try {
                    images.add(Files.readAllBytes(newFile.toPath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                updateImageViews();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.v = inflater.inflate(R.layout.fragment_party_pictures, container, false);
        takePicture = v.findViewById(R.id.addPictureButton);
        takePicture.setOnClickListener(this::askPermAndTakeImg);
        // Inflate the layout for this fragment
        initElements();
        return v;
    }

    private void initElements(){
        loadPicturesOfParty();
        initUploadPictures();
        updateImageViews();
    }

    private int mainImageIndex = 0;

    private void updateImageViews(){
        LinearLayout linearLayout = v.findViewById(R.id.party_pictures_linear_layout);
        linearLayout.removeAllViews();

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
                    updateImageViews();
                }
            };

            Fragment imageFragment = new PartyPictureItem(images.get(i), deleter, mainImageSetter, i == mainImageIndex);
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
                PartyPersistence.getInstance().deletePartyPictures(p.getId()).subscribe(count -> {
                    Schedulers.io().scheduleDirect(() -> {
                        if(images.size() != 0 && mainImageIndex < images.size() && mainImageIndex >= 0){
                            uploadPic(images.get(mainImageIndex));
                        }



                        for(int i = 0; i < images.size(); i++){
                            if(i != mainImageIndex){
                                int finalI = i;
                                        byte[] bytes = images.get(finalI);
                                        uploadPic(bytes);
                            }
                        }
                    });
                }, throwable -> {
                    Log.v("Image Upload", throwable.getMessage());
                });
                //todo return to partyOverview
            }
        });

    }


    private void uploadPic(byte[] bytes){
        String base64 = Base64.encodeToString(bytes, Base64.DEFAULT);
        Observable<Pictures> response = PartyPersistence.getInstance().uploadPartyPictures(p.getId(), base64);
        response.subscribeOn(Schedulers.io())
                .subscribe(responseBody -> getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Upload was successful", Toast.LENGTH_SHORT).show();
                        }), throwable -> {
                            getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Upload failure :(", Toast.LENGTH_SHORT).show());
                            Log.v("Image Upload", throwable.getMessage());
                        }



                );
    }
    private void askPermAndTakeImg(View clickedView) {
        //Checking for camera permissions
        if (ContextCompat.checkSelfPermission(
                getContext(), Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            takePhotoActivity.launch(takeImageUri);
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }
}
