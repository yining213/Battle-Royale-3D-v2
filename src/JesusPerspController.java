import java.security.Key;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

public class JesusPerspController implements Runnable{
    final int WIDTH = 1200;
    final int HEIGHT = 750;
    private double distX = 0;
    private double distY = 0;
    
    @FXML
    Pane pane;
    @FXML
    Box box;
    @FXML PerspectiveCamera camera;

    public void initialize() {
        System.out.println("in init");
        pane.getScene().setCamera(camera);
        camera.translateXProperty().set(-WIDTH/2);
        camera.translateYProperty().set(-HEIGHT/2+15);
        camera.translateZProperty().set(0);
    }
    @FXML
    public void mouseScroll(ScrollEvent e){
        // System.out.println("in scroll");
        
        double delta = e.getDeltaY();
        box.translateZProperty().set(box.getTranslateZ() + delta);
        System.out.println(box.getTranslateZ());
    }
    @FXML
    public void mouseMoved(MouseEvent e){
        // System.out.println("moved");
        double curX = e.getSceneX();
        double curY = e.getSceneY();
        distX = curX - WIDTH/2;
        distY = curY - HEIGHT/2;
    }
    @Override
    public void run(){
        while(Math.abs(distX) > 20 && Math.abs(distY) > 20){
            
        }
    }

}
