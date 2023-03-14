import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.geometry.Rectangle2D;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.ObjectInputFilter.Config;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.Flow.Subscriber;

import javafx.animation.AnimationTimer;
import javafx.scene.Camera;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
// import javafx.animation.Animation.Status;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;

import javax.swing.Action;
import javax.swing.plaf.synth.SynthStyle;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;


public class FirstScene extends Scene{
    static final int leftBar = 300;
    private Gun gun;
    Player player;
    Vector<Enemy> eVector = new Vector<>();
    Stage stage;
    LeftBar_time leftbar_timeController;
    LeftBar_sur leftbar_surController;
    Menu menu;
    Timeline enemyGenerate;
    Timeline timer;
    static IntegerProperty lastTime = new SimpleIntegerProperty();
    World world;
    static boolean pause = false;
    SubScene subScene;
    public FirstScene(Stage stage, World world) {
        super(new SmartGroup(), stage.getScene().getWidth(), stage.getScene().getHeight(), true);
        this.stage = stage;
        this.world = world;
        stage.setScene(this);
        SmartGroup g = (SmartGroup)getRoot();
        
        //deal with 3D subscene
        SmartGroup group = new SmartGroup();
        subScene = new SubScene(group, stage.getWidth()-leftBar, stage.getHeight(), true, SceneAntialiasing.BALANCED);
        group.getChildren().add(world);
        subScene.setFill(Color.SILVER);
        
        Pane pane = new Pane(subScene);
        BorderPane bPane = new BorderPane();
        //set left bar
        try{
            if(Menu.timerMode){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("LeftBar_time.fxml"));
                BorderPane p = loader.load();
                leftbar_timeController = loader.getController();
                leftbar_timeController.getRoot(p);
                bPane.setLeft(p);
            }
            else{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("LeftBar_sur.fxml"));
                BorderPane p = loader.load();
                leftbar_surController = loader.getController();
                leftbar_surController.getRoot(p);
                System.out.println("load survival fxml");
                bPane.setLeft(p);
            }
            
        }catch(IOException err){
            System.out.println("left bar error: " + err);
        }
        bPane.setCenter(pane);
        g.getChildren().add(bPane);
        
        player = new Player(subScene, world, this);
        eVector.add(new Enemy(subScene, world, player, "Little_Ghost/tinker.obj"));
        // eVector.add(new Enemy(subScene, world, Object3D.objectEnum.GHOST));
        eVector.add(new Bat(subScene, world, player));

        player.getEnemy(eVector);
        // for(Enemy e:eVector){
        //     e.getPlayer(player);
        // }
        
        subScene.setCamera(player.getJesusCamera());
        subScene.setCamera(player.getFirstPCamera());

        //initialize weapon
        gun = new Gun(group,player.getFirstPCamera(), player);
        gun.addGunSight(pane, subScene);
        player.getWeapon(gun);
        if(Menu.timerMode) leftbar_timeController.setPlayerEnemy(player, eVector);
        else leftbar_surController.setPlayerEnemy(player, eVector);
        // randomly generate an enemy
        enemyGenerate = new Timeline(
            new KeyFrame(Duration.millis(20000),
                new EventHandler<ActionEvent>() {
                    @Override 
                    public void handle(ActionEvent event){
                        System.out.println("generate an enemy");
                        Enemy e = new Bat(subScene, world, player);
                        if(Menu.timerMode == true) e.bindMap(leftbar_timeController.getMap_Group(), leftbar_timeController.getMap_Rect());
                        else e.bindMap(leftbar_surController.getMap_Group(), leftbar_surController.getMap_Rect());
                        eVector.add(e);
                    }
                }));
        enemyGenerate.setCycleCount(Timeline.INDEFINITE);
        enemyGenerate.play();
        
        if(Menu.timerMode == false){
            world.generateLadder(subScene);
            leftbar_surController.bind_ladderLb();
        }
        
        world.generateTreasure(subScene);
        
        addEventHandler(KeyEvent.KEY_PRESSED, e->{
            if(e.getCode() == KeyCode.P){
                pauseAll();
                player.pauseAll();
                for(Enemy en : eVector) en.pauseAll();
                world.pauseAll();

                player.curActiveKeys.clear();
                pause = true;
                Stage pause_stage = new Stage();
                Pause p = new Pause(pause_stage, player, eVector, this, world);
                
                if(Menu.timerMode) Camera3D.music_src.music.get("Man_Timer").pause();
                else Camera3D.music_src.music.get("Man_Survival").pause();
                if(Home.musicOn){
                    Camera3D.music_src.music.get("Pause").play();
                    Camera3D.music_src.music.get("Walking").stop();
                }            
            }
            else if(e.getCode() == KeyCode.Q){
                player.changeGun();
            }
        });
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            bPane.setPrefWidth(stage.getWidth());
            subScene.setWidth(stage.getWidth()-leftBar);
        });
        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            bPane.setPrefHeight(stage.getHeight());
            subScene.setHeight(stage.getHeight());
        });
        if(Menu.timerMode){
            lastTime.set(60);
            timer = new Timeline(
                new KeyFrame(Duration.millis(1000),
                    new EventHandler<ActionEvent>() {
                        @Override 
                        public void handle(ActionEvent event){
                            lastTime.set(lastTime.get()-1);
                            if(lastTime.get()==0 && player.HP>0){
                                endGame(true);
                            }
                        }
                    }));
            timer.setCycleCount(60);
            timer.setDelay(Duration.seconds(1));
            timer.play();
        }
    }
    
    public void backToMenu() {
        if(Home.musicOn){
            if(Camera3D.music_src.music.get("Walking").getStatus().equals(Status.PLAYING)){
                Camera3D.music_src.music.get("Walking").stop();
            }
        }
        try{
            player.stopAll();
            for(Enemy e: eVector) e.stopAll();
            this.stopAll();
            world.stopAll();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));
            Scene menu = new Scene(loader.load());
            Menu controller = loader.getController();
            controller.setStage(stage);
            stage.setTitle("MENU");
            stage.setScene(menu);
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2); 
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        }
        catch(Exception error){

        }
    }
    public void setMenu(Menu m){
        menu = m;
    }
    public void endGame(boolean success){
        if(Menu.timerMode) Camera3D.music_src.music.get("Man_Timer").stop();
        else Camera3D.music_src.music.get("Man_Survival").stop();
        if(Home.musicOn){
            if(success) Camera3D.music_src.music.get("Success").play();
            else Camera3D.music_src.music.get("Fail").play();
            if(Camera3D.music_src.music.get("Walking").getStatus().equals(Status.PLAYING)){
                Camera3D.music_src.music.get("Walking").stop();
            }
        }
        try{
            player.stopAll();
            for(Enemy e: eVector) e.stopAll();
            this.stopAll();
            world.stopAll();

            stage.close();
            stage.setTitle("END");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("End.fxml"));
            Scene end_scene = new Scene(loader.load());
            End controller = loader.getController();
            controller.setStage(stage);
            controller.setFirstScene(this);
            controller.setMenu(menu);
            controller.setResult(success);
            stage.setScene(end_scene);
            stage.show();
        }catch(Exception exc){

        }
    }
    public void pauseAll(){
        if(timer!=null) timer.pause();
        enemyGenerate.pause();
    }
    public void stopAll(){
        if(timer!=null) timer.stop();
        enemyGenerate.stop();
    }
    public void resumeAll(){
        if(timer!=null) timer.play();
        enemyGenerate.play();
    }
}
