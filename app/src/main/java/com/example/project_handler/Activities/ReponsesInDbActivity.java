package com.example.project_handler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project_handler.Data.DatabaseHandler;
import com.example.project_handler.Data.ReponseInDbViewAdapter;
import com.example.project_handler.Data.ReponseViewAdapter;
import com.example.project_handler.Model.Formulaire;
import com.example.project_handler.Model.Question;
import com.example.project_handler.Model.ReponseByFormulaireInDb;
import com.example.project_handler.Model.ReponsesByFormulaire;
import com.example.project_handler.R;
import com.example.project_handler.Utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReponsesInDbActivity extends AppCompatActivity {

    TextView titreDbTextView;

    LinearLayout publishLinear;
    Button publishButton;
    Context context;

    public static Formulaire formulaire = new Formulaire();
    public static ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reponses_in_db);

        context = this;

        //final ArrayList<ReponseByFormulaireInDb> reponsesByFormulaires = new ArrayList<>();
        Question question = null;
        final DatabaseHandler databaseHandler = new DatabaseHandler(this);

        formulaire = (Formulaire) getIntent().getSerializableExtra("formulaire");

        titreDbTextView = (TextView) findViewById(R.id.titreDbTextView);
        titreDbTextView.setText("Reponses du formulaire " + formulaire.getName());

        publishLinear = (LinearLayout) findViewById(R.id.publishLinear);
        publishButton = new Button(this);
        publishButton.setText("Publish All");
        publishButton.setBackgroundColor(Color.rgb(0, 123, 255));
        publishButton.setTextColor(Color.WHITE);
        publishButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        publishButton.setTextSize(20);
        publishButton.setPadding(0,20, 0, 0);
        publishButton.setWidth(800);

        publishLinear.addView(publishButton);

        final ArrayList<ReponseByFormulaireInDb> reponsesByFormulaires = databaseHandler.getReponseByFormulaires(formulaire.getId() + "");

        if (reponsesByFormulaires.size() <= 0){
            titreDbTextView.setText("Aucune saisie dans le telephone pour le formulaire " + formulaire.getName());
            listView = (ListView) findViewById(R.id.reponsesInDbListView);

            publishButton.setVisibility(View.INVISIBLE);

            ArrayList<HashMap<String, ReponseByFormulaireInDb>> keyValueReponses = new ArrayList<HashMap<String, ReponseByFormulaireInDb>>();
            ReponseInDbViewAdapter adapter = new ReponseInDbViewAdapter(keyValueReponses);
            adapter.addContext(context);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            return;
        }
        HashMap<String, ReponseByFormulaireInDb> keyValueRep = new HashMap<String, ReponseByFormulaireInDb>();
        HashMap<String, String> keyValueRepHeader = new HashMap<String, String>();
        final ArrayList<HashMap<String, ReponseByFormulaireInDb>> keyValueReponses = new ArrayList<HashMap<String, ReponseByFormulaireInDb>>();

        int nombreQuestions = 0;
        int questionId = reponsesByFormulaires.get(0).getQuestionId();
        for(int compteur = 0; compteur < reponsesByFormulaires.size(); compteur++){
            if(reponsesByFormulaires.get(compteur).getQuestionId() == questionId)
                nombreQuestions++;
        }

        int nbreQuestions = reponsesByFormulaires.size()/nombreQuestions;

        int groupe = reponsesByFormulaires.get(0).getGroupe();
        int currentGroupe = 0;



        for(int compteur = 0; compteur < reponsesByFormulaires.size(); compteur++){

            /*
            currentGroupe = reponsesByFormulaires.get(compteur).getGroupe();
            if(groupe == currentGroupe){
                keyValueRepHeader.put(reponsesByFormulaires.get(compteur).getQuestionName()+"",reponsesByFormulaires.get(compteur).getValeur());
                keyValueRep.put(reponsesByFormulaires.get(compteur).getQuestionName()+"",reponsesByFormulaires.get(compteur));
            }
            else {
                keyValueReponses.add(keyValueRep);
                keyValueRep = new HashMap<String, ReponseByFormulaireInDb>();
                groupe = currentGroupe;
                keyValueRepHeader.put(reponsesByFormulaires.get(compteur).getQuestionName()+"",reponsesByFormulaires.get(compteur).getValeur());
                keyValueRep.put(reponsesByFormulaires.get(compteur).getQuestionName()+"",reponsesByFormulaires.get(compteur));
            }*/

            //*
            keyValueRepHeader.put(reponsesByFormulaires.get(compteur).getQuestionName()+"",reponsesByFormulaires.get(compteur).getValeur());
            keyValueRep.put(reponsesByFormulaires.get(compteur).getQuestionName()+"",reponsesByFormulaires.get(compteur));

            if (compteur > 0 && (compteur + 1) % nbreQuestions == 0) {
                keyValueReponses.add(keyValueRep);
                keyValueRep = new HashMap<String, ReponseByFormulaireInDb>();
            }//*/
        }

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirmation!!!");
                builder.setMessage("Are you sure you want to publish all to the server?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();


                        final RequestQueue queueMax = Volley.newRequestQueue(context);
                        JsonArrayRequest arrayRequestMax = new JsonArrayRequest(Constants.URL_MAX_GROUPE,
                                new Response.Listener<JSONArray>(){
                                    @Override
                                    public void onResponse(JSONArray response) {

                                        if(response.length() <= 0){
                                            return;
                                        }

                                        try {
                                            JSONObject maxGroupeObject = response.getJSONObject(0);
                                            int maxGroupe = maxGroupeObject.getInt("Groupe");

                                            for(int compteur = 0; compteur < keyValueReponses.size(); compteur++){

                                                maxGroupe++;
                                                for(Map.Entry<String, ReponseByFormulaireInDb> entry: keyValueReponses.get(compteur).entrySet()) {
                                                    if(entry.getValue().getComponentId() == 1 || entry.getValue().getComponentId() == 5 || entry.getValue().getComponentId() == 6) {
                                                        try {
                                                            System.out.println("Compteur: " + compteur);
                                                            final RequestQueue queueRep = Volley.newRequestQueue(context);
                                                            JSONObject reponseObject = new JSONObject();
                                                            reponseObject.put("QuestionId", entry.getValue().getQuestionId());
                                                            reponseObject.put("Valeur", entry.getValue().getValeur());
                                                            reponseObject.put("CreePar", "Concepteur");
                                                            reponseObject.put("Groupe", maxGroupe);
                                                            Date c = Calendar.getInstance().getTime();

                                                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
                                                            String formattedDate = df.format(c);
                                                            reponseObject.put("CreeLe", formattedDate);
                                                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.URL_REPONSE, reponseObject, new Response.Listener<JSONObject>() {
                                                                @Override
                                                                public void onResponse(JSONObject response) {
                                                                    Toast.makeText(context,"Reponses sauvegardées avec succès!", Toast.LENGTH_LONG).show();
                                                                }
                                                            }, new Response.ErrorListener() {
                                                                @Override
                                                                public void onErrorResponse(VolleyError error) {
                                                                    System.out.println("Erreur");
                                                                }
                                                            });
                                                            queueRep.add(request);
                                                        } catch (JSONException e) {

                                                        }
                                                    }
                                                    if(entry.getValue().getComponentId() == 2 || entry.getValue().getComponentId() == 3 || entry.getValue().getComponentId() == 4) {
                                                        try {
                                                            final RequestQueue queueRep = Volley.newRequestQueue(context);
                                                            JSONObject reponseObject = new JSONObject();
                                                            reponseObject.put("QuestionId", entry.getValue().getQuestionId());
                                                            reponseObject.put("Valeur", entry.getValue().getValeur());
                                                            reponseObject.put("Code", entry.getValue().getCode());
                                                            reponseObject.put("Texte", entry.getValue().getTexte());
                                                            reponseObject.put("CreePar", "Concepteur");
                                                            reponseObject.put("Groupe", maxGroupe);
                                                            Date c = Calendar.getInstance().getTime();

                                                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
                                                            String formattedDate = df.format(c);
                                                            reponseObject.put("CreeLe", formattedDate);
                                                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.URL_REPONSE, reponseObject, new Response.Listener<JSONObject>() {
                                                                @Override
                                                                public void onResponse(JSONObject response) {
                                                                    Toast.makeText(context,"Reponses sauvegardées avec succès!", Toast.LENGTH_LONG).show();
                                                                }
                                                            }, new Response.ErrorListener() {
                                                                @Override
                                                                public void onErrorResponse(VolleyError error) {
                                                                    System.out.println("Erreur");
                                                                }
                                                            });
                                                            queueRep.add(request);
                                                        } catch (JSONException e) {

                                                        }
                                                    }
                                                }


                                            }

                                            databaseHandler.delete();



                                            ListView listView = (ListView) findViewById(R.id.reponsesInDbListView);

                                            ArrayList<HashMap<String, ReponseByFormulaireInDb>> keyValueReponses = new ArrayList<HashMap<String, ReponseByFormulaireInDb>>();
                                            ReponseInDbViewAdapter adapter = new ReponseInDbViewAdapter(keyValueReponses);
                                            adapter.addContext(context);
                                            listView.setAdapter(adapter);
                                            adapter.notifyDataSetChanged();
                                        }
                                        catch (JSONException e){

                                        }


                                    }
                                }, new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d("Error", error.getMessage());
                                System.out.println("Erreur formulaire: " + error);
                            }
                        });

                        queueMax.add(arrayRequestMax);





                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        listView = (ListView) findViewById(R.id.reponsesInDbListView);

        ReponseInDbViewAdapter adapter = new ReponseInDbViewAdapter(keyValueReponses);
        adapter.addContext(context);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
