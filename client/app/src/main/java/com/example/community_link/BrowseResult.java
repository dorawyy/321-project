package com.example.community_link;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BrowseResult extends AppCompatActivity {

    private List<ServiceData> sdList = new ArrayList<ServiceData>();
    private FusedLocationProviderClient fusedLocationClient;
    private static final int REQUEST_CODE = 101;
    private int i = 0;
    private TextView txv;
    private int size;
    private Location userLoc;


    @SuppressLint("SetTextI18n")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_result);

        /*<--------------HARDCODING----------------------->*/
        txv = (TextView) findViewById(R.id.browseResult);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //getLocation();

        // Retrieve search criteria for services
        Float currLat = getIntent().getExtras().getFloat("currLat",49);
        Float currLong = getIntent().getExtras().getFloat("currLong", -123);
        double dist = getIntent().getExtras().getFloat("dist", 15);
        double latDiff = dist / 111;
        double longDiff = dist / (Math.cos(Math.toRadians(currLat)) * 111);
        double latMin = currLat - latDiff;
        double latMax = currLat + latDiff;
        double longMin = currLong - longDiff;
        double longMax = currLong + longDiff;

        String title = getIntent().getExtras().getString("title");
        System.out.println("Title: " + title);

        JSONObject conditions = new JSONObject();
        try {
            conditions.put("date-min", "2020-10-15");
            conditions.put("name", title);
            conditions.put("lat-min", latMin);
            conditions.put("lat-max", latMax);
            conditions.put("longi-min", longMin);
            conditions.put("longi-max", longMax);
        }catch(JSONException e) {
            e.printStackTrace();
        }

        System.out.println(conditions);

        Response.Listener getServicesResponseCallback = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new Gson();
                for(int index=0;index<response.length();index++) {
                    try {
                        sdList.add(gson.fromJson(response.getString(index), ServiceData.class));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                size = sdList.size();
                if (size == 0) {
                    System.out.println("No services");
                    CharSequence errorMess = "Sorry, no services found. Please enter different search criteria";
                    Toast errorToast = Toast.makeText(getApplicationContext(), errorMess, Toast.LENGTH_LONG);
                    errorToast.setGravity(Gravity.CENTER, 0, 0);
                    errorToast.show();

                    Intent browseService = new Intent(getApplicationContext(), BrowseServiceCond.class);
                    startActivity(browseService);
                } else {
                    txv.setText(sdList.get(i).toString());
                }
            }
        };

        Response.ErrorListener errorCallback = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("HTTP response didn't work");
                System.out.println(error.toString());
            }
        };

        CommunityLinkApp.requestManager.getServices(conditions, getServicesResponseCallback, errorCallback);

        /*<--------------HARDCODING----------------------->*/

    }

    public void goPrev(View view){
        if(i != 0){
            i--;
        }
        txv.setText(sdList.get(i).toString());

    }

    public void goNext(View view){
        if(i != size-1) {
            i++;
        }
        txv.setText(sdList.get(i).toString());
    }

    //This isn't working for some reason
/*
    private void getLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            System.out.println("Couldn't get permission _____________-------------------__________________---------");
            return;
        }
        Task<Location> task = fusedLocationClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    System.out.println("GOT LOCATION _____________-------------------__________________---------");
                    userLoc = location;
                    Toast.makeText(getApplicationContext(), userLoc.getLatitude() + "" + userLoc.getLongitude(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }*/

    public void viewOnMap(View view) {
        Intent mapActivity = new Intent(this, MapActivity.class);
        mapActivity.putExtra("service", sdList.get(i));
        startActivity(mapActivity);
    }
}