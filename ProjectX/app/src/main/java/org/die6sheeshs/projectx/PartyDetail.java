package org.die6sheeshs.projectx;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Picture;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

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
        initPartyTitle(v);
        initLocationImages(v);

        // Inflate the layout for this fragment
        return v;
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

    private void initPartyTitle(View v) {
        TextView title = (TextView) v.findViewById(R.id.partyTitle);
        title.setText(partyId.toString());
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
