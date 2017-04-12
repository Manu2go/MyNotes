package manan.mynotes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class login extends AppCompatActivity implements View.OnClickListener {

    private EditText Usrnam;
    private EditText Psswrd;

    private Button signin;

    private static final String REGISTER_URL = "http://chaipeeo.esy.es/login_notes.php";


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

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                //Toast.makeText(login1.this,s,Toast.LENGTH_LONG).show();

                if (s.equals("0")||s.equals("Error")) {
                    Toast.makeText(login.this, "Username or Password incorrect!", Toast.LENGTH_LONG).show();

                } else {

                    String[] words = s.split("!@#\\$%");
                    Intent a = new Intent(login.this, Notes.class);

                    SharedPreferences user = getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = user.edit();
                    //Toast.makeText(login.this, s, Toast.LENGTH_LONG).show();
                   edit.putString("username", words[1]);
                    edit.putString("password", words[2]);
                    edit.apply();
                    Bundle b=new Bundle();
                    b.putStringArray("words",words);
                    a.putExtras(b);

                    //  Toast.makeText(login1.this,words[0]+" "+words[1],Toast.LENGTH_LONG).show();
                    startActivity(a);
                }
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String, String>();
                data.put("username", params[0]);
                data.put("password", params[1]);


                String result = ruc.sendPostRequest(REGISTER_URL, data);
                return result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(username, password);
    }
}



