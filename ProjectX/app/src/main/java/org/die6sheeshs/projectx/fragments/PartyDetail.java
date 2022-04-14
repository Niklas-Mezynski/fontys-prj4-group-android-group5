package org.die6sheeshs.projectx.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.activities.MainActivity;
import org.die6sheeshs.projectx.entities.Count;
import org.die6sheeshs.projectx.entities.EventLocation;
import org.die6sheeshs.projectx.entities.Party;
import org.die6sheeshs.projectx.entities.Ticket;
import org.die6sheeshs.projectx.entities.TicketRequest;
import org.die6sheeshs.projectx.entities.User;
import org.die6sheeshs.projectx.helpers.SessionManager;
import org.die6sheeshs.projectx.helpers.SimpleFuture;
import org.die6sheeshs.projectx.helpers.SimpleObservable;
import org.die6sheeshs.projectx.helpers.SimpleObserver;
import org.die6sheeshs.projectx.restAPI.PartyPersistence;
import org.die6sheeshs.projectx.restAPI.TicketPersistence;
import org.die6sheeshs.projectx.restAPI.TicketRequestPersistence;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;


public class PartyDetail extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String partyId;

    private static final int NO_TICKET = 0;
    private static final int TICKET_PENDING = 1;
    private static final int TICKET_ACCEPTED = 2;


    private View partyDetail;

    public PartyDetail(String partyID){
        partyId = partyID;
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
    public static PartyDetail newInstance(String param1, String param2, String partyId) {
        PartyDetail fragment = new PartyDetail(partyId);
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

        View v = inflater.inflate(R.layout.fragment_party_detail, container, false);
        initShareButton(v);
        initLocationImages(v);
        setPartyData(v);

        // Inflate the layout for this fragment
        return v;
    }


    private SimpleFuture<Integer> getTicketState(){//todo implement ticket state request

        SimpleFuture<Integer> returnValue = new SimpleFuture<Integer>();

        String userId = SessionManager.getInstance().getUserId();
        ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
                "Loading Tickets. Please wait...", true);
        Observable<List<Ticket>> ticketsOfUser = TicketPersistence.getInstance().getTickets(SessionManager.getInstance().getUserId(), "");
        ticketsOfUser.subscribeOn(Schedulers.io())
                .subscribe(tickets -> {
                    long countTickets = tickets.stream().filter(t -> t.getEvent_id().equals(partyId)&&t.getUser_id().equals(userId)).count();

                    if(countTickets > 0){
                        returnValue.setValue(TICKET_ACCEPTED);
                        //return TICKET_ACCEPTED;
                    }else{// else check if has request

                        Observable<List<TicketRequest>> ticketRequestsForParty = TicketRequestPersistence.getInstance().getTicketRequestsOfUser(SessionManager.getInstance().getUserId());
                        ticketRequestsForParty.subscribeOn(Schedulers.io())
                                .subscribe(ticketRequests -> {
                                    long countTicketRequests = ticketRequests.stream().filter(ticketRequest -> ticketRequest.getPartyId().equals(partyId)).count();
                                    if(countTicketRequests > 0){
                                        returnValue.setValue(TICKET_PENDING);
                                    }else{
                                        returnValue.setValue(NO_TICKET);

                                    }
                                }, error -> {
                                    returnValue.setValue(NO_TICKET);
                                    Log.v("Error retrieveing Tickets: ", error.getMessage());
                                });




                    }
                    dialog.dismiss();
                }, error -> {
                    dialog.dismiss();
                    returnValue.setValue(NO_TICKET);
                    Log.v("Error retrieveing Tickets: ", error.getMessage());
                });


        return returnValue;
    }


    private void setPartyData(View v) {
        int ticksAvailABC = 0;

        ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
                "Loading PartyData. Please wait...", true);

        Observable<Party> party = PartyPersistence.getInstance().getParty(partyId);
        party.subscribeOn(Schedulers.io())
                .subscribe(p ->{


                    Observable<List<Count>> countTickets = PartyPersistence.getInstance().getCountTicketsOfParty(partyId);
                    countTickets.subscribeOn(Schedulers.io())
                            .subscribe(counts -> {

                                Observable<User> userBelongingToEvent = PartyPersistence.getInstance().getOwner(partyId);
                                userBelongingToEvent.subscribeOn(Schedulers.io())
                                        .subscribe( eUser -> {


                                            getActivity().runOnUiThread(() -> {
                                                SimpleFuture<Integer> ticketStateFuture = getTicketState();
                                                ticketStateFuture.doActionWhenValueSet(ticketState -> {
                                                    initRequestButton(v, eUser.getId(), p.getMax_people() - counts.stream().findFirst().get().getCount(), ticketState);
                                                });
                                            });

                                            dialog.dismiss();
                                        },
                                                (error) -> Log.v("Party", "Party Error No User Found: "+ error.getMessage()));

                                getActivity().runOnUiThread(()->{
                                    int availTickets = -1;
                                    if(counts.size() == 1){
                                        Count first = counts.get(0);
                                        availTickets = first.getCount();
                                    }
                                    setTicksAvail(v, p.getMax_people() - availTickets);
                                });
                            },
                                    (error) -> Log.v("Party", "Party Error: " + error.getMessage()));




                    getActivity().runOnUiThread(() -> {

                        Log.v("Party Detail", "Details fetched :" + p.getDescription());
                        setPartyTitle(v, p.getName());
                        setMaxParticipants(v, p.getMax_people());
                        setPrice(v, p.getPrice());
                        setStart(v, p.getStart());
                        setEnd(v, p.getEnd());
                        setDescription(v, p.getDescription());

                        EventLocation eLoc = p.getEventLocation();

                        double lat;
                        lat = eLoc.getLatitude();
                        setPartyCity(v, lat + "");


                    });






                });





    }

    private void setDescription(View v, String s) {
        TextView title = (TextView) v.findViewById(R.id.descText);
        title.setText(s);
    }


    private void setEnd(View v, LocalDateTime end){
        TextView title = (TextView) v.findViewById(R.id.endText);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        title.setText(end.format(formatter));
    }

    private void setStart(View v, LocalDateTime start){
        TextView title = (TextView) v.findViewById(R.id.startText);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        title.setText(start.format(formatter));
    }

    private void setPrice(View v, double price) {
        TextView title = (TextView) v.findViewById(R.id.priceText);
        title.setText(""+price);
    }

    private void setTicksAvail(View v, int i) {
        TextView title = (TextView) v.findViewById(R.id.ticketsAvailText);
        title.setText(""+i);
    }

    private void setMaxParticipants(View v, int i) {
        TextView title = (TextView) v.findViewById(R.id.maxParticipantsText);
        title.setText(""+i);

    }

    private void setPartyStreetHouseNum(View v, String s, int houseNum) {
        TextView title = (TextView) v.findViewById(R.id.streetHou);
        title.setText(s+", "+houseNum);
    }

    private void setPartyCity(View v, String s) {
        TextView title = (TextView) v.findViewById(R.id.city);
        title.setText(s);
    }

    private void setPartyTitle(View v, String s) {
        TextView title = (TextView) v.findViewById(R.id.partyTitle);
        title.setText(s);
    }

    private void initLocationImages(View v) {
        ImageView curImg = (ImageView) v.findViewById(R.id.displayedLocationImage);
        Bitmap bmp = Bitmap.createBitmap(1920,1080, Bitmap.Config.ARGB_8888);
        for(int x = 0; x < 1919; x++){
            for(int y = 0; y < 1079; y++){
                bmp.setPixel(x,y, Color.rgb(x%256,y%256,0));
            }
        }
        curImg.setImageBitmap(bmp);
    }



    private void initShareButton(View v){
        Button share = (Button) v.findViewById(R.id.shareButton);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo share with friends
                new UnsupportedOperationException("Sharing with friends not supported yet!").printStackTrace();
            }
        });
    }



    private void initRequestButton(View v, String partyOwnerUUID, int availTickets, int ticketState){
        String currentUUID = SessionManager.getInstance().getUserId();
        Button btn = (Button) v.findViewById(R.id.sendReqBtn);
        System.out.println("Current User: "+ currentUUID);
        System.out.println("Party Owner: "+partyOwnerUUID);
        if(currentUUID.equals(partyOwnerUUID)){
            System.out.println("Party Owner is Current User");
            getActivity().runOnUiThread(()->{
                btn.setText("Cancel Party");
                btn.setBackgroundColor(Color.RED);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Cancel Party")
                                .setMessage("Do you really want to cancel your party?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        //todo cancel party
                                    }})
                                .setNegativeButton(android.R.string.no, null).show();
                    }
                });
                Button scanButton = (Button) v.findViewById(R.id.scanQRButton);
                scanButton.setVisibility(View.VISIBLE);
                initQRCodeScanButton(v);
            });


        }else{

            if(ticketState == NO_TICKET){
                if(availTickets > 0){

                    updateRequestButton(btn, ticketState);


                }else{
                    getActivity().runOnUiThread(()->{
                        btn.setBackgroundColor(Color.rgb(80,80,80));
                        btn.setText("Party is full");
                    });

                }
            }else{
                updateRequestButton(btn, ticketState);
            }


        }

    }

    private void updateRequestButton(Button btn, int ticketState){
        if(ticketState == NO_TICKET){
            getActivity().runOnUiThread(()->{
                btn.setText("Let me in!");
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postTicketRequest(view, btn);
                    }
                });
            });

        }else if(ticketState == TICKET_PENDING){
            getActivity().runOnUiThread(()->{
                btn.setText("Cancel Request");
                btn.setBackgroundColor(Color.rgb(80,80,80));
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cancelTicketRequest(view, btn);
                    }
                });
            });

        }else if(ticketState == TICKET_ACCEPTED){
            getActivity().runOnUiThread(()->{
                btn.setText("Show Ticket");
                btn.setBackgroundColor(Color.RED);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gotoTicket(view, btn);
                    }
                });
            });
        }
    }

    private void postTicketRequest(View v, Button requestButton){
        LocalDateTime createdOn = LocalDateTime.now();
        String userId = SessionManager.getInstance().getUserId();
        Observable<TicketRequest> tr = TicketRequestPersistence.getInstance().createTicketRequest(userId, partyId, createdOn);
            tr.subscribeOn(Schedulers.io())
                    .subscribe(ticketRequest -> {
                        getActivity().runOnUiThread(()->{
                            if(ticketRequest.getPartyId().equals(partyId) && ticketRequest.getUserId().equals(userId)){
                                updateRequestButton(requestButton, TICKET_PENDING);
                            }
                        });
                    });

    }

    private void cancelTicketRequest(View v, Button requestButton){
        String userId = SessionManager.getInstance().getUserId();
        Observable<List<TicketRequest>> t = TicketRequestPersistence.getInstance().getTicketRequestsOfUser(userId);
        t.subscribeOn(Schedulers.io())
                .subscribe(ticketRequests -> {
                    TicketRequest trForThisParty = ticketRequests.stream().filter(ticketRequest -> ticketRequest.getPartyId().equals(partyId)).findFirst().get();
                    if(trForThisParty != null){
                        //todo delete ticket request
                        

                    }else{
                        updateRequestButton(requestButton, NO_TICKET);
                    }
                },
                        (error) -> Log.v("Party", "Error on count"+error.getMessage()));

    }

    private void gotoTicket(View v, Button requestButton){
        getActivity().runOnUiThread(()->{
            Observable<Party> pObs = PartyPersistence.getInstance().getParty(partyId);
            pObs.subscribeOn(Schedulers.io())
                    .subscribe(party -> {
                        Observable<List<Ticket>> ticketsObs = TicketPersistence.getInstance().getTickets(SessionManager.getInstance().getUserId(), "");
                        ticketsObs.subscribeOn(Schedulers.io())
                                .subscribe(tickets -> {
                                    long numTickets = tickets.stream()
                                            .filter(ticket -> ticket.getEvent_id().equals(partyId)&&ticket.getUser_id().equals(SessionManager.getInstance().getUserId()))
                                            .count();
                                    if(numTickets != 1){
                                        throw new RuntimeException("Number of tickets must be 1!");
                                    }else{
                                        Ticket t = tickets.get(0);
                                        getActivity().runOnUiThread(()->{
                                            Fragment tDetail = new TicketDetail(party, t);
                                            ((MainActivity) getActivity()).replaceFragment(tDetail);
                                        });
                                    }
                                },error -> {
                                    Log.v("Error retrieving Tickets: ", error.getMessage());
                                });
                    }, error -> {
                        Log.v("Error retrieving party: ", error.getMessage());
                    });

        });

    }

    public void initQRCodeScanButton(View v){
        Button qrButton = v.findViewById(R.id.scanQRButton);
        //here you go lukas
    }

}
