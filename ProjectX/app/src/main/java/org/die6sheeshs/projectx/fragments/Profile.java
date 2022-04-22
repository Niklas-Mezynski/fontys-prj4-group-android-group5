package org.die6sheeshs.projectx.fragments;

import static androidx.core.content.FileProvider.getUriForFile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.activities.MainActivity;
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

        File newFile = new File(getContext().getExternalFilesDir("my_images"), "cameraProfilePic.jpg");
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
        imageView.setOnClickListener(clickedView -> {
            ((MainActivity) getActivity()).requestCameraPermission();
            takePhotoActivity.launch(takeImageUri);

        });

        uploadPicture.setOnClickListener(this::upload);

        initProfileData();

        return view;
    }

    private void initProfileData() {
        Observable<User> userDataResponse = UserPersistence.getInstance().getUserData(SessionManager.getInstance().getUserId());
        userDataResponse.subscribeOn(Schedulers.io())
                .subscribe(user -> {
                    getActivity().runOnUiThread(() -> {
                        tv_firstName.setText(user.getFirstName());
                        tv_lastName.setText(user.getLastName());
                        tv_email.setText(user.getEmail());
                        tv_nickname.setText(user.getNick_name());
                        if (user.getProfile_pic() != null) {
                            displayProfilePicture(user.getProfile_pic());
                        }
                    });
                }, (error) -> Log.e("Get user data in profile", error.getMessage()));
    }

    private void displayProfilePicture(String base64) {
        //Convert base64 string into a byte array and then into a bitmap in order to set it to the imageView
        byte[] decode = Base64.decode(base64, Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        getActivity().runOnUiThread(() -> imageView.setImageBitmap(bmp));
//        getActivity().runOnUiThread(() -> imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, imageView.getWidth(), imageView.getHeight(), false)));

    }

    private void uploadPicture() {
        //TODO Run that on a separate thread
        Schedulers.io().scheduleDirect(() -> {
            String id = SessionManager.getInstance().getUserId();
            //Reading the file
            File file = new File(getContext().getExternalFilesDir("my_images"), "cameraProfilePic.jpg");
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
        uploadPicture();

    }
}