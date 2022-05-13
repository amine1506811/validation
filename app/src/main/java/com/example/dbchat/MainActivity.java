package com.example.dbchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.dbchat.Adapter.FragmentsAdapter;
import com.example.dbchat.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    //binding the layout to the java
    ActivityMainBinding binding;
    //instance of firebase auth
    FirebaseAuth mAuth;
    //initialisation for the fragment adapter
    FragmentsAdapter fragmentsAdapter;
    TabLayout tabLayout;
    ViewPager2 viewPager2;

    //broadcast
    BroadcastReceiver broadcastReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //the binding process
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //getting instance for firebase instance
        mAuth = FirebaseAuth.getInstance();
    //Broadcast call
        broadcastReceiver = new InternetReceiver();
        InternetStatus();
        //fragment
        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager);
        fragmentsAdapter=new FragmentsAdapter(this);
        viewPager2.setAdapter(fragmentsAdapter);
        //creation of tabs when creating the activity
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //getting the wanted page of fragment
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

    }

    //BroadCast activation
    public void InternetStatus(){
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }
    //BroadCast ending when stopping the app
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    //creation of option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    //choosing the activity that will be launched when clicking on one of them
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case  R.id.settings:
               // Toast.makeText(this, "setting is cliked", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent2);
            break;

            case R.id.groupChat:
               // Toast.makeText(this, "group chat clicked", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(MainActivity.this,GroupChatActivity.class);
            startActivity(intent1);
                break;

                //logout of the account with firebase auth proprty
            case R.id.logout:
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this,SignInActivity.class);
                startActivity(intent);
            break;
        }

        //returning the selected activity
        return super.onOptionsItemSelected(item);
    }
}