package Tanks;

import javafx.application.Application;
import javafx.css.PseudoClass;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.Statement;

public class Main extends Application {

    public static final String ACTION_addPlayer = "add_player";
    public static final String ACTION_startGame = "start_game";
    public static final String ACTION_makeMove = "make_move";
    public static final String ACTION_playerKilled = "player_killed";
    public static final String ACTION_endGame = "end_game";

    public static int userSelectedTank = -1;
    public static int isHost = 0;
    public static Player localPlayer = null;

    public static Board mainBoard;
    public static lobbyWindowController lobbyWindowController;

    public static ClientTCP_Listener clientConnection;

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Create database for storing results

        Connection conn = JDBC_Helper.connect();
        Statement statement = JDBC_Helper.statement(conn);

        // Create database if not exists
        JDBC_Helper.executeUpdate(statement, "CREATE DATABASE IF NOT EXISTS `tanks`");
        JDBC_Helper.executeUpdate(statement, "USE `tanks`");
        // Create table if not exists
        JDBC_Helper.executeUpdate(statement, "CREATE TABLE `scoreboard` (\n" +
                "  `id` int(11) NOT NULL,\n" +
                "  `player` varchar(20) COLLATE utf8_unicode_ci NOT NULL,\n" +
                "  `gameId` int(11) NOT NULL,\n" +
                "  `score` int(11) NOT NULL\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;");

        JDBC_Helper.close(conn, statement);

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
