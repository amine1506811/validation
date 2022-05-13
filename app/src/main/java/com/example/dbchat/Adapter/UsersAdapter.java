package com.example.dbchat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbchat.ChatDetailActivity;
import com.example.dbchat.Models.Users;
import com.example.dbchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
//this adapter is the responsable of showing the users + the last message that will be shown next to user
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.viewHolder>{
//initialazing the user in a arraylist
    ArrayList<Users> list;
    //defining the context
    Context context;
//model class for users
    public UsersAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override//creating the viewholder to call the view
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sample_show_user,parent,false);
        return new viewHolder(view);
    }

    @Override//binding with the view and using the library picasso to load
    // the image from uri that will be found in firebase realtime database
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Users users=list.get(position);
        Picasso.get().load(users.getProfilePic()).placeholder(R.drawable.avatar3).into((holder.image));
        holder.userName.setText(users.getUserName());

        //to set last message using the timestamp
        FirebaseDatabase.getInstance().getReference().child("chats")
                .child(FirebaseAuth.getInstance().getUid()+users.getUserId())
                .orderByChild("timesdtamp")
                .limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override//getting the last message and parsing it toString
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren()){
                            for(DataSnapshot snapshot1:snapshot.getChildren()){
                                holder.LastMessage.setText(snapshot1.child("message").getValue().toString());

                            }
                        }
                    }

                    @Override//in case that a problem accured no
                    // traitment done here cause it happen's 1time in 1 million year xD (firebase is down for exemple )
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        //when clicking on a user we will take with us the selected user infos to the next activty (ChatDetailActivity)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatDetailActivity.class);
                intent.putExtra("userId",users.getUserId());
                intent.putExtra("profilePic",users.getProfilePic());
                intent.putExtra("userName",users.getUserName());
                context.startActivity(intent);
            }
        });

    }

    @Override//getting the number of items
    public int getItemCount() {
        return list.size();
    }
//this recycler view is for the last message and data that will appear next in the user frame
    public class viewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView userName,LastMessage;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.profile_image);
            userName=itemView.findViewById(R.id.userNameList);
            LastMessage= itemView.findViewById(R.id.LastMessage);
        }
    }
}
