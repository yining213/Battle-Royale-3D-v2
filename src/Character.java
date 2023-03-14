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


abstract public class Character extends Group{
    double speed;
    int healthP;
    boolean status;
    protected Group body;
    
    abstract void HPChange(int change);
    public Group loadModel(URL url) {
        //Group modelRoot = new Group();
        try{
            ObjModelImporter importer = new ObjModelImporter();
            importer.read(url);

            for (MeshView view : importer.getImport()) {
                this.getChildren().add(view);
            }
            importer.close();
        }catch(Exception e){
            System.out.println("error: " + e);
        }
        
        return (Group)this;
    }
    public Bounds getBound(){
        return body.getBoundsInParent();
    }  
}
