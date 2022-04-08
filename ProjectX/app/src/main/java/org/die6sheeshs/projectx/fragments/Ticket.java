package org.die6sheeshs.projectx.fragments;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.die6sheeshs.projectx.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Ticket#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Ticket extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;

    public Ticket() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Ticket.
     */
    // TODO: Rename and change types and number of parameters
    public static Ticket newInstance(String param1, String param2) {
        Ticket fragment = new Ticket();
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
        this.view = inflater.inflate(R.layout.fragment_ticket, container, false);
        init();
        return view;
    }

    private void init(){
        ScrollView scrollView = view.findViewById(R.id.scroll);
        LinearLayout linearLayoutV = view.findViewById(R.id.linlayV);
        TextView header = view.findViewById(R.id.header);


        for(int i = 0;i<5;i++){

            ImageView iv = new ImageView(getContext());
            LinearLayout layoutH = new LinearLayout(getContext());
            layoutH.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layoutH.setOrientation(LinearLayout.HORIZONTAL);
            linearLayoutV.addView(layoutH);

            LinearLayout layoutV2 = new LinearLayout(getContext());
            layoutV2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layoutV2.setOrientation(LinearLayout.VERTICAL);
            layoutH.addView(layoutV2);
            layoutH.addView(iv);
            layoutH.setPadding(0,0,0,10);

            TextView tv1 = new TextView(getContext());
            tv1.setText("Location");
            tv1.setTypeface(tv1.getTypeface(), Typeface.BOLD);
            TextView tv2 = new TextView(getContext());
            int price = 30;
            tv2.setText(price+"€");
            TextView tv3 = new TextView(getContext());
            tv3.setText("07.04.2022");

            layoutV2.addView(tv1);
            layoutV2.addView(tv2);
            layoutV2.addView(tv3);

            layoutH.setOnClickListener((l)-> header.setText("Your Tickets"));
        }
    }
}