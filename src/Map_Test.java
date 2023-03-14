import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class Map_Test extends Scene{
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 750;
    BorderPane bdPane = new BorderPane();
    Scene subscene;
    private final Rotate rotateX = new Rotate(-20, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(-20, Rotate.Y_AXIS);
    public Map_Test(Stage stage){
        super(new SmartGroup(), WIDTH, HEIGHT, true);
        Box box = new Box(5, 5, 5);
        box.setMaterial(new PhongMaterial(Color.GREENYELLOW));
    

        stage.setScene(this);

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.getTransforms().addAll (
            rotateX, 
            rotateY, 
            new Translate(100, 100, 0)
        );
        SimpleDoubleProperty angleY = new SimpleDoubleProperty();
        rotateY.angleProperty().bind(angleY);

        camera.setNearClip(0.1);
        camera.setFarClip(10000);
        World world = new World("src/Map/map_1.obj");
        Group root3D = new Group(camera,world);
        SubScene subScene = new SubScene(root3D, 1200, 750, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.ANTIQUEWHITE);
        subScene.setCamera(camera);
        
        
        SmartGroup g = (SmartGroup)getRoot();
        Pane pane = new Pane(subScene);
        
        Circle c = new Circle(20);
        c.setLayoutX(WIDTH/2);
        c.setLayoutY(HEIGHT/2);
        pane.getChildren().add(c);
        // BorderPane pane = new BorderPane();
        // bPane.setCenter(subScene);
        // BorderPane bPane = new BorderPane();
        // bPane.setCenter(pane);

        addEventHandler(KeyEvent.KEY_PRESSED, e->{
            System.out.println("in press");
            if(e.getCode() == KeyCode.RIGHT){
                angleY.set(angleY.get() + 5);
                c.setCenterX(c.getCenterX()-5);
            }
            else if(e.getCode() == KeyCode.LEFT){
                angleY.set(angleY.get() - 5);
                c.setCenterX(c.getCenterX()+5);
            }
            else if(e.getCode() == KeyCode.DOWN){
                camera.setTranslateZ(camera.getTranslateZ() - 10);
            }
        });
        
        g.getChildren().add(pane);
        // stage.setScene(this);
        System.out.println(subScene.getScene().getWindow().getHeight());
    }
}
