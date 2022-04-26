package org.die6sheeshs.projectx.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.activities.MainActivity;
import org.die6sheeshs.projectx.entities.EventLocation;
import org.die6sheeshs.projectx.entities.EventWithLocation;
import org.die6sheeshs.projectx.entities.Party;
import org.die6sheeshs.projectx.helpers.SessionManager;
import org.die6sheeshs.projectx.restAPI.PartyPersistence;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PartyCreate#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PartyCreate extends Fragment {

    private View view;
    private final PartyPersistence partyPersistence = PartyPersistence.getInstance();
    private final SessionManager sessionManager = SessionManager.getInstance();
    private final Map<String, TextView> failFields = new HashMap<>();
    private Party createdParty;

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your app.
                    submitThePartyWithLocation(createdParty);
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                    Toast.makeText(getContext(), "Cannot post the new party without having location permissions", Toast.LENGTH_SHORT).show();
                }
            });

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
        initializeDateButtons();
        initializeSubmitButton();

        return view;
    }

    private void initializeAbortButton() {
        Button abortButton = view.findViewById(R.id.button_abort);
        abortButton.setOnClickListener(view -> {
            Fragment frag = new PartyOverview();
            ((MainActivity) getActivity()).replaceFragment(frag);
        });
    }

    private void initializeDateButtons() {
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

    private void initializeSubmitButton() {
        Button submitButton = view.findViewById(R.id.button_submit_create);
        submitButton.setOnClickListener(view -> {
            Party party = getPartyFromInputs();
            if (checkFields(party)) {
                submitParty(party);
            }
        });
    }

    private Party getPartyFromInputs() {
        String partyName, partyDescription;
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        int maxPeople;
        double price;

        TextInputLayout fieldPartyName = view.findViewById(R.id.field_party_name);
        partyName = fieldPartyName.getEditText().getText().toString();

        TextInputLayout fieldPartyDescription = view.findViewById(R.id.field_party_description);
        partyDescription = fieldPartyDescription.getEditText().getText().toString();

        EditText fieldMaxPeople = view.findViewById(R.id.field_max_people);
        String stringMaxPeople = fieldMaxPeople.getText().toString();
        maxPeople = stringMaxPeople.equals("") ? -1 : Integer.parseInt(stringMaxPeople);

        EditText fieldPrice = view.findViewById(R.id.field_price);
        String stringPrice = fieldPrice.getText().toString();
        price = stringPrice.equals("") ? 0D : Double.parseDouble(stringPrice);

        TextView startDateText = view.findViewById(R.id.textView_start);
        String startDateString = String.valueOf(startDateText.getText());
        TextView endDateText = view.findViewById(R.id.textView_end);
        String endDateString = String.valueOf(endDateText.getText());

        try {
            startTime = LocalDateTime.parse(startDateString, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
            endTime = LocalDateTime.parse(endDateString, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        } catch (Exception e) {
            Log.v("Date", "Could not transform string");
        }

        return new Party(partyName, partyDescription, startTime, endTime, maxPeople, sessionManager.getUserId(), price, null);
    }

    private void showDateTimeDialog(final TextView date_time_in) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

                        date_time_in.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(getActivity(), timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        };

        new DatePickerDialog(getActivity(), dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void submitParty(Party p) {
        createdParty = p;
        //Checking for location permissions
        if (ContextCompat.checkSelfPermission(
                getContext(), Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            submitThePartyWithLocation(p);
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }

    }

    private void submitThePartyWithLocation(Party p) {
        MainActivity mainActivity = (MainActivity) getActivity();

        mainActivity.getAndConsumeLastLocation(location -> {
            EventWithLocation eventWithLocation = new EventWithLocation(p.getName(), p.getDescription(),
                    p.getStart(), p.getEnd(), p.getPrice(), p.getMax_people(), location.getLatitude(),
                    location.getLongitude(), null, p.getUser_id());
            Observable<Party> response = partyPersistence.createEventWithLocation(eventWithLocation);

            response.subscribeOn(Schedulers.io())
                    .subscribe(result -> {
                        getActivity().runOnUiThread(() -> {
                            if (result != null) {
                                //Toast.makeText(getActivity(), "Successfully created a new party!", Toast.LENGTH_SHORT).show();
                                showToast("Successfully create a new party", "#ff00ff00");
                                Fragment frag = new PartyOverview();
                                ((MainActivity) getActivity()).replaceFragment(frag);
                            }
                        });
                    }, error -> Log.e("Submit party", "Could post a new party to RestAPI: " + error.getMessage()));
        });
    }

    private void findFailFields() {
        failFields.put("name", view.findViewById(R.id.failed_name));
        failFields.put("description", view.findViewById(R.id.failed_description));
        failFields.put("start", view.findViewById(R.id.failed_start));
        failFields.put("start_past", view.findViewById(R.id.failed_start_past));
        failFields.put("end", view.findViewById(R.id.failed_end));
        failFields.put("price", view.findViewById(R.id.failed_price));
        failFields.put("max", view.findViewById(R.id.failed_max));
    }

    private boolean checkFields(Party p) {
        if (failFields.isEmpty()) findFailFields();
        resetFailFields();

        boolean success = true;

        if (p.getName() == null || p.getName().isEmpty()) {
            failFields.get("name").setVisibility(View.VISIBLE);
            success = false;
        }
        if (p.getDescription() == null || p.getDescription().isEmpty()) {
            failFields.get("description").setVisibility(View.VISIBLE);
            success = false;
        }
        if (p.getStart() == null) {
            failFields.get("start").setVisibility(View.VISIBLE);
            success = false;
        } else if (p.getStart().isBefore(LocalDateTime.now())) {
            failFields.get("start_past").setVisibility(View.VISIBLE);
            success = false;
        }
        if (p.getEnd() != null && p.getEnd().isBefore(p.getStart())) {
            failFields.get("end").setVisibility(View.VISIBLE);
            success = false;
        }
        if (p.getPrice() < 0) {
            failFields.get("price").setVisibility(View.VISIBLE);
            success = false;
        }
        if (p.getMax_people() < 1) {
            failFields.get("max").setVisibility(View.VISIBLE);
            success = false;
        }

        return success;
    }

    /**
     * Resets all fail fields and sets them invisible
     */
    private void resetFailFields() {
        for (TextView v : failFields.values()) {
            v.setVisibility(View.INVISIBLE);
        }
    }

    private void showToast(String msg, String color) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, null);

        ImageView image = (ImageView) layout.findViewById(R.id.image);
        image.setImageResource(R.drawable.ic_baseline_check_24);
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(msg);
        layout.setBackgroundColor(Color.parseColor(color));
        Toast toast = new Toast(getContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

}
