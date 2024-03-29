package com.example.project_handler.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.example.project_handler.Utils.TextValidator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import me.srodrigo.androidhintspinner.HintAdapter;
import me.srodrigo.androidhintspinner.HintSpinner;

import static com.android.volley.VolleyLog.TAG;

public class ReponseFormActivity extends AppCompatActivity {

    Context context;
    TextView titreForm;
    LinearLayout ReponseForm;
    EditText editText;
    Spinner combobox;
    RadioGroup radioGroup;
    ArrayList<RadioButton> radioButtons;
    ArrayList<CheckBox> checkBoxes;
    DatePickerDialog datePicker;
    Button saveButton;

    String test;

    String[] spinnerArray;
    HashMap<String, String> spinnerMap = new HashMap<String, String>();
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

    private void getQuestions(final String idForm) {
        final ArrayList<Question> questions = new ArrayList<Question>();
        final RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Constants.URL_QUESTION + "/" + idForm,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        for (int i = 0; i < response.length(); i++) {
                            try {
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

                            } catch (JSONException e) {
                                e.printStackTrace();
                                //nameTextView.setText(e.getMessage());
                            }
                        }

                        for (int compteur = 0; compteur < questions.size(); compteur++) {
                            if (questions.get(compteur).getComponentId() == 1) {
                                editText = new EditText(context);
                                editText.setId(compteur);
                                editText.setHint(questions.get(compteur).getMessage());
                                ReponseForm.addView(editText);
                                /*
                                final int compt = compteur;
                                editText.addTextChangedListener(new TextValidator(editText) {
                                    @Override public void validate(TextView textView, String text) {
                                        Toast.makeText(context,textView.getText(), Toast.LENGTH_LONG).show();
                                        if(editText.getText().equals("") && questions.get(compt).getRequired() == true)
                                        {

                                            textView.setText("Ce champ est obligatoire!!!");
                                            Toast.makeText(context,textView.getText(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });*/
                            }

                            if (questions.get(compteur).getComponentId() == 2) {
                                combobox = new Spinner(context);
                                combobox.setId(compteur);

                                ReponseForm.addView(combobox);

                                getReferences(questions.get(compteur), combobox, radioGroup);
                            }

                            if (questions.get(compteur).getComponentId() == 3) {
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

                            if (questions.get(compteur).getComponentId() == 4) {
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

                            if (questions.get(compteur).getComponentId() == 5) {
                                editText = new EditText(context);
                                editText.setId(compteur);
                                editText.setHint(questions.get(compteur).getMessage());
                                editText.setSingleLine(false);
                                editText.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                                //editText.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                                ReponseForm.addView(editText);
                            }

                            if (questions.get(compteur).getComponentId() == 6) {
                                editText = new EditText(context);
                                editText.setId(compteur);
                                editText.setHint(questions.get(compteur).getMessage());
                                editText.setSingleLine(false);
                                editText.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                                //editText.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                                ReponseForm.addView(editText);
                                editText.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        final Calendar cldr = Calendar.getInstance();
                                        int day = cldr.get(Calendar.DAY_OF_MONTH);
                                        int month = cldr.get(Calendar.MONTH);
                                        int year = cldr.get(Calendar.YEAR);
                                        // date picker dialog
                                        datePicker = new DatePickerDialog(ReponseFormActivity.this,
                                                new DatePickerDialog.OnDateSetListener() {
                                                    @Override
                                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                                        editText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                                    }
                                                }, year, month, day);
                                        datePicker.show();
                                    }
                                });
                            }

                            if (questions.get(compteur).getComponentId() == 7) {
                                editText = new EditText(context);
                                editText.setId(compteur);
                                editText.setHint(questions.get(compteur).getMessage());
                                LocationManager locationManager = (LocationManager)
                                        getSystemService(Context.LOCATION_SERVICE);
                                LocationListener locationListener = new MyLocationListener();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                        // TODO: Consider calling
                                        //    Activity#requestPermissions
                                        // here to request the missing permissions, and then overriding
                                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                        //                                          int[] grantResults)
                                        // to handle the case where the user grants the permission. See the documentation
                                        // for Activity#requestPermissions for more details.
                                        return;
                                    }
                                }
                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
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

                                    reponsesByFormulaire.setCreeLe(c);
                                    if(questions.get(compteur).getComponentId() == 1 || questions.get(compteur).getComponentId() == 5 || questions.get(compteur).getComponentId() == 6) {

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

                                    ArrayList<DynamicReference> dynamicReferences = databaseHandler.getReferences(questions.get(compteur).getId() + "");

                                    if(questions.get(compteur).getComponentId() == 3){
                                        RadioButton radioButton;
                                        RadioGroup radioGroup;

                                        /*
                                        radioGroup = (RadioGroup) ReponseForm.findViewById(compteur);

                                        radioButton = (RadioButton) ReponseForm.findViewById(radioGroup.getCheckedRadioButtonId());

                                        reponsesByFormulaire.setValeur(radioButton.getText().toString());
                                        reponsesByFormulaire.setTexte(radioButton.getText().toString());*/
                                        for (int i = 0; i < dynamicReferences.size(); i++){
                                            radioButton = ReponseForm.findViewById(dynamicReferences.get(i).getId());
                                            if (radioButton.isChecked()){
                                                reponsesByFormulaire.setCode(dynamicReferences.get(i).getCode());
                                                reponsesByFormulaire.setTexte(dynamicReferences.get(i).getTexte());
                                                reponsesByFormulaire.setValeur(dynamicReferences.get(i).getCode());
                                            }
                                        }
                                    }

                                    if(questions.get(compteur).getComponentId() == 4){
                                        String checkedValues = "";
                                        String checkedText = "";




                                        CheckBox checkBox;
                                        for (int i = 0; i < dynamicReferences.size(); i++){
                                            checkBox = new CheckBox(context);
                                            checkBox = (CheckBox) ReponseForm.findViewById(dynamicReferences.get(i).getId());
                                            if(checkBox.isChecked()){

                                                if(checkedValues.isEmpty()) {
                                                    checkedValues = dynamicReferences.get(i).getCode();
                                                    checkedText = dynamicReferences.get(i).getTexte();
                                                }
                                                else {
                                                    checkedValues += "|" + dynamicReferences.get(i).getCode();
                                                    checkedText += "|" + dynamicReferences.get(i).getTexte();
                                                }
                                            }
                                        }
                                        reponsesByFormulaire.setValeur(checkedValues);
                                        reponsesByFormulaire.setCode(checkedValues);
                                        reponsesByFormulaire.setTexte(checkedText);
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
                                Date date = new Date();
                                reference.setCreeLe(date);

                                references.add(reference);

                                databaseHandler.addReference(reference);

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
                                radioButton.setId(references.get(i).getId());
                                radioGroup.addView(radioButton);
                            }
                        }

                        if(question.getComponentId() == 4){
                            for (int i = 0; i < references.size(); i++)
                            {
                                CheckBox checkBox = new CheckBox(context);
                                checkBox.setText(references.get(i).getTexte());
                                checkBox.setId(references.get(i).getId());
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

    public class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            editText.setText("");
            //pb.setVisibility(View.INVISIBLE);
            Toast.makeText(
                    getBaseContext(),
                    "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                            + loc.getLongitude(), Toast.LENGTH_SHORT).show();
            String longitude = "Longitude: " + loc.getLongitude();
            Log.v(TAG, longitude);
            String latitude = "Latitude: " + loc.getLatitude();
            Log.v(TAG, latitude);

            /*------- To get city name from coordinates -------- */
            String cityName = null;
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
                if (addresses.size() > 0) {
                    System.out.println(addresses.get(0).getLocality());
                    cityName = addresses.get(0).getLocality();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
                    + cityName;
            editText.setText(s);
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }
}
