/*
package com.example.dbchat.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.dbchat.Fragments.CallsFragment;
import com.example.dbchat.Fragments.ChatsFragment;
import com.example.dbchat.Fragments.StatusFragment;



public class FragmentsAdapter extends FragmentPagerAdapter {
    public FragmentsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new ChatsFragment();
            case 1: return new StatusFragment();
            case 2: return new CallsFragment();
            default: return new ChatsFragment();
        }

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        String title = null;
        if(position== 0){
            title="CHATS";
        }
        if(position== 1){
            title="STATUS";
        }
        if(position== 2){
            title="CALLS";
        }

        return title;
    }
}*/
package com.example.dbchat.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.dbchat.Fragments.CallsFragment;
import com.example.dbchat.Fragments.ChatsFragment;
import com.example.dbchat.Fragments.StatusFragment;
public class FragmentsAdapter extends FragmentStateAdapter {
//this class in the adapter who do the job for fragments
    public FragmentsAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){//choosing the fragment by possition
            case 0: return new ChatsFragment();
            case 1: return new StatusFragment();
            case 2: return new CallsFragment();
            default: return new ChatsFragment();
        }
    }

    @Override//getting the number of items
    public int getItemCount() {
        return 3;
    }
}