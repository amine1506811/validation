package com.example.dbchat.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.dbchat.Adapter.UsersAdapter;
import com.example.dbchat.Models.Users;
import com.example.dbchat.R;
import com.example.dbchat.databinding.FragmentChatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//this fragment that will the chat users
public class ChatsFragment extends Fragment {



    public ChatsFragment() {
        // Required empty public constructor
    }

//the binding as usual + the initialisation
    FragmentChatsBinding binding;
    ArrayList<Users> list =new ArrayList<>();
    FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(inflater,container,false);
        //instance of data base that will be used next
        database = FirebaseDatabase.getInstance();
        //calling the users adapteur
        UsersAdapter adapter = new UsersAdapter(list,getContext());
        binding.chatRecyclerView.setAdapter(adapter);
        //we used the LinearLayout to display our data
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatRecyclerView.setLayoutManager(layoutManager);
        //getting our data from firebase realtime database
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override//loading data from firebase db
            public void onDataChange(@NonNull DataSnapshot snapshot) {
    list.clear();//clearing the old data to make new
    for(DataSnapshot dataSnapshot : snapshot.getChildren()){//looking for users and
        // retrieving them one by one if they exist
        Users users= dataSnapshot.getValue(Users.class);
        users.setUserId(dataSnapshot.getKey());
        if(!users.getUserId().equals(FirebaseAuth.getInstance().getUid())){
        list.add(users);}
    }//notifiying the adapted if a change have been made
        adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //this to bind with root
        return binding.getRoot();
    }
}