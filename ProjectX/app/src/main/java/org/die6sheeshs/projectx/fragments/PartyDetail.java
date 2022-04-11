package org.die6sheeshs.projectx.fragments;

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

import androidx.fragment.app.Fragment;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.activities.MainActivity;
import org.die6sheeshs.projectx.entities.Party;
import org.die6sheeshs.projectx.restAPI.PartyPersistence;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

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
    private UUID partyId;

    private View partyDetail;

    public PartyDetail(UUID partyID){
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
    public static PartyDetail newInstance(String param1, String param2, UUID partyId) {
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

        Observable<Party> party = PartyPersistence.getInstance().getParty(partyId);
        party.subscribeOn(Schedulers.io())
                .doOnError((error) -> Log.v("Party", "Party Error: " + error.getMessage()))
                .subscribe(p ->{

                    Log.v("Party Detail", "Details fetched :"+p.getDescription());
                    setPartyTitle(v, p.getName());
                    setPartyCity(v, "Not implemented");//todo fetch location
                    setPartyStreetHouseNum(v, "Not Implemented", 0);//todo fetch location
                    setMaxParticipants(v, p.getMax_people());
                    setTicksAvail(v, p.getMax_people()-123);//todo calc sold tickets
                    setPrice(v, -999.999);//todo add price to relation
                    setStart(v, p.getStart());
                    setEnd(v, p.getEnd());
                    setDescription(v, p.getDescription());

                });





        setPartyTitle(v, "EmS");
        setPartyCity(v, "Viersen");
        setPartyStreetHouseNum(v, "Am Hohen Busch",1);
        setMaxParticipants(v, 123);
        setTicksAvail(v, 123);
        setPrice(v, 50.00);
        setStart(v, LocalDateTime.now());
        setEnd(v, LocalDateTime.now());
        setDescription(v, "Liebe Freunde des morgendlichen Rühreis, \n" +
                "\n" +
                "wir haben uns sehr schweren Herzens dazu entschieden, das Eier mit Speck Festival nicht mehr weiter fortzusetzen.  \n" +
                "\n" +
                "Die Entscheidung ist in den letzten Wochen gereift und wir haben es uns wahrlich nicht leicht gemacht – aber ein Eier mit Speck ohne Tappi ist für uns nicht das was es einmal war. \n" +
                "Ein weiteres Festival ohne unseren fehlenden Kompagnon würde sich einfach in so vielerlei Hinsicht falsch anfühlen. \n" +
                "\n" +
                "Wir blicken auf 14 tolle Jahre, auf großartige Momente und gemeinsame Erinnerungen zurück. \n" +
                "\n" +
                "Wir danken an dieser Stelle nochmal all den tollen Leuten die das Eier mit Speck einzigartig in der Festivallandschaft gemacht haben – unzähligen Helfern, Sponsoren, Partnern, der Stadt Viersen, Feuerwehr und DRK, den Bands und nicht zuletzt Euch Besuchern.  \n" +
                "\n" +
                "Denkt beim Frühstück mal gelegentlich an uns!  \n" +
                "\n" +
                "Eure Speckies");

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
                new UnsupportedOperationException("Sharing with friends not supported yet!").printStackTrace();
                //System.out.println("HELLO");
            }
        });
    }

}
