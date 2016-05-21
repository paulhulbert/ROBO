import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

/**
 * Created by Paul on 5/31/2015.
 */
public class HeadsUpDisplay {


    public static void display(GraphicsContext graphicsShell) {

        graphicsShell.setFill(Color.RED);
        graphicsShell.fillRect(Screen.getPrimary().getBounds().getMaxX() / 2 - 50, Screen.getPrimary().getBounds().getMaxY() - 30,
                Constants.mainPlayerLife, 30);

        graphicsShell.strokeText(String.format("%02d:%02d:%02d ", Constants.hourCount, Constants.minuteCount, Constants.secondCount), 1000, 100);
        graphicsShell.strokeText(" " + Constants.framesPerSecond, 1000, 150);
    }


}
