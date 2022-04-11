package org.die6sheeshs.projectx.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.activities.MainActivity;
import org.die6sheeshs.projectx.entities.Party;

import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PartyListItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PartyListItem extends Fragment {

    private View view;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String INDEX_PARAM = "list_index";
    private Party party;

    // TODO: Rename and change types of parameters
    private String mParam1;

    public PartyListItem(Party party) {
        this.party = party;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param party Index of list item.
     * @return A new instance of fragment party_list_item.
     */
    // TODO: Rename and change types and number of parameters
    public static PartyListItem newInstance(Party party) {
        PartyListItem fragment = new PartyListItem(party);
        Bundle args = new Bundle();
        args.putString(INDEX_PARAM, "party");
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(INDEX_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_party_list_item, container, false);

        setLocation(party.getName());

        initButton();

        return view;
    }

    private void initButton() {
        LinearLayout wrapper = view.findViewById(R.id.linearLayout_wrapper);
        wrapper.setOnClickListener(view -> {
            Fragment frag = new PartyDetail(UUID.randomUUID());
            ((MainActivity)getActivity()).replaceFragment(frag);
        });
    }

    private void setLocation(String location) {
        TextView locationTextView = (TextView) view.findViewById(R.id.textView_location);
        locationTextView.setText(location);
    }

    private void setPrice(Double )

}
