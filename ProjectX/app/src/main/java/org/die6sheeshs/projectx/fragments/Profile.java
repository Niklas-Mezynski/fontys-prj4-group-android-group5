package org.die6sheeshs.projectx.fragments;

import static android.app.Activity.RESULT_OK;

import static androidx.core.content.FileProvider.getUriForFile;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.activities.MainActivity;
import org.die6sheeshs.projectx.helpers.SessionManager;
import org.die6sheeshs.projectx.restAPI.UserPersistence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import io.reactivex.Completable;
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
    private String mParam1;
    private String mParam2;
    private ImageView imageView;
    private ImageView imageViewForUpload;
    private View view;
    private Button pictureButton;
    private Button uploadButton;
    private ActivityResultLauncher<Uri> takePhotoActivity;
    private Uri takeImageUri;

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
            Log.v("Snens", result.toString());
            imageViewForUpload.setImageURI(takeImageUri);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        imageView = view.findViewById(R.id.imageView);
        imageViewForUpload = view.findViewById(R.id.imageViewForUpload);
        pictureButton = view.findViewById(R.id.button_take_picture);
        uploadButton = view.findViewById(R.id.button_upload_pic);

        downloadPicture();

        pictureButton.setOnClickListener(view1 -> {
            ((MainActivity) getActivity()).requestCameraPermission();
            takePhotoActivity.launch(takeImageUri);
        });

        uploadButton.setOnClickListener(this::onClick);

        return view;
    }

    public void downloadPicture() {
        String id = SessionManager.getInstance().getUserId();
        Observable<ResponseBody> imageRequest = UserPersistence.getInstance().downloadProfilePic(id);

        imageRequest.subscribeOn(Schedulers.io())
                .subscribe(response -> {
                    Log.v("File download", "Server has file");
                    boolean success = writeResponseBodyToDisk(response, "snens.png", imageView);
                    Log.v("File download", "Successfully converted file");
                }, (error) -> Log.v("File download", error.getMessage()));

    }

    public void uploadPicture() {
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

    private boolean writeResponseBodyToDisk(ResponseBody bodyWithFile, String filename, ImageView imageView) {
        try {
            // todo change the file location/name according to your needs
//            File file = new File(getExternalFilesDir(null) + File.separator + "Future Studio Icon.png");


            File file = new File(view.getContext().getFilesDir(), filename);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                inputStream = bodyWithFile.byteStream();
                outputStream = new FileOutputStream(file);
//                outputStream = view.getContext().openFileOutput("snens.png", Context.MODE_PRIVATE);

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
//                Log.v("File location", file.toString());
                getActivity().runOnUiThread(() -> imageView.setImageURI(Uri.fromFile(file)));

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