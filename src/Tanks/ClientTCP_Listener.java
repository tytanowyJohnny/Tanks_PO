package Tanks;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;


public class ClientTCP_Listener {

    private ConnectionToServer server;
    private LinkedBlockingQueue<Object> messages;
    private Socket socket;

    private ArrayList<Player> players = new ArrayList<>();

    public ClientTCP_Listener(String IPAddress, int port) throws IOException {

        socket = new Socket(IPAddress, port);
        messages = new LinkedBlockingQueue<Object>();
        server = new ConnectionToServer(socket);

        System.out.println("New Client created!");

        Thread messageHandling = new Thread(() -> {

            while(true) {

                System.out.println("In while..");

                try {

                    Object message = messages.take();

                    TCP_Message inMessage = (TCP_Message) message;

                    System.out.println("Client: Action - " + inMessage.getAction());

                    switch (inMessage.getAction()) {

                        case Main.ACTION_addPlayer:

                            System.out.println("IN ACTION ADD PLAYER");

                            this.players.clear();

                            // split players
                            String[] players = inMessage.getPayload().split(";");

                            for(String playerString : players) {

                                String[] temPlayerDetails = playerString.split(",");
                                this.players.add(new Player(Integer.parseInt(temPlayerDetails[0]), temPlayerDetails[1], Integer.parseInt(temPlayerDetails[2]), Integer.parseInt(temPlayerDetails[3])));
                            }


                            Platform.runLater(() -> {
                                lobbyWindowController.items.clear();
                                for(Player player : this.players)
                                    lobbyWindowController.items.add(player.getPlayerName());
                            });

                            // Assign player instance from server
                            if(Main.localPlayer == null)
                                Main.localPlayer = inMessage.getPlayer();

                            System.out.println("Added player ID: " + Main.localPlayer.getPlayerID());

                            break;

                        case Main.ACTION_startGame:

                            System.out.println("players num: " + this.players.size());

                            Platform.runLater(() -> {

                                // Open a new one
                                Main.mainBoard = new Board();

                                System.out.println("MAIN player ID: " + Main.localPlayer.getPlayerID());

                                GridPane pane = Main.mainBoard.new BoardGUI_().createBoard();

                                Main.mainBoard.addPlayerTank(Main.localPlayer.getPlayerID(), Main.localPlayer.getPlayerTank());

                                for(Player player : this.players)
                                    Main.mainBoard.setStartPos(player.getPlayerID());

                                Scene newGameScene = new Scene(pane, 800, 540);
                                Stage newGameStage = new Stage();

                                newGameStage.setTitle("Tanks - Game!");

                                newGameStage.setScene(newGameScene);
                                newGameStage.show();

                                if(inMessage.getPlayerID() == Main.localPlayer.getPlayerID())
                                    showTurnMessage("You are first! Start the game by making a move.");

                            });

                            break;

                        case Main.ACTION_makeMove:

                            Move playerMove = inMessage.getMove();

                            if(!Main.mainBoard.getPlayersTanks().containsKey(inMessage.getPlayer().getPlayerID()))
                                Main.mainBoard.addPlayerTank(inMessage.getPlayer().getPlayerID(), inMessage.getPlayer().getPlayerTank());

                            Main.mainBoard.makeMoveXY(inMessage.getPlayer(), playerMove.getX(), playerMove.getY());

                            if(inMessage.getPlayer().getPlayerID() != Main.localPlayer.getPlayerID()) {

                                Platform.runLater(() -> {

                                    showTurnMessage("Now it's your turn! Make a move.");

                                });

                            }
                            break;

                        case Main.ACTION_playerKilled:

                            Platform.runLater(() -> {

                                showTurnMessage("Player " + inMessage.getPlayer().getPlayerName() + " earned a point!");

                                Main.mainBoard.resetBoard();
                            });

                            break;

                        default:
                            //
                            break;
                    }

                    System.out.println("Message Received: " + message);

                }

                catch(InterruptedException ignored){ }
            }
        });

        messageHandling.setDaemon(true);
        messageHandling.start();
    }

    private void showTurnMessage(String text) {

        try {
            // Open a new one
            FXMLLoader loader = new FXMLLoader(getClass().getResource("infoMessage.fxml"));
            AnchorPane pane = loader.load();

            infoMessageController infoMessageController = loader.getController();

            infoMessageController.setInfoLabelText(text);

            Scene turnMessageScene = new Scene(pane);
            Stage turnMessageStage = new Stage();

            turnMessageStage.setTitle("It's time to make a move!");

            turnMessageStage.setScene(turnMessageScene);
            turnMessageStage.show();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    private class ConnectionToServer {

        ObjectInputStream in;
        ObjectOutputStream out;
        Socket socket;

        ConnectionToServer(Socket socket) throws IOException {


            this.socket = socket;
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            Thread read = new Thread(() -> {

                while(true){

                    try{

                        //System.out.println("ConnectionToServer reading..");
                        Object obj = in.readObject();
                        messages.put(obj);

                    }
                    catch(Exception ignored){ }
                }

            });

            read.setDaemon(true);
            read.start();
        }

        private void write(Object obj) {

            try{

                out.writeObject(obj);

            }

            catch(IOException e){ e.printStackTrace(); }
        }


    }

    public void send(Object obj) {
        server.write(obj);
    }
}
