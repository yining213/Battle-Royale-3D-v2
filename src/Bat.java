import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.Cylinder;
import javafx.stage.Stage;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import java.io.File;
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

public class Bat extends Enemy {
    Timeline shootAnimation;

    public Bat(SubScene subscene, World world, Player player) {
        super(subscene, world, player, "bat/tinker.obj");
        // super(subscene, world, Object3D.objectEnum.BAT);
        body.getTransforms().add(new Translate(0, -20, 0));
    }

    public void attack() {
        // System.out.println("bat attack");
        Bullet bullet = new Bullet();
        bullet.power = 1;
        bullet.setColor(Color.AQUA);
        bullet.translateXProperty().set(body.localToParent(0, 0, 0).getX());
        bullet.translateYProperty().set(body.localToParent(0, 0, 0).getY());
        bullet.translateZProperty().set(body.localToParent(0, 0, 0).getZ());
        g.getChildren().add(bullet);
        bullet.timeline = new Timeline(
                new KeyFrame(Duration.millis(10),
                        new EventHandler<ActionEvent>() {
                            Point3D dir = player.localToParent(0, 0, 0).add(0, -20, 0)
                                    .subtract(body.localToParent(0, 0, 0)).normalize();
                            double velocity = 5;
                            Bullet b = bullet;

                            @Override
                            public void handle(final ActionEvent e) {
                                b.translateXProperty().set(b.getTranslateX() + dir.getX() * velocity);
                                b.translateYProperty().set(b.getTranslateY() + dir.getY() * velocity);
                                b.translateZProperty().set(b.getTranslateZ() + dir.getZ() * velocity);
                                // shoot on player
                                if (b.getBoundsInParent().intersects(player.getBoundsInParent())) {
                                    System.out.println("bat shoot on player");
                                    player.HPChange(-power);
                                    if (b.timeline != null) {
                                        b.timeline.stop();
                                        b.timeline.getKeyFrames().clear();
                                        g.getChildren().remove(b);
                                    }
                                }
                                if (world.hitWall3(b.getTranslateX(), b.getTranslateZ()) == true) {
                                    // System.out.println("bat shoot on wall");
                                    if (b.timeline != null) {
                                        b.timeline.stop();
                                        b.timeline.getKeyFrames().clear();
                                        g.getChildren().remove(b);
                                        b.setVisible(false);
                                    }
                                }
                            }
                        }));
        bullet.timeline.setCycleCount(Timeline.INDEFINITE);
        bullet.timeline.play();
    }

    void enemyTracing(Point3D target) {
        // enemy start to trace player till die
        System.out.println("bat is attacked");
        trace(target);
        vel = 20;
        // if (traceTimeline.getStatus() == Status.RUNNING) {
            traceTimeline.stop();
            traceTimeline.getKeyFrames().clear();
            traceTimeline = new Timeline(
                    new KeyFrame(Duration.millis(1000),
                            new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(final ActionEvent e) {
                                    trace(player.localToParent(0, 0, 0));
                                    attack(); // start to attack player
                                }
                            }));
            traceTimeline.setCycleCount(Timeline.INDEFINITE);
            traceTimeline.play();
        // }
    }

}
