package com.example.parsetagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.parsetagram.fragments.ComposeFragment;
import com.example.parsetagram.fragments.FeedFragment;
import com.example.parsetagram.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;


public class MainActivity extends AppCompatActivity {

    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getSupportActionBar().hide();

        final FragmentManager fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_main);

        // define your fragments here
        final Fragment fragment1 = new FeedFragment();
        final Fragment fragment2 = new ComposeFragment();
        final Fragment fragment3 = new ProfileFragment(ParseUser.getCurrentUser());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);


        // handle navigation selection
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_feed:
                        Toast.makeText(MainActivity.this, "feed", Toast.LENGTH_SHORT).show();
                        fragment = fragment1;
                        // do something here
                        break;
                    case R.id.action_image:
                        Toast.makeText(MainActivity.this, "image", Toast.LENGTH_SHORT).show();
                        fragment = fragment2;
                        // do something here
                        break;
                    case R.id.action_profile:
                        Toast.makeText(MainActivity.this, "profile", Toast.LENGTH_SHORT).show();
                        fragment = fragment2;
                        break;
                    default:
                        fragment = fragment1;
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;

            }
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_feed);
    }
}
