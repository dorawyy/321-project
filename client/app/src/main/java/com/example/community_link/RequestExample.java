package com.example.community_link;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RequestExample extends AppCompatActivity {

    private TextView res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_example);

        res = findViewById(R.id.res);
    }

    public void getServicesButton(View view) {
        JSONObject conditions = new JSONObject();
        try {
            conditions.put("date-min", "2020-10-15");
            conditions.put("lat-min", 48.61284);
            conditions.put("lat-max", 56.73);
        }catch(JSONException e) {
            e.printStackTrace();
        }

        Response.Listener getServicesResponseCallback = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("response", response.toString());
                res.setText(response.toString());
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

    }

    public void addServiceButton(View view) {
        JSONObject service = new JSONObject();
        try {
            service.put("name", "A service");
            service.put("date", "2020-10-15");
            service.put("dow", "Monday");
            service.put("time", "12:30:00");
            service.put("lat", 48.61284);
            service.put("longi", 56.73);
            service.put("owner", "Me");
            service.put("type", "Food");
            service.put("description", "There will be some food!");
        }catch(JSONException e) {
            e.printStackTrace();
        }

        Response.Listener addServiceResponseCallback = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("response", response.toString());
                res.setText(response.toString());
            }
        };

        Response.ErrorListener errorCallback = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("HTTP response didn't work");
                System.out.println(error.toString());
            }
        };

        CommunityLinkApp.requestManager.addService(service, addServiceResponseCallback, errorCallback);
    }

    public void getMessagesButton(View view) {
        JSONObject conditions = new JSONObject();
        try {
            conditions.put("user1", "me");
            conditions.put("user2", "you");
            conditions.put("newest", "2020-10-22 10:51:33");
        }catch(JSONException e) {
            e.printStackTrace();
        }


        Response.Listener getMessagesResponseCallback = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("response", response.toString());
                res.setText(response.toString());
            }
        };

        Response.ErrorListener errorCallback = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("HTTP response didn't work");
                System.out.println(error.toString());
            }
        };

        CommunityLinkApp.requestManager.getMessages(conditions, getMessagesResponseCallback, errorCallback);

    }

    public void addMessageButton(View view) {
        JSONObject message = new JSONObject();
        try {
            message.put("sender", "me");
            message.put("recipient", "you");
            message.put("timestamp", "2020-10-22 10:51:33");
            message.put("content", "This is a message to you from me");
        }catch(JSONException e) {
            e.printStackTrace();
        }

        Response.Listener addServiceResponseCallback = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("response", response.toString());
                res.setText(response.toString());
            }
        };

        Response.ErrorListener errorCallback = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("HTTP response didn't work");
                System.out.println(error.toString());
            }
        };

        CommunityLinkApp.requestManager.addMessage(message, addServiceResponseCallback, errorCallback);
    }
}