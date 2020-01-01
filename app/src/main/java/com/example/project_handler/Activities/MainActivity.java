package com.example.project_handler.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import com.example.project_handler.Data.ProjetRecyclerViewAdapter;
import com.example.project_handler.Model.Domaine;
import com.example.project_handler.Model.Projet;
import com.example.project_handler.R;
import com.example.project_handler.Utils.Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button callApiButton;
    private TextView nameTextView;
    private TextView descriptionTextView;

    private RecyclerView recyclerView;
    ProjetRecyclerViewAdapter projetRecyclerViewAdapter;


    private final static String url = "http://localhost:26922/api/projet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Preferences preferences = new Preferences(MainActivity.this);
        String search = preferences.getSearch();

        projetRecyclerViewAdapter = new ProjetRecyclerViewAdapter(this, getProjets());
        recyclerView.setAdapter(projetRecyclerViewAdapter);
        projetRecyclerViewAdapter.notifyDataSetChanged();

    }

    public List<Projet> getProjets(){
        final ArrayList<Projet> projets = new ArrayList<Projet>();
        final RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest arrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i = 0; i<response.length(); i++){
                            try
                            {
                                JSONObject projetObject = response.getJSONObject(i);

                                Projet projet = new Projet();
                                Domaine domaine = new Domaine();

                                projet.setName(projetObject.getString("Name"));
                                projet.setDescription(projetObject.getString("Description"));
                                projet.setDomaineId(projetObject.getInt("DomaineId"));
                                JSONObject domaineObject = (JSONObject) JSONObject.wrap(projetObject.getString("Domaine"));
                                domaine.setName(domaineObject.getString("Name"));
                                projet.setDomaine(domaine);
                                projet.setCreePar(projetObject.getString("CreePar"));
                                projet.setCreeLe(projetObject.getString("CreeLe"));

                                projets.add(projet);


                                Log.d("Items :", projet.getName());
                                //nameTextView.setText(projet.getString("Name"));
                                //descriptionTextView.setText(projet.getString("Description"));

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
        return projets;
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
