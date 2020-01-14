package com.example.project_handler.Data;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

import com.example.project_handler.Model.Component;
import com.example.project_handler.Model.Question;
import com.example.project_handler.Model.ReponseByFormulaireInDb;
import com.example.project_handler.Model.ReponsesByFormulaire;
import com.example.project_handler.Utils.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHandler extends SQLiteOpenHelper {
    public DatabaseHandler(@Nullable Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_ReponseByFormulaire_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY, " + Constants.KEY_FORMULAIRE_ID + " TEXT, "
                + Constants.KEY_VALEUR + " TEXT, "
                + Constants.KEY_QUESTION_ID + " INTEGER, " + Constants.KEY_QUESTION_NAME + " TEXT, "
                + Constants.KEY_QUESTION_DESCRIPTION + " TEXT, " + Constants.KEY_REFERENCE_ID + " INTEGER, "
                + Constants.KEY_CODE + " TEXT, " + Constants.KEY_TEXTE + " TEXT, "
                + Constants.KEY_CREE_PAR + " TEXT, " + Constants.KEY_CREE_LE + " DATE, " + Constants.Q_COMPONENT_ID + " INTEGER); ";

        /*
        String CREATE_Question_TABLE = "CREATE TABLE " + Constants.TABLE_QUESTION + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY, " + Constants.Q_NAME + " TEXT, "
                + Constants.Q_MESSAGE + " TEXT, " + Constants.KEY_FORMULAIRE_ID + " TEXT, "
                + Constants.Q_COMPONENT_ID + " INTEGER, " + Constants.Q_TYPE_DONNEE_ID + " INTEGER, "
                + Constants.Q_MINIMUM + " TEXT, " + Constants.Q_MAXIMUM + " TEXT, " + Constants.Q_REQUIRED + " BOOLEAN,"
                + Constants.KEY_CREE_PAR + " TEXT, " + Constants.KEY_CREE_LE + " DATE, " + Constants.KEY_QUESTION_ID + " INTEGER);";*/

        sqLiteDatabase.execSQL(CREATE_ReponseByFormulaire_TABLE);
        //sqLiteDatabase.execSQL(CREATE_Question_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_QUESTION);
        onCreate(sqLiteDatabase);
    }

    public void addQuestion(Question question){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.KEY_QUESTION_ID, question.getId());
        contentValues.put(Constants.Q_NAME, question.getName());
        contentValues.put(Constants.Q_MESSAGE, question.getMessage());
        contentValues.put(Constants.KEY_FORMULAIRE_ID, question.getFormulaireId());
        contentValues.put(Constants.Q_COMPONENT_ID, question.getComponentId());
        contentValues.put(Constants.Q_TYPE_DONNEE_ID, question.getTypeDonneeId());
        contentValues.put(Constants.Q_MINIMUM, question.getMinimum());
        contentValues.put(Constants.Q_MAXIMUM, question.getMaximum());
        contentValues.put(Constants.Q_REQUIRED, question.getRequired());
        contentValues.put(Constants.KEY_CREE_PAR, question.getCreePar());
        contentValues.put(Constants.KEY_CREE_LE, question.getCreeLe().toString());

        //Question question1 = getQuestion(question.getId() + "");

        /*
        if (question1 == null) {
            sqLiteDatabase.insert(Constants.TABLE_QUESTION, null, contentValues);
            sqLiteDatabase.close();
        }*/
    }

    public Question getQuestion(String id){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<ReponsesByFormulaire> reponsesByFormulaires = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.query(Constants.TABLE_QUESTION, new String[] {Constants.KEY_ID,
                Constants.Q_NAME, Constants.Q_MESSAGE, Constants.KEY_FORMULAIRE_ID,
                Constants.Q_COMPONENT_ID, Constants.Q_TYPE_DONNEE_ID, Constants.Q_MINIMUM,
                Constants.Q_MAXIMUM, Constants.Q_REQUIRED, Constants.KEY_CREE_PAR, Constants.KEY_CREE_LE, Constants.KEY_QUESTION_ID},
                Constants.KEY_QUESTION_ID + "=?",
                new String[]{id}, null, null, null, null);

        cursor.moveToFirst();

        DateFormat formatter=new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        Date date = new Date();
        try {
            date = formatter.parse(cursor.getString(10));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        Question question = new Question(Integer.parseInt(cursor.getString(11)), cursor.getString(1),
                cursor.getString(2), Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)),
                Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)), Boolean.parseBoolean(cursor.getString(8)), cursor.getString(9), date);

        return question;
    }

    public void addReponseByFormulaire(ReponseByFormulaireInDb reponseByFormulaireInDb, String idForm){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.KEY_FORMULAIRE_ID, idForm);
        contentValues.put(Constants.KEY_VALEUR, reponseByFormulaireInDb.getValeur());
        contentValues.put(Constants.KEY_QUESTION_ID, reponseByFormulaireInDb.getQuestionId());
        contentValues.put(Constants.KEY_QUESTION_NAME, reponseByFormulaireInDb.getQuestionName());
        contentValues.put(Constants.KEY_QUESTION_DESCRIPTION, reponseByFormulaireInDb.getQuestionDescription());
        contentValues.put(Constants.KEY_REFERENCE_ID, reponseByFormulaireInDb.getReferenceId());
        contentValues.put(Constants.KEY_CODE, reponseByFormulaireInDb.getCode());
        contentValues.put(Constants.KEY_TEXTE, reponseByFormulaireInDb.getTexte());
        contentValues.put(Constants.KEY_CREE_PAR, reponseByFormulaireInDb.getCreePar());
        contentValues.put(Constants.KEY_CREE_LE, reponseByFormulaireInDb.getCreeLe().toString());
        contentValues.put(Constants.Q_COMPONENT_ID, reponseByFormulaireInDb.getComponentId() + "");

        sqLiteDatabase.insert(Constants.TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
    }

    public void addReponsesByFormulaire( HashMap<String, ReponseByFormulaireInDb> reponsesByFormulaireInDb, String idForm){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        for(Map.Entry<String, ReponseByFormulaireInDb> entry: reponsesByFormulaireInDb.entrySet()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Constants.KEY_FORMULAIRE_ID, idForm);
            contentValues.put(Constants.KEY_VALEUR, entry.getValue().getValeur());
            contentValues.put(Constants.KEY_QUESTION_ID, entry.getValue().getQuestionId());
            contentValues.put(Constants.KEY_QUESTION_NAME, entry.getValue().getQuestionName());
            contentValues.put(Constants.KEY_QUESTION_DESCRIPTION, entry.getValue().getQuestionDescription());
            contentValues.put(Constants.KEY_REFERENCE_ID, entry.getValue().getReferenceId());
            contentValues.put(Constants.KEY_CODE, entry.getValue().getCode());
            contentValues.put(Constants.KEY_TEXTE, entry.getValue().getTexte());
            contentValues.put(Constants.KEY_CREE_PAR, entry.getValue().getCreePar());
            contentValues.put(Constants.KEY_CREE_LE, entry.getValue().getCreeLe().toString());
            contentValues.put(Constants.Q_COMPONENT_ID, entry.getValue().getComponentId() + "");

            sqLiteDatabase.insert(Constants.TABLE_NAME, null, contentValues);
        }
        sqLiteDatabase.close();
    }

    public ArrayList<ReponseByFormulaireInDb> getReponseByFormulaires(String idForm){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<ReponseByFormulaireInDb> reponseByFormulaireInDbs = new ArrayList<>();

        /*
        Cursor cursor = sqLiteDatabase.query(Constants.TABLE_NAME, new String[] {Constants.KEY_ID,
                Constants.KEY_VALEUR, Constants.KEY_FORMULAIRE_ID, Constants.KEY_QUESTION_ID,
                Constants.KEY_REFERENCE_ID, Constants.KEY_CODE, Constants.KEY_TEXTE,
                Constants.KEY_CREE_PAR, Constants.KEY_CREE_LE},
                Constants.KEY_FORMULAIRE_ID + "=?",
                new String[]{idForm}, null, null, null, null);*/

        String selectAll = "SELECT * FROM " + Constants.TABLE_NAME
                + " WHERE " + Constants.KEY_FORMULAIRE_ID + " = " + idForm;

        Cursor cursor = sqLiteDatabase.rawQuery(selectAll, null);

        if(cursor.moveToFirst()){
            do{
                DateFormat formatter=new SimpleDateFormat("yyyyMMdd HH:mm:ss");
                Date date = new Date();
                try {
                    date = formatter.parse(cursor.getString(10));
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
                System.out.println("Date sauvegardee: " + cursor.getString(10));
                System.out.println("Date formatee: " + date);
                ReponseByFormulaireInDb reponseByFormulaireInDb = new ReponseByFormulaireInDb();
                reponseByFormulaireInDb.setId(Integer.parseInt(cursor.getString(0)));
                reponseByFormulaireInDb.setValeur(cursor.getString(2));
                reponseByFormulaireInDb.setQuestionId(Integer.parseInt(cursor.getString(3)));
                reponseByFormulaireInDb.setQuestionName(cursor.getString(4));
                reponseByFormulaireInDb.setQuestionDescription(cursor.getString(5));
                reponseByFormulaireInDb.setReferenceId(Integer.parseInt(cursor.getString(6)));
                reponseByFormulaireInDb.setCode(cursor.getString(7));
                reponseByFormulaireInDb.setTexte(cursor.getString(8));
                reponseByFormulaireInDb.setCreePar(cursor.getString(9));
                reponseByFormulaireInDb.setCreeLe(date);
                reponseByFormulaireInDb.setComponentId(Integer.parseInt(cursor.getString(11)));


                reponseByFormulaireInDbs.add(reponseByFormulaireInDb);

            }while (cursor.moveToNext());
        }

        return reponseByFormulaireInDbs;
    }

    public void delete(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + Constants.TABLE_NAME);
        sqLiteDatabase.close();
    }

    public void deleteReponse(int id){
        System.out.println("ReponseId: " + id);
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + Constants.TABLE_NAME + " WHERE " + Constants.KEY_ID + " = " + id);
        sqLiteDatabase.close();
    }
}
