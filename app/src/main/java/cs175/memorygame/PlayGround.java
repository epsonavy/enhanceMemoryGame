package cs175.memorygame;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayGround extends AppCompatActivity implements View.OnClickListener {

    // All images source
    private final int[] imageIds = {
            R.drawable.china,
            R.drawable.finland,
            R.drawable.france,
            R.drawable.germany,
            R.drawable.greece,
            R.drawable.hongkong,
            R.drawable.india,
            R.drawable.japan,
            R.drawable.turkey,
            R.drawable.uk
    };

    // All locations of the imageButtons
    private int[] locateIds = {
            R.id.image1,
            R.id.image2,
            R.id.image3,
            R.id.image4,
            R.id.image5,
            R.id.image6,
            R.id.image7,
            R.id.image8,
            R.id.image9,
            R.id.image10,
            R.id.image11,
            R.id.image12,
            R.id.image13,
            R.id.image14,
            R.id.image15,
            R.id.image16,
            R.id.image17,
            R.id.image18,
            R.id.image19,
            R.id.image20,
    };

    // Initial 20 space for picking random tiles
    private ArrayList<Integer> ary = new ArrayList<>(20);

    // Clicked status, default value set false
    private Boolean clicked = new Boolean(false);

    // Save picked random tiles set
    // HashMap <Integer: locationID , Integer: imageID>
    private HashMap<Integer, Integer> randomHM = new HashMap<>(20);

    // Record cleared tiles
    // HashMap <Integer: locationID , Integer: imageID>
    private HashMap<Integer, Integer> vanishHM = new HashMap<>(20);

    // ImageButton array
    final ImageButton[] btns = new ImageButton[20];

    // Log information
    private static final String TAG = "LogInfo";

    // Game points
    private int score = 0;

    // Use to check both tiles if they are match
    private int[] pair = new int[2];

    // Integer number indicate how many in pair.
    private int isPairReady = 0;

    // Handler delay feature
    private Handler handler = new Handler();

    // Clickable status for All buttons
    private boolean allClickable = true;

    // Resume or restart the game
    private boolean resumeStatus = false;

    // Shuffle status
    private boolean shuffleStatus = false;

    // Random generator
    private Random generator;

    // current flip tile
    private int currentFlip = 0;

    // Data singleton to save all data
    private DataSingleton singleton;

    @BindView(R.id.menu_btn) Button menuBtn;
    @BindView(R.id.score) TextView s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playground);
        ButterKnife.bind(this);

        singleton = DataSingleton.getInstance();
        resumeStatus = singleton.renewBoolean;
        shuffleStatus = singleton.shuffle;

        if (shuffleStatus) {
            randomHM = singleton.randomHM;
            vanishHM = singleton.vanishHM;
            shuffle(randomHM, vanishHM);
        }

        // Adding all 20 tiles to ary
        for (int i = 0; i < imageIds.length; i++) {
            ary.add(imageIds[i]);
            ary.add(imageIds[i]); // add 2 sets images
        }

        // Initial property of all tiles, and generate randomSet
        for (int i = 0; i < btns.length; i++) {
            btns[i] = (ImageButton) findViewById(locateIds[i]);
            btns[i].setTag(clicked); // wasn't clicked

            if (resumeStatus != true) {
                generator = new Random();
                int index = generator.nextInt(ary.size());
                int randomImageId = ary.get(index);
                randomHM.put(locateIds[i], randomImageId);
                btns[i].setImageResource(R.drawable.back);
                ary.remove(index);
            } else {
                randomHM = singleton.randomHM;
                vanishHM = singleton.vanishHM;
                btns[i].setImageResource(R.drawable.back);
            }

            btns[i].setOnClickListener(this);

        }

        if (resumeStatus == true) {
            for (Integer key : vanishHM.keySet()) {
                ImageButton myBtn = (ImageButton) findViewById(key);
                myBtn.setVisibility(View.INVISIBLE);
            }
            score = singleton.score;
            updateScore();

            currentFlip = singleton.currentOpen;
            if (currentFlip != 0) {
                ImageButton myBtn = (ImageButton) findViewById(currentFlip);
                myBtn.setImageResource(randomHM.get(currentFlip));
                myBtn.setTag(new Boolean(true));
                addPair(currentFlip);
            }
        }

    }

    // Move cleared tiles to the end
    private void shuffle(HashMap<Integer, Integer> hm1, HashMap<Integer, Integer> hm2) {
        if (hm1.size() == 0) {
            return;
        }
        HashMap<Integer, Integer> tempHM = new HashMap<>();
        int temp;
        int i = locateIds.length - 1;
        for (Integer key : hm2.keySet()) {
            temp = hm1.get(locateIds[i]);
            hm1.put(locateIds[i], hm2.get(key));
            tempHM.put(locateIds[i], hm2.get(key));
            hm1.put(key, temp);
            i--;
        }
        hm2.clear();
        hm2.putAll(tempHM);
    }


    @OnClick(R.id.menu_btn)
    public void onClickMenu(final View v) {
        PopupMenu popup = new PopupMenu(PlayGround.this, menuBtn);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().equals("Start Over")) {
                    singleton.renewBoolean = false;
                    recreate();
                }
                if (item.getTitle().equals("Shuffle")) {
                    singleton.renewBoolean = true;
                    singleton.randomHM = randomHM;
                    singleton.vanishHM = vanishHM;
                    singleton.score = score;
                    singleton.currentOpen = currentFlip;
                    singleton.shuffle = true;
                    recreate();
                }
                return true;
            }
        });

        popup.show();
    }

    // Save state changes to the savedInstanceState.
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Store all data even rotate screen
        savedInstanceState.putBoolean("renewBoolean", false);
    }

    // Click event handle connection imageButton to randomSet
    // And implement flip over and flip back by setTag
    @Override
    public void onClick(View v) {

        if(allClickable == false) {
            return;
        }

        ImageButton myBtn = (ImageButton) findViewById(v.getId());

        // Animation Lib - from https://github.com/daimajia
        YoYo.with(Techniques.FlipOutX)
                .duration(1000)
                .playOn(myBtn);

        if (((Boolean)myBtn.getTag()) == false ) {
            currentFlip = v.getId();
            myBtn.setImageResource(randomHM.get(currentFlip));
            myBtn.setTag(new Boolean(true));
        } else {
            myBtn.setImageResource(R.drawable.back);
            myBtn.setTag(new Boolean(false));
        }

        YoYo.with(Techniques.FlipInX)
                .duration(1000)
                .playOn(myBtn);

        addPair(v.getId());
        if (isPairReady == 2) {
            // After 2 clicked, disable any more click
            allClickable = false;
            // Delay 500ms to validate those 2 tiles
            handler.postDelayed(matchValidate, 800);
            clearPair();
        }
    }

    private Runnable matchValidate = new Runnable() {
        @Override
        public void run() {
            matchCheck();
            allClickable = true;
        }
    };

    private void addPair(int item) {
        if(isPairReady == 0) {
            pair[0] = item;
        } else if (isPairReady == 1) {
            pair[1] = item;
        }
        isPairReady++;
        Log.i(TAG,"addPair: " + randomHM.get(item) + " how many tiles = " + isPairReady);
    }

    private void removeTile(int item) {
        ImageButton needRemove = (ImageButton) findViewById(item);
        needRemove.setVisibility(View.INVISIBLE);
        vanishHM.put(item, randomHM.get(item));
        Log.i(TAG,"remove: " + randomHM.get(item));
    }

    private void recoverTile(int item) {
        ImageButton needRecover = (ImageButton) findViewById(item);
        needRecover.setImageResource(R.drawable.back);
        needRecover.setTag(new Boolean(false));
        Log.i(TAG,"recover: " + randomHM.get(item));
    }

    private void clearPair() {
        isPairReady -= 2;
        currentFlip = 0;
        Log.i(TAG,"clearPair!");
    }

    private void matchCheck() {

        if(randomHM.get(pair[0]).equals(randomHM.get(pair[1])) && pair[0] != pair[1]) {
            score++;
            updateScore();
            removeTile(pair[0]);
            removeTile(pair[1]);
        } else if (pair[0] == pair[1]) { // if clicked on the same tile
            recoverTile(pair[0]);
        } else {
            recoverTile(pair[0]);
            recoverTile(pair[1]);
        }
    }

    private void updateScore() {
        s.setText(String.valueOf(score));
    }

    @Override
    public void onBackPressed() {
        // Store all data if player want to resume the game;
        singleton.renewBoolean = true;
        singleton.randomHM = randomHM;
        singleton.vanishHM = vanishHM;
        singleton.score = score;
        singleton.currentOpen = currentFlip;
        singleton.shuffle = shuffleStatus;
        super.onBackPressed();
    }

}
