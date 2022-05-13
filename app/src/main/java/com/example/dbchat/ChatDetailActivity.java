package com.example.dbchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dbchat.Adapter.ChatAdapter;
import com.example.dbchat.Models.MessageModel;
import com.example.dbchat.databinding.ActivityChatDetailBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
//this class is the responsable for the chat detail
public class ChatDetailActivity extends AppCompatActivity {
//initialization of binding + firebase instance and database
    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //the binding process
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //hidding the action bar for a better design
        getSupportActionBar().hide();
        //getting firebase instances
        database=FirebaseDatabase.getInstance();
        auth =FirebaseAuth.getInstance();
        //getting our user id from firebase auth
        final String senderId= auth.getUid();
        //getting the infos from the intent that has extra
        String recieveId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic");
        //showing our user name in the top of the layout
        binding.userName.setText(userName);
        //loading our user picture with picasso
        // (it's still statique because we have a problem loading image from firebase storagre)
        Picasso.get().load(profilePic).placeholder(R.drawable.avatar).into(binding.profileImage);
        //binging with the arrow and setting an intent to the main activity
        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//simple intent to main activity
                Intent intent= new Intent(ChatDetailActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        //initializing message model + instance of chatAdapter
        final ArrayList<MessageModel>messageModels=new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter(messageModels,this,recieveId);
        //binding the chatRecyclerView with adapter and the linearlayout with the context
        binding.chatRecyclerView.setAdapter(chatAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);
        //defining te sender and receiver
        final String senderRoom= senderId + recieveId;
        final String receiverRoom= recieveId + senderId;
        //loading the messages
        database.getReference().child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageModels.clear();//clear old data
                        for(DataSnapshot snapshot1 : snapshot.getChildren())
                        {//retriving messages
                            MessageModel model = snapshot1.getValue(MessageModel.class);
                            model.setMessageId(snapshot1.getKey());
                            messageModels.add(model);

                        }//notifiying if a change has been made
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        //binding with send button and sending process
        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override//getting the text field
            public void onClick(View view) {
                String message = binding.enterMessage.getText().toString();
                final MessageModel model = new MessageModel(senderId,message);
                model.setTimestamp(new Date().getTime());
                binding.enterMessage.setText("");
                //pushing to new message to the database
                database.getReference().child("chats")
                        .child(senderRoom)
                        .push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override//if the operation successed the new message will be shown in chat
                    public void onSuccess(Void unused) {
                        database.getReference().child("chats")
                                .child(receiverRoom)
                                .push()//pushed throw the model
                                .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
                    }
                });

            }
        });

    }
}