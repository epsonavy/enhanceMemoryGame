package cs175.memorygame;

import android.app.Application;
import java.util.HashMap;

public class MyApp extends Application {
    private boolean renewBoolean = false;
    private boolean shuffle = false;
    private HashMap<Integer, Integer> randomHM = new HashMap<>(20);
    private HashMap<Integer, Integer> vanishHM= new HashMap<>(20);
    private int score;
    private int currentOpen;

    public void storeBoolean(boolean data) {
        this.renewBoolean = data;
    }

    public void storeShuffle(boolean data) {
        this.shuffle = data;
    }

    public void storeHM(HashMap<Integer, Integer> hm) {
        this.randomHM = hm;
    }

    public void storeVanishHM(HashMap<Integer, Integer> hm) {
        this.vanishHM = hm;
    }

    public void storeScore(int s) {
        this.score = s;
    }

    public void storeCurrentOpen(int s) {
        this.currentOpen = s;
    }

    public boolean getBoolean() {
        return renewBoolean;
    }

    public boolean getShuffle() {
        return shuffle;
    }

    public HashMap<Integer, Integer> getHM() {
        return randomHM;
    }

    public HashMap<Integer, Integer> getVanishHM() {
        return vanishHM;
    }

    public int getScore() {
        return score;
    }

    public int getCurrentOpen() {
        return currentOpen;
    }
}

