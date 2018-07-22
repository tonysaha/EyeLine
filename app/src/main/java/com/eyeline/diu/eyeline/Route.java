package com.eyeline.diu.eyeline;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Route extends AppCompatActivity implements OnMapReadyCallback{
    private LatLng  mylocation;
    private LatLng mydestination;
     Context context;

    RequestQueue rq;
    public Route(Context context,LatLng mylocation, LatLng mydestination) {
        this.mylocation = mylocation;
        this.mydestination = mydestination;

        Log.d("LocationUrl", "ok done");
        rq = Volley.newRequestQueue(context);
         getroute();

    }

    public LatLng getMylocation() {
        return mylocation;
    }

    public void setMylocation(LatLng mylocation) {
        this.mylocation = mylocation;
    }

    public LatLng getMydestination() {
        return mydestination;
    }

    public void setMydestination(LatLng mydestination) {
        this.mydestination = mydestination;
    }

    private void getroute() {

        try {
            Log.d("LocationUrl", String.valueOf(getMydestination().latitude));
            String endLat = String.valueOf(getMydestination().latitude);
            String endLon = String.valueOf(getMydestination().longitude);
            String stLat = String.valueOf(getMylocation().latitude);
            String stLon = String.valueOf(getMylocation().longitude);
            StringBuilder googlePlacesUrl =
                    new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
            googlePlacesUrl.append("origin=").append(stLat).append(",").append(stLon);
            googlePlacesUrl.append("&destination=").append(endLat).append(",").append(endLon);

            googlePlacesUrl.append("&key=" + "AIzaSyAowCbmyBSJtLUGn_FKoeGuVj4Sk_z5Rfw");

            String url = googlePlacesUrl.toString();
            Log.d("LocationUrl", url);


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {


                            // Log.d("points", "ok");
                            try {
                                Log.d("points", "ok");
                                JSONArray stepPath = response.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");

                                // Log.d("points", stepPath.toString());

                                int count = stepPath.length();
                                String[] polyins_array = new String[count];


                                for (int i = 0; i < count; i++) {
                                    JSONObject list = new JSONObject(String.valueOf(stepPath.get(i)));

                                    String polygon = list.getString("html_instructions");
                                    polyins_array[i] = polygon;

                                    Log.d("points", polygon);
                                }


                                // Log.d("Location",contacts.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.d("points", "error");
                        }
                    });
            rq.add(jsonObjectRequest);
        } catch (Exception e) {
            Log.d("points", e.toString());


        }
    }

        @Override
        public void onMapReady (GoogleMap googleMap){

        }
    }
