package app.mueller.schiller.weber.com.vicab.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import app.mueller.schiller.weber.com.vicab.PersistanceClasses.LanguageItem;
import app.mueller.schiller.weber.com.vicab.PersistanceClasses.ListItem;
import app.mueller.schiller.weber.com.vicab.PersistanceClasses.VocItem;


public class DBAdmin {

    private ToDoDBOpenHelper dbHelper;

    private SQLiteDatabase db;

    public DBAdmin(Context context) {
        dbHelper = new ToDoDBOpenHelper(context, ViCabContract.DATA_BASE_NAME, null,
                ViCabContract.DATABASE_VERSION);
    }

    public void open() throws SQLException {
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            db = dbHelper.getReadableDatabase();
        }
    }

    public void close() {
        db.close();
    }

    public long addLanguage(LanguageItem langItem) {
        ContentValues languageItem = new ContentValues();

        languageItem.put(ViCabContract.LanguageEntry.COLUMN_NAME_COUPLE, langItem.getCouple());
        languageItem.put(ViCabContract.LanguageEntry.COLUMN_NAME_SOURCE_LANGUAGE, langItem.getSourceLanguage());
        languageItem.put(ViCabContract.LanguageEntry.COLUMN_NAME_TARGET_LANGUAGE, langItem.getTargetLanguage());

        return db.insert(ViCabContract.LanguageEntry.TABLE_NAME, null, languageItem);
    }


    public long addList(String listName) {
        ContentValues listItem = new ContentValues();

        listItem.put(ViCabContract.ListEntry.COLUMN_NAME_NAME, listName);
        listItem.put(ViCabContract.ListEntry.COLUMN_NAME_HAS_LANGUAGE, ViCabContract.CHOSEN_LANGUAGE_MIX);

        return db.insert(ViCabContract.ListEntry.TABLE_NAME, null, listItem);
    }


    public long addVocab(String sourceVocab, String targetVocab, String fotoLink, String soundLink, String wordType, int importance, String hasList) {
        ContentValues vocItem = new ContentValues();

        vocItem.put(ViCabContract.VocabEntry.COLUMN_NAME_SOURCE_VOCAB, sourceVocab);
        vocItem.put(ViCabContract.VocabEntry.COLUMN_NAME_TARGET_VOCAB, targetVocab);
        vocItem.put(ViCabContract.VocabEntry.COLUMN_NAME_FOTO_LINK, fotoLink);
        vocItem.put(ViCabContract.VocabEntry.COLUMN_NAME_SOUND_LINK, soundLink);
        vocItem.put(ViCabContract.VocabEntry.COLUMN_NAME_WORD_TYPE, wordType);
        vocItem.put(ViCabContract.VocabEntry.COLUMN_NAME_IMPORTANCE, importance);
        vocItem.put(ViCabContract.VocabEntry.COLUMN_NAME_HAS_LIST, hasList);
        vocItem.put(ViCabContract.VocabEntry.COLUMN_NAME_HAS_LANGUAGE, ViCabContract.CHOSEN_LANGUAGE_MIX);

        return db.insert(ViCabContract.VocabEntry.TABLE_NAME, null, vocItem);
    }


    public int removeLanguage(LanguageItem item) {

        String whereClause = ViCabContract.LanguageEntry.COLUMN_NAME_SOURCE_LANGUAGE + " = '" + item.getSourceLanguage() + "' AND "
                + ViCabContract.LanguageEntry.COLUMN_NAME_TARGET_LANGUAGE + " = '" + item.getTargetLanguage() + "'";

        return db.delete(ViCabContract.LanguageEntry.TABLE_NAME, whereClause, null);
    }

    public int removeList(ListItem item) {

        String whereClause = ViCabContract.ListEntry.COLUMN_NAME_NAME + " = '" + item.getName() + "'";

        return db.delete(ViCabContract.ListEntry.TABLE_NAME, whereClause, null);
    }

    public int removeVocab(VocItem item) {

        String whereClause = ViCabContract.VocabEntry.COLUMN_NAME_SOURCE_VOCAB + " = '" + item.getSourceVocab() + "' AND "
                + ViCabContract.VocabEntry.COLUMN_NAME_TARGET_VOCAB + " = '" + item.getTargetVocab() + "'";

        return db.delete(ViCabContract.VocabEntry.TABLE_NAME, whereClause, null);
    }

    public int removeAllVocabWithSameAsDeletedLanguage(LanguageItem item) {

        String language = item.getCouple();
        String whereClause = ViCabContract.VocabEntry.COLUMN_NAME_HAS_LANGUAGE + " = '" + language + "'";

        return db.delete(ViCabContract.LanguageEntry.TABLE_NAME, whereClause, null);
    }

    /*

    public LanguageItem getOneLanguageItem (String id) {
        String whereClause = ViCabContract.LanguageEntry.COLUMN_NAME_ENTRY_ID + " = '" + id + "'";

        Cursor cursor = db.query(ViCabContract.LanguageEntry.TABLE_NAME, new String[]{
        ViCabContract.LanguageEntry.COLUMN_NAME_COUPLE, ViCabContract.LanguageEntry.COLUMN_NAME_SOURCE_LANGUAGE, ViCabContract.LanguageEntry.COLUMN_NAME_TARGET_LANGUAGE},
                whereClause, null, null, null, null);
        String couple = cursor.getString(ViCabContract.LanguageEntry.COLUMN_COUPLE_INDEX);
        String sourceLanguage = cursor.getString(ViCabContract.LanguageEntry.COLUMN_SOURCE_LANGUAGE_INDEX);
        String targetLanguage = cursor.getString(ViCabContract.LanguageEntry.COLUMN_TARGET_LANGUAGE_INDEX);
        LanguageItem langItem = new LanguageItem(couple, sourceLanguage, targetLanguage);
        return langItem;
    }

    */

    public ArrayList<LanguageItem> getAllLanguages() {
        ArrayList<LanguageItem> items = new ArrayList<>();
        Cursor cursor = db.query(ViCabContract.LanguageEntry.TABLE_NAME, new String[] { ViCabContract.LanguageEntry.COLUMN_NAME_ENTRY_ID, ViCabContract.LanguageEntry.COLUMN_NAME_COUPLE,
                ViCabContract.LanguageEntry.COLUMN_NAME_SOURCE_LANGUAGE, ViCabContract.LanguageEntry.COLUMN_NAME_TARGET_LANGUAGE }, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String dbID = cursor.getString(ViCabContract.LanguageEntry.COLUMN_COUPLE_INDEX);
                String sourceLanguage = cursor.getString(ViCabContract.LanguageEntry.COLUMN_SOURCE_LANGUAGE_INDEX);
                String targetLanguage= cursor.getString(ViCabContract.LanguageEntry.COLUMN_TARGET_LANGUAGE_INDEX);
                Log.d("MyDB","String: uri " + sourceLanguage+", title: " + targetLanguage + " id = " + dbID);

                items.add(new LanguageItem(dbID, sourceLanguage, targetLanguage));

            } while (cursor.moveToNext());
        }
        return items;
    }



    public ArrayList<ListItem> getAllListsForChosenLanguage() {
        ArrayList<ListItem> items = new ArrayList<>();
        String selection = ViCabContract.ListEntry.COLUMN_NAME_HAS_LANGUAGE + " = ?";
        String[] args = {ViCabContract.CHOSEN_LANGUAGE_MIX};
        Cursor cursor = db.query(ViCabContract.ListEntry.TABLE_NAME, new String[] { ViCabContract.ListEntry.COLUMN_NAME_ENTRY_ID,
                ViCabContract.ListEntry.COLUMN_NAME_NAME, ViCabContract.ListEntry.COLUMN_NAME_HAS_LANGUAGE }, selection, args, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(ViCabContract.ListEntry.COLUMN_NAME_INDEX);
                String hasLanguage= cursor.getString(ViCabContract.ListEntry.COLUMN_HAS_LANGUAGE_INDEX);

                items.add(new ListItem(name, hasLanguage));

            } while (cursor.moveToNext());
        }
        return items;
    }


    public ArrayList<VocItem> getAllVocabForLanguage() {
        ArrayList<VocItem> items = new ArrayList<>();
        String selection = ViCabContract.VocabEntry.COLUMN_NAME_HAS_LANGUAGE + " = ?";
        String[] args = {ViCabContract.CHOSEN_LANGUAGE_MIX};
        Cursor cursor = db.query(ViCabContract.VocabEntry.TABLE_NAME, new String[] { ViCabContract.VocabEntry.COLUMN_NAME_ENTRY_ID,
                ViCabContract.VocabEntry.COLUMN_NAME_SOURCE_VOCAB, ViCabContract.VocabEntry.COLUMN_NAME_TARGET_VOCAB,
                ViCabContract.VocabEntry.COLUMN_NAME_FOTO_LINK, ViCabContract.VocabEntry.COLUMN_NAME_SOUND_LINK,
                ViCabContract.VocabEntry.COLUMN_NAME_WORD_TYPE, ViCabContract.VocabEntry.COLUMN_NAME_IMPORTANCE,
                ViCabContract.VocabEntry.COLUMN_NAME_HAS_LIST, ViCabContract.VocabEntry.COLUMN_NAME_HAS_LANGUAGE}, selection, args, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String sourceVocab = cursor.getString(ViCabContract.VocabEntry.COLUMN_SOURCE_VOCAB_INDEX);
                String targetVocab= cursor.getString(ViCabContract.VocabEntry.COLUMN_TARGET_VOCAB_INDEX);
                String foto= cursor.getString(ViCabContract.VocabEntry.COLUMN_FOTO_LINK_INDEX);
                String sound= cursor.getString(ViCabContract.VocabEntry.COLUMN_SOUND_LINK_INDEX);
                String wordType= cursor.getString(ViCabContract.VocabEntry.COLUMN_WORD_TYPE_INDEX);
                int importance= cursor.getInt(ViCabContract.VocabEntry.COLUMN_IMPORTANCE_INDEX);
                String hasList= cursor.getString(ViCabContract.VocabEntry.COLUMN_HAS_LIST_INDEX);
                String hasLanguage = cursor.getString(ViCabContract.VocabEntry.COLUMN_HAS_LANGUAGE_INDEX);

                items.add(new VocItem(sourceVocab, targetVocab, foto, sound, wordType, importance, hasList, hasLanguage));

            } while (cursor.moveToNext());
        }
        return items;
    }

    /*
    public ArrayList<LanguageItem> getVocabItem() {
        ArrayList<LanguageItem> items = new ArrayList<LanguageItem>();
        Cursor cursor = db.query(ViCabContract.LanguageEntry.TABLE_NAME, new String[] { ViCabContract.LanguageEntry.COLUMN_NAME_ENTRY_ID,
                ViCabContract.LanguageEntry.COLUMN_NAME_SOURCE_LANGUAGE, ViCabContract.LanguageEntry.COLUMN_NAME_TARGET_LANGUAGE }, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String sourceLanguage = cursor.getString(ViCabContract.LanguageEntry.COLUMN_SOURCE_LANGUAGE_INDEX);
                String targetLanguage= cursor.getString(ViCabContract.LanguageEntry.COLUMN_TARGET_LANGUAGE_INDEX);
                Log.d("String: uri " + sourceLanguage,", title: " + targetLanguage);

                items.add(new LanguageItem(sourceLanguage, targetLanguage));

            } while (cursor.moveToNext());
        }
        return items;
    }
    */

    public int renameList(ListItem item, String newName) {

        String whereClause = ViCabContract.ListEntry.COLUMN_NAME_NAME + " = '" + item.getName() + "'";
        ContentValues cv = new ContentValues();
        cv.put(ViCabContract.ListEntry.COLUMN_NAME_NAME, newName);
        return db.update(ViCabContract.ListEntry.TABLE_NAME,cv, whereClause, null);
    }

    // Update VocabItem
    public long updateVocabSource(String vocabItemID, String sourceVoc){
        String whereClause = ViCabContract.VocabEntry.COLUMN_NAME_ENTRY_ID + " = '" + vocabItemID + "'";
        ContentValues cv = new ContentValues();
        cv.put(ViCabContract.VocabEntry.COLUMN_NAME_SOURCE_VOCAB, sourceVoc);
        return db.update(ViCabContract.VocabEntry.TABLE_NAME, cv, whereClause, null);
    }

    public long updateVocabTarget(String vocabItemID, String targetVoc){
        String whereClause = ViCabContract.VocabEntry.COLUMN_NAME_ENTRY_ID + " = '" + vocabItemID + "'";
        ContentValues cv = new ContentValues();
        cv.put(ViCabContract.VocabEntry.COLUMN_NAME_TARGET_VOCAB, targetVoc);
        return db.update(ViCabContract.VocabEntry.TABLE_NAME, cv, whereClause, null);
    }

    public long updateVocabFoto(String vocabItemID, String fotoLink){
        String whereClause = ViCabContract.VocabEntry.COLUMN_NAME_ENTRY_ID + " = '" + vocabItemID + "'";
        ContentValues cv = new ContentValues();
        cv.put(ViCabContract.VocabEntry.COLUMN_NAME_FOTO_LINK, fotoLink);
        return db.update(ViCabContract.VocabEntry.TABLE_NAME, cv, whereClause, null);
    }

    public long updateVocabSound(String vocabItemID, String soundLink){
        String whereClause = ViCabContract.VocabEntry.COLUMN_NAME_ENTRY_ID + " = '" + vocabItemID + "'";
        ContentValues cv = new ContentValues();
        cv.put(ViCabContract.VocabEntry.COLUMN_NAME_SOUND_LINK, soundLink);
        return db.update(ViCabContract.VocabEntry.TABLE_NAME, cv, whereClause, null);
    }

    public long updateVocabWordType(String vocabItemID, String wordType){
        String whereClause = ViCabContract.VocabEntry.COLUMN_NAME_ENTRY_ID + " = '" + vocabItemID + "'";
        ContentValues cv = new ContentValues();
        cv.put(ViCabContract.VocabEntry.COLUMN_NAME_WORD_TYPE, wordType);
        return db.update(ViCabContract.VocabEntry.TABLE_NAME, cv, whereClause, null);
    }

    public long updateVocabImportance(String vocabItemID, String importance){
        String whereClause = ViCabContract.VocabEntry.COLUMN_NAME_ENTRY_ID + " = '" + vocabItemID + "'";
        ContentValues cv = new ContentValues();
        cv.put(ViCabContract.VocabEntry.COLUMN_NAME_IMPORTANCE, importance);
        return db.update(ViCabContract.VocabEntry.TABLE_NAME, cv, whereClause, null);
    }

    // Update Name
    public long updateListName(String listItemID,String name){
        String whereClause = ViCabContract.ListEntry.COLUMN_NAME_ENTRY_ID + " = '" + listItemID + "'";
        ContentValues cv = new ContentValues();
        cv.put(ViCabContract.ListEntry.COLUMN_NAME_NAME, name);
        return db.update(ViCabContract.ListEntry.TABLE_NAME, cv, whereClause, null);
    }


    private class ToDoDBOpenHelper extends SQLiteOpenHelper {
        private static final String TABLE_LANGUAGE_CREATE = "create table "
                + ViCabContract.LanguageEntry.TABLE_NAME + "(" + ViCabContract.LanguageEntry.COLUMN_NAME_ENTRY_ID +
                " integer primary key autoincrement , " + ViCabContract.LanguageEntry.COLUMN_NAME_COUPLE +
                " text not null, "+ ViCabContract.LanguageEntry.COLUMN_NAME_SOURCE_LANGUAGE +
                " text not null, " + ViCabContract.LanguageEntry.COLUMN_NAME_TARGET_LANGUAGE +
                " text not null)";

        private static final String TABLE_LIST_CREATE = "create table " + ViCabContract.ListEntry.TABLE_NAME +
                "(" + ViCabContract.ListEntry.COLUMN_NAME_ENTRY_ID +  " integer primary key autoincrement , " + ViCabContract.ListEntry.COLUMN_NAME_NAME +
                " text not null , " + ViCabContract.ListEntry.COLUMN_NAME_HAS_LANGUAGE + " integer not null)";


        private static final String TABLE_VOCAB_CREATE = "create table " + ViCabContract.VocabEntry.TABLE_NAME + "(" +
                ViCabContract.VocabEntry.COLUMN_NAME_ENTRY_ID + " integer primary key autoincrement , " + ViCabContract.VocabEntry.COLUMN_NAME_SOURCE_VOCAB +
                " text not null , " + ViCabContract.VocabEntry.COLUMN_NAME_TARGET_VOCAB + " text not null , " + ViCabContract.VocabEntry.COLUMN_NAME_FOTO_LINK +
                " text not null, " + ViCabContract.VocabEntry.COLUMN_NAME_SOUND_LINK + " text not null, " + ViCabContract.VocabEntry.COLUMN_NAME_WORD_TYPE +
                " text not null, " + ViCabContract.VocabEntry.COLUMN_NAME_IMPORTANCE + " integer not null, " + ViCabContract.VocabEntry.COLUMN_NAME_HAS_LIST +
                " text not null, " + ViCabContract.VocabEntry.COLUMN_NAME_HAS_LANGUAGE + " text not null)";



        public ToDoDBOpenHelper(Context c, String dbname,
                                SQLiteDatabase.CursorFactory factory, int version) {
            super(c, dbname, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_LANGUAGE_CREATE);
            db.execSQL(TABLE_LIST_CREATE);
            db.execSQL(TABLE_VOCAB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
