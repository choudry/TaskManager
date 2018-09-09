package sultaani.com.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import sultaani.com.taskmanager.Helper.Utill;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

         android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();





        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Utill.getDataSP("login",SplashScreen.this)!=null) {
                    if (Utill.getDataSP("login", SplashScreen.this).equals("true")) {
                        startActivity(new Intent(SplashScreen.this, FirstActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                        finish();
                    }
                }else {
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                    finish();
                }
            }
        }, 3000);

    }
}
