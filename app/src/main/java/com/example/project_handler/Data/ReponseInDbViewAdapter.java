package com.example.project_handler.Data;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project_handler.Activities.MainActivity;
import com.example.project_handler.Activities.ReponsesInDbActivity;
import com.example.project_handler.Model.Formulaire;
import com.example.project_handler.Model.ReponseByFormulaireInDb;
import com.example.project_handler.Model.ReponsesByFormulaire;
import com.example.project_handler.R;
import com.example.project_handler.Utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReponseInDbViewAdapter extends BaseAdapter {
    Context context;
    ArrayList<HashMap<String, ReponseByFormulaireInDb>> listeReponses = new ArrayList<HashMap<String, ReponseByFormulaireInDb>>();
    CheckBox publishCheckBox;
    CheckBox delete;

    private MainActivity activity;

    public ReponseInDbViewAdapter(ArrayList<HashMap<String, ReponseByFormulaireInDb>> listeReponses) {
        super();
        this.listeReponses = listeReponses;
    }

    public void addContext(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return listeReponses.size();
    }

    @Override
    public Object getItem(int i) {
        return listeReponses.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        LayoutInflater mInflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView Title;
        TextView Valeur;
        TextView ReponseTextView;

        View formView = mInflater.inflate(R.layout.reponses_by_question_ticket, viewGroup, false);
        //final ReponseByFormulaireInDb reponse = listeReponses.get(i).get("Nom");

        if(listeReponses.size() <= 0) return formView;

        LinearLayout m_layout = (LinearLayout)  formView.findViewById(R.id.reponseLinear);
        LinearLayout publishLayout = new LinearLayout(viewGroup.getContext());
        publishLayout.setOrientation(LinearLayout.HORIZONTAL);
        publishCheckBox = new CheckBox(viewGroup.getContext());
        publishCheckBox.setText("Publish");
        publishCheckBox.setTextSize(20);
        publishCheckBox.setTypeface(publishCheckBox.getTypeface(), Typeface.BOLD);
        publishCheckBox.setTextColor(Color.rgb(0, 0, 255));

        delete = new CheckBox(viewGroup.getContext());
        delete.setText("Delete");
        delete.setTextSize(20);
        delete.setTextColor(Color.rgb(0, 0, 255));
        delete.setTypeface(publishCheckBox.getTypeface(), Typeface.BOLD);

        LinearLayout h_layout;

        for(Map.Entry<String, ReponseByFormulaireInDb> entry: listeReponses.get(i).entrySet()) {
            Title = new TextView(formView.getContext());
            Title.setText(entry.getKey() + "   : ");
            Title.setTextSize(20);
            Title.setTypeface(Title.getTypeface(), Typeface.BOLD);

            Valeur = new TextView(formView.getContext());
            Valeur.setText(entry.getValue().getValeur());
            Valeur.setTextSize(17);
            Valeur.setTypeface(Title.getTypeface(), Typeface.ITALIC);

            if (entry.getValue().getComponentId() == 2 || entry.getValue().getComponentId() == 3)
                Valeur.setText(entry.getValue().getTexte());

            h_layout = new LinearLayout(viewGroup.getContext());
            h_layout.setOrientation(LinearLayout.HORIZONTAL);


            h_layout.addView(Title);
            h_layout.addView(Valeur);
            m_layout.addView(h_layout);
        }

        h_layout = new LinearLayout(viewGroup.getContext());
        h_layout.setOrientation(LinearLayout.HORIZONTAL);
        h_layout.setPadding(500, 0, 0, 0);
        h_layout.addView(publishCheckBox);
        h_layout.addView(delete);
        m_layout.addView(h_layout);
        if(i%2 == 0) {
            formView.setBackgroundColor(Color.rgb(200, 200, 200));
        }
        if(i%2 == 1) {
            formView.setBackgroundColor(Color.rgb(240, 240, 240));
        }

        publishCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(viewGroup.getContext());
                builder.setTitle("Confirmation!!!");
                builder.setMessage("Are you sure you want to publish these to the server?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        for(final Map.Entry<String, ReponseByFormulaireInDb> entry: listeReponses.get(i).entrySet()) {
                            if(entry.getValue().getComponentId() == 1) {
                                try {
                                    final RequestQueue queueRep = Volley.newRequestQueue(viewGroup.getContext());
                                    JSONObject reponseObject = new JSONObject();
                                    reponseObject.put("QuestionId", entry.getValue().getQuestionId());
                                    reponseObject.put("Valeur", entry.getValue().getValeur());
                                    reponseObject.put("CreePar", "Concepteur");
                                    Date c = Calendar.getInstance().getTime();

                                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
                                    String formattedDate = df.format(c);
                                    reponseObject.put("CreeLe", formattedDate);
                                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.URL_REPONSE, reponseObject, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Toast.makeText(viewGroup.getContext(),"Reponses sauvegardées avec succès!", Toast.LENGTH_LONG).show();
                                            DatabaseHandler databaseHandler = new DatabaseHandler(viewGroup.getContext());
                                            databaseHandler.deleteReponse(entry.getValue().getId());
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
                            if(entry.getValue().getComponentId() == 2) {
                                try {
                                    final RequestQueue queueRep = Volley.newRequestQueue(viewGroup.getContext());
                                    JSONObject reponseObject = new JSONObject();
                                    reponseObject.put("QuestionId", entry.getValue().getQuestionId());
                                    reponseObject.put("Valeur", entry.getValue().getValeur());
                                    reponseObject.put("Code", entry.getValue().getCode());
                                    reponseObject.put("Texte", entry.getValue().getTexte());
                                    reponseObject.put("CreePar", "Concepteur");
                                    Date c = Calendar.getInstance().getTime();

                                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
                                    String formattedDate = df.format(c);
                                    reponseObject.put("CreeLe", formattedDate);
                                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.URL_REPONSE, reponseObject, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Toast.makeText(viewGroup.getContext(),"Reponses sauvegardées avec succès!", Toast.LENGTH_LONG).show();
                                            DatabaseHandler databaseHandler = new DatabaseHandler(viewGroup.getContext());
                                            databaseHandler.deleteReponse(entry.getValue().getId());
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
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                /*
                ReponsesInDbActivity.listView.setAdapter(null);

                if (context.getClass().equals(ReponsesInDbActivity.class)) {
                    Formulaire formulaire = ReponsesInDbActivity.formulaire;
                    ((ReponsesInDbActivity) context).getIntent().putExtra("formulaire", formulaire);
                    ((ReponsesInDbActivity) context).finish();
                    context.startActivity(((ReponsesInDbActivity) context).getIntent());
                }*/
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(viewGroup.getContext());
                builder.setTitle("Confirmation!!!");
                builder.setMessage("Are you sure you want to delete these ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        for(final Map.Entry<String, ReponseByFormulaireInDb> entry: listeReponses.get(i).entrySet()) {
                            DatabaseHandler databaseHandler = new DatabaseHandler(viewGroup.getContext());
                            databaseHandler.deleteReponse(entry.getValue().getId());
                        }

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

        //ReponsesInDbActivity.listView.setAdapter(null);

        return formView;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public ViewHolder(View itemView, Context ctx){
            super(itemView);
            context = ctx;


        }


        public void onClick(View v){

        }
    }
}
