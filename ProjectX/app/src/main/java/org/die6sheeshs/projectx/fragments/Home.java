package org.die6sheeshs.projectx.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.activities.MainActivity;
import org.die6sheeshs.projectx.entities.EventWithLocation;
import org.die6sheeshs.projectx.restAPI.PartyPersistence;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private PartyPersistence partyPersistence = PartyPersistence.getInstance();
    private SeekBar seekBar;
    private MainActivity mainActivity;
    private LinearLayout linearLayout;
    private TextView tv_kilometers;

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your app.
                    updateLocation(seekBar.getProgress());
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                    Toast.makeText(getContext(), "Cannot list nearby parties without having location permissions", Toast.LENGTH_SHORT).show();
                }
            });

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_home, container, false);
        init();
        return view;
    }

    public void init() {
        //ScrollView scrollView = view.findViewById(R.id.);
        linearLayout = view.findViewById(R.id.linlayVHome);
        seekBar = view.findViewById(R.id.seekBarHome);
        tv_kilometers = view.findViewById(R.id.tv_kilometers);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tv_kilometers.setText(seekBar.getProgress() + " km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tv_kilometers.setText(seekBar.getProgress() + " km");
                linearLayout.removeAllViews();
                updateLocation(seekBar.getProgress());
            }
        });

        this.mainActivity = (MainActivity) getActivity();

        //Checking for location permissions
        if (ContextCompat.checkSelfPermission(
                getContext(), Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            updateLocation(seekBar.getProgress());
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }

//        updateLocation(5);
    }

    private void updateLocation(int radius) {
        mainActivity.getAndConsumeLastLocation(location -> listParties(location, radius));
    }


    private void listParties(Location userLocation, int radius) {
        Observable<List<EventWithLocation>> response = partyPersistence.getPartiesByLocation(userLocation.getLatitude(), userLocation.getLongitude(), radius);
        response.subscribeOn(Schedulers.io())
                .subscribe(eventWithLocations -> {
                    getActivity().runOnUiThread(() -> {

                        FragmentManager fragmentManager = getChildFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        for (EventWithLocation p : eventWithLocations) {
                            //System.out.println(p.getName() + ": " + p.getId());
                            View.OnClickListener buttonAction = view -> {
                                Fragment frag = new PartyDetail(p.getId());
                                (mainActivity).replaceFragment(frag);
                            };
                            Fragment fragment = new PartyListItem(p, buttonAction, userLocation);

                            fragmentTransaction.add(linearLayout.getId(), fragment, "party#" + p.getId());
                        }

                        fragmentTransaction.commit();

                    });
                }, (error) -> Log.v("Getting List of Parties", "Parties GET error: " + error.getMessage()));
    }
}