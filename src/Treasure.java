import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Cylinder;
import javafx.stage.Stage;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import java.io.File;
import java.sql.Time;
import java.util.HashMap;
import java.awt.MouseInfo;
import java.beans.EventHandler;

import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.geometry.Bounds;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import org.w3c.dom.events.EventException;
import java.net.URL;

public class Treasure extends Group {
    protected Group body;
    World world;
    Scene scene;
    PointLight pointlight;
    Translate pivot;
    int bulletSupplement;
    int HPSupplement;

    public Treasure(SubScene subScene, World world) {
        super();
        scene = subScene.getScene();
        this.world = world;

        URL url = getClass().getResource("TreasureBox/tinker.obj");
        ObjModelImporter importer = new ObjModelImporter();
        importer.read(url);
        int count = 0;
        for (MeshView view : importer.getImport()) {
            this.getChildren().add(view);
            count++;
        }
        body = this;
        // body = loadModel(getClass().getResource("TreasureBox/tinker.obj"));

        pointlight = new PointLight(Color.CYAN);
        pointlight.setMaxRange(30);
        body.getChildren().add(pointlight);
        pivot = new Translate();
        body.getTransforms().addAll(
                pivot);
        // pointlight.getTransforms().addAll(
        //         pivot);
        setIniPos();

        bulletSupplement = (int) (Math.random() * 11);
        HPSupplement = (int) (Math.random() * 11);

        Group g = (Group) subScene.getRoot();
        g.getChildren().addAll(this);

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

    public Group loadModel(URL url) {
        ObjModelImporter importer = new ObjModelImporter();
        importer.read(url);

        for (MeshView view : importer.getImport()) {
            this.getChildren().add(view);
        }
        importer.close();
        return (Group) this;
    }

    public Bounds getBound() {
        return body.getBoundsInParent();
    }
}
