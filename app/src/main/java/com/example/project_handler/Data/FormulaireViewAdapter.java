package com.example.project_handler.Data;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project_handler.Activities.MainActivity;
import com.example.project_handler.Activities.ReponseFormActivity;
import com.example.project_handler.Activities.ReponsesByFormulaireActivity;
import com.example.project_handler.Model.Formulaire;
import com.example.project_handler.R;

import java.util.ArrayList;

public class FormulaireViewAdapter extends BaseAdapter {

    Context context;
    ArrayList<Formulaire> listForm = new ArrayList<Formulaire>();
    private MainActivity activity;

    public FormulaireViewAdapter(ArrayList<Formulaire> listForm) {
        super();
        this.listForm = listForm;
    }

    @Override
    public int getCount() {
        return listForm.size();
    }

    @Override
    public Object getItem(int i) {
        return listForm.get(i);
    }

    @Override
    public long getItemId(int i) {
        return listForm.get(i).getId();
    }

    @Override
    public View getView(int i, View view, final ViewGroup viewGroup) {
        LayoutInflater mInflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView Name;
        TextView Description;
        TextView Projet;
        ImageButton fillOut;
        ImageButton questions;
        ImageButton entries;
        ImageButton delete;


        View formView = mInflater.inflate(R.layout.formulaire_activity, viewGroup, false);
        final Formulaire formulaire = listForm.get(i);

        Name = (TextView) formView.findViewById(R.id.nameTextView);
        Description = (TextView) formView.findViewById(R.id.descriptionTextView);
        Projet = (TextView) formView.findViewById(R.id.projetTextView);
        fillOut = (ImageButton) formView.findViewById(R.id.fillOutImageButton);
        questions = (ImageButton) formView.findViewById(R.id.detailsImageButton);
        entries = (ImageButton) formView.findViewById(R.id.entriesImageButton);
        delete = (ImageButton) formView.findViewById(R.id.deleteImageButton);

        Name.setText(formulaire.getName());
        Description.setText(formulaire.getDescription());
        Projet.setText(formulaire.getProjet().getName());

        if(i%2 == 0) {
            formView.setBackgroundColor(Color.rgb(200, 200, 200));
            fillOut.setBackgroundColor(Color.rgb(200, 200, 200));
            questions.setBackgroundColor(Color.rgb(200, 200, 200));
            entries.setBackgroundColor(Color.rgb(200, 200, 200));
            delete.setBackgroundColor(Color.rgb(200, 200, 200));
        }
        if(i%2 == 1) {
            formView.setBackgroundColor(Color.rgb(240, 240, 240));
            fillOut.setBackgroundColor(Color.rgb(240, 240, 240));
            questions.setBackgroundColor(Color.rgb(240, 240, 240));
            entries.setBackgroundColor(Color.rgb(240, 240, 240));
            delete.setBackgroundColor(Color.rgb(240, 240, 240));
        }

        fillOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(viewGroup.getContext(), ReponseFormActivity.class);

                intent.putExtra("formulaire", formulaire);
                viewGroup.getContext().startActivity(intent);
            }
        });

        questions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("questions of " + formulaire.getName());
            }
        });

        entries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(viewGroup.getContext(), ReponsesByFormulaireActivity.class);

                intent.putExtra("formulaire", formulaire);
                viewGroup.getContext().startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Are you sure you want to delete " + formulaire.getName());
            }
        });

        return formView;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView Name;
        TextView Description;

        public ViewHolder(View itemView, Context ctx){
            super(itemView);
            context = ctx;

            //Name = (TextView) itemView.findViewById(R.id.nameTextView);
            //Description = (TextView) itemView.findViewById(R.id.descriptionTextView);
        }


        public void onClick(View v){

        }
    }
}