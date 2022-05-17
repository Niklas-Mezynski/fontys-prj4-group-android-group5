package org.die6sheeshs.projectx.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
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
import org.die6sheeshs.projectx.entities.Address;
import org.die6sheeshs.projectx.entities.EventLocation;
import org.die6sheeshs.projectx.entities.Party;
import org.die6sheeshs.projectx.entities.Pictures;
import org.die6sheeshs.projectx.entities.Ticket;
import org.die6sheeshs.projectx.helpers.EntityHelpers;
import org.die6sheeshs.projectx.helpers.ImageConversion;
import org.die6sheeshs.projectx.restAPI.GeocoderPersistence;
import org.die6sheeshs.projectx.restAPI.PartyPersistence;
import org.die6sheeshs.projectx.restAPI.TicketPersistence;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

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
    private Ticket t;
    private TicketPersistence ticketPersistence = TicketPersistence.getInstance();

    public TicketDetail(Party party, Ticket t) {
        // Required empty public constructor
        this.party = party;
        this.t = t;
        System.out.println("ticket id: " + t.getId());
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
    public static TicketDetail newInstance(String param1, String param2, Party party, Ticket t) {
        TicketDetail fragment = new TicketDetail(party, t);
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

    private void init() {

        setPicture();
        setName();
        setPrice();
        setAddress();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        setStart(formatter);
        setEnd(formatter);


        Button gotoPartyDetail = (Button) view.findViewById(R.id.toPartyDetail);
        gotoPartyDetail.setOnClickListener(view -> getActivity().runOnUiThread(() -> {
            Fragment pDetail = new PartyDetail(party.getId());
            ((MainActivity) getActivity()).replaceFragment(pDetail);
        }));

        Button cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener((l) -> {
            Observable<Void> resp = ticketPersistence.deleteTicket(t.getId());
            resp.subscribeOn(Schedulers.io())
                    .subscribe(v -> {
                        getActivity().runOnUiThread(() -> {
                            Fragment frag = new Tickets();
                            ((MainActivity) getActivity()).replaceFragment(frag);
                        });
                    }, (error) -> Log.v("Error deleting ticket", "User Ticket DELETE error " + error.getMessage()));
        });

        Button show = view.findViewById(R.id.qr);
        show.setOnClickListener((l) -> {
            Fragment frag = new ShowQR(t);
            ((MainActivity) getActivity()).replaceFragment(frag);
        });
    }

    private void setPicture() {
        ImageView image = view.findViewById(R.id.imageView2);
        String partyId = party.getId();
        Observable<List<Pictures>> picsObservable = PartyPersistence.getInstance().getPartyPictures(partyId);
        picsObservable.subscribeOn(Schedulers.io())
                .subscribe(pictureList -> {
                    Optional<Pictures> firstImgOpt = pictureList.stream().findFirst();
                    if (firstImgOpt.isPresent()) {
                        byte[] decode = Base64.decode(firstImgOpt.get().getPicture(), Base64.DEFAULT);
                        Bitmap decodedBmp = BitmapFactory.decodeByteArray(decode, 0, decode.length);
                        getActivity().runOnUiThread(() -> {// todo fix throws error attempt to invoke method(runOnUiThread) on null object reference(get activity seems to be null, du to long loading times)
                            image.setImageBitmap(decodedBmp);
                        });
                    } else {

                    }

                });
    }

    private void setName() {
        TextView name = view.findViewById(R.id.nameT);
        name.setText(party.getName());
    }

    private void setPrice() {
        TextView price = view.findViewById(R.id.priceT);
        price.setText(String.format("%.2f €", party.getPrice()));
    }

    private void setAddress() {
        TextView address_tv = view.findViewById(R.id.addressT);
        Optional<EventLocation> optional = EntityHelpers.partyToEnvLocation(party);
        if (!optional.isPresent()) {
            address_tv.setText("Location not found");
            return;
        }
        EventLocation eventLocation = optional.get();
        Observable<Address> addressObservable = GeocoderPersistence.getInstance().getAddressShort(eventLocation.getLatitude(), eventLocation.getLongtitude());
        addressObservable.subscribeOn(Schedulers.io())
                .subscribe(
                        (address) -> getActivity().runOnUiThread(() -> address_tv.setText(address.getAddress())),
                        (error) -> getActivity().runOnUiThread(() -> address_tv.setText(String.format("Lat: %f | Lon: %f", eventLocation.getLatitude(), eventLocation.getLongtitude())))
                );
    }

    private void setStart(DateTimeFormatter formatter) {
        TextView start = view.findViewById(R.id.startT);
        start.setText(party.getStart().format(formatter));
    }

    private void setEnd(DateTimeFormatter formatter) {
        TextView end = view.findViewById(R.id.endT);
        if (party.getEnd() == null) {
            end.setText("Open end");
            return;
        }
        end.setText(party.getEnd().format(formatter));
    }

}
