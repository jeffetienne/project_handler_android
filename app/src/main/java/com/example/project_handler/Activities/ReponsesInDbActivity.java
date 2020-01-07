package com.example.project_handler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.project_handler.Data.DatabaseHandler;
import com.example.project_handler.Data.ReponseInDbViewAdapter;
import com.example.project_handler.Data.ReponseViewAdapter;
import com.example.project_handler.Model.Formulaire;
import com.example.project_handler.Model.Question;
import com.example.project_handler.Model.ReponsesByFormulaire;
import com.example.project_handler.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ReponsesInDbActivity extends AppCompatActivity {

    TextView titreDbTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reponses_in_db);

        ArrayList<ReponsesByFormulaire> reponsesByFormulaires = new ArrayList<>();
        Question question = null;
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        Formulaire formulaire = (Formulaire) getIntent().getSerializableExtra("formulaire");
        titreDbTextView = (TextView) findViewById(R.id.titreDbTextView);
        titreDbTextView.setText("Reponses du formulaire " + formulaire.getId() + " - " + formulaire.getName());
        reponsesByFormulaires = databaseHandler.getReponseByFormulaires(formulaire.getId() + "");

        HashMap<String, ReponsesByFormulaire> keyValueRep = new HashMap<String, ReponsesByFormulaire>();
        HashMap<String, String> keyValueRepHeader = new HashMap<String, String>();
        ArrayList<HashMap<String, ReponsesByFormulaire>> keyValueReponses = new ArrayList<HashMap<String, ReponsesByFormulaire>>();

        int nombreQuestions = 0;
        int questionId = reponsesByFormulaires.get(0).getQuestionId();
        for(int compteur = 0; compteur < reponsesByFormulaires.size(); compteur++){
            if(reponsesByFormulaires.get(compteur).getQuestionId() == questionId)
                nombreQuestions++;
        }

        int nbreQuestions = reponsesByFormulaires.size()/nombreQuestions;

        for(int compteur = 0; compteur < reponsesByFormulaires.size(); compteur++){
            if(reponsesByFormulaires.get(compteur).getQuestionId() > 0)
            {
                question = databaseHandler.getQuestion(reponsesByFormulaires.get(compteur).getQuestionId() + "");
            }

            if (question != null)
                reponsesByFormulaires.get(compteur).setQuestion(question);
            keyValueRepHeader.put(reponsesByFormulaires.get(compteur).getQuestion().getName()+"",reponsesByFormulaires.get(compteur).getValeur());
            keyValueRep.put(reponsesByFormulaires.get(compteur).getQuestion().getName()+"",reponsesByFormulaires.get(compteur));

            if (compteur > 0 && (compteur + 1) % nbreQuestions == 0) {
                keyValueReponses.add(keyValueRep);
                keyValueRep = new HashMap<String, ReponsesByFormulaire>();
            }
        }


        ListView listView = (ListView) findViewById(R.id.reponsesInDbListView);

        ReponseInDbViewAdapter adapter = new ReponseInDbViewAdapter(keyValueReponses);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
