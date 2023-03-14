import javafx.event.ActionEvent; 
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton; 
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent; 
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color; 
import javafx.scene.paint.Paint; 
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BR3dController extends Scene{ 
    static final int WIDTH = 1200;
    static final int HEIGHT = 750;
    public BR3dController(Stage stage){
        super(new Group(), WIDTH, HEIGHT);
        Group group = (Group)getRoot();
        
        Text title = new Text("BATTLE ROYALE 3D II");
        Text press_space = new Text("Press SPACE to Continue");
        title.setFont(Font.loadFont(BR3dController.class.getResourceAsStream("fonts/Impacted2.0.ttf"), 80));
        press_space.setFont(Font.loadFont(BR3dController.class.getResourceAsStream("fonts/Impacted2.0.ttf"), 20));
        title.setLayoutX(getWidth()/2 - title.getLayoutBounds().getWidth()/2);
        title.setLayoutY(getHeight()/2 - title.getLayoutBounds().getHeight()/2);
        press_space.setLayoutX(getWidth()/2 - press_space.getLayoutBounds().getWidth()/2);
        press_space.setLayoutY(getHeight()/2 - press_space.getLayoutBounds().getHeight()/2);

        group.getChildren().addAll(title, press_space);
        stage.setScene(this);

        addEventHandler(KeyEvent.KEY_PRESSED, e->{
            if(e.getCode() == KeyCode.SPACE){
                try{
                    stage.setTitle("MENU");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));
                    Scene menu_scene = new Scene(loader.load());
                    Menu controller = loader.getController();
                    controller.setStage(stage);
                    stage.setScene(menu_scene);
                    stage.show();
                    // stage.setTitle("FirstPerspective");
                    // // FXMLLoader loader = new FXMLLoader(getClass().getResource("JesusPersp.fxml"));
                    // // JesusScene scene = new JesusScene(stage);            
                    // FirstScene scene = new FirstScene(stage);            
                }catch(Exception exc){
        
                }
            }
        });
    }
    // @FXML 
    // private Label titleLb;
    // @FXML 
    // private AnchorPane pane;
    // @FXML
    // private Label textLb;
    // private Stage stage;
    // public void initialize() {
    //     System.out.println("in initialize");
    //     titleLb.setFont(Font.loadFont(BR3dController.class.getResourceAsStream("fonts/Impacted2.0.ttf"), 80));
    //     textLb.setFont(Font.loadFont(BR3dController.class.getResourceAsStream("fonts/PressStart2P.ttf"), 20));
    // }
    // @FXML
    // void onMousePressed(MouseEvent e){
    //     System.out.println("mouse");
    //     // stage.close();
        
    //     try{
    //         stage.setTitle("MENU");
    //         FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));
    //         Scene menu_scene = new Scene(loader.load());
    //         Menu controller = loader.getController();
    //         controller.setStage(stage);
    //         stage.setScene(menu_scene);
    //         // stage.setTitle("FirstPerspective");
    //         // // FXMLLoader loader = new FXMLLoader(getClass().getResource("JesusPersp.fxml"));
    //         // // JesusScene scene = new JesusScene(stage);            
    //         // FirstScene scene = new FirstScene(stage);            
    //     }catch(Exception exc){

    //     }
        
    // }
    // void setStage(Stage stage){
    //     this.stage = stage;
    // }
    // @FXML
    // void onKeyPressed(KeyEvent event) {
    //     System.out.println("in key event");
    //     // if(event.getCode().equals(KeyCode.ENTER)) {
    //     //     System.out.println("space pressed");
    //     // }
    // }
    // // handles drawingArea's onMouseDragged MouseEvent
    
}