package com.example.finalproject16;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class InfoFetcher {
    /**
     * @Pre
     *
     * @Post
     *      onDataRecieverListener interface implemented
     *
     * Source Zybooks 6.9
     * */
    public interface OnDataReceivedListener {
        void onDataReceived(List<String> info);
        void onErrorResponse(VolleyError error);
    }

    private RequestQueue mRequestQueue;
    private String url = "https://6l5z0zjqx5.execute-api.us-east-1.amazonaws.com/prod/user";
    private String url2 = "https://6l5z0zjqx5.execute-api.us-east-1.amazonaws.com/prod/user/getinfo";

    /**
     * @Pre
     *      mRequestQueue is null
     * @Post
     *      mRequestQueue is initialized and set to a volley request
     *
     * Source Zybooks 6.9
     * */
    public InfoFetcher(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    /**
     * @Pre
     *      url contains an api call
     *      listener wants an api call
     * @Post
     *      listener receives parsed info to be displayed or an error message
     *
     * Source Zybooks 6.9
     * */
    public void getUser(final OnDataReceivedListener listener, String email) throws JSONException {

        JSONObject json = new JSONObject();
        json.put("email", email);

        // Request all subjects
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.POST, url2, json, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("INFO", response.toString());
                        List<String> info = parseJson2(response);
                        listener.onDataReceived(info);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", error.toString());
                        listener.onErrorResponse(error);
                    }
                });

        mRequestQueue.add(request);
    }

    /**
     * @Pre
     *      url contains an api call
     *      listener wants an api call
     * @Post
     *      listener receives parsed info to be displayed or an error message
     *
     * Source Zybooks 6.9
     * */
    public void getAll(final OnDataReceivedListener listener) {

        // Request all subjects
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("INFO", response.toString());
                        List<String> info = parseJson(response);
                        listener.onDataReceived(info);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", error.toString());
                        listener.onErrorResponse(error);
                    }
                });

        mRequestQueue.add(request);
    }

    /**
     * @Pre
     *      url contains an api call
     *      listener wants an api call
     * @Post
     *      listener receives parsed info to be displayed or an error message
     *
     * Source Zybooks 6.9
     * */
    public void updateuser(final OnDataReceivedListener listener, String email) throws JSONException {

        JSONObject json = new JSONObject();
        json.put("email", email);
        json.put("points", 1);

        // Request all subjects
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        List<String> info = parseJson(response);
                        Log.d("INFO", info.get(0));
                        listener.onDataReceived(info);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onErrorResponse(error);
                    }
                });

        mRequestQueue.add(request);
    }

    /**
     * @Pre
     *      url contains an api call
     *      listener wants an api call
     * @Post
     *      listener receives parsed info to be displayed or an error message
     *
     * Source Zybooks 6.9
     * */
    public void createUser(final OnDataReceivedListener listener, String email) throws JSONException {

        JSONObject json = new JSONObject();
        json.put("email", email);

        // Request all subjects
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.PUT, url, json, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("INFO", response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onErrorResponse(error);
                    }
                });

        mRequestQueue.add(request);
    }

    /**
     * @Pre
     *      json is from an api call with weather info
     * @Post
     *      info is a list of strings to be displayed with weather info
     *      info contains: temp, main conditions, condition description, wind speed, feels like temp
     *
     * Source Zybooks 6.9
     * */
    private List<String> parseJson(JSONObject json) {

        // Create a list of subjects
        List<String> info = new ArrayList<>();
        Log.d("JSON", json.toString());
        try {
            JSONArray items = json.getJSONObject("body").getJSONArray("Items");
            for(int j = 0; j < items.length(); j++){
                info.add(items.getJSONObject(j).getString("Email"));
                info.add(items.getJSONObject(j).getString("Points"));
            }

            Log.d("PARSED", info.toString());

        }
        catch (Exception e) {
            Log.e("Error", "One or more fields not found in the JSON data");
        }

        return info;
    }

    /**
     * @Pre
     *      json is from an api call with weather info
     * @Post
     *      info is a list of strings to be displayed with weather info
     *      info contains: temp, main conditions, condition description, wind speed, feels like temp
     *
     * Source Zybooks 6.9
     * */
    private List<String> parseJson2(JSONObject json) {

        // Create a list of subjects
        List<String> info = new ArrayList<>();
        Log.d("JSON", json.toString());
        try {
            info.add(json.getJSONObject("body").getJSONObject("Item").getString("Email"));
            Log.d("PARSED", info.toString());

        }
        catch (Exception e) {
            Log.e("Error", "One or more fields not found in the JSON data");
        }

        return info;
    }
}
