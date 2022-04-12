package org.die6sheeshs.projectx.fragments;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.activities.MainActivity;
import org.die6sheeshs.projectx.entities.Count;
import org.die6sheeshs.projectx.entities.EventLocation;
import org.die6sheeshs.projectx.entities.Party;
import org.die6sheeshs.projectx.entities.User;
import org.die6sheeshs.projectx.helpers.SessionManager;
import org.die6sheeshs.projectx.restAPI.PartyPersistence;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class PartyDetail extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String partyId;


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



    private void setPartyData(View v) {
        int ticksAvailABC = 0;

        Observable<Party> party = PartyPersistence.getInstance().getParty(partyId);
        party.subscribeOn(Schedulers.io())
                .doOnError((error) -> Log.v("Party", "Party Error: " + error.getMessage()))
                .subscribe(p ->{


                    Observable<List<Count>> countTickets = PartyPersistence.getInstance().getCountTicketsOfParty(partyId);
                    countTickets.subscribeOn(Schedulers.io())
                            .doOnError((error) -> Log.v("Party", "Error on count"+error.getMessage()))
                            .subscribe(counts -> {

                                Observable<User> userBelongingToEvent = PartyPersistence.getInstance().getOwner(partyId);
                                userBelongingToEvent.subscribeOn(Schedulers.io())
                                        .doOnError((error) -> Log.v("Party", "Party Error No User Found: "+ error.getMessage()))
                                        .subscribe( eUser -> {
                                            getActivity().runOnUiThread(() -> {
                                                initRequestButton(v, eUser.getId(), p.getMax_people() - counts.stream().findFirst().get().getCount());
                                            });
                                        });

                                getActivity().runOnUiThread(()->{
                                    int availTickets = -1;
                                    if(counts.size() == 1){
                                        Count first = counts.get(0);
                                        availTickets = first.getCount();
                                    }
                                    setTicksAvail(v, p.getMax_people() - availTickets);
                                });
                            });




                    getActivity().runOnUiThread(() -> {

                        Log.v("Party Detail", "Details fetched :" + p.getDescription());
                        setPartyTitle(v, p.getName());
                        setMaxParticipants(v, p.getMax_people());
                        setPrice(v, p.getPrice());
                        setStart(v, p.getStart());
                        setEnd(v, p.getEnd());
                        setDescription(v, p.getDescription());

                        EventLocation eLoc = p.getEventLocation();

                        double lat, lng;
                        lat = eLoc.getLatitude();
                        lng = eLoc.getLongtitude();
                        setPartyCity(v, lat + "");
                        setPartyStreetHouseNum(v, lng + "", 0);//todo fetch address from location

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

    private void initRequestButton(View v, String partyOwnerUUID, int availTickets){
        String currentUUID = SessionManager.getInstance().getUserId();
        Button btn = (Button) v.findViewById(R.id.sendReqBtn);
        if(currentUUID.equals(partyOwnerUUID)){
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
        }else{
            if(availTickets > 0){
                //todo send party request
                btn.setText("Let me in!");
            }else{
                btn.setBackgroundColor(Color.rgb(80,80,80));
                btn.setText("Party is full");
            }

        }

    }

}
