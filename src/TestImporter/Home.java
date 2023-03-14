package TestImporter;

import java.io.File;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class Home extends Application{
    @Override
    public void start(Stage stage){
        Scene scene = new Scene(new Group(), 500, 500);
        Group group = (Group)scene.getRoot();
        Sphere s = new Sphere(20);
        s.setTranslateX(scene.getWidth()/2);
        s.setTranslateY(scene.getHeight()/2);
        // group.getChildren().add(s);

        try{
            ObjModelImporter importer = new ObjModelImporter();
            File file = new File("../Mini_person/tinker.obj");
            importer.read(file);
            group.getChildren().addAll(importer.getImport());
            importer.close();
        }
        catch(ImportException e){
            e.printStackTrace();
            return;
        }
        
        PerspectiveCamera camera = new PerspectiveCamera();
        camera.getTransforms().addAll(
            new Translate(0,-20,0),
            new Rotate(0, Rotate.X_AXIS),
            new Rotate(0, Rotate.Y_AXIS)

        );
        scene.setCamera(camera);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
