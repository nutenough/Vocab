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

    public long addVocab(String sourceVocab, String targetVocab, String fotoLink, String soundLink, String wordType, String importance, String hasList, int known, int asked, int time) {
        ContentValues vocItem = new ContentValues();

        vocItem.put(ViCabContract.VocabEntry.COLUMN_NAME_SOURCE_VOCAB, sourceVocab);
        vocItem.put(ViCabContract.VocabEntry.COLUMN_NAME_TARGET_VOCAB, targetVocab);
        vocItem.put(ViCabContract.VocabEntry.COLUMN_NAME_FOTO_LINK, fotoLink);
        vocItem.put(ViCabContract.VocabEntry.COLUMN_NAME_SOUND_LINK, soundLink);
        vocItem.put(ViCabContract.VocabEntry.COLUMN_NAME_WORD_TYPE, wordType);
        vocItem.put(ViCabContract.VocabEntry.COLUMN_NAME_IMPORTANCE, importance);
        vocItem.put(ViCabContract.VocabEntry.COLUMN_NAME_HAS_LIST, hasList);
        vocItem.put(ViCabContract.VocabEntry.COLUMN_NAME_HAS_LANGUAGE, ViCabContract.CHOSEN_LANGUAGE_MIX);
        vocItem.put(ViCabContract.VocabEntry.COLUMN_NAME_KNOWN, known);
        vocItem.put(ViCabContract.VocabEntry.COLUMN_NAME_ASKED, asked);
        vocItem.put(ViCabContract.VocabEntry.COLUMN_NAME_TIME_STAMP, time);

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


    public ArrayList<LanguageItem> getAllLanguages() {
        ArrayList<LanguageItem> items = new ArrayList<>();
        Cursor cursor = db.query(ViCabContract.LanguageEntry.TABLE_NAME, new String[]{ViCabContract.LanguageEntry.COLUMN_NAME_ENTRY_ID, ViCabContract.LanguageEntry.COLUMN_NAME_COUPLE,
                ViCabContract.LanguageEntry.COLUMN_NAME_SOURCE_LANGUAGE, ViCabContract.LanguageEntry.COLUMN_NAME_TARGET_LANGUAGE}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String dbID = cursor.getString(ViCabContract.LanguageEntry.COLUMN_COUPLE_INDEX);
                String sourceLanguage = cursor.getString(ViCabContract.LanguageEntry.COLUMN_SOURCE_LANGUAGE_INDEX);
                String targetLanguage = cursor.getString(ViCabContract.LanguageEntry.COLUMN_TARGET_LANGUAGE_INDEX);

                items.add(new LanguageItem(dbID, sourceLanguage, targetLanguage));

            } while (cursor.moveToNext());
        }
        return items;
    }


    public ArrayList<ListItem> getAllListsForChosenLanguage() {
        ArrayList<ListItem> items = new ArrayList<>();
        String selection = ViCabContract.ListEntry.COLUMN_NAME_HAS_LANGUAGE + " = ?";
        String[] args = {ViCabContract.CHOSEN_LANGUAGE_MIX};
        Cursor cursor = db.query(ViCabContract.ListEntry.TABLE_NAME, new String[]{ViCabContract.ListEntry.COLUMN_NAME_ENTRY_ID,
                ViCabContract.ListEntry.COLUMN_NAME_NAME, ViCabContract.ListEntry.COLUMN_NAME_HAS_LANGUAGE}, selection, args, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(ViCabContract.ListEntry.COLUMN_NAME_INDEX);
                String hasLanguage = cursor.getString(ViCabContract.ListEntry.COLUMN_HAS_LANGUAGE_INDEX);

                items.add(new ListItem(name, hasLanguage));

            } while (cursor.moveToNext());
        }
        return items;
    }

    public ArrayList<VocItem> getAllVocabForLanguage() {
        ArrayList<VocItem> items = new ArrayList<>();
        String selection = ViCabContract.VocabEntry.COLUMN_NAME_HAS_LANGUAGE + " = ?";
        String[] args = {ViCabContract.CHOSEN_LANGUAGE_MIX};
        Cursor cursor = db.query(ViCabContract.VocabEntry.TABLE_NAME, getProjection(), selection, args, null, null, null);

        if (cursor.moveToFirst()) {
            do {

                items.add(getItem(cursor));

            } while (cursor.moveToNext());
        }
        return items;
    }

    public ArrayList<VocItem> getAllVocabForList(String listName) {
        ArrayList<VocItem> items = new ArrayList<>();
        String selection = ViCabContract.VocabEntry.COLUMN_NAME_HAS_LANGUAGE + " = ? AND " + ViCabContract.VocabEntry.COLUMN_NAME_HAS_LIST + "= ?";
        String[] args = {ViCabContract.CHOSEN_LANGUAGE_MIX, listName};
        String[] projection = getProjection();
        Cursor cursor = db.query(ViCabContract.VocabEntry.TABLE_NAME, projection, selection, args, null, null, null);


        if (cursor.moveToFirst()) {
            do {

                items.add(getItem(cursor));

            } while (cursor.moveToNext());
        }
        return items;
    }

    public ArrayList<VocItem> getRecentVocab() {
        ArrayList<VocItem> items = new ArrayList<>();
        String selection = ViCabContract.VocabEntry.COLUMN_NAME_HAS_LANGUAGE + " = ?";
        String[] args = {ViCabContract.CHOSEN_LANGUAGE_MIX};
        Cursor cursor = db.query(ViCabContract.VocabEntry.TABLE_NAME, getProjection(), selection, args, null, null, ViCabContract.VocabEntry.COLUMN_NAME_TIME_STAMP + " desc", "20");
        if (cursor.moveToFirst()) {
            do {

                items.add(getItem(cursor));

            } while (cursor.moveToNext());
        }
        return items;
    }

    private String[] getProjection() {
        return new String[]{ViCabContract.VocabEntry.COLUMN_NAME_ENTRY_ID,
                ViCabContract.VocabEntry.COLUMN_NAME_SOURCE_VOCAB, ViCabContract.VocabEntry.COLUMN_NAME_TARGET_VOCAB,
                ViCabContract.VocabEntry.COLUMN_NAME_FOTO_LINK, ViCabContract.VocabEntry.COLUMN_NAME_SOUND_LINK,
                ViCabContract.VocabEntry.COLUMN_NAME_WORD_TYPE, ViCabContract.VocabEntry.COLUMN_NAME_IMPORTANCE,
                ViCabContract.VocabEntry.COLUMN_NAME_HAS_LIST, ViCabContract.VocabEntry.COLUMN_NAME_HAS_LANGUAGE,
                ViCabContract.VocabEntry.COLUMN_NAME_KNOWN, ViCabContract.VocabEntry.COLUMN_NAME_ASKED,
                ViCabContract.VocabEntry.COLUMN_NAME_TIME_STAMP};
    }

    private VocItem getItem(Cursor cursor) {
        String sourceVocab = cursor.getString(ViCabContract.VocabEntry.COLUMN_SOURCE_VOCAB_INDEX);
        String targetVocab = cursor.getString(ViCabContract.VocabEntry.COLUMN_TARGET_VOCAB_INDEX);
        String foto = cursor.getString(ViCabContract.VocabEntry.COLUMN_FOTO_LINK_INDEX);
        String sound = cursor.getString(ViCabContract.VocabEntry.COLUMN_SOUND_LINK_INDEX);
        String wordType = cursor.getString(ViCabContract.VocabEntry.COLUMN_WORD_TYPE_INDEX);
        String importance = cursor.getString(ViCabContract.VocabEntry.COLUMN_IMPORTANCE_INDEX);
        String hasList = cursor.getString(ViCabContract.VocabEntry.COLUMN_HAS_LIST_INDEX);
        String hasLanguage = cursor.getString(ViCabContract.VocabEntry.COLUMN_HAS_LANGUAGE_INDEX);
        int known = cursor.getInt(ViCabContract.VocabEntry.COLUMN_NAME_KNOWN_INDEX);
        int asked = cursor.getInt(ViCabContract.VocabEntry.COLUMN_NAME_ASKED_INDEX);
        int time = cursor.getInt(ViCabContract.VocabEntry.COLUMN_NAME_TIME_STAMP_INDEX);

        return new VocItem(sourceVocab, targetVocab, foto, sound, wordType, importance, hasList, hasLanguage, known, asked, time);
    }


    // update List
    public int renameList(ListItem item, String newName) {

        String whereClause = ViCabContract.ListEntry.COLUMN_NAME_NAME + " = '" + item.getName() + "'";
        ContentValues cv = new ContentValues();
        cv.put(ViCabContract.ListEntry.COLUMN_NAME_NAME, newName);
        return db.update(ViCabContract.ListEntry.TABLE_NAME, cv, whereClause, null);
    }

    // Update VocabItem

    public long updateAskedAndKnown(VocItem item) {
        String whereClause = ViCabContract.VocabEntry.COLUMN_NAME_SOURCE_VOCAB + " = '" + item.getSourceVocab() + "' AND "
                + ViCabContract.VocabEntry.COLUMN_NAME_TARGET_VOCAB + " = '" + item.getTargetVocab() + "'";
        ContentValues cv = new ContentValues();
        cv.put(ViCabContract.VocabEntry.COLUMN_NAME_KNOWN, item.getKnown());
        cv.put(ViCabContract.VocabEntry.COLUMN_NAME_ASKED, item.getAsked());

        return db.update(ViCabContract.VocabEntry.TABLE_NAME, cv, whereClause, null);
    }

    public long updateVocabItem(String oldSourceVoc, String oldTargetVoc, String sourceVoc, String targetVoc, String hasList, String wordType, String importance) {

        String whereClause = ViCabContract.VocabEntry.COLUMN_NAME_SOURCE_VOCAB + " = '" + oldSourceVoc + "' AND "+ ViCabContract.VocabEntry.COLUMN_NAME_TARGET_VOCAB + " = '" + oldTargetVoc+"'";
        ContentValues cv = new ContentValues();
        cv.put(ViCabContract.VocabEntry.COLUMN_NAME_SOURCE_VOCAB, sourceVoc);
        cv.put(ViCabContract.VocabEntry.COLUMN_NAME_TARGET_VOCAB, targetVoc);
        cv.put(ViCabContract.VocabEntry.COLUMN_NAME_HAS_LIST, hasList);
        cv.put(ViCabContract.VocabEntry.COLUMN_NAME_WORD_TYPE, wordType);
        cv.put(ViCabContract.VocabEntry.COLUMN_NAME_IMPORTANCE, importance);

        return db.update(ViCabContract.VocabEntry.TABLE_NAME, cv, whereClause, null);
    }



    private class ToDoDBOpenHelper extends SQLiteOpenHelper {
        private static final String TABLE_LANGUAGE_CREATE = "create table "
                + ViCabContract.LanguageEntry.TABLE_NAME + "(" + ViCabContract.LanguageEntry.COLUMN_NAME_ENTRY_ID +
                " integer primary key autoincrement , " + ViCabContract.LanguageEntry.COLUMN_NAME_COUPLE +
                " text not null, " + ViCabContract.LanguageEntry.COLUMN_NAME_SOURCE_LANGUAGE +
                " text not null, " + ViCabContract.LanguageEntry.COLUMN_NAME_TARGET_LANGUAGE +
                " text not null)";

        private static final String TABLE_LIST_CREATE = "create table " + ViCabContract.ListEntry.TABLE_NAME +
                "(" + ViCabContract.ListEntry.COLUMN_NAME_ENTRY_ID + " integer primary key autoincrement , " + ViCabContract.ListEntry.COLUMN_NAME_NAME +
                " text not null , " + ViCabContract.ListEntry.COLUMN_NAME_HAS_LANGUAGE + " text not null)";


        private static final String TABLE_VOCAB_CREATE = "create table " + ViCabContract.VocabEntry.TABLE_NAME + "(" +
                ViCabContract.VocabEntry.COLUMN_NAME_ENTRY_ID + " integer primary key autoincrement , " + ViCabContract.VocabEntry.COLUMN_NAME_SOURCE_VOCAB +
                " text not null , " + ViCabContract.VocabEntry.COLUMN_NAME_TARGET_VOCAB + " text not null , " + ViCabContract.VocabEntry.COLUMN_NAME_FOTO_LINK +
                " text not null, " + ViCabContract.VocabEntry.COLUMN_NAME_SOUND_LINK + " text not null, " + ViCabContract.VocabEntry.COLUMN_NAME_WORD_TYPE +
                " text not null, " + ViCabContract.VocabEntry.COLUMN_NAME_IMPORTANCE + " text not null, " + ViCabContract.VocabEntry.COLUMN_NAME_HAS_LIST +
                " text not null, " + ViCabContract.VocabEntry.COLUMN_NAME_HAS_LANGUAGE + " text not null, " + ViCabContract.VocabEntry.COLUMN_NAME_KNOWN +
                " integer not null, " + ViCabContract.VocabEntry.COLUMN_NAME_ASKED + " integer not null, " + ViCabContract.VocabEntry.COLUMN_NAME_TIME_STAMP + " integer not null);";

        private static final String INSERT_TEST_LANGUAGE = "INSERT INTO `language`(`couple`,`source_language`,`target_language`) VALUES ('Deutsch - Englisch','Deutsch','Englisch')";

        private static final String INSERT_TEST_VOCAB_1 = "INSERT INTO `vocab`(`source_vocab`,`target_vocab`,`foto_link`,`sound_link`,`word_type`,`importance`,`has_list`,`has_language`,`known`,`asked`,`time_stamp`) VALUES ('blau','blue','','','Adjektiv',0,'Farben','Deutsch - Englisch',0,0,10)";
        private static final String INSERT_TEST_VOCAB_2 = "INSERT INTO `vocab`(`source_vocab`,`target_vocab`,`foto_link`,`sound_link`,`word_type`,`importance`,`has_list`,`has_language`,`known`,`asked`,`time_stamp`) VALUES ('rot','red','','','Adjektiv',0,'Farben','Deutsch - Englisch',0,0,10)";
        private static final String INSERT_TEST_VOCAB_3 = "INSERT INTO `vocab`(`source_vocab`,`target_vocab`,`foto_link`,`sound_link`,`word_type`,`importance`,`has_list`,`has_language`,`known`,`asked`,`time_stamp`) VALUES ('weiß','white','','','Adjektiv',0,'Farben','Deutsch - Englisch',0,0,10)";
        private static final String INSERT_TEST_VOCAB_4 = "INSERT INTO `vocab`(`source_vocab`,`target_vocab`,`foto_link`,`sound_link`,`word_type`,`importance`,`has_list`,`has_language`,`known`,`asked`,`time_stamp`) VALUES ('grün','green','','','Adjektiv',0,'Farben','Deutsch - Englisch',0,0,10)";
        private static final String INSERT_TEST_VOCAB_5 = "INSERT INTO `vocab`(`source_vocab`,`target_vocab`,`foto_link`,`sound_link`,`word_type`,`importance`,`has_list`,`has_language`,`known`,`asked`,`time_stamp`) VALUES ('gelb','yellow','','','Adjektiv',0,'Farben','Deutsch - Englisch',0,0,10)";

        private static final String INSERT_TEST_VOCAB_6 = "INSERT INTO `vocab`(`source_vocab`,`target_vocab`,`foto_link`,`sound_link`,`word_type`,`importance`,`has_list`,`has_language`,`known`,`asked`,`time_stamp`) VALUES ('null','zero','','','Sonstiges',0,'Zahlen','Deutsch - Englisch',0,0,10)";
        private static final String INSERT_TEST_VOCAB_7 = "INSERT INTO `vocab`(`source_vocab`,`target_vocab`,`foto_link`,`sound_link`,`word_type`,`importance`,`has_list`,`has_language`,`known`,`asked`,`time_stamp`) VALUES ('eins','one','','','Sonstiges',0,'Zahlen','Deutsch - Englisch',0,0,10)";
        private static final String INSERT_TEST_VOCAB_8 = "INSERT INTO `vocab`(`source_vocab`,`target_vocab`,`foto_link`,`sound_link`,`word_type`,`importance`,`has_list`,`has_language`,`known`,`asked`,`time_stamp`) VALUES ('zwei','two','','','Sonstiges',0,'Zahlen','Deutsch - Englisch',0,0,10)";
        private static final String INSERT_TEST_VOCAB_9 = "INSERT INTO `vocab`(`source_vocab`,`target_vocab`,`foto_link`,`sound_link`,`word_type`,`importance`,`has_list`,`has_language`,`known`,`asked`,`time_stamp`) VALUES ('drei','three','','','Sonstiges',0,'Zahlen','Deutsch - Englisch',0,0,10)";
        private static final String INSERT_TEST_VOCAB_10 = "INSERT INTO `vocab`(`source_vocab`,`target_vocab`,`foto_link`,`sound_link`,`word_type`,`importance`,`has_list`,`has_language`,`known`,`asked`,`time_stamp`) VALUES ('vier','four','','','Sonstiges',0,'Zahlen','Deutsch - Englisch',0,0,10)";

        private static final String INSERT_TEST_VOCAB_11 = "INSERT INTO `vocab`(`source_vocab`,`target_vocab`,`foto_link`,`sound_link`,`word_type`,`importance`,`has_list`,`has_language`,`known`,`asked`,`time_stamp`) VALUES ('Schule','school','','','Substantiv',0,'Schule','Deutsch - Englisch',0,0,10)";
        private static final String INSERT_TEST_VOCAB_12 = "INSERT INTO `vocab`(`source_vocab`,`target_vocab`,`foto_link`,`sound_link`,`word_type`,`importance`,`has_list`,`has_language`,`known`,`asked`,`time_stamp`) VALUES ('Hausaufgabe','homework','','','Substantiv',0,'Schule','Deutsch - Englisch',0,0,10)";
        private static final String INSERT_TEST_VOCAB_13 = "INSERT INTO `vocab`(`source_vocab`,`target_vocab`,`foto_link`,`sound_link`,`word_type`,`importance`,`has_list`,`has_language`,`known`,`asked`,`time_stamp`) VALUES ('lernen','to learn','','','Verb',0,'Schule','Deutsch - Englisch',0,0,10)";
        private static final String INSERT_TEST_VOCAB_14 = "INSERT INTO `vocab`(`source_vocab`,`target_vocab`,`foto_link`,`sound_link`,`word_type`,`importance`,`has_list`,`has_language`,`known`,`asked`,`time_stamp`) VALUES ('Prüfung','exam','','','Substantiv',0,'Schule','Deutsch - Englisch',0,0,10)";
        private static final String INSERT_TEST_VOCAB_15 = "INSERT INTO `vocab`(`source_vocab`,`target_vocab`,`foto_link`,`sound_link`,`word_type`,`importance`,`has_list`,`has_language`,`known`,`asked`,`time_stamp`) VALUES ('Lehrer','teacher','','','Substantiv',0,'Schule','Deutsch - Englisch',0,0,10)";

        private static final String INSERT_TEST_VOCAB_16 = "INSERT INTO `vocab`(`source_vocab`,`target_vocab`,`foto_link`,`sound_link`,`word_type`,`importance`,`has_list`,`has_language`,`known`,`asked`,`time_stamp`) VALUES ('groß','tall','','','Adjektiv',0,'Person','Deutsch - Englisch',0,0,10)";
        private static final String INSERT_TEST_VOCAB_17 = "INSERT INTO `vocab`(`source_vocab`,`target_vocab`,`foto_link`,`sound_link`,`word_type`,`importance`,`has_list`,`has_language`,`known`,`asked`,`time_stamp`) VALUES ('klein','small','','','Adjektiv',0,'Person','Deutsch - Englisch',0,0,10)";
        private static final String INSERT_TEST_VOCAB_18 = "INSERT INTO `vocab`(`source_vocab`,`target_vocab`,`foto_link`,`sound_link`,`word_type`,`importance`,`has_list`,`has_language`,`known`,`asked`,`time_stamp`) VALUES ('grüßen','to greet','','','Verb',0,'Person','Deutsch - Englisch',0,0,10)";
        private static final String INSERT_TEST_VOCAB_19 = "INSERT INTO `vocab`(`source_vocab`,`target_vocab`,`foto_link`,`sound_link`,`word_type`,`importance`,`has_list`,`has_language`,`known`,`asked`,`time_stamp`) VALUES ('Sommersprossen','freckles','','','Substantiv',0,'Person','Deutsch - Englisch',0,0,10)";
        private static final String INSERT_TEST_VOCAB_20 = "INSERT INTO `vocab`(`source_vocab`,`target_vocab`,`foto_link`,`sound_link`,`word_type`,`importance`,`has_list`,`has_language`,`known`,`asked`,`time_stamp`) VALUES ('sprechen','to speak','','','Verb',0,'Person','Deutsch - Englisch',0,0,10)";

        private static final String INSERT_TEST_VOCAB_21 = "INSERT INTO `vocab`(`source_vocab`,`target_vocab`,`foto_link`,`sound_link`,`word_type`,`importance`,`has_list`,`has_language`,`known`,`asked`,`time_stamp`) VALUES ('essen','to eat','','','Verb',0,'Essen','Deutsch - Englisch',0,0,10)";
        private static final String INSERT_TEST_VOCAB_22 = "INSERT INTO `vocab`(`source_vocab`,`target_vocab`,`foto_link`,`sound_link`,`word_type`,`importance`,`has_list`,`has_language`,`known`,`asked`,`time_stamp`) VALUES ('Essen','meal','','','Substantiv',0,'Essen','Deutsch - Englisch',0,0,10)";
        private static final String INSERT_TEST_VOCAB_23 = "INSERT INTO `vocab`(`source_vocab`,`target_vocab`,`foto_link`,`sound_link`,`word_type`,`importance`,`has_list`,`has_language`,`known`,`asked`,`time_stamp`) VALUES ('kochen','to cook','','','Verb',0,'Essen','Deutsch - Englisch',0,0,10)";
        private static final String INSERT_TEST_VOCAB_24 = "INSERT INTO `vocab`(`source_vocab`,`target_vocab`,`foto_link`,`sound_link`,`word_type`,`importance`,`has_list`,`has_language`,`known`,`asked`,`time_stamp`) VALUES ('Kaffee','coffee','','','Substantiv',0,'Essen','Deutsch - Englisch',0,0,10)";
        private static final String INSERT_TEST_VOCAB_25 = "INSERT INTO `vocab`(`source_vocab`,`target_vocab`,`foto_link`,`sound_link`,`word_type`,`importance`,`has_list`,`has_language`,`known`,`asked`,`time_stamp`) VALUES ('Brötchen','bun','','','Substantiv',0,'Essen','Deutsch - Englisch',0,0,10)";

        private static final String INSERT_TEST_LIST_1 = "INSERT INTO `list`(`name`,`has_language`) VALUES ('Farben', 'Deutsch - Englisch')";
        private static final String INSERT_TEST_LIST_2 = "INSERT INTO `list`(`name`,`has_language`) VALUES ('Zahlen', 'Deutsch - Englisch')";
        private static final String INSERT_TEST_LIST_3 = "INSERT INTO `list`(`name`,`has_language`) VALUES ('Schule', 'Deutsch - Englisch')";
        private static final String INSERT_TEST_LIST_4 = "INSERT INTO `list`(`name`,`has_language`) VALUES ('Person', 'Deutsch - Englisch')";
        private static final String INSERT_TEST_LIST_5 = "INSERT INTO `list`(`name`,`has_language`) VALUES ('Essen', 'Deutsch - Englisch')";

        public ToDoDBOpenHelper(Context c, String dbname,
                                SQLiteDatabase.CursorFactory factory, int version) {
            super(c, dbname, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // Create tables for language couple, vocabs and lists
            db.execSQL(TABLE_LANGUAGE_CREATE);
            db.execSQL(TABLE_LIST_CREATE);
            db.execSQL(TABLE_VOCAB_CREATE);
            // Insert language couple
            db.execSQL(INSERT_TEST_LANGUAGE);
            // Insert vocabs
            db.execSQL(INSERT_TEST_VOCAB_1);
            db.execSQL(INSERT_TEST_VOCAB_2);
            db.execSQL(INSERT_TEST_VOCAB_3);
            db.execSQL(INSERT_TEST_VOCAB_4);
            db.execSQL(INSERT_TEST_VOCAB_5);
            db.execSQL(INSERT_TEST_VOCAB_6);
            db.execSQL(INSERT_TEST_VOCAB_7);
            db.execSQL(INSERT_TEST_VOCAB_8);
            db.execSQL(INSERT_TEST_VOCAB_9);
            db.execSQL(INSERT_TEST_VOCAB_10);
            db.execSQL(INSERT_TEST_VOCAB_11);
            db.execSQL(INSERT_TEST_VOCAB_12);
            db.execSQL(INSERT_TEST_VOCAB_13);
            db.execSQL(INSERT_TEST_VOCAB_14);
            db.execSQL(INSERT_TEST_VOCAB_15);
            db.execSQL(INSERT_TEST_VOCAB_16);
            db.execSQL(INSERT_TEST_VOCAB_17);
            db.execSQL(INSERT_TEST_VOCAB_18);
            db.execSQL(INSERT_TEST_VOCAB_19);
            db.execSQL(INSERT_TEST_VOCAB_20);
            db.execSQL(INSERT_TEST_VOCAB_21);
            db.execSQL(INSERT_TEST_VOCAB_22);
            db.execSQL(INSERT_TEST_VOCAB_23);
            db.execSQL(INSERT_TEST_VOCAB_24);
            db.execSQL(INSERT_TEST_VOCAB_25);
            // Insert lists
            db.execSQL(INSERT_TEST_LIST_1);
            db.execSQL(INSERT_TEST_LIST_2);
            db.execSQL(INSERT_TEST_LIST_3);
            db.execSQL(INSERT_TEST_LIST_4);
            db.execSQL(INSERT_TEST_LIST_5);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
