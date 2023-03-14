import javafx.scene.shape.Shape3D;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.MeshView;
import javafx.stage.Stage;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.image.Image;

import java.io.File;
import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;


public class Bullet extends Group{
    double vel;
    int status;
    Sphere sphere;
    int power;
    Timeline timeline;

    public Bullet(){
        //super(5);
        
        // ObjModelImporter importer = new ObjModelImporter();
        // try{
        //     //File file = new File("src/Classic(3D).obj");
        //     File file = new File("src/bullet/tinker.obj");
        //     importer.read(file);
        // }
        // catch (ImportException e) {
        //     e.printStackTrace();
        //     return;
        // }
        // MeshView[] meshViews = importer.getImport();
        // importer.close();
        // this.getChildren().addAll(meshViews);
        sphere = new Sphere(0.8);
        this.getChildren().add(sphere);
        power = 5;
        vel = 5;
    }
    public void setRadius(double r){
        sphere.setRadius(r);
    }
    public void setColor(Color c){
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(c);
        sphere.setMaterial(material);
    }
    public void setDiffuseMap(String path){
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(new Image(getClass().getResource(path).toExternalForm()));
        sphere.setDrawMode(DrawMode.FILL);
        sphere.setMaterial(material);
    }
    Sphere getSphere(){
        return sphere;
    }
    // boolean collideWithWall(){

    // }

    void vanish(){

    }
}
