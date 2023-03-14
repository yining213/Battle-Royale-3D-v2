import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Mesh;
import javafx.stage.Stage;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.sql.Time;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;
import java.awt.MouseInfo;

import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.scene.Camera;
import javafx.scene.effect.Light.Point;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import org.w3c.dom.events.EventException;

import javafx.util.*;
import java.net.URL;
import java.util.Collections;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Animation.Status;
import javafx.event.ActionEvent;
import javafx.scene.shape.Rectangle;

public class Enemy extends Character implements Serializable {
    Translate pivot;
    AnimationTimer moveTimer;
    Timeline traceTimeline;
    Timeline detectPlayerTimeline;
    Timeline randomWalkingTimeline;
    RotateTimer RotTimer;
    boolean firstShot;
    Player player;
    Rotate worldRotY;
    DoubleProperty angleY;
    int HP;
    DoubleProperty HPpoint = new SimpleDoubleProperty();
    Bar HPBar;
    int power;
    World world;
    Group g;
    Scene scene;
    boolean isAttacked = false;
    boolean isBat = false;
    SimpleIntegerProperty killNum = new SimpleIntegerProperty();
    double vel = 10;

    public Enemy(SubScene subscene, World world, Player player, String file) {
        // public Enemy(SubScene subscene, World world, Object3D.objectEnum m) {
        super();
        if (this instanceof Bat) {
            isBat = true;
        }

        scene = subscene.getScene();
        URL u = getClass().getResource(file);
        body = loadModel(u);
        // body = new Group();
        // body.getChildren().addAll(Object3D.getModel(m));

        this.player = player;
        this.world = world;
        power = 1;
        g = (Group) subscene.getRoot();
        g.getChildren().add(this);
        // g.getChildren().add(body);
        pivot = new Translate();
        worldRotY = new Rotate(0, Rotate.Y_AXIS);
        angleY = new SimpleDoubleProperty(0);
        worldRotY.angleProperty().bind(angleY);
        body.getTransforms().addAll(
                pivot,
                worldRotY);
        setIniPos();
        HP = 100;
        HPpoint.set(100);
        killNum.set(0);
        HPBar = new Bar();
        HPBar.getTransforms().addAll(
                pivot,
                worldRotY,
                new Translate(-HPBar.w / 2., -35, 0));
        g.getChildren().add(HPBar);
        firstShot = true;

        // random walking
        int row, col;
        do {
            row = (int) (Math.random() * world.ROW);
            col = (int) (Math.random() * world.COL);
        } while (!world.isUnBlocked(row, col));
        double x = world.bound.getMinX() + col * world.len;
        double z = world.bound.getMinZ() + row * world.len;
        trace(new Point3D(x, 0, z));
        randomWalkingTimeline = new Timeline(
                new KeyFrame(Duration.millis(5000),
                        new EventHandler<ActionEvent>() {
                            int row, col;

                            @Override
                            public void handle(final ActionEvent e) {
                                do {
                                    row = (int) (Math.random() * world.ROW);
                                    col = (int) (Math.random() * world.COL);
                                } while (!world.isUnBlocked(row, col));
                                double x = world.bound.getMinX() + col * world.len;
                                double z = world.bound.getMinZ() + row * world.len;
                                trace(new Point3D(x, 0, z));
                            }
                        }));
        if(!Menu.timerMode){
            randomWalkingTimeline.setCycleCount(Timeline.INDEFINITE);
            randomWalkingTimeline.play();
        } 
        

        // initial traceTimeline
        traceTimeline = new Timeline(
                new KeyFrame(Duration.millis(1000),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(final ActionEvent e) {
                                trace(player.localToParent(0, 0, 0));
                                // System.out.println(i);
                            }
                        }));
        if(Menu.timerMode){
            vel = 20;
            traceTimeline.setCycleCount(Timeline.INDEFINITE);
            traceTimeline.play();
        }

        // initial detectPlayerTimeline
        detectPlayerTimeline = new Timeline(
                new KeyFrame(Duration.millis(100),
                        new EventHandler<ActionEvent>() {
                            boolean closeFlag = false;
                            @Override
                            public void handle(final ActionEvent e) {
                                if (body.getBoundsInParent().intersects(player.getBound()) && player.HP > 0) {
                                    System.out.println("enemy hits the player");
                                    player.HPChange(-power);
                                }
                                // player is close to enemy
                                else if (!isAttacked
                                        && body.localToParent(0, 0, 0).distance(player.localToParent(0, 0, 0)) <= 150) {
                                    vel = 30;
                                    if (Menu.timerMode && !closeFlag) {
                                        // timer mode
                                        System.out.println("player close to enemy");
                                        enemyTracing(player.localToParent(0,0,0));
                                        closeFlag = true;
                                        // randomWalkingTimeline.stop();
                                        // traceTimeline.setCycleCount(Timeline.INDEFINITE);
                                        // traceTimeline.play();
                                    } else if (!Menu.timerMode && traceTimeline.getStatus() == Status.STOPPED) {
                                        // survival mode
                                        System.out.println("player close to enemy");
                                        randomWalkingTimeline.pause();
                                        traceTimeline.setCycleCount(15);
                                        traceTimeline.play();
                                    }
                                } else if (!isAttacked) {
                                    vel = 10;
                                    if (!Menu.timerMode
                                            && randomWalkingTimeline.getStatus() == Status.PAUSED
                                            && traceTimeline.getStatus() == Status.STOPPED) {
                                        System.out.println("enemy continue random walk");
                                        randomWalkingTimeline.play();
                                    }
                                }
                            }
                        }));
        detectPlayerTimeline.setCycleCount(Timeline.INDEFINITE);
        detectPlayerTimeline.play();

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

    public void bindMap(Group map_group, Rectangle map_rec) {
        // GraphicsContext gc = map_canvas.getGraphicsContext2D();
        double w = map_rec.getWidth() / world.COL;
        double h = map_rec.getHeight() / world.ROW;
        Vector<Double> infoX = new Vector<>();
        Vector<Double> infoZ = new Vector<>();
        for (int i = 0; i < 4; i++) {
            infoX.add(0.);
            infoZ.add(0.);
        }
        Circle c = new Circle(4);
        c.setFill(Color.RED);
        map_group.getChildren().add(c);

        pivot.xProperty().addListener((t, oldV, newV) -> {
            int row = world.getPosInMap_Row(pivot.getZ());
            int col = world.getPosInMap_Col(pivot.getX());
            c.setCenterX(row * w);
            c.setCenterY(col * h);
        });
        pivot.zProperty().addListener((t, oldV, newV) -> {
            int row = world.getPosInMap_Row(pivot.getZ());
            int col = world.getPosInMap_Col(pivot.getX());

            c.setCenterX(row * w);
            c.setCenterY(col * h);
        });
        HPpoint.addListener(
            (t, oldV, newV)->{
                if(HPpoint.get() == 0){
                    map_group.getChildren().remove(c);
                }
        });
    }

 /*    public void getPlayer(Player p) {
        player = p;
        // timerMode: start to trace player when starting the game
        detectPlayerTimeline = new Timeline(
                new KeyFrame(Duration.millis(100),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(final ActionEvent e) {
                                if (body.getBoundsInParent().intersects(player.getBound()) && player.HP > 0) {
                                    // System.out.println("enemy hits the player");
                                    player.HPChange(-power);
                                }
                                // player is close to enemy
                                else if (body.localToParent(0, 0, 0).distance(player.localToParent(0, 0, 0)) <= 100) {
                                    // System.out.println("player is close to enemy");
                                    randomWalkingTimeline.stop();
                                    if (traceTimeline == null) {
                                        System.out.println("player is close to enemy");
                                        traceTimeline = new Timeline(
                                                new KeyFrame(Duration.millis(1000),
                                                        new EventHandler<ActionEvent>() {
                                                            @Override
                                                            public void handle(final ActionEvent e) {
                                                                trace(player.localToParent(0, 0, 0));
                                                                // System.out.println(i);
                                                            }
                                                        }));
                                        traceTimeline.setCycleCount(10);
                                        traceTimeline.play();
                                    } else if (traceTimeline.getStatus() == Status.STOPPED) {
                                        // System.out.println("continue trace");
                                        traceTimeline.setCycleCount(10);
                                        traceTimeline.play();
                                    }

                                } else if (traceTimeline != null
                                        && randomWalkingTimeline.getStatus() == Status.STOPPED
                                        && traceTimeline.getStatus() == Status.STOPPED) {
                                    System.out.println("enemy continue random walk");
                                    randomWalkingTimeline.play();
                                }
                            }
                        }));
        detectPlayerTimeline.setCycleCount(Timeline.INDEFINITE);
        detectPlayerTimeline.play();
    }
*/
    void enemyTracing(Point3D target) {
        // enemy start to trace player till die
        vel = 30;
        trace(target);
        // if (traceTimeline.getStatus() == Status.STOPPED) {
            traceTimeline.stop();
            traceTimeline.setCycleCount(Timeline.INDEFINITE);
            traceTimeline.play();
        // }
    }

    void trace(Point3D target) {
        Cell result = world.AStarSearch(body.localToParent(0, 0, 0), target);
        // world.printPath(result);
        Vector<Point3D>pathVec = new Vector<>();
        if (result == null)
            return;
        while (result.parent_row != -1 && result.parent_col != -1) {
            double nX = world.bound.getMinX() + result.col * world.len;
            double nZ = world.bound.getMinZ() + result.row * world.len;
            pathVec.add(new Point3D(nX, 0, nZ));
            result = world.cellDetails[result.parent_row][result.parent_col];
        }

        if (pathVec.isEmpty())
            return;
        else if (!pathVec.isEmpty()) {
            Collections.reverse(pathVec);
            pathVec.remove(0);
            if (pathVec.isEmpty())
                return;
            // restart timer
            if (firstShot) {
                firstShot = false;
            } else {
                if(moveTimer!=null)moveTimer.stop();
                if(RotTimer!=null)RotTimer.stop();
                // System.out.println("timer stop");
            }
            // System.out.println(new Point3D(pivot.getX(), pivot.getY(), pivot.getZ()));
            
            moveTimer = new AnimationTimer() {
                long previousTime = System.nanoTime();
                Point3D nextP = pathVec.get(0);
                int idx = 0;
                boolean firstRot = true;
                double heightNum = 0.3;

                @Override
                public void handle(long now) {
                    double elapsedTime = (now - previousTime) / 1000000000.0;
                    previousTime = now;
                    double scale = 0.015 * vel;
                    Point3D dir = new Point3D(nextP.getX() - pivot.getX(), 0,
                            nextP.getZ() - pivot.getZ()).normalize();

                    pivot.setX(pivot.getX() + scale * dir.getX());
                    pivot.setZ(pivot.getZ() + scale * dir.getZ());
                    if (isBat) {
                        // System.out.println(dir);
                        // System.out.println(pivot.getY());
                        pivot.setY(pivot.getY() + heightNum);
                        if (pivot.getY() >= 15 || pivot.getY() <= -10) {
                            heightNum = -heightNum;
                            if (pivot.getY() >= 15)
                                pivot.setY(15);
                            if (pivot.getY() <= -10)
                                pivot.setY(-10);
                        }
                    }
                    if (firstRot) {
                        RotTimer = new RotateTimer(dir);
                        RotTimer.start();
                        firstRot = false;
                    }
                    // System.out.println("d:"+nextP.distance(new Point3D(pivot.getX(),
                    // pivot.getY(), pivot.getZ())));
                    if (nextP.distance(new Point3D(pivot.getX(), 0, pivot.getZ())) < 5) {
                        if (idx + 1 == pathVec.size()) {
                            // System.out.println("arrive destination");
                            this.stop();
                            RotTimer.stop();
                            trace(player.localToParent(0, 0, 0));
                        }
                        // System.out.println("arrive point " + pivot.getX() + " " + pivot.getZ());
                        nextP = pathVec.get(idx);
                        dir = new Point3D(nextP.getX() - pivot.getX(), 0,
                                nextP.getZ() - pivot.getZ()).normalize();
                        idx++;
                        RotTimer.stop();
                        RotTimer = new RotateTimer(dir);
                        RotTimer.start();
                    }
                }
            };
            moveTimer.start();
            
        }

    }

    class RotateTimer extends AnimationTimer {
        double Rotvel = 200;
        long previousTime = System.nanoTime();
        int idx = 0;
        double angle;
        Point3D front_dir;
        double delta = 0;

        RotateTimer(final Point3D dir) {
            front_dir = body.localToParent(0, 0, 1).subtract(body.localToParent(0, 0, 0)).normalize();
            double dot = dir.dotProduct(front_dir);
            double dis = dir.distance(0, 0, 0) * front_dir.distance(0, 0, 0);
            // double cos = dot / dis;
            // double tan = (dir.getX()-front_dir.getX())/(dir.getZ()-front_dir.getZ());
            Point3D crs = dir.crossProduct(front_dir);
            angle = Math.toDegrees(Math.acos(dot / dis));
            // angle = Math.atan(tan)*(180/Math.PI);
            // System.out.println("rot init");
            if (crs.getY() > 0) {
                angle *= -1;
            }
            // if(tan>0 && cos>0){
            // angle*=-1;
            // }else if(tan<0 && cos<0){
            // angle*=-1;
            // }else if(tan>0 && cos<0){
            // }else if(tan<0 && cos>0){
            // }
            // System.out.println("angle = " + angle);
            if (angle < 0)
                Rotvel *= -1;
        }

        @Override
        public void handle(long now) {
            if (Math.abs(angle) < 10) {
                // System.out.println("don't need to rotate");
                this.stop();
            } else if (Math.abs(delta - angle) <= 5) {
                // System.out.println("stop rot");
                this.stop();
            } else {
                double elapsedTime = (now - previousTime) / 1000000000.0;
                previousTime = now;
                angleY.set(angleY.get() + Rotvel * elapsedTime);
                delta += Rotvel * elapsedTime;
                // System.out.println("delta:"+delta);
            }
        }
    };

    void getPosInMap(Integer row, Integer col) {
        row = (int) ((this.getTranslateZ() - world.bound.getMinZ()) / world.len);
        col = (int) ((this.getTranslateX() - world.bound.getMinX()) / world.len);
    }

    class Bar extends Group implements Serializable {
        Rectangle frame;
        Rectangle slide;
        int w = 20;
        int h = 2;
        DoubleProperty slideProperty = new SimpleDoubleProperty();

        Bar() {
            slide = new Rectangle(w, h);
            // slide.setX(w/2);
            slide.setFill(Color.RED);
            slide.widthProperty().bind(slideProperty);
            slideProperty.set(20);
            slideProperty.bind(HPpoint.divide(100.).multiply(w));

            frame = new Rectangle(w, h);
            frame.setFill(Color.TRANSPARENT);
            frame.setStroke(Color.BLACK);
            frame.setStrokeWidth(1);
            // frame.setX(-w/2);

            this.getChildren().addAll(frame, slide);
            // this.setLayoutX(-w/2);
        }
    }

    void HPChange(int change) {
        HP += change;
        HPpoint.set(HP);
        if (HP <= 0) {
            stopAll();
            g.getChildren().remove(this);
            g.getChildren().remove(HPBar);
            player.eVector.remove(this);
            killNum.set(killNum.get() + 1);
            System.out.println(this + " " + killNum.get());
        }
        // System.out.println("Enemy current HP = " + HP);
    }

    public void pauseAll() {
        moveTimer.stop();
        if (traceTimeline != null)
            traceTimeline.pause();
        RotTimer.stop();
        detectPlayerTimeline.pause();
        randomWalkingTimeline.pause();
    }

    public void stopAll() {
        moveTimer.stop();
        if (traceTimeline != null)
            traceTimeline.stop();
        RotTimer.stop();
        detectPlayerTimeline.stop();
        randomWalkingTimeline.stop();
    }

    public void resumeAll() {
        moveTimer.start();
        if (traceTimeline != null)
            traceTimeline.play();
        RotTimer.start();
        detectPlayerTimeline.play();
        randomWalkingTimeline.play();
    }
}
