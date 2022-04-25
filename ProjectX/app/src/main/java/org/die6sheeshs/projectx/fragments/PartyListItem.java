package org.die6sheeshs.projectx.fragments;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.activities.MainActivity;
import org.die6sheeshs.projectx.entities.EventLocation;
import org.die6sheeshs.projectx.entities.EventWithLocation;
import org.die6sheeshs.projectx.entities.Party;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private View.OnClickListener buttonAction;
    private Location userLocation;

    // TODO: Rename and change types of parameters
    private String mParam1;

    public PartyListItem(Party party, View.OnClickListener buttonAction) {
        this.party = party;
        this.buttonAction = buttonAction;
    }

    public PartyListItem(Party party, View.OnClickListener buttonAction, Location userLocation) {
        this.party = party;
        this.buttonAction = buttonAction;
        this.userLocation = userLocation;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param party Index of list item.
     * @return A new instance of fragment party_list_item.
     */
    // TODO: Rename and change types and number of parameters
    public static PartyListItem newInstance(Party party, View.OnClickListener buttonAction) {
        PartyListItem fragment = new PartyListItem(party, buttonAction);
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

        initContent(party, userLocation);

        return view;
    }

    private void initContent(Party party, Location location) {
        setButtonAction();
        setLocation(party.getName());
        setPrice(0D);
        setStartDate(party.getStart());
        setEndDate(party.getEnd());
        setDistance(party, userLocation);
    }

    private void setButtonAction() {
        LinearLayout wrapper = view.findViewById(R.id.linearLayout_wrapper);
//        wrapper.setOnClickListener(view -> {
//            Fragment frag = new PartyDetail(party.getId());
//            ((MainActivity)getActivity()).replaceFragment(frag);
//        });
        wrapper.setOnClickListener(buttonAction);
    }

    private void setLocation(String location) {
        TextView locationTextView = (TextView) view.findViewById(R.id.textView_location);
        locationTextView.setText(location);
    }

    private void setPrice(Double price) {
        TextView priceTextView = (TextView) view.findViewById(R.id.textView_price);
        priceTextView.setText(String.format("%.2f €", price));
    }

    private void setStartDate(LocalDateTime startDate) {
        TextView startDateTextView = (TextView) view.findViewById(R.id.textView_startDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        startDateTextView.setText(startDate.format(formatter));
    }

    private void setEndDate(LocalDateTime endDate) {
        TextView endDateTextView = (TextView) view.findViewById(R.id.textView_endDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        if (endDate == null) {
            endDateTextView.setText("Open-End");
        } else {
            endDateTextView.setText(endDate.format(formatter));
        }
    }

    private void setDistance(Party party, Location userLocation) {
        if (userLocation == null) {
            return;
        }
        float distance;
        float[] result = new float[3];
        if (party instanceof EventWithLocation) {
            EventWithLocation eventWithLocation = (EventWithLocation) party;
            Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(), eventWithLocation.getLatitude(), eventWithLocation.getLongitude(), result);
            distance = result[0];
        } else {
            if (party.getEventLocation() == null)
                return;
            Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(), party.getEventLocation().getLatitude(), party.getEventLocation().getLongtitude(), result);
            distance = result[0];
        }

        TextView tv_distance = (TextView) view.findViewById(R.id.tv_distance);
        tv_distance.setVisibility(View.VISIBLE);
        int roundedDistKm = Math.round(distance / 1000);
        tv_distance.setText(roundedDistKm + "km");
    }

}
