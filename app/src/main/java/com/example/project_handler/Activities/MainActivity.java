package com.example.project_handler.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Button;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.project_handler.Data.DatabaseHandler;
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
import java.sql.Ref;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button callApiButton;
    private TextView nameTextView;
    private TextView projetListTextView;
    private TextView descriptionTextView;
    private TextView titreTextView;
    private Context context;
    private TableLayout tableLayout;

    private RecyclerView recyclerView;
    ProjetRecyclerViewAdapter projetRecyclerViewAdapter;

    ArrayList<Formulaire> liste = new ArrayList<Formulaire>();



    private final static String url = "http://192.168.0.165:26922/api/projet";
    private final static String urlForm = "http://192.168.0.165:26922/api/formulaire";

    final ArrayList<Formulaire> formulaires = new ArrayList<Formulaire>();

    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        databaseHandler = new DatabaseHandler(this);
        //databaseHandler.delete();
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

        //getProjets();

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
                                JSONObject domaineObject = projetObject.getJSONObject("Domaine");

                                Projet projet = new Projet();
                                Domaine domaine = new Domaine();

                                domaine.setId(domaineObject.getInt("Id"));
                                domaine.setName(domaineObject.getString("Name"));

                                projet.setName(projetObject.getString("Name"));
                                projet.setDescription(projetObject.getString("Description"));
                                projet.setDomaineId(projetObject.getInt("DomaineId"));
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
                        //tableLayout = findViewById(R.id.tableLayoutProjet);

                        TableRow tableRow = new TableRow(context);
                        tableRow.setLayoutParams(new TableRow.LayoutParams(
                                TableRow.LayoutParams.FILL_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT));

                        TextView textViewName = new TextView(context);
                        textViewName.setText("Name");
                        textViewName.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                        textViewName.setPadding(5, 5, 5, 0);
                        textViewName.setTextSize(20);
                        textViewName.setBackgroundResource(R.drawable.border);
                        tableRow.addView(textViewName);

                        TextView textViewDescription = new TextView(context);
                        textViewDescription.setBackgroundResource(R.drawable.border);
                        textViewDescription.setText("Description");
                        textViewDescription.setTextSize(20);
                        textViewDescription.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                        textViewDescription.setPadding(5, 5, 5, 0);
                        tableRow.addView(textViewDescription);

                        TextView textViewDomaine = new TextView(context);
                        textViewDomaine.setText("Domaine");
                        textViewDomaine.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                        textViewDomaine.setPadding(5, 5, 5, 0);
                        textViewDomaine.setTextSize(20);
                        textViewDomaine.setBackgroundResource(R.drawable.border);
                        tableRow.addView(textViewDomaine);

                        TextView textViewCreePar = new TextView(context);
                        textViewCreePar.setText("Cree par");
                        textViewCreePar.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                        textViewCreePar.setPadding(5, 5, 5, 0);
                        textViewCreePar.setTextSize(20);
                        textViewCreePar.setBackgroundResource(R.drawable.border);
                        tableRow.addView(textViewCreePar);

                        TextView textViewCreeLe = new TextView(context);
                        textViewCreeLe.setText("Cree le");
                        textViewCreeLe.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                        textViewCreeLe.setPadding(5, 5, 5, 0);
                        textViewCreeLe.setTextSize(20);
                        textViewCreeLe.setBackgroundResource(R.drawable.border);
                        tableRow.addView(textViewCreeLe);

                        //tableRow.setBackgroundResource(R.drawable.border);
                        tableLayout.addView(tableRow, new TableLayout.LayoutParams(
                                TableRow.LayoutParams.FILL_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT));

                        //tableLayout.setBackgroundResource(R.drawable.border);
                        for (int compteur = 0; compteur < projets.size(); compteur++)
                        {
                            TableRow tableRow1 = new TableRow(context);

                            TextView textViewName1 = new TextView(context);
                            textViewName1.setText(projets.get(compteur).getName());
                            textViewName1.setPadding(5, 5, 5, 0);
                            textViewName1.setBackgroundResource(R.drawable.border);
                            tableRow1.addView(textViewName1);

                            TextView textViewDescription1 = new TextView(context);
                            textViewDescription1.setText(projets.get(compteur).getDescription());
                            textViewDescription1.setPadding(5, 5, 5, 0);
                            textViewDescription1.setBackgroundResource(R.drawable.border);
                            tableRow1.addView(textViewDescription1);

                            TextView textViewDomaine1 = new TextView(context);
                            textViewDomaine1.setText(projets.get(compteur).getDomaine().getName());
                            textViewDomaine1.setPadding(5, 5, 5, 0);
                            textViewDomaine1.setBackgroundResource(R.drawable.border);
                            tableRow1.addView(textViewDomaine1);

                            TextView textViewCreePar1 = new TextView(context);
                            textViewCreePar1.setText(projets.get(compteur).getCreePar());
                            textViewCreePar1.setPadding(5, 5, 5, 0);
                            textViewCreePar1.setBackgroundResource(R.drawable.border);
                            tableRow1.addView(textViewCreePar1);

                            TextView textViewCreeLe1 = new TextView(context);
                            textViewCreeLe1.setText(projets.get(compteur).getCreeLe());
                            textViewCreeLe1.setPadding(5, 5, 5, 0);
                            textViewCreeLe1.setBackgroundResource(R.drawable.border);
                            tableRow1.addView(textViewCreeLe1);

                            //tableRow1.setBackgroundResource(R.drawable.border);
                            tableLayout.addView(tableRow1, new TableLayout.LayoutParams(
                                    TableRow.LayoutParams.FILL_PARENT,
                                    TableRow.LayoutParams.WRAP_CONTENT));

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
                                JSONObject projetObject = response.getJSONObject(i).getJSONObject("Projet");

                                Formulaire formulaire = new Formulaire();
                                Projet projet = new Projet();

                                projet.setName(projetObject.getString("Name"));

                                formulaire.setId(formulaireObject.getInt("Id"));
                                formulaire.setName(formulaireObject.getString("Name"));
                                formulaire.setDescription(formulaireObject.getString("Description"));
                                formulaire.setProjet(projet);
                                formulaire.setCreePar(formulaireObject.getString("CreePar"));
                                //formulaire.setCreeLe(formulaireObject.getd("CreeLe"));

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
                System.out.println("Erreur formulaire: " + error);
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
                            ListView listView = (ListView) findViewById(R.id.reponsesListView);


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
