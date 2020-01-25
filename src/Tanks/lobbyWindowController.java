package Tanks;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.awt.*;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;

public class lobbyWindowController implements Initializable {

    //private ServerTCP newTankServer;

    public static ObservableList<String> items = FXCollections.observableArrayList ();

    @FXML
    public ListView<String> lobbyListView;

    @FXML
    private Button lobbyStartGame;

    public lobbyWindowController() {


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if(Main.isHost != 0) {

            lobbyListView.setItems(items);

            // Start new server thread
            try {

                new ServerTCP_New(4848);

            } catch (IOException e) {

                e.printStackTrace();

            }

        } else {

            lobbyListView.setItems(items);

        }
    }

    @FXML
    protected void handleLobbyStartButton(ActionEvent ae) {

        if(items.size() < 2) {
            showInfoMessage("Brak wystarczającej ilości graczy do rozpoczęcia gry!");
        } else {

            // Send information that game has been started
            Main.clientConnection.send(new TCP_Message(Main.ACTION_startGame));
        }

        //Stage.getWindows().stream().filter(Window::isShowing).close();

    }

    private void showInfoMessage(String text) {

        try {

            // Get X & Y
            Stage mainWindow = (Stage) lobbyListView.getScene().getWindow();

            double x = mainWindow.getX();
            double y = mainWindow.getY();

            // Open a new one
            FXMLLoader loader = new FXMLLoader(getClass().getResource("infoMessage.fxml"));
            AnchorPane pane = loader.load();

            infoMessageController infoMessageController = loader.getController();

            infoMessageController.setInfoLabelText(text);

            Scene turnMessageScene = new Scene(pane);
            Stage turnMessageStage = new Stage();

            turnMessageStage.setTitle("It's time to make a move!");

            turnMessageStage.setScene(turnMessageScene);
            turnMessageStage.setX(x);
            turnMessageStage.setY(y);
            turnMessageStage.show();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public void addPlayer(String mUsername, int mIsHost, int mUserSelectedTank) {

        // Add new player at server side
        try {

            System.out.println(InetAddress.getByName("localhost").toString().split("/")[1]);

            try {
                Main.clientConnection = new ClientTCP_Listener(InetAddress.getByName("localhost").toString().split("/")[1], 4848);
                Main.clientConnection.send(new TCP_Message(Main.ACTION_addPlayer, mUsername, mIsHost, mUserSelectedTank));
            } catch (ConnectException e) {

                Parent root = FXMLLoader.load(getClass().getResource("startWindow.fxml"));
                Scene mainScene = new Scene(root, 800, 550);

                Stage primaryStage = new Stage();

                primaryStage.setTitle("Tanks - Bartosz Kubacki & Bartłomiej Urbanek");
                //primaryStage.setFullScreen(true);
                primaryStage.setScene(mainScene);
                primaryStage.show();

                showInfoMessage("Aktualnie nie trwają zapisy do żadnej rozgrywki!");

                // Close previous Window
                Stage mainWindow = (Stage) lobbyListView.getScene().getWindow();
                mainWindow.close();

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
}
