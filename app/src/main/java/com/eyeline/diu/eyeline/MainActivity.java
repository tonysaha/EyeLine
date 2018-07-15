package com.eyeline.diu.eyeline;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener{
    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private android.location.Location location;
    private GoogleApiClient googleApiClient;
    private LocationListener locationListener;
    private Geocoder geocoder;
    private List<Address>addressList;
    private LocationRequest locationRequest;


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
        googleApiClient.disconnect();


    }
//End Location.........................
    String request="";
    String destination="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Handler handler = new Handler();
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    final String myText = result.get(0);
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
                                t1.speak("Ok All data are clear ..", TextToSpeech.QUEUE_FLUSH, null);
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
                    else if (destination.equals("")){

                            destination=myText;
                            t1.speak("Your Destination is "+destination, TextToSpeech.QUEUE_FLUSH, null);
                        }
                        else{
                            t1.speak("Your Destination is "+destination, TextToSpeech.QUEUE_FLUSH, null);
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

    @Override
    public void onLocationChanged(Location location) {

        try {
            addressList=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            //Address.setText(addressList.get(0).getAddressLine(0));
            Address=addressList.get(0).getAddressLine(0);
           // System.out.println(Address);

           // Toast.makeText(getBaseContext(),"Value: "+Address,Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //End Current Location........



}
