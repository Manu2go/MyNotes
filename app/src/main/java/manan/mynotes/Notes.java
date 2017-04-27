package manan.mynotes;

//import android.app.Fragment;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.internal.app.ToolbarActionBar;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import manan.mynotes.URLs;

public class Notes extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener, listentry.listener, listupdate.listener1 {
    ListView mylist;
    SharedPreferences shared;
    View v;


    String nme;
    int uID;
    ArrayList<note> note_info;

    FloatingActionButton fab;
    TextView username;
    note_sqlite not_sql;
    SQLiteDatabase db;
    String[] NTE;
    String[] CONENT;
    int c = 0;
    int maxnotes = 50;
    View temp;
    AdapterView p;
    Toolbar second_toolbar;
    TextView totalnotes, notesmade;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        note_info= new ArrayList<note>();
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        not_sql=new note_sqlite(getApplicationContext());
        shared = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = shared.edit();
        nme = shared.getString("username", "");
        String ID = shared.getString("uID", "");
        uID=Integer.parseInt(ID);
        second_toolbar=(Toolbar)findViewById(R.id.second_toolbar);
        totalnotes = (TextView) findViewById(R.id.totalnotes);
        notesmade = (TextView) findViewById(R.id.notesmade);

        c=fetchnotes();
        totalnotes.setText("You can make " + maxnotes + " Notes");
        if (c == 1) {
            notesmade.setText("You made " + c + " Note");
        } else {
            notesmade.setText("You made " + c + " Notes");
        }


        NavigationView NavView = (NavigationView) findViewById(R.id.nav_view);
        View testview = NavView.inflateHeaderView(R.layout.nav_header_notes);//add a nav_header_home1.xml file as the header of nav_view
        username = (TextView) testview.findViewById(R.id.uname);


        username.setText(nme + "");


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (c < maxnotes) {
                    listentry entry = new listentry();
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    second_toolbar.setVisibility(View.GONE);
                    transaction.add(R.id.noteupdate, entry, "note");
                    transaction.commit();
                } else {
                    Toast.makeText(Notes.this, "Sorry!! Only" + " " + maxnotes + " " + "can be added!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        v = findViewById(R.id.content_notes);
        mylist = (ListView) v.findViewById(R.id.listview);
        mylist.setAdapter(new adapter(this));
        mylist.setOnItemClickListener(this);

    }

    private int fetchnotes() {

        SQLiteDatabase db1=not_sql.getWritableDatabase();
        Cursor c = db1.query(true, not_sql.TB_name, null, null, null, null, null, null, null);
        while (c.moveToNext()) {
            String note = c.getString(c.getColumnIndex("note"));
            String content = c.getString(c.getColumnIndex("content"));
            int id = c.getInt(c.getColumnIndex("id"));
            note u=new note(note,content,id);
           note_info.add(u);
        }
        c.moveToFirst();
        int r =c.getCount();
        c.close();
        return r;
    }



    public void note(String note, String content) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransac = manager.beginTransaction();
        Fragment f = manager.findFragmentByTag("note");
        if (f != null) {
            fragmentTransac.remove(f);
            fragmentTransac.commit();
        }
        fab.setEnabled(true);
        second_toolbar.setVisibility(View.VISIBLE);
        add_note(note, content);
    }

    public void update(String note, String content, String i) {


        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransac = manager.beginTransaction();
        Fragment f = manager.findFragmentByTag("noteupdate");
        if (f != null) {
            fragmentTransac.remove(f);
            fragmentTransac.commit();
        }
        fab.setEnabled(true);
        second_toolbar.setVisibility(View.VISIBLE);
        add_note(note, content);
        int p = Integer.parseInt(i);
        update_note(note, content,note_info.get(p).id,p );

    }

    public void delete(String note, String content, String position) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransac = manager.beginTransaction();
        Fragment f = manager.findFragmentByTag("noteupdate");
        if (f != null) {
            fragmentTransac.remove(f);
            fragmentTransac.commit();
        }
        fab.setEnabled(true);
        second_toolbar.setVisibility(View.VISIBLE);
        add_note(note, content);
        int p = Integer.parseInt(position);
        delete_note(note_info.get(p).id,p);
    }

    public void back(View v) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransac = manager.beginTransaction();
        Fragment f = manager.findFragmentByTag("note");
        second_toolbar.setVisibility(View.VISIBLE);
        fab.setEnabled(true);
        if (f != null) {
            fragmentTransac.remove(f);
            fragmentTransac.commit();
        } else {
            f = manager.findFragmentByTag("noteupdate");
            if (f != null) {
                fragmentTransac.remove(f);
                fragmentTransac.commit();
            }
        }

    }



   /* private void register(String username, String note, String content, int c, int y) {

        final String urlSuffix;

        if (y == 0) {
            urlSuffix = "http://" + manan.mynotes.URL.URL_ADD_NOTES + "?note=" + note + "&content=" + content + "&username=" + username + "&count=" + c;
        } else if (y == 1) {
            urlSuffix = "http://" + manan.mynotes.URL.URL_CHANGE_NOTES + "?note=" + note + "&content=" + content + "&username=" + username + "&position=" + c;
        } else {
            urlSuffix = "http://" + manan.mynotes.URL.URL_DELETE_NOTES + "?note=" + note + "&content=" + content + "&username=" + username + "&position=" + c;
        }

        class RegisterUser extends AsyncTask<String, Void, String> {

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Notes.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equals("1")) {
                    Toast.makeText(getApplicationContext(), "Operation Successful", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

                }

            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(urlSuffix);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String result;

                    result = bufferedReader.readLine();

                    return result;
                } catch (Exception e) {
                    return null;
                }
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(urlSuffix);
    }*/
    private void add_note(final String note,final String content) {

        final String urlSuffix;


        urlSuffix = URLs.URL_ADD_NOTES + "?note=" + note + "&content=" + content + "&uID=" + uID;


        class RegisterUser extends AsyncTask<String, Void, String> {

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Notes.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Log.i("r",s);
                try {
                    JSONObject res = new JSONObject(s);
                    String error=res.getString("error");
                    if(error.equals("false")){
                        Toast.makeText(getApplicationContext(), "Operation Successful", Toast.LENGTH_LONG).show();
                        int id=res.getInt("id");
                        note_info.add(new note(note,content,id));
                        db=not_sql.getWritableDatabase();
                        ContentValues ci=new ContentValues();
                        ci.put("note",note);
                        ci.put("content",content);
                        ci.put("id",id);
                        db.insert(not_sql.TB_name,null,ci);
                        c++;
                        if (c == 1) {
                            notesmade.setText("You made " + c + " Note");
                        } else {
                            notesmade.setText("You made " + c + " Notes");
                        }
                        mylist.setAdapter(new adapter(Notes.this));
                    }
                    else if(error.equals("true")){
                        Toast.makeText(Notes.this, s, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Notes.this, "Operation Unsuccessful!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(urlSuffix);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestProperty("Cookie", "__test=9fab03b5dbd43c100a8b83a7a2bbc88c; expires=Fri, 01-01-38 05:25:55 GMT; path=/");
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String result;

                    result = bufferedReader.readLine();

                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Operation Unsuccessful";
                }
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(urlSuffix);
    }

    private void update_note(final String note,final String content,final int ID,final int position) {

        final String urlSuffix;


        urlSuffix = URLs.URL_CHANGE_NOTES +"?note=" + note + "&content=" + content + "&id=" +ID;;


        class RegisterUser extends AsyncTask<String, Void, String> {

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Notes.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Log.i("u",s);
                try {
                    JSONObject res = new JSONObject(s);
                    String error=res.getString("error");
                    if(error.equals("false")){
                        Toast.makeText(getApplicationContext(), "Operation Successful", Toast.LENGTH_LONG).show();
                        Log.i("up",position+"");
                        note_info.set(position,new note(note,content,ID));
                        mylist.setAdapter(new adapter(Notes.this));
                    }
                    else if(error.equals("true")){
                        Toast.makeText(Notes.this, s, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Notes.this, "Operation Unsuccessful!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(urlSuffix);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestProperty("Cookie", "__test=9fab03b5dbd43c100a8b83a7a2bbc88c; expires=Fri, 01-01-38 05:25:55 GMT; path=/");
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String result;

                    result = bufferedReader.readLine();

                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Operation Unsuccessful";
                }
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(urlSuffix);
    }

    private void delete_note(final int ID,final int position) {

        final String urlSuffix;


        urlSuffix = URLs.URL_DELETE_NOTES + "?id=" + ID;


        class RegisterUser extends AsyncTask<String, Void, String> {

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Notes.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Log.i("t",s);
                try {
                    JSONObject res = new JSONObject(s);
                    String error=res.getString("error");
                    if(error.equals("false")){
                        Toast.makeText(getApplicationContext(), "Operation Successful", Toast.LENGTH_LONG).show();
                        Log.i("dp",position+"");
                        note_info.remove(position);
                        c--;
                        mylist.setAdapter(new adapter(Notes.this));
                    }
                    else if(error.equals("true")) {
                        Toast.makeText(Notes.this, s, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Notes.this, "Operation Unsuccessful!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(urlSuffix);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestProperty("Cookie", "__test=9fab03b5dbd43c100a8b83a7a2bbc88c; expires=Fri, 01-01-38 05:25:55 GMT; path=/");
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String result;

                    result = bufferedReader.readLine();

                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Operation Unsuccessful";
                }
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(urlSuffix);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        p = parent;
        temp = view;
        fab.setEnabled(false);
        second_toolbar.setVisibility(View.GONE);
        listupdate entry = new listupdate();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Bundle data = new Bundle();
        data.putString("note", note_info.get(position).note);
        data.putString("content",note_info.get(position).content);
        data.putString("index", String.valueOf(position));
        entry.setArguments(data);
        transaction.add(R.id.noteupdate, entry, "noteupdate");
        transaction.commit();

    }

    class item {

        String note;
        String content;

        item(String note, String content) {

            this.note = note;
            this.content = content;
        }
    }

    class ViewHolder {

        TextView note;
        TextView content;

        ViewHolder(View v) {

            note = (TextView) v.findViewById(R.id.nt);
            content = (TextView) v.findViewById(R.id.cnt);
        }
    }

    class adapter extends BaseAdapter {
        ArrayList<item> list;
        Context context;

        adapter(Context context) {
            this.context = context;
            list = new ArrayList<item>();
            for (int i = 0; i < note_info.size(); i++) {
                item temp = new item(note_info.get(i).note,note_info.get(i).content);
                list.add(temp);
            }
        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            View row = convertView;
            ViewHolder holder = null;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.single_item, parent, false);
                holder = new ViewHolder(row);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();

            }

            item temp = list.get(i);
            holder.note.setText(temp.note);
            holder.content.setText(temp.content);
            return row;
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {

        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent i;

        if (id == R.id.logout) {
            db=not_sql.getWritableDatabase();
            db.delete(not_sql.TB_name,null,null);
            db.close();
            finish();
            SharedPreferences user = getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit=user.edit();
            edit.putString("login_status","0");
            edit.apply();
            i = new Intent(Notes.this, login.class);
            startActivity(i);

        }
        if (id == R.id.home) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction fragmentTransac = manager.beginTransaction();
            Fragment f = manager.findFragmentByTag("note");
            fab.setEnabled(true);
            second_toolbar.setVisibility(View.VISIBLE);
            if (f != null) {
                fragmentTransac.remove(f);
                fragmentTransac.commit();
            } else {
                f = manager.findFragmentByTag("noteupdate");
                if (f != null) {
                    fragmentTransac.remove(f);
                    fragmentTransac.commit();
                }
            }

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
