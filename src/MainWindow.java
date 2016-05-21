import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Created by Paul on 5/25/2015.
 */
public class MainWindow extends Application {

    Canvas canvas;

    private int frameSkipCounter = 0;


    private Map firstMap = new Map();

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     * <p/>
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set. The primary stage will be embedded in
     *                     the browser if the application was launched as an applet.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages and will not be embedded in the browser.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {



        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();


        //set Stage boundaries to visible bounds of the main screen
        primaryStage.setFullScreen(true);
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());


        canvas = new Canvas(primaryScreenBounds.getWidth(),primaryScreenBounds.getHeight() + 40);




        Group group = new Group(canvas);

        Scene scene = new Scene(group);

        Timer timer = new Timer(1000/60, new timerHandler());

        timer.start();



        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });



        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                if (ke.getCode().toString().equals(Constants.leftKey)) {
                    firstMap.setMainPlayerMovingLeft(true);
                }
                if (ke.getCode().toString().equals(Constants.rightKey)) {
                    firstMap.setMainPlayerMovingRight(true);
                }
                if (ke.getCode().toString().equals(Constants.jumpKey)) {
                    firstMap.mainPlayerJump();
                }

                if (ke.getCode().toString().equals(Constants.primaryAttackKey)) {
                    firstMap.setAttacking(true);
                }
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                //System.out.println("Key Pressed: " + ke.getCode());
                if (ke.getCode().toString().equals(Constants.leftKey)) {
                    firstMap.setMainPlayerMovingLeft(false);
                }
                if (ke.getCode().toString().equals(Constants.rightKey)) {
                    firstMap.setMainPlayerMovingRight(false);
                }
                if (ke.getCode().toString().equals(Constants.jumpKey)) {
                    firstMap.mainPlayerReleaseJump();
                }
                if (ke.getCode().toString().equals(Constants.primaryAttackKey)) {
                    firstMap.setAttacking(false);
                }
                if (ke.getCode().toString().equals("Q")) {
                    timer.getActionListeners()[0].actionPerformed(new ActionEvent(this, 1, "Hello"));
                }

            }
        });


        primaryStage.show();
    }


    private class timerHandler implements ActionListener {
        public void actionPerformed(ActionEvent event) {

            Constants.addFrame();

            if (Constants.framesPerSecond != 60) {
                frameSkipCounter++;
                if (frameSkipCounter >= 60 / Constants.framesPerSecond) {
                    frameSkipCounter = 0;
                }
            } else {
                frameSkipCounter = 0;
            }

            GraphicsShell graphicsShell;

            if (frameSkipCounter == 0) {
                GraphicsContext g = canvas.getGraphicsContext2D();

                graphicsShell = new GraphicsShell(g);

                g.clearRect(0, 0, g.getCanvas().getWidth(), g.getCanvas().getHeight());

                firstMap.step(graphicsShell);

                HeadsUpDisplay.display(g);
            } else {
                graphicsShell = new GraphicsShell();

                firstMap.step(graphicsShell);
            }




        }
    }



    public static void main(String[] args) {
        Application.launch(args);
    }
}
