package com.example.project_handler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project_handler.Data.DatabaseHandler;
import com.example.project_handler.Data.FormulaireViewAdapter;
import com.example.project_handler.Model.Component;
import com.example.project_handler.Model.Domaine;
import com.example.project_handler.Model.DynamicReference;
import com.example.project_handler.Model.Formulaire;
import com.example.project_handler.Model.Question;
import com.example.project_handler.Model.ReponseByFormulaireInDb;
import com.example.project_handler.Model.ReponsesByFormulaire;
import com.example.project_handler.Model.TypeDonnee;
import com.example.project_handler.R;
import com.example.project_handler.Utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import me.srodrigo.androidhintspinner.HintAdapter;
import me.srodrigo.androidhintspinner.HintSpinner;

public class ReponseFormActivity extends AppCompatActivity {

    Context context;
    TextView titreForm;
    LinearLayout ReponseForm;
    EditText editText;
    Spinner combobox;
    RadioGroup radioGroup;
    ArrayList<RadioButton> radioButtons;
    ArrayList<CheckBox> checkBoxes;
    Button saveButton;

    String test;

    String[] spinnerArray;
    HashMap<String ,String> spinnerMap = new HashMap<String, String>();
    ArrayList<DynamicReference> references = null;
    DatabaseHandler databaseHandler;
    Formulaire formulaire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reponse_form);

        formulaire = (Formulaire) getIntent().getSerializableExtra("formulaire");
        titreForm = (TextView) findViewById(R.id.titreForm);
        titreForm.setText(formulaire.getDescription());

        context = this;
        ReponseForm = (LinearLayout) findViewById(R.id.reponseFormLinearLayout);

        databaseHandler = new DatabaseHandler(this);
        getQuestions(formulaire.getId() + "");
    }

    private void getQuestions(final String idForm){
        final ArrayList<Question> questions = new ArrayList<Question>();
        final RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Constants.URL_QUESTION + "/" + idForm,
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
                                question.setFormulaireId(form.getId());
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
                                editText.setId(compteur);
                                editText.setHint(questions.get(compteur).getMessage());
                                ReponseForm.addView(editText);
                            }

                            if(questions.get(compteur).getComponentId() == 2)
                            {
                                combobox = new Spinner(context);
                                combobox.setId(compteur);

                                ReponseForm.addView(combobox);

                                getReferences(questions.get(compteur), combobox, radioGroup);
                            }

                            if(questions.get(compteur).getComponentId() == 3)
                            {
                                radioGroup = new RadioGroup(context);
                                radioButtons = new ArrayList<RadioButton>();
                                radioGroup.setId(compteur);
                                radioGroup.setOrientation(LinearLayout.VERTICAL);

                                TextView tv = new TextView(context);
                                tv.setText(questions.get(compteur).getMessage());
                                tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
                                tv.setTextSize(20);
                                ReponseForm.addView(tv);
                                getReferences(questions.get(compteur), combobox, radioGroup);

                                ReponseForm.addView(radioGroup);
                            }

                            if(questions.get(compteur).getComponentId() == 4)
                            {
                                checkBoxes = new ArrayList<>();

                                radioGroup = new RadioGroup(context);
                                radioGroup.setId(compteur);
                                radioGroup.setOrientation(LinearLayout.VERTICAL);

                                TextView tv = new TextView(context);
                                tv.setText(questions.get(compteur).getMessage());
                                tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
                                tv.setTextSize(20);
                                ReponseForm.addView(tv);
                                getReferences(questions.get(compteur), combobox, radioGroup);
                                ReponseForm.addView(radioGroup);

                            }

                            if(questions.get(compteur).getComponentId() == 5)
                            {
                                editText = new EditText(context);
                                editText.setId(compteur);
                                editText.setHint(questions.get(compteur).getMessage());
                                editText.setSingleLine(false);
                                editText.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                                //editText.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                                ReponseForm.addView(editText);
                            }

                            //databaseHandler.addQuestion(questions.get(compteur));
                        }
                        saveButton = new Button(context);
                        saveButton.setText("Save");
                        saveButton.setBackgroundColor(Color.rgb(0, 123, 255));
                        saveButton.setTextColor(Color.WHITE);
                        saveButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                        saveButton.setTextSize(20);
                        saveButton.setPadding(0,20, 0, 0);
                        ReponseForm.addView(saveButton);

                        saveButton.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {

                                for (int compteur = 0; compteur < questions.size(); compteur++){
                                    ReponseByFormulaireInDb reponsesByFormulaire = new ReponseByFormulaireInDb();
                                    reponsesByFormulaire.setQuestionId(questions.get(compteur).getId());
                                    reponsesByFormulaire.setQuestionName(questions.get(compteur).getName());
                                    reponsesByFormulaire.setQuestionDescription(questions.get(compteur).getMessage());
                                    reponsesByFormulaire.setComponentId(questions.get(compteur).getComponentId());
                                    reponsesByFormulaire.setCreePar("Concepteur");

                                    Date c = Calendar.getInstance().getTime();

                                    System.out.println("Date du jour: " + c);
                                    reponsesByFormulaire.setCreeLe(c);
                                    if(questions.get(compteur).getComponentId() == 1) {

                                        editText = (EditText) ReponseForm.findViewById(compteur);
                                        reponsesByFormulaire.setValeur(editText.getText().toString());
                                    }

                                    if(questions.get(compteur).getComponentId() == 2)
                                    {
                                        combobox = (Spinner) ReponseForm.findViewById(compteur);
                                        String selectedText = combobox.getSelectedItem().toString();
                                        String selectedValue = spinnerMap.get(selectedText);
                                        reponsesByFormulaire.setValeur(selectedValue);
                                        reponsesByFormulaire.setTexte(selectedText);
                                    }

                                    if(questions.get(compteur).getComponentId() == 3){

                                    }

                                    databaseHandler.addReponseByFormulaire(reponsesByFormulaire, questions.get(compteur).getFormulaire().getId() + "");
                                }

                                //ArrayList<ReponseByFormulaireInDb> reponsesByFormulaires;

                                //reponsesByFormulaires = databaseHandler.getReponseByFormulaires(idForm);

                                Intent intent = new Intent(context, ReponsesInDbActivity.class);

                                intent.putExtra("formulaire", formulaire);
                                context.startActivity(intent);

                                finish();

                                /*
                                final RequestQueue queueRep = Volley.newRequestQueue(context);
                                JSONObject reponseObject = new JSONObject();

                                for (int compteur = 0; compteur < questions.size(); compteur++){
                                    if(questions.get(compteur).getComponentId() == 1)
                                    {
                                        editText = (EditText) ReponseForm.findViewById(compteur);
                                        try {
                                            reponseObject.put("QuestionId", questions.get(compteur).getId());
                                            reponseObject.put("Valeur", editText.getText());
                                            reponseObject.put("CreePar", "Concepteur");
                                            Date c = Calendar.getInstance().getTime();

                                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                            String formattedDate = df.format(c);
                                            reponseObject.put("CreeLe", formattedDate);
                                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlRep, reponseObject, new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    System.out.println("Success");
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    System.out.println("Erreur");
                                                }
                                            });
                                            queueRep.add(request);
                                        }catch (JSONException e){

                                        }

                                    }

                                    if(questions.get(compteur).getComponentId() == 2)
                                    {
                                        combobox = (Spinner) ReponseForm.findViewById(compteur);
                                        try {
                                            String selectedText = combobox.getSelectedItem().toString();
                                            String selectedValue = spinnerMap.get(selectedText);

                                            reponseObject.put("QuestionId", questions.get(compteur).getId());
                                            reponseObject.put("Valeur", selectedValue);
                                            reponseObject.put("CreePar", "Concepteur");
                                            Date c = Calendar.getInstance().getTime();

                                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                            String formattedDate = df.format(c);
                                            reponseObject.put("CreeLe", formattedDate);
                                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlRep, reponseObject, new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    finish();
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    System.out.println("Erreur");
                                                }
                                            });
                                            queueRep.add(request);
                                        }catch (JSONException e){

                                        }
                                    }

                                    if(questions.get(compteur).getComponentId() == 3){

                                    }

                                }*/
                            }
                        });

                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", error.getMessage());
                System.out.println("Erreur: " + error.getMessage());
            }
        });
        queue.add(arrayRequest);
    }

    private void getReferences(final Question question, final Spinner spinner, final RadioGroup radioGroup){

        final RequestQueue queue = Volley.newRequestQueue(this);


        JsonArrayRequest arrayRequest = new JsonArrayRequest(Constants.URL_REFERENCE + "/" + question.getId(),
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {

                        if(references == null)
                            references = new ArrayList<DynamicReference>();
                        else references.clear();

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

                        if(question.getComponentId() == 2){
                            ArrayList<String> liste = new ArrayList<String>();
                            for (int i = 0; i < references.size(); i++){
                                liste.add(references.get(i).getTexte());
                            }

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                                    android.R.layout.simple_spinner_item, liste);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            spinnerArray = new String[references.size()];
                            for (int i = 0; i < references.size(); i++)
                            {
                                spinnerMap.put(references.get(i).getTexte(), references.get(i).getCode());
                                spinnerArray[i] = references.get(i).getTexte();
                            }
                            HintSpinner<String> hintSpinner = new HintSpinner<>(
                                    spinner,
                                    // Default layout - You don't need to pass in any layout id, just your hint text and
                                    // your list data
                                    new HintAdapter<String>(context, question.getMessage(), Arrays.asList(spinnerArray)),
                                    new HintSpinner.Callback<String>() {
                                        @Override
                                        public void onItemSelected(int position, String itemAtPosition) {
                                            // Here you handle the on item selected event (this skips the hint selected event)
                                        }
                                    });
                            hintSpinner.init();
                        }

                        if(question.getComponentId() == 3){
                            for (int i = 0; i < references.size(); i++)
                            {
                                RadioButton radioButton = new RadioButton(context);
                                radioButton.setText(references.get(i).getTexte());
                                radioGroup.addView(radioButton);
                            }
                        }

                        if(question.getComponentId() == 4){
                            for (int i = 0; i < references.size(); i++)
                            {
                                CheckBox checkBox = new CheckBox(context);
                                checkBox.setText(references.get(i).getTexte());
                                radioGroup.addView(checkBox);
                                //ReponseForm.addView(checkBox);
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
}
