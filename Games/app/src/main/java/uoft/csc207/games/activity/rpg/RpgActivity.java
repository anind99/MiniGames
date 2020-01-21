package uoft.csc207.games.activity.rpg;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import uoft.csc207.games.controller.ProfileManager;
import uoft.csc207.games.model.Score;
import uoft.csc207.games.controller.scoreboard.ScoreBoard;
import uoft.csc207.games.model.rpg.GameSurface;
import uoft.csc207.games.model.rpg.RpgGameState;

/**
 * The activity of the rpg game. Responsible for graphics related features of the rpg game that don't
 * involve drawing on the canvas.
 */
public class RpgActivity extends Activity implements PopupMenu.OnMenuItemClickListener {
    private GameSurface gameSurface;
    private FrameLayout gameFrame;
    private RelativeLayout widgetHolder;
    private Button settingsBtn;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameSurface = new GameSurface(this);
        gameFrame = new FrameLayout(this);
        widgetHolder = new RelativeLayout(this);
        settingsBtn = createButton(widgetHolder);

        // Set fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set No Title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        gameFrame.addView(gameSurface);
        gameFrame.addView(widgetHolder);
        this.setContentView(gameFrame);

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });
    }

    /**
     * What pops up the popup menu
     * @param v The view to show the popup menu in
     */
    public void showPopup(View v){
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.game_menu, popup.getMenu());
        popup.show();
    }

    /**
     * Does all the things necessary to properly finish the rpg game, and then transitions to the
     * AddScoreActivity to give the player the option of adding their score to the leader board.
     * @param rpgState The rpgState to reset the current stats of
     * @param wipeGameStats Whether or not to reset the stats
     */
    public void finishGame(RpgGameState rpgState, boolean wipeGameStats){
        ScoreBoard.setCurrentScore(new Score("", rpgState.getScore(), rpgState.getGameCurrency(),
                RpgActivity.class.getName()));
        if(rpgState != null && wipeGameStats){
            rpgState.restart();
        }
        ProfileManager.getProfileManager(getApplicationContext()).saveProfiles();
        gameSurface.getGameThread().setRunning(false);

        Intent myIntent;
        myIntent = new Intent(RpgActivity.this, AddScoreActivity.class);
        startActivity(myIntent);
    }

    /**
     * Assigns the function of each of the popup menu's items, consists of the customization items
     * and the exit button.
     * @param item The menu item that has been clicked
     * @return whether the menu item successfully executed its function
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.male_item:
                gameSurface.getRpgGameManager().getCurrentGameState().chooseCharacter("male");
                break;
            case R.id.female_item:
                gameSurface.getRpgGameManager().getCurrentGameState().chooseCharacter("female");
                break;
            case R.id.black_item:
                gameSurface.getRpgGameManager().getCurrentGameState().chooseColor("black");
                gameSurface.getRpgGameManager().getScorePaint().setColor(Color.BLACK);
                break;
            case R.id.red_item:
                gameSurface.getRpgGameManager().getCurrentGameState().chooseColor(RpgGameState.FONT_COLOR_RED);
                gameSurface.getRpgGameManager().getScorePaint().setColor(Color.RED);
                break;
            case R.id.white_item:
                gameSurface.getRpgGameManager().getCurrentGameState().chooseColor(RpgGameState.FONT_COLOR_WHITE);
                gameSurface.getRpgGameManager().getScorePaint().setColor(Color.WHITE);
                break;
            case R.id.monospace_item:
                gameSurface.getRpgGameManager().getCurrentGameState().chooseFont(RpgGameState.FONT_TYPE_MONOSPACE);
                gameSurface.getRpgGameManager().getScorePaint().setTypeface(Typeface.MONOSPACE);
                break;
            case R.id.sans_serif_item:
                gameSurface.getRpgGameManager().getCurrentGameState().chooseFont(RpgGameState.FONT_TYPE_SANS_SERIF);
                gameSurface.getRpgGameManager().getScorePaint().setTypeface(Typeface.SANS_SERIF);;
                break;
            case R.id.exit_rpg_item:
                gameSurface.getGameThread().setRunning(false);
                ProfileManager.getProfileManager(getApplicationContext()).saveProfiles();
                Intent myIntent = new Intent(RpgActivity.this, GameSelectActivity.class);
                startActivity(myIntent);
                break;
        }
        return false;
    }

    /**
     * Creates a button for the settings popup menu
     * @param widgetHolder The RelativeLayout the menu button is in
     * @return The created button
     */
    private Button createButton(RelativeLayout widgetHolder) {
        Button settingsBtn = new Button(this);
        settingsBtn.setText(R.string.rpg_setting);

        RelativeLayout.LayoutParams params4Btn = new RelativeLayout.LayoutParams(RelativeLayout.
                LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams params4Layout = new RelativeLayout.LayoutParams(RelativeLayout.
                LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        widgetHolder.setLayoutParams(params4Layout);
        widgetHolder.addView(settingsBtn);
        params4Btn.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        params4Btn.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        settingsBtn.setLayoutParams(params4Btn);
        return settingsBtn;
    }
}
