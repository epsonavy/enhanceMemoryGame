package cs175.memorygame;

import java.util.HashMap;

public class DataSingleton {
    private static DataSingleton ourInstance;

    public boolean renewBoolean = false;
    public boolean shuffle = false;
    public HashMap<Integer, Integer> randomHM = new HashMap<>(20);
    public HashMap<Integer, Integer> vanishHM = new HashMap<>(20);
    public int score;
    public int currentOpen;

    public static DataSingleton getInstance() {
        if (ourInstance == null)
            ourInstance = new DataSingleton();

        return ourInstance;
    }

    private DataSingleton() {
    }
}
