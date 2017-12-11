package cosw.eci.edu.pancomido.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cosw.eci.edu.pancomido.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Intent intent = getIntent();

        if(intent.getExtras()!=null && intent.getStringExtra("command_id")!=null){
            String command_id = intent.getStringExtra("command_id");
            Intent i = new Intent(this, CommandReadyActivity.class);
            i.putExtra("command_id", command_id);
            startActivity(i);
        }else{
            Thread timer = new Thread(){
                //El nuevo Thread exige el metodo run
                public void run(){
                    try{
                        sleep(2000);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }finally{
                        Intent intent = new Intent();
                        intent.setClass(SplashScreen.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            };
            timer.start();
        }
    }
}
