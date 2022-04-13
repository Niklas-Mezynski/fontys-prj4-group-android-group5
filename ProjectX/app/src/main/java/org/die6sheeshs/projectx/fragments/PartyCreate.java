package org.die6sheeshs.projectx.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.activities.MainActivity;
import org.die6sheeshs.projectx.entities.Party;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PartyCreate#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PartyCreate extends Fragment {

    private View view;

    public PartyCreate() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PartyCreate.
     */
    // TODO: Rename and change types and number of parameters
    public static PartyCreate newInstance(String param1, String param2) {
        PartyCreate fragment = new PartyCreate();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_party_create, container, false);
        initializeAbortButton();

        return view;
    }

    private void initializeAbortButton() {
        Button abortButton = view.findViewById(R.id.button_abort);
        abortButton.setOnClickListener(view -> {
            Fragment frag = new PartyOverview();
            ((MainActivity)getActivity()).replaceFragment(frag);
        });

        TextView startDateText = view.findViewById(R.id.textView_start);
        Button buttonChangeStart = view.findViewById(R.id.button_change_start);
        buttonChangeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(startDateText);
            }
        });

        TextView endDateText = view.findViewById(R.id.textView_end);
        Button buttonChangeEnd = view.findViewById(R.id.button_change_end);
        buttonChangeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(endDateText);
            }
        });
    }

    private Party getPartyFromInputs() {
        String partyName, partyDescription;
        LocalDateTime startTime, endTime;
        int maxPeople;
        double price;

        TextInputLayout fieldPartyName = view.findViewById(R.id.field_party_name);
        partyName = fieldPartyName.getEditText().getText().toString();

        TextInputLayout fieldPartyDescription = view.findViewById(R.id.field_party_description);
        partyDescription = fieldPartyDescription.getEditText().getText().toString();

        EditText fieldMaxPeople = view.findViewById(R.id.field_max_people);
        maxPeople = Integer.parseInt(fieldMaxPeople.getText().toString());

        EditText fieldPrice = view.findViewById(R.id.field_price);
        price = Double.parseDouble(fieldPrice.getText().toString());



//        Party party = new Party(partyName, partyDescription, startTime, endTime maxPeople);
        return null;
    }

    private void showDateTimeDialog(final TextView date_time_in) {
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);

                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy HH:mm");

                        date_time_in.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(getActivity(),timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(getActivity(),dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

}