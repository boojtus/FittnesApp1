package com.example.aplikacjafitness;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.media.MediaPlayer;
import android.widget.Toast;

import java.util.Locale;

public class  TimerActivity extends AppCompatActivity {

    public MediaPlayer beep;

    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button mButtonReset;
    private Button Decrease;
    private Button Increase;
    private Button Resetserii;
    TextView Seria;


    int sekundy, seria ;
    private CountDownTimer mCountDownTimer;
//bry
    private boolean mTimerRunning;

    boolean s1=true;

    private long mTimeLeftInMillis = sekundy * 1000;
    private long mEndTime;

    //beep = MediaPlayer.create(getApplicationContext(), R.raw.beep);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        beep = MediaPlayer.create(this, R.raw.beep);
        //MediaPlayer beep= MediaPlayer.create(MainActivity.this,R.raw.beep);

        if (savedInstanceState != null){
            sekundy = savedInstanceState.getInt("t");
            seria = savedInstanceState.getInt("s");
        }
        else if(savedInstanceState == null){
            sekundy = 30;
            seria = 1;
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTextViewCountDown = findViewById(R.id.text_view_countdown);

        Seria = findViewById(R.id.seria);
        Seria.setText(Integer.toString(seria));

        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);

        Increase = findViewById(R.id.increase);
        Decrease = findViewById(R.id.decrease);
        Resetserii = findViewById(R.id.Reset_serii);

        Decrease.setVisibility(View.VISIBLE);
        Increase.setVisibility(View.VISIBLE);

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    if(s1 == true) {
                        seria++;
                        Seria.setText(Integer.toString(seria));
                    }
                    s1 = false;
                    Resetserii.setVisibility(View.INVISIBLE);
                    startTimer();
                }
            }
        });




        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        updateCountDownText();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        //super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("t", sekundy);
        savedInstanceState.putInt("s", seria);
        super.onSaveInstanceState(savedInstanceState);
    }
    /*@Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("t", sekundy);
        outState.putInt("s", seria);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        sekundy = savedInstanceState.getInt("s");
        seria = savedInstanceState.getInt("t");

    }*/

    public void Increse (View view){
        sekundy++;
        mTimeLeftInMillis = sekundy * 1000;
        updateCountDownText();
    }
    public void Decrease (View view){
        if(sekundy>30)sekundy--;
        mTimeLeftInMillis = sekundy * 1000;
        updateCountDownText();
    }
    public void Reset_serii (View view){
        if(s1 == true) {
            seria = 1;
            Seria.setText(Integer.toString(seria));
        }
    }

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateButtons();
            }
        }.start();

        mTimerRunning = true;
        Decrease.setVisibility(View.INVISIBLE);
        Increase.setVisibility(View.INVISIBLE);
        updateButtons();
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateButtons();
    }

    private void resetTimer() {
        mTimeLeftInMillis = sekundy * 1000;
        Decrease.setVisibility(View.VISIBLE);
        Increase.setVisibility(View.VISIBLE);
        s1 = true;

        Resetserii.setVisibility(View.VISIBLE);
        updateCountDownText();
        updateButtons();
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        if(seconds<6){
            //TU MUZYKA GRAC BEDZIE
            beep.start();
        }

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
        Seria.setText(Integer.toString(seria));
    }

    private void updateButtons() {
        if (mTimerRunning) {
            mButtonReset.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText("Pause");
        } else {
            mButtonStartPause.setText("Start");

            if (mTimeLeftInMillis < 1000) {
                mButtonStartPause.setVisibility(View.INVISIBLE);
            } else {
                mButtonStartPause.setVisibility(View.VISIBLE);
            }

            if (mTimeLeftInMillis < sekundy * 1000) {
                mButtonReset.setVisibility(View.VISIBLE);
            } else {
                mButtonReset.setVisibility(View.INVISIBLE);
            }

        }
    }


    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);
        editor.putInt("sekundy",sekundy);
        editor.putInt("seria",seria);

        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        mTimeLeftInMillis = prefs.getLong("millisLeft", sekundy * 1000);
        mTimerRunning = prefs.getBoolean("timerRunning", false);
        sekundy = prefs.getInt("sekundy",sekundy);
        seria = prefs.getInt("seria",seria);

        updateCountDownText();
        updateButtons();

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();



            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
                updateButtons();
            } else {
                startTimer();
            }
        }
    }
}
