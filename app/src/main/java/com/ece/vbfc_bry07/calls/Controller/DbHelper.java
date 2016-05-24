package com.ece.vbfc_bry07.calls.Controller;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DbHelper extends SQLiteOpenHelper {
    public static String CREATED_AT = "created_at",
            UPDATED_AT = "updated_at",
            DELETED_AT = "deleted_at",
            AI_ID = "_id";

    private static String DB_PATH;
    private static String DB_NAME = "ECE_calls";
    private static SQLiteDatabase myDataBase;
    private Context myContext;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;

        if (android.os.Build.VERSION.SDK_INT >= 17)
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        else
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();

        if (!dbExist) {
            this.getReadableDatabase();

            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("" + e);
            }
        }
    }

    private boolean checkDataBase() {
        File databasePath = myContext.getDatabasePath(DB_NAME);
        return databasePath.exists();
    }

    private void copyDataBase() throws IOException {
        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String path = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(path);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {
        String path = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();

        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
