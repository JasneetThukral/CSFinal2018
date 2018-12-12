package com.example.ishani.csfinal2018;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public final class MainActivity extends AppCompatActivity {
    private static final String TAG = "FinalProject:Main";

    private static RequestQueue requestQueue;

    /**
     * Run when our activity comes into view.
     *
     * @param savedInstanceState state that was saved by the activity last time it was paused
     */

    private EditText adviceSearch;
    /**
     * Tv is textview.
     */
    private TextView tv;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestQueue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_main);

        //tv = (TextView) findViewById(R.id.searchResult);


        final Button searchButton = findViewById(R.id.search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Search button clicked");
                tv = (TextView) findViewById(R.id.searchResult);
                adviceSearch = findViewById(R.id.searchAdvice);
                String query = adviceSearch.getText().toString();
                Log.e(TAG, query);

                searchButton(query, tv);

            }
        });

        final Button startAPICall = findViewById(R.id.startAPICall);
        startAPICall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Start API button clicked");
                tv = (TextView) findViewById(R.id.searchResult);
                startAPICall(tv);
            }
        });
    }

    /**
     * Make an API call.
     * @param t view to change
     */
    void startAPICall(final TextView t) {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://api.adviceslip.com/advice",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Log.d(TAG, response.toString());
                            try {
                                t.setText("\n" + ((JSONObject) response.get("slip")).get("advice").toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.w(TAG, error.toString());
                    t.setText(error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * search for advice
     * Make an API call.
     * @param query search query
     * @param v textview for which random quote is generated
     */
    void searchButton(final String query, final TextView v) {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://api.adviceslip.com/advice/search/" + query,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Log.d(TAG, response.toString());
                            try {
                                String toSet = "";
                                int length = ((JSONArray) response.get("slips")).length();
                                /*if (length > 5) {
                                length = 5;
                                }*/
                                for (int i = 0; i < length; i++) {
                                    String temp = (((JSONArray) response.get("slips")).getJSONObject(i)).get("advice").toString();
                                    toSet = toSet + "\n\n" + temp;
                                }
                                v.setText(toSet);
                            } catch (Exception e) {
                                String toSet = null;
                                try {
                                    toSet = ("\n" + ((JSONObject) response.get("message")).get("text").toString());
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                                v.setText(toSet);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.w(TAG, error.toString());
                    v.setText(error.toString());
                }

            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

