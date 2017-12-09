
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author claudio
 */
public class PingPong extends Application {

    public static void main(String[] args) {
        // TODO code application logic here

        Application.launch(args);

    }
    Scene scene;

    Pane pane;
    Rectangle player1;
    Rectangle player2;
    Ellipse ball;
    Group group = new Group();

    Label score1L;
    Label score2L;
    final MenuBar menubar = new MenuBar();
    boolean ende = false;
    boolean anfang = false;
    Label message = new Label();

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("PING PONG");
        primaryStage.setMinHeight(Var.height);
        primaryStage.setMinWidth(Var.width);

        pane = new Pane(group);

        pane.setPrefSize(Var.width, Var.height);
        pane.setStyle("-fx-background-color: black;");

        Line line = new Line(Var.width / 2, 0, Var.width / 2, Var.height);
        line.setStroke(Color.WHITE);

        score1L = new Label("" + Var.score1);
        score2L = new Label("" + Var.score2);

        score1L.setTextFill(Color.WHITE);
        score1L.setLayoutX((Var.width / 2) - 50);
        score1L.setLayoutY(menubar.getHeight() + 50);
        score1L.setFont(new Font("Impact", 37));
        score2L.setTextFill(Color.WHITE);
        score2L.setLayoutX((Var.width / 2) + 25);
        score2L.setLayoutY(menubar.getHeight() + 50);
        score2L.setFont(new Font("Impact", 37));

        //ANFANG ENDE 
        message.setTextFill(Color.WHITE);
        message.setLayoutX((Var.width / 2) - 110);
        message.setLayoutY(Var.height / 2);
        message.setFont(new Font("Impact", 37));

        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
 
                anfang = true;
                ende = true;
                group.getChildren().remove(message);
            }
        });
        //   <----

        group.getChildren().addAll(line, score1L, score2L);

        scene = new Scene(pane, Var.width, Var.height, Color.BLACK);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        //MENU 
        Menu menuFile = new Menu("_File");
        Menu menuEdit = new Menu("_Edit");

        MenuItem neu = new MenuItem("New Game");
        neu.setAccelerator(KeyCombination.keyCombination("shortcut+N"));
        MenuItem close = new MenuItem("Close");
        close.setAccelerator(KeyCombination.keyCombination("shortcut+C"));

        MenuItem level = new MenuItem("Level");
        level.setAccelerator(KeyCombination.keyCombination("shortcut+L"));

        //New Event
        neu.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {

                reset();
                anfang = false;
            }
        });

        //Exit Event
        close.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                System.exit(0);
            }
        });

        menuEdit.getItems().addAll(level);
        menuFile.getItems().addAll(neu, close);
        menubar.getMenus().addAll(menuFile, menuEdit);
        menubar.prefWidthProperty().bind(primaryStage.widthProperty());
        pane.getChildren().add(menubar);

        player1 = new Rectangle(Var.PLAYER_WIDTH, Var.player_height, Color.WHITE);
        player2 = new Rectangle(Var.PLAYER_WIDTH, Var.player_height, Color.WHITE);
        player2.setX(pane.getWidth() - Var.PLAYER_WIDTH);
        player2.setY(pane.getHeight() / 2);
        group.getChildren().add(player1);
        group.getChildren().add(player2);

        ball = new Ellipse(Var.width / 2, Var.height / 2, Var.radius, Var.radius);
        ball.setFill(Color.WHITE);
        group.getChildren().add(ball);

        primaryStage.show();

        new AnimationTimer() { //animate all circles
            @Override
            public void handle(long now) {

                anfangEnde();

                if (anfang && ende) {
                    playerAnimation();
                    kunstlicheIntelligenz();
                    ballAnimation();
                };

            }
        }.start();

    }

    public void playerAnimation() {

        pane.setOnMouseMoved(e -> Var.playerOneYPos = e.getY());

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.A) {
                Var.playerOneYPos = Var.playerOneYPos + Var.geschwindigkeit;
            } else if (e.getCode() == KeyCode.Q) {
                Var.playerOneYPos = Var.playerOneYPos - Var.geschwindigkeit;

            }
        });

        if (player1.getY() > Var.height - Var.player_height) {
            Var.playerOneYPos = Var.height - Var.player_height / 2;

        }
        if (Var.playerOneYPos < 80) {
            Var.playerOneYPos = 80;

        }

        player1.setY(Var.playerOneYPos - (Var.player_height / 2));

        try {
            Thread.sleep(10);

        } catch (InterruptedException e) {
        }

    }

    public void ballAnimation() {
        ball.setCenterX(ball.getCenterX() + Var.ballGeschwindigkeitX);
        ball.setCenterY(ball.getCenterY() + Var.ballGeschwindigkeitY);

        if (ball.getCenterY() + ball.getRadiusY() >= pane.getHeight()) {
            Var.ballGeschwindigkeitY *= -1;
        } else if (ball.getCenterY() - ball.getRadiusY() <= 0 + menubar.getHeight()) {
            Var.ballGeschwindigkeitY *= -1;
        } else if (((ball.getCenterX() + ball.getRadiusX() > player2.getX()) && ball.getCenterY() >= player2.getY() && ball.getCenterY() <= player2.getY() + Var.player_height)
                || ball.getCenterX() - ball.getRadiusX() <= player1.getX() + Var.PLAYER_WIDTH && ball.getCenterY() >= player1.getY() && ball.getCenterY() <= player1.getY() + Var.player_height) {

            effektBall();
            Var.ballGeschwindigkeitX *= -1.03;

        }

        if (ball.getCenterX() >= pane.getWidth()) {
            Var.score1 += 1;
            score1L.setText("" + Var.score1);

            Var.ballGeschwindigkeitX = 2;
            Var.ballGeschwindigkeitY = 2;

            ball.setCenterX(Var.width / 2);
            ball.setCenterY(Var.height / 2);

        } else if (ball.getCenterX() <= 0) {
            Var.score2 += 1;
            score2L.setText("" + Var.score2);
            Var.ballGeschwindigkeitX = -2;
            Var.ballGeschwindigkeitY = 2;

            ball.setCenterX(Var.width / 2);
            ball.setCenterY(Var.height / 2);

        }

    }

    private void effektBall() {

        Rectangle player;
        double koeffizient = 0;

        if (Var.ballGeschwindigkeitX > 0) {
            player = player2;
        } else {
            player = player1;
        }

        koeffizient = ball.getCenterY() - (player.getY() + player.getY() + Var.player_height) / 2;

        Var.ballGeschwindigkeitY = koeffizient / 10;

    }

    public void kunstlicheIntelligenz() {

        double center = (player2.getY() + (player2.getY() + Var.player_height)) / 2;

        if (ball.getCenterX() > Var.width / 2 && Var.ballGeschwindigkeitX > 0) {

            if (center + 15 < ball.getCenterY() && center < Var.height - Var.player_height / 2) {
                player2.setY(player2.getY() + Var.geschwindigkeit);

            } else if (center - 15 > ball.getCenterY() && center > 0 + Var.player_height / 2 + menubar.getHeight()) {
                player2.setY(player2.getY() - Var.geschwindigkeit);

            }
        }

    }

    private void anfangEnde() {

        if (Var.score1 >= Var.maxPunktzahl | Var.score2 >= Var.maxPunktzahl) {
            ende = false;
            reset();

        }

        if (!anfang) {
            message.setText("Click To Start!");
            try {
                group.getChildren().add(message);
            } catch (java.lang.IllegalArgumentException e) {
                //exception fangen wegen children mehrmals in schleife geaddet
            }
        } else if (!ende) {
            message.setText("GAME OVER!");
            try {
                group.getChildren().add(message);
            } catch (java.lang.IllegalArgumentException e) {
                //exception fangen wegen children mehrmals in schleife geaddet
            }
        }

    }

    private void reset() {
        Var.score1 = 0;
        Var.score2 = 0;
        score2L.setText("" + Var.score2);
        score1L.setText("" + Var.score1);

        Var.ballGeschwindigkeitX = 2;
        Var.ballGeschwindigkeitY = 2;

        ball.setCenterX(Var.width / 2);
        ball.setCenterY(Var.height / 2);
    }

}
