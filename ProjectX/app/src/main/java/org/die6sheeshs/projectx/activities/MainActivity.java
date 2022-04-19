package org.die6sheeshs.projectx.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.databinding.ActivityMainBinding;
import org.die6sheeshs.projectx.fragments.PartyDetail;
import org.die6sheeshs.projectx.fragments.PartyOverview;
import org.die6sheeshs.projectx.fragments.Home;
import org.die6sheeshs.projectx.fragments.Profile;
import org.die6sheeshs.projectx.fragments.Tickets;
import org.die6sheeshs.projectx.helpers.PropertyService;
import org.die6sheeshs.projectx.restAPI.FirebaseMessagingHandler;

import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.function.Function;

import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    public static final int BASE_INTERVAL = 60 * 1000;
    public static final int FASTEST_INTERVAL = 5 * 1000;
    public static final int PERMISSIONS_FINE_LOCATION = 69;
    public static final int PERMISSIONS_CAMERA = 10;
    ActivityMainBinding binding;

    //Google API for location services
    private FusedLocationProviderClient fusedLocationProviderClient;

    //Firebase Messaging Handler for Notifications
    private FirebaseMessagingHandler firebaseMessagingHandler;

    //Configures the type of location request
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Simple user login verification for debugging purpose
//        if (authUserId == null || authUserId.isEmpty()) {
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
//        }
        PropertyService.registerContext(this);
        setContentView(R.layout.activity_main);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        updateLocation(location -> Log.v("Location", "Location fetched successfully"));

        //Initialize Firebase Messaging Handler
        firebaseMessagingHandler = new FirebaseMessagingHandler();

        findViewById(R.id.createParty);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Starting the first fragment
        replaceFragment(Home.newInstance("", ""));
        binding.bottomNavigationView.setOnItemSelectedListener(menuListener);

        setupLocationRequest();
    }

    public void updateLocation(Consumer<Location> func) {
        //Get user permission
        //Get current location from the fused client
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Get the location
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
                //Location successfully fetched
                if (location != null) {
                    func.accept(location);

                } else {
                    Log.v("Location null", "Location object is null");
                    Toast.makeText(this, "There was an error getting your current location, please try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.v("Permissions", "No location permission granted yet");
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
        }
    }

    public void requestCameraPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //Get the location
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.v("Permissions", "No location permission granted yet");
                requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSIONS_CAMERA);
            }
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        switch (requestCode) {
//            case PERMISSIONS_FINE_LOCATION:
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    updateLocation();
//                }
//                else {
//                    Toast.makeText(this, "This app requires location permissions to be granted in order to run properly", Toast.LENGTH_LONG).show();
//                }
//        }
//    }

    private void setupLocationRequest() {
        locationRequest = LocationRequest.create();

        locationRequest.setInterval(BASE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        //Sets how accurate the location should be, also affects power consumption
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

    }


    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.flFragment, fragment)
                .commit();
    }

    private NavigationBarView.OnItemSelectedListener menuListener = item -> {
        switch (item.getItemId()) {
            case R.id.homeFragment:
                replaceFragment(new Home());
                break;
            case R.id.ticketFragment:
                replaceFragment(new Tickets());
                break;
            case R.id.createParty:
                replaceFragment(new PartyOverview());
//                replaceFragment(new PartyDetail("20a17700-7980-4393-8f60-2fd4d6fec376"));
                break;
            case R.id.profile:
                replaceFragment(new Profile());
                break;
        }
        return true;
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
