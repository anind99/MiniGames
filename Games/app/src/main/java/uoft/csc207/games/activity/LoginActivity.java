package uoft.csc207.games.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import uoft.csc207.games.model.PlayerProfile;
import uoft.csc207.games.controller.ProfileManager;
import uoft.csc207.games.R;

/**
 * Provides interface for the user's login. Consists of EditTexts for the username and password, will
 * display an appropriate error message for each attempted login/create account error. Also allows for
 * two players to login at the same time.
 */
public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button login;
    private Button create;
    private Button doubleLogin;
    private TextView errorMsg;
    private ProfileManager profileManager;
    /**
     * Whether it is the first login of the two players that are logging in or not.
     */
    private boolean isFirstLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        profileManager = ProfileManager.getProfileManager(getApplicationContext());

        username = (EditText) findViewById(R.id.etName);
        password = (EditText) findViewById(R.id.etPassword);
        login = (Button) findViewById(R.id.btnLogin);
        create = (Button) findViewById(R.id.btnCreate);
        doubleLogin = (Button) findViewById(R.id.btnDoubleLogin);
        errorMsg = (TextView) findViewById(R.id.txtErrorMsg);
        isFirstLogin = true;

        //Immediately logs in a use if their entered fields match an existing PlayerProfile
        login.setOnClickListener(new View.OnClickListener(){
           public void onClick(View view){
               validate(username.getText().toString(), password.getText().toString());
               profileManager.setIsTwoPlayerMode(false);
           }
        });
        //Creates a PlayerProfile if there doesn't already exist a PlayerProfile with the same username
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createProfile(username.getText().toString(), password.getText().toString());
            }
        });
        //Used for logging in both the first and second user
        doubleLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                profileManager.setIsTwoPlayerMode(true);
                validate(username.getText().toString(), password.getText().toString());
                username.setText("");
                password.setText("");
                doubleLogin.setText("2 Player Login (2 of 2)");
            }
        });
    }

    /**
     * Validates whether a login is correct and displays the appropriate message based off the outcome
     * @param userName The entered username
     * @param passWord The entered password
     */
    private void validate(String userName, String passWord){
        if(null == userName || userName.equalsIgnoreCase("")){
            errorMsg.setText("Username is required");
            return;
        }
        if(null == passWord || passWord.equalsIgnoreCase("")){
            errorMsg.setText("Password is required");
            return;
        }
        PlayerProfile tmpProfile = profileManager.getProfileById(userName);
        if(tmpProfile == null || !tmpProfile.getId().equalsIgnoreCase(userName)){
            errorMsg.setText("User: " + userName + " doesn't exist!");
            return;
        }
        if(!tmpProfile.getPassword().equals(passWord)){
            errorMsg.setText("The password of user: " + userName + " is incorrect!");
            return;
        }
        executeCorrectLoginAction(tmpProfile);
    }

    /**
     * Decides and executes the correct course of action if the login attempt satisfies all validation
     * checks. If it's a single login, the passed PlayerProfile will log in as the currentPlayer. If it's a
     * double login, depending on whether it is the first or second login, will either log in the
     * passed PlayerProfile as the currentPlayer and clear the text fields for the second player to
     * enter their information, or log in the passed PlayerProfile as the secondPlayer and move to
     * the GameSelectActivity menu
     * @param tmpProfile The PlayerProfile to be logged in
     */
    private void executeCorrectLoginAction(PlayerProfile tmpProfile){
        if(!profileManager.isTwoPlayerMode()){
            moveToGameSelectActivity();
            profileManager.setCurrentPlayer(tmpProfile);
        } else {
            if(isFirstLogin){
                profileManager.setCurrentPlayer(tmpProfile);
                errorMsg.setText("User " + tmpProfile.getId() + " has been logged in");
                isFirstLogin = false;
            } else {
                if(profileManager.getCurrentPlayer() == tmpProfile){
                    errorMsg.setText("User " + tmpProfile.getId() + " has already been logged in");
                } else {
                    profileManager.setSecondPlayer(tmpProfile);
                    moveToGameSelectActivity();
                }
            }
        }
    }

    /**
     * Creates a PlayerProfile and adds it to to ProfileManager's tree map of PlayerProfiles. If a PlayerProfile
     * with the same id (username) already exists in the tree map, will reject the createProfile attempt.
     * Displays the appropriate message depending on the outcome.
     * @param userName Username of the PlayerProfile to be created
     * @param passWord Password of the PlayerProfile to be created
     */
    private void createProfile(String userName, String passWord){
        PlayerProfile newProfile = new PlayerProfile(userName, passWord);
        if(profileManager.getProfileById(userName) != null){
            errorMsg.setText("User name: " + userName + " is already used by another player!");
            return;
        }
        profileManager.createProfile(newProfile);
        errorMsg.setText("User has been created.");
    }

    /**
     * Moves from LoginActivity to GameSelectActivity
     */
    private void moveToGameSelectActivity(){
        Intent intent = new Intent(LoginActivity.this, GameSelectActivity.class);
        startActivity(intent);
    }
}
