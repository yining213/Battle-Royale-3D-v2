import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class Camera3D extends Application {
    static public SceneController sceneController; 
    static final int WIDTH = 1200;
    static final int HEIGHT = 750;
    static Music music_src = new Music();

    @Override
    public void start(Stage stage) throws Exception {
        music_src.music.get("Home").play();
        Home home_scene = new Home(stage);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}