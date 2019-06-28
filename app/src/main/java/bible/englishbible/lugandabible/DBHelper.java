package bible.englishbible.lugandabible;

/**
 * Created by user on 5/15/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DBHelper
        extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "dailyverseLugandaEnglish28May19.sqlite";
    private static final int DATABASE_VERSION = 1;
    private static final String DB_PATH_SUFFIX = "/databases/";
    static Context ctx;
    Bundle bundle = new Bundle();
    private static final String DATE = "date";
    private static final String ID = "id";
    private static final String NOTES_TABLE = "notes";
    private static final String NOTES_TITLE = "notes_title";
    private static final String NOTES_MESSAGE = "notes_message";

    public DBHelper(Context paramContext) {
        super(paramContext, DATABASE_NAME, null, 1);
        ctx = paramContext;
    }

    private static String getDatabasePath() {
        return ctx.getApplicationInfo().dataDir + "/databases/" + DATABASE_NAME;
    }

    public void CopyDataBaseFromAsset()
            throws IOException {
        InputStream localInputStream = ctx.getAssets().open(DATABASE_NAME);
        String str = getDatabasePath();
        File localFile = new File(ctx.getApplicationInfo().dataDir + "/databases/");
        if (!localFile.exists()) {
            localFile.mkdir();
        }
        FileOutputStream localFileOutputStream = new FileOutputStream(str);
        byte[] arrayOfByte = new byte['Ѐ'];
        for (; ; ) {
            int i = localInputStream.read(arrayOfByte);
            if (i <= 0) {
                break;
            }
            localFileOutputStream.write(arrayOfByte, 0, i);
        }
        localFileOutputStream.flush();
        localFileOutputStream.close();
        localInputStream.close();
    }


    public ArrayList getVerse(int doy) {
        File localFile = ctx.getDatabasePath(DATABASE_NAME);
        try {
            if (!localFile.exists()) {
                CopyDataBaseFromAsset();
            }
        } catch (Exception e) {
            System.out.println("Error in saveBookmark");
        }
        int day = doy;
        ArrayList<String> dateAndVerse = new ArrayList<String>();
        String verse = "Amen";
        Cursor localCursor = getReadableDatabase().rawQuery("SELECT verse,date  FROM verses where id =" + doy, null);
        while (localCursor.moveToNext()) {
            dateAndVerse.add(localCursor.getString(0));
            dateAndVerse.add(localCursor.getString(1));
            System.out.println(dateAndVerse.add(localCursor.getString(0) + " id and Date "
                    + dateAndVerse.add(localCursor.getString(1))));
        }
        return dateAndVerse;
    }

    public boolean updateVersesDate(String doy, String today) {
        // openDataBase();
        try {//dailyverseLugandaEnglish1
            File localFile = ctx.getDatabasePath(DATABASE_NAME);
            try {
                if (!localFile.exists()) {
                    CopyDataBaseFromAsset();
                }
            } catch (Exception e) {
                System.out.println("Error in saveBookmark");
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put("date", today);
            getWritableDatabase().update("verses", contentValues, "ID = ?", new String[]{doy});
            getWritableDatabase().close();
            return true;
        } catch (Exception e) {
            System.out.println("Error in updateVersesDate");
        }
        return true;
    }


    public void onCreate(SQLiteDatabase paramSQLiteDatabase) {
    }

    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2) {
    }

    public SQLiteDatabase openDataBase() {

        File localFile = ctx.getDatabasePath(DATABASE_NAME);

        try {
            if (!localFile.exists()) {
                CopyDataBaseFromAsset();
            }
            //the below block commented to addres Favorite table refresh on each application cloing time
            // CopyDataBaseFromAsset();
            return SQLiteDatabase.openDatabase(localFile.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
        } catch (IOException localIOException) {
            throw new RuntimeException("Error creating source database", localIOException);
        }
    }

    public String getPraises(String id) {
        File localFile = ctx.getDatabasePath(DATABASE_NAME);
        try {
            if (!localFile.exists()) {
                CopyDataBaseFromAsset();
            }
        } catch (Exception e) {
            System.out.println("Error in saveBookmark");
        }
        int number2 = Integer.parseInt(id) - 99;
        String from = String.valueOf(number2);
        StringBuffer sf = new StringBuffer();
        Cursor localCursor = getReadableDatabase().rawQuery("SELECT id,praise,reference FROM praisesenglishkjvniv where id between " + number2 + " and " + id, null);
        while (localCursor.moveToNext()) {
            sf.append("\n" + localCursor.getString(0) + ".  " + localCursor.getString(1) + "  ( " + localCursor.getString(2) + " )\n");
        }
        return sf.toString();
    }

    public String[] getSongDetails() {
        File localFile = ctx.getDatabasePath(DATABASE_NAME);
        try {
            if (!localFile.exists()) {
                CopyDataBaseFromAsset();
            }
        } catch (Exception e) {
            System.out.println("Error in saveBookmark");
        }
        ArrayList localArrayList = new ArrayList();
        Cursor localCursor = getReadableDatabase().rawQuery("SELECT TITLE FROM ENGLISHSONGS ORDER BY title", null);
        int i = 1;
        while (localCursor.moveToNext()) {
            localArrayList.add(i + "." + localCursor.getString(0));
            i++;
        }
        return (String[]) localArrayList.toArray(new String[localArrayList.size()]);
    }

    public String getLyrics(String title) {
        File localFile = ctx.getDatabasePath(DATABASE_NAME);
        try {
            if (!localFile.exists()) {
                CopyDataBaseFromAsset();
            }
        } catch (Exception e) {
            System.out.println("Error in saveBookmark");
        }
        Cursor localCursor = getReadableDatabase().rawQuery("Select  title,lyrics from ENGLISHSONGS where title ='" + title + "'", null);
        int i = 0;
        String str = new String();
        while (localCursor.moveToNext()) {
            str = localCursor.getString(0) + "\n" + localCursor.getString(1);
        }
        return str;

    }

    public ArrayList searchSong(String word) {
        File localFile = ctx.getDatabasePath(DATABASE_NAME);
        try {
            if (!localFile.exists()) {
                CopyDataBaseFromAsset();
            }
        } catch (Exception e) {
            System.out.println("Error in saveBookmark");
        }
        ArrayList localArrayList = new ArrayList();
        Cursor localCursor = getReadableDatabase().rawQuery("SELECT title FROM ENGLISHSONGS where  " +
                "title like '%" + word + "%'", null);
        while (localCursor.moveToNext()) {
            localArrayList.add(localCursor.getString(0));
        }
        return localArrayList;
    }


    public void saveBookmark(String word) {
        File localFile = ctx.getDatabasePath(DATABASE_NAME);
        try {
            if (!localFile.exists()) {
                CopyDataBaseFromAsset();
            }
        } catch (Exception e) {
            System.out.println("Error in saveBookmark");
        }
        Log.i("myTag", "This is my message word" + word);
        ArrayList localArrayList = new ArrayList();
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("verse", word);
        localContentValues.put("date", getCurrentDate());
        try {
            getWritableDatabase().insertOrThrow("bookmark", null, localContentValues);
            getWritableDatabase().close();
        } catch (Exception exception) {
            Log.i("myTag", "saveBookmark Exception #  " + exception);
        }
    }

    public String[] getAllBookmarks() {
        File localFile = ctx.getDatabasePath(DATABASE_NAME);
        try {
            if (!localFile.exists()) {
                CopyDataBaseFromAsset();
            }
        } catch (Exception e) {
            System.out.println("Error in saveBookmark");
        }
        ArrayList localArrayList = new ArrayList();
        Cursor localCursor = getReadableDatabase().rawQuery("SELECT VERSE,DATE FROM BOOKMARK order by id desc", null);
        while (localCursor.moveToNext()) {
            localArrayList.add(localCursor.getString(1) + "# \n" + localCursor.getString(0));
        }
        return (String[]) localArrayList.toArray(new String[localArrayList.size()]);
    }

    public void deleteBookmark(String selectedBookmark) {
        File localFile = ctx.getDatabasePath(DATABASE_NAME);
        String whereClause = "verse=?";
        try {
            if (!localFile.exists()) {
                CopyDataBaseFromAsset();
            }
        } catch (Exception e) {
            System.out.println("Error in deleteBookmark");
        }
        try {
            getWritableDatabase().delete("bookmark", whereClause, new String[]{selectedBookmark});
            getWritableDatabase().close();
        } catch (Exception exception) {
            Log.i("myTag", "saveBookmark Exception #  " + exception);
        }
    }

    public void saveNote(String title, String message) {
        File localFile = ctx.getDatabasePath(DATABASE_NAME);
        try {
            if (!localFile.exists()) {
                CopyDataBaseFromAsset();
            }
        } catch (Exception e) {
            System.out.println("Error in saveNote");
        }
        ArrayList localArrayList = new ArrayList();
        ContentValues localContentValues = new ContentValues();
        localContentValues.put(NOTES_TITLE, title);
        localContentValues.put(NOTES_MESSAGE, message);
        localContentValues.put(DATE, getCurrentDate());
        try {
            getWritableDatabase().insertOrThrow(NOTES_TABLE, null, localContentValues);
            getWritableDatabase().close();
        } catch (Exception exception) {
            //  Log.i("myTag", "saveNote Exception #  " + exception);
        }
    }

    public void updateNote(String id, String title, String message) {
        File localFile = ctx.getDatabasePath(DATABASE_NAME);
        try {
            if (!localFile.exists()) {
                CopyDataBaseFromAsset();
            }
        } catch (Exception e) {
            System.out.println("Error in saveNote");
        }
        ArrayList localArrayList = new ArrayList();
        ContentValues localContentValues = new ContentValues();
        //localContentValues.put(ID, id);
        localContentValues.put(NOTES_TITLE, title);
        localContentValues.put(NOTES_MESSAGE, message);
        localContentValues.put(DATE, getCurrentDate());
        String sql = "UPDATE " + NOTES_TABLE + " SET " + "notes_title = " + title + " , notes_message = "
                + message + " WHERE id=" + id;
        try {
            //getWritableDatabase().update(NOTES_TABLE, null, localContentValues);
            //  getWritableDatabase().insertOrThrow(NOTES_TABLE, null, localContentValues);
            SQLiteDatabase db = getWritableDatabase();
            db.update(NOTES_TABLE, localContentValues, "id=" + id, null);
            //getWritableDatabase().execSQL(sql);
            getWritableDatabase().close();
        } catch (Exception exception) {
            Log.i("myTag", "updateNote #  " + exception);
        }
    }

    public String[] getAllNotes() {
        File localFile = ctx.getDatabasePath(DATABASE_NAME);
        try {
            if (!localFile.exists()) {
                CopyDataBaseFromAsset();
            }
        } catch (Exception e) {
            System.out.println("Error in getAllNotes");
        }
        ArrayList localArrayList = new ArrayList();
        Cursor localCursor = getReadableDatabase().rawQuery("SELECT id,date,notes_title FROM notes order by id desc", null);
        while (localCursor.moveToNext()) {
            localArrayList.add(localCursor.getString(0) + ". " + localCursor.getString(1) + "# \n" + localCursor.getString(2));
            // Log.i("getAllNotes", "getAllNotes #  " +localCursor.getString(0));
        }
        return (String[]) localArrayList.toArray(new String[localArrayList.size()]);
    }

    public String getNotesById(String id) {
        File localFile = ctx.getDatabasePath(DATABASE_NAME);
        ArrayList localArrayList = new ArrayList();
        String message = "";
        try {
            if (!localFile.exists()) {
                CopyDataBaseFromAsset();
            }
        } catch (Exception e) {
            System.out.println("Error in getNotesById");
        }
        try {
            Cursor localCursor = getReadableDatabase().rawQuery("Select notes_title,notes_message from notes where id ='" + id + "'", null);
            while (localCursor.moveToNext()) {
                message = "Title # " + localCursor.getString(0) + "\n Message \n " + localCursor.getString(1);
            }
        } catch (Exception e) {
            Log.i("myTag", "getNotesById Exception#  " + e);
        }
        return message;
    }

    public String getTitleMessageById(String id) {
        File localFile = ctx.getDatabasePath(DATABASE_NAME);
        ArrayList localArrayList = new ArrayList();
        String messageTitle = "";
        try {
            if (!localFile.exists()) {
                CopyDataBaseFromAsset();
            }
        } catch (Exception e) {
            System.out.println("Error in getNotesById");
        }
        try {
            Cursor localCursor = getReadableDatabase().rawQuery("Select notes_title,notes_message from notes where id ='" + id + "'", null);
            while (localCursor.moveToNext()) {
                messageTitle = localCursor.getString(0) + "#" + localCursor.getString(1);
            }
        } catch (Exception e) {
            Log.i("myTag", "getTitleMessageById Exception#  " + e);
        }
        return messageTitle;
    }


    public void deleteNote(String id) {
        File localFile = ctx.getDatabasePath(DATABASE_NAME);
        String whereClause = "id=?";
        try {
            if (!localFile.exists()) {
                CopyDataBaseFromAsset();
            }
        } catch (Exception e) {
            System.out.println("Error in deleteNote");
        }
        try {
            getWritableDatabase().delete(NOTES_TABLE, whereClause, new String[]{id});
            getWritableDatabase().close();
        } catch (Exception exception) {
            Log.i("myTag", "deleteNote Exception #  " + exception);
        }
    }

    public String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MMM-dd-yyyy HH:mm:ss");
        return (formatter.format(date));
    }
}

