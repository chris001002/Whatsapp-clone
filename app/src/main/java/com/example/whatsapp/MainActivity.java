package com.example.whatsapp;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.whatsapp.Fragments.CallsFragment;
import com.example.whatsapp.Fragments.ChatsFragment;
import com.example.whatsapp.Fragments.StatusFragment;
import com.example.whatsapp.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private ActivityMainBinding binding; //refer to view binding to directly reference views
    private SensorManager sensorManager;//variable of type SensorManager to interact and utilize sensor
    private Sensor accelerometer;//variable of type Sensor to utilize accelerometer

    //The code below is used to define an adapter for ViewPager2 to swipe between different fragments
    private class ViewPagerAdapter extends FragmentStateAdapter { //A custom class extending FragmentStateAdapter
        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) { //A constructor
            super(fragmentActivity); //This constructor allow for the adaptor to manage fragment creation and destruction
        }

        @NonNull
        @Override
        //The method below is overriden to return the appropriate fragment(s) for each pages based on-
        //current position
        public Fragment createFragment(int position) {
            switch (position) { //Used to determine which fragment to show depending on the position
                case 0: return new ChatsFragment(); //first page (pos 0)
                case 1: return new StatusFragment(); //second page (pos 1)
                case 2: return new CallsFragment(); //third page (poa 2)
                default: return new ChatsFragment(); //invalid pos, return to ChatsFragment as default
            }
        }

        @Override
        //The method below returns the number of pages the ViewPager2 should handle
        public int getItemCount() {
            return 3;
        } //Returning the three fragments
    }

    @Override
    //The method below is to initialize activity UI and functionality. It's called when activity first created
    protected void onCreate(Bundle savedInstanceState) {

        //The if statement below is to check whether or not the user enabled dark mode
        if (Preferences.getDarkMode(MainActivity.this)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        //otherwise, the app will be set to light mode
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);

        // Set up view binding to easily reference views from layout XML
        binding = ActivityMainBinding.inflate(getLayoutInflater()); //inflates the layout
        setContentView(binding.getRoot()); //sets the layout as content for activity

        // Initialize Firebase Auth
        FirebaseAuth mAuth = FirebaseAuth.getInstance(); //mAuth allow app to perform authentication task (sign in, sign out)

        // Set up ViewPager and TabLayout
        TabLayout tabLayout = findViewById(R.id.tabLayout); //Used for tab navigation
        ViewPager2 viewPager2 = findViewById(R.id.viewPager); //Allows swiping between fragments
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this); //An adapter that supplies fragments to be shown
        viewPager2.setAdapter(viewPagerAdapter); //Links adapter to ViewPager2 to allow swiping

        //The method below is used to synchronize selected tab in TabLayout with current page in ViewPager
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setVisibility(View.VISIBLE);
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        //Method below is used to update selected tab in TabLayout when user swipes between pages
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (tabLayout.getTabAt(position) != null) {
                    tabLayout.getTabAt(position).select(); //When page is swiped, corresponding tab will be selected
                }
                super.onPageSelected(position);
            }
        });

        // Menu Button Logic (Pop up menu)
        ImageView menuButton = findViewById(R.id.menu_button); //Trigger pop up menu when clicked
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Method below creates a pop up menu attached to view
                PopupMenu popupMenu = new PopupMenu(MainActivity.this,view);

                //Method to inflate menu items from menu.xml
                popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());

                //The code below is used to handle menu item clicks
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        CharSequence title = menuItem.getTitle();
                        if (title.equals("Settings")) { //Navigate to SettingsActivity
                            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                            return true;
                        } else if (title.equals("Log out")) { //Signing out and navigate to SingInActivity
                            mAuth.signOut();
                            startActivity(new Intent(MainActivity.this, SignInActivity.class));
                            return true;
                        } else if (title.equals("Add Contact")){ //Navigate to AddContact activity
                            startActivity(new Intent(MainActivity.this, AddContact.class));
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        // Camera Button Logic
        ImageView camera = findViewById(R.id.camera_button); //To find ImageView with camera_button id to act as camera button
        camera.setOnClickListener(view -> { //Click listener for the camera button, activated when user tap the button

            //Below is used to create an intent to start or open CameraActivity
            Intent intent = new Intent(MainActivity.this, CameraActivity.class);
            startActivity(intent); //Launches the Camera Activity
        });

        // Initialize SensorManager and accelerometer
        ////sensorManager is a variable to store the initialization of sensor manager and return system service for accessing sensor
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        //Accelerometer variable is used to retrieve the accelerometer sensor from sensorManager and fetch the default accelerometer of the device
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    //Method is called when activity becomes visible and start interacting with user after returning from paused state
    protected void onResume() {
        super.onResume();
        // Register the sensor listener when the activity resumes
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    //Method generally called when activity is about to go to the background either due to leaving or another app starting
    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the sensor listener when the activity pauses
        sensorManager.unregisterListener(this);
    }

    //Method is called when there is a change in sensor data
    @Override
    public void onSensorChanged(SensorEvent event) { //check if sensor event is from accelerometer
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0]; //extract x acceleration values from sensor event
            float y = event.values[1]; //extract y acceleration values from sensor event

            // Check device orientation
            if (Math.abs(x) > Math.abs(y)) {//check whether acceleration in x is greater than y
                // Device is in landscape orientation, lock to portrait
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }
    }

    //The method called when the accuracy of sensor changed
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }
}
