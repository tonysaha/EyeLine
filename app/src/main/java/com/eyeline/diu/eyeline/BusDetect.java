package com.eyeline.diu.eyeline;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BusDetect {
    DatabaseReference mDatabaseReference;
    FirebaseDatabase database;
    public BusDetect() {

        database=FirebaseDatabase.getInstance();
        mDatabaseReference=database.getReference();
        mDatabaseReference.child("Drivers");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.child("0TbI0GkY3dPtiQVLL5NIzJT6ckK2").child("destination").getValue(String.class);
                Log.d("Rdata", "Value is: " + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


                Log.d("Rdata", "Value is: " + databaseError);

            }
        });

    }
}
