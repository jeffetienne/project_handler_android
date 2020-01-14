package com.example.project_handler.Data;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project_handler.Activities.MainActivity;
import com.example.project_handler.Activities.ReponsesByFormulaireActivity;
import com.example.project_handler.Model.Formulaire;
import com.example.project_handler.Model.ReponsesByFormulaire;
import com.example.project_handler.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ReponseViewAdapter extends BaseAdapter {
    Context context;
    ArrayList<HashMap<String, ReponsesByFormulaire>> listeReponses = new ArrayList<HashMap<String, ReponsesByFormulaire>>();

    private MainActivity activity;

    public ReponseViewAdapter(ArrayList<HashMap<String, ReponsesByFormulaire>> listeReponses) {
        super();
        this.listeReponses = listeReponses;
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
    public View getView(int i, View view, final ViewGroup viewGroup) {
        LayoutInflater mInflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView Title;
        TextView Valeur;
        TextView ReponseTextView;

        View formView = mInflater.inflate(R.layout.reponses_by_question_ticket, viewGroup, false);
        final ReponsesByFormulaire reponse = listeReponses.get(i).get("Nom");

        if(listeReponses.size() <= 0) return formView;
        LinearLayout m_layout = (LinearLayout)  formView.findViewById(R.id.reponseLinear);


        for(Map.Entry<String, ReponsesByFormulaire> entry: listeReponses.get(i).entrySet()) {
            Title = new TextView(formView.getContext());
            Title.setText(entry.getKey() + "   : ");
            Title.setTextSize(20);
            Title.setTypeface(Title.getTypeface(), Typeface.BOLD);

            Valeur = new TextView(formView.getContext());
            Valeur.setText(entry.getValue().getValeur());
            Valeur.setTextSize(17);
            Valeur.setTypeface(Title.getTypeface(), Typeface.ITALIC);
            if (entry.getValue().getQuestion().getComponentId() == 2 || entry.getValue().getQuestion().getComponentId() == 3)
                Valeur.setText(entry.getValue().getTexte());

            LinearLayout h_layout = new LinearLayout(viewGroup.getContext());
            h_layout.setOrientation(LinearLayout.HORIZONTAL);


            h_layout.addView(Title);
            h_layout.addView(Valeur);
            m_layout.addView(h_layout);
        }

        if(i%2 == 0) {
            formView.setBackgroundColor(Color.rgb(200, 200, 200));
        }
        if(i%2 == 1) {
            formView.setBackgroundColor(Color.rgb(240, 240, 240));
        }




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
