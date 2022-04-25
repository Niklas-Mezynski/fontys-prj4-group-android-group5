package org.die6sheeshs.projectx.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.entities.Ticket;
import org.die6sheeshs.projectx.entities.TicketRequest;
import org.die6sheeshs.projectx.entities.User;
import org.die6sheeshs.projectx.restAPI.TicketPersistence;
import org.die6sheeshs.projectx.restAPI.TicketRequestPersistence;
import org.die6sheeshs.projectx.restAPI.UserPersistence;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.NoSuchElementException;

import io.reactivex.Observable;

public class TicketRequestListItem extends Fragment {

    private View view;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String INDEX_PARAM = "list_index";
    private User user;
    private TicketRequest ticketRequest;

    // TODO: Rename and change types of parameters
    private String mParam1;

    public TicketRequestListItem(TicketRequest ticketRequest) {
        if (ticketRequest == null) {
            this.ticketRequest = new TicketRequest(LocalDateTime.of(2022, 4, 14, 13, 58, 27), "12", "def");
        } else {
            this.ticketRequest = ticketRequest;
        }
        try {
            user = UserPersistence.getInstance().getUserData(this.ticketRequest.getUserId()).blockingFirst();
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
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

        initView(ticketRequest, user);

        // Inflate the layout for this fragment
        return view;
    }

    private void initView(TicketRequest ticketRequest, User user) {
        initAcceptButton(view);
        initDeclineButton(view);
        setRequestData(view);
    }

    private void setRequestData(View v) {
        setFirstName(v, user.getFirstName());
        setLastName(v, user.getLastName());
        setAboutMe(v, user.getAbout_me());
        setProfilePicture(v, user.getProfile_pic());
    }

    private void setFirstName(View v, String firstName) {
        TextView fName = (TextView) v.findViewById(R.id.firstName);
        fName.setText(firstName);
    }

    private void setLastName(View v, String lastName) {
        TextView lName = (TextView) v.findViewById(R.id.lastName);
        lName.setText(lastName);
    }

    private void setAboutMe(View v, String aboutMe) {
        TextView aMe = (TextView) v.findViewById(R.id.aboutMe);
        aMe.setText(aboutMe);
    }

    private void setProfilePicture(View v, String pictureURL) {
        ImageView profile_pic = (ImageView) v.findViewById(R.id.image_profilepic);
        Picasso.get().load(pictureURL).into(profile_pic);
    }

    private void initAcceptButton(View v) {
        /*Button accept = (Button) v.findViewById(R.id.acceptButton);
        accept.setOnClickListener(new View.OnClickListener() {
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
                    System.out.println(newTicket);
                } catch (Exception e) {
                    Log.w("Error", e.getMessage());
                }
                Log.w("Info", "Request accepted!");
            }
        });*/
    }

    private void initDeclineButton(View v) {
        /*Button decline = (Button) v.findViewById(R.id.declineButton);
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new UnsupportedOperationException("Sharing with friends not supported yet!").printStackTrace();
                TicketRequestPersistence.getInstance().deleteTicketRequest(ticketRequest.getUserId(), ticketRequest.getPartyId());
                Log.w("Info","Request declined!");
            }
        });*/
    }
}
