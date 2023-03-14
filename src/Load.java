

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Animation.Status;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Load {
    @FXML Label load_lb;
    @FXML BorderPane pane;
    int count = -1;
    Stage stage;
    World world;
    Menu menu;
    Timeline load_timer;
    public void initialize(){
        load_timer = new Timeline(
            new KeyFrame(Duration.millis(1000),
                new EventHandler<ActionEvent>() {
                    @Override 
                    public void handle(ActionEvent event){
                        count = (count+1)%6;
                        if(count == 0) load_lb.setText("L O A D I N G ");
                        else load_lb.setText(load_lb.getText()+". ");
                    }
                })
        );
        load_timer.setCycleCount(10);
        load_timer.play();
        load_timer.statusProperty().addListener(
            e->{
                if(load_timer.getStatus().equals(Status.STOPPED)){
                    stage.setTitle("Battle Royale 3D II");
                    FirstScene scene = new FirstScene(stage, world);
                    scene.setMenu(menu);
                    // Map map = new Map(stage);
                    Camera3D.music_src.music.get("Menu").stop();
                    if(Home.musicOn){
                        if(Menu.timerMode) Camera3D.music_src.music.get("Man_Timer").play();
                        else Camera3D.music_src.music.get("Man_Survival").play();
                    }
                }
            }
        );
        
    }
    public void setInfo(Stage stage, World world, Menu m){
        this.stage = stage;
        this.world = world;
        menu = m;
        pane.setPrefWidth(stage.getScene().getWidth());
        pane.setPrefHeight(stage.getScene().getHeight());
        
    }
    public void setPressEvent(){
        stage.getScene().addEventHandler(KeyEvent.KEY_PRESSED, 
            e->{
                if(e.getCode() == KeyCode.SPACE){
                    load_timer.stop();
                }
            });
    }
}
