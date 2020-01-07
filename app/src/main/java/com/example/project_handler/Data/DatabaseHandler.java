package com.example.project_handler.Data;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.project_handler.Model.Question;
import com.example.project_handler.Model.ReponsesByFormulaire;
import com.example.project_handler.Utils.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseHandler extends SQLiteOpenHelper {
    public DatabaseHandler(@Nullable Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_ReponseByFormulaire_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY, " + Constants.KEY_FORMULAIRE_ID + " TEXT, "
                + Constants.KEY_VALEUR + " TEXT, "
                + Constants.KEY_QUESTION_ID + " INTEGER, " + Constants.KEY_REFERENCE_ID + " INTEGER, "
                + Constants.KEY_CODE + " TEXT, " + Constants.KEY_TEXTE + " TEXT, "
                + Constants.KEY_CREE_PAR + " TEXT, " + Constants.KEY_CREE_LE + " DATE); "
                + "CREATE TABLE " + Constants.TABLE_QUESTION + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY, " + Constants.Q_NAME + " TEXT, "
                + Constants.Q_MESSAGE + " TEXT, " + Constants.KEY_FORMULAIRE_ID + " TEXT, "
                + Constants.Q_COMPONENT_ID + " INTEGER, " + Constants.Q_TYPE_DONNEE_ID + " INTEGER, "
                + Constants.Q_MINIMUM + " TEXT, " + Constants.Q_MAXIMUM + " TEXT, " + Constants.Q_REQUIRED + " BOOLEAN,"
                + Constants.KEY_CREE_PAR + " TEXT, " + Constants.KEY_CREE_LE + " DATE, " + Constants.KEY_QUESTION_ID + " INTEGER);";

        sqLiteDatabase.execSQL(CREATE_ReponseByFormulaire_TABLE);
        //sqLiteDatabase.execSQL(CREATE_Question_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
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

        Question question1 = getQuestion(question.getId() + "");

        if (question1 == null) {
            sqLiteDatabase.insert(Constants.TABLE_QUESTION, null, contentValues);
            sqLiteDatabase.close();
        }
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

        cursor.moveToNext();

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

    public void addReponseByFormulaire(ReponsesByFormulaire reponsesByFormulaire, String idForm){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.KEY_FORMULAIRE_ID, idForm);
        contentValues.put(Constants.KEY_VALEUR, reponsesByFormulaire.getValeur());
        contentValues.put(Constants.KEY_QUESTION_ID, reponsesByFormulaire.getQuestionId());
        contentValues.put(Constants.KEY_REFERENCE_ID, reponsesByFormulaire.getReferenceId());
        contentValues.put(Constants.KEY_CODE, reponsesByFormulaire.getCode());
        contentValues.put(Constants.KEY_TEXTE, reponsesByFormulaire.getTexte());
        contentValues.put(Constants.KEY_CREE_PAR, reponsesByFormulaire.getCreePar());
        contentValues.put(Constants.KEY_CREE_LE, reponsesByFormulaire.getCreeLe().toString());

        sqLiteDatabase.insert(Constants.TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
    }

    public ArrayList<ReponsesByFormulaire> getReponseByFormulaires(String idForm){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<ReponsesByFormulaire> reponsesByFormulaires = new ArrayList<>();

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
                    date = formatter.parse(cursor.getString(8));
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }

                ReponsesByFormulaire reponsesByFormulaire = new ReponsesByFormulaire(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(2), Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7), date);

                reponsesByFormulaires.add(reponsesByFormulaire);

            }while (cursor.moveToNext());
        }

        return reponsesByFormulaires;
    }

    public void delete(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + Constants.TABLE_NAME);
        sqLiteDatabase.close();
    }
}
