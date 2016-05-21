import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

/**
 * Created by Paul on 5/30/2015.
 */
public class GraphicsShell {


    GraphicsContext graphicsContext;

    private boolean isActive = true;


    public GraphicsShell(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }

    public GraphicsShell() {
        isActive = false;
    }


    public void drawImage(Image image, double x, double y, double width, double height) {
        if (!isActive) {
            return;
        }
        graphicsContext.drawImage(image, x + Constants.getXShift(), y + Constants.getYShift(), width, height);
    }

    public void fillRect(double x, double y, double width, double height) {
        if (!isActive) {
            return;
        }
        graphicsContext.fillRect(x + Constants.getXShift(), y + Constants.getYShift(), width, height);

    }


    public void setFill(Color color) {
        if (!isActive) {
            return;
        }
        graphicsContext.setFill(color);
    }
}
