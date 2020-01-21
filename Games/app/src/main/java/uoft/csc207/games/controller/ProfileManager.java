package uoft.csc207.games.controller;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.TreeMap;

import uoft.csc207.games.model.Game;
import uoft.csc207.games.model.PlayerProfile;

/**
 * This class manages game players' profile, such as login name, password and the like. It also stores
 * status of each game that a player plaid. The current version of this game app is designed for
 * off-line play. All profiles are stored on a player's device.
 * It provides functions to create and save a player's profile. To avoid a profile has multiple
 * copies, this class is implemented as a singleton pattern. that has following advantages:
 * 1) Guarantee a single copy of a player profile is referenced, updated within the app; implicates
 *    a better data integrity
 * 2) Provide reliable login function
 * 3) Save run-time memory, because of single object. it's important for an app running on portable
 *    device that has limited amount of memory
 * This class also hides profile storage location from the rest of app. In the current version,
 * profiles are stored locally; In a future version, profiles can be stored on a remote location,
 * such as database server in cloud. The complexity of retrieving/storing profiles from/to a remote
 * location is transparent to other components, that use functions of this class.
 *
 * @author group 0630
 * @version 1.0
 */
public class ProfileManager {
    /*
        IMPORTANT: PLEASE DON'T CHANGE ANYTHING IN THIS CLASS. IF YOU THINK THERE'S AN ISSUE, CONTACT
        WILLIAM IN THE GROUP CHAT
     */

    private ProfileManager(){}
    public static String CURRENT_PLAYER = "currentPlayer";
    private static String NAME_OF_PROFILE_STORE = "players.profiles";

    private TreeMap<String, PlayerProfile> profileMap;

    public TreeMap getMap(){
        return profileMap;
    }

    public Context getAppContext() {
        return appContext;
    }

/*
    public void setAppContext(Context appContext) {
        this.appContext = appContext;
    }
*/
    private Context appContext;

    public PlayerProfile getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(PlayerProfile currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    public PlayerProfile getSecondPlayer(){ return secondPlayer; }

    public void setSecondPlayer(PlayerProfile secondPlayer){
        this.secondPlayer = secondPlayer;
    }
    public boolean isFirstTurn(){ return isFirstTurn; }

    private PlayerProfile currentPlayer;
    private PlayerProfile secondPlayer;
    private boolean isFirstTurn = false;
    private boolean isTwoPlayerMode;
    private static ProfileManager singletonProfileManager;

    public void setIsTwoPlayerMode(boolean b){
        isTwoPlayerMode = b;
    }
    public boolean isTwoPlayerMode(){
        return isTwoPlayerMode;
    }

    /**
     * Retrieve a singleton ProfileManager object for a caller
     * @param appContext a Context object to provide system resource for the app
     * @return a singleton ProfileManager object
     *
     * @since 1.0
     */
    public static ProfileManager getProfileManager(Context appContext){
        if(singletonProfileManager == null){
            singletonProfileManager = new ProfileManager();
            singletonProfileManager.init(appContext);
            return singletonProfileManager;
        }else {
            return singletonProfileManager;
        }
    }

    private void init(Context appContext){
        this.appContext = appContext;
        loadProfiles();
        if (profileMap == null){
            profileMap = new TreeMap<>();
        }
    }

    /**
     * Swaps which PlayerProfile is the current player
     */
    public void changeCurrentPlayer(){
        PlayerProfile temp = currentPlayer;
        currentPlayer = secondPlayer;
        secondPlayer = temp;
        isFirstTurn = !isFirstTurn;
    }
    public PlayerProfile getProfileById(String id){
        return profileMap.get(id);
    }

    /**
     * Add a new profile to the profile list
     * @param profile a PlayerProfile object
     */
    public void createProfile(PlayerProfile profile){
        //boolean profileExist = false;
        if(profileMap.get(profile.getId()) != null){
            profileMap.replace(profile.getId(), profile);
        }else {
            profileMap.put(profile.getId(), profile);
        }
        saveProfiles();
    }


    private void loadProfiles(){
        File dir2store = getAppContext().getFilesDir();
        String filePath = dir2store.getPath() + File.pathSeparator + NAME_OF_PROFILE_STORE;
        FileInputStream fileInputStream = null;
        try {
            File fileStoredProfiles = new File(filePath);
            if(!fileStoredProfiles.exists()){
                profileMap = new TreeMap<String, PlayerProfile>();
                return;
            }
            fileInputStream = new FileInputStream(new File(filePath));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            profileMap = (TreeMap<String, PlayerProfile>)objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * Serialize all profiles to a system file
     */
    public void saveProfiles(){
        File dir2store = getAppContext().getFilesDir();
        String filePath = dir2store.getPath() + File.pathSeparator + NAME_OF_PROFILE_STORE;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(new File(filePath));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(profileMap);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
