package org.die6sheeshs.projectx.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationBarView;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.databinding.ActivityMainBinding;
import org.die6sheeshs.projectx.fragments.CreateParty;
import org.die6sheeshs.projectx.fragments.Home;
import org.die6sheeshs.projectx.fragments.PartyDetail;
import org.die6sheeshs.projectx.fragments.Profile;
import org.die6sheeshs.projectx.fragments.Ticket_detail;
import org.die6sheeshs.projectx.helpers.PropertyService;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

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
        findViewById(R.id.createParty);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView.setOnItemSelectedListener(menuListener);
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
                replaceFragment(new Ticket_detail());
                break;
            case R.id.createParty:
                replaceFragment(new CreateParty());
//                replaceFragment(new PartyDetail("20a17700-7980-4393-8f60-2fd4d6fec376"));
                break;
            case R.id.profile:
                replaceFragment(new Profile());
                break;
        }
        return true;
    };
}
