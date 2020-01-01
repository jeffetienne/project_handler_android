package com.example.project_handler.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;

import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.project_handler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private Button callApiButton;
    private TextView nameTextView;
    private TextView descriptionTextView;


    private final static String url = "http://192.168.0.165:26922/api/projet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTextView = (TextView) findViewById(R.id.nameTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        callApiButton = (Button) findViewById(R.id.callApiButton);

        final RequestQueue queue = Volley.newRequestQueue(this);
        callApiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //nameTextView.setText("Name");
                //descriptionTextView.setText("Description");

                new MyAsyncTask().execute(url);
                JsonArrayRequest arrayRequest = new JsonArrayRequest(url,
                        new Response.Listener<JSONArray>(){
                            @Override
                            public void onResponse(JSONArray response) {
                                nameTextView.setText("Name");
                                descriptionTextView.setText("Description");
                                //nameTextView.setText(response.length());
                                for(int i = 0; i<response.length(); i++){
                                    try
                                    {
                                        JSONObject projet = response.getJSONObject(i);
                                        //Log.d("Items :", projet.getString("Name"));
                                        nameTextView.setText(projet.getString("Name"));
                                        descriptionTextView.setText(projet.getString("Description"));
                                    }
                                    catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                        //nameTextView.setText(e.getMessage());
                                    }
                                }
                            }
                        }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Error", error.getMessage());
                    }
                });
                queue.add(arrayRequest);
            }
        });
    }

    class MyAsyncTask extends AsyncTask<String,Void,JSONObject> {

        @Override
        protected JSONObject doInBackground(String... urls) {
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            JSONObject projet = jsonObject;
            //nameTextView.setText(projet.getString("Name"));
            //descriptionTextView.setText(projet.getString("Description"));
        }
    }
}
