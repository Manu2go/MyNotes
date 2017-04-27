package manan.mynotes;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by root on 4/3/17.
 */
public class note_sqlite extends SQLiteOpenHelper {

    public static String DB_name="Notes";
    public static String TB_name="Notes";
    public static int version=1;
    public static Context context;
    public static String create_query="CREATE TABLE "+TB_name+" (id int ,note varchar(5000),content varchar(5000));";
    public static String drop="Drop table if exists "+TB_name;
    public note_sqlite(Context c){
        super(c,DB_name,null,version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL(create_query);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(drop);
            onCreate(db);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
