package com.example.parstagram;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.parstagram.fragments.ComposeFragment;
import com.example.parstagram.fragments.PostsFragment;
import com.example.parstagram.models.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
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
                fragment = new ComposeFragment();
                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        Log.i(TAG, "action_home");
                        fragment = new PostsFragment();
                        break;
                    case R.id.action_compose:
                        Log.i(TAG, "action_compose");
                        fragment = new ComposeFragment();
                        break;
                    case R.id.action_profile:
                        Log.i(TAG, "action_profile");
                        //fragment = new ProfileFragment();
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "Unknown menu Item selected!",Toast.LENGTH_SHORT).show();
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


}