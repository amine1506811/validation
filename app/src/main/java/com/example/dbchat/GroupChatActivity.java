package com.example.dbchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.dbchat.Adapter.ChatAdapter;
import com.example.dbchat.Models.MessageModel;
import com.example.dbchat.databinding.ActivityChatDetailBinding;
import com.example.dbchat.databinding.ActivityGroupChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
//this class will use chat adapter to show groupe messages
public class GroupChatActivity extends AppCompatActivity {
//binding def
    ActivityGroupChatBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //as usual bing with the view
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//hiding action bar for a better design
    getSupportActionBar().hide();
//binding with backArrow item and setting the intent to main activity
    binding.backArrow.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {//simple intent to the mainActivity
            Intent intent=new Intent(GroupChatActivity.this,MainActivity.class);
            startActivity(intent);
        }
    });//getting instance from firebase + arraylist for messages
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final ArrayList<MessageModel>messageModels=new ArrayList<>();

//getting the id of the current user with the help of firebase auth
    final String senderId= FirebaseAuth.getInstance().getUid();
    //showing the current user name
    binding.userName.setText("Group Chat");
    //linking the chatAdapter with chatRecyclerView + messageModels
    final ChatAdapter adapter= new ChatAdapter(messageModels,this);
    binding.chatRecyclerView.setAdapter(adapter);

        //linking the layoutManager with recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);

        //loading the groupe messages
        database.getReference().child("Group Chat")
                .addValueEventListener(new ValueEventListener() {
                    @Override//
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageModels.clear();//clearing old data
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){//using for to parse all
                            // message throw the message model
                            MessageModel model = dataSnapshot.getValue(MessageModel.class);
                            messageModels.add(model);
                        }
                        adapter.notifyDataSetChanged();//notifiying the adapter if any change have been made
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        //Binding with send button and pushing the new message to the database
        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String message =binding.enterMessage.getText().toString();//getting text from field
                final MessageModel model = new MessageModel(senderId,message);//prepare message + sender id
                model.setTimestamp(new Date().getTime()); //getting the timestamp

                binding.enterMessage.setText("");//binging with field of message
                database.getReference().child("Group Chat")
                        .push()
                        .setValue(model)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {//showing a text when the process is done
                                Toast.makeText(GroupChatActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }
}