import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.Cylinder;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableArray;
import javafx.event.ActionEvent;
import javafx.geometry.Point3D;
import java.io.File;
import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.awt.MouseInfo;
import java.beans.EventHandler;
import javafx.geometry.Bounds;

import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Animation.Status;
import javafx.scene.Camera;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import org.w3c.dom.events.EventException;
import java.net.URL;
import javafx.scene.PointLight;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;


public class Player extends Character{
    private PerspectiveCamera jesus_camera;
    private PerspectiveCamera firstp_camera;
    //private Cylinder body;
    // private Group body;
    Boolean mousePressed = false;
    HashMap<String, Boolean> curActiveKeys = new HashMap<>();
    HashMap<String, Boolean> KeysDoubleClick = new HashMap<>();
    AnimationTimer moveTimer;
    Timeline mousePressTimer;
    private Gun gun;
    Vector<Enemy> eVector = new Vector<>();
    double distance = 70;
    double height = 40;
    World world;
    int HP;
    Bar HPBar;
    DoubleProperty HPpoint = new SimpleDoubleProperty();
    PointLight pointlight;

    DoubleProperty angleX;
    DoubleProperty angleY;
    Translate pivot;
    Rotate worldRotX ;
    Rotate worldRotY ;
    Translate jesusLoc;
    Scene scene;
    Group g;
    boolean onLadder = false;
    boolean escape = false;
    FirstScene f;
    Timeline timer;
    boolean endGame = false;
    public Player(SubScene subscene, World world, FirstScene f){
        super();
        scene = subscene.getScene();
        this.f = f;
        //body = new Cylinder(5, 50);
        // ObjModelImporter importer = new ObjModelImporter();
        // try{
        //     //File file = new File("src/Classic(3D).obj");
        //     File file = new File("src/Mini_person/tinker.obj");
        //     importer.read(file);
        // }
        // catch (ImportException e) {
        //     e.printStackTrace();
        //     return;
        // }
        // MeshView[] meshViews = importer.getImport();
        // body.getChildren().addAll(meshViews);
        // System.out.println("mesh size:"+meshViews.length);
        // importer.close();
        endGame = false;
        this.world = world;
        body = loadModel(getClass().getResource("Mini_person/tinker.obj"));
        g = (Group)subscene.getRoot();
        
        firstp_camera = new PerspectiveCamera(true);
        jesus_camera = new PerspectiveCamera(true);
        firstp_camera.setNearClip(0.1);
        firstp_camera.setFarClip(1000);
        jesus_camera.setNearClip(0.1);
        jesus_camera.setFarClip(1000);
        
        HP = 100;
        HPpoint.set(100);
        HPBar = new Bar();
        
        pointlight = new PointLight();
        pointlight.setMaxRange(200);
        pointlight.setLinearAttenuation(0.02);
        
        initCamera(subscene);
        g.getChildren().addAll(body, HPBar, pointlight);
        
        subscene.setCamera(firstp_camera);
    }
    public void setIniPos() {
        int row, col;
        do {
            row = (int) (Math.random() * world.ROW);
            col = (int) (Math.random() * world.COL);
        } while (!world.isUnBlocked(row, col));
        double x = world.bound.getMinX() + col * world.len;
        double z = world.bound.getMinZ() + row * world.len;
        pivot.setX(x);
        pivot.setZ(z);
    }
    public void getWeapon(Weapon w){
        gun = (Gun)w;
    }
    public void getEnemy(Vector<Enemy> eVector){
        this.eVector = eVector;
    }
    void HPSupplement(int hp) {
        if(HP+hp<=100)
            HP += hp;
        else 
            HP=100;
        HPpoint.set(HP);
    }
    public void initCamera(SubScene subscene){
        angleX = new SimpleDoubleProperty(0);
        angleY = new SimpleDoubleProperty(0);
        pivot = new Translate();
        setIniPos();
        worldRotX = new Rotate(0, Rotate.X_AXIS);
        worldRotY = new Rotate(0, Rotate.Y_AXIS);
        jesusLoc = new Translate(0,-height,-distance);
        worldRotX.angleProperty().bind(angleX);
        worldRotY.angleProperty().bind(angleY);
        
        firstp_camera.getTransforms().addAll (
                new Translate(0,-20,0),
                // new Translate(500,0,0),
                pivot,
                worldRotY, worldRotX
        ); 
        jesus_camera.getTransforms().addAll (
                pivot,
                worldRotY, worldRotX,
                jesusLoc
        ); 
        body.getTransforms().addAll (
                pivot,
                worldRotY
        );  
        HPBar.getTransforms().addAll (
            pivot,
            worldRotY,
            new Translate(-HPBar.w/2.,-35,0)
        ); 
        pointlight.getTransforms().addAll(
            pivot,
            worldRotY,worldRotX,
            new Translate(0, -20, 10)
        );
        firstp_camera.setFieldOfView(50);
        firstp_camera.setDepthTest(DepthTest.ENABLE);
        firstp_camera.setNearClip(0.01);
        firstp_camera.setFarClip(1000);
        jesus_camera.setFieldOfView(50);
        jesus_camera.setDepthTest(DepthTest.ENABLE);
        jesus_camera.setNearClip(0.01);
        jesus_camera.setFarClip(1000);
        
        scene.addEventHandler(ScrollEvent.SCROLL, event -> {
            //Get how much scroll was done in Y axis.
            double delta = event.getDeltaY();
            //Add it to the Z-axis location.
            if(subscene.getCamera() == jesus_camera){
                // new AnimationTimer() {
                //     long previousTime = System.nanoTime(); // time since app launch
                //     @Override
                //     public void handle(long now) {
                //     }   
                // }.start();
                double ny = jesusLoc.getY()+height*delta*0.005;
                double nz = jesusLoc.getZ()+distance*delta*0.005;
                if(ny<= -height && ny>=-2*height && nz<=-distance && nz>=-3*distance){
                    jesusLoc.setY(ny);
                    jesusLoc.setZ(nz);
                }
            }
        });

        
        scene.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if(subscene.getCamera() == firstp_camera){
                gun.attack();
                if(Home.musicOn){
                    if(gun.MagNum.get()>0) Camera3D.music_src.effect.get("Gun").play();
                    else Camera3D.music_src.effect.get("Empty_Mag").play();
                }
            }
        });
        scene.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            if(subscene.getCamera() == firstp_camera){
                mousePressed = true;
                // if(Home.musicOn){
                //     if(gun.MagNum.get()>0) Camera3D.music_src.effect.get("Gun").play();
                //     else Camera3D.music_src.effect.get("Empty_Mag").play();
                // }
            }
        });
        scene.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            if(subscene.getCamera() == firstp_camera){
                mousePressed = false;
                // if(Home.musicOn){
                //     if(gun.MagNum.get()>0) Camera3D.music_src.effect.get("Gun").play();
                //     else Camera3D.music_src.effect.get("Empty_Mag").play();
                // }
            }
        });
        mousePressTimer = new Timeline(
            new KeyFrame(Duration.millis(100),
                new javafx.event.EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        if(mousePressed){
                            gun.attack();
                            if(Home.musicOn){
                                if(gun.MagNum.get()>0) Camera3D.music_src.effect.get("Gun").play();
                                else Camera3D.music_src.effect.get("Empty_Mag").play();
                            }
                        }
                    }
                })
        );
        mousePressTimer.setCycleCount(Timeline.INDEFINITE);
        mousePressTimer.play();
        timer = new Timeline(
            new KeyFrame(Duration.millis(9),
                new javafx.event.EventHandler<ActionEvent>() {
                    private double vel = 7;
                    private double Rotvel = 5;
                    @Override
                    public void handle(ActionEvent e) {
                        double posX = MouseInfo.getPointerInfo().getLocation().x - scene.getWindow().getX() - f.leftBar;
                        double posY = MouseInfo.getPointerInfo().getLocation().y- scene.getWindow().getY() - 28;
                        double distX = posX - subscene.getWidth()/2;
                        double distY = posY - subscene.getHeight()/2;
                        Point3D front_dir = firstp_camera.localToParent(0,0,1).subtract(firstp_camera.localToParent(0,0,0)).normalize();
                        Point3D side_dir = firstp_camera.localToParent(1,0,0).subtract(firstp_camera.localToParent(0,0,0)).normalize();
                        double vel = 0.5;
                        boolean collision = false;
                        if(0<=posX && posX<=subscene.getWidth() && 0<=posY && posY<=subscene.getHeight()){
                            if(Math.abs(distX) > 0){
                                // front_dir = firstp_camera.localToParent(0,0,1).subtract(firstp_camera.localToParent(0,0,0)).normalize();
                                // side_dir = firstp_camera.localToParent(1,0,0).subtract(firstp_camera.localToParent(0,0,0)).normalize();
                                double angle = (angleY.get() + Rotvel * (distX/2000))%360;
                                angleY.set(angle);
                            }
                            if(Math.abs(distY) > 0){
                                if(!((angleX.get() < -10 && distY > 0) || (angleX.get() > 10 && distY < 0))){
                                    angleX.set(angleX.get() - Rotvel * (distY/3000)); 
                                }
                            }
                            if(!f.pause) scene.setCursor(Cursor.NONE);
                        }
                        else{
                            scene.setCursor(Cursor.DEFAULT);
                        }
                        // if(0<=posX && posX<=subscene.getWidth() && 0<=posY && posY<=subscene.getHeight()){
                        //     scene.setCursor(Cursor.NONE);
                        //     System.out.println(scene.getCursor());
                        // }
                        // else scene.setCursor(Cursor.DEFAULT);
                    } 
                })
        );
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();

        KeysDoubleClick.put(KeyCode.W.toString(), false);
        KeysDoubleClick.put(KeyCode.A.toString(), false);
        KeysDoubleClick.put(KeyCode.S.toString(), false);
        KeysDoubleClick.put(KeyCode.D.toString(), false);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if(!curActiveKeys.containsKey(event.getCode().toString())){
                curActiveKeys.put(event.getCode().toString(), true);
            }
            switch(event.getCode()){
                case SPACE:
                    new JumpAnimation().start();
                    break;
                case CONTROL:
                    new SquatDownAnimation().start();
                    break;    
            }
        });

        scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if(curActiveKeys.containsKey(event.getCode().toString())){
                curActiveKeys.remove(event.getCode().toString(), true);
            }
            switch(event.getCode()){
                case CONTROL:
                    new SquatUpAnimation().start();
                    break;   
                // case SPACE:
                //     new JumpAnimation().start();
                //     break; 
            }
        });
        moveTimer = new AnimationTimer() {
            Point3D front_dir = firstp_camera.localToParent(0,0,1).subtract(firstp_camera.localToParent(0,0,0)).normalize();
            Point3D side_dir = firstp_camera.localToParent(1,0,0).subtract(firstp_camera.localToParent(0,0,0)).normalize();
            double vel = 60;
            // double vel = 40;
            boolean collision = false;
            long previousTime = System.nanoTime(); // time since app launch

            @Override
            public void handle(long now){
                double elapsedTime = (now - previousTime) / 1000000000.0;
                previousTime = now;
                double scale = vel*elapsedTime;
                front_dir = firstp_camera.localToParent(0,0,1).subtract(firstp_camera.localToParent(0,0,0)).normalize();
                side_dir = firstp_camera.localToParent(1,0,0).subtract(firstp_camera.localToParent(0,0,0)).normalize();
                if(curActiveKeys.containsKey(KeyCode.W.toString()) ||
                    curActiveKeys.containsKey(KeyCode.A.toString()) || 
                    curActiveKeys.containsKey(KeyCode.S.toString()) ||
                    curActiveKeys.containsKey(KeyCode.D.toString())){
                        if(Home.musicOn && Camera3D.music_src.music.get("Walking").getStatus().equals(Status.RUNNING) == false){
                            Camera3D.music_src.music.get("Walking").play();
                        }
                    }
                else Camera3D.music_src.music.get("Walking").stop();

                if(curActiveKeys.containsKey(KeyCode.W.toString()) && curActiveKeys.containsKey(KeyCode.R.toString()) && !curActiveKeys.containsKey(KeyCode.CONTROL.toString())) 
                    vel = 100;
                else vel = 60;

                if(curActiveKeys.containsKey(KeyCode.W.toString()) && curActiveKeys.containsKey(KeyCode.CONTROL.toString())){
                    if(world.hitWall3(pivot.getX()+scale/3*front_dir.getX(), pivot.getZ()+scale/3*front_dir.getZ()) == false){
                        pivot.setX(pivot.getX()+scale/3*front_dir.getX());
                        pivot.setZ(pivot.getZ()+scale/3*front_dir.getZ());
                    }else {
                        scale*=2;
                        pivot.setX(pivot.getX()-scale/3*front_dir.getX());
                        pivot.setZ(pivot.getZ()-scale/3*front_dir.getZ());
                    }
                }
                else if(curActiveKeys.containsKey(KeyCode.S.toString()) && curActiveKeys.containsKey(KeyCode.CONTROL.toString())){
                    if(world.hitWall3(pivot.getX()-scale/3*front_dir.getX(), pivot.getZ()-scale/3*front_dir.getZ()) == false){
                        pivot.setX(pivot.getX()-scale/3*front_dir.getX());
                        pivot.setZ(pivot.getZ()-scale/3*front_dir.getZ());
                    }else {
                        scale*=2;
                        pivot.setX(pivot.getX()+scale/3*front_dir.getX());
                        pivot.setZ(pivot.getZ()+scale/3*front_dir.getZ());
                    }
                }
                if(curActiveKeys.containsKey(KeyCode.A.toString()) && curActiveKeys.containsKey(KeyCode.CONTROL.toString())){
                    if(world.hitWall3(pivot.getX()-scale/3*side_dir.getX(), pivot.getZ()-scale/3*side_dir.getZ()) == false){
                        pivot.setX(pivot.getX()-scale/3*side_dir.getX());
                        pivot.setZ(pivot.getZ()-scale/3*side_dir.getZ());
                    }else {
                        scale*=2;
                        pivot.setX(pivot.getX()+scale/3*side_dir.getX());
                        pivot.setZ(pivot.getZ()+scale/3*side_dir.getZ());
                    }
                }
                else if(curActiveKeys.containsKey(KeyCode.D.toString()) && curActiveKeys.containsKey(KeyCode.CONTROL.toString())){
                    if(world.hitWall3(pivot.getX()+scale/3*side_dir.getX(), pivot.getZ()+scale/3*side_dir.getZ()) == false){
                        pivot.setX(pivot.getX()+scale/3*side_dir.getX());
                        pivot.setZ(pivot.getZ()+scale/3*side_dir.getZ());
                    }else {
                        scale*=2;
                        pivot.setX(pivot.getX()-scale/3*side_dir.getX());
                        pivot.setZ(pivot.getZ()-scale/3*side_dir.getZ());
                    }     
                }
                if(curActiveKeys.containsKey(KeyCode.W.toString())){
                    // if(KeysDoubleClick.get(KeyCode.W.toString()) == true){
                    //     System.out.println("W second click");
                    //     scale*=2;
                    // }
                    if(!Menu.timerMode && world.ladderShow.get() == true && world.ladder.getBound().intersects(body.getBoundsInParent())){
                        pivot.setY(pivot.getY()-1);
                        world.ladderGenerate.pause();
                        onLadder = true;
                        if(world.getSkyBounds().intersects(body.getBoundsInParent()) && !endGame){
                            // System.out.println("bound sky");
                            escape = true;
                            f.endGame(escape);
                            endGame = true;
                        }
                    }
                    else if(onLadder == true){
                        new FallAnimation().start();
                        onLadder = false;
                        world.ladderGenerate.play();
                    }
                    else if(world.hitWall3(pivot.getX()+scale*front_dir.getX(), pivot.getZ()+scale*front_dir.getZ()) == false){
                        pivot.setX(pivot.getX()+scale*front_dir.getX());
                        pivot.setZ(pivot.getZ()+scale*front_dir.getZ());
                    }
                    else{
                        scale*=2;
                        pivot.setX(pivot.getX()-scale*front_dir.getX());
                        pivot.setZ(pivot.getZ()-scale*front_dir.getZ());
                    }
                    
                }
                else if(curActiveKeys.containsKey(KeyCode.S.toString())){
                    if(!Menu.timerMode && world.ladderShow.get() && world.ladder.getBound().intersects(body.getBoundsInParent())){
                        pivot.setY(pivot.getY()+1);
                        world.ladderGenerate.pause();
                        onLadder = true;
                    }
                    else if(onLadder == true){
                        // new FallAnimation().start();
                        if(pivot.getY() >= 0) pivot.setY(0);
                        onLadder = false;
                        world.ladderGenerate.play();
                    }
                    else if(world.hitWall3(pivot.getX()-scale*front_dir.getX(), pivot.getZ()-scale*front_dir.getZ()) == false){
                        pivot.setX(pivot.getX()-scale*front_dir.getX());
                        pivot.setZ(pivot.getZ()-scale*front_dir.getZ());
                    }else {
                        scale*=2;
                        pivot.setX(pivot.getX()+scale*front_dir.getX());
                        pivot.setZ(pivot.getZ()+scale*front_dir.getZ());
                    }
                }
                if(curActiveKeys.containsKey(KeyCode.A.toString())){
                    if(world.hitWall3(pivot.getX()-scale*side_dir.getX(), pivot.getZ()-scale*side_dir.getZ()) == false){
                        pivot.setX(pivot.getX()-scale*side_dir.getX());
                        pivot.setZ(pivot.getZ()-scale*side_dir.getZ());
                    }else {
                        scale*=2;
                        pivot.setX(pivot.getX()+scale*side_dir.getX());
                        pivot.setZ(pivot.getZ()+scale*side_dir.getZ());
                    }
                }
                else if(curActiveKeys.containsKey(KeyCode.D.toString())){
                    if(world.hitWall3(pivot.getX()+scale*side_dir.getX(), pivot.getZ()+scale*side_dir.getZ()) == false){
                        pivot.setX(pivot.getX()+scale*side_dir.getX());
                        pivot.setZ(pivot.getZ()+scale*side_dir.getZ());
                    }else {
                        scale*=2;
                        pivot.setX(pivot.getX()-scale*side_dir.getX());
                        pivot.setZ(pivot.getZ()-scale*side_dir.getZ());
                    }
                }
                Treasure remove_Treasure = null;
                for(Treasure t: world.treasures){
                    if(t.getBound().intersects(body.getBoundsInParent())){
                        remove_Treasure = t;
                        gun.BulletSupplement(t.bulletSupplement);
                        HPSupplement(t.HPSupplement);
                        // g.getChildren().remove(t);
                        // world.treasures.remove(t);
                    }
                }
                if(remove_Treasure != null){
                    try{
                        g.getChildren().remove(remove_Treasure);
                        world.treasures.remove(remove_Treasure);
                        System.out.println(world.treasures.size());
                    }catch(Exception err){
                        System.out.println(err);
                    }
                }
                
                //vel = 0.5;
                //     System.out.println("hit Wall");
                //     // vel = -Math.abs(vel);
                //     // vel = -1;
                    
                //     if(curActiveKeys.containsKey(KeyCode.W.toString())){
                //         pivot.setX(pivot.getX()-vel*front_dir.getX()*2);
                //         pivot.setZ(pivot.getZ()-vel*front_dir.getZ()*2);
                //     }
                //     else if(curActiveKeys.containsKey(KeyCode.S.toString())){
                //         pivot.setX(pivot.getX()+vel*front_dir.getX()*2);
                //         pivot.setZ(pivot.getZ()+vel*front_dir.getZ()*2);
                //     }
                //     else if(curActiveKeys.containsKey(KeyCode.A.toString())){
                //         pivot.setX(pivot.getX()+vel*side_dir.getX()*2);
                //         pivot.setZ(pivot.getZ()+vel*side_dir.getZ()*2);
                //     }
                //     else if(curActiveKeys.containsKey(KeyCode.D.toString())){
                //         pivot.setX(pivot.getX()-vel*side_dir.getX()*2);
                //         pivot.setZ(pivot.getZ()-vel*side_dir.getZ()*2);
                //     }
                // }
                // else {
                //     // vel = Math.abs(vel);
                //     //vel = 0.5;
                
                //     if(curActiveKeys.containsKey(KeyCode.W.toString()) && curActiveKeys.containsKey(KeyCode.CONTROL.toString())){
                //         // firstp_camera.setTranslateZ(firstp_camera.getTranslateZ() + vel*front_dir.getZ()/3);
                //         // firstp_camera.setTranslateX(firstp_camera.getTranslateX() + vel*front_dir.getX()/3);
                //         pivot.setX(pivot.getX()+vel/3*front_dir.getX());
                //         // pivot.setY(pivot.getY()+vel/3*front_dir.getY());
                //         pivot.setZ(pivot.getZ()+vel/3*front_dir.getZ());
                //     }
                //     else if(curActiveKeys.containsKey(KeyCode.S.toString()) && curActiveKeys.containsKey(KeyCode.CONTROL.toString())){
                //         // firstp_camera.setTranslateZ(firstp_camera.getTranslateZ() - vel*front_dir.getZ()/3);
                //         // firstp_camera.setTranslateX(firstp_camera.getTranslateX() - vel*front_dir.getX()/3);
                //         pivot.setX(pivot.getX()-vel/3*front_dir.getX());
                //         // pivot.setY(pivot.getY()-vel/3*front_dir.getY());
                //         pivot.setZ(pivot.getZ()-vel/3*front_dir.getZ());
                //     }
                //     else if(curActiveKeys.containsKey(KeyCode.A.toString()) && curActiveKeys.containsKey(KeyCode.CONTROL.toString())){
                //         // firstp_camera.setTranslateZ(firstp_camera.getTranslateZ() - vel*side_dir.getZ()/3);
                //         // firstp_camera.setTranslateX(firstp_camera.getTranslateX() - vel*side_dir.getX()/3);
                //         pivot.setX(pivot.getX()-vel/3*side_dir.getX());
                //         // pivot.setY(pivot.getY()-vel/3*side_dir.getY());
                //         pivot.setZ(pivot.getZ()-vel/3*side_dir.getZ());
                //     }
                //     else if(curActiveKeys.containsKey(KeyCode.D.toString()) && curActiveKeys.containsKey(KeyCode.CONTROL.toString())){
                //         // firstp_camera.setTranslateZ(firstp_camera.getTranslateZ() + vel*side_dir.getZ()/3);
                //         // firstp_camera.setTranslateX(firstp_camera.getTranslateX() + vel*side_dir.getX()/3);
                //         pivot.setX(pivot.getX()+vel/3*side_dir.getX());
                //         // pivot.setY(pivot.getY()+vel/3*side_dir.getY());
                //         pivot.setZ(pivot.getZ()+vel/3*side_dir.getZ());                
                //     }
                //     else if(curActiveKeys.containsKey(KeyCode.W.toString())){
                //         // firstp_camera.setTranslateZ(firstp_camera.getTranslateZ() + vel*front_dir.getZ());
                //         // firstp_camera.setTranslateX(firstp_camera.getTranslateX() + vel*front_dir.getX());
                //         pivot.setX(pivot.getX()+vel*front_dir.getX());
                //         // pivot.setY(pivot.getY()+vel*front_dir.getY());
                //         pivot.setZ(pivot.getZ()+vel*front_dir.getZ());
                //     }
                //     else if(curActiveKeys.containsKey(KeyCode.S.toString())){
                //         // firstp_camera.setTranslateZ(firstp_camera.getTranslateZ() - vel*front_dir.getZ());
                //         // firstp_camera.setTranslateX(firstp_camera.getTranslateX() - vel*front_dir.getX());
                //         pivot.setX(pivot.getX()-vel*front_dir.getX());
                //         // pivot.setY(pivot.getY()-vel*front_dir.getY());
                //         pivot.setZ(pivot.getZ()-vel*front_dir.getZ());
                //     }
                //     else if(curActiveKeys.containsKey(KeyCode.A.toString())){
                //         // firstp_camera.setTranslateZ(firstp_camera.getTranslateZ() - vel*side_dir.getZ());
                //         // firstp_camera.setTranslateX(firstp_camera.getTranslateX() - vel*side_dir.getX());
                //         pivot.setX(pivot.getX()-vel*side_dir.getX());
                //         // pivot.setY(pivot.getY()-vel*side_dir.getY());
                //         pivot.setZ(pivot.getZ()-vel*side_dir.getZ());
                //     }
                //     else if(curActiveKeys.containsKey(KeyCode.D.toString())){
                //         // firstp_camera.setTranslateZ(firstp_camera.getTranslateZ() + vel*side_dir.getZ());
                //         // firstp_camera.setTranslateX(firstp_camera.getTranslateX() + vel*side_dir.getX());
                //         pivot.setX(pivot.getX()+vel*side_dir.getX());
                //         // pivot.setY(pivot.getY()+vel*side_dir.getY());
                //         pivot.setZ(pivot.getZ()+vel*side_dir.getZ());
                //     }
                // }
            }
        };
        moveTimer.start();

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case TAB:
                    if(subscene.getCamera() == getFirstPCamera()){
                        // setCamera(jesus_camera);
                        subscene.setCamera(getJesusCamera());
                        world.enableSky(false);
                        gun.enableGunsight(false);
                        moveTimer.stop();
                        pointlight.setMaxRange(Double.POSITIVE_INFINITY);
                        pointlight.setLinearAttenuation(0);;
                        if(Menu.timerMode) Camera3D.music_src.music.get("Man_Timer").stop();
                        else Camera3D.music_src.music.get("Man_Survival").stop();
                        if(Home.musicOn){
                            Camera3D.music_src.music.get("God").play();
                        }
                        f.pauseAll();
                        for(Enemy en: f.eVector) en.pauseAll();
                        f.world.pauseAll();
                    }
                    else{
                        // setCamera(firstp_camera);
                        subscene.setCamera(getFirstPCamera());
                        world.enableSky(true);
                        gun.enableGunsight(true);
                        moveTimer.start();
                        pointlight.setMaxRange(200);
                        pointlight.setLinearAttenuation(0.02);;
                        Camera3D.music_src.music.get("God").stop();
                        if(Home.musicOn){
                            if(Menu.timerMode) Camera3D.music_src.music.get("Man_Timer").play();
                            else Camera3D.music_src.music.get("Man_Survival").play();
                        }
                        f.resumeAll();
                        for(Enemy en: f.eVector) en.resumeAll();
                        f.world.resumeAll();
                    }
                    // stage.setTitle("JesusPerspective");
                    // try{
                    //     JesusScene scene = new JesusScene(stage);
                    // }catch(Exception exc){
            
                    // }
                break;
            }
        });
    }
    private class JumpAnimation extends AnimationTimer{
        double velocity = -50; 
        double grav = 100;
        long previousTime = System.nanoTime(); // time since app launch
        @Override
        public void handle(long now) {
            double elapsedTime = (now - previousTime) / 1000000000.0;
            previousTime = now;
            velocity += grav * elapsedTime;
            double scale = elapsedTime * velocity;
            firstp_camera.setTranslateY(firstp_camera.getTranslateY() + scale);
            jesus_camera.setTranslateY(jesus_camera.getTranslateY() + scale);
            body.setTranslateY(body.getTranslateY() + scale);
            if(firstp_camera.getTranslateY() >= 0){
                // System.out.println("jump stop");
                firstp_camera.setTranslateY(0);
                jesus_camera.setTranslateY(0);
                body.setTranslateY(0);
                
                this.stop();
            }
        }
    }
    private class SquatDownAnimation extends AnimationTimer{
        double velocity = -30; 
        double grav = 100;
        long previousTime = System.nanoTime(); // time since app launch
        @Override
        public void handle(long now) {
            double elapsedTime = (now - previousTime) / 1000000000.0;
            previousTime = now;
            velocity += grav * elapsedTime;
            double scale = elapsedTime * velocity;
            firstp_camera.setTranslateY(firstp_camera.getTranslateY() - scale);
            jesus_camera.setTranslateY(jesus_camera.getTranslateY() - scale);
            //System.out.println(firstp_camera.getTranslateY());
            if(firstp_camera.getTranslateY() >=4 || firstp_camera.getTranslateY() <=0 ){
                // System.out.println("squat down stop");
                firstp_camera.setTranslateY(4);
                jesus_camera.setTranslateY(4);
                this.stop();
            }
        }
    }
    private class FallAnimation extends AnimationTimer{
        double velocity = -50; 
        double grav = 100;
        long previousTime = System.nanoTime(); // time since app launch
        @Override
        public void handle(long now) {
            double elapsedTime = (now - previousTime) / 1000000000.0;
            previousTime = now;
            velocity += grav * elapsedTime;
            double scale = elapsedTime * velocity;
            firstp_camera.setTranslateY(firstp_camera.getTranslateY() + scale);
            jesus_camera.setTranslateY(jesus_camera.getTranslateY() + scale);
            body.setTranslateY(body.getTranslateY() + scale);
            System.out.println(firstp_camera.getTranslateY());
            if(firstp_camera.getTranslateY() <= 0){
                System.out.println("fall stop");
                firstp_camera.setTranslateY(0);
                jesus_camera.setTranslateY(0);
                body.setTranslateY(0);
                this.stop();
            }
        }
    }
    private class SquatUpAnimation extends AnimationTimer{
        double velocity = -30;
        double grav = 100;
        long previousTime = System.nanoTime(); // time since app launch
        @Override
        public void handle(long now) {
            double elapsedTime = (now - previousTime) / 1000000000.0;
            previousTime = now;
            velocity += grav * elapsedTime;
            double scale = elapsedTime * velocity;
            
            firstp_camera.setTranslateY(firstp_camera.getTranslateY() + scale);
            jesus_camera.setTranslateY(jesus_camera.getTranslateY() + scale);
            //System.out.println(firstp_camera.getTranslateY());
            if(firstp_camera.getTranslateY() <= 0 || firstp_camera.getTranslateY() >=4){
                // System.out.println("squat up stop");
                firstp_camera.setTranslateY(0);
                jesus_camera.setTranslateY(0);
                this.stop();
            }
        }
    }
    public PerspectiveCamera getFirstPCamera(){
        return firstp_camera;
    }
    public PerspectiveCamera getJesusCamera(){
        return jesus_camera;
    }
    class Bar extends Group implements Serializable{
        Rectangle frame;
        Rectangle slide;
        int w = 20;
        int h = 2;
        DoubleProperty slideProperty = new SimpleDoubleProperty();
        Bar(){
            slide = new Rectangle(w,h);
            // slide.setX(w/2);
            slide.setFill(Color.RED);
            slide.widthProperty().bind(slideProperty);
            // slideProperty.set(20);
            slideProperty.bind(HPpoint.divide(100.).multiply(w));
            
            frame = new Rectangle(w,h);
            frame.setFill(Color.TRANSPARENT);
            frame.setStroke(Color.BLACK);
            frame.setStrokeWidth(1);
            // frame.setX(-w/2);

            this.getChildren().addAll(frame, slide);
            // this.setLayoutX(-w/2);
        }
    }
    public void bindHPbar(Rectangle r, Label HPlabel){
        r.widthProperty().bind(HPpoint);
        HPpoint.addListener(
            (t, oldV, newV)->{
                HPlabel.setText(HPpoint.get()+"/100");
                if(newV.intValue() == 0 && !endGame){
                    f.endGame(false);
                    endGame = true;
                }
        });
    }
    public void bindMag(Label mag_lb){
        gun.MagNum.addListener((t, oldV, newV)->{
            mag_lb.setText(newV+"/"+gun.bulletNum);
        });
    }
    public void bindKillNum(Label kill_lb){
        for(int i=0; i<eVector.size(); i++){
            eVector.get(i).killNum.addListener(
                (t, oldV, newV)->{
                    System.out.println(t + "kill num = " + newV);
                    kill_lb.setText(Integer.toString(Integer.parseInt(kill_lb.getText())+(newV.intValue()-oldV.intValue())));
                });
        }
    }
    public void bindMap(Group map_group, Rectangle map_rec){
        // GraphicsContext gc =  map_canvas.getGraphicsContext2D(); 
        double w = map_rec.getWidth()/world.COL;
        double h = map_rec.getHeight()/world.ROW;
        Vector<Double> infoX = new Vector<>();
        Vector<Double> infoZ = new Vector<>();
        for(int i=0; i<4;i++){
            infoX.add(0.);
            infoZ.add(0.);
        }
        Circle c = new Circle(4);
        c.setFill(Color.BLUE);
        c.setCenterX(world.getPosInMap_Row(pivot.getZ()) * w);
        c.setCenterY(world.getPosInMap_Col(pivot.getX()) * h);
        map_group.getChildren().add(c);
        
        pivot.xProperty().addListener((t, oldV, newV)->{
            int row = world.getPosInMap_Row(pivot.getZ());
            int col = world.getPosInMap_Col(pivot.getX());
            c.setCenterX(row*w);
            c.setCenterY(col*h);
        });
        pivot.zProperty().addListener((t, oldV, newV)->{
            int row = world.getPosInMap_Row(pivot.getZ());
            int col = world.getPosInMap_Col(pivot.getX());
            
            c.setCenterX(row*w);
            c.setCenterY(col*h);
        });

    }
    void HPChange(int change){
        if(HP>0){
            HP+=change;
            HPpoint.set(HP);
        }        
        // HPBar.slideProperty.set(HP/100.*HPBar.w);
        // System.out.println("player current HP = " + HP);
    }
    public void stopAll(){
        moveTimer.stop();
        timer.stop();
        mousePressTimer.stop();
    }
    public void pauseAll(){
        moveTimer.stop();
        timer.pause();
        mousePressTimer.pause();
    }
    public void resumeAll(){
        moveTimer.start();
        timer.play();
        mousePressTimer.play();
    }
    public void changeGun(){
        gun.gunInd.set((gun.gunInd.get()+1)%3);
    }
}