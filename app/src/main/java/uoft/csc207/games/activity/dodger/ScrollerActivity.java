package uoft.csc207.games.activity.dodger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import uoft.csc207.games.R;
import uoft.csc207.games.activity.AddScoreActivity;
import uoft.csc207.games.activity.GameSelectActivity;
import uoft.csc207.games.activity.TurnDisplayActivity;
import uoft.csc207.games.controller.ProfileManager;
import uoft.csc207.games.controller.ScoreBoard;
import uoft.csc207.games.model.ScrollerGame;
import uoft.csc207.games.model.dodger.Constants;
import uoft.csc207.games.model.dodger.GamePanel;
import uoft.csc207.games.model.dodger.MainThread;

public class ScrollerActivity extends Activity {
    private GamePanel scrollerPanel;
    private FrameLayout gameFrame;
    private RelativeLayout widgetHolder;
    private Button exit;
    public boolean exited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        exited = false;
        Constants.CURRENT_CONTEXT = this;
        DisplayMetrics d = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(d);
        Constants.SCREEN_HEIGHT = d.heightPixels;
        Constants.SCREEN_WIDTH = d.widthPixels;
        Constants.activity = this;

        widgetHolder = new RelativeLayout(this);
        scrollerPanel = new GamePanel(this);
        exit = createButton(widgetHolder);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitGame();
            }
        });

        gameFrame = new FrameLayout(this);
        gameFrame.addView(scrollerPanel);
        gameFrame.addView(widgetHolder);

        this.setContentView(gameFrame);
    }

    public void finishGame() {
        ProfileManager.getProfileManager(getApplicationContext()).saveProfiles();
        MainThread t = (MainThread) scrollerPanel.getMainThread();
        t.setRunning(false);
        Intent myIntent = new Intent(ScrollerActivity.this, AddScoreActivity.class);
        startActivity(myIntent);
    }

    public void  exitGame(){
        exited = true;
        Intent myIntent;
        if(ProfileManager.getProfileManager(this).isTwoPlayerMode()){
            myIntent = new Intent(ScrollerActivity.this, TurnDisplayActivity.class);
            myIntent.putExtra("SOURCE_ACTIVITY", ScrollerActivity.class.getName());
            ProfileManager.getProfileManager(this).changeCurrentPlayer();
        } else {
            myIntent = new Intent(ScrollerActivity.this, GameSelectActivity.class);
        }
        startActivity(myIntent);
    }
    private Button createButton(RelativeLayout widgetHolder) {
        Button pauseResumeBtn = new Button(this);
        pauseResumeBtn.setText(R.string.exit);

        RelativeLayout.LayoutParams params4Btn = new RelativeLayout.LayoutParams(RelativeLayout.
                LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams params4Layout = new RelativeLayout.LayoutParams(RelativeLayout.
                LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        widgetHolder.setLayoutParams(params4Layout);
        widgetHolder.addView(pauseResumeBtn);
        params4Btn.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        params4Btn.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        pauseResumeBtn.setLayoutParams(params4Btn);
        return pauseResumeBtn;
    }
}
