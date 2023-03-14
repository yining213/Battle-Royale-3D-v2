import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.URL;

import javax.security.auth.x500.X500Principal;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Home extends Scene{
    static final int WIDTH = 1200;
    static final int HEIGHT = 750;
    static boolean musicOn = true;
    Label music_lb = new Label();
    public Home(Stage stage){
        super(new Group(), WIDTH, HEIGHT);
        Group group = (Group)getRoot();

        Text title = new Text("BATTLE ROYALE 3D II");
        Text press_space = new Text("Press SPACE to Continue");
        title.setFont(Font.loadFont(BR3dController.class.getResourceAsStream("fonts/Melted Monster.ttf"), 90));
        press_space.setFont(Font.loadFont(BR3dController.class.getResourceAsStream("fonts/PressStart2P.ttf"), 20));
        title.setLayoutX(getWidth()/2 - title.getLayoutBounds().getWidth()/2);
        title.setLayoutY(getHeight()/2 - title.getLayoutBounds().getHeight());
        press_space.setLayoutX(getWidth()/2 - press_space.getLayoutBounds().getWidth()/2);
        press_space.setLayoutY(getHeight() - press_space.getLayoutBounds().getHeight()*10);
        title.setFill(Color.WHITE);
        press_space.setFill(Color.WHITE);
        Image imageOn = new Image(getClass().getResourceAsStream("images/sound.png"));
        Image imageoff = new Image(getClass().getResourceAsStream("images/soundoff.png"));
        if(musicOn) music_lb.setGraphic(new ImageView(imageOn));
        else music_lb.setGraphic(new ImageView(imageoff));
        music_lb.setPrefWidth(50);
        music_lb.setPrefHeight(50);
        music_lb.setLayoutX(getWidth()-music_lb.getWidth()-120);
        music_lb.setLayoutY(getHeight() - music_lb.getHeight() - 120);
        music_lb.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
            if(musicOn){
                musicOn = false;
                music_lb.setGraphic(new ImageView(imageoff));
                Camera3D.music_src.music.get("Home").pause();
                
            }
            else{
                music_lb.setGraphic(new ImageView(imageOn));
                musicOn = true;
                Camera3D.music_src.music.get("Home").play();
            }
        });
        group.getChildren().addAll(title, press_space, music_lb);
        
        setFill(Color.BLACK);
        stage.setScene(this);
        stage.show();

        addEventHandler(KeyEvent.KEY_PRESSED, e->{
            if(e.getCode() == KeyCode.SPACE){
                try{
                    if(musicOn) Camera3D.music_src.effect.get("Click").play();
                    stage.setTitle("MENU");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));
                    Scene menu_scene = new Scene(loader.load());
                    Menu controller = loader.getController();
                    controller.setStage(stage);
                    stage.setScene(menu_scene);
                    stage.show();
                    Camera3D.music_src.music.get("Home").stop();
                    if(musicOn){
                        Camera3D.music_src.music.get("Menu").play();
                    }
                    
                }catch(Exception exc){
                    System.out.println(exc);
                }
            }
        });
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            // System.out.println("window width changed: " + stage.getWidth());
            title.setLayoutX(getWidth()/2 - title.getLayoutBounds().getWidth()/2);
            press_space.setLayoutX(getWidth()/2 - press_space.getLayoutBounds().getWidth()/2);
        });
        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            // System.out.println("window height changed: " + stage.getHeight());
            title.setLayoutY(getHeight()/2 - title.getLayoutBounds().getHeight());
            press_space.setLayoutY(getHeight() - press_space.getLayoutBounds().getHeight()*10);
        });
    }
}
