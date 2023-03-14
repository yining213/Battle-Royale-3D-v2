import java.util.Vector;
import java.util.Vector.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.security.SecureRandom;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Shape;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Box;
import javafx.util.Duration;
import javafx.scene.text.Text;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.effect.Light.Point;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Point3D;
import javafx.scene.transform.Translate;
import javafx.scene.Parent;
import javafx.scene.Group;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.paint.Color;
import javafx.scene.Node;
import javafx.scene.input.PickResult;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

public class Gun extends Weapon {
    int bulletNum;
    Vector<Bullet> bulletVec;
    Color bullColor;
    int shootingCenterX;
    int shootingCenterY;
    int curBullet;
    Group group;
    Player owner;
    private PerspectiveCamera camera;
    World world;

    boolean firstTracing;
    Circle c1, c2;
    IntegerProperty MagNum = new SimpleIntegerProperty();
    ImageView[] gunImages = new ImageView[3];
    IntegerProperty gunInd = new SimpleIntegerProperty();

    public Gun(Parent parent, PerspectiveCamera c, Player p) {
        owner = p;
        camera = c;

        gunInd.set(0);
        gunInd.addListener((t, oldV, newV) -> {
            if (newV.intValue() == 0) {
                if (Menu.timerMode)
                    owner.f.leftbar_timeController.gun_type_lb.setGraphic(gunImages[newV.intValue()]);
                else
                    owner.f.leftbar_surController.gun_type_lb.setGraphic(gunImages[newV.intValue()]);
                owner.mousePressTimer.stop();
            } else if (newV.intValue() == 1) {
                if (Menu.timerMode)
                    owner.f.leftbar_timeController.gun_type_lb.setGraphic(gunImages[newV.intValue()]);
                else
                    owner.f.leftbar_surController.gun_type_lb.setGraphic(gunImages[newV.intValue()]);
                owner.mousePressTimer.play();
            } else if (newV.intValue() == 2) {
                if (Menu.timerMode)
                    owner.f.leftbar_timeController.gun_type_lb.setGraphic(gunImages[newV.intValue()]);
                else
                    owner.f.leftbar_surController.gun_type_lb.setGraphic(gunImages[newV.intValue()]);
                owner.mousePressTimer.stop();
            }
        });

        bulletNum = 50;
        MagNum.set(bulletNum);
        bulletVec = new Vector<Bullet>();
        for (int i = 0; i < bulletNum; i++)
            bulletVec.add(new Bullet());
        // curBullet = 0;
        group = (Group) parent;
        world = p.world;
        firstTracing = true;
        bullColor = Color.FLORALWHITE;

        gunImages[0] = new ImageView(new Image(getClass().getResourceAsStream("images/pistol.png")));
        gunImages[0].setFitHeight(50);
        gunImages[0].setFitWidth(50);
        gunImages[1] = new ImageView(new Image(getClass().getResourceAsStream("images/machine_gun.png")));
        gunImages[1].setFitHeight(50);
        gunImages[1].setFitWidth(50);
        gunImages[2] = new ImageView(new Image(getClass().getResourceAsStream("images/water_gun.png")));
        gunImages[2].setFitHeight(50);
        gunImages[2].setFitWidth(50);
        if (Menu.timerMode)
            owner.f.leftbar_timeController.gun_type_lb.setGraphic(gunImages[0]);
        else
            owner.f.leftbar_surController.gun_type_lb.setGraphic(gunImages[0]);
    }

    public void addGunSight(Pane p, SubScene subscene) {
        c1 = new Circle(30);
        c2 = new Circle(5);
        SimpleDoubleProperty width = new SimpleDoubleProperty();
        SimpleDoubleProperty height = new SimpleDoubleProperty();
        width.bind(subscene.widthProperty());
        height.bind(subscene.heightProperty());
        c1.centerXProperty().bind(width.divide(2));
        c1.centerYProperty().bind(height.divide(2));
        c2.centerXProperty().bind(width.divide(2));
        c2.centerYProperty().bind(height.divide(2));

        p.getChildren().add(c1);
        p.getChildren().add(c2);
        // c1.setCenterX(width.get()/2);
        // c1.setCenterY(height.get()/2);
        // c2.setCenterX(width.get()/2);
        // c2.setCenterY(height.get()/2);

        c1.setFill(null);
        c1.setStroke(Color.RED);
        c2.setFill(Color.RED);
    }

    public void enableGunsight(boolean b) {
        c1.setVisible(b);
        c2.setVisible(b);
    }

    private Group loadModel(URL url) {
        Group modelRoot = new Group();

        ObjModelImporter importer = new ObjModelImporter();
        importer.read(url);

        for (MeshView view : importer.getImport()) {
            modelRoot.getChildren().add(view);
        }
        return modelRoot;
    }

    public void attack() {
        if (!bulletVec.isEmpty()) {
            // System.out.println("shoot");
            // System.out.println(bulletVec.size());
            Bullet bullet = bulletVec.get(bulletVec.size() - 1);
            bulletVec.remove(bullet);
            switch (gunInd.get()) {
                case 0:
                    bullet.setColor(Color.ANTIQUEWHITE);
                    bullet.vel = 7;
                    bullet.power = 1;
                    break;
                case 1:
                    bullet.setColor(Color.CORAL);
                    bullet.vel = 5;
                    bullet.power = 5;
                    break;
                case 2:
                    bullet.setDiffuseMap("images/water.png");
                    bullet.vel = 0.1;
                    bullet.power = 15;
                    break;
            }
            group.getChildren().add(bullet);
            Point3D pos = camera.localToParent(0, 0, 0);
            bullet.translateXProperty().set(pos.getX());
            bullet.translateYProperty().set(pos.getY());
            bullet.translateZProperty().set(pos.getZ());

            bullet.timeline = new Timeline(
                    new KeyFrame(Duration.millis(5),
                            new EventHandler<ActionEvent>() {
                                // Point3D dir = camera.localToParent(0,0,0).multiply(-1).normalize();
                                Point3D dir = camera.localToParent(0, 0, 1).subtract(camera.localToParent(0, 0, 0))
                                        .normalize();
                                Bullet b = bullet;

                                @Override
                                public void handle(final ActionEvent e) {

                                    // System.out.println("run timer");
                                    // System.out.println(dir.toString());
                                    b.translateXProperty().set(b.getTranslateX() + dir.getX() * b.vel);
                                    b.translateYProperty().set(b.getTranslateY() + dir.getY() * b.vel);
                                    b.translateZProperty().set(b.getTranslateZ() + dir.getZ() * b.vel);
                                    // System.out.print(b.getTranslateX());
                                    // System.out.print(" "+b.getTranslateY());
                                    // System.out.println(" "+b.getTranslateZ());

                                    if (checkBounds(b)) {
                                        // b.setVisible(false);
                                        group.getChildren().remove(b);
                                    }
                                    // System.out.println(group.getChildren().size());
                                    // System.out.println("bulletVec.size = " + bulletVec.size());
                                }
                            }));

            bullet.timeline.setCycleCount(Timeline.INDEFINITE);
            // bulletAnimation.setCycleCount(50);
            bullet.timeline.play();
            // curBullet++;
            MagNum.set(bulletVec.size());
        }
        // else {
        // refillBullet();
        // }
    }

    private synchronized boolean checkBounds(Bullet bullet) {
        // for(int i=0;i<group.getChildren().size();i++){
        // Node static_bloc = group.getChildren().get(i);
        // if(static_bloc instanceof Enemy) {
        // Enemy e = (Enemy) static_bloc;
        // if (bullet.getBoundsInParent().intersects(e.getBound())) {
        // //System.out.println(static_bloc.getBoundsInParent());
        // System.out.println("hit enemy");
        // e.HPChange(-bullet.power);
        // if(e.firstTracing){
        // e.enemyTracing(owner.localToParent(0,0,0));
        // }
        // return true;
        // }
        // }
        // }
        for (Enemy e : owner.eVector) {
            if (bullet.getBoundsInParent().intersects(e.getBound())) {
                // System.out.println(static_bloc.getBoundsInParent());
                System.out.println("hit enemy");
                if (bullet.timeline != null){
                    bullet.timeline.stop();
                    bullet.timeline.getKeyFrames().clear();
                }
                // if(Home.musicOn) Camera3D.music_src.effect.get("Hit_Enemy").play();
                if (!e.isAttacked) {
                    e.isAttacked = true;
                    if (e.isBat) {
                        Bat b = (Bat) e;
                        b.enemyTracing(owner.localToParent(0, 0, 0));
                    } else
                        e.enemyTracing(owner.localToParent(0, 0, 0));
                }
                e.HPChange(-bullet.power);
                return true;
            }
        }
        if (world.hitWall3(bullet.getTranslateX(), bullet.getTranslateZ()) == true) {
            // System.out.println("bullet hit wall");
            return true;
        }
        return false;
    }

    void BulletSupplement(int n) {
        int len = bulletVec.size();
        for (int i = len; i < len + n && i < bulletNum; i++)
            bulletVec.add(new Bullet());
        MagNum.set(bulletVec.size());
        if (n > 0 && Home.musicOn)
            Camera3D.music_src.effect.get("Gun_Sup").play();
    }

    void refillBullet() {
        int len = bulletVec.size();
        for (int i = len; i < bulletNum; i++)
            bulletVec.add(new Bullet());
    }
}
