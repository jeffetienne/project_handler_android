package com.example.project_handler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.project_handler.Data.FormulaireViewAdapter;
import com.example.project_handler.Model.Component;
import com.example.project_handler.Model.Domaine;
import com.example.project_handler.Model.DynamicReference;
import com.example.project_handler.Model.Formulaire;
import com.example.project_handler.Model.Question;
import com.example.project_handler.Model.TypeDonnee;
import com.example.project_handler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReponseFormActivity extends AppCompatActivity {

    Context context;
    TextView titreForm;
    LinearLayout ReponseForm;
    EditText editText;
    Spinner combobox;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button saveButton;

    String test;
    private final static String url = "http://192.168.0.165:26922/api/questionsbyformulaire";
    private final static String urlRef = "http://192.168.0.165:26922/api/dynamicreferencebyquestion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reponse_form);

        Formulaire formulaire = (Formulaire) getIntent().getSerializableExtra("formulaire");
        titreForm = (TextView) findViewById(R.id.titreForm);
        titreForm.setText(formulaire.getDescription());

        context = this;
        ReponseForm = (LinearLayout) findViewById(R.id.reponseFormLinearLayout);
        getQuestions(formulaire.getId() + "");
    }

    private void getQuestions(final String idForm){
        final ArrayList<Question> questions = new ArrayList<Question>();
        final RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest arrayRequest = new JsonArrayRequest(url + "/" + idForm,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {


                        for(int i = 0; i<response.length(); i++){
                            try
                            {
                                JSONObject questionObject = response.getJSONObject(i);

                                JSONObject componentObject = response.getJSONObject(i).getJSONObject("Component");
                                JSONObject formulaireObject = response.getJSONObject(i).getJSONObject("Formulaire");
                                JSONObject typeDonneeObject = response.getJSONObject(i).getJSONObject("TypeDonnee");

                                Question question = new Question();
                                Component component = new Component();
                                Formulaire form = new Formulaire();
                                TypeDonnee typeDonnee = new TypeDonnee();

                                component.setId(componentObject.getInt("Id"));
                                component.setName(componentObject.getString("Name"));

                                form.setId(formulaireObject.getInt("Id"));
                                form.setName(formulaireObject.getString("Name"));
                                form.setDescription(formulaireObject.getString("Description"));

                                typeDonnee.setId(typeDonneeObject.getInt("Id"));
                                typeDonnee.setName(typeDonneeObject.getString("Name"));

                                question.setId(questionObject.getInt("Id"));
                                question.setName(questionObject.getString("Name"));
                                question.setMessage(questionObject.getString("Message"));
                                question.setComponent(component);
                                question.setComponentId(questionObject.getInt("ComponentId"));
                                question.setFormulaire(form);
                                question.setTypeDonnee(typeDonnee);
                                if (questionObject.getInt("TypeDonneeId") == 1)
                                    question.setMaximum(questionObject.getInt("Maximum"));
                                if (questionObject.getInt("TypeDonneeId") == 1)
                                    question.setMinimum(questionObject.getInt("Minimum"));
                                question.setRequired(questionObject.getBoolean("Required"));

                                questions.add(question);

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

                        for (int compteur = 0; compteur < questions.size(); compteur++){
                            if(questions.get(compteur).getComponentId() == 1)
                            {
                                editText = new EditText(context);
                                editText.setHint(questions.get(compteur).getMessage());
                                ReponseForm.addView(editText);
                            }

                            if(questions.get(compteur).getComponentId() == 2)
                            {
                                combobox = new Spinner(context);
                                combobox.setPrompt(questions.get(compteur).getMessage());
                                ReponseForm.addView(combobox);

                                getReferences(questions.get(compteur).getId() + "", combobox);
                            }
                        }
                        saveButton = new Button(context);
                        saveButton.setText("Save");
                        saveButton.setBackgroundColor(Color.rgb(0, 123, 255));
                        saveButton.setTextColor(Color.WHITE);
                        saveButton.setPadding(0,20, 0, 0);
                        ReponseForm.addView(saveButton);

                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", error.getMessage());
            }
        });
        queue.add(arrayRequest);
    }

    private void getReferences(final String idQuestion, final Spinner spinner){
        final ArrayList<DynamicReference> references = new ArrayList<DynamicReference>();
        final RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest arrayRequest = new JsonArrayRequest(urlRef + "/" + idQuestion,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {

                        for(int i = 0; i<response.length(); i++){
                            try
                            {
                                JSONObject referenceObject = response.getJSONObject(i);

                                JSONObject questionObject = response.getJSONObject(i).getJSONObject("Question");

                                Question question = new Question();
                                DynamicReference reference = new DynamicReference();

                                question.setId(questionObject.getInt("Id"));
                                question.setName(questionObject.getString("Name"));
                                question.setMessage(questionObject.getString("Message"));
                                question.setComponentId(questionObject.getInt("ComponentId"));
                                if (questionObject.getInt("TypeDonneeId") == 1)
                                    question.setMaximum(questionObject.getInt("Maximum"));
                                if (questionObject.getInt("TypeDonneeId") == 1)
                                    question.setMinimum(questionObject.getInt("Minimum"));
                                question.setRequired(questionObject.getBoolean("Required"));

                                reference.setId(referenceObject.getInt("Id"));
                                reference.setCode(referenceObject.getString("Code"));
                                reference.setTexte(referenceObject.getString("Texte"));
                                reference.setQuestionId(referenceObject.getInt("QuestionId"));
                                reference.setQuestion(question);

                                references.add(reference);

                                //nameTextView.setText(projets.get(0).getName());

                                //nameTextView.setText(projet.getString("Name"));
                                //descriptionTextView.setText(projet.getString("Description"));

                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                                System.out.println("Erreur: " + e.getMessage());
                                //nameTextView.setText(e.getMessage());
                            }
                        }
                        ArrayList<String> liste = new ArrayList<String>();
                        for (int i = 0; i < references.size(); i++){
                            liste.add(references.get(i).getTexte());
                        }
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                                android.R.layout.simple_spinner_item, liste);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(dataAdapter);

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
