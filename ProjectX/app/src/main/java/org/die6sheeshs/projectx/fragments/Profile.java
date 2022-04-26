package org.die6sheeshs.projectx.fragments;

import static androidx.core.content.FileProvider.getUriForFile;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.entities.User;
import org.die6sheeshs.projectx.helpers.SessionManager;
import org.die6sheeshs.projectx.restAPI.UserPersistence;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    static final int REQUEST_IMAGE_CAPTURE = 187;

    private static final String fileName = "cameraProfilePic.jpg";

    // TODO: Rename and change types of parameters
    private ActivityResultLauncher<Uri> takePhotoActivity;
    private Uri takeImageUri;
    private String mParam1;
    private String mParam2;
    private View view;
    private ImageView imageView;
    private TextView tv_firstName;
    private TextView tv_lastName;
    private TextView tv_email;
    private TextView tv_nickname;
    private AppCompatImageButton uploadPicture;
    private AppCompatImageButton cancelUpload;
    private TabLayout tabLayout;
    private ViewPager profileViewPager;

    //Register the callback (action to perform) when user answered the permission request
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


    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
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

        File newFile = new File(getContext().getExternalFilesDir("my_images"), fileName);
        this.takeImageUri = getUriForFile(getContext(), "org.die6sheeshs.projectx.fileprovider", newFile);
        takePhotoActivity = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
            //Do something with the image after a picture was taken
            if (result && imageView != null) {
                imageView.setImageURI(takeImageUri);
                uploadPicture.setVisibility(View.VISIBLE);
                cancelUpload.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        //Assign all the views
        imageView = view.findViewById(R.id.imageView);
        tv_firstName = view.findViewById(R.id.tv_profile_firstname);
        tv_lastName = view.findViewById(R.id.tv_profile_lastname);
        tv_email = view.findViewById(R.id.tv_profile_email);
        tv_nickname = view.findViewById(R.id.tv_profile_nickname);
        uploadPicture = view.findViewById(R.id.button_uploadProfilePicture);
        cancelUpload = view.findViewById(R.id.button_retakeProfilePicture);

        //Add listeners

        //Listener for taking an image
        imageView.setOnClickListener(this::askPermAndTakeImg);
        //Listener for uploading the taken image
        uploadPicture.setOnClickListener(this::upload);
        //Listener for cancelling the profile picture update
        cancelUpload.setOnClickListener(view1 -> {
            initProfileData();
        });

        //Display the Users data (and profile pic) in the fragment
        initProfileData();

        initTabs();

        return view;
    }

    private void initProfileData() {
        User user = SessionManager.getInstance().getUser();
        tv_firstName.setText(user.getFirstName());
        tv_lastName.setText(user.getLastName());
        tv_email.setText(user.getEmail());
        tv_nickname.setText(user.getNick_name());
        if (user.getProfile_pic() != null || !user.getProfile_pic().isEmpty()) {
            displayProfilePicture(user.getProfile_pic());
        }
    }

    private void displayProfilePicture(String base64) {
        //Convert base64 string into a byte array and then into a bitmap in order to set it to the imageView
        Schedulers.io().scheduleDirect(() -> {
            byte[] decode = Base64.decode(base64, Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(decode, 0, decode.length);
            if (bmp == null) {
                return;
            }
            getActivity().runOnUiThread(() -> imageView.setImageBitmap(bmp));
        });
//        getActivity().runOnUiThread(() -> imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, imageView.getWidth(), imageView.getHeight(), false)));
    }

    private void uploadPicture(String fileName) {
        //TODO Run that on a separate thread
        Schedulers.io().scheduleDirect(() -> {
            String id = SessionManager.getInstance().getUserId();
            //Reading the file
            File file = new File(getContext().getExternalFilesDir("my_images"), fileName);
            byte[] bytes;
            //Converting it into a byte array
            try {
                bytes = Files.readAllBytes(file.toPath());
            } catch (IOException e) {
                Toast.makeText(getContext(), "Upload failure :(", Toast.LENGTH_SHORT).show();
                Log.e("File upload", e.getMessage());
                return;
            }
            //Encode to base64 and send it to the restAPI
            String base64 = Base64.encodeToString(bytes, Base64.DEFAULT);

            Observable<ResponseBody> response = UserPersistence.getInstance().uploadPicture(id, base64);
            response.subscribeOn(Schedulers.io())
                    .subscribe(responseBody -> getActivity().runOnUiThread(() -> {
                                uploadPicture.setVisibility(View.GONE);
                                cancelUpload.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Upload was successful", Toast.LENGTH_SHORT).show();
                            })
                            , throwable -> getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Upload failure :(", Toast.LENGTH_SHORT).show()));
        });
    }

    private void upload(View clickedView) {
        uploadPicture(fileName);
    }

    private void initTabs() {
        tabLayout = view.findViewById(R.id.profile_tabLayout);
        profileViewPager = view.findViewById(R.id.profile_viewPager);

        final ProfileViewPagerAdapter profileViewPagerAdapter = new ProfileViewPagerAdapter(getActivity().getSupportFragmentManager());

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                profileViewPagerAdapter.addFragment(ProfileFriendsTab.getInstance(), "Friends");
                profileViewPagerAdapter.addFragment(ProfileSearchTab.getInstance(), "Search");
                profileViewPagerAdapter.addFragment(ProfileNotificationsTab.getInstance(), "Notifications");
                profileViewPager.setAdapter(profileViewPagerAdapter);
                tabLayout.setupWithViewPager(profileViewPager);
            }
        });
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