package com.example.project_handler.Data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_handler.Model.Projet;
import com.example.project_handler.R;

import java.util.ArrayList;
import java.util.List;

public class ProjetRecyclerViewAdapter extends RecyclerView.Adapter<ProjetRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Projet> projets;

    public ProjetRecyclerViewAdapter(Context context, ArrayList<Projet> projets) {
        this.context = context;
        this.projets = projets;
    }

    @Override
    public ProjetRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.projet_activity, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Projet projet = this.projets.get(position);

        holder.Name.setText(projet.getName());
        holder.Description.setText(projet.getDescription());
        //holder.Domaine.setText(projet.getDomaine().getName());
        holder.CreePar.setText(projet.getCreePar());
        holder.CreeLe.setText(projet.getCreeLe().toString());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return this.projets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView Name;
        TextView Description;
        TextView Domaine;
        TextView CreePar;
        TextView CreeLe;

        public ViewHolder(View itemView, Context ctx){
            super(itemView);
            context = ctx;

            Name = (TextView) itemView.findViewById(R.id.projetName);
            Description = (TextView) itemView.findViewById(R.id.projetDescription);
            Domaine = (TextView) itemView.findViewById(R.id.projetDomaine);
            CreePar = (TextView) itemView.findViewById(R.id.projetCreePar);
            CreeLe = (TextView) itemView.findViewById(R.id.projetCreeLe);
        }


        public void onClick(View v){

        }
    }
}
