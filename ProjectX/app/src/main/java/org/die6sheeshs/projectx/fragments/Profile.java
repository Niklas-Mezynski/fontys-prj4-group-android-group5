package org.die6sheeshs.projectx.fragments;

import static androidx.core.content.FileProvider.getUriForFile;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.entities.User;
import org.die6sheeshs.projectx.helpers.SessionManager;
import org.die6sheeshs.projectx.restAPI.UserPersistence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Consumer;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

        File newFile = new File(getContext().getExternalFilesDir("my_images"), "test123.jpg");
        this.takeImageUri = getUriForFile(getContext(), "org.die6sheeshs.projectx.fileprovider", newFile);
        takePhotoActivity = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
            //Do something with the image after a picture was taken
//            imageViewForUpload.setImageURI(takeImageUri);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        imageView = view.findViewById(R.id.imageView);
        tv_firstName = view.findViewById(R.id.tv_profile_firstname);
        tv_lastName = view.findViewById(R.id.tv_profile_lastname);
        tv_email = view.findViewById(R.id.tv_profile_email);
        tv_nickname = view.findViewById(R.id.tv_profile_nickname);

        initProfileData();
        downloadProfilePicture();

        //Take picture action
//        pictureButton.setOnClickListener(view1 -> {
//            ((MainActivity) getActivity()).requestCameraPermission();
//            takePhotoActivity.launch(takeImageUri);
//        });

        //Upload action
//        uploadButton.setOnClickListener(this::onClick);

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
                    });
                }, (error) -> Log.e("Get user data in profile", error.getMessage()));
    }

    private void downloadProfilePicture() {
        String id = SessionManager.getInstance().getUserId();
        Observable<ResponseBody> imageRequest = UserPersistence.getInstance().downloadProfilePic(id);

        imageRequest.subscribeOn(Schedulers.io())
                .subscribe(response -> {
                    Log.v("File download", "Server has file");
                    boolean success = writeResponseBodyToDisk(response, "profile_pic.png", imageView, (file) -> getActivity().runOnUiThread(() -> imageView.setImageURI(Uri.fromFile(file))));
                    Log.v("File download", "Successfully converted file");
                }, (error) -> Log.v("File download", error.getMessage()));

    }

    private void uploadPicture() {
        String id = SessionManager.getInstance().getUserId();
        File file = new File(getContext().getExternalFilesDir("my_images"), "test123.jpg");
        if (!file.exists()) {
            Log.v("File upload", "File doesn't exist.");
            return;
        }

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part imageBody = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        Observable<ResponseBody> uploadResponse = UserPersistence.getInstance().uploadPicture(id, imageBody);

        uploadResponse.subscribeOn(Schedulers.io())
                .subscribe(responseBody -> {
                    Log.v("Upload successful", "Profile picture update");
                }, error -> Log.v("Upload error", error.getMessage()));
    }

    private boolean writeResponseBodyToDisk(ResponseBody bodyWithFile, String filename, ImageView imageView, Consumer<File> func) {
        try {
            File file = new File(view.getContext().getFilesDir(), filename);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
//                byte[] fileReader = new byte[8192];
                byte[] fileReader = new byte[4096];

                inputStream = bodyWithFile.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

//                    Log.d("File success", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }
//                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                outputStream.close();
//                getActivity().runOnUiThread(() -> imageView.setImageURI(Uri.fromFile(file)));
                func.accept(file);
                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    private void onClick(View view1) {
        uploadPicture();
    }
}