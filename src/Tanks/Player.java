package Tanks;


import java.io.Serializable;

public class Player implements Serializable {

    private String playerName;
    private int playerScore = 0;
    private int playerTank;
    private int playerID;
    private int isHost;

    public Player(int mPlayerID, String mPlayerName, int mPlayerTank, int mIsHost) {

        this.playerName = mPlayerName;
        this.playerID = mPlayerID;
        this.playerTank = mPlayerTank;
        this.isHost = mIsHost;
    }

    public int isHost() {
        return isHost;
    }

    public void setHost(int host) {
        isHost = host;
    }

    public  int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public int getPlayerTank() {
        return playerTank;
    }

    public void setPlayerTank(int playerTank) {
        this.playerTank = playerTank;
    }

    public String getPlayerName() {

        return playerName;

    }

    public void setPlayerName(String playerName) {

        this.playerName = playerName;

    }
}
