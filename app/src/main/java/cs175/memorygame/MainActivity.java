package cs175.memorygame;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.playbtn) Button playBtn;
    @BindView(R.id.rulebtn) Button ruleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.playbtn)
    public void onClickPlay(View v) {
        if(((MyApp)getApplication()).getBoolean() == true ) {
            PopupMenu popup = new PopupMenu(MainActivity.this, findViewById(R.id.playbtn));
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.popup_menu2, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getTitle().equals("Start Over"))
                        ((MyApp)getApplication()).storeBoolean(false);

                    Intent play = new Intent(MainActivity.this, PlayGround.class);
                    startActivity(play);
                    return true;
                }
            });

            popup.show();
        } else {
            Intent play = new Intent(MainActivity.this, PlayGround.class);
            startActivity(play);
        }

    }

    @OnClick(R.id.rulebtn)
    public void onClickRule(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RuleFragment rule_fragment = new RuleFragment();
        fragmentTransaction.replace(android.R.id.content, rule_fragment);
        fragmentTransaction.commit();
    }
}
