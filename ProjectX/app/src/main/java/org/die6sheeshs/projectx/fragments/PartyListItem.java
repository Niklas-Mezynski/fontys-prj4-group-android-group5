package org.die6sheeshs.projectx.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.entities.City;
import org.die6sheeshs.projectx.entities.EventLocation;
import org.die6sheeshs.projectx.entities.EventWithLocation;
import org.die6sheeshs.projectx.entities.Party;
import org.die6sheeshs.projectx.entities.Pictures;
import org.die6sheeshs.projectx.helpers.EntityHelpers;
import org.die6sheeshs.projectx.helpers.UIThread;
import org.die6sheeshs.projectx.restAPI.GeocoderPersistence;
import org.die6sheeshs.projectx.restAPI.PartyPersistence;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PartyListItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PartyListItem extends Fragment {

    private View view;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String INDEX_PARAM = "list_index";
    private Party party;
    private View.OnClickListener buttonAction;
    private Location userLocation;

    // TODO: Rename and change types of parameters
    private String mParam1;

    public PartyListItem(Party party, View.OnClickListener buttonAction) {
        this.party = party;
        this.buttonAction = buttonAction;
    }

    public PartyListItem(Party party, View.OnClickListener buttonAction, Location userLocation) {
        this.party = party;
        this.buttonAction = buttonAction;
        this.userLocation = userLocation;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param party Index of list item.
     * @return A new instance of fragment party_list_item.
     */
    // TODO: Rename and change types and number of parameters
    public static PartyListItem newInstance(Party party, View.OnClickListener buttonAction) {
        PartyListItem fragment = new PartyListItem(party, buttonAction);
        Bundle args = new Bundle();
        args.putString(INDEX_PARAM, "party");
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
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_party_list_item, container, false);

        initContent(party, userLocation);

        return view;
    }

    private void initContent(Party party, Location location) {
        setButtonAction();
        setLocation(party.getName());
        setPrice(party.getPrice());
        setStartDate(party.getStart());
        setEndDate(party.getEnd());
        setDistance(party, userLocation);
        setImage(party);
        setCityName(party);
    }

    private void setCityName(Party party) {
        Optional<EventLocation> optionalEventLocation = EntityHelpers.partyToEnvLocation(party);
        if (!optionalEventLocation.isPresent())
            return;
        EventLocation eventLocation = optionalEventLocation.get();
        //TODO (Niklas) get the name of the city where the party location is in
        Observable<City> cityByCoordinate = GeocoderPersistence.getInstance().getCityByCoordinate(eventLocation.getLatitude(), eventLocation.getLongtitude());
        cityByCoordinate.subscribeOn(Schedulers.io())
                .subscribe(
                        city -> {
                            getActivity().runOnUiThread(() -> {
                                TextView cityTV = view.findViewById(R.id.tv_city_name);
                                cityTV.setVisibility(View.VISIBLE);
                                cityTV.setText(city.getLong_name());
                            });
                        },
                        error -> Log.v("PartyListItem - getCityName", error.getMessage())
                );
    }

    private void setButtonAction() {
        LinearLayout wrapper = view.findViewById(R.id.linearLayout_wrapper);
//        wrapper.setOnClickListener(view -> {
//            Fragment frag = new PartyDetail(party.getId());
//            ((MainActivity)getActivity()).replaceFragment(frag);
//        });
        wrapper.setOnClickListener(buttonAction);
    }

    private void setLocation(String location) {
        TextView locationTextView = (TextView) view.findViewById(R.id.textView_location);
        locationTextView.setText(location);
    }

    private void setPrice(Double price) {
        TextView priceTextView = (TextView) view.findViewById(R.id.textView_price);
        priceTextView.setText(String.format("%.2f €", price));
    }

    private void setStartDate(LocalDateTime startDate) {
        TextView startDateTextView = (TextView) view.findViewById(R.id.textView_startDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        startDateTextView.setText(startDate.format(formatter));
    }

    private void setEndDate(LocalDateTime endDate) {
        TextView endDateTextView = (TextView) view.findViewById(R.id.textView_endDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        if (endDate == null) {
            endDateTextView.setText("Open-End");
        } else {
            endDateTextView.setText(endDate.format(formatter));
        }
    }

    private void setDistance(Party party, Location userLocation) {
        if (userLocation == null) {
            return;
        }
        float distance;
        float[] result = new float[3];
        Optional<EventLocation> el = EntityHelpers.partyToEnvLocation(party);
        if (!el.isPresent()) {
            return;
        }
        EventLocation eventLocation = el.get();
        Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(), eventLocation.getLatitude(), eventLocation.getLongtitude(), result);
        distance = result[0];

        TextView tv_distance = (TextView) view.findViewById(R.id.tv_distance);
        tv_distance.setVisibility(View.VISIBLE);
        int roundedDistKm = Math.round(distance / 1000);
        tv_distance.setText(roundedDistKm + "km");
    }

//    private Optional<EventLocation> partyToEnvLocation(Party party) {
//        if (party instanceof EventWithLocation) {
//            EventWithLocation eventWithLocation = (EventWithLocation) party;
//            return Optional.of(new EventLocation(eventWithLocation.getLatitude(), eventWithLocation.getLongitude(), LocalDateTime.now(), party.getId()));
//        }
//        if (party.getEventLocation() == null)
//            return Optional.empty();
//        return Optional.of(party.getEventLocation());
//    }

    private void setImage(Party p) {
        ImageView img = (ImageView) view.findViewById(R.id.party_list_item_img);
        String partyId = p.getId();
        Observable<List<Pictures>> picsObservable = PartyPersistence.getInstance().getPartyPictures(partyId);
        picsObservable.subscribeOn(Schedulers.io())
                .subscribe(pictureList -> {
                    Optional<Pictures> firstImgOpt = pictureList.stream().filter(pic -> pic.isMain_img()).findFirst();
                    if (firstImgOpt.isPresent()) {
                        byte[] decode = Base64.decode(firstImgOpt.get().getPicture(), Base64.DEFAULT);
                        Bitmap decodedBmp = BitmapFactory.decodeByteArray(decode, 0, decode.length);
                        /**
                         getActivity().runOnUiThread(()->{// todo fix throws error attempt to invoke method(runOnUiThread) on null object reference(get activity seems to be null, du to long loading times)
                         img.setImageBitmap(decodedBmp);
                         });*/
                        UIThread.runOnUiThread(() -> {
                            img.setImageBitmap(decodedBmp);//experimetal. Report if throws error
                        });
                    } else {

                    }

                });

    }

}
