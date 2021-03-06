package manan.mynotes;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class login extends AppCompatActivity implements View.OnClickListener {

    private EditText Usrnam;
    private EditText Psswrd;

    private Button signin;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Psswrd = (EditText) findViewById(R.id.psswrd);
        Usrnam = (EditText) findViewById(R.id.usernamelogin);
        signin = (Button) findViewById(R.id.signin_button);


        signin.setOnClickListener(this);
    }

  /*  public void register(View v)
    {
        Intent i=new Intent(login.this,Register.class);
        startActivity(i);
    }*/

    public void signup(View v) {
        Intent i = new Intent(login.this, Register.class);
        startActivity(i);
    }

    public void onClick(View v) {
        if (v == signin) {
            loginuser();

        }
    }

    private void loginuser() {

        String name = Usrnam.getText().toString();
        String passwrd = Psswrd.getText().toString();


        log(name, passwrd);
    }

    private void log(final String username, String password) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            manan.mynotes.login1 ruc = new manan.mynotes.login1();

            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(login.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.i("e",s);
                loading.dismiss();
                try {
                    JSONObject res = new JSONObject(s);

                    String error=res.getString("error");
                    if(error.equals("false")){
                        SharedPreferences user = getSharedPreferences("user", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = user.edit();
                        //Toast.makeText(login.this, s, Toast.LENGTH_LONG).show();
                        edit.putString("username", res.getString("username"));
                        edit.putString("uID", res.getString("uID"));
                        edit.putString("status", "1");
                        edit.apply();
                        JSONArray thread = res.getJSONArray("notes");
                        note_sqlite sql=new note_sqlite(getApplicationContext());
                        SQLiteDatabase db=sql.getWritableDatabase();
                        ContentValues c=new ContentValues();
                        for (int i = 0; i < thread.length(); i++) {
                            JSONObject obj = thread.getJSONObject(i);
                            c.put("id",obj.getInt("id"));
                            c.put("note",obj.getString("note"));
                            c.put("content",obj.getString("content"));
                            db.insert(sql.TB_name,null,c);
                        }
                        db.close();
                        sql.close();
                        finish();
                        Intent a = new Intent(login.this, Notes.class);
                        startActivity(a);

                    }
                    else if(error.equals("true")){
                        Toast.makeText(login.this, "Login unsuccessful!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(login.this, "Login unsuccessful!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String, String>();
                data.put("username", params[0]);
                data.put("password", params[1]);


                String result = ruc.sendPostRequest(URLs.URL_LOGIN, data);
                return result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(username, password);
    }
}



