package org.die6sheeshs.projectx.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.entities.Party;

import java.time.format.DateTimeFormatter;

public class TicketDetail extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private Party party;

    public TicketDetail(Party party) {
        // Required empty public constructor
        this.party = party;
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
    public static TicketDetail newInstance(String param1, String param2,Party party) {
        TicketDetail fragment = new TicketDetail(party);
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
        this.view = inflater.inflate(R.layout.fragment_ticket_detail, container, false);
        init();
        return view;
    }

    private void init(){
        TextView name = view.findViewById(R.id.nameT);
        name.setText(party.getName());
        TextView price = view.findViewById(R.id.priceT);
        price.setText("PRICE IN BACKEND MUST BE ADDED");
        TextView address = view.findViewById(R.id.addressT);
        address.setText("ADRESS IN BACKEND MUST BE ADDED");
        TextView start = view.findViewById(R.id.startT);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        start.setText(party.getStart().format(formatter));
        TextView end = view.findViewById(R.id.endT);
        end.setText(party.getEnd().format(formatter));
    }
}
