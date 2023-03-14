import java.util.Vector;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Camera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class PauseController {
    @FXML
    Label resume_btn;
    @FXML
    Label quit_btn;
    @FXML Pane pane;
    @FXML Label sound;
    Stage stage;
    FirstScene f;
    Player player;
    Vector<Enemy> eVector;
    World world;
    public void initialize() {
        if(Home.musicOn){
            sound.setStyle("-fx-background-image: url(images/soundBlack.png); ");
        }
        else{
            sound.setStyle("-fx-background-image: url(images/soundoffblack.png); ");
        }
        
        // titleLb.setFont(Font.loadFont(BR3dController.class.getResourceAsStream("fonts/Melted Monster.ttf"), 80));
        // Font impact2 = Font.loadFont(Menu.class.getResourceAsStream("fonts/Impacted2.0.ttf"), 20);
    }
    public void setInfo(Stage stage, Player p, Vector<Enemy> eVec, FirstScene f, World w){
        this.stage = stage;
        this.player = p;
        this.eVector = eVec;
        this.f = f;
        world = w;
        stage.setOnCloseRequest(e->{
            stage.close();
            f.pause = false;
            f.resumeAll();
            player.resumeAll();
            for(Enemy en: eVector) en.resumeAll();
            world.resumeAll();
            
            Camera3D.music_src.music.get("Pause").stop();
            if(Home.musicOn){
                if(Menu.timerMode) Camera3D.music_src.music.get("Man_Timer").play();
                else Camera3D.music_src.music.get("Man_Survival").play();
            }
        });
    }
    
    public void btnPressed(MouseEvent e){
        if(e.getSource() == resume_btn){
            // System.out.println("resume");
            stage.close();
            f.pause = false;
            f.resumeAll();
            player.resumeAll();
            for(Enemy en: eVector) en.resumeAll();
            world.resumeAll();
            
            Camera3D.music_src.music.get("Pause").stop();
            if(Home.musicOn){
                if(Menu.timerMode) Camera3D.music_src.music.get("Man_Timer").play();
                else Camera3D.music_src.music.get("Man_Survival").play();
            }
        }
        else if(e.getSource()==quit_btn){
            // System.out.println("quit");
            stage.close();
            f.backToMenu();
            Camera3D.music_src.music.get("Pause").stop();
            if(Menu.timerMode) Camera3D.music_src.music.get("Man_Timer").stop();
            else Camera3D.music_src.music.get("Man_Survival").stop();
            if(Home.musicOn){
                Camera3D.music_src.music.get("Menu").play();
            }
        }
        else if(e.getSource() == sound){
            if(Home.musicOn){
                Home.musicOn = false;
                sound.setStyle("-fx-background-image: url(images/soundoffblack.png); ");
                Camera3D.music_src.music.get("Pause").pause();
            }
            else{
                Home.musicOn = true;
                sound.setStyle("-fx-background-image: url(images/soundBlack.png); ");
                Camera3D.music_src.music.get("Pause").play();
            }
        }
    }
    
}
