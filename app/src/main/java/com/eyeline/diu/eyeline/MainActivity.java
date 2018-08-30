package com.eyeline.diu.eyeline;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener{

    DatabaseHelper mydb;
    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private android.location.Location location;
    private GoogleApiClient googleApiClient;
    private LocationListener locationListener;
    private Geocoder geocoder;
    private List<Address>addressList;
    private LocationRequest locationRequest;

    private LatLng mylocation;
    private List<String> sugessionList;


    TextToSpeech t1;

public String Address;
public String CurrentLocation;
public String Destination;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);


        //Location...........................
        geocoder=new Geocoder(this);
        //End Location.....................


        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);

                }

            }
        });





        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

    //Location........................
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
      //End Location.............

    }
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }
//Location............
    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
       // googleApiClient.disconnect();


    }
//End Location.........................
    String request="";
    String destination="";
    String destinationNum="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final Handler handler = new Handler();

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    final String myText = result.get(0);
                    destinationNum=result.get(0);


                    if (myText.equals("clear")||!request.equals("travel")) {
                        switch (myText) {

                            case "current location":
                                txtSpeechInput.setText(Address + "ok");
                                // Locationc loc=new Locationc(this);
                                //txtSpeechInput.setText(loc.Address2+"ok");
//                            LocationGM lc=new LocationGM();
//                            lc.CurentLagLat();

                                Toast.makeText(getApplicationContext(), Address, Toast.LENGTH_SHORT).show();
                                t1.speak(Address, TextToSpeech.QUEUE_FLUSH, null);

//                            txtSpeechInput.setText(String.valueOf(lc.ck));

                                break;
                            case "clear":
                                request="clear";
                                destination="";
                                t1.speak("Ok All data are clear ..", TextToSpeech.QUEUE_FLUSH, null);
                                mydb=new DatabaseHelper(MainActivity.this);
                                mydb.deleteall();
                                break;
                            case "Travels":
                                t1.speak("Please give your destination ..wait", TextToSpeech.QUEUE_FLUSH, null);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        t1.speak("Please say", TextToSpeech.QUEUE_FLUSH, null);
                                        promptSpeechInput();
                                        request = "travel";
                                        Toast.makeText(getApplicationContext(), myText, Toast.LENGTH_SHORT).show();

                                    }
                                }, 5000);


                                break;
                            case "travel":
                                t1.speak("Please give your destination ..wait", TextToSpeech.QUEUE_FLUSH, null);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        t1.speak("Please say", TextToSpeech.QUEUE_FLUSH, null);

                                        promptSpeechInput();


                                        request = "travel";
                                        Toast.makeText(getApplicationContext(), myText, Toast.LENGTH_SHORT).show();


                                    }
                                }, 5000);


                                myText.equals(null);
                                break;
                            case "bus":

                                BusDetect busDetect=new BusDetect(MainActivity.this,mylocation);

                                break;
                            case "time":

                                DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
                                String date = df.format(Calendar.getInstance().getTime());
                                t1.speak(date, TextToSpeech.QUEUE_FLUSH, null);
                                while (t1.isSpeaking()){

                                }

                                break;
                            default:
                                txtSpeechInput.setText(myText);

                        }




                    }
                    else{
                        if(myText.equals("change")){
                            destination="";


                        t1.speak("ok change destination ", TextToSpeech.QUEUE_FLUSH, null);

                        Toast.makeText(getApplicationContext(), myText, Toast.LENGTH_SHORT).show();

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    t1.speak("Please say", TextToSpeech.QUEUE_FLUSH, null);
                                    promptSpeechInput();
                                    /*request = "travel";
                                    Toast.makeText(getApplicationContext(), myText, Toast.LENGTH_SHORT).show();*/

                                }
                            }, 5000);

                        }

                    else if (destination.equals("") || myText!="change"){

                            if (destination.equals("")&&myText!="0"&&myText!="1"&&myText!="2"&&myText!="3"&&myText!="4") {
                                destination = myText;
                                PlaceSugesion placeSugesion = new PlaceSugesion();
                                //List<String> list=new ArrayList<>();


                                List<String> list = new ArrayList<String>();

                                list = placeSugesion.getSugession(MainActivity.this, destination);
                          /*  handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    promptSpeechInput();
                                    Log.d("getss", destinationNum);
                                }
                            },5000);*/

                                final List<String> finalList = list;
                                sugessionList=finalList;
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("PlaceUrl2", "okkkkkkkkkkkkk");

                                       /* List<Address> address;
                                        String first=finalList.get(1);*/





                                        t1.speak("please  chose a number " , TextToSpeech.QUEUE_FLUSH, null);
                                        while (t1.isSpeaking()){

                                        }

                                       // promptSpeechInput();


                                    }
                                },4000);

                            }

                            else if(myText.equals("0")||myText.equals("1")||myText.equals("2")||myText.equals("3")||myText.equals("4")) {
                                t1.speak("Please wait a moment.......", TextToSpeech.QUEUE_FLUSH, null);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        t1.speak("Your Final Destination is......"+sugessionList.get(Integer.valueOf(myText)), TextToSpeech.QUEUE_FLUSH, null);


                                        destination=sugessionList.get(Integer.valueOf(myText));



                                           // promptSpeechInput();
                                        try {
                                            String num=String.valueOf(destination);
                                            Log.d("getss", num);
                                            addressList=geocoder.getFromLocationName(destination,1);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                       double endlat= addressList.get(0).getLatitude();
                                        double endlng=addressList.get(0).getLongitude();
                                       // Log.d("getss", String.valueOf();


                                        LatLng endloc=new LatLng(endlat,endlng);
                                        Route route=new Route(MainActivity.this,mylocation,endloc);
                                        request = "travel";
                                        Toast.makeText(getApplicationContext(), myText, Toast.LENGTH_SHORT).show();


                                    }
                                }, 5000);
                                myText.equals(null);
                            }







                        }
                        else if(myText=="1"){
                            String s="ok";
                        }
                        else{
                            t1.speak("Your Destination is "+destination, TextToSpeech.QUEUE_FLUSH, null);
                            LatLng endloc=new LatLng(5.87,3.54);

                            Route route=new Route(MainActivity.this,mylocation,endloc);
                        }

                    }
                }

                break;
            }

        }
    }
//Location....................
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = LocationRequest.create()
                .setInterval(1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,
                locationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    Handler handler = new Handler();
    String instructionp;
    @Override


    public void onLocationChanged(Location location) {

        try {
            //Log.d("Alldata","loc change");
            //Toast.makeText(MainActivity.this, request.toString() ,Toast.LENGTH_SHORT).show();
            addressList=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

            //Address.setText(addressList.get(0).getAddressLine(0));
            Address=addressList.get(0).getAddressLine(0);
           // System.out.println(Address);

            mylocation= new LatLng(location.getLatitude(),location.getLongitude());
           // Toast.makeText(getBaseContext(),"Value: "+Address,Toast.LENGTH_LONG).show();
            mydb=new DatabaseHelper(MainActivity.this);


            if(request.equals("travel")){
                Log.d("Alldata","loc change");
               // mydb.getDatalive();

                //SQLiteDatabase sqLiteDatabase=mydb.getReadableDatabase();
                final Cursor cursor=mydb.getDatalive();
                List posts = new ArrayList();
                {
               //     Log.d("count", cursor.getCount() + " post rows");

                    if (cursor.moveToFirst()) {
                        do {

                           // Log.d("count", String.valueOf(cursor.getString(2)));
                            String steplat=cursor.getString(2);
                            String steplng=cursor.getString(3);
                            LatLng stepDestination=new LatLng(Double.parseDouble(steplat),Double.parseDouble(steplng));
                            double stepdistance=getDistancestep(mylocation,stepDestination);
                            Log.d("count", instructionp+String.valueOf(stepdistance));
                            if(stepdistance<50){
                                final String instruction=cursor.getString(1);
                                final int drow=cursor.getInt(0);
                                instructionp=instruction;

                               // Log.d("count", instructionp+String.valueOf(stepdistance));
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        for(int i=0;i<1;i++) {
                                            t1.speak("You Got An Instruction Now.............................. " + instructionp, TextToSpeech.QUEUE_FLUSH, null);
                                            //  mydb.deleteRow(String.valueOf(cursor.getInt(0)));
                                        }

                                    }
                                }, 5000);

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {


                                        mydb.deleteRow(String.valueOf(drow));
                                        instructionp="";




                                    }


                                }, 15000);




                            }




                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                }
               // Toast.makeText(MainActivity.this, String.valueOf(location.getLatitude()) ,Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //End Current Location........
    public double getDistancestep( LatLng mylocation,LatLng steplocation) {



        Location location1=new Location("LocationA");
        location1.setLatitude(mylocation.latitude);
        location1.setLongitude(mylocation.longitude);
        Location location2=new Location("LocationB");
        location2.setLatitude(steplocation.latitude);
        location2.setLongitude(steplocation.longitude);
        double distance =location1.distanceTo(location2);



        DecimalFormat myFormatter = new DecimalFormat("0.00");
        return distance;



    }


}
