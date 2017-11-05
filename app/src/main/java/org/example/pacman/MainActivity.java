package org.example.pacman;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.button;

public class MainActivity extends Activity {
    GameView gameView;
    Game game;
    //Counter for tics
    private int counter = 0;
    public int secondsLeft;
    //Instantiate timer objects
    private Timer myTimer;
    private Timer secondsLeftTimer;
    private boolean secondsRunning;

    private TextView remaining;
    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //saying we want the game to run in one mode only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);


        gameView =  findViewById(R.id.gameView);
        TextView textView = findViewById(R.id.points);

        remaining = findViewById(R.id.remainingSeconds);

        activity = this;

        secondsLeft = 30;


        game = new Game(this,textView);
        game.setGameView(gameView);
        gameView.setGame(game);

        game.newGame();

        CreateTimers();

        //Timer moving the pacman
        myTimer = new Timer();
        game.running = false;
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                game.TimerMethod();
            }

        }, 0, 20); //0 indicates we start now

        //------RIGHT
        Button buttonRight = findViewById(R.id.moveRight);
        buttonRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                game.running = true;
                game.setDirection("right");
            }
        });

        //------- LEFT
        Button buttonLeft = findViewById(R.id.moveLeft);
        buttonLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                game.running = true;
                game.setDirection("left");
            }
        });

        //------ UP
        Button buttonUp = findViewById(R.id.moveUp);
        //listener of our pacman, when somebody clicks it
        buttonUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                game.running = true;
                game.setDirection("up");
            }
        });

        //----- DOWN
        Button buttonDown = findViewById(R.id.moveDown);
        //listener of our pacman, when somebody clicks it
        buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.running = true;
                game.setDirection("down");
            }
        });


        Button newGame = (Button) findViewById(R.id.new_game);
        newGame.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                myTimer.cancel();
                secondsLeftTimer.cancel();
                Bundle bundle = new Bundle();
                onCreate(bundle);
            };
        });

        final Button Pause = (Button) findViewById(R.id.pause);
        Pause.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (game.running == true){
                    game.running = false;
                    Pause.setText("Continue");
                    secondsRunning = false;

                } else {
                    game.running = true;
                    Pause.setText("Pause");
                    secondsRunning = true;
                }
            };
        });

    }

    private void CreateTimers(){


        //make a new timer FOR SECONDS LEFT
        secondsLeftTimer = new Timer();
        secondsRunning = false;
        //We will call the timer 5 times each second
        secondsLeftTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SecondsTimerMethod();
            }

        }, 0, 1000); //0 indicates we start now
    }

    @Override
    protected void onStop() {
        super.onStop();
        //just to make sure if the app is killed, that we stop the timer.
        myTimer.cancel();
        secondsLeftTimer.cancel();
    }



    private void SecondsTimerMethod()
    {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.

        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(Seconds_Timer_Tick);
    }


    private Runnable Seconds_Timer_Tick = new Runnable() {
        public void run() {
            //This method runs in the same thread as the UI.
            // so we can draw
            if (game.running)
            {
                if (secondsLeft != 0) {
                    secondsLeft--;
                    remaining.setText("Timer: " + secondsLeft);
                } else{
                    game.running = false;
                    Toast.makeText(activity, "The pills was found by the police", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(this,"settings clicked",Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
