package com.eyeline.diu.eyeline;


import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BusDetect extends AppCompatActivity implements MapRoute{

    FirebaseFirestore db;
    FirebaseDatabase fd;
    DatabaseReference dbReference;
    List<BusLocationModel> cities;
    public BusDetect(final Context context, final LatLng mylocation) {
        cities = new ArrayList<>();
      /*  db=FirebaseFirestore.getInstance();

        Map<String,Object> note=new HashMap<>();
        note.put("title","tony saha");
        db.collection("Notebook").document("note").set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context,"Data save",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,"Data not save",Toast.LENGTH_SHORT).show();
            }
        });*/



      fd=FirebaseDatabase.getInstance();
      dbReference=fd.getReference("DriversLocation");

      dbReference.addValueEventListener(new ValueEventListener() {

          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {


/*
              Map<String,String> map=(Map)dataSnapshot.getValue();
              String t=map.get("latitude");
              Log.d("data", t);*/

              BusLocationModel bus;
              Gson gson = new Gson();
              for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                  String jsonString = gson.toJson(postSnapshot.getValue());
                  String lat=postSnapshot.child("latitude").getValue().toString();
                  String lon=postSnapshot.child("longitude").getValue().toString();
                  LatLng buslocation=new LatLng(Double.parseDouble(lat),Double.parseDouble(lon));
                  boolean distance=getDistancestep(mylocation,buslocation);
                  if (distance==true){
                      bus=new BusLocationModel(postSnapshot.getKey(),lat,lon);


                      cities.add(bus);
                  }


              }


              dbReference.removeEventListener(this);

              for (BusLocationModel list:cities){
                  Log.d("data", "Use id "+list.getId()+" latitude "+list.getLatitude()+" longitude "+list.getLongitude());
                  getlatlonpoint1(list.getId());
              }
              Log.d("data", ".................................................................................");
              /*Gson gson = new Gson();
              String jsonString = gson.toJson(dataSnapshot.getValue());

              Log.d("data",jsonString);
              try {
                  JSONObject mainObject = new JSONObject(jsonString);
                 // JSONArray jsonArray=new JSONArray(mainObject);
                  mainObject.length();
                  for (int i=0;i<mainObject.length();i++){
                      //Log.d("data", String.valueOf(mainObject.get()));
                  }
                // String lat= jsonArray.get(0).toString();

              } catch (JSONException e) {
                  e.printStackTrace();
              }*/



             /* int i=0;
              for(DataSnapshot driverlocationlist:dataSnapshot.getChildren()){
                  Log.d("data"+i,dataSnapshot.getValue().toString());

                 // BusLocationModel buslist=dataSnapshot.getValue(BusLocationModel.class);
                  i++;
              }*/
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

              Log.d("data","erorr");
          }
      });

      Log.d("data", String.valueOf(cities.size()));

    }

    //get bus destination and from
    private void getlatlonpoint1(final String id) {
      DatabaseReference dbreReference=FirebaseDatabase.getInstance().getReference("Drivers").child(String.valueOf(id));
      dbreReference.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
              String point1= String.valueOf(dataSnapshot.child("from").getValue());
              String point2= String.valueOf(dataSnapshot.child("destination").getValue());


              Log.d("data1","--------------------");
              Log.d("data1",point1);
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
      });

    }
// Check area ....for bus//////
    private boolean getDistancestep(LatLng mylocation, LatLng buslocation) {
        boolean flag=false;
        //Log.d("mylocation", String.valueOf(mylocation.latitude));
        Location location1=new Location("LocationA");
        location1.setLatitude(mylocation.latitude);
        location1.setLongitude(mylocation.longitude);
        Location location2=new Location("LocationB");
        location2.setLatitude(buslocation.latitude);
        location2.setLongitude(buslocation.longitude);
        double distance =location1.distanceTo(location2);

        if(distance<2000){
            flag=true;

        }
        return flag;

    }
}
