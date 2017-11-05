package org.example.pacman;

/**
 * Created by Anders on 13-10-2017.
 */

public class Pill {

    private int pillx;

    private int pilly;

    boolean isTaken = false;

    public int getPillx(){
        return pillx;
    }

    public void setPillx(int x){
        this.pillx = x;
    }

    public int getPilly(){
        return pilly;
    }

    public void setPilly(int y){
        this.pilly = y;
    }
}
