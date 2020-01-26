package Tanks;

import javafx.application.Platform;

import javax.swing.text.html.ImageView;
import java.io.Serializable;
import java.util.ArrayList;

public class TCP_Message implements Serializable {

    private String action;
    private Player player;
    private String payload;
    private int playerID;

    private String playerUsername;
    private int playerTank;
    private int playerHost;
    private Move move;
    private ImageView image;



    private ArrayList<Player> playerList = new ArrayList<>();


    public TCP_Message(String mAction, Player mPlayer, String mPayload) {

        this.action = mAction;
        this.player = mPlayer;
        this.payload = mPayload;
    }

    public TCP_Message(String mAction, Player mPlayer, ArrayList<Player> mPlayerList) {

        this.action = mAction;
        this.player = mPlayer;
        this.playerList = mPlayerList;
    }

    public TCP_Message(String mAction, Player mPlayer) {

        this.action = mAction;
        this.player = mPlayer;
    }

    public TCP_Message(String mAction, int mPlayerID) {

        this.action = mAction;
        this.playerID = mPlayerID;
    }

    public TCP_Message(String mAction) {

        this.action = mAction;
    }

    public TCP_Message(String mAction, String mPayload) {

        this.action = mAction;
        this.payload = mPayload;
    }

    public TCP_Message(String mAction, String mPlayerUsername, int mPlayerHost, int mPlayerTank) {

        this.action = mAction;
        this.playerUsername = mPlayerUsername;
        this.playerTank = mPlayerTank;
        this.playerHost = mPlayerHost;

    }



    public TCP_Message(String mAction, Player mPlayer, Move mMove) {

        this.action = mAction;
        this.player = mPlayer;
        this.move = mMove;
    }

    public TCP_Message() {}


    public ArrayList<Player> getPlayerList() {
        return playerList;
    }


//    public String compose() {
//
//        return String.format("%s:%s:%s:%s:%s", action, String.valueOf(playerID), String.valueOf(isHost), String.valueOf(playerTank), payload);
//
//    }
//
//    public void decompose(String composedMessage) {
//
//        String[] messageSplit = composedMessage.split(":");
//
//        this.action = messageSplit[0];
//        this.playerID = Integer.parseInt(messageSplit[1]);
//        this.isHost = Integer.parseInt(messageSplit[2]);
//        this.playerTank = Integer.parseInt(messageSplit[3]);
//        this.payload = messageSplit[4];
//
//    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public String getPlayerUsername() {
        return playerUsername;
    }

    public void setPlayerUsername(String playerUsername) {
        this.playerUsername = playerUsername;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerTank() {
        return playerTank;
    }

    public void setPlayerTank(int playerTank) {
        this.playerTank = playerTank;
    }

    public int getPlayerHost() {
        return playerHost;
    }

    public void setPlayerHost(int playerHost) {
        this.playerHost = playerHost;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
