
package edu.itchii.plat2.pf;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.Map.Entry;
import java.util.Set;

class EncDB {

     private String TABLE_NAME = "ENCUESTA";
     int DATABASE_VERSION = 1;
    private String _ID = "id";
    private String _CHK_VALUES = "checkbox_value";

    public String getTABLE_NAME(){
        return TABLE_NAME;
    }

    public int getDATABASE_VERSION (){
        return DATABASE_VERSION;
    }
    public String getID()       {       return _ID;         }
    public String getScore()    {       return _CHK_VALUES;     }

    public String getDatabaseCreateQuery()
    {
        final String DATABASE_CREATE =
                "create table IF NOT EXISTS " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY, "
                        + _CHK_VALUES + " TEXT NOT NULL)";

        return DATABASE_CREATE;
    }


}

class dbOperation {

    static String DB_TABLE ;
    static int DB_VERSION = 1;
    static String[] DATABASE_CREATE;
    private Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public dbOperation(Context ctx,String[] query)
    {
        this.context = ctx;
        DATABASE_CREATE = query;
        DBHelper = new DatabaseHelper(context);
    }
    public dbOperation(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    public dbOperation(String tablename)
    {
        DB_TABLE = tablename;
        DBHelper = new DatabaseHelper(context);
    }

    public dbOperation open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }
    public void close()
    {
        DBHelper.close();
    }
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, "ENCUESTA.db", null, DB_VERSION);
        }
        public void onCreate(SQLiteDatabase db)
        {
            try
            {
                for (String s  : DATABASE_CREATE)
                {
                    db.execSQL(s);
                }
            }
            catch (Exception e) {
                System.out.println("Error creating items Per screen in the constructor" + e.getMessage());
            }
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion)
        {
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(db);
        }
    }

    public long insertTableData(String tablename, ContentValues values)  throws SQLException
    {
        DB_TABLE = tablename;
        ContentValues initialValues2 = new ContentValues();
        Set<Entry<String, Object>> s =  values.valueSet();
        String new_val = "";
        for (Entry<String, Object> entry : s) {
            new_val = values.getAsString(entry.getKey());
            initialValues2.put(entry.getKey(), new_val);
        }
        return db.insert(DB_TABLE, null, initialValues2);
    }
    public boolean deleteTableData(String tablename,String condition)  throws SQLException
    {
        DB_TABLE = tablename;
        return db.delete(DB_TABLE, condition, null) > 0;
    }
    public Cursor getAllTableData(String tablename, String[] fields)  throws SQLException
    {
        DB_TABLE = tablename;
        return db.query(DB_TABLE, fields,null, null, null, null, null);
    }
    public Cursor getTableRow(String tablename,String[] dbFields, String condition,String order,String limit) throws SQLException
    {
        DB_TABLE = tablename;
        Cursor mCursor =    db.query(false, DB_TABLE, dbFields,condition,
                null,null,null, order, limit);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    public boolean updateTable(String tablename,ContentValues args,String condition)
    {
        DB_TABLE = tablename;
        return db.update(DB_TABLE, args,condition , null) > 0;

    }
    public int lastInsertedID(String tablename)
    {
        int retVar=0;
        Cursor mCursor = db.rawQuery("select max(id) from "+tablename, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
            retVar =Integer.parseInt(mCursor.getString(0));
        }
        mCursor.close();
        mCursor.deactivate();
        return retVar ;
    }

}

