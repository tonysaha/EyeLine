package com.eyeline.diu.eyeline;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class PlaceSugesion extends AppCompatActivity {
    RequestQueue rqt;
    TextToSpeech tt1;
    private String text;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    Context context;

    public PlaceSugesion( Context context,String pram) {
        this.context=context;
        tt1=new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if(status != TextToSpeech.ERROR) {
                    tt1.setLanguage(Locale.UK);

                }

            }
        });


        rqt = Volley.newRequestQueue(context);
        try {


            StringBuilder googlePlacesUrl =
                    new StringBuilder("https://maps.googleapis.com/maps/api/place/autocomplete/json?");
            googlePlacesUrl.append("input=").append(pram);

            googlePlacesUrl.append("&key=" + "AIzaSyAowCbmyBSJtLUGn_FKoeGuVj4Sk_z5Rfw");

            String url = googlePlacesUrl.toString();
           url=url.replace(" ",",");
            Log.d("PlaceUrll", url);


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {


                            // Log.d("points", "ok");



                           // Log.d("PlaceUrl", response.toString());
                                Log.d("points", "ok");
                                //JSONArray stepPath = response.getJSONArray("predictions");     // Log.d("points", stepPath.toString());
                            try{
                               JSONArray jsonArray=response.getJSONArray("predictions");


                               for (int i=0;i<jsonArray.length();i++){
                                   JSONObject sugOb=jsonArray.getJSONObject(i);
                                   String giveSugesion=sugOb.getString("description");

                                   Log.d("PlaceUrl1", giveSugesion);
                                   tt1.speak("Number "+i+" "+giveSugesion, TextToSpeech.QUEUE_FLUSH, null);
                                   while (tt1.isSpeaking()){
                                       Log.d("PlaceUrl2", "speek now");
                                   }
                                  // promptSpeechInput();

                                   if (text=="yes"){
                                       Log.d("Text",text);
                                   }
                               }

                                //promptSpeechInput();



                            }catch (JSONException e){
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.d("points", "error");
                        }
                    });
            rqt.add(jsonObjectRequest);
        } catch (Exception e) {
            Log.d("points", e.toString());


        }

    }


    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Yes Or No");
        try {
            text="";
             startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(context,
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Handler handler = new Handler();
        String s="ok";
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String mytext = result.get(0);

                    if(mytext =="Yes"||mytext=="yes"){

                        text="yes";

                    }


                }
            }
        }
    }
}
