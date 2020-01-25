package Tanks;

import javafx.application.Application;
import javafx.css.PseudoClass;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static final String ACTION_addPlayer = "add_player";
    public static final String ACTION_startGame = "start_game";
    public static final String ACTION_makeMove = "make_move";
    public static final String ACTION_playerKilled = "player_killed";

    public static int userSelectedTank = -1;
    public static int isHost = 0;
    public static Player localPlayer = null;

    public static Board mainBoard;
    public static lobbyWindowController lobbyWindowController;

    public static ClientTCP_Listener clientConnection;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("startWindow.fxml"));
        Scene mainScene = new Scene(root, 800, 550);

        primaryStage.setTitle("Tanks - Bartosz Kubacki & Bartłomiej Urbanek");
        //primaryStage.setFullScreen(true);
        primaryStage.setScene(mainScene);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
