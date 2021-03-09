package com.example.parstagram;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.parstagram.fragments.ComposeFragment;
import com.example.parstagram.fragments.PostsFragment;
import com.example.parstagram.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final int REQUEST_CODE = 20;

    final FragmentManager fragmentManager = getSupportFragmentManager();

    private FrameLayout flContainer;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;

                bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.instagram_home_outline_24);
                bottomNavigationView.getMenu().getItem(1).setIcon(R.drawable.instagram_new_post_outline_24);
                bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.instagram_user_outline_24);

                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        Log.i(TAG, "action_home");
                        menuItem.setIcon(R.drawable.instagram_home_filled_24);
                        fragment = new PostsFragment();
                        break;
                    case R.id.action_compose:
                        Log.i(TAG, "action_compose");
                        menuItem.setIcon(R.drawable.instagram_new_post_filled_24);
                        fragment = new ComposeFragment();
                        break;
                    case R.id.action_profile:
                        Log.i(TAG, "action_profile");
                        menuItem.setIcon(R.drawable.instagram_user_filled_24);
                        fragment = new ProfileFragment();
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "Unknown menu Item selected!",Toast.LENGTH_SHORT).show();
                        fragment = new PostsFragment();     // Defaulting to home
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }

    // Added one import: import android.view.Menu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu; this adds items to action bar (if it is present
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;    // Must return true for menu to be displayed, false will result in no menu display
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.inbox) {
            Log.i(TAG, "inbox selected");
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.i(TAG, "onActivityResult, requestCode: " + requestCode + ", resultCode: "+ resultCode);
        if (requestCode == REQUEST_CODE) {
            switch(resultCode) {
                case RESULT_OK:
                    logout();
                    break;
                default:
                    // Doesn't matter
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void logout() {
        ParseUser.logOut();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

}