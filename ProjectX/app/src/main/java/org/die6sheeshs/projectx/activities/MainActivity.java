package org.die6sheeshs.projectx.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.messaging.FirebaseMessaging;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.databinding.ActivityMainBinding;
import org.die6sheeshs.projectx.entities.User;
import org.die6sheeshs.projectx.fragments.Home;
import org.die6sheeshs.projectx.fragments.PartyOverview;
import org.die6sheeshs.projectx.fragments.Profile;
import org.die6sheeshs.projectx.fragments.Tickets;
import org.die6sheeshs.projectx.helpers.PropertyService;
import org.die6sheeshs.projectx.helpers.SessionManager;
import org.die6sheeshs.projectx.restAPI.FirebaseMessagingHandler;
import org.die6sheeshs.projectx.restAPI.UserPersistence;

import java.util.function.Consumer;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    public static final int PERMISSIONS_FINE_LOCATION = 69;
    public static final int PERMISSIONS_CAMERA = 10;
    ActivityMainBinding binding;

    //Google API for location services
    private FusedLocationProviderClient fusedLocationProviderClient;

    //Firebase Messaging Handler for Notifications
    private FirebaseMessagingHandler firebaseMessagingHandler;

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
//        getAndConsumeLastLocation(location -> Log.v("Location", "Location fetched successfully"));

        sendFirebaseTokenToServer();


        findViewById(R.id.createParty);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Starting the first fragment
        replaceFragment(Home.newInstance("", ""));
        binding.bottomNavigationView.setOnItemSelectedListener(menuListener);

    }


    public void getAndConsumeLastLocation(Consumer<Location> func) {
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

    @Override
    public void onBackPressed() {
        //On back press -> go back to home fragment
        replaceFragment(Home.newInstance("", ""));
        binding.bottomNavigationView.setSelectedItemId(R.id.homeFragment);
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

    private void sendFirebaseTokenToServer() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FirebaseTokenGeneration", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("FirebaseTokenGeneration", msg);

                        //Upload the new token to the server
                        FirebaseMessagingHandler.uploadFirebaseTokenToServer(token);
                    }
                });
    }
}
