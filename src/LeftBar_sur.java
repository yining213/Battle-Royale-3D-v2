import java.util.Timer;
import java.util.Vector;

import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class LeftBar_sur {
    @FXML
    Rectangle hp_bar, map_rec;
    @FXML
    Label mag_lb, kill_lb, ladder_lb;
    @FXML
    Button pause_btn;
    @FXML
    Label HPlabel, gun_type_lb;
    @FXML
    Group map_group;
    @FXML
    Canvas map_canvas;
    
    BorderPane bPane;

    Player player;
    Vector<Enemy> eVector;
    World world;
    public void initialize(){
        mag_lb.setText("50/50");
    }
    public void setPlayerEnemy(Player p, Vector<Enemy> eVect){
        player = p;
        eVector = eVect;
        world = player.world;
        propertyBindInit();
        // setMap();
    }
    public void setMap(){
        System.out.println("set map");
        int map[][] = world.map;
        double w = map_canvas.getWidth()/world.COL;
        double h = map_canvas.getWidth()/world.ROW;
        GraphicsContext gc =  map_canvas.getGraphicsContext2D(); 
        for(int i=0;i<world.ROW;i++){
            for(int j=0;j<world.COL;j++){
                if(map[i][j] == 0){
                    gc.setFill(Color.BLACK);
                    gc.fillRect(i*w, j*h, w, h);
                }else if(map[i][j] == 1){
                    gc.setFill(Color.WHITE);
                    gc.fillRect(i*w, j*h, w, h);
                }
            }
        }
    }
    public Rectangle getMap_Rect(){return map_rec;}
    public Group getMap_Group(){return map_group;}
    public void getRoot(BorderPane p){
        bPane = p;
    }
    public void bind_ladderLb(){
        world.ladderShow.addListener(
            (b)->{
                // System.out.println("ladder show1: " + b);
                ladder_lb.setText("DOWN");
            }
        );
    }
    public void propertyBindInit(){
        player.bindHPbar(hp_bar, HPlabel);
        player.bindMag(mag_lb);
        player.bindKillNum(kill_lb);
        player.bindMap(map_group, map_rec);
        for(Enemy i: eVector){
            i.bindMap(map_group, map_rec);
        }
    }
    public Rectangle getHPbar(){
        return hp_bar;
    }
    public void btnPressed(MouseEvent e){
        if(e.getSource() == pause_btn){
            player.f.pauseAll();
            player.pauseAll();
            for(Enemy en : eVector) en.pauseAll();
            world.pauseAll();

            player.f.pause = true;
            player.curActiveKeys.clear();

            Stage pause_stage = new Stage();
            pause_stage.setResizable(false);
            Pause p = new Pause(pause_stage, player, eVector, player.f, world);
            
            if(Menu.timerMode) Camera3D.music_src.music.get("Man_Timer").pause();
            else Camera3D.music_src.music.get("Man_Survival").pause();
            if(Home.musicOn){
                Camera3D.music_src.music.get("Pause").play();
                Camera3D.music_src.music.get("Walking").stop();
            }
        }
    }
}
