package org.die6sheeshs.projectx.fragments;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.entities.Count;
import org.die6sheeshs.projectx.entities.EventLocation;
import org.die6sheeshs.projectx.entities.Party;
import org.die6sheeshs.projectx.entities.Ticket;
import org.die6sheeshs.projectx.entities.User;
import org.die6sheeshs.projectx.helpers.SessionManager;
import org.die6sheeshs.projectx.restAPI.PartyPersistence;
import org.die6sheeshs.projectx.restAPI.UserPersistence;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;




public class ShowQR extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Ticket ticket;



    public ShowQR(Ticket ticket){
        this.ticket = ticket;
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
    public static ShowQR newInstance(String param1, String param2, Ticket ticket) {
        ShowQR fragment = new ShowQR(ticket);
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

        View v = inflater.inflate(R.layout.fragment_show_qr, container, false);

        setTicketData(v);

        // Inflate the layout for this fragment
        return v;
    }



    private void setTicketData(View v) {
        int ticksAvailABC = 0;


        Observable<User> userObs = UserPersistence.getInstance().getUserData(SessionManager.getInstance().getUserId());
        userObs.subscribeOn(Schedulers.io())
                .doOnError((error) -> Log.v("Ticket", "Ticket Error: "+error.getMessage()))
                .subscribe(user -> {
                    getActivity().runOnUiThread(()->{
                        setNames(v, user.getFirstName(), user.getLastName(), user.getNick_name());
                    });

                });


        Observable<Party> party = PartyPersistence.getInstance().getParty(ticket.getEvent_id());
        party.subscribeOn(Schedulers.io())
                .doOnError((error) -> Log.v("Party", "Party Error: " + error.getMessage()))
                .subscribe(p ->{
                    getActivity().runOnUiThread(() -> {
                        setPartyTitle(v, p.getName());
                    });
                });


        setQRCode(v, ticket.getId());


    }



    private void setQRCode(View v, String data){
        ImageView qrImg = (ImageView) v.findViewById(R.id.qrImageView);
        int dimensions = 1080;

        MultiFormatWriter mfw = new MultiFormatWriter();
        BitMatrix bm = null;
        try {
            bm = mfw.encode(data, BarcodeFormat.QR_CODE,dimensions,dimensions);

            Bitmap bmp = Bitmap.createBitmap(dimensions, dimensions, Bitmap.Config.RGB_565);
            for(int x = 0; x < dimensions; x++){
                for(int y = 0; y < dimensions; y++){
                    bmp.setPixel(x, y, bm.get(x,y) ? Color.BLACK : Color.WHITE);
                }
            }
            qrImg.setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }






    }

    private void setPartyTitle(View v, String s) {
        TextView title = (TextView) v.findViewById(R.id.PartyTitle);
        title.setText(s);
    }

    private void setNames(View v, String firstName, String lastName, String nickName) {
        TextView fName = (TextView) v.findViewById(R.id.fNameText);
        TextView lName = (TextView) v.findViewById(R.id.lNameText);
        TextView nName = (TextView) v.findViewById(R.id.nNameText);
        fName.setText(firstName);
        lName.setText(lastName);
        nName.setText(nickName);
    }







}
