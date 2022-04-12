package org.die6sheeshs.projectx.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.activities.MainActivity;
import org.die6sheeshs.projectx.entities.Party;
import org.die6sheeshs.projectx.restAPI.PartyPersistence;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PartyOverview#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PartyOverview extends Fragment {

    private View view;
    private PartyPersistence partyPersistence = PartyPersistence.getInstance();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PartyOverview() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateParty.
     */
    // TODO: Rename and change types and number of parameters
    public static PartyOverview newInstance(String param1, String param2) {
        PartyOverview fragment = new PartyOverview();
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

        view = inflater.inflate(R.layout.fragment_party_overview, container, false);
        initializeList();
        initializeCreateButton();

        return view;
    }

    private void initializeCreateButton() {
        Button createPartyButton = view.findViewById(R.id.button_new_party);
        createPartyButton.setOnClickListener(view -> {
            Fragment frag = new PartyCreate();
            ((MainActivity)getActivity()).replaceFragment(frag);
        });
    }

    private void initializeList() {
        LinearLayout linearLayout = view.findViewById(R.id.party_list_linear_layout);
        Observable<List<Party>> response = partyPersistence.getAllParties();

        response
                .subscribeOn(Schedulers.io())
                .subscribe(result -> {
                    getActivity().runOnUiThread(() -> {

                        FragmentManager fragMan = getChildFragmentManager();
                        FragmentTransaction fragTransaction = fragMan.beginTransaction();

                        for (Party p : result) {
                            System.out.println(p.getName() + ": " + p.getId());
                            View.OnClickListener buttonAction = view -> {
                                Fragment frag = new PartyDetail(p.getId());
                                ((MainActivity)getActivity()).replaceFragment(frag);
                            };
                            Fragment fragment = new PartyListItem(p, buttonAction);

                            fragTransaction.add(linearLayout.getId(), fragment, "party#" + p.getId());
                        }

                        fragTransaction.commit();

                    });
                }, error -> {
//                    TextView textView = new TextView(view.getContext());
//                    textView.setLayoutParams(linearLayout.getLayoutParams());
//                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                    textView.setText("No parties planned yet");
//
//                    linearLayout.addView(textView);
                    Log.v("Getting List of parties", "All Parties GET error: " + error.getMessage());
                });
    }

}