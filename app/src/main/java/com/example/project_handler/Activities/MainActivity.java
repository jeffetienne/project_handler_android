package com.example.project_handler.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.project_handler.Data.ProjetRecyclerViewAdapter;
import com.example.project_handler.Data.FormulaireViewAdapter;
import com.example.project_handler.Model.Domaine;
import com.example.project_handler.Model.Formulaire;
import com.example.project_handler.Model.Projet;
import com.example.project_handler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button callApiButton;
    private TextView nameTextView;
    private TextView projetListTextView;
    private TextView descriptionTextView;
    private TextView titreTextView;
    private Context context;

    private RecyclerView recyclerView;
    ProjetRecyclerViewAdapter projetRecyclerViewAdapter;

    ArrayList<Formulaire> liste = new ArrayList<Formulaire>();



    private final static String url = "http://192.168.0.165:26922/api/projet";
    private final static String urlForm = "http://192.168.0.165:26922/api/formulaire";

    final ArrayList<Formulaire> formulaires = new ArrayList<Formulaire>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        //nameTextView = (TextView) findViewById(R.id.nameTextView);
        ///projetListTextView = (TextView) findViewById(R.id.projetListTextView);

        /*
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Preferences preferences = new Preferences(MainActivity.this);
        String search = preferences.getSearch();

        ArrayList<Projet> projets = getProjets();


        //nameTextView.setText(projets.size());
        //projetListTextView.setText("Liste des projets");

        projetRecyclerViewAdapter = new ProjetRecyclerViewAdapter(this, projets);
        recyclerView.setAdapter(projetRecyclerViewAdapter);
        projetRecyclerViewAdapter.notifyDataSetChanged();*/


        //new MyAsyncTask().execute(urlForm);
        getFormulaires();

    }

    public ArrayList<Projet> getProjets(){
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
                                //JSONObject domaineObject = (JSONObject) JSONObject.wrap(projetObject.getString("Domaine"));
                                //domaine.setName(domaineObject.getString("Name"));
                                projet.setDomaine(domaine);
                                projet.setCreePar(projetObject.getString("CreePar"));
                                projet.setCreeLe(projetObject.getString("CreeLe"));

                                projets.add(projet);

                                //nameTextView.setText(projets.get(0).getName());

                                //Log.d("Items :", projet.getName());
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

    public void getFormulaires(){
        final ArrayList<Formulaire> formulaires = new ArrayList<Formulaire>();
        final RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest arrayRequest = new JsonArrayRequest(urlForm,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {

                        for(int i = 0; i<response.length(); i++){
                            try
                            {
                                JSONObject formulaireObject = response.getJSONObject(i);

                                Formulaire formulaire = new Formulaire();
                                Domaine domaine = new Domaine();

                                formulaire.setId(formulaireObject.getInt("Id"));
                                formulaire.setName(formulaireObject.getString("Name"));
                                formulaire.setDescription(formulaireObject.getString("Description"));

                                formulaires.add(formulaire);

                                //nameTextView.setText(projets.get(0).getName());


                                //nameTextView.setText(projet.getString("Name"));
                                //descriptionTextView.setText(projet.getString("Description"));

                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                                //nameTextView.setText(e.getMessage());
                            }
                        }

                        titreTextView = (TextView) findViewById(R.id.titreTextView);
                        titreTextView.setText("Liste des formulaires");
                        ListView listView = (ListView) findViewById(R.id.formulaireListView);


                        FormulaireViewAdapter adapter = new FormulaireViewAdapter(formulaires);
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", error.getMessage());
            }
        });
        queue.add(arrayRequest);
    }

    class MyAsyncTask extends AsyncTask<String,Void,JSONObject> {

        @Override
        protected JSONObject doInBackground(String... urls) {
            final RequestQueue queue = Volley.newRequestQueue(context);

            JsonArrayRequest arrayRequest = new JsonArrayRequest(urls[0],
                    new Response.Listener<JSONArray>(){
                        @Override
                        public void onResponse(JSONArray response) {
                            //System.out.println("do in background");
                            for(int i = 0; i<response.length(); i++){
                                try
                                {
                                    JSONObject formulaireObject = response.getJSONObject(i);

                                    Formulaire formulaire = new Formulaire();
                                    Domaine domaine = new Domaine();

                                    formulaire.setName(formulaireObject.getString("Name"));
                                    formulaire.setDescription(formulaireObject.getString("Description"));

                                    formulaires.add(formulaire);
                                    System.out.println("ItemCount: " + formulaires.size());
                                    //nameTextView.setText(projets.get(0).getName());


                                    //nameTextView.setText(projet.getString("Name"));
                                    //descriptionTextView.setText(projet.getString("Description"));

                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                    //nameTextView.setText(e.getMessage());
                                }
                            }
                            //System.out.println("ItemCount: " + formulaires.size());
                            titreTextView = (TextView) findViewById(R.id.titreTextView);
                            titreTextView.setText("Liste des formulaires");
                            ListView listView = (ListView) findViewById(R.id.formulaireListView);


                            FormulaireViewAdapter adapter = new FormulaireViewAdapter(formulaires);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("Error", error.getMessage());
                }
            });
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
