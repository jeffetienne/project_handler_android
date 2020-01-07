package com.example.project_handler.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.project_handler.Data.DatabaseHandler;
import com.example.project_handler.Data.FormulaireViewAdapter;
import com.example.project_handler.Data.ReponseViewAdapter;
import com.example.project_handler.Model.Domaine;
import com.example.project_handler.Model.Formulaire;
import com.example.project_handler.Model.Question;
import com.example.project_handler.Model.ReponsesByFormulaire;
import com.example.project_handler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ReponsesByFormulaireActivity extends AppCompatActivity {

    TextView titreFormTextView;

    private final static String urlForm = "http://192.168.0.165:26922/api/reponsesbyformulaire";
    HashMap<String, ReponsesByFormulaire> keyValueRep = new HashMap<String, ReponsesByFormulaire>();
    HashMap<String, String> keyValueRepHeader = new HashMap<String, String>();
    ArrayList<HashMap<String, ReponsesByFormulaire>> keyValueReponses = new ArrayList<HashMap<String, ReponsesByFormulaire>>();
    Context context;
    DatabaseHandler databaseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reponses_by_formulaire);

        context = this;

        ConstraintLayout m_layout = (ConstraintLayout) findViewById(R.id.testLayout);

        Formulaire formulaire = (Formulaire) getIntent().getSerializableExtra("formulaire");
        titreFormTextView = (TextView) findViewById(R.id.titreFormTextView);
        titreFormTextView.setText("Reponses du formulaire " + formulaire.getId() + " - " + formulaire.getName());

        databaseHandler = new DatabaseHandler(this);

        //TextView tv = new TextView(this);
        //tv.setText("Add view dynamically");
        //tv.setPadding(5,100, 0, 0);
        //m_layout.addView(tv);

        getReponsesByFormulaire(formulaire.getId() + "");
    }

    public void getReponsesByFormulaire(final String idForm){
        final ArrayList<ReponsesByFormulaire> reponsesByFormulaire = new ArrayList<ReponsesByFormulaire>();
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest arrayRequest = new JsonArrayRequest(urlForm + "/" + idForm,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {

                        int questionId = 0;
                        try {
                            questionId = response.getJSONObject(0).getInt("QuestionId");
                        }catch (JSONException e){
                             e.printStackTrace();
                        }

                        int nombreQuestions = 0;

                        for(int i = 0; i<response.length(); i++){
                            try
                            {
                                JSONObject reponseObject = response.getJSONObject(i);

                                JSONObject questionObject = response.getJSONObject(i).getJSONObject("Question");


                                ReponsesByFormulaire reponse = new ReponsesByFormulaire();
                                Question question = new Question();
                                question.setId(questionObject.getInt("Id"));
                                question.setName(questionObject.getString("Name"));
                                question.setComponentId(questionObject.getInt("ComponentId"));
                                Date date = new Date();
                                DateFormat formatter=new SimpleDateFormat("yyyyMMdd HH:mm:ss");
                                try {
                                    date = formatter.parse(questionObject.getString("CreeLe"));
                                }catch (Exception e){
                                    System.out.println(e.getMessage());
                                }
                                question.setCreeLe(date);

                                //reponse.setId(reponseObject.getInt("Id"));
                                reponse.setQuestionId(reponseObject.getInt("QuestionId"));
                                reponse.setValeur(reponseObject.getString("Valeur"));
                                reponse.setQuestion(question);
                                reponse.setTexte(reponseObject.getString("Texte"));
                                reponse.setCode(reponseObject.getString("Code"));
                                reponse.setCreePar(reponseObject.getString("CreePar"));

                                try {
                                    date = formatter.parse(reponseObject.getString("CreeLe"));
                                }catch (Exception e){
                                    System.out.println(e.getMessage());
                                }
                                reponse.setCreeLe(date);

                                databaseHandler.addQuestion(question);
                                databaseHandler.addReponseByFormulaire(reponse, idForm);

                                //reponsesByFormulaire.add(reponse);

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




                        questionId = reponsesByFormulaire.get(0).getQuestionId();
                        for(int compteur = 0; compteur < reponsesByFormulaire.size(); compteur++){
                            if(reponsesByFormulaire.get(compteur).getQuestionId() == questionId)
                                nombreQuestions++;
                        }

                        int nbreQuestions = reponsesByFormulaire.size()/nombreQuestions;

                        for(int compteur = 0; compteur < reponsesByFormulaire.size(); compteur++){
                            keyValueRepHeader.put(reponsesByFormulaire.get(compteur).getQuestion().getName()+"",reponsesByFormulaire.get(compteur).getValeur());
                            keyValueRep.put(reponsesByFormulaire.get(compteur).getQuestion().getName()+"",reponsesByFormulaire.get(compteur));

                            if (compteur > 0 && (compteur + 1) % nbreQuestions == 0) {
                                keyValueReponses.add(keyValueRep);
                                keyValueRep = new HashMap<String, ReponsesByFormulaire>();
                            }
                        }


                        ListView listView = (ListView) findViewById(R.id.reponsesListView);

                        ReponseViewAdapter adapter = new ReponseViewAdapter(keyValueReponses);
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
}
