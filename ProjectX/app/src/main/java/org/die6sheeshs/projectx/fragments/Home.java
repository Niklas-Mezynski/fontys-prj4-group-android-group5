package org.die6sheeshs.projectx.fragments;

import static org.die6sheeshs.projectx.activities.MainActivity.PERMISSIONS_FINE_LOCATION;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.activities.MainActivity;
import org.die6sheeshs.projectx.entities.EventWithLocation;
import org.die6sheeshs.projectx.entities.Party;
import org.die6sheeshs.projectx.helpers.SessionManager;
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

        updateLocation(5);
    }

    private void updateLocation(int radius) {
        mainActivity.updateLocation(location -> listParties(location, radius));
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