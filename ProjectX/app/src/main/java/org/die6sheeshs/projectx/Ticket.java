package org.die6sheeshs.projectx;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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
        init();
        return inflater.inflate(R.layout.fragment_ticket, container, false);
    }

    private void init(){
        ScrollView scrollView = getView().findViewById(R.id.scroll);
        LinearLayout linearLayoutV = getView().findViewById(R.id.linlayV);


        for(int i = 0;i<1;i++){
            LinearLayout layout2 = new LinearLayout(getContext());

            layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layout2.setOrientation(LinearLayout.HORIZONTAL);
            linearLayoutV.addView(layout2);

            TextView tv1 = new TextView(getContext());
            tv1.setText("HALLO");
            TextView tv2 = new TextView(getContext());
            tv2.setText("HALLO1");
            TextView tv3 = new TextView(getContext());
            tv3.setText("HALLO2");
            TextView tv4 = new TextView(getContext());
            tv4.setText("HALLO3");

            layout2.addView(tv1);
            layout2.addView(tv2);
            layout2.addView(tv3);
            layout2.addView(tv4);
        }
    }
}