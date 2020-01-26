package Tanks;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.Statement;
import java.time.chrono.HijrahDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ServerTCP_New {

    public static ArrayList<Player> playersList = new ArrayList<>();
    public static int playerID = -1;

    private int activePlayerID = -1;
    private int gameID = new Random().nextInt(1000);

    private ArrayList<ConnectionToClient> clientList;
    private LinkedBlockingQueue<Object> messages;
    private ServerSocket serverSocket;

    //private ReentrantLock lock = new ReentrantLock();

    public ServerTCP_New(int port) throws IOException {

        clientList = new ArrayList<ConnectionToClient>();
        messages = new LinkedBlockingQueue<Object>();
        serverSocket = new ServerSocket(port);

        Thread accept = new Thread(() -> {

            while(true){

                try{

                    System.out.println("Server up & running");

                    Socket s = serverSocket.accept();
                    clientList.add(new ConnectionToClient(s));

                }

                catch(IOException e){ e.printStackTrace(); }
            }
        });

        accept.setDaemon(true);
        accept.start();

        Thread messageHandling = new Thread(() -> {

            while(true){

                try{

                    Object message = messages.take();

                    TCP_Message inMessage = (TCP_Message) message;

                    switch(inMessage.getAction()) {

                        case Main.ACTION_addPlayer:

                            // Create new player
                            playerID++;
                            Player tempPlayer = new Player(playerID, inMessage.getPlayerUsername(), inMessage.getPlayerTank(), inMessage.getPlayerHost());
                            playersList.add(tempPlayer.getPlayerID(), tempPlayer);

                            // Respond to clients with already created player & players list
                            TCP_Message sendMessage = new TCP_Message(Main.ACTION_addPlayer, tempPlayer, playersToString());

                            System.out.println("Server players list: " + playersList.toString());

                            sendToAll(sendMessage);

                            break;

                        case Main.ACTION_startGame:

                            // Choose which player is first
                            Random randomGenerator = new Random();
                            int rndIndx = randomGenerator.nextInt(playersList.size());

                            activePlayerID = rndIndx;

                            System.out.println("Active player: " + rndIndx);

                            sendToAll(new TCP_Message(Main.ACTION_startGame, rndIndx));

                            // Schedule end of game
                            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

                            Runnable endGame = () -> {
                                sendToAll(new TCP_Message(Main.ACTION_endGame, playersToString()));
                                executor.shutdown();

                                // Save results to mysql database
                                Connection connection = JDBC_Helper.connect();
                                Statement statement = JDBC_Helper.statement(connection);

                                JDBC_Helper.executeUpdate(statement, "USE tanks;");

                                for(Player player : playersList)
                                    JDBC_Helper.executeUpdate(statement, "INSERT INTO `scoreboard` (player, gameId, score) VALUES ('" + player.getPlayerName() + "', " + gameID + ", " + player.getPlayerScore() + ")");

                                JDBC_Helper.close(connection, statement);
                            };

                            executor.schedule(endGame, 108, TimeUnit.SECONDS);

                            break;

                        case Main.ACTION_makeMove:

                            System.out.println(activePlayerID);

                            if(inMessage.getPlayer().getPlayerID() == activePlayerID) {

                                sendToAll(new TCP_Message(Main.ACTION_makeMove, inMessage.getPlayer(), inMessage.getMove()));

                                // Pass control to different player
                                if (activePlayerID + 1 >= playersList.size())
                                    activePlayerID = 0;
                                else
                                    activePlayerID++;
                            }

                            break;

                        case Main.ACTION_playerKilled:

                            System.out.println(inMessage.getPlayer().getPlayerName() + "earned a point!");

                            addPoint(inMessage.getPlayer().getPlayerID());

                            sendToAll(new TCP_Message(Main.ACTION_playerKilled, inMessage.getPlayer()));

                            break;

                        default:
                            ////
                            break;

                    }

                }
                catch(InterruptedException e){

                    e.printStackTrace();
                }
            }
        });

        messageHandling.setDaemon(true);
        messageHandling.start();
    }

    private void addPoint(int playerID) {

        for(Player player : playersList)
            if(player.getPlayerID() == playerID)
                player.setPlayerScore(player.getPlayerScore() + 1);

    }

    private class ConnectionToClient {

        ObjectInputStream in;
        ObjectOutputStream out;
        Socket socket;
        int connectionID;

        ConnectionToClient(Socket socket) throws IOException {
            this.socket = socket;
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

            Thread read = new Thread(){
                public void run(){

                    while(true){

                        try{

                            Object obj = in.readObject();
                            messages.put(obj);

                        }
                        catch(Exception e){ e.printStackTrace(); }
                    }
                }
            };

            read.setDaemon(true); // terminate when main ends
            read.start();
        }

        public void write(Object obj) {
            try{
                out.writeObject(obj);
            }
            catch(IOException e){ e.printStackTrace(); }
        }

        public void assignConnectionID(int mID) {
            this.connectionID = mID;
        }
    }

    public void sendToOne(int index, Object message)throws IndexOutOfBoundsException {
        clientList.get(index).write(message);
    }

    public void sendToAll(Object message){

        for(ConnectionToClient client : clientList)
            client.write(message);

    }

    private String playersToString() {

        StringBuilder temp = new StringBuilder();

        for(int i=0; i<playersList.size(); i++) {

            StringBuilder temPlayerString = new StringBuilder();

            temPlayerString.append(playersList.get(i).getPlayerID()).append(',')
                    .append(playersList.get(i).getPlayerName()).append(',')
                    .append(playersList.get(i).getPlayerTank()).append(',')
                    .append(playersList.get(i).isHost()).append(',')
                    .append(playersList.get(i).getPlayerScore());

            if(i!=0)
                temp.append(";").append(temPlayerString);
            else
                temp.append(temPlayerString);
        }

        return temp.toString();
    }

}