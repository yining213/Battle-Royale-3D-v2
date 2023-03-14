import java.util.HashMap;
import javafx.scene.Scene;
import javafx.stage.*;
import javafx.scene.layout.Pane;

public class SceneController{
    private HashMap<String, Scene> sceneMap = new HashMap<>();
    private Stage stage;
    public SceneController(Stage stage){
        this.stage = stage;
    }
    protected void add(String name, Scene scene){
        sceneMap.put(name, scene);
    }

    protected void remove(String name){
        sceneMap.remove(name);
    }
    protected void activate(String name){
        System.out.println("activate");
        stage.setScene(sceneMap.get(name));
        stage.show();
    }
}