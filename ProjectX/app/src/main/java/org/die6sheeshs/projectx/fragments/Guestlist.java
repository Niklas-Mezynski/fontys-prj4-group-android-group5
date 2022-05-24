package org.die6sheeshs.projectx.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.entities.Party;
import org.die6sheeshs.projectx.entities.Ticket;
import org.die6sheeshs.projectx.entities.User;
import org.die6sheeshs.projectx.restAPI.PartyPersistence;
import org.die6sheeshs.projectx.restAPI.UserPersistence;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Guestlist#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Guestlist extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String partyID;
    private View view;
    private PartyPersistence partyPersistence = PartyPersistence.getInstance();
    private UserPersistence userPersistence = UserPersistence.getInstance();

    public Guestlist(String partyId) {
        partyID = partyId;
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Guestlist.
     */
    // TODO: Rename and change types and number of parameters
    public static Guestlist newInstance(String param1, String param2, String partyID) {
        Guestlist fragment = new Guestlist(partyID);
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
        view = inflater.inflate(R.layout.fragment_guestlist, container, false);
        initializeList();
        return view;
    }

    private void initializeList(){
        LinearLayout linearLayout = view.findViewById(R.id.guestListLinearLayout);

        List<String> userList = new ArrayList<>();

        Observable<List<Ticket>> response = partyPersistence.getTickets(partyID);
        response.subscribeOn(Schedulers.io())
                .subscribe(result -> {
                    for (Ticket t : result) {
                        Observable<User> response2 = userPersistence.getUserData(t.getUser_id());
                        response2.subscribeOn(Schedulers.io())
                                .subscribe(user -> {
                                    getActivity().runOnUiThread(() -> {
                                        FragmentManager fragMan = getChildFragmentManager();
                                        FragmentTransaction fragTransaction = fragMan.beginTransaction();

                                        Fragment fragment = new GuestlistItem(user);
                                        fragTransaction.add(linearLayout.getId(), fragment, "#user" + user.getId());
                                        fragTransaction.commit();
                                    });

                                });
                    }
                });
    }


//    Observable<List<Party>> response = partyPersistence.getAllParties();
//        response.subscribeOn(Schedulers.io())
//                .subscribe(result -> {
//        getActivity().runOnUiThread(() -> {
//            for (Party p : result) {
//                if (p.getId().equals(partyID)) {
////                                Fragment fragment = new GuestlistItem(p);
////                                fragTransaction.add(linearLayout.getId(), fragment, "guests");
//                    Observable<List>
//                }
//            }
//            fragTransaction.commit();
//        });
//    }, error -> {
//        Log.v("GuestListError", "ERROR!!!");
//    });









//    Observable<List<Ticket>> response = partyPersistence.getTickets(partyID);
//        response.subscribeOn(Schedulers.io())
//                .subscribe(result -> {
//
//        FragmentManager fragMan = getChildFragmentManager();
//        FragmentTransaction fragTransaction = fragMan.beginTransaction();
//        for (Ticket t : result) {
////                            Observable<User> responseUser = userPersistence.getUserData(t.getUser_id());
////                            responseUser.subscribeOn(Schedulers.io())
////                                    .subscribe(r -> {
////                                            GuestlistItem fragment = new GuestlistItem(r);
////                                            fragTransaction.add(linearLayout.getId(), fragment);
////                                            fragTransaction.commit();
////                                    });
//            Log.v("ERROR", "ID is: " + t.getUser_id());
//            Fragment fragment = new GuestlistItem();
//            fragTransaction.add(linearLayout.getId(), fragment);
//            fragTransaction.commit();
//        }
//        fragTransaction.commit();
//
//    }, error -> {
//        Log.v("GuestListError", error.getMessage());
//    });

}