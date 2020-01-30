package com.example.project_handler.Utils;

public class Constants {
    public final static String PORT = "26922";
    public final static String URL_SERVER = "http://192.168.128.13";
    public static final String URL_PROJET = URL_SERVER + ":" + PORT + "/api/projet";
    public final static String URL_REPONSE = URL_SERVER + ":" + PORT + "/api/reponse";
    public final static String URL_FORMULAIRE = URL_SERVER + ":" + PORT + "/api/formulaire";
    public final static String URL_QUESTION = URL_SERVER + ":" + PORT + "/api/questionsbyformulaire";
    public final static String URL_REFERENCE = URL_SERVER + ":" + PORT + "/api/dynamicreferencebyquestion";
    public final static String URL_REPONSE_FORM = URL_SERVER + ":" + PORT + "/api/reponsesbyformulaire";
    public final static String URL_MAX_GROUPE = URL_SERVER + ':' + PORT + "/api/maxgroupe";

    public static final int DATABASE_VERSION = 1;
    public static String DATABASE_NAME = "ProjectHandler";
    public static String TABLE_NAME = "ReponseByFormulaire";
    public static String TABLE_QUESTION = "Question";
    public static String TABLE_REFERENCE = "DynamicReference";

    public static final String KEY_ID = "Id";
    public static final String KEY_FORMULAIRE_ID = "FormulaireId";
    public static final String KEY_VALEUR = "Valeur";
    public static final String KEY_QUESTION_ID = "QuestionId";
    public static final String KEY_QUESTION_NAME = "QuestionName";
    public static final String KEY_QUESTION_DESCRIPTION = "QuestionDescription";
    public static final String KEY_REFERENCE_ID = "ReferenceId";
    public static final String KEY_CODE = "Code";
    public static final String KEY_TEXTE = "Texte";
    public static final String KEY_CREE_PAR = "CreePar";
    public static final String KEY_CREE_LE = "CreeLe";
    public static final String KEY_GROUPE = "Groupe";

    public static final String Q_NAME = "Name";
    public static final String Q_MESSAGE = "Message";
    public static final String Q_COMPONENT_ID = "ComponentId";
    public static final String Q_TYPE_DONNEE_ID = "TypeDonneeId";
    public static final String Q_MINIMUM = "Minimum";
    public static final String Q_MAXIMUM = "Maximum";
    public static final String Q_REQUIRED = "Required";
}
