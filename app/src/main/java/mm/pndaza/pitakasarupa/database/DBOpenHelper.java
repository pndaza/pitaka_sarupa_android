package mm.pndaza.pitakasarupa.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import mm.pndaza.pitakasarupa.model.Bookmark;
import mm.pndaza.pitakasarupa.model.Detail;
import mm.pndaza.pitakasarupa.model.Recent;
import mm.pndaza.pitakasarupa.model.Word;
import mm.pndaza.pitakasarupa.utils.MDetect;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static DBOpenHelper sInstance;
    private static final String DATABASE_NAME = "pitaka_sarupa.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_SARUPA = "sarupa";
    private static final String COLUMN_WORDLIST_ZG = "wordlist_zg";
    private static final String COLUMN_WORDLIST_UNI = "wordlist_uni";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_REFERENCE = "reference";


    public static synchronized DBOpenHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.

        if (sInstance == null) {
            sInstance = new DBOpenHelper(context);
        }
        return sInstance;
    }

    private DBOpenHelper(Context context) {
        super(context, context.getFilesDir() + "/databases/" + DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public ArrayList<Word> getAllWordList(boolean uni_encoding) {

        String wordListColumn;
        if (uni_encoding) {
            wordListColumn = COLUMN_WORDLIST_UNI;
        } else {
            wordListColumn = COLUMN_WORDLIST_ZG;
        }

        String query = "SELECT _id," + wordListColumn + " FROM sarupa";
        Cursor cursor = this.getReadableDatabase().rawQuery(query, null);

        ArrayList<Word> list = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                list = new ArrayList<>();
                int _id;
                String word;
                do {
                    _id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                    word = cursor.getString(cursor.getColumnIndexOrThrow(wordListColumn));
                    list.add(new Word(_id, word));
                } while (cursor.moveToNext());
            }
        }
        assert cursor != null;
        cursor.close();
        return list;
    }

    private String getWordById(int _id, boolean isUnicode) {
        String wordColumn = "wordlist_zg";
        if (isUnicode) {
            wordColumn = "wordlist_uni";
        }
        String sql = "SELECT " + wordColumn + " FROM sarupa WHERE _id = " + _id;
        Cursor cursor = getReadableDatabase().rawQuery(sql, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow(wordColumn));
            }
            cursor.close();
        }

        return null;
    }

    public Detail getDetailById(int _id) {
        String word;
        String content;
        String reference;
        String[] FIELDS = {COLUMN_WORDLIST_ZG, COLUMN_CONTENT, COLUMN_REFERENCE};
        String WHERE = "_id = " + _id;
        Cursor cursor = getReadableDatabase().query(TABLE_SARUPA, FIELDS, WHERE,
                null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                word = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WORDLIST_ZG));
                content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT));
                reference = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REFERENCE));
                return new Detail(word, content, reference);
            }
            cursor.close();
        }
        return null;
    }

    public ArrayList<Bookmark> getAllBookmarks(boolean isUnicode) {
        ArrayList<Bookmark> bookmarks = new ArrayList<>();
        int word_id;
        String word;
        Cursor cursor = getReadableDatabase().
                rawQuery("SELECT id, word_id FROM bookmark ORDER BY id DESC", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    word_id = cursor.getInt(cursor.getColumnIndexOrThrow("word_id"));
                    word = getWordById(word_id, isUnicode);
                    bookmarks.add(new Bookmark(word_id, word));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return bookmarks;
    }

    public boolean isBookmarkExist(int word_id) {
        Cursor cursor = this.getReadableDatabase().rawQuery
                ("SELECT word_id FROM bookmark Where word_id = " + word_id, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public void addToBookmark(int word_id) {
        getWritableDatabase()
                .execSQL("INSERT INTO bookmark (word_id) VALUES (?)", new Object[]{word_id});
    }

    public void removeBookmarkById(int word_id) {
        getWritableDatabase()
                .execSQL("DELETE FROM bookmark WHERE word_id = ?", new Object[]{word_id});
    }

    public ArrayList<Recent> getAllRecentWord() {
        ArrayList<Recent> recentList = new ArrayList<>();
        Cursor cursor = getReadableDatabase()
                .rawQuery(" SELECT id, word_id FROM recent ORDER BY id DESC", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int word_id = cursor.getInt(cursor.getColumnIndexOrThrow("word_id"));
                    String word = getWordById(word_id, MDetect.isUnicode());
                    recentList.add(new Recent(word_id, word));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return recentList;
    }

    public boolean isRecentExist(int word_id) {
        Cursor cursor = this.getReadableDatabase()
                .rawQuery("SELECT word_id FROM recent Where word_id = " + word_id, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public void addToRecent(int word_id) {
        getWritableDatabase()
                .execSQL("INSERT INTO recent (word_id) VALUES (?)", new Object[]{word_id});
    }

    public void removeAllRecent() {
        getWritableDatabase().execSQL("DELETE FROM recent");
    }

}
