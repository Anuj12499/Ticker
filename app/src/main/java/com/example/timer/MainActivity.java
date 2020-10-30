package com.example.timer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SeekBar seek;
    private TextView text;
    private boolean timerOn;
    private Button button;
    private CountDownTimer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seek=findViewById(R.id.seekBar);
        text=findViewById(R.id.textView);
        seek.setMax(600);
        seek.setProgress(60);
        timerOn=false;
        button=findViewById(R.id.button);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String minutes;
                String seconds;
                int min,sec;
                min=progress/60;
                sec=progress%60;
                if(min<10)
                {
                    minutes="0"+String.valueOf(min);
                }
                else
                {
                    minutes=String.valueOf(min);
                }
                if(sec<10)
                {
                    seconds="0"+String.valueOf(sec);
                }
                else
                {
                    seconds=String.valueOf(sec);
                }
                text.setText(minutes+":"+seconds);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
    public void start(View view){
        if(!timerOn) {
            button.setText("Stop");
            seek.setEnabled(false);
            timerOn = true;
            timer = new CountDownTimer(seek.getProgress() * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    String minutes;
                    String seconds;
                    int min, sec;
                    int progress = (int) millisUntilFinished / 1000;
                    min = progress / 60;
                    sec = progress % 60;
                    if (min < 10) {
                        minutes = "0" + String.valueOf(min);
                    } else {
                        minutes = String.valueOf(min);
                    }
                    if (sec < 10) {
                        seconds = "0" + String.valueOf(sec);
                    } else {
                        seconds = String.valueOf(sec);
                    }
                    text.setText(minutes + ":" + seconds);
                }
                @Override
                public void onFinish() {
                    SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    if(sharedPreferences.getBoolean("enable sound",true)) {
                        MediaPlayer player;
                         String melodyName=sharedPreferences.getString("timer_melody","bell");
                         if(melodyName.equals("bell"))
                         {
                             player = MediaPlayer.create(getApplicationContext(), R.raw.bell);
                         }
                         else if(melodyName.equals("alarm_siren"))
                         {
                             player = MediaPlayer.create(getApplicationContext(), R.raw.siren);
                         }
                         else
                         {
                             player = MediaPlayer.create(getApplicationContext(), R.raw.horn);
                         }
                        player.start();
                   }
                    resetTimer();
                }
            };
            timer.start();
        }
        else
        {
            resetTimer();
        }
    }
    public void resetTimer()
    {
        timer.cancel();
        text.setText("00:60");
        button.setText("Start");
        seek.setEnabled(true);
        timerOn=false;
        seek.setProgress(60);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.timer_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_settings1)
        {
            Intent openSettings=new Intent(this,SettingsActivity.class);
            startActivity(openSettings);
            return true;
        }
        else if(id==R.id.action_settings2)
        {
            Intent openAbout=new Intent(this,AboutActivity.class);
            startActivity(openAbout);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}



