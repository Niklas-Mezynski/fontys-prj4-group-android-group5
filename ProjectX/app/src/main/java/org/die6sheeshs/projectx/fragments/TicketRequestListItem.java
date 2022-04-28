package org.die6sheeshs.projectx.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.entities.Party;
import org.die6sheeshs.projectx.entities.Ticket;
import org.die6sheeshs.projectx.entities.TicketRequest;
import org.die6sheeshs.projectx.entities.User;
import org.die6sheeshs.projectx.helpers.ImageConversion;
import org.die6sheeshs.projectx.restAPI.PartyPersistence;
import org.die6sheeshs.projectx.restAPI.TicketPersistence;
import org.die6sheeshs.projectx.restAPI.TicketRequestPersistence;
import org.die6sheeshs.projectx.restAPI.UserPersistence;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.NoSuchElementException;
import java.util.Vector;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class TicketRequestListItem extends Fragment {

    private View view;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String INDEX_PARAM = "list_index";
    private User user;
    private Party party;
    private TicketRequest ticketRequest;
    private LinearLayout layout_acceptDecline;

    // TODO: Rename and change types of parameters
    private String mParam1;

    public TicketRequestListItem() {}

    public TicketRequestListItem(TicketRequest ticketRequest) {
        if (ticketRequest == null) {
            Log.e("Error", "No TicketRequest given!");
        } else {
            this.ticketRequest = ticketRequest;
        }
        /*try {

            /*userObservable.subscribe(user1 -> {
                    Log.println(Log.INFO, "!!!USER-OBSERVABLE!!!", user1.toString());
            });
        } catch (NoSuchElementException e) {
            Log.println(Log.ERROR, "NoSuchUser", e.getMessage());
        }*/
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param ticketRequest TicketRequest.
     * @return A new instance of fragment CreateParty.
     */
    // TODO: Rename and change types and number of parameters
    public static TicketRequestListItem newInstance(TicketRequest ticketRequest) {
        TicketRequestListItem fragment = new TicketRequestListItem(ticketRequest);
        Bundle args = new Bundle();
        // args.putString(ARG_PARAM1, param1);
        // args.putString(ARG_PARAM2, param2);
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
        view = inflater.inflate(R.layout.fragment_party_request_list_item, container, false);

        initView(ticketRequest);
        layout_acceptDecline = view.findViewById(R.id.layout_acceptDecline);

        // Inflate the layout for this fragment
        return view;
    }

    private void initParty() {
        Observable<Party> partyObservable = PartyPersistence.getInstance().getParty(this.ticketRequest.getPartyId());
        partyObservable.subscribeOn(Schedulers.io())
                .subscribe(party1 -> {
                    this.party = party1;
                    // Log.println(Log.INFO,"PartyMain", this.party.toString());
                    getActivity().runOnUiThread(() -> {
                        setRequestData(view);
                    });
                }, error -> Log.e("PartyMainError", error.getMessage()));
    }

    private void initView(TicketRequest ticketRequest) {
        Observable<User> userObservable = UserPersistence.getInstance().getUserData(this.ticketRequest.getUserId());
        userObservable.subscribeOn(Schedulers.io())
                .subscribe(user1 -> {
                    this.user = user1;
                    //Log.println(Log.INFO,"UserMain", this.user.toString());
                    getActivity().runOnUiThread(() -> {
                        initAcceptButton(view);
                        initDeclineButton(view);
                        initParty();
                    });
                }, error -> Log.e("UserMainError", error.getMessage()));
    }

    private void setRequestData(View v) {
        setFirstName(v, user.getFirstName());
        setLastName(v, user.getLastName());
        setProfilePicture(v, user.getProfile_pic());
        setPartyName(v, party.getName());
    }

    private void setFirstName(View v, String firstName) {
        TextView fName = (TextView) v.findViewById(R.id.firstName);
        fName.setText("Firstname: "+firstName);
    }

    private void setLastName(View v, String lastName) {
        TextView lName = (TextView) v.findViewById(R.id.lastName);
        lName.setText("Lastname: "+lastName);
    }

    private void setProfilePicture(View v, String pictureURL) {
        ImageView profile_pic = (ImageView) v.findViewById(R.id.image_profilepic);
        //Picasso.get().load(pictureURL).into(profile_pic);
        /*byte[] decodedString = Base64.getDecoder().decode(pictureURL);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);*/
        Bitmap decodedPic = ImageConversion.base64ToBitmap(pictureURL);
        profile_pic.setImageBitmap(decodedPic);
    }

    private void setPartyName(View v, String partyName) {
        TextView pName = (TextView) v.findViewById(R.id.partyName);
        pName.setText("Party: "+partyName);
    }

    private void initAcceptButton(View v) {
        ImageView imageView_accept = (ImageView) v.findViewById(R.id.imageView_acceptButton);
        imageView_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new UnsupportedOperationException("Sharing with friends not supported yet!").printStackTrace();
                try {
                    byte[] bytesOfUserPartyId = (ticketRequest.getUserId()+ticketRequest.getPartyId()).getBytes("UTF-8");
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    String newTicketId = Base64.getEncoder().encodeToString(md.digest(bytesOfUserPartyId));
                    Ticket newTicket = new Ticket(newTicketId, ticketRequest.getPartyId(), ticketRequest.getUserId());
                    // TODO: Implement createTicket in TicketPersistence and uncomment afterwards
                    // TicketPersistence.getInstance().createTicket(newTicket);
                    Observable<Integer> resp = TicketRequestPersistence.getInstance().deleteTicketRequest(ticketRequest.getUserId(), ticketRequest.getPartyId());
                    resp.subscribeOn(Schedulers.io()).subscribe(deletedCount -> {
                        Log.i("Deleted TicketRequests", ""+deletedCount);
                    });
                    Log.i("New Ticket", newTicket.toString());
                } catch (Exception e) {
                    Log.w("Error", e.getMessage());
                }
                Log.w("Info", "Request accepted!");
            }
        });
    }

    private void initDeclineButton(View v) {
        ImageView imageView_decline = (ImageView) v.findViewById(R.id.imageView_declineButton);
        imageView_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new UnsupportedOperationException("Sharing with friends not supported yet!").printStackTrace();
                Observable<Integer> resp = TicketRequestPersistence.getInstance().deleteTicketRequest(ticketRequest.getUserId(), ticketRequest.getPartyId());
                resp.subscribeOn(Schedulers.io()).subscribe(deletedCount -> {
                    Log.i("Deleted TicketRequests", ""+deletedCount);
                });
                Log.w("Info","Request declined!");
            }
        });
    }
}
