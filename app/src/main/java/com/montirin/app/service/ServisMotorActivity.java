package com.montirin.app.service;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.montirin.app.R;
import com.montirin.app.adapter.UsersAdapter;
import com.montirin.app.model.UsersItem;

public class ServisMotorActivity extends AppCompatActivity {

    RecyclerView recview;
    UsersAdapter adapter;
    TextView emptyData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servis_motor);

        recview = findViewById(R.id.recview);
        emptyData = findViewById(R.id.empty_view);
        recview.setLayoutManager(new LinearLayoutManager(this));
        FirebaseDatabase Datab = FirebaseDatabase.getInstance();
        Query ref = Datab.getReference("Users").orderByChild("kategori").equalTo("Servis Motor");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    emptyData.setVisibility(View.GONE);
                    recview.setVisibility(View.VISIBLE);
                } else {
                    recview.setVisibility(View.GONE);
                    emptyData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseRecyclerOptions<UsersItem> options =
                new FirebaseRecyclerOptions.Builder<UsersItem>()
                        .setQuery(ref, UsersItem.class)
                        .build();
        adapter = new UsersAdapter(options);
        recview.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}