package org.example.pacman;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Random;

/**
 *
 * This class should contain all your game logic
 */

public class Game {
    //context is a reference to the activity
    private Context context;
    private Integer points = 0; //how points do we have
    private MainActivity activity;
    int pacSize = 150;
    int pillSize = 80;
    int enemySize = 130;

    public void setDirection(String direction) {
        this.direction = direction;
    }

    private String direction = "right";
    public boolean running = false;
    //bitmap of the pacman
    private Bitmap pacBitmap;
    //bitmap of enemy
    private Bitmap enemyBitmap;
    //bitmap of coin
    private Bitmap pillBitmap;
    //textview reference to points
    private TextView pointsView;
    private int pacx, pacy;
    private int enemyx, enemyy;
    private int amountOfPills = 6;
    private int amountOfEnemies = 3;
    //the list of pills - initially empty
    public ArrayList<Pill> PillsArray = new ArrayList<>();
    private ArrayList<Pill> PillsLeftOnScreen = new ArrayList<>();
    private ArrayList<Enemies> EnemiesArray = new ArrayList<>();
    //a reference to the gameview
    private GameView gameView;
    private int h,w; //height and width of screen

    public Game(Context context, TextView view)
    {
        this.activity = (MainActivity) context;
        this.context = context;
        this.pointsView = view;
        pacBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman);
        pillBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.pill);
        enemyBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.police);
    }

    public void setGameView(GameView view)
    {
        this.gameView = view;
    }

    public void newGame()
    {
        pacx = 50;
        pacy = 400; //just some starting coordinates


        for(int i = 0; i < amountOfEnemies; i++){
            Enemies enemies = new Enemies();
            EnemiesArray.add(enemies);
        }

        addEnemies();

        for(int i = 0; i < amountOfPills; i++){
            Pill pill = new Pill();
            PillsArray.add(pill);
        }

        addPills();

        activity.secondsLeft = 20;

        //reset the points
        points = 0;
        pointsView.setText(context.getResources().getString(R.string.points)+" "+points);
        gameView.invalidate(); //redraw screen
    }

    public void TimerMethod()
    {
        activity.runOnUiThread(Timer_Tick);
    }

    private Runnable Timer_Tick = new Runnable() {
        public void run() {
            //This method runs in the same thread as the UI.
            // so we can draw
            if(running) {
                if (direction == "up") {
                    movePacmanUp(-5);
                }
                if (direction == "right") {
                    movePacmanRight(5);
                }
                if (direction == "down") {
                    movePacmanDown(5);
                }
                if (direction == "left") {
                    movePacmanLeft(-5);
                }
                enemyLogic();
            }
        }
    };

    public void enemyLogic(){
        for (Enemies enemies : EnemiesArray){
            int EnemyX = enemies.getEnemyX();
            int EnemyY = enemies.getEnemyY();
            int PacX = getPacx();
            int PacY = getPacy();

            if(EnemyX > PacX){
                enemies.setEnemyX(enemies.getEnemyX() + -3);
            } else if(EnemyX < PacX){
                enemies.setEnemyX(enemies.getEnemyX() + 3);
            }

            if(EnemyY > PacY){
                enemies.setEnemyY(enemies.getEnemyY() + -3);
            } else if(EnemyY < PacY){
                enemies.setEnemyY(enemies.getEnemyY() + 3);
            }
        }
    }

    private int randomNumber(){
        Random rand = new Random();
        int randomNum = rand.nextInt(900 - 50);
        return randomNum;
    }


    private int randomNumberEnemies(){
        Random rand = new Random();
        int randomNum = rand.nextInt(500 - 0)+160;
        return randomNum;
    }

    public void addEnemies(){
        for (Enemies enemies : EnemiesArray) {
            enemies.setEnemyX(randomNumberEnemies());
            enemies.setEnemyY(randomNumberEnemies());
        }
    }

    public void addPills(){
        for (Pill pill : PillsArray) {
            pill.setPillx(randomNumber());
            pill.setPilly(randomNumber());
        }
    }


    public void setSize(int h, int w)
    {
        this.h = h;
        this.w = w;
    }

    public void moveEnemyX(int pixels){
        for(Enemies enemies : EnemiesArray) {
            enemies.setEnemyX(enemies.getEnemyX() + pixels);
            gameView.invalidate();
        }

    }

    public void moveEnemyY(int pixels){
        for(Enemies enemies : EnemiesArray) {
            enemies.setEnemyY(enemies.getEnemyY()+ pixels);
            gameView.invalidate();
        }
    }

    public void movePacmanRight(int pixels)
    {
        //still within our boundaries?
        if (pacx+pixels+(pacBitmap.getWidth()/2)<w) {
            pacx = pacx + pixels;
        } else{
            pacx =  0 - (pacBitmap.getWidth());
        }
        gameView.invalidate();
    }

    public void movePacmanLeft(int pixels){
        if(pacx+pixels+pacBitmap.getWidth()>0){
            pacx = pacx + pixels;
        } else{
            pacx = w + (pacBitmap.getWidth()/2) ;
        }
        gameView.invalidate();
    }


    public void movePacmanUp(int pixels){
        if(pacy+pixels+pacBitmap.getHeight()>0){
            pacy = pacy + pixels;
        } else{
            pacy = h  + (pacBitmap.getHeight()/2);
        }
        gameView.invalidate();
    }

    public void movePacmanDown(int pixels){
        if(pacy+pixels+(pacBitmap.getHeight()/2)<h){
            pacy = pacy + pixels;
        }
        else{
            pacy = 0 - (pacBitmap.getHeight());
        }
        gameView.invalidate();
    }

    public void doCollisionCheck()
    {
        int pacCenterX = pacx + (pacSize/2);
        int pacCenterY = pacy + (pacSize/2);
        for (Pill pill : PillsArray){
            if (pill.isTaken == false){
                int pillCenterX = pill.getPillx() + (pillSize/2);
                int pillCenterY = pill.getPilly() + (pillSize/2);
                double quickmath = Math.sqrt(Math.pow(pillCenterX - pacCenterX, 2) + Math.pow(pillCenterY - pacCenterY, 2));
                if(quickmath < 80){
                    pill.isTaken = true;
                    points++;
                    pointsView.setText("Points:" + points.toString());
                    if (PillsArray.size() == points){
                        running = false;
                        Toast.makeText(context, "Pacman safely ate all the pills!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
    public void enemyCollisionCheck()
    {
        for(Enemies enemies : EnemiesArray) {
            int pacCenterX = pacx + (pacSize / 2);
            int pacCenterY = pacy + (pacSize / 2);
            int enemyCenterX = enemies.getEnemyX() + (enemySize / 2);
            int enemyCenterY = enemies.getEnemyY() + (enemySize / 2);
            double quickmath = Math.sqrt(Math.pow(enemyCenterX - pacCenterX, 2) + Math.pow(enemyCenterY - pacCenterY, 2));
            System.out.println(quickmath + "HALLO");
            if (quickmath < 80) {
                running = false;
                Toast.makeText(context, "You got caught and didn't make it to the party", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public int getPacx()
    {
        return pacx;
    }

    public int getPacy()
    {
        return pacy;
    }

    public int getEnemyx()
    {
        return enemyx;
    }

    public int getEnemyy()
    {
        return enemyy;
    }

    public int getPoints()
    {
        return points;
    }

    public ArrayList<Pill> getPills()
    {
        return PillsArray;
    }

    public ArrayList<Enemies> getEnemies()
    {
        return EnemiesArray;
    }

    public Bitmap getPillBitmap(){
        return pillBitmap;
    }

    public Bitmap getPacBitmap()
    {
        return pacBitmap;
    }

    public Bitmap getEnemyBitmap()
    {
        return enemyBitmap;
    }



}
