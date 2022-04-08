package org.die6sheeshs.projectx.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import org.die6sheeshs.projectx.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateParty#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateParty extends Fragment {

    private View view;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreateParty() {
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
    public static CreateParty newInstance(String param1, String param2) {
        CreateParty fragment = new CreateParty();
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
        view = inflater.inflate(R.layout.fragment_create_party, container, false);
        init();
        return view;
    }

    private void init() {
        ScrollView scrollView = view.findViewById(R.id.party_list_scroll_view);
        LinearLayout linearLayout = view.findViewById(R.id.party_list_linear_layout);

        FragmentManager fragMan = getChildFragmentManager();
        FragmentTransaction fragTransaction = fragMan.beginTransaction();

        for (int i = 0; i < 5; i++) {
            Fragment fragment = new PartyListItem();
//            ((PartyListItem)fragment).setLocation("Deine Mums Haus"+i);

            fragTransaction.add(linearLayout.getId(), fragment, "party#" + i);
        }

        fragTransaction.commit();
//        TextView textView = new TextView(view.getContext());
//        textView.setLayoutParams(linearLayout.getLayoutParams());
//        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//        textView.setText("No parties planned yet");
//
//        linearLayout.addView(textView);
    }

    private int convertPixelToDP(int pixels) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixels, getResources().getDisplayMetrics());
    }

}