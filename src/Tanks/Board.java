package Tanks;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import javax.imageio.ImageReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class Board {

    private HashMap<Integer, ImageView> current_pos = new HashMap<>();
    private GridPane gameBoard = new GridPane();

    private HashMap<Integer, ImageView> imageViewHashMap = new HashMap<>();

    private int[][] startPositions = {  {4, 0},
                                        {0, 8},
                                        {9, 8},
                                        {4, 15}  };

    private HashMap<Integer, Image> playersTanks = new HashMap<>();
    private HashMap<Integer, Move> playersPos = new HashMap<>();

    private final int BOARD_SIZE_WIDTH = 16;
    private final int BOARD_SIZE_HEIGHT = 10;


    public Board() {


    }

    public class BoardGUI_ {

        public GridPane createBoard() {

            gameBoard.setPrefSize(800, 540);

            for (int i = 0; i < BOARD_SIZE_HEIGHT; i++) {
                for (int j = 0; j < BOARD_SIZE_WIDTH; j++) {

                    Rectangle tile = new Rectangle(49, 53);
                    tile.setFill(Color.DARKGREEN);
                    tile.setStroke(Color.BLACK);

                    Text text = new Text();
                    text.setFont(Font.font(40));

                    ImageView tankImageView = new ImageView();

                    StackPane stackPane = new StackPane(tile, tankImageView);

                    stackPane.setOnMouseClicked(event -> {
                        makeMove(tankImageView, stackPane);
                    });

                    gameBoard.add(stackPane, j, i);

                    if(i == 4 && j == 0)
                        imageViewHashMap.put(0, tankImageView);
                    if(i== 0 && j == 8)
                        imageViewHashMap.put(1, tankImageView);
                    if(i == 9 && j == 8)
                        imageViewHashMap.put(2, tankImageView);
                    if(i == 4 && j == 15)
                        imageViewHashMap.put(3, tankImageView);

                }
            }
            return gameBoard;
        }

        public void drawMove(Text text) {
            text.setText("O");
            text.setFill(Color.BLACK);
        }


    }

    public void addPlayerTank(int playerID, int tankID) {

        //System.out.println("images/TANK0" + (tankID+1) + ".PNG");
        playersTanks.put(playerID, new Image(Main.class.getResourceAsStream("images/TANK0" + tankID + ".PNG")));

    }

    public void resetBoard() {

        for (int i = 0; i < BOARD_SIZE_HEIGHT; i++) {
            for (int j = 0; j < BOARD_SIZE_WIDTH; j++) {

                StackPane pane = (StackPane) getNodeByRowColumnIndex(i, j, gameBoard);

                Rectangle tile = (Rectangle) pane.getChildren().get(0);

                tile.setFill(Color.DARKGREEN);
                tile.setStroke(Color.BLACK);

                ImageView view = (ImageView) pane.getChildren().get(1);

                if(view.getImage() != null)
                    view.setImage(null);

                for(int k = 0; k< playersTanks.size(); k++)
                    setStartPos(k);

            }
        }

    }


    public void makeMove(ImageView image, StackPane mStackPane) {

        // Check of move can be done
        if(checkMove(Main.localPlayer, GridPane.getRowIndex(mStackPane), GridPane.getColumnIndex(mStackPane)) == 0)
            Main.clientConnection.send(new TCP_Message(Main.ACTION_makeMove, Main.localPlayer, new Move(GridPane.getRowIndex(mStackPane), GridPane.getColumnIndex(mStackPane))));
        else if(checkMove(Main.localPlayer, GridPane.getRowIndex(mStackPane), GridPane.getColumnIndex(mStackPane)) == 1)
            Main.clientConnection.send(new TCP_Message(Main.ACTION_playerKilled, Main.localPlayer));

    }

    private int checkMove(Player player, int rowX, int colY) {

        // First check if move made on allowed tile
        if((rowX == playersPos.get(player.getPlayerID()).getX() - 2 || rowX == playersPos.get(player.getPlayerID()).getX() +2) && colY == playersPos.get(player.getPlayerID()).getY() ||
                (colY == playersPos.get(player.getPlayerID()).getY() - 2 || colY == playersPos.get(player.getPlayerID()).getY() + 2) && rowX == playersPos.get(player.getPlayerID()).getX()) {

            // Second, check if player killed other player
            // get node
            StackPane node = (StackPane) getNodeByRowColumnIndex(rowX, colY, gameBoard);
            ImageView view = (ImageView) node.getChildren().get(1);

            if(view.getImage() != null) {
                System.out.println("In checkMove() - killed player!");
                return 1; // killed other player
            }

            return 0; // normal move
        }

        return -1; // bad move

    }

    public void showPossibleMoves(int rowX, int colY, boolean show) {

        StackPane node;
        Rectangle tile;

        //get Image view
        node = (StackPane) getNodeByRowColumnIndex(rowX, colY, gameBoard);
        ImageView view = (ImageView) node.getChildren().get(1);

        if(view.getImage() != null) {

            // up
            node = (StackPane) getNodeByRowColumnIndex(rowX - 2, colY, gameBoard);
            if (node != null) {
                tile = (Rectangle) node.getChildren().get(0);
                if(show)
                    tile.setFill(Color.LIGHTYELLOW);
                else
                    tile.setFill(Color.DARKGREEN);
            }

            // down
            node = (StackPane) getNodeByRowColumnIndex(rowX + 2, colY, gameBoard);
            if (node != null) {
                tile = (Rectangle) node.getChildren().get(0);
                if(show)
                    tile.setFill(Color.LIGHTYELLOW);
                else
                    tile.setFill(Color.DARKGREEN);
            }

            // left
            node = (StackPane) getNodeByRowColumnIndex(rowX, colY - 2, gameBoard);
            if (node != null) {
                tile = (Rectangle) node.getChildren().get(0);
                if(show)
                    tile.setFill(Color.LIGHTYELLOW);
                else
                    tile.setFill(Color.DARKGREEN);
            }

            // right
            node = (StackPane) getNodeByRowColumnIndex(rowX, colY + 2, gameBoard);
            if (node != null) {
                tile = (Rectangle) node.getChildren().get(0);
                if(show)
                    tile.setFill(Color.LIGHTYELLOW);
                else
                    tile.setFill(Color.DARKGREEN);
            }
        }


    }

    public void makeMoveXY(Player player, int rowX, int colY) {


        showPossibleMoves(playersPos.get(player.getPlayerID()).getX(), playersPos.get(player.getPlayerID()).getY(), false);

        System.out.println("ID: " + player.getPlayerID());

        StackPane node = (StackPane) getNodeByRowColumnIndex(rowX, colY, gameBoard);
        ImageView image = (ImageView) node.getChildren().get(1);

        if(current_pos.get(player.getPlayerID()) != null)
            current_pos.get(player.getPlayerID()).setImage(null);

        current_pos.replace(player.getPlayerID(), null);

        image.setFitHeight(50);
        image.setFitWidth(45);
        image.setImage(playersTanks.get(player.getPlayerID()));

        current_pos.replace(player.getPlayerID(), image);
        playersPos.replace(player.getPlayerID(), new Move(rowX, colY));

        showPossibleMoves(rowX, colY, true);
    }

    public void setStartPos(int posNum) {

        imageViewHashMap.get(posNum).setFitHeight(50);
        imageViewHashMap.get(posNum).setFitWidth(45);
        imageViewHashMap.get(posNum).setImage(playersTanks.get(posNum));

        current_pos.put(posNum, imageViewHashMap.get(posNum));

        System.out.println("posNUM: " + posNum);

        showPossibleMoves(startPositions[posNum][0], startPositions[posNum][1], true);
        playersPos.put(posNum, new Move(startPositions[posNum][0], startPositions[posNum][1]));

    }

    public Node getNodeByRowColumnIndex (final int row, final int column, GridPane gridPane) {

        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();

        for (Node node : childrens) {
            if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }

    public HashMap<Integer, Image> getPlayersTanks() {
        return playersTanks;
    }


}
