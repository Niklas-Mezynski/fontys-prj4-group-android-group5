package org.die6sheeshs.projectx.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.entities.Ticket;
import org.die6sheeshs.projectx.helpers.SessionManager;
import org.die6sheeshs.projectx.restAPI.UserTicketPersistence;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Tickets#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tickets extends Fragment {

    UserTicketPersistence userTicketPersistence = UserTicketPersistence.getInstance();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;

    public Tickets() {
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
    public static Tickets newInstance(String param1, String param2) {
        Tickets fragment = new Tickets();
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
        String id = SessionManager.getInstance().getUserId();
        String jwt = SessionManager.getInstance().getToken();
        Observable<List<Ticket>> response = userTicketPersistence.getTickets(id,jwt);
        response.subscribeOn(Schedulers.io())
                .doOnError((error) -> Log.v("Getting List of tickets", "User Tickets GET error: " + error.getMessage()))
                .subscribe(tickets -> {
                    getActivity().runOnUiThread(()->{
                        for (Ticket t :tickets) {
                            //for each ticket get the party id then fetch information from party

                        }
                    });
                });
    }
}