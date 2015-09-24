package app.mueller.schiller.weber.com.vicab.Database;

import android.provider.BaseColumns;

public final class ViCabContract {
    public static final String DATA_BASE_NAME = "vicab.db";
    public static final int DATABASE_VERSION = 1;
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public static String CHOSEN_LANGUAGE_MIX = "";
    public static String CHOSEN_LANGUAGE_SOURCE = "";
    public static String CHOSEN_LANGUAGE_TARGET = "";

    public ViCabContract() {}

    /* Inner class that defines the table contents */
    public static abstract class LanguageEntry implements BaseColumns {
        public static final String TABLE_NAME = "language";
        public static final String COLUMN_NAME_ENTRY_ID = "language_id";
        public static final String COLUMN_NAME_COUPLE = "couple";
        public static final String COLUMN_NAME_SOURCE_LANGUAGE = "source_language";
        public static final String COLUMN_NAME_TARGET_LANGUAGE = "target_language";

        public static final int COLUMN_ID_INDEX = 0;
        public static final int COLUMN_COUPLE_INDEX = 1;
        public static final int COLUMN_SOURCE_LANGUAGE_INDEX = 2;
        public static final int COLUMN_TARGET_LANGUAGE_INDEX = 3;
    }

    public static abstract class ListEntry implements BaseColumns {
        public static final String TABLE_NAME = "list";
        public static final String COLUMN_NAME_ENTRY_ID = "list_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_HAS_LANGUAGE = "has_language";

        public static final int COLUMN_ID_INDEX = 0;
        public static final int COLUMN_NAME_INDEX = 1;
        public static final int COLUMN_HAS_LANGUAGE_INDEX = 2;
    }

    public static abstract class VocabEntry implements BaseColumns {
        public static final String TABLE_NAME = "vocab";
        public static final String COLUMN_NAME_ENTRY_ID = "vocab_id";
        public static final String COLUMN_NAME_SOURCE_VOCAB = "source_vocab";
        public static final String COLUMN_NAME_TARGET_VOCAB = "target_vocab";
        public static final String COLUMN_NAME_FOTO_LINK = "foto_link";
        public static final String COLUMN_NAME_SOUND_LINK = "sound_link";
        public static final String COLUMN_NAME_WORD_TYPE = "word_type";
        public static final String COLUMN_NAME_IMPORTANCE = "importance";
        public static final String COLUMN_NAME_HAS_LIST = "has_list";
        public static final String COLUMN_NAME_HAS_LANGUAGE = "has_language";

        public static final int COLUMN_ID_INDEX = 0;
        public static final int COLUMN_SOURCE_VOCAB_INDEX = 1;
        public static final int COLUMN_TARGET_VOCAB_INDEX = 2;
        public static final int COLUMN_FOTO_LINK_INDEX = 3;
        public static final int COLUMN_SOUND_LINK_INDEX = 4;
        public static final int COLUMN_WORD_TYPE_INDEX = 5;
        public static final int COLUMN_IMPORTANCE_INDEX = 6;
        public static final int COLUMN_HAS_LIST_INDEX = 7;
        public static final int COLUMN_HAS_LANGUAGE_INDEX = 8;
    }
}

