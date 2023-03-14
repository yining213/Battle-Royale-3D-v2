import java.util.Vector;

import javafx.application.Application;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class Pause{
    public Pause(Stage stage, Player p, Vector<Enemy> e, FirstScene firstScene, World w){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("pause.fxml"));
            Scene pause_scene = new Scene(loader.load());
            PauseController controller = loader.getController();
            controller.setInfo(stage, p, e, firstScene, w);
            stage.setTitle("PAUSE");
            stage.setResizable(false);
            stage.setScene(pause_scene);
            stage.show();
        }catch(Exception error){
            System.err.println(error
            );
        }
    }
    
}