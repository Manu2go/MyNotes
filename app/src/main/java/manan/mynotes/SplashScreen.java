package manan.mynotes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    private ImageView splashImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences user = getSharedPreferences("user", Context.MODE_PRIVATE);
        if(user.getString("login_status","0").equals("1")){
            finish();
            Intent i=new Intent(this, Notes.class);
            startActivity(i);
        }
        else {
            splashImageView = new ImageView(this);
            splashImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            splashImageView.setImageResource(R.drawable.welcome);
            setContentView(splashImageView);

            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                public void run() {
                    change();

                }

            }, 1000);
        }
    }
    public void change(){
        finish();
        Intent i=new Intent(this, manan.mynotes.login.class);
        startActivity(i);
    }

}

