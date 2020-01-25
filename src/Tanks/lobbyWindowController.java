package Tanks;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;

public class lobbyWindowController implements Initializable {

    //private ServerTCP newTankServer;

    public static ObservableList<String> items = FXCollections.observableArrayList ();

    @FXML
    private ListView<String> lobbyListView;

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

            try {
                // Open a new one
                FXMLLoader loader = new FXMLLoader(getClass().getResource("infoMessage.fxml"));
                AnchorPane pane = loader.load();

                infoMessageController infoMessageController = loader.getController();

                infoMessageController.setInfoLabelText("Brak wystarczającej ilości graczy do rozpoczęcia gry!");

                Scene turnMessageScene = new Scene(pane);
                Stage turnMessageStage = new Stage();

                turnMessageStage.setTitle("It's time to make a move!");

                turnMessageStage.setScene(turnMessageScene);
                turnMessageStage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            // Send information that game has been started
            Main.clientConnection.send(new TCP_Message(Main.ACTION_startGame));
        }

        //Stage.getWindows().stream().filter(Window::isShowing).close();

    }

    public void addPlayer(String mUsername, int mIsHost, int mUserSelectedTank) {

        // Add new player at server side
        try {

            System.out.println(InetAddress.getByName("localhost").toString().split("/")[1]);

            Main.clientConnection = new ClientTCP_Listener(InetAddress.getByName("localhost").toString().split("/")[1], 4848);
            Main.clientConnection.send(new TCP_Message(Main.ACTION_addPlayer, mUsername, mIsHost, mUserSelectedTank));

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
}
