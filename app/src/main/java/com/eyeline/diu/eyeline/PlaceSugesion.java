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
import java.util.List;
import java.util.Locale;

public class PlaceSugesion extends MainActivity {
    RequestQueue rqt;
    TextToSpeech tt1;
    private String text;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    Context context;
    Handler mHandler;
    Runnable mRunnable;


    public PlaceSugesion() {


    }


    public   List<String> getSugession(Context context,String pram){
        this.context=context;
       final List<String> list=new ArrayList<String>();
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
                                        //Log.d("PlaceUrl2", "speek now");


                                    }
                                    list.add(giveSugesion);

                                    // promptSpeechInput();

                                    if (text=="yes"){
                                        Log.d("Text",text);
                                    }

                                }

                                for (int i=0;i<list.size();i++){
                                    Log.d("PlaceUrl2", list.size()+list.get(i));
                                }





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

        return list;
    }

    private void demo() {
        Log.d("demo","demo");
    }


}

