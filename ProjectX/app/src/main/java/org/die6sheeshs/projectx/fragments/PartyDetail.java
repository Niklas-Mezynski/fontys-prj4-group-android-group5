package org.die6sheeshs.projectx.fragments;

import android.Manifest;
import android.app.ProgressDialog;

import static android.app.Activity.RESULT_CANCELED;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.collection.CircularArray;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.encoder.QRCode;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.activities.MainActivity;
import org.die6sheeshs.projectx.entities.City;
import org.die6sheeshs.projectx.entities.Count;
import org.die6sheeshs.projectx.entities.EventLocation;
import org.die6sheeshs.projectx.entities.Party;
import org.die6sheeshs.projectx.entities.Pictures;
import org.die6sheeshs.projectx.entities.Ticket;
import org.die6sheeshs.projectx.entities.TicketRequest;
import org.die6sheeshs.projectx.entities.User;
import org.die6sheeshs.projectx.helpers.GeneralHelperMethods;
import org.die6sheeshs.projectx.helpers.SessionManager;
import org.die6sheeshs.projectx.helpers.SimpleFuture;
import org.die6sheeshs.projectx.helpers.SimpleObservable;
import org.die6sheeshs.projectx.helpers.SimpleObserver;
import org.die6sheeshs.projectx.restAPI.GeocoderPersistence;
import org.die6sheeshs.projectx.restAPI.PartyPersistence;
import org.die6sheeshs.projectx.restAPI.TicketPersistence;
import org.die6sheeshs.projectx.restAPI.TicketRequestPersistence;
import org.die6sheeshs.projectx.restAPI.UserPersistence;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.UUID;
import java.util.jar.JarOutputStream;

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
    private ActivityResultLauncher<Intent> intentActivityResultLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private Intent scanIntent;
    private static final int NO_TICKET = 0;
    private static final int TICKET_PENDING = 1;
    private static final int TICKET_ACCEPTED = 2;

    private View partyDetail;
    private ImageView imageView;

    List<Bitmap> partyImages = new ArrayList<>();

    public PartyDetail(String partyID) {
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
        ScanOptions options = new ScanOptions();
        options.setOrientationLocked(false);
        options.setPrompt("Scan QR code");
        options.setBeepEnabled(false);
        options.setBarcodeImageEnabled(true);
        scanIntent = options.createScanIntent(getContext());
        intentActivityResultLauncher = registerForActivityResult
                (new ActivityResultContracts.StartActivityForResult(), result -> {
                    ScanIntentResult scanIntentResult = ScanIntentResult.parseActivityResult(result.getResultCode(), result.getData());
                    if (result != null) {
                        if (scanIntentResult == null) {
                            Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();
                        } else {
                            //use result.getContents() um zu gucken ob es das ticket gibt;
                            Observable<Ticket> response = TicketPersistence.getInstance().getTicketById(scanIntentResult.getContents());

                            response.subscribeOn(Schedulers.io())
                                    .subscribe(ticket -> {
                                        if (ticket.getEvent_id().equals(partyId)) {
                                            Observable<User> response2 = UserPersistence.getInstance().getUserData(ticket.getUser_id());
                                            response2.subscribeOn(Schedulers.io())
                                                    .subscribe(user -> {
                                                        getActivity().runOnUiThread(() -> {
                                                            OurToast.makeToast("Valid ticket: " + user.getFirstName() + " " + user.getLastName(), "#ff00ff00", R.drawable.ic_baseline_check_24, getContext(), getLayoutInflater());
                                                        });

                                                    }, error -> Log.v("User", "Error get User with id " + error.getMessage()));
                                        } else {
                                            getActivity().runOnUiThread(() -> {
                                                OurToast.makeToast("Not your party bro", "#ffff0000", R.drawable.ic_baseline_clear_24, getContext(), getLayoutInflater());
                                            });
                                        }
                                    }, (error) -> {
                                        Log.v("Ticket", "Error get Ticket with id " + error.getMessage());
                                        getActivity().runOnUiThread(() -> {
                                            OurToast.makeToast("Ticket not found", "#ffff0000", R.drawable.ic_baseline_clear_24, getContext(), getLayoutInflater());
                                        });
                                    });
                        }
                    }
                });

        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        // Permission is granted. Continue the action or workflow in your app.
                        intentActivityResultLauncher.launch(scanIntent);
                    } else {
                        // Explain to the user that the feature is unavailable because the
                        // features requires a permission that the user has denied. At the
                        // same time, respect the user's decision. Don't link to system
                        // settings in an effort to convince the user to change their
                        // decision.
                        Toast.makeText(getContext(), "Cannot take a new profile picture without permission", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_party_detail, container, false);
        imageView = v.findViewById(R.id.displayedLocationImage);
        initShareButton(v);
        initLocationImages(v);
        setPartyData(v);
        // Inflate the layout for this fragment
        return v;
    }


    private SimpleFuture<Integer> getTicketState() {//todo implement ticket state request

        SimpleFuture<Integer> returnValue = new SimpleFuture<Integer>();

        String userId = SessionManager.getInstance().getUserId();
        ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
                "Loading Tickets. Please wait...", true);
        Observable<List<Ticket>> ticketsOfUser = TicketPersistence.getInstance().getTickets(SessionManager.getInstance().getUserId(), "");
        ticketsOfUser.subscribeOn(Schedulers.io())
                .subscribe(tickets -> {
                    long countTickets = tickets.stream().filter(t -> t.getEvent_id().equals(partyId) && t.getUser_id().equals(userId)).count();

                    if (countTickets > 0) {
                        returnValue.setValue(TICKET_ACCEPTED);
                        //return TICKET_ACCEPTED;
                    } else {// else check if has request

                        Observable<List<TicketRequest>> ticketRequestsForParty = TicketRequestPersistence.getInstance().getTicketRequestsOfUser(SessionManager.getInstance().getUserId());
                        ticketRequestsForParty.subscribeOn(Schedulers.io())
                                .subscribe(ticketRequests -> {
                                    long countTicketRequests = ticketRequests.stream().filter(ticketRequest -> ticketRequest.getPartyId().equals(partyId)).count();
                                    if (countTicketRequests > 0) {
                                        returnValue.setValue(TICKET_PENDING);
                                    } else {
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
                .subscribe(p -> {


                    Observable<List<Count>> countTickets = PartyPersistence.getInstance().getCountTicketsOfParty(partyId);
                    countTickets.subscribeOn(Schedulers.io())
                            .subscribe(counts -> {

                                        Observable<User> userBelongingToEvent = PartyPersistence.getInstance().getOwner(partyId);
                                        userBelongingToEvent.subscribeOn(Schedulers.io())
                                                .subscribe(eUser -> {


                                                            getActivity().runOnUiThread(() -> {
                                                                SimpleFuture<Integer> ticketStateFuture = getTicketState();
                                                                ticketStateFuture.doActionWhenValueSet(ticketState -> {
                                                                    initRequestButton(v, eUser.getId(), p.getMax_people() - counts.stream().findFirst().get().getCount(), ticketState);
                                                                });
                                                            });

                                                            dialog.dismiss();
                                                        },
                                                        (error) -> Log.v("Party", "Party Error No User Found: " + error.getMessage()));

                                        getActivity().runOnUiThread(() -> {
                                            int availTickets = -1;
                                            if (counts.size() == 1) {
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

                        setPartyCity(v, "Loading...");
                    });
                    //Load the correct party location async
                    EventLocation eLoc = p.getEventLocation();
                    loadAndSetCity(v, eLoc);
                });


    }

    private void loadAndSetCity(View v, EventLocation eLoc) {
        if (eLoc == null) {
            setPartyCity(v, "Party location error");
        }
        Observable<City> cityObservable = GeocoderPersistence.getInstance().getCityByCoordinate(eLoc.getLatitude(), eLoc.getLongtitude());
        cityObservable.subscribeOn(Schedulers.io())
                .subscribe(
                        city -> getActivity().runOnUiThread(() -> setPartyCity(v, city.getLong_name())),
                        error -> getActivity().runOnUiThread(() -> setPartyCity(v, "Cannot load city name"))
                );
    }

    private SimpleFuture<List<Pictures>> getPictures() {
        SimpleFuture<List<Pictures>> listOfPictures = new SimpleFuture<>();
        Observable<List<Pictures>> pictures = PartyPersistence.getInstance().getPartyPictures(partyId);
        pictures.subscribeOn(Schedulers.io())
                .subscribe(pictureList -> {
                    listOfPictures.setValue(pictureList);
                });
        return listOfPictures;
    }

    private void setDescription(View v, String s) {
        TextView title = (TextView) v.findViewById(R.id.descText);
        title.setText(s);
    }


    private void setEnd(View v, LocalDateTime end) {
        TextView title = (TextView) v.findViewById(R.id.endText);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        if (end == null) {
            title.setText("Open-End");
        } else {
            title.setText(end.format(formatter));
        }
    }

    private void setStart(View v, LocalDateTime start) {
        TextView title = (TextView) v.findViewById(R.id.startText);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        title.setText(start.format(formatter));
    }

    private void setPrice(View v, double price) {
        TextView title = (TextView) v.findViewById(R.id.priceText);
        title.setText("" + price);
    }

    private void setTicksAvail(View v, int i) {
        TextView title = (TextView) v.findViewById(R.id.ticketsAvailText);
        title.setText("" + i);
    }

    private void setMaxParticipants(View v, int i) {
        TextView title = (TextView) v.findViewById(R.id.maxParticipantsText);
        title.setText("" + i);

    }

    private void setPartyStreetHouseNum(View v, String s, int houseNum) {
        TextView title = (TextView) v.findViewById(R.id.streetHou);
        title.setText(s + ", " + houseNum);
    }

    private void setPartyCity(View v, String s) {
        TextView title = (TextView) v.findViewById(R.id.city);
        title.setText(s);
    }

    private void setPartyTitle(View v, String s) {
        TextView title = (TextView) v.findViewById(R.id.partyTitle);
        title.setText(s);
    }

    SimpleObservable<Integer> imageIndex = new SimpleObservable<>();

    private void initLocationImages(View v) {
        ImageView curImg = (ImageView) v.findViewById(R.id.displayedLocationImage);
        Bitmap bmp = Bitmap.createBitmap(1920, 1080, Bitmap.Config.ARGB_8888);
        for (int x = 0; x < 1919; x++) {
            for (int y = 0; y < 1079; y++) {
                bmp.setPixel(x, y, Color.GRAY);
            }
        }
        /**

         Bitmap bmp1 = Bitmap.createBitmap(1920,1080, Bitmap.Config.ARGB_8888);
         for(int x = 0; x < 1919; x++){
         for(int y = 0; y < 1079; y++){
         bmp1.setPixel(x,y, Color.rgb(x%256,y%256,x%256));
         }
         }
         Bitmap bmp2 = Bitmap.createBitmap(1920,1080, Bitmap.Config.ARGB_8888);
         for(int x = 0; x < 1919; x++){
         for(int y = 0; y < 1079; y++){
         bmp2.setPixel(x,y, Color.rgb(x%256,0,x%256));
         }
         }
         Bitmap bmp3 = Bitmap.createBitmap(1920,1080, Bitmap.Config.ARGB_8888);
         for(int x = 0; x < 1919; x++){
         for(int y = 0; y < 1079; y++){
         bmp3.setPixel(x,y, Color.rgb(0,y%256,x%256));
         }
         }

         partyImages.add(bmp);
         partyImages.add(bmp1);
         partyImages.add(bmp3);
         partyImages.add(bmp2);
         **/
        SimpleFuture<List<Pictures>> future = getPictures();
        //SimpleFuture<List<Pictures>> future = new SimpleFuture<>();
        //future.setValue(new ArrayList<>());
        future.doActionWhenValueSet(pics -> {
            for (Pictures p : pics) {
                byte[] decode = Base64.decode(p.getPicture(), Base64.DEFAULT);
                Bitmap decodeBmp = BitmapFactory.decodeByteArray(decode, 0, decode.length);
                if (bmp != null) {
                    partyImages.add(decodeBmp);
                }
            }


            if (partyImages.size() == 0) {
                partyImages.add(bmp);
            }


            SimpleObserver<Integer> indexChangeObserver = new SimpleObserver<Integer>() {
                @Override
                public void doAction(Integer value) {
                    getActivity().runOnUiThread(() -> {
                        ProgressBar pgb = (ProgressBar) v.findViewById(R.id.imgProgBar);
                        pgb.setMax(partyImages.size() - 1);
                        pgb.setProgress(value, true);
                        if (value >= partyImages.size()) {

                        } else {
                            curImg.setImageBitmap(partyImages.get(value));
                        }
                    });


                }
            };
            imageIndex.subscribe(indexChangeObserver);
            initNextImageButton(imageIndex, partyImages, v);
            initPreviousImageButton(imageIndex, partyImages, v);

            imageIndex.runActions(0);
        });

    }


    private void initNextImageButton(SimpleObservable<Integer> index, List<Bitmap> images, View v) {
        Button nextImageButton = (Button) v.findViewById(R.id.nextImgBtn);

        index.subscribe(new SimpleObserver<Integer>() {
            @Override
            public void doAction(Integer value) {
                if (value >= images.size() - 1) {
                    //disable next button
                    disableButton(nextImageButton);
                } else {
                    // enable next button
                    enableButton(nextImageButton, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            index.runActions(value + 1);
                        }
                    });
                }
            }
        });
    }

    private void initPreviousImageButton(SimpleObservable<Integer> index, List<Bitmap> images, View v) {
        Button previousImageButton = (Button) v.findViewById(R.id.previousImgBtn);

        index.subscribe(new SimpleObserver<Integer>() {
            @Override
            public void doAction(Integer value) {
                if (value <= 0) {
                    // disable previous button
                    disableButton(previousImageButton);
                } else {
                    // enable next button
                    enableButton(previousImageButton, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            index.runActions(value - 1);
                        }
                    });
                }
            }
        });
    }

    private void disableButton(Button b) {
        getActivity().runOnUiThread(() -> {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            b.setBackgroundColor(Color.GRAY);
        });
    }

    private void enableButton(Button b, View.OnClickListener o) {
        getActivity().runOnUiThread(() -> {
            b.setBackgroundColor(GeneralHelperMethods.getThemePrimaryColor(getContext()));
            b.setOnClickListener(o);
        });
    }


    private void initShareButton(View v) {
        Button share = (Button) v.findViewById(R.id.shareButton);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().runOnUiThread(() -> {
                    Fragment frag = new ShareParty(partyId);
                    ((MainActivity) getActivity()).replaceFragment(frag);
                });

            }
        });
    }

    private void cancelParty(String partyId, View v) {
        //todo cancel Party (WIP)
        Observable<Integer> resp = TicketRequestPersistence.getInstance().deleteTicketRequest("", partyId);
        resp.subscribeOn(Schedulers.io())
                .subscribe(r -> {
                    Observable<Count> resp2 = PartyPersistence.getInstance().deleteTicketsFromParty(partyId);
                    resp2.subscribeOn(Schedulers.io())
                            .subscribe(r2 -> {
                                Observable<Void> resp3 = PartyPersistence.getInstance().deleteEvent(partyId);
                                resp3.subscribeOn(Schedulers.io())
                                        .subscribe(r3 -> {
                                            getActivity().runOnUiThread(() -> {
                                                Fragment frag = new PartyOverview();
                                                ((MainActivity) getActivity()).replaceFragment(frag);
                                                OurToast.makeToast("Party deleted successfully", "#ff00ff00", R.drawable.ic_baseline_clear_24, getContext(), getLayoutInflater());
                                            });
                                        }, (error) -> {
                                            Log.v("Party", "Error delete party by party id  " + error.getMessage());
                                        });
                            }, (error) -> {
                                Log.v("Ticket", "Error delete ticket by party id  " + error.getMessage());
                            });
                }, (error) -> {
                    Log.v("Ticket Request", "Error delete ticket request by party id  " + error.getMessage());
                });
    }

    private void initRequestButton(View v, String partyOwnerUUID, int availTickets, int ticketState) {
        String currentUUID = SessionManager.getInstance().getUserId();
        Button btn = (Button) v.findViewById(R.id.sendReqBtn);
        System.out.println("Current User: " + currentUUID);
        System.out.println("Party Owner: " + partyOwnerUUID);
        if (currentUUID.equals(partyOwnerUUID)) {
            System.out.println("Party Owner is Current User");
            getActivity().runOnUiThread(() -> {
                btn.setText("Cancel Party");
                btn.setBackgroundColor(getResources().getColor(R.color.errorRed, getContext().getTheme()));
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Cancel Party")
                                .setMessage("Do you really want to cancel your party?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        cancelParty(partyId, v);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null).show();
                    }
                });
                Button scanButton = (Button) v.findViewById(R.id.scanQRButton);
                scanButton.setVisibility(View.VISIBLE);
                Button guestListButton = (Button) v.findViewById(R.id.guestListBtn);
                guestListButton.setVisibility(View.VISIBLE);
                initQRCodeScanButton(v);
                initGuestListButton(v);
            });


        } else {

            if (ticketState == NO_TICKET) {
                if (availTickets > 0) {

                    updateRequestButton(btn, ticketState);


                } else {
                    getActivity().runOnUiThread(() -> {
                        btn.setBackgroundColor(Color.GRAY);
                        btn.setText("Party is full");
                    });

                }
            } else {
                updateRequestButton(btn, ticketState);
            }


        }

    }

    private void updateRequestButton(Button btn, int ticketState) {
        if (ticketState == NO_TICKET) {
            getActivity().runOnUiThread(() -> {
                btn.setBackgroundColor(GeneralHelperMethods.getThemePrimaryColor(getContext()));
                btn.setText("SeNd reQuEst");
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postTicketRequest(view, btn);
                    }
                });
            });

        } else if (ticketState == TICKET_PENDING) {
            getActivity().runOnUiThread(() -> {
                btn.setText("Cancel Request");
                btn.setBackgroundColor(Color.GRAY);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cancelTicketRequest(view, btn);
                    }
                });
            });

        } else if (ticketState == TICKET_ACCEPTED) {
            getActivity().runOnUiThread(() -> {
                btn.setText("Show Ticket");
                btn.setBackgroundColor(getResources().getColor(R.color.errorRed, getContext().getTheme()));
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gotoTicket(view, btn);
                    }
                });
            });
        }
    }

    private void postTicketRequest(View v, Button requestButton) {
        LocalDateTime createdOn = LocalDateTime.now();
        String userId = SessionManager.getInstance().getUserId();
        Observable<TicketRequest> tr = TicketRequestPersistence.getInstance().createTicketRequest(userId, partyId, createdOn);
        tr.subscribeOn(Schedulers.io())
                .subscribe(ticketRequest -> {
                    getActivity().runOnUiThread(() -> {
                        if (ticketRequest.getPartyId().equals(partyId) && ticketRequest.getUserId().equals(userId)) {
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Looper.prepare();
                                    SimpleFuture<Integer> ticketStateFuture = getTicketState();
                                    ticketStateFuture.doActionWhenValueSet(ticketState -> {

                                        updateRequestButton(requestButton, ticketState);
                                    });
                                }
                            });
                            thread.start();
                        }
                    });
                });

    }

    private void cancelTicketRequest(View v, Button requestButton) {
        String userId = SessionManager.getInstance().getUserId();
        Observable<List<TicketRequest>> t = TicketRequestPersistence.getInstance().getTicketRequestsOfUser(userId);
        t.subscribeOn(Schedulers.io())
                .subscribe(ticketRequests -> {
                            TicketRequest trForThisParty = ticketRequests.stream().filter(ticketRequest -> ticketRequest.getPartyId().equals(partyId)).findFirst().get();
                            if (trForThisParty != null) {
                                Observable<Integer> deleteObs = TicketRequestPersistence.getInstance().deleteTicketRequest(SessionManager.getInstance().getUserId(), partyId);
                                deleteObs.subscribeOn(Schedulers.io())
                                        .subscribe((str) -> {
                                            Thread thread = new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Looper.prepare();
                                                    SimpleFuture<Integer> ticketStateFuture = getTicketState();
                                                    ticketStateFuture.doActionWhenValueSet(ticketState -> {

                                                        updateRequestButton(requestButton, ticketState);
                                                    });
                                                }
                                            });
                                            thread.start();

                                        }, error -> {
                                            Log.v("Delete TicketRequest", error.getMessage());
                                        });
                            } else {
                                updateRequestButton(requestButton, NO_TICKET);
                            }
                        },
                        (error) -> Log.v("Party", "Error on count" + error.getMessage()));

    }

    private void gotoTicket(View v, Button requestButton) {
        getActivity().runOnUiThread(() -> {
            Observable<Party> pObs = PartyPersistence.getInstance().getParty(partyId);
            pObs.subscribeOn(Schedulers.io())
                    .subscribe(party -> {
                        Observable<List<Ticket>> ticketsObs = TicketPersistence.getInstance().getTickets(SessionManager.getInstance().getUserId(), "");
                        ticketsObs.subscribeOn(Schedulers.io())
                                .subscribe(tickets -> {
                                    long numTickets = tickets.stream()
                                            .filter(ticket -> ticket.getEvent_id().equals(partyId) && ticket.getUser_id().equals(SessionManager.getInstance().getUserId()))
                                            .count();
                                    if (numTickets != 1) {
                                        throw new RuntimeException("Number of tickets must be 1!");
                                    } else {
                                        Ticket t = tickets.get(0);
                                        getActivity().runOnUiThread(() -> {
                                            Fragment tDetail = new TicketDetail(party, t);
                                            ((MainActivity) getActivity()).replaceFragment(tDetail);
                                        });
                                    }
                                }, error -> {
                                    Log.v("Error retrieving Tickets: ", error.getMessage());
                                });
                    }, error -> {
                        Log.v("Error retrieving party: ", error.getMessage());
                    });

        });

    }

    public void initGuestListButton(View v) {

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Button qrButton = v.findViewById(R.id.guestListBtn);
        qrButton.setOnClickListener((l) -> {
            Guestlist guestList = new Guestlist(partyId);
            ((MainActivity) getActivity()).replaceFragment(guestList);
            //todo Show guest list
            System.out.println("TODO: Show GuestList");
        });
    }

    public void initQRCodeScanButton(View v) {
        Button qrButton = v.findViewById(R.id.scanQRButton);
        qrButton.setOnClickListener((l) -> {
            if (ContextCompat.checkSelfPermission(
                    getContext(), Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED) {
                // You can use the API that requires the permission.
                intentActivityResultLauncher.launch(scanIntent);
            } else {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(Manifest.permission.CAMERA);
            }
        });
    }

    private void askPermAndTakeImg(View clickedView) {
        //Checking for camera permissions

    }

}
