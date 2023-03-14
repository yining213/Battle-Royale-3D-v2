import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Menu {
    @FXML
    BorderPane bPane;
    @FXML
    Button rightArrow_btn;
    @FXML
    Button leftArrow_btn;
    @FXML
    ImageView imageView;
    @FXML
    Label home_lb;
    @FXML
    Label play_lb;
    @FXML
    Label exit_lb;
    @FXML Label timer_lb, survival_lb;
    @FXML Label on_lb, off_lb;
    
    Stage stage;
    Image images[];
    int mapNo = 0;
    World world[] = {
        new World("Map/map_1.obj"),
        new World("Map/map_2.obj"),
        new World("Map/map_3.obj"),
    };
    static boolean timerMode = true;
    public void initialize() {
        loadMapImages();
        timer_lb.setTextFill(Color.RED);
        if(Home.musicOn) on_lb.setTextFill(Color.RED);
        else off_lb.setTextFill(Color.RED);
    }
    public void setStage(Stage stage){
        this.stage = stage;
        bPane.setPrefWidth(stage.getScene().getWidth());
        bPane.setPrefHeight(stage.getScene().getHeight());
    }
    public void btnPressed(MouseEvent e){
        if(Home.musicOn) Camera3D.music_src.effect.get("Click").play();
        if(e.getSource() == rightArrow_btn){
            mapNo = (mapNo+1)%3;
            imageView.setImage(images[mapNo]);
        }
        else if(e.getSource() == leftArrow_btn){
            if(mapNo==0) mapNo = 2;
            else mapNo-=1;
            imageView.setImage(images[mapNo]);
        }
    }
    public void lbPressed(MouseEvent e){
        // Home.music_src.music.get("click").stop();
        if(Home.musicOn) Camera3D.music_src.effect.get("Click").play();
        // System.out.println("press");
        // music_src.music.get("click").play();
        if(e.getSource() == home_lb){
            Home home_scene = new Home(stage);
            if(Home.musicOn) Camera3D.music_src.music.get("Home").play();
            Camera3D.music_src.music.get("Menu").stop();
        }
        else if(e.getSource() == play_lb){
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("load.fxml"));
                Scene load_scene = new Scene(loader.load());
                Load loadController = loader.getController();
                loadController.setInfo(stage, world[mapNo], this);
                stage.setScene(load_scene);
                loadController.setPressEvent();
                stage.setTitle("Loading");
                Camera3D.music_src.music.get("Menu").stop();
                if(Home.musicOn) Camera3D.music_src.music.get("Loading").play();
            }
            catch(IOException ex){
                System.err.println(ex);
            }
            
        }
        else if(e.getSource() == exit_lb){
            stage.close();
        }
        else if(e.getSource() == timer_lb){
            timer_lb.setTextFill(Color.RED);
            survival_lb.setTextFill(Color.WHITE);
            timerMode = true;
        }
        else if(e.getSource() == survival_lb){
            timer_lb.setTextFill(Color.WHITE);
            survival_lb.setTextFill(Color.RED);
            timerMode = false;
        }
        else if(e.getSource() == on_lb){
            on_lb.setTextFill(Color.RED);
            off_lb.setTextFill(Color.WHITE);
            if(!Home.musicOn){
                Home.musicOn = true;
                Camera3D.music_src.music.get("Menu").play();
            }
        }
        else if(e.getSource() == off_lb){
            on_lb.setTextFill(Color.WHITE);
            off_lb.setTextFill(Color.RED);
            if(Home.musicOn){
                Home.musicOn = false;
                Camera3D.music_src.music.get("Menu").pause();
            }
        }
    }
    public void mouseEntered(MouseEvent e){
        if(e.getSource() == timer_lb && timerMode == false) timer_lb.setTextFill(Color.RED);
        else if(e.getSource() == survival_lb && timerMode == true) survival_lb.setTextFill(Color.RED);
        else if(e.getSource() == on_lb && Home.musicOn == false) on_lb.setTextFill(Color.RED); 
        else if(e.getSource() == off_lb && Home.musicOn == true) off_lb.setTextFill(Color.RED); 
    }
    public void mouseExited(MouseEvent e){
        if(e.getSource() == timer_lb && timerMode == false) timer_lb.setTextFill(Color.WHITE);
        else if(e.getSource() == survival_lb && timerMode == true) survival_lb.setTextFill(Color.WHITE);
        else if(e.getSource() == on_lb && Home.musicOn == false) on_lb.setTextFill(Color.WHITE); 
        else if(e.getSource() == off_lb && Home.musicOn == true) off_lb.setTextFill(Color.WHITE); 
    }
    public void loadMapImages(){
        try{
            images = new Image[3];
            images[0] = new Image(new FileInputStream("images/map_1.png"));
            images[1] = new Image(new FileInputStream("images/map_2.png"));
            images[2] = new Image(new FileInputStream("images/map_3.png"));
        }catch(FileNotFoundException e){
            System.out.println("map file not found");
        }
    }
    public World getWorld(){
        return world[mapNo];
    }
}
