package com.mycheckins.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="checkin.db";
    public static final String TABLE_NAME="checkin_table";
    public static final String COL_ID="_id";
    public static final String COL_TITLE="TITLE";
    public static final String COL_PLACE="PLACE";
    public static final String COL_DETAILS="DETAILS";
    public static final String COL_DATE="DATE";
    public static final String COL_LATLONG="latlong";

    public static final String CREATE_TABLE_SQL =
            "CREATE TABLE "
                    + TABLE_NAME
                    + "("
                    + COL_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COL_TITLE
                    + " VARCHAR ,"
                    + COL_DATE
                    + " VARCHAR,"
                    + COL_PLACE
                    + " VARCHAR,"
                    + COL_DETAILS
                    + " VARCHAR,"
                    + COL_LATLONG
                    + " VARCHAR"
                    + ")";


    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("table query ==> ",""+CREATE_TABLE_SQL);
        db.execSQL(CREATE_TABLE_SQL);


      //  db.execSQL("create table "+TABLE_NAME+"(ID INTEGER  , NAME TEXT , SURNAME TEXT , MARKS INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CREATE_TABLE_SQL);
        onCreate(db);

    }

    public boolean insertData(String title, String date, String details,String place,String latlong){

        Log.e("insert database",title+"  "+date+"  "+details+"  "+place+"  "+latlong);

        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TITLE,title);
        contentValues.put(COL_DATE,date);
        contentValues.put(COL_DETAILS,details);
        contentValues.put(COL_PLACE,place);
        contentValues.put(COL_LATLONG,latlong);

        long result= db.insert(TABLE_NAME,null,contentValues);
        Log.e("Result Database==>>", String.valueOf(result));

        if(result == -1)
            return false;
            else
                return true;

    }


    public Cursor getAllData(){
        SQLiteDatabase db =this.getWritableDatabase();
        Cursor res= db.rawQuery("select * from "+TABLE_NAME,null);
        if(res!=null)
            res.moveToFirst();
        return res;

    }

    public Cursor getSelectedData(int id){
        SQLiteDatabase db =this.getWritableDatabase();
        //Cursor cursorc = db.rawQuery("select * from list where ID = ?", new String[] {id+""});
     //   Cursor res= db.rawQuery("select * from  student_table where ID=?",new String[] {id+""});
        Cursor res = db.rawQuery("SELECT * FROM student_table WHERE " + COL_ID + " = " + id, null);
        return res;

    }


    public boolean updateData(String id , String title, String date, String details,String place,String latlong){

        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID,id);
        contentValues.put(COL_TITLE,title);
        contentValues.put(COL_DATE,date);
        contentValues.put(COL_DETAILS,details);
        contentValues.put(COL_PLACE,place);
        contentValues.put(COL_LATLONG,latlong);
        long result= db.update(TABLE_NAME,contentValues,"_id=?" ,new String[]{id});

        if(result == -1)
            return false;
        else
            return true;

    }


    public Integer deleteData (String id){

        SQLiteDatabase db = this.getWritableDatabase();
         return db.delete(TABLE_NAME,"_id= ?",new String[]{id});
    }
}
